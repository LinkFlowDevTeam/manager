/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
 * Copyright 2014-2016 the libsecp256k1 contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test32.common.wrapper.bittrade.crypto.core;

import com.test32.common.wrapper.bittrade.crypto.core.crypto.*;
import com.test32.common.wrapper.google.common.annotations.VisibleForTesting;
import com.test32.common.wrapper.google.common.base.MoreObjects;
import com.test32.common.wrapper.google.common.base.Objects;
import com.test32.common.wrapper.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.asn1.x9.X9IntegerConverter;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.ec.CustomNamedCurves;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.signers.ECDSASigner;
import org.spongycastle.crypto.signers.HMacDSAKCalculator;
import org.spongycastle.math.ec.ECAlgorithms;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.math.ec.FixedPointCombMultiplier;
import org.spongycastle.math.ec.FixedPointUtil;
import org.spongycastle.math.ec.custom.sec.SecP256K1Curve;
import org.spongycastle.util.encoders.Base64;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;

import static com.test32.common.wrapper.google.common.base.Preconditions.*;

// TODO: Move this class to tracking compression state itself.
// The Bouncy Castle developers are deprecating their own tracking of the compression state.

/**
 * <p>
 * Represents an elliptic curve public and (optionally) private key, usable for
 * digital signatures but not encryption. Creating a new ECKey with the empty
 * constructor will generate a new random keypair. Other static methods can be
 * used when you already have the public or private parts. If you create a key
 * with only the public part, you can check signatures but not create them.
 * </p>
 *
 * <p>
 * ECKey also provides access to Bitcoin Core compatible text message signing,
 * as accessible via the UI or JSON-RPC. This is slightly different to signing
 * raw bytes - if you want to sign your own data and it won't be exposed as text
 * to people, you don't want to use this. If in doubt, ask on the mailing list.
 * </p>
 *
 * <p>
 * The ECDSA algorithm supports <i>key recovery</i> in which a signature plus a
 * couple of discriminator bits can be reversed to find the public key used to
 * calculate it. This can be convenient when you have a message and a signature
 * and want to find out who signed it, rather than requiring the user to provide
 * the expected identity.
 * </p>
 *
 * <p>
 * This class supports a variety of serialization forms. The methods that
 * accept/return byte arrays serialize private keys as raw byte arrays and
 * public keys using the SEC standard byte encoding for public keys. Signatures
 * are encoded using ASN.1/DER inside the Bitcoin protocol.
 * </p>
 *
 * <p>
 * A key can be <i>compressed</i> or <i>uncompressed</i>. This refers to whether
 * the public key is represented when encoded into bytes as an (x, y) coordinate
 * on the elliptic curve, or whether it's represented as just an X co-ordinate
 * and an extra byte that carries a sign bit. With the latter form the Y
 * coordinate can be calculated dynamically, however, <b>because the binary
 * serialization is different the address of a key changes if its compression
 * status is changed</b>. If you deviate from the defaults it's important to
 * understand this: money sent to a compressed version of the key will have a
 * different address to the same key in uncompressed form. Whether a public key
 * is compressed or not is recorded in the SEC binary serialisation format, and
 * preserved in a flag in this class so round-tripping preserves state. Unless
 * you're working with old software or doing unusual things, you can usually
 * ignore the compressed/uncompressed distinction.
 * </p>
 */
public class ECKey {
    private static final Logger log = LoggerFactory.getLogger(ECKey.class);

    /**
     * Compares pub key bytes using
     * {@link com.test32.common.wrapper.google.common.primitives.UnsignedBytes#lexicographicalComparator()}
     */

    // The parameters of the secp256k1 curve that Bitcoin uses.
    private static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");

    /** The parameters of the secp256k1 curve that Bitcoin uses. */
    public static final ECDomainParameters CURVE;

    /**
     * Equal to CURVE.getN().shiftRight(1), used for canonicalising the S value
     * of a signature. If you aren't sure what this is about, you can ignore it.
     */
    public static final BigInteger HALF_CURVE_ORDER;

    private static final SecureRandom secureRandom;

    static {
        // Init proper random number generator, as some old Android
        // installations have bugs that make it unsecure.
        if (CryptoUtils.isAndroidRuntime())
            new LinuxSecureRandom();

        // Tell Bouncy Castle to precompute data that's needed during secp256k1
        // calculations. Increasing the width
        // number makes calculations faster, but at a cost of extra memory usage
        // and with decreasing returns. 12 was
        // picked after consulting with the BC team.
        FixedPointUtil.precompute(CURVE_PARAMS.getG(), 12);
        CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(),
                CURVE_PARAMS.getH());
        HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);
        secureRandom = new SecureRandom();
    }

    // The two parts of the key. If "priv" is set, "pub" can always be
    // calculated. If "pub" is set but not "priv", we
    // can only verify signatures not make them.
    protected final BigInteger priv; // A field element.
    protected final LazyECPoint pub;

    // Creation time of the key in seconds since the epoch, or zero if the key
    // was deserialized from a version that did
    // not have this field.
    protected long creationTimeSeconds;

    protected KeyCrypter keyCrypter;
    protected EncryptedData encryptedPrivateKey;


    protected ECKey(@Nullable BigInteger priv, ECPoint pub) {
        this(priv, new LazyECPoint(checkNotNull(pub)));
    }

    protected ECKey(@Nullable BigInteger priv, LazyECPoint pub) {
        if (priv != null) {
            checkArgument(priv.bitLength() <= 32 * 8, "private key exceeds 32 bytes: %s bits", priv.bitLength());
            // Try and catch buggy callers or bad key imports, etc. Zero and one
            // are special because these are often
            // used as sentinel values and because scripting languages have a
            // habit of auto-casting true and false to
            // 1 and 0 or vice-versa. Type confusion bugs could therefore result
            // in private keys with these values.
            checkArgument(!priv.equals(BigInteger.ZERO));
            checkArgument(!priv.equals(BigInteger.ONE));
        }
        this.priv = priv;
        this.pub = checkNotNull(pub);
    }

    /**
     * Utility for decompressing an elliptic curve point. Returns the same point
     * if it's already compressed. See the ECKey class docs for a discussion of
     * point compression.
     */
    public static ECPoint decompressPoint(ECPoint point) {
        return getPointWithCompression(point, false);
    }

    private static ECPoint getPointWithCompression(ECPoint point, boolean compressed) {
        if (point.isCompressed() == compressed)
            return point;
        point = point.normalize();
        BigInteger x = point.getAffineXCoord().toBigInteger();
        BigInteger y = point.getAffineYCoord().toBigInteger();
        return CURVE.getCurve().createPoint(x, y, compressed);
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated
     * from it (this is slow). The resulting public key is compressed.
     */
    public static ECKey fromPrivate(BigInteger privKey) {
        return fromPrivate(privKey, true);
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated
     * from it (this is slow), either compressed or not.
     */
    public static ECKey fromPrivate(BigInteger privKey, boolean compressed) {
        ECPoint point = publicPointFromPrivate(privKey);
        return new ECKey(privKey, getPointWithCompression(point, compressed));
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated
     * from it (this is slow). The resulting public key is compressed.
     */
    public static ECKey fromPrivate(byte[] privKeyBytes) {
        return fromPrivate(new BigInteger(1, privKeyBytes));
    }

    /**
     * Creates an ECKey given the private key only. The public key is calculated
     * from it (this is slow), either compressed or not.
     */
    public static ECKey fromPrivate(byte[] privKeyBytes, boolean compressed) {
        return fromPrivate(new BigInteger(1, privKeyBytes), compressed);
    }

    /**
     * Creates an ECKey that cannot be used for signing, only verifying
     * signatures, from the given point. The compression state of pub will be
     * preserved.
     */
    public static ECKey fromPublicOnly(ECPoint pub) {
        return new ECKey(null, pub);
    }


    /**
     * Utility for compressing an elliptic curve point. Returns the same point
     * if it's already compressed. See the ECKey class docs for a discussion of
     * point compression.
     */
    public static ECPoint compressPoint(ECPoint point) {
        return getPointWithCompression(point, true);
    }


    /**
     * Creates an ECKey that cannot be used for signing, only verifying
     * signatures, from the given encoded point. The compression state of pub
     * will be preserved.
     */
    public static ECKey fromPublicOnly(byte[] pub) {
        return new ECKey(null, CURVE.getCurve().decodePoint(pub));
    }

    /**
     * Returns a copy of this key, but with the public point represented in
     * uncompressed form. Normally you would never need this: it's for
     * specialised scenarios or when backwards compatibility in encoded form is
     * necessary.
     */
    public ECKey decompress() {
        if (!pub.isCompressed())
            return this;
        else
            return new ECKey(priv, decompressPoint(pub.get()));
    }

    /**
     * Creates an ECKey given only the private key bytes. This is the same as
     * using the BigInteger constructor, but is more convenient if you are
     * importing a key from elsewhere. The public key will be automatically
     * derived from the private key.
     */
    @Deprecated
    public ECKey(@Nullable byte[] privKeyBytes, @Nullable byte[] pubKey) {
        this(privKeyBytes == null ? null : new BigInteger(1, privKeyBytes), pubKey);
    }

    /**
     * Create a new ECKey with an encrypted private key, a public key and a
     * KeyCrypter.
     *
     * @param encryptedPrivateKey
     *            The private key, encrypted,
     * @param pubKey
     *            The keys public key
     * @param keyCrypter
     *            The KeyCrypter that will be used, with an AES key, to encrypt
     *            and decrypt the private key
     */
    @Deprecated
    public ECKey(EncryptedData encryptedPrivateKey, byte[] pubKey, KeyCrypter keyCrypter) {
        this((byte[]) null, pubKey);

        this.keyCrypter = checkNotNull(keyCrypter);
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    /**
     * Constructs a key that has an encrypted private component. The given
     * object wraps encrypted bytes and an initialization vector. Note that the
     * key will not be decrypted during this call: the returned ECKey is
     * unusable for signing unless a decryption key is supplied.
     */
    public static ECKey fromEncrypted(EncryptedData encryptedPrivateKey, KeyCrypter crypter, byte[] pubKey) {
        ECKey key = fromPublicOnly(pubKey);
        key.encryptedPrivateKey = checkNotNull(encryptedPrivateKey);
        key.keyCrypter = checkNotNull(crypter);
        return key;
    }

    /**
     * Creates an ECKey given either the private key only, the public key only,
     * or both. If only the private key is supplied, the public key will be
     * calculated from it (this is slow). If both are supplied, it's assumed the
     * public key already correctly matches the private key. If only the public
     * key is supplied, this ECKey cannot be used for signing.
     * 
     * @param compressed
     *            If set to true and pubKey is null, the derived public key will
     *            be in compressed form.
     */
    @Deprecated
    public ECKey(@Nullable BigInteger privKey, @Nullable byte[] pubKey, boolean compressed) {
        if (privKey == null && pubKey == null)
            throw new IllegalArgumentException("ECKey requires at least private or public key");
        this.priv = privKey;
        if (pubKey == null) {
            // Derive public from private.
            ECPoint point = publicPointFromPrivate(privKey);
            point = getPointWithCompression(point, compressed);
            this.pub = new LazyECPoint(point);
        } else {
            // We expect the pubkey to be in regular encoded form, just as a
            // BigInteger. Therefore the first byte is
            // a special marker byte.
            // TODO: This is probably not a useful API and may be confusing.
            this.pub = new LazyECPoint(CURVE.getCurve(), pubKey);
        }
    }

    /**
     * Creates an ECKey given either the private key only, the public key only,
     * or both. If only the private key is supplied, the public key will be
     * calculated from it (this is slow). If both are supplied, it's assumed the
     * public key already correctly matches the public key. If only the public
     * key is supplied, this ECKey cannot be used for signing.
     */
    @Deprecated
    private ECKey(@Nullable BigInteger privKey, @Nullable byte[] pubKey) {
        this(privKey, pubKey, false);
    }

    /**
     * Returns true if this key doesn't have unencrypted access to private key
     * bytes. This may be because it was never given any private key bytes to
     * begin with (a watching key), or because the key is encrypted. You can use
     * {@link #isEncrypted()} to tell the cases apart.
     */
    public boolean isPubKeyOnly() {
        return priv == null;
    }

    /**
     * Returns public key point from the given private key. To convert a byte
     * array into a BigInteger, use <tt>
     * new BigInteger(1, bytes);</tt>
     */
    public static ECPoint publicPointFromPrivate(BigInteger privKey) {
        /*
         * TODO: FixedPointCombMultiplier currently doesn't support scalars
         * longer than the group order, but that could change in future
         * versions.
         */
        if (privKey.bitLength() > CURVE.getN().bitLength()) {
            privKey = privKey.mod(CURVE.getN());
        }
        return new FixedPointCombMultiplier().multiply(CURVE.getG(), privKey);
    }
    /**
     * Gets the raw public key value. This appears in transaction scriptSigs.
     * Note that this is <b>not</b> the same as the pubKeyHash/address.
     */
    public byte[] getPubKey() {
        return pub.getEncoded();
    }

    /**
     * Gets the public key in the form of an elliptic curve point object from
     * Bouncy Castle.
     */
    public ECPoint getPubKeyPoint() {
        return pub.get();
    }

    /**
     * Gets the private key in the form of an integer field element. The public
     * key is derived by performing EC point addition this number of times (i.e.
     * point multiplying).
     *
     * @throws IllegalStateException
     *             if the private key bytes are not available.
     */
    public BigInteger getPrivKey() {
        if (priv == null)
            throw new MissingPrivateKeyException();
        return priv;
    }

    /**
     * Returns whether this key is using the compressed form or not. Compressed
     * pubkeys are only 33 bytes, not 64.
     */
    public boolean isCompressed() {
        return pub.isCompressed();
    }


    /**
     * Groups the two components that make up a signature, and provides a way to
     * encode to DER form, which is how ECDSA signatures are represented when
     * embedded in other data structures in the Bitcoin protocol. The raw
     * components can be useful for doing further EC maths on them.
     */
    public static class ECDSASignature {
        /** The two components of the signature. */
        public final BigInteger r, s;

        /**
         * Constructs a signature with the given components. Does NOT
         * automatically canonicalise the signature.
         */
        public ECDSASignature(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
        }

        /**
         * Returns true if the S component is "low", that means it is below
         * {@link ECKey#HALF_CURVE_ORDER}. See <a href=
         * "https://github.com/bitcoin/bips/blob/master/bip-0062.mediawiki#Low_S_values_in_signatures">BIP62</a>.
         */
        public boolean isCanonical() {
            return s.compareTo(HALF_CURVE_ORDER) <= 0;
        }

        /**
         * Will automatically adjust the S component to be less than or equal to
         * half the curve order, if necessary. This is required because for
         * every signature (r,s) the signature (r, -s (mod N)) is a valid
         * signature of the same message. However, we dislike the ability to
         * modify the bits of a Bitcoin transaction after it's been signed, as
         * that violates various assumed invariants. Thus in future only one of
         * those forms will be considered legal and the other will be banned.
         */
        public ECDSASignature toCanonicalised() {
            if (!isCanonical()) {
                // The order of the curve is the number of valid points that
                // exist on that curve. If S is in the upper
                // half of the number of valid points, then bring it back to the
                // lower half. Otherwise, imagine that
                // N = 10
                // s = 8, so (-8 % 10 == 2) thus both (r, 8) and (r, 2) are
                // valid solutions.
                // 10 - 8 == 2, giving us always the latter solution, which is
                // canonical.
                return new ECDSASignature(r, CURVE.getN().subtract(s));
            } else {
                return this;
            }
        }

        /**
         * DER is an international standard for serializing data structures
         * which is widely used in cryptography. It's somewhat like protocol
         * buffers but less convenient. This method returns a standard DER
         * encoding of the signature, as recognized by OpenSSL and other
         * libraries.
         */
        public byte[] encodeToDER() {
            try {
                return derByteStream().toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e); // Cannot happen.
            }
        }

        public static ECDSASignature decodeFromDER(byte[] bytes) throws IllegalArgumentException {
            ASN1InputStream decoder = null;
            try {
                decoder = new ASN1InputStream(bytes);
                final ASN1Primitive seqObj = decoder.readObject();
                if (seqObj == null)
                    throw new IllegalArgumentException("Reached past end of ASN.1 stream.");
                if (!(seqObj instanceof DLSequence))
                    throw new IllegalArgumentException("Read unexpected class: " + seqObj.getClass().getName());
                final DLSequence seq = (DLSequence) seqObj;
                ASN1Integer r, s;
                try {
                    r = (ASN1Integer) seq.getObjectAt(0);
                    s = (ASN1Integer) seq.getObjectAt(1);
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException(e);
                }
                // OpenSSL deviates from the DER spec by interpreting these
                // values as unsigned, though they should not be
                // Thus, we always use the positive versions. See:
                // http://r6.ca/blog/20111119T211504Z.html
                return new ECDSASignature(r.getPositiveValue(), s.getPositiveValue());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            } finally {
                if (decoder != null)
                    try {
                        decoder.close();
                    } catch (IOException x) {
                    }
            }
        }

        protected ByteArrayOutputStream derByteStream() throws IOException {
            // Usually 70-72 bytes.
            ByteArrayOutputStream bos = new ByteArrayOutputStream(72);
            DERSequenceGenerator seq = new DERSequenceGenerator(bos);
            seq.addObject(new ASN1Integer(r));
            seq.addObject(new ASN1Integer(s));
            seq.close();
            return bos;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            ECDSASignature other = (ECDSASignature) o;
            return r.equals(other.r) && s.equals(other.s);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(r, s);
        }
    }

    public ECDSASignature sign(Sha256Hash input) throws KeyCrypterException {
        return sign(input, null);
    }

    /**
     * If this global variable is set to true, sign() creates a dummy signature
     * and verify() always returns true. This is intended to help accelerate
     * unit tests that do a lot of signing/verifying, which in the debugger can
     * be painfully slow.
     */
    @VisibleForTesting
    public static boolean FAKE_SIGNATURES = false;

    /**
     * Signs the given hash and returns the R and S components as BigIntegers.
     * In the Bitcoin protocol, they are usually encoded using DER format, so
     * you want
     * {@link ECDSASignature#encodeToDER()}
     * instead. However sometimes the independent components can be useful, for
     * instance, if you're doing to do further EC maths on them.
     *
     * @param aesKey
     *            The AES key to use for decryption of the private key. If null
     *            then no decryption is required.
     * @throws KeyCrypterException
     *             if there's something wrong with aesKey.
     * @throws MissingPrivateKeyException
     *             if this key cannot sign because it's pubkey only.
     */
    public ECDSASignature sign(Sha256Hash input, @Nullable KeyParameter aesKey) throws KeyCrypterException {
        KeyCrypter crypter = getKeyCrypter();
        if (crypter != null) {
            if (aesKey == null)
                throw new KeyIsEncryptedException();
            return decrypt(aesKey).sign(input);
        } else {
            // No decryption of private key required.
            if (priv == null)
                throw new MissingPrivateKeyException();
        }
        return doSign(input, priv);
    }

    protected ECDSASignature doSign(Sha256Hash input, BigInteger privateKeyForSigning) {
//        if (Secp256k1Context.isEnabled()) {
//            try {
//                byte[] signature = NativeSecp256k1.sign(input.getBytes(),
//                        CryptoUtils.bigIntegerToBytes(privateKeyForSigning, 32));
//                return ECDSASignature.decodeFromDER(signature);
//            } catch (NativeSecp256k1Util.AssertFailException e) {
//                log.error("Caught AssertFailException inside secp256k1", e);
//                throw new RuntimeException(e);
//            }
//        }
        if (FAKE_SIGNATURES)
            // return TransactionSignature.dummy();
            checkNotNull(privateKeyForSigning);
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(privateKeyForSigning, CURVE);
        signer.init(true, privKey);
        BigInteger[] components = signer.generateSignature(input.getBytes());
        return new ECDSASignature(components[0], components[1]).toCanonicalised();
    }

    /**
     * <p>
     * Verifies the given ECDSA signature against the message bytes using the
     * public key bytes.
     * </p>
     * 
     * <p>
     * When using native ECDSA verification, data must be 32 bytes, and no
     * element may be larger than 520 bytes.
     * </p>
     *
     * @param data
     *            Hash of the data to verify.
     * @param signature
     *            ASN.1 encoded signature.
     * @param pub
     *            The public key bytes to use.
     */
    public static boolean verify(byte[] data, ECDSASignature signature, byte[] pub) {
        if (FAKE_SIGNATURES)
            return true;

//        if (Secp256k1Context.isEnabled()) {
//            try {
//                return NativeSecp256k1.verify(data, signature.encodeToDER(), pub);
//            } catch (NativeSecp256k1Util.AssertFailException e) {
//                log.error("Caught AssertFailException inside secp256k1", e);
//                return false;
//            }
//        }

        ECDSASigner signer = new ECDSASigner();
        ECPublicKeyParameters params = new ECPublicKeyParameters(CURVE.getCurve().decodePoint(pub), CURVE);
        signer.init(false, params);
        try {
            return signer.verifySignature(data, signature.r, signature.s);
        } catch (NullPointerException e) {
            // Bouncy Castle contains a bug that can cause NPEs given specially
            // crafted signatures. Those signatures
            // are inherently invalid/attack sigs so we just fail them here
            // rather than crash the thread.
            log.error("Caught NPE inside bouncy castle", e);
            return false;
        }
    }

    /**
     * Verifies the given ASN.1 encoded ECDSA signature against a hash using the
     * public key.
     *
     * @param data
     *            Hash of the data to verify.
     * @param signature
     *            ASN.1 encoded signature.
     * @param pub
     *            The public key bytes to use.
     */
    public static boolean verify(byte[] data, byte[] signature, byte[] pub) {
//        if (Secp256k1Context.isEnabled()) {
//            try {
//                return NativeSecp256k1.verify(data, signature, pub);
//            } catch (NativeSecp256k1Util.AssertFailException e) {
//                log.error("Caught AssertFailException inside secp256k1", e);
//                return false;
//            }
//        }
        return verify(data, ECDSASignature.decodeFromDER(signature), pub);
    }

    /**
     * Verifies the given ASN.1 encoded ECDSA signature against a hash using the
     * public key.
     *
     * @param hash
     *            Hash of the data to verify.
     * @param signature
     *            ASN.1 encoded signature.
     */
    public boolean verify(byte[] hash, byte[] signature) {
        return ECKey.verify(hash, signature, getPubKey());
    }

    /**
     * Verifies the given R/S pair (signature) against a hash using the public
     * key.
     */
    public boolean verify(Sha256Hash sigHash, ECDSASignature signature) {
        return verify(sigHash.getBytes(), signature, getPubKey());
    }

    public String signMessage(Sha256Hash messageHash) {
        return signMessage(messageHash, null);
    }

    public String signMessage(Sha256Hash messageHash, @Nullable KeyParameter aesKey) {
        ECDSASignature sig = sign(messageHash, aesKey);
        // Now we have to work backwards to figure out the recId needed to
        // recover the signature.
        int recId = -1;
        for (int i = 0; i < 4; i++) {
            ECKey k = ECKey.recoverFromSignature(i, sig, messageHash, isCompressed());
            if (k != null && k.pub.equals(pub)) {
                recId = i;
                break;
            }
        }
        if (recId == -1)
            throw new RuntimeException("Could not construct a recoverable key. This should never happen.");
        int headerByte = recId + 27 + (isCompressed() ? 4 : 0);
        byte[] sigData = new byte[65]; // 1 header + 32 bytes for R + 32 bytes
                                       // for S
        sigData[0] = (byte) headerByte;
        System.arraycopy(CryptoUtils.bigIntegerToBytes(sig.r, 32), 0, sigData, 1, 32);
        System.arraycopy(CryptoUtils.bigIntegerToBytes(sig.s, 32), 0, sigData, 33, 32);
        return new String(Base64.encode(sigData), Charset.forName("UTF-8"));
    }

    @Nullable
    public static ECKey recoverFromSignature(int recId, ECDSASignature sig, Sha256Hash message, boolean compressed) {
        Preconditions.checkArgument(recId >= 0, "recId must be positive");
        Preconditions.checkArgument(sig.r.signum() >= 0, "r must be positive");
        Preconditions.checkArgument(sig.s.signum() >= 0, "s must be positive");
        Preconditions.checkNotNull(message);
        // 1.0 For j from 0 to h (h == recId here and the loop is outside this
        // function)
        // 1.1 Let x = r + jn
        BigInteger n = CURVE.getN(); // Curve order.
        BigInteger i = BigInteger.valueOf((long) recId / 2);
        BigInteger x = sig.r.add(i.multiply(n));
        // 1.2. Convert the integer x to an octet string X of length mlen using
        // the conversion routine
        // specified in Section 2.3.7, where mlen = ???(log2 p)/8??? or mlen =
        // ???m/8???.
        // 1.3. Convert the octet string (16 set binary digits)||X to an
        // elliptic curve point R using the
        // conversion routine specified in Section 2.3.4. If this conversion
        // routine outputs ???invalid???, then
        // do another iteration of Step 1.
        //
        // More concisely, what these points mean is to use X as a compressed
        // public key.
        BigInteger prime = SecP256K1Curve.q;
        if (x.compareTo(prime) >= 0) {
            // Cannot have point co-ordinates larger than this as everything
            // takes place modulo Q.
            return null;
        }
        // Compressed keys require you to know an extra bit of data about the
        // y-coord as there are two possibilities.
        // So it's encoded in the recId.
        ECPoint R = decompressKey(x, (recId & 1) == 1);
        // 1.4. If nR != point at infinity, then do another iteration of Step 1
        // (callers responsibility).
        if (!R.multiply(n).isInfinity())
            return null;
        // 1.5. Compute e from M using Steps 2 and 3 of ECDSA signature
        // verification.
        BigInteger e = message.toBigInteger();
        // 1.6. For k from 1 to 2 do the following. (loop is outside this
        // function via iterating recId)
        // 1.6.1. Compute a candidate public key as:
        // Q = mi(r) * (sR - eG)
        //
        // Where mi(x) is the modular multiplicative inverse. We transform this
        // into the following:
        // Q = (mi(r) * s ** R) + (mi(r) * -e ** G)
        // Where -e is the modular additive inverse of e, that is z such that z
        // + e = 0 (mod n). In the above equation
        // ** is point multiplication and + is point addition (the EC group
        // operator).
        //
        // We can find the additive inverse by subtracting e from zero then
        // taking the mod. For example the additive
        // inverse of 3 modulo 11 is 8 because 3 + 8 mod 11 = 0, and -3 mod 11 =
        // 8.
        BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
        BigInteger rInv = sig.r.modInverse(n);
        BigInteger srInv = rInv.multiply(sig.s).mod(n);
        BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
        ECPoint q = ECAlgorithms.sumOfTwoMultiplies(CURVE.getG(), eInvrInv, R, srInv);
        return ECKey.fromPublicOnly(q.getEncoded(compressed));
    }

    /** Decompress a compressed public key (x co-ord and low-bit of y-coord). */
    private static ECPoint decompressKey(BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.getCurve()));
        compEnc[0] = (byte) (yBit ? 0x03 : 0x02);
        return CURVE.getCurve().decodePoint(compEnc);
    }

    /**
     * Returns a 32 byte array containing the private key.
     * 
     * @throws MissingPrivateKeyException
     *             if the private key bytes are missing/encrypted.
     */
    public byte[] getPrivKeyBytes() {
        return CryptoUtils.bigIntegerToBytes(getPrivKey(), 32);
    }

    /**
     * Exports the private key in the form used by Bitcoin Core's "dumpprivkey"
     * and "importprivkey" commands. Use the
     * {@link DumpedPrivateKey#toString()} method to get
     * the string.
     *
     * @return Private key bytes as a {@link DumpedPrivateKey}.
     * @throws IllegalStateException
     *             if the private key is not available.
     */
    public DumpedPrivateKey getPrivateKeyEncoded(Integer privateKeyHeader) {
        return new DumpedPrivateKey(privateKeyHeader, getPrivKeyBytes(), isCompressed());
    }

    /**
     * Sets the creation time of this key. Zero is a convention to mean
     * "unavailable". This method can be useful when you have a raw key you are
     * importing from somewhere else.
     */
    public void setCreationTimeSeconds(long newCreationTimeSeconds) {
        if (newCreationTimeSeconds < 0)
            throw new IllegalArgumentException("Cannot set creation time to negative value: " + newCreationTimeSeconds);
        creationTimeSeconds = newCreationTimeSeconds;
    }

    /**
     * Create an encrypted private key with the keyCrypter and the AES key
     * supplied. This method returns a new encrypted key and leaves the original
     * unchanged.
     *
     * @param keyCrypter
     *            The keyCrypter that specifies exactly how the encrypted bytes
     *            are created.
     * @param aesKey
     *            The KeyParameter with the AES encryption key (usually
     *            constructed with keyCrypter#deriveKey and cached as it is slow
     *            to create).
     * @return encryptedKey
     */
    public ECKey encrypt(KeyCrypter keyCrypter, KeyParameter aesKey) throws KeyCrypterException {
        checkNotNull(keyCrypter);
        final byte[] privKeyBytes = getPrivKeyBytes();
        EncryptedData encryptedPrivateKey = keyCrypter.encrypt(privKeyBytes, aesKey);
        ECKey result = ECKey.fromEncrypted(encryptedPrivateKey, keyCrypter, getPubKey());
        result.setCreationTimeSeconds(creationTimeSeconds);
        return result;
    }

    /**
     * Create a decrypted private key with the keyCrypter and AES key supplied.
     * Note that if the aesKey is wrong, this has some chance of throwing
     * KeyCrypterException due to the corrupted padding that will result, but it
     * can also just yield a garbage key.
     *
     * @param keyCrypter
     *            The keyCrypter that specifies exactly how the decrypted bytes
     *            are created.
     * @param aesKey
     *            The KeyParameter with the AES encryption key (usually
     *            constructed with keyCrypter#deriveKey and cached).
     */
    public ECKey decrypt(KeyCrypter keyCrypter, KeyParameter aesKey) throws KeyCrypterException {
        checkNotNull(keyCrypter);
        // Check that the keyCrypter matches the one used to encrypt the keys,
        // if set.
        if (this.keyCrypter != null && !this.keyCrypter.equals(keyCrypter))
            throw new KeyCrypterException(
                    "The keyCrypter being used to decrypt the key is different to the one that was used to encrypt it");
        checkState(encryptedPrivateKey != null, "This key is not encrypted");
        byte[] unencryptedPrivateKey = keyCrypter.decrypt(encryptedPrivateKey, aesKey);
        ECKey key = ECKey.fromPrivate(unencryptedPrivateKey);
        if (!isCompressed())
            key = key.decompress();
        if (!Arrays.equals(key.getPubKey(), getPubKey()))
            throw new KeyCrypterException("Provided AES key is wrong");
        key.setCreationTimeSeconds(creationTimeSeconds);
        return key;
    }

    /**
     * Create a decrypted private key with AES key. Note that if the AES key is
     * wrong, this has some chance of throwing KeyCrypterException due to the
     * corrupted padding that will result, but it can also just yield a garbage
     * key.
     *
     * @param aesKey
     *            The KeyParameter with the AES encryption key (usually
     *            constructed with keyCrypter#deriveKey and cached).
     */
    public ECKey decrypt(KeyParameter aesKey) throws KeyCrypterException {
        final KeyCrypter crypter = getKeyCrypter();
        if (crypter == null)
            throw new KeyCrypterException("No key crypter available");
        return decrypt(crypter, aesKey);
    }


    /**
     * Indicates whether the private key is encrypted (true) or not (false). A
     * private key is deemed to be encrypted when there is both a KeyCrypter and
     * the encryptedPrivateKey is non-zero.
     */
    public boolean isEncrypted() {
        return keyCrypter != null && encryptedPrivateKey != null && encryptedPrivateKey.encryptedBytes.length > 0;
    }

    /**
     * Returns the KeyCrypter that was used to encrypt to encrypt this ECKey.
     * You need this to decrypt the ECKey.
     */
    @Nullable
    public KeyCrypter getKeyCrypter() {
        return keyCrypter;
    }

    public static class MissingPrivateKeyException extends RuntimeException {
    }

    public static class KeyIsEncryptedException extends MissingPrivateKeyException {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !(o instanceof ECKey))
            return false;
        ECKey other = (ECKey) o;
        return Objects.equal(this.priv, other.priv) && Objects.equal(this.pub, other.pub)
                && Objects.equal(this.creationTimeSeconds, other.creationTimeSeconds)
                && Objects.equal(this.keyCrypter, other.keyCrypter)
                && Objects.equal(this.encryptedPrivateKey, other.encryptedPrivateKey);
    }

    @Override
    public int hashCode() {
        return pub.hashCode();
    }

    @Override
    public String toString() {
        return toString(false, null, null);
    }


    public String getPrivateKeyAsHex() {
        return CryptoUtils.HEX.encode(getPrivKeyBytes());
    }

    public String getPublicKeyAsHex() {
        return CryptoUtils.HEX.encode(pub.getEncoded());
    }

    public String getPrivateKeyAsWiF(Integer privateKeyHeader) {
        return getPrivateKeyEncoded(privateKeyHeader).toString();
    }

    private String toString(boolean includePrivate, @Nullable KeyParameter aesKey, Integer privateKeyHeader) {
        final MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).omitNullValues();
        helper.add("pub HEX", getPublicKeyAsHex());
        if (includePrivate) {
            ECKey decryptedKey = isEncrypted() ? decrypt(checkNotNull(aesKey)) : this;
            try {
                helper.add("priv HEX", decryptedKey.getPrivateKeyAsHex());
                helper.add("priv WIF", decryptedKey.getPrivateKeyAsWiF(privateKeyHeader));
            } catch (IllegalStateException e) {
                // TODO: Make hasPrivKey() work for deterministic keys and fix
                // this.
            } catch (Exception e) {
                final String message = e.getMessage();
                helper.add("priv EXCEPTION", e.getClass().getName() + (message != null ? ": " + message : ""));
            }
        }
        if (creationTimeSeconds > 0)
            helper.add("creationTimeSeconds", creationTimeSeconds);
        helper.add("keyCrypter", keyCrypter);
        if (includePrivate)
            helper.add("encryptedPrivateKey", encryptedPrivateKey);
        helper.add("isEncrypted", isEncrypted());
        helper.add("isPubKeyOnly", isPubKeyOnly());
        return helper.toString();
    }
}