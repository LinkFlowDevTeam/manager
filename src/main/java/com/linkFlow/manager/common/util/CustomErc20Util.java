package com.linkFlow.manager.common.util;

import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.Erc20QueueVO;
import com.linkFlow.manager.common.model.vo.ProductInfoVO;
import com.linkFlow.manager.common.model.vo.SysDataVO;
import com.linkFlow.manager.common.model.vo.TokenInfoVO;
import com.linkFlow.manager.common.service.Erc20QueueService;
import com.linkFlow.manager.common.service.Erc20TxService;
import com.linkFlow.manager.common.service.SysDataService;
import com.test32.common.model.CustomHttpResponse;
import com.test32.common.model.blockChain.erc20.Erc20KeyPair;
import com.test32.common.model.blockChain.erc20.Erc20SendEtherResponse;
import com.test32.common.model.blockChain.erc20.Erc20TxResponse;
import com.test32.common.model.blockChain.erc20.jsonRpc.*;
import com.test32.common.model.blockChain.etherscan.ResEtherScanEthPriceRoot;
import com.test32.common.util.AES256Util;
import com.test32.common.util.CommonHttpClientUtil;
import com.test32.common.util.CommonStaticUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.UUID;

@Component
public class CustomErc20Util
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Integer SERVER_CONNECT_TIMEOUT = 30000;
    private final Integer SERVER_REQUEST_TIMEOUT = 30000;

    @Autowired private ConfigDataManager configDataManager;
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private Erc20QueueService erc20QueueService;
    @Autowired private SysDataService sysDataService;

    @Bean
    public Web3j web3j() {
        Web3j web3j = Web3j.build(new HttpService(getServiceUrl()));
        logger.info("web3j init: " + getServiceUrl());
        return web3j;
    }

    @Autowired private Web3j web3j;

    private String getServiceUrl()
    {
        return configDataManager.getConfigData().getDefaultServerConfigData().getInfuraUrl() + configDataManager.getConfigData().getDefaultServerConfigData().getInfuraId();
    }

    public BigInteger getErc20GasLimit()
    {
        return new BigInteger("" + sysDataService.selectDefault().getSsErc20GasLimit());
    }

    public Integer getErc20GasPricePercent()
    {
        return sysDataService.selectDefault().getSsErc20GasPricePercent();
    }

    public String convertStringToHex(String input)
    {
        return Numeric.toHexString(input.getBytes());
    }
    public BigInteger convertHexToQuantity(String input)
    {
        return Numeric.decodeQuantity(input);
    }
    public String convertQuantityToHex(Long quantity)
    {
        return convertQuantityToHex(BigInteger.valueOf(quantity));
    }
    public String convertQuantityToHex(BigInteger quantity)
    {
        return Numeric.encodeQuantity(quantity);
    }

    public BigDecimal convertHexToDecimal(String input)
    {
        //return BigDecimal.valueOf(Numeric.decodeQuantity(input).longValue());
        return new BigDecimal("" + Numeric.decodeQuantity(input));
    }

    public BigDecimal convertHexToDecimalWithTokenInfo(String hexString, int decimalLength)
    {
        //BigDecimal amountString = BigDecimal.valueOf(Numeric.decodeQuantity(hexString).longValue());
        BigDecimal amountString = new BigDecimal("" + Numeric.decodeQuantity(hexString));
        String decimalBase = "%0" + decimalLength + "d";
        String multiplierString = "0." + String.format(decimalBase, 1);
        return amountString.multiply(new BigDecimal(multiplierString));
    }

    public Erc20KeyPair generateKeyPair()
    {
        try
        {
            Erc20KeyPair erc20KeyPair = new Erc20KeyPair();
            String seed = UUID.randomUUID().toString();
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();

            String sPrivateKeyInHex = privateKeyInDec.toString(16);

            WalletFile aWallet = Wallet.createLight(seed, ecKeyPair);
            String sAddress = aWallet.getAddress();

            erc20KeyPair.setAddress("0x" + sAddress);
            erc20KeyPair.setPrivateKey(sPrivateKeyInHex);
            return erc20KeyPair;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    private String getAesKey(ProductInfoVO productInfoVO)
    {
        String baseString = String.format("%016d", productInfoVO.getPdIdx());
        //return "z9" + productInfoVO.getMbId().substring(0, 1) + baseString.substring(3);
        return "z9" + ("" + productInfoVO.getPdIdx()).substring(0, 1) + baseString.substring(3);
    }

    public String encodeErc20Key(ProductInfoVO productInfoVO, String key)
    {
        try
        {
            AES256Util aes256Util = new AES256Util(getAesKey(productInfoVO));
            return aes256Util.encrypt(key);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public String decodeEr20Key(ProductInfoVO productInfoVO, String raw)
    {
        try
        {
            AES256Util aes256Util = new AES256Util(getAesKey(productInfoVO));
            return aes256Util.decrypt(raw);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public JsonRpcBalanceOf ethBalance(String targetAddress)
    {
        JsonRpcBalanceOf result = null;
        CustomHttpResponse response = null;
        try
        {
            //String reqParam = "{"jsonrpc":"2.0","method":"eth_getBalance","params": ["0x11824636b1a8fe1959effd3170823986f86a3657", "latest"],"id":1}";
            String reqParam = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBalance\",\"params\": [\"{targetAddress}\", \"latest\"],\"id\":1}";
            if(targetAddress.startsWith("0x"))
                reqParam = reqParam.replace("{targetAddress}", targetAddress);
            else
                reqParam = reqParam.replace("{targetAddress}", "0x" + targetAddress);
            response = httpsPostRequest(getServiceUrl(),reqParam);
            if(response.getResponseCode() != null && 200 == response.getResponseCode())
                result = CommonStaticUtil.convertJsonStringToObject(response.getBaseResponse(), JsonRpcBalanceOf.class);
            else
                logger.error(response.toString());
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            if(response != null)
                logger.error(response.toString());
        }
        return result;
    }

    public JsonRpcBalanceOf balanceOf(String contractAddress, String targetAddress)
    {
        JsonRpcBalanceOf result = null;
        CustomHttpResponse response = null;
        try
        {
            //String reqParam = "{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"eth_call\",\"params\":[{\"data\":\"0x70a082310000000000000000000000001479a9BFa1ee3C33956eA946827A6D9f3a96dC5B\",\"to\":\"0xfD623dBDFC6D0ff85c9A476B8B1205e310e363AE\"}, \"latest\"]}";
            String reqParam = "{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"eth_call\",\"params\":[{\"data\":\"0x70a08231000000000000000000000000{targetAddress}\",\"to\":\"{contractAddress}\"}, \"latest\"]}";
            reqParam = reqParam.replace("{contractAddress}", contractAddress);
            if(targetAddress.startsWith("0x"))
                reqParam = reqParam.replace("{targetAddress}", targetAddress.substring(2));
            else
                reqParam = reqParam.replace("{targetAddress}", targetAddress);
            response = httpsPostRequest(getServiceUrl(),reqParam);
            if(response.getResponseCode() != null && 200 == response.getResponseCode())
                result = CommonStaticUtil.convertJsonStringToObject(response.getBaseResponse(), JsonRpcBalanceOf.class);
            else
                logger.error(response.toString());
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            if(response != null)
                logger.error(response.toString());
        }
        return result;
    }

    public JsonRpcBlockNumber getBlockNumber()
    {
        JsonRpcBlockNumber result = new JsonRpcBlockNumber();
        CustomHttpResponse response = null;
        try
        {
            String reqParam = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_blockNumber\",\"params\": [],\"id\":1}";
            response = httpsPostRequest(getServiceUrl(),reqParam);
            if(response.getResponseCode() != null && 200 == response.getResponseCode())
                result = CommonStaticUtil.convertJsonStringToObject(response.getBaseResponse(), JsonRpcBlockNumber.class);
            else
                logger.error(response.toString());
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            if(response != null)
                logger.error(response.toString());
        }
        return result;
    }

    public JsonRpcGetBlockByNumberSimple getBlockByNumberSimple(Long blockNumber)
    {
        return getBlockByNumberSimple(convertQuantityToHex(blockNumber));
    }

    private JsonRpcGetBlockByNumberSimple getBlockByNumberSimple(String blockNumber)
    {
        JsonRpcGetBlockByNumberSimple result = new JsonRpcGetBlockByNumberSimple();
        CustomHttpResponse response = null;
        try
        {
            // SHOW TRANSACTION DETAILS FLAG [required] - if set to true, it returns the full transaction objects, if false only the hashes of the transactions.
            //{"jsonrpc":"2.0","method":"eth_getBlockByNumber","params": ["0x5BAD55",false],"id":1}
            String reqParam = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBlockByNumber\",\"params\": [\"{blockNumber}\",{isShowDetail}],\"id\":1}";
            reqParam = reqParam.replace("{blockNumber}", blockNumber);
            reqParam = reqParam.replace("{isShowDetail}", "false");
            response = httpsPostRequest(getServiceUrl(),reqParam);
            if(response.getResponseCode() != null && 200 == response.getResponseCode())
                result = CommonStaticUtil.convertJsonStringToObject(response.getBaseResponse(), JsonRpcGetBlockByNumberSimple.class);
            else
                logger.error(response.toString());
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            if(response != null)
                logger.error(response.toString());
        }
        return result;
    }

    public JsonRpcGetBlockByNumberDetail getBlockByNumberDetail(Long blockNumber)
    {
        return getBlockByNumberDetail(convertQuantityToHex(blockNumber));
    }

    private JsonRpcGetBlockByNumberDetail getBlockByNumberDetail(String blockNumber)
    {
        JsonRpcGetBlockByNumberDetail result = new JsonRpcGetBlockByNumberDetail();
        CustomHttpResponse response = null;
        try
        {
            // SHOW TRANSACTION DETAILS FLAG [required] - if set to true, it returns the full transaction objects, if false only the hashes of the transactions.
            //{"jsonrpc":"2.0","method":"eth_getBlockByNumber","params": ["0x5BAD55",false],"id":1}
            String reqParam = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBlockByNumber\",\"params\": [\"{blockNumber}\",{isShowDetail}],\"id\":1}";
            reqParam = reqParam.replace("{blockNumber}", blockNumber);
            reqParam = reqParam.replace("{isShowDetail}", "true");
            response = httpsPostRequest(getServiceUrl(),reqParam);
            if(response.getResponseCode() != null && 200 == response.getResponseCode())
                result = CommonStaticUtil.convertJsonStringToObject(response.getBaseResponse(), JsonRpcGetBlockByNumberDetail.class);
            else
                logger.error(response.toString());
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            if(response != null)
                logger.error(response.toString());
        }
        return result;
    }

    public JsonRpcGetTransactionReceipt getTransactionReceipt(String txHash)
    {
        JsonRpcGetTransactionReceipt result = null;
        CustomHttpResponse response = null;
        try
        {
            //{"jsonrpc":"2.0","method":"eth_getTransactionReceipt","params": ["0x827a90c0752118b5f6a781e061a8ebbbe74640c55dadc4f8d0602c6d69ce9186"],"id":1}
            String reqParam = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionReceipt\",\"params\": [\"{txHash}\"],\"id\":1}";
            reqParam = reqParam.replace("{txHash}", txHash);
            response = httpsPostRequest(getServiceUrl(),reqParam);
            if(response.getResponseCode() != null && 200 == response.getResponseCode())
                result = CommonStaticUtil.convertJsonStringToObject(response.getBaseResponse(), JsonRpcGetTransactionReceipt.class);
            else
                logger.error(response.toString());
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            if(response != null)
                logger.error(response.toString());
        }
        return result;
    }

    public JsonRpcGetTransactionByHash getTransactionByHash(String txHash)
    {
        JsonRpcGetTransactionByHash result = null;
        CustomHttpResponse response = null;
        try
        {
            //{"jsonrpc":"2.0","method":"eth_getTransactionByHash","params": ["0x51ea1e3bb57164a88cb45bfe3e62f39842105e13efc165e0d62073b9a23dec1d"],"id":1}
            String reqParam = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionByHash\",\"params\": [\"{txHash}\"],\"id\":1}";
            reqParam = reqParam.replace("{txHash}", txHash);
            response = httpsPostRequest(getServiceUrl(),reqParam);
            if(response.getResponseCode() != null && 200 == response.getResponseCode())
                result = CommonStaticUtil.convertJsonStringToObject(response.getBaseResponse(), JsonRpcGetTransactionByHash.class);
            else
                logger.error(response.toString());
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            if(response != null)
                logger.error(response.toString());
        }
        return result;
    }

    private String encodeTransferData(String toAddress, BigInteger sum)
    {
        Function function = new Function(
                "transfer",  // function we're calling
                Arrays.asList(new Address(toAddress), new Uint256(sum)),  // Parameters to pass as Solidity Types
                Arrays.asList(new org.web3j.abi.TypeReference<Bool>() {}));
        return FunctionEncoder.encode(function);
    }

    private CustomHttpResponse httpsPostRequest(String requestUrl, String requestParam)
    {
        CustomHttpResponse result = new CustomHttpResponse();
        result.setRequestUrl(requestUrl);
        HttpsURLConnection conn = null;

        String baseResponse = null;
        Integer responseCode = null;
        String errorMessage = null;
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager()
                    {
                        public X509Certificate[] getAcceptedIssuers()
                        {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType)
                        {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType)
                        {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setRequestProperty("User-Agent", "");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", "" + Integer.toString(requestParam.getBytes().length));

            conn.setReadTimeout(SERVER_REQUEST_TIMEOUT);
            conn.setConnectTimeout(SERVER_CONNECT_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(requestParam);
            writer.close();
            wr.close();

            InputStream is;
            if (conn.getResponseCode() == 200)
                is = conn.getInputStream();
            else
                is = conn.getErrorStream();
            if (is == null)
                is = conn.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null)
            {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            responseCode = conn.getResponseCode();
            baseResponse = response.toString();
        }
        catch (Exception e)
        {
            System.out.println("## http Exception toString: " + e.toString());
            System.out.println("## http Exception getMessage: " + e.getMessage());
            errorMessage = e.getMessage();
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }

        result.setResponseCode(responseCode);
        result.setBaseResponse(baseResponse);
        result.setErrorMessage(errorMessage);
        return result;
    }

    private Erc20TxResponse checkRequestValid(TokenInfoVO tokenInfoVO, BigDecimal transferAmount)
    {
//        Erc20TxResponse resCheckTokenType = checkTokenType(tokenInfoVO);
//        if(resCheckTokenType != null)
//            return resCheckTokenType;
        Erc20TxResponse resCheckAmount = checkTransferAmount(transferAmount);
        if(resCheckAmount != null)
            return resCheckAmount;

        return null;
    }
    private Erc20TxResponse checkTransferAmount(BigDecimal transferAmount)
    {
        if(transferAmount == null || transferAmount.compareTo(BigDecimal.ZERO) <= 0)
        {
            Erc20TxResponse response = new Erc20TxResponse();
            response.setReturnCode(ReturnCode.INVALID_REQUEST_DATA);
            response.setDescription(response.getReturnCode().getMessage());
            response.setExtraMessage("invalid transfer amount");
            return response;
        }
        return null;
    }


    public Erc20TxResponse depositErc20TokenFromSystemToMember(ProductInfoVO productInfoVO, TokenInfoVO tokenInfoVO, BigDecimal transferAmount, BigInteger gasLimit, BigInteger gasPrice, BigInteger nonce)
    {
        Erc20TxResponse checkTypeResult = checkRequestValid(tokenInfoVO, transferAmount);
        if(checkTypeResult != null)
            return  checkTypeResult;
        BigInteger erc20Amount = convertErc20TransferAmount(tokenInfoVO, transferAmount);
        return sendErc20Token(productInfoVO, Erc20QueueService.IO_DEPOSIT, configDataManager.getConfigData().getDefaultServerConfigData().getErc20WithdrawKey(), tokenInfoVO, configDataManager.getConfigData().getDefaultServerConfigData().getErc20WithdrawAccount(), productInfoVO.getPdErc20Address(), transferAmount, erc20Amount, gasLimit, gasPrice, nonce);
    }

    public Erc20TxResponse withdrawErc20TokenFromMemberToSystem(ProductInfoVO productInfoVO, TokenInfoVO tokenInfoVO, BigDecimal transferAmount, BigInteger gasLimit, BigInteger gasPrice, BigInteger nonce)
    {
        Erc20TxResponse checkTypeResult = checkRequestValid(tokenInfoVO, transferAmount);
        if(checkTypeResult != null)
            return  checkTypeResult;
        BigInteger erc20Amount = convertErc20TransferAmount(tokenInfoVO, transferAmount);
        SysDataVO sysDataVO = sysDataService.selectDefault();
        return sendErc20Token(productInfoVO, Erc20QueueService.IO_WITHDRAW, customComponentUtil.getErc20Key(productInfoVO), tokenInfoVO, productInfoVO.getPdErc20Address(), sysDataVO.getSsProductPurchaseWithdrawAddress(), transferAmount, erc20Amount, gasLimit, gasPrice, nonce);
    }


    public BigInteger convertErc20TransferAmount(TokenInfoVO tokenInfoVO, BigDecimal rawAmount)
    {
        return convertErc20TransferAmount(tokenInfoVO.getTkDecimal(), rawAmount);
    }
    public BigInteger convertErc20TransferAmount(Integer decimalPoint, BigDecimal rawAmount)
    {
        String multiplierString = "10000000000000000000000000000".substring(0, decimalPoint + 1);
        return rawAmount.multiply(new BigDecimal(multiplierString)).toBigInteger();
    }

    public Erc20TxResponse sendErc20TokenByKey(ProductInfoVO productInfoVO, Integer io, TokenInfoVO tokenInfoVO, String fromKey, String fromAddress, String toAddress, BigDecimal rawAmount, BigInteger sendAmount, BigInteger gasLimit, BigInteger gasPrice, BigInteger nonce)
    {
        return sendErc20Token(productInfoVO, io, fromKey, tokenInfoVO, fromAddress, toAddress, rawAmount, sendAmount, gasLimit, gasPrice, nonce);
    }

    private Erc20TxResponse sendErc20Token(ProductInfoVO productInfoVO, Integer io, String privateKey, TokenInfoVO tokenInfoVO, String fromAddress, String toAddress, BigDecimal rawAmount, BigInteger sendAmount, BigInteger gasLimit, BigInteger gasPrice, BigInteger nonce)
    {
        Erc20TxResponse response = new Erc20TxResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = "";
        try
        {
            String contractAddress = tokenInfoVO.getTkAddress();
            if(gasLimit == null)
                gasLimit = getErc20GasLimit();
            if(gasPrice == null)
                gasPrice = web3j.ethGasPrice().send().getGasPrice();

            if(nonce == null)
                nonce = getNonce(fromAddress);
            Erc20QueueVO erc20QueueVO = new Erc20QueueVO();
            erc20QueueVO.setEqState(Erc20QueueService.STATE_TRANSFER_REQUEST);
            erc20QueueVO.setEqMbIdx(Long.valueOf(productInfoVO.getPdIdx()));
            erc20QueueVO.setEqIo(io);
            erc20QueueVO.setEqFrom(fromAddress);
            erc20QueueVO.setEqTo(toAddress);
            erc20QueueVO.setEqAmount(rawAmount);
            erc20QueueVO.setEqTkIdx(tokenInfoVO.getTkIdx());
            erc20QueueVO.setEqContractAddress(tokenInfoVO.getTkAddress());
            erc20QueueVO.setEqSymbol(tokenInfoVO.getTkSymbol());
            erc20QueueVO.setEqGasLimit(gasLimit.longValue());
            erc20QueueVO.setEqGasPrice(gasPrice.longValue());

            if(nonce != null)
                erc20QueueVO.setEqNonce(nonce.longValue());
            if(erc20QueueService.insert(erc20QueueVO))
            {
                try
                {
                    Credentials credentials = Credentials.create(privateKey);
                    CustomErc20RawTransactionManager manager = new CustomErc20RawTransactionManager(web3j, credentials);
                    String data = encodeTransferData(toAddress, sendAmount);
                    EthSendTransaction transaction = manager.sendTransactionWithNonce(gasPrice, gasLimit, contractAddress, data, BigInteger.ZERO, nonce);
                    response.setTransaction(transaction);
                    logger.info("transaction: " + transaction);
                    logger.info("hash: " + transaction.getTransactionHash());
                    logger.info("result: " + transaction.getResult());
                    if(transaction.getError() != null)
                    {
                        extraMessage = "" + transaction.getError().getMessage() + " " + transaction.getError().getCode() + transaction.getError().getData();
                        logger.error(extraMessage);

                        Erc20QueueVO forUpdate = new Erc20QueueVO();
                        forUpdate.setEqIdx(erc20QueueVO.getEqIdx());
                        forUpdate.setEqState(Erc20QueueService.STATE_TRANSFER_RESPONSE_ERROR);
                        forUpdate.setEqTxHash(transaction.getTransactionHash());
                        forUpdate.setEqExtraMessage(extraMessage);
                        erc20QueueService.update(forUpdate);
                    }
                    else
                    {
                        Erc20QueueVO forUpdate = new Erc20QueueVO();
                        forUpdate.setEqIdx(erc20QueueVO.getEqIdx());
                        forUpdate.setEqState(Erc20QueueService.STATE_TRANSFER_RESPONSE_SUCCESS);
                        forUpdate.setEqTxHash(transaction.getTransactionHash());

                        if(erc20QueueService.update(forUpdate))
                            returnCode = ReturnCode.SUCCESS;
                        else
                            logger.error("failed to : erc20QueueService.update");
                    }
                }
                catch (Exception subE)
                {
                    extraMessage = subE.toString();
                    Erc20QueueVO forUpdate = new Erc20QueueVO();
                    forUpdate.setEqIdx(erc20QueueVO.getEqIdx());
                    forUpdate.setEqState(Erc20QueueService.STATE_TRANSFER_REQUEST_ERROR);
                    forUpdate.setEqExtraMessage(extraMessage);
                    erc20QueueService.update(forUpdate);

                    logger.error(subE.toString());
                    subE.printStackTrace();
                }
            }
            else
            {
                extraMessage = "failed to: erc20QueueService.insert";
            }
        }
        catch (Exception e)
        {
            extraMessage = e.toString();
            logger.error(e.toString());
            e.printStackTrace();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    public Erc20SendEtherResponse depositEth(ProductInfoVO productInfoVO, BigDecimal transferAmount)
    {
        return sendEther(productInfoVO, Erc20QueueService.IO_DEPOSIT, configDataManager.getConfigData().getDefaultServerConfigData().getErc20WithdrawKey(), configDataManager.getConfigData().getDefaultServerConfigData().getErc20WithdrawAccount(), productInfoVO.getPdErc20Address(), transferAmount);
    }

    public Erc20SendEtherResponse withdrawEth(ProductInfoVO productInfoVO, BigDecimal transferAmount)
    {
        SysDataVO sysDataVO = sysDataService.selectDefault();
        return sendEther(productInfoVO, Erc20QueueService.IO_WITHDRAW, customComponentUtil.getErc20Key(productInfoVO), productInfoVO.getPdErc20Address(), sysDataVO.getSsGasFeeAddress(), transferAmount);
    }

    private Erc20SendEtherResponse sendEther(ProductInfoVO productInfoVO, Integer io, String privateKey, String fromAddress, String toAddress, BigDecimal reqAmount)
    {
        Erc20SendEtherResponse response = new Erc20SendEtherResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = "";
        try
        {
            if(reqAmount == null || reqAmount.compareTo(BigDecimal.ZERO) <= 0)
            {
                returnCode = ReturnCode.INVALID_REQUEST_DATA;
                extraMessage = "invalid amount";
            }
            else
            {
                Erc20QueueVO erc20QueueVO = new Erc20QueueVO();
                erc20QueueVO.setEqState(Erc20QueueService.STATE_TRANSFER_REQUEST);
                erc20QueueVO.setEqMbIdx(productInfoVO.getPdIdx().longValue());
                erc20QueueVO.setEqIo(io);
                erc20QueueVO.setEqFrom(fromAddress);
                erc20QueueVO.setEqTo(toAddress);
                erc20QueueVO.setEqAmount(reqAmount);
                erc20QueueVO.setEqGasLimit(Transfer.GAS_LIMIT.longValue());
                if(erc20QueueService.insert(erc20QueueVO))
                {
                    try
                    {
                        Credentials credentials = Credentials.create(privateKey);
                        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                                web3j, credentials, toAddress,
                                reqAmount, Convert.Unit.ETHER).send();

                        response.setTransactionReceipt(transactionReceipt);

                        Erc20QueueVO forUpdate = new Erc20QueueVO();
                        forUpdate.setEqIdx(erc20QueueVO.getEqIdx());

                        if(transactionReceipt != null)
                        {
                            if(Erc20TxService.STATUS_RECEIPT_SUCCESS.equalsIgnoreCase(transactionReceipt.getStatus()))
                            {
                                forUpdate.setEqState(Erc20QueueService.STATE_TRANSFER_RESPONSE_SUCCESS);
                            }
                            else
                            {
                                forUpdate.setEqState(Erc20QueueService.STATE_TRANSFER_RESPONSE_ERROR);
                            }
                            forUpdate.setEqSymbol("ETH");
                            forUpdate.setEqStatus(transactionReceipt.getStatus());
                            forUpdate.setEqGasUsed(transactionReceipt.getGasUsedRaw());
                            forUpdate.setEqTxHash(transactionReceipt.getTransactionHash());
                            forUpdate.setEqBlockHash(transactionReceipt.getBlockHash());
                            forUpdate.setEqBlockString(transactionReceipt.getBlockNumberRaw());
                            forUpdate.setEqBlockNumber(transactionReceipt.getBlockNumber().longValue());

                            logger.info("status: " + transactionReceipt.getStatus());
                            logger.info("hash: " + transactionReceipt.getTransactionHash());
                            logger.info("getGasUsed: " + transactionReceipt.getGasUsed());
                        }
                        if(erc20QueueService.update(forUpdate))
                            returnCode = ReturnCode.SUCCESS;
                        else
                        {
                            logger.error("failed to : erc20QueueService.update");
                        }
                    }
                    catch (Exception subE)
                    {
                        extraMessage = subE.toString();
                        Erc20QueueVO forUpdate = new Erc20QueueVO();
                        forUpdate.setEqIdx(erc20QueueVO.getEqIdx());
                        forUpdate.setEqState(Erc20QueueService.STATE_TRANSFER_REQUEST_ERROR);
                        forUpdate.setEqExtraMessage(extraMessage);
                        erc20QueueService.update(forUpdate);

                        logger.error(subE.toString());
                        subE.printStackTrace();
                    }
                }
                else
                {
                    extraMessage = "failed to: erc20QueueService.insert";
                }
            }
        }
        catch (Exception e)
        {
            extraMessage = e.toString();
            logger.error(e.toString());
            e.printStackTrace();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    public Erc20TxResponse sendZeroEthByRaw(String privateKey, String fromAddress, String toAddress, BigInteger nonce, Integer extraPercent)
    {
        Erc20TxResponse response = new Erc20TxResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage;
        try
        {
            BigInteger gasLimit = Transfer.GAS_LIMIT;

            if(nonce == null)
                nonce = getNonce(fromAddress);
            try
            {
                if(extraPercent < 0 || extraPercent > 20)
                {
                    returnCode = ReturnCode.INVALID_REQUEST_DATA;
                    extraMessage = "범위: 0~20";
                }
                else
                {
                    BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();

                    if(extraPercent > 0)
                    {
                        // input: 20
                        BigDecimal extraAmount = new BigDecimal("" + gasPrice).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal("" + extraPercent)).multiply(new BigDecimal("0.01"));
                        BigInteger extraPrice = extraAmount.toBigInteger();
                        logger.info("gasPrice: " + gasPrice);
                        logger.info("extraPrice: " + extraPrice);
                        gasPrice = gasPrice.add(extraPrice);
                    }

                    Credentials credentials = Credentials.create(privateKey);
                    CustomErc20RawTransactionManager manager = new CustomErc20RawTransactionManager(web3j, credentials);
                    String data = "0x";
                    EthSendTransaction transaction = manager.sendTransactionWithNonce(gasPrice, gasLimit, toAddress, data, BigInteger.ZERO, nonce);
                    response.setTransaction(transaction);
                    logger.info("transaction: " + transaction);
                    logger.info("hash: " + transaction.getTransactionHash());
                    logger.info("result: " + transaction.getResult());
                    if(transaction.getError() != null)
                    {
                        extraMessage = "" + transaction.getError().getMessage() + " " + transaction.getError().getCode() + transaction.getError().getData();
                        logger.error(extraMessage);
                    }
                    else
                    {
                        returnCode = ReturnCode.SUCCESS;
                        extraMessage = transaction.getTransactionHash();
                    }
                }
            }
            catch (Exception subE)
            {
                extraMessage = subE.toString();
                logger.error(subE.toString());
                subE.printStackTrace();
            }
        }
        catch (Exception e)
        {
            extraMessage = e.toString();
            logger.error(e.toString());
            e.printStackTrace();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    public Erc20TxResponse sendZeroEthWithMessage(String privateKey, String fromAddress, String toAddress, BigInteger nonce, Integer extraPercent, String data)
    {
        Erc20TxResponse response = new Erc20TxResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage;
        try
        {
            //BigInteger gasLimit = Transfer.GAS_LIMIT;
                BigInteger   gasLimit = getErc20GasLimit();


            if(nonce == null)
                nonce = getNonce(fromAddress);
            try
            {
                if(extraPercent < 0 || extraPercent > 20)
                {
                    returnCode = ReturnCode.INVALID_REQUEST_DATA;
                    extraMessage = "범위: 0~20";
                }
                else
                {
                    BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();

                    if(extraPercent > 0)
                    {
                        // input: 20
                        BigDecimal extraAmount = new BigDecimal("" + gasPrice).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal("" + extraPercent)).multiply(new BigDecimal("0.01"));
                        BigInteger extraPrice = extraAmount.toBigInteger();
                        logger.info("gasPrice: " + gasPrice);
                        logger.info("extraPrice: " + extraPrice);
                        gasPrice = gasPrice.add(extraPrice);
                    }

                    Credentials credentials = Credentials.create(privateKey);
                    CustomErc20RawTransactionManager manager = new CustomErc20RawTransactionManager(web3j, credentials);
                    //String data = "0x";
                    EthSendTransaction transaction = manager.sendTransactionWithNonce(gasPrice, gasLimit, toAddress, data, BigInteger.ZERO, nonce);
                    response.setTransaction(transaction);
                    logger.info("transaction: " + transaction);
                    logger.info("hash: " + transaction.getTransactionHash());
                    logger.info("result: " + transaction.getResult());
                    if(transaction.getError() != null)
                    {
                        extraMessage = "" + transaction.getError().getMessage() + " " + transaction.getError().getCode() + " " + transaction.getError().getData();
                        logger.error(extraMessage);
                    }
                    else
                    {
                        returnCode = ReturnCode.SUCCESS;
                        extraMessage = transaction.getTransactionHash();
                    }
                }
            }
            catch (Exception subE)
            {
                extraMessage = subE.toString();
                logger.error(subE.toString());
                subE.printStackTrace();
            }
        }
        catch (Exception e)
        {
            extraMessage = e.toString();
            logger.error(e.toString());
            e.printStackTrace();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    public Erc20TxResponse sendEthByRaw(String privateKey, String fromAddress, String toAddress, BigInteger nonce, BigDecimal amount, Integer extraPercent)
    {
        Erc20TxResponse response = new Erc20TxResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage;
        try
        {
            BigInteger gasLimit = Transfer.GAS_LIMIT;

            if(nonce == null)
                nonce = getNonce(fromAddress);
            try
            {
                if(extraPercent < 0 || extraPercent > 20)
                {
                    returnCode = ReturnCode.INVALID_REQUEST_DATA;
                    extraMessage = "범위: 0~20";
                }
                else
                {
                    BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();

                    if(extraPercent > 0)
                    {
                        // input: 20
                        BigDecimal extraAmount = new BigDecimal("" + gasPrice).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal("" + extraPercent)).multiply(new BigDecimal("0.01"));
                        BigInteger extraPrice = extraAmount.toBigInteger();
                        logger.info("gasPrice: " + gasPrice);
                        logger.info("extraPrice: " + extraPrice);
                        gasPrice = gasPrice.add(extraPrice);
                    }
                    BigInteger erc20Amount = convertErc20TransferAmount(18, amount);

                    Credentials credentials = Credentials.create(privateKey);
                    CustomErc20RawTransactionManager manager = new CustomErc20RawTransactionManager(web3j, credentials);
                    String data = "0x";
                    EthSendTransaction transaction = manager.sendTransactionWithNonce(gasPrice, gasLimit, toAddress, data, erc20Amount, nonce);
                    response.setTransaction(transaction);
                    logger.info("transaction: " + transaction);
                    logger.info("hash: " + transaction.getTransactionHash());
                    logger.info("result: " + transaction.getResult());
                    if(transaction.getError() != null)
                    {
                        extraMessage = "" + transaction.getError().getMessage() + " " + transaction.getError().getCode() + transaction.getError().getData();
                        logger.error(extraMessage);
                    }
                    else
                    {
                        returnCode = ReturnCode.SUCCESS;
                        extraMessage = transaction.getTransactionHash();
                    }
                }
            }
            catch (Exception subE)
            {
                extraMessage = subE.toString();
                logger.error(subE.toString());
                subE.printStackTrace();
            }
        }
        catch (Exception e)
        {
            extraMessage = e.toString();
            logger.error(e.toString());
            e.printStackTrace();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    public EthGasPrice getGasPrice()
    {
        try
        {
            return web3j.ethGasPrice().send();
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public BigInteger getNonce(String address)
    {
        try
        {
            EthGetTransactionCount ethGetTransactionCount = this.web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            return ethGetTransactionCount.getTransactionCount();
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        return null;
    }

    public ResEtherScanEthPriceRoot getEthPriceFromEtherScan()
    {
        String url = configDataManager.getConfigData().getDefaultServerConfigData().getEtherscanEthPriceUrl();
        String baseResponse = null;
        ResEtherScanEthPriceRoot response = null;
        try
        {
            baseResponse = CommonHttpClientUtil.httpsGetRequest(url);
            if( ! StringUtils.isEmpty(baseResponse))
            {
                response = CommonStaticUtil.convertJsonStringToObject(baseResponse, ResEtherScanEthPriceRoot.class);
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            if(baseResponse!= null)
                logger.error(baseResponse);
        }
        return response;
    }

    private final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";
    public boolean isValidSignature(String address, String message, String signature)
    {
        boolean isMatch = false;
        String addressRecovered = null;
        try
        {
            String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
            byte[] msgHash = Hash.sha3((prefix + message).getBytes());

            byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
            byte v = signatureBytes[64];
            if (v < 27) {
                v += 27;
            }

            Sign.SignatureData sd = new Sign.SignatureData(
                    v,
                    Arrays.copyOfRange(signatureBytes, 0, 32),
                    Arrays.copyOfRange(signatureBytes, 32, 64));

            for (int i = 0; i < 4; i++) {
                BigInteger publicKey = Sign.recoverFromSignature(
                        (byte) i,
                        new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                        msgHash);

                if (publicKey != null) {
                    addressRecovered = "0x" + Keys.getAddress(publicKey);

                    if (addressRecovered.equalsIgnoreCase(address)) {
                        isMatch = true;
                        break;
                    }
                }
            }
            if( ! isMatch)
            {
                //logger.info("addressRecovered: " + addressRecovered);
                logger.error("isMatch: false");
                logger.info("address: " + address);
                logger.info("message: " + message);
                logger.info("signature: " + signature);
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return isMatch;
    }
}