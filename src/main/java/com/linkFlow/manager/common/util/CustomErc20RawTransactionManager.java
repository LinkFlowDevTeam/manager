package com.linkFlow.manager.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.TxHashMismatchException;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Numeric;
import org.web3j.utils.TxHashVerifier;

import java.io.IOException;
import java.math.BigInteger;

public class CustomErc20RawTransactionManager extends TransactionManager
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Web3j web3j;
    final Credentials credentials;
    private final long chainId;
    protected TxHashVerifier txHashVerifier;

    public CustomErc20RawTransactionManager(Web3j web3j, Credentials credentials, long chainId) {
        super(web3j, credentials.getAddress());
        this.txHashVerifier = new TxHashVerifier();
        this.web3j = web3j;
        this.credentials = credentials;
        this.chainId = chainId;
    }

    public CustomErc20RawTransactionManager(Web3j web3j, Credentials credentials, long chainId, TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, credentials.getAddress());
        this.txHashVerifier = new TxHashVerifier();
        this.web3j = web3j;
        this.credentials = credentials;
        this.chainId = chainId;
    }

    public CustomErc20RawTransactionManager(Web3j web3j, Credentials credentials, long chainId, int attempts, long sleepDuration) {
        super(web3j, attempts, sleepDuration, credentials.getAddress());
        this.txHashVerifier = new TxHashVerifier();
        this.web3j = web3j;
        this.credentials = credentials;
        this.chainId = chainId;
    }

    public CustomErc20RawTransactionManager(Web3j web3j, Credentials credentials) {
        this(web3j, credentials, -1L);
    }

    public CustomErc20RawTransactionManager(Web3j web3j, Credentials credentials, int attempts, int sleepDuration) {
        this(web3j, credentials, -1L, attempts, (long)sleepDuration);
    }

    public BigInteger getNonce() throws IOException {
        EthGetTransactionCount ethGetTransactionCount = this.web3j.ethGetTransactionCount(this.credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
        return ethGetTransactionCount.getTransactionCount();
    }
    public BigInteger getNonce(String address) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = this.web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
        return ethGetTransactionCount.getTransactionCount();
    }


    public TxHashVerifier getTxHashVerifier() {
        return this.txHashVerifier;
    }

    public void setTxHashVerifier(TxHashVerifier txHashVerifier) {
        this.txHashVerifier = txHashVerifier;
    }

    public EthSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {
        BigInteger nonce = this.getNonce();
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
        return this.signAndSend(rawTransaction);
    }
    public EthSendTransaction sendTransactionWithNonce(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value, BigInteger nonce) throws IOException {
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
        return this.signAndSend(rawTransaction);
    }


    public String sendCall(String to, String data, DefaultBlockParameter defaultBlockParameter) throws IOException {
        return this.web3j.ethCall(Transaction.createEthCallTransaction(this.getFromAddress(), to, data), defaultBlockParameter).send().getValue();
    }

    public String sign(RawTransaction rawTransaction) {
        byte[] signedMessage;
        if (this.chainId > -1L) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, this.chainId, this.credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, this.credentials);
        }

        return Numeric.toHexString(signedMessage);
    }

    public EthSendTransaction signAndSend(RawTransaction rawTransaction) throws IOException {
        String hexValue = this.sign(rawTransaction);
        EthSendTransaction ethSendTransaction = this.web3j.ethSendRawTransaction(hexValue).send();
        if (ethSendTransaction != null && !ethSendTransaction.hasError()) {
            String txHashLocal = Hash.sha3(hexValue);
            String txHashRemote = ethSendTransaction.getTransactionHash();
            if (!this.txHashVerifier.verify(txHashLocal, txHashRemote)) {
                throw new TxHashMismatchException(txHashLocal, txHashRemote);
            }
        }

        return ethSendTransaction;
    }
}