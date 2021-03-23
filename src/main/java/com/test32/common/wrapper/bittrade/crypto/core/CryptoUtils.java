/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
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

import com.test32.common.wrapper.google.common.io.BaseEncoding;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.test32.common.wrapper.google.common.base.Preconditions.checkArgument;
import static com.test32.common.wrapper.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;

/**
 * A collection of various utility methods that are helpful for working with a
 * blockchain protocol.
 */
public class CryptoUtils {
    /** Mock for testing. */
    private static BlockingQueue<Boolean> mockSleepQueue;

    /**
     * Private constructor to hide the implicit public one.
     */
    private CryptoUtils() {
    }

    /**
     * <p>
     * The regular {@link BigInteger#toByteArray()} includes the sign
     * bit of the number and might result in an extra byte addition. This method
     * removes this extra byte.
     * </p>
     * <p>
     * Assuming only positive numbers, it's possible to discriminate if an extra
     * byte is added by checking if the first element of the array is 0
     * (0000_0000). Due to the minimal representation provided by BigInteger, it
     * means that the bit sign is the least significant bit 0000_000<b>0</b> .
     * Otherwise the representation is not minimal. For example, if the sign bit
     * is 0000_00<b>0</b>0, then the representation is not minimal due to the
     * rightmost zero.
     * </p>
     *
     * @param b
     *            the integer to format into a byte array
     * @param numBytes
     *            the desired size of the resulting byte array
     * @return numBytes byte long array.
     */
    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        checkArgument(b.signum() >= 0, "b must be positive or zero");
        checkArgument(numBytes > 0, "numBytes must be positive");
        byte[] src = b.toByteArray();
        byte[] dest = new byte[numBytes];
        boolean isFirstByteOnlyForSign = src[0] == 0;
        int length = isFirstByteOnlyForSign ? src.length - 1 : src.length;
        checkArgument(length <= numBytes, "The given number does not fit in " + numBytes);
        int srcPos = isFirstByteOnlyForSign ? 1 : 0;
        int destPos = numBytes - length;
        System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }

//    public static void uint32ToByteArrayBE(long val, byte[] out, int offset) {
//        out[offset] = (byte) (0xFF & (val >> 24));
//        out[offset + 1] = (byte) (0xFF & (val >> 16));
//        out[offset + 2] = (byte) (0xFF & (val >> 8));
//        out[offset + 3] = (byte) (0xFF & val);
//    }

    public static void uint32ToByteArrayLE(long val, byte[] out, int offset) {
        out[offset] = (byte) (0xFF & val);
        out[offset + 1] = (byte) (0xFF & (val >> 8));
        out[offset + 2] = (byte) (0xFF & (val >> 16));
        out[offset + 3] = (byte) (0xFF & (val >> 24));
    }

    public static void uint64ToByteArrayLE(long val, byte[] out, int offset) {
        out[offset] = (byte) (0xFF & val);
        out[offset + 1] = (byte) (0xFF & (val >> 8));
        out[offset + 2] = (byte) (0xFF & (val >> 16));
        out[offset + 3] = (byte) (0xFF & (val >> 24));
        out[offset + 4] = (byte) (0xFF & (val >> 32));
        out[offset + 5] = (byte) (0xFF & (val >> 40));
        out[offset + 6] = (byte) (0xFF & (val >> 48));
        out[offset + 7] = (byte) (0xFF & (val >> 56));
    }

    public static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();

//    public static byte[] reverseBytes(byte[] bytes) {
//        // We could use the XOR trick here but it's easier to understand if we
//        // don't. If we find this is really a
//        // performance issue the matter can be revisited.
//        byte[] buf = new byte[bytes.length];
//        for (int i = 0; i < bytes.length; i++)
//            buf[i] = bytes[bytes.length - 1 - i];
//        return buf;
//    }

    public static long readUint32(byte[] bytes, int offset) {
        return (bytes[offset] & 0xffl) | ((bytes[offset + 1] & 0xffl) << 8) | ((bytes[offset + 2] & 0xffl) << 16)
                | ((bytes[offset + 3] & 0xffl) << 24);
    }

    public static long readInt64(byte[] bytes, int offset) {
        return (bytes[offset] & 0xffl) | ((bytes[offset + 1] & 0xffl) << 8) | ((bytes[offset + 2] & 0xffl) << 16)
                | ((bytes[offset + 3] & 0xffl) << 24) | ((bytes[offset + 4] & 0xffl) << 32)
                | ((bytes[offset + 5] & 0xffl) << 40) | ((bytes[offset + 6] & 0xffl) << 48)
                | ((bytes[offset + 7] & 0xffl) << 56);
    }

//    private static long readUint32BE(byte[] bytes, int offset) {
//        return ((bytes[offset] & 0xffl) << 24) | ((bytes[offset + 1] & 0xffl) << 16)
//                | ((bytes[offset + 2] & 0xffl) << 8) | (bytes[offset + 3] & 0xffl);
//    }
//    public static BigInteger decodeMPI(byte[] mpi, boolean hasLength) {
//        byte[] buf;
//        if (hasLength) {
//            int length = (int) readUint32BE(mpi, 0);
//            buf = new byte[length];
//            System.arraycopy(mpi, 4, buf, 0, length);
//        } else
//            buf = mpi;
//        if (buf.length == 0)
//            return BigInteger.ZERO;
//        boolean isNegative = (buf[0] & 0x80) == 0x80;
//        if (isNegative)
//            buf[0] &= 0x7f;
//        BigInteger result = new BigInteger(buf);
//        return isNegative ? result.negate() : result;
//    }

    private static volatile Date mockTime;

    private static Date rollMockClockMillis(long millis) {
        if (mockTime == null)
            throw new IllegalStateException("You need to use setMockClock() first.");
        mockTime = new Date(mockTime.getTime() + millis);
        return mockTime;
    }

    public static Date now() {
        return mockTime != null ? mockTime : new Date();
    }
//    static long currentTimeMillis() {
//        return mockTime != null ? mockTime.getTime() : System.currentTimeMillis();
//    }

//    static long currentTimeSeconds() {
//        return currentTimeMillis() / 1000;
//    }
//
//    public static byte[] copyOf(byte[] in, int length) {
//        byte[] out = new byte[length];
//        System.arraycopy(in, 0, out, 0, Math.min(length, in.length));
//        return out;
//    }

    public static String toString(byte[] bytes, String charsetName) {
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toBytes(CharSequence str, String charsetName) {
        try {
            return str.toString().getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /** Sleep for a span of time, or mock sleep if enabled */
    public static void sleep(long millis) {
        if (mockSleepQueue == null) {
            sleepUninterruptibly(millis, TimeUnit.MILLISECONDS);
        } else {
            try {
                boolean isMultiPass = mockSleepQueue.take();
                rollMockClockMillis(millis);
                if (isMultiPass)
                    mockSleepQueue.offer(true);
            } catch (InterruptedException e) {
                // Ignored.
            }
        }
    }

    private static int isAndroid = -1;

    static boolean isAndroidRuntime() {
        if (isAndroid == -1) {
            final String runtime = System.getProperty("java.runtime.name");
            isAndroid = (runtime != null && runtime.equals("Android Runtime")) ? 1 : 0;
        }
        return isAndroid == 1;
    }
}