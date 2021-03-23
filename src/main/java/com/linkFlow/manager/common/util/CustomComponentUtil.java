package com.linkFlow.manager.common.util;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.slack.SlackMessageAttachmentField;
import com.linkFlow.manager.common.model.slack.SlackMessageAttachmentWithdrawRequest;
import com.linkFlow.manager.common.model.slack.SlackMessageRoot;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.*;
import com.test32.common.axisj.AxisjGridData;
import com.test32.common.axisj.AxisjGridDataConverter;
import com.test32.common.config.ServerConfigAssistantUtil;
import com.test32.common.model.blockChain.erc20.Erc20KeyPair;
import com.test32.common.paging.PagingData;
import com.test32.common.util.CommonHttpClientUtil;
import com.test32.common.util.CommonStaticUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CustomComponentUtil
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private CustomErc20Util customErc20Util;
    @Autowired private CommonQueryService commonQueryService;
    @Autowired private TokenInfoService tokenInfoService;
    @Autowired private MemberService memberService;
    @Autowired private ProductInfoService productInfoService;
    @Autowired private SysDataService sysDataService;
    @Autowired private TokenPointService tokenPointService;

    public boolean isLoginWebRequestAvailable()
    {
        return configDataManager.getConfigData().getDefaultServerConfigData().getManageLoginWebRequest();
    }
    public BaseResponse returnLoginWebDisabledForUpdate()
    {
        BaseResponse response = new BaseResponse();
        response.setReturnCode(ReturnCode.DISABLED_FOR_SCHEDULED_UPDATE);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    public Integer getErc20PendingNoticeAfterMinute()
    {
        return sysDataService.selectDefault().getSsErc20PendingNoticeM();
    }

    public String makeSlackWithdrawalMessage(Date date, String title, List<String> lineString)
    {
        SlackMessageRoot root = new SlackMessageRoot();
        List<SlackMessageAttachmentWithdrawRequest> attachments = new ArrayList<>();
        root.setAttachments(attachments);

        SlackMessageAttachmentWithdrawRequest attach = new SlackMessageAttachmentWithdrawRequest();
        attachments.add(attach);

        attach.setColor("#36a64f");
        attach.setDate(date);
        List<SlackMessageAttachmentField> fields = new ArrayList<>();
        attach.setFields(fields);
        SlackMessageAttachmentField field = new SlackMessageAttachmentField();
        fields.add(field);
        field.setTitle(title);

        String valueString = "";
        for(String item : lineString)
        {
            if( ! StringUtils.isEmpty(valueString))
                valueString += "\n";
            valueString += item;
        }
        field.setValue(valueString);
        return CommonStaticUtil.convertObjectToJsonWithoutException(root);
    }

    public boolean notifySlackWithdrawRequest(MemberVO memberVO, String to, BigDecimal amount, String symbol, String memo, String title)
    {
        try
        {
            List<String> lineList = new ArrayList<>();
            lineList.add("아이디: " + memberVO.getMbId());
            lineList.add("수량: " + amount + " " + symbol);
            lineList.add("출금주소: " + to);
            lineList.add("메모: " + memo);
            String sendString = makeSlackWithdrawalMessage(new Date(), title, lineList);
            String urlSlackWithdraw = configDataManager.getConfigData().getDefaultServerConfigData().getUrlSlackWithdraw();
            if( ! StringUtils.isEmpty(urlSlackWithdraw))
            {
                CommonHttpClientUtil.postJson(urlSlackWithdraw, sendString);
                return true;
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }
    public boolean notifySlackWithdrawError(String from, String to, String quantity, String memo, String errorMessage)
    {
        try
        {
            List<String> lineList = new ArrayList<>();
            lineList.add("From: " + from);
            lineList.add("출금주소: " + to);
            lineList.add("수량: " + quantity);
            lineList.add("메모: " + memo);
            lineList.add("오류: " + errorMessage);
            String sendString = makeSlackWithdrawalMessage(new Date(), "출금오류", lineList);
            String urlSlackWithdraw = configDataManager.getConfigData().getDefaultServerConfigData().getUrlSlackWithdraw();
            if( ! StringUtils.isEmpty(urlSlackWithdraw))
            {
                CommonHttpClientUtil.postJson(urlSlackWithdraw, sendString);
                return true;
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }

    public boolean notifySlackWithdrawPending(PaymentQueueVO paymentQueueVO)
    {
        try
        {
            String from = "관리자";
            String to = paymentQueueVO.getPqEtTo();
            String memo = paymentQueueVO.getPqEtFrom();
            String finalReturn = paymentQueueVO.getPqEtActualTo();
            String quantity = finalReturn + " " + paymentQueueVO.getPqSymbol();

            SimpleDateFormat sdf = newSimpleDateFormatFull();

            List<String> lineList = new ArrayList<>();
            lineList.add("출금번호: " + paymentQueueVO.getPqIdx());
            lineList.add("요청시간: " + sdf.format(paymentQueueVO.getPqRqDate()));
            lineList.add("From: " + from);
            lineList.add("출금주소: " + to);
            lineList.add("수량: " + quantity);
            lineList.add("메모: " + memo);
            String sendString = makeSlackWithdrawalMessage(new Date(), "ERC20 Pending", lineList);
            String urlSlackWithdraw = configDataManager.getConfigData().getDefaultServerConfigData().getUrlSlackWithdraw();
            if( ! StringUtils.isEmpty(urlSlackWithdraw))
            {
                CommonHttpClientUtil.postJson(urlSlackWithdraw, sendString);
                return true;
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }

    private boolean notifySlack(String type, String data)
    {
        try
        {
            switch (type)
            {
                case CommonConstants.SLACK_TYPE_WITHDRAW_REQUEST:
                case CommonConstants.SLACK_TYPE_WITHDRAW_ERROR:
                {
                    String urlSlackWithdraw = configDataManager.getConfigData().getDefaultServerConfigData().getUrlSlackWithdraw();
                    if( ! StringUtils.isEmpty(urlSlackWithdraw))
                    {
                        String dataJson = "{\"text\":\"" + data + "\"}";
                        CommonHttpClientUtil.postJson(urlSlackWithdraw, dataJson);
                        return true;
                    }
                    break;
                }
                default:
                {
                    logger.warn("out of case.  this code must not be seen.");
                    logger.warn("type: " + type);
                    logger.warn("data: " + data);
                }
            }

        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }

    public BigDecimal getWithdrawalFeeForMultiply(TokenInfoVO tokenInfoVO)
    {
        // input: 10
        String feeString = tokenInfoVO.getTkWithdrawFee();
        if(StringUtils.isEmpty(feeString))
        {
            logger.error("withdrawalFee is not set");
            return null;
        }
        // 0.1
        BigDecimal feeRate = new BigDecimal(feeString).setScale(tokenInfoVO.getTkRoundingPoint(), RoundingMode.HALF_UP).divide(new BigDecimal("100"), tokenInfoVO.getTkRoundingPoint(), RoundingMode.HALF_UP).setScale(tokenInfoVO.getTkRoundingPoint(), RoundingMode.HALF_UP);
        if(feeRate.compareTo(BigDecimal.ZERO) < 0 || feeRate.compareTo(new BigDecimal("100")) > 0)
        {
            logger.error("invalid withdrawalFee");
            return null;
        }
        // return 0.1000
        return feeRate;
    }

    public String generateRandomForNumberCert(int length)
    {
        String result = "";
        for(int i = 0; i < length; i++)
        {
            double dValue = Math.random();
            int iValue = (int)(dValue * 10);
            result += iValue;
        }
        return result;
    }

    public String generateRandomForTicketId(int length)
    {
        String result = "";
        for(int i = 0; i < 12; i++)
        {
            double dValue = Math.random();
            char cValue = (char)((dValue * 26) + 65);   // 대문자
            result += cValue;
        }
        result += "meaninglessSaltAdded1";
        String generated = CommonStaticUtil.createMD5(result);
        return ("id" + generated.substring(2, generated.length())).substring(0, length);
    }
    public String generateRandomForTicketSign(int length)
    {
        String result = "";
        for(int i = 0; i < 12; i++)
        {
            double dValue = Math.random();
            char cValue = (char)((dValue * 26) + 65);   // 대문자
            result += cValue;
        }
        result += "meaninglessSaltAdded2";
        String generated = CommonStaticUtil.createMD5(result);
        return ("sign" + generated.substring(3, generated.length())).substring(0, length);
    }

    public String generateRandomPassword(Integer length)
    {
        String resultData;
        try
        {
            for(int i = 0; i < 100; i++)
            {
                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString().replaceAll("-", "");
                resultData = uuidString.substring(0, length);
                Map<String, Object> parameter = new HashMap<>();
                parameter.put("mbId", resultData);
                List<MemberVO> list = memberService.selectBySearch(parameter);
                if(list.size() == 0)
                {
                    return resultData;
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    private String generateQrString()
    {
        String resultData;
        try
        {
            for(int i = 0; i < 100; i++)
            {
                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString().replaceAll("-", "");

                logger.info(uuidString);
                //resultData = uuidString.substring(0, 4) + "-" + uuidString.substring(4, 8) + "-" + uuidString.substring(8, 16);
                resultData = uuidString.substring(0, 8) + "-" + uuidString.substring(8, 12) + "-" + uuidString.substring(12, 16) + "-" + uuidString.substring(16, 20) + "-" + uuidString.substring(20, 28);

                Map<String, Object> parameter = new HashMap<>();
                parameter.put("mbQrData", resultData);
                List<MemberVO> list = memberService.selectBySearch(parameter);
                if(list.size() == 0)
                {
                    return resultData;
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public String getErc20MultipleWithdrawPrivateKey(String address)
    {
        String baseAccountString = configDataManager.getConfigData().getDefaultServerConfigData().getErc20MultipleWithdrawAccountArray();
        String[] accountArray = baseAccountString.split(",");

        String baseKeyString = configDataManager.getConfigData().getDefaultServerConfigData().getErc20MultipleWithdrawKeyArray();
        String[] keyArray = baseKeyString.split(",");

        if(accountArray.length != keyArray.length)
        {
            logger.error("invalid config data");
            logger.error("baseAccountString: " + baseAccountString + " " + accountArray.length);
            logger.error("baseKeyString: " + baseKeyString + " " + keyArray.length);
        }
        else
        {
            for(int i = 0; i < keyArray.length;i++)
            {
                if(accountArray[i].equalsIgnoreCase(address))
                {
                    return keyArray[i];
                }
            }
        }
        logger.error("failed to find privateKey from config.  address: " + address);
        return null;
    }

    public String getErc20WithdrawPrivateKey()
    {
        return configDataManager.getConfigData().getDefaultServerConfigData().getErc20WithdrawKey();
    }

    public Boolean generateErc20Address(ProductInfoVO productInfoVO)
    {
        try
        {
            ProductInfoVO searched = productInfoService.select(productInfoVO.getPdIdx());
            if( ! StringUtils.isEmpty(productInfoVO.getPdErc20Key()))
                return false;
            ProductInfoVO forUpdate = new ProductInfoVO();
            forUpdate.setPdIdx(productInfoVO.getPdIdx());

            Erc20KeyPair keyPair = customErc20Util.generateKeyPair();
            if(keyPair != null)
            {
                forUpdate.setPdErc20Address(keyPair.getAddress());
                forUpdate.setPdErc20Key(customErc20Util.encodeErc20Key(searched, keyPair.getPrivateKey()));
                if(forUpdate.getPdErc20Key().length() > 128)
                {
                    logger.error("check key length");
                    logger.error("" + keyPair.toString());
                    logger.error("" + forUpdate.getPdErc20Key());
                    return false;
                }
                return productInfoService.update(forUpdate);
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }

    public String getErc20Key(ProductInfoVO productInfoVO)
    {
        if(StringUtils.isEmpty(productInfoVO.getPdErc20Key()))
            return null;
        return customErc20Util.decodeEr20Key(productInfoVO, productInfoVO.getPdErc20Key());
    }

    public void generateMissingTokenPointForMember(MemberVO memberVO)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tpMbIdx", memberVO.getMbIdx());

        List<String> havingSymbolList = new ArrayList<>();
        List<TokenPointVOForApi> allList = tokenPointService.selectBySearchForApi(parameter);
        for(TokenPointVOForApi item : allList)
            havingSymbolList.add(item.getSymbol().toUpperCase());

        List<TokenInfoVO> defaultGenerationList = tokenInfoService.selectDefaultGenerationList();
        for(TokenInfoVO generationItem : defaultGenerationList)
        {
            if( ! havingSymbolList.contains(generationItem.getTkSymbol().toUpperCase()))
                tokenPointService.createNew(memberVO, generationItem);
        }
    }

    public Date getDatabaseNow()
    {
        try {
            SimpleDateFormat dateFormat = newSimpleDateFormatFull();
            return dateFormat.parse(commonQueryService.selectNowString());
        }
        catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public AxisjGridData returnEmptyAxisGridData(String message)
    {
        PagingData pagingData = new PagingData();
        pagingData.setCurrentPage(1);
        pagingData.setTotalPageSize(1);
        pagingData.setCurrentPageRowCount(0);
        pagingData.setObject(new ArrayList());
        pagingData.setPageSize(0);

        if (StringUtils.isEmpty(message))
            return AxisjGridDataConverter.Convert(pagingData);
        else
            return AxisjGridDataConverter.Convert(pagingData, message);
    }

    public SimpleDateFormat newSimpleDateFormatFull()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone(CommonConstants.CONFIG_TIME_ZONE));
        return dateFormat;
    }

    public SimpleDateFormat newSimpleDateFormatHalf()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone(CommonConstants.CONFIG_TIME_ZONE));
        return dateFormat;
    }

    private String getUploadPathRoot()
    {
        String rootPath;
        if (ServerConfigAssistantUtil.isWindows())
            rootPath = "";
        else
            rootPath = ServerConfigAssistantUtil.LINUX_PATH_PREFIX;
        return rootPath + configDataManager.getConfigData().getDefaultServerConfigData().getUploadFilePath();
    }

    public String getReleasePathRoot()
    {
        return configDataManager.getConfigData().getDefaultServerConfigData().getReleaseFilePath();
    }

    public String getUploadPathForNotice()
    {
        return getUploadPathRoot() + CommonConstants.FILE_PATH_NOTICE;
    }
    public String getUploadPathForFeed()
    {
        return getUploadPathRoot() + CommonConstants.FILE_PATH_FEED;
    }
    public String getUploadPathForBanner()
    {
        return getUploadPathRoot() + CommonConstants.FILE_PATH_BANNER;
    }
    public String getUploadPathForProduct()
    {
        return getUploadPathRoot() + CommonConstants.FILE_PATH_PRODUCT;
    }
    public String getUploadPathForQrCode()
    {
        return getUploadPathRoot() + CommonConstants.FILE_PATH_QR_CODE;
    }
    public String getUploadPathForUserKyc() { return getUploadPathRoot() + CommonConstants.FILE_PATH_USER_KYC; }



    public String getReleasePathForUserKyc() { return getReleasePathRoot() + CommonConstants.FILE_PATH_USER_KYC; }
    public String getReleasePathForFeed() { return getReleasePathRoot() + CommonConstants.FILE_PATH_FEED; }
}