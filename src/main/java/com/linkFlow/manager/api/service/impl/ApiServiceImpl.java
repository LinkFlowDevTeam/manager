package com.linkFlow.manager.api.service.impl;

import com.linkFlow.manager.api.model.request.*;
import com.linkFlow.manager.api.model.response.*;
import com.linkFlow.manager.api.service.ApiService;
import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.*;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.linkFlow.manager.common.util.CustomErc20Util;
import com.linkFlow.manager.common.util.WalletTransactionIdGenerator;
import com.test32.common.paging.PagingData;
import com.test32.common.util.CommonDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "walletApiService")
public class ApiServiceImpl implements ApiService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CustomErc20Util customErc20Util;
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private MemberService memberService;
    @Autowired private MemberTierService memberTierService;
    @Autowired private CommonTicketService commonTicketService;
    @Autowired private TokenPointService tokenPointService;
    @Autowired private TransactionService transactionService;
    @Autowired private PaymentQueueService paymentQueueService;
    @Autowired private ProductInfoService productInfoService;
    @Autowired private TokenInfoService tokenInfoService;
    @Autowired private NoticeService noticeService;
    @Autowired private SysDataService sysDataService;
    @Autowired private RandomTicketService randomTicketService;
    @Autowired private CommonWorkQueueService commonWorkQueueService;
    @Autowired private LfUserService lfUserService;
    @Autowired private ProductUserService productUserService;
    @Autowired private DailyProductSummaryService dailyProductSummaryService;

    void appendSearchTimeStringToMap(Map<String, Object> parameter, String start, String end)
    {
        if( ! StringUtils.isEmpty(start))
        {
            if(start.length() == 10)
                parameter.put("searchStartDate", start + " 00:00:00");
            else
                parameter.put("searchStartDate", start);
        }

        if( ! StringUtils.isEmpty(end))
        {
            if(end.length() == 10)
                parameter.put("searchEndDate", end + " 23:59:59");
            else
                parameter.put("searchEndDate", end);
        }
    }


    @Override
    public ResSystemInfo systemInfo()
    {
        ResSystemInfo response = new ResSystemInfo();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        try
        {
            response.setData(sysDataService.selectByIndexForApi());
            returnCode = ReturnCode.SUCCESS;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    @Override
    public ResCert certRequest(ReqCert req)
    {
        ResCert response = new ResCert();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        try
        {
            String key = WalletTransactionIdGenerator.getInstance().generateUniqueTransactionIdByServerTag("");
            response.setCert(randomTicketService.generate(req.getAddress(), key).getRtKey());
            returnCode = ReturnCode.SUCCESS;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    @Override
    public ResUserInfo userInfo(ReqUserAddressOnly auth)
    {
        ResUserInfo response = new ResUserInfo();
        MemberVO memberVO = memberService.selectById(auth.getAddress());
        if(memberVO == null)
        {
            MemberVOForApi searched = new MemberVOForApi();
            searched.setCreateDate(null);
            searched.setTier(0);
            searched.setId(auth.getAddress());
            response.setData(searched);
            response.setReturnCodeAndDescription(ReturnCode.SUCCESS);
        }
        else
        {
            MemberVOForApi searched = memberService.selectForApi(memberVO.getMbIdx());
            if(searched != null)
            {
                response.setData(searched);
                response.setReturnCodeAndDescription(ReturnCode.SUCCESS);
            }
            else{
                response.setReturnCodeAndDescription(ReturnCode.ACCOUNT_NOT_FOUND);
            }
        }
        return response;
    }

    @Override
    public ResUserToken userToken(ReqUserAddressOnly auth, ReqTokenSymbol req)
    {
        ResUserToken response = new ResUserToken(ReturnCode.SUCCESS);

        MemberVO memberVO = memberService.selectById(auth.getAddress());
        if(memberVO == null)
        {
            response.setList(new ArrayList<>());
        }
        else
        {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("tpMbIdx", memberVO.getMbIdx());

            response.setList(tokenPointService.selectBySearchForApi(parameter));
        }

        return response;
    }

    @Override
    public ResUserLf userLf(ReqUserAddressOnly auth)
    {
        ResUserLf response = new ResUserLf(ReturnCode.SUCCESS);
        MemberVO memberVO = memberService.selectById(auth.getAddress());
        if(memberVO == null)
        {
            response.setList(new ArrayList<>());
        }
        else
        {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("luMbIdx", memberVO.getMbIdx());
            parameter.put("luState", LfUserService.STATE_ACTIVE);
            response.setList(lfUserService.selectBySearchForApi(parameter));
        }
        return response;
    }

    @Override
    public ResUserProduct userProduct(ReqUserProduct req)
    {
        ResUserProduct response = new ResUserProduct(ReturnCode.SUCCESS);
        MemberVO memberVO = memberService.selectById(req.getAddress());
        if(memberVO == null)
        {
            response.setList(new ArrayList<>());
        }
        else
        {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("puMbIdx", memberVO.getMbIdx());
            if(req.getProductId() != null)
                parameter.put("puPdIdx", req.getProductId());
            response.setList(productUserService.selectBySearchForApi(parameter));
        }
        return response;
    }

    @Override
    public ResUserInfo userInfo(ReqUserAfterAuth auth)
    {
        return null;
    }

    @Override
    public ResUserToken userToken(ReqUserAfterAuth auth, ReqTokenSymbol req)
    {

        return null;
    }


    @Override
    @Transactional("apiTransactionManager")
    public BaseResponse userWithdraw(ReqUserWithdraw req)
    {
        req.trimRequest();

        // toLower
        if( ! StringUtils.isEmpty(req.getTo()))
            req.setTo(req.getTo().toLowerCase());

        try
        {
            if( ! customErc20Util.isValidSignature(req.getAddress(), req.getMessage(), req.getSignature()))
            {
                logger.warn("invalid request: " + req);
                return new ResTicketRequest(ReturnCode.BAD_REQUEST);
            }

            MemberVO memberVO = memberService.selectById(req.getAddress());

            TokenInfoVO targetToken = tokenInfoService.selectBySymbol(req.getSymbol());
            if (targetToken == null)
            {
                logger.info("token not registered. " + req.toString());
                return new BaseResponse(ReturnCode.INVALID_REQUEST_DATA);
            }
            else if (targetToken.getTkLockWithdraw() == 1)
            {
                logger.info("withdrawal is limited by config. " + req.toString());
                return new BaseResponse(ReturnCode.TOKEN_WITHDRAWAL_LOCKED);
            }

            try
            {
                BigDecimal amount = new BigDecimal(req.getAmount());
                if (amount.compareTo(BigDecimal.ZERO) <= 0)
                {
                    return new BaseResponse(ReturnCode.BAD_REQUEST);
                }

                boolean isEnoughValue = false;
                TokenPointVO tokenPointVO = tokenPointService.selectByMemberTokenSymbol(memberVO.getMbIdx(), req.getSymbol());
                if (tokenPointVO != null) {
                    if (tokenPointVO.getTpPoint().compareTo(new BigDecimal(req.getAmount())) >= 0)
                        isEnoughValue = true;
                }
                if (!isEnoughValue) {
                    return new BaseResponse(ReturnCode.OUT_OF_BALANCE);
                }

                String serverTag = configDataManager.getConfigData().getDefaultServerConfigData().getServerTag();
                String uniqueId = WalletTransactionIdGenerator.getInstance().generateUniqueTransactionIdByServerTag(serverTag);
                if (StringUtils.isEmpty(uniqueId)) {
                    return new BaseResponse(ReturnCode.SYSTEM_IS_BUSY_TRY_AGAIN);
                }


                if (paymentQueueService.insertWithdrawalRequest(memberVO, tokenPointVO, amount, uniqueId, req.getTo(), req.getMemo()))
                {
                    if (targetToken.getTkAutoWithdraw() == 0) {
                        // 자동출금 아닐경우만 전송
                        customComponentUtil.notifySlackWithdrawRequest(memberVO, req.getTo(), amount, req.getSymbol(), req.getMemo(), "출금요청");
                    }

                    String to = req.getTo();
                    String finalReturn = req.getAmount();
                    String memo = req.getMemo();
                    String quantity = finalReturn + " " + req.getSymbol();
                    if (StringUtils.isEmpty(to) || !to.toLowerCase().startsWith("0x")) {
                        customComponentUtil.notifySlackWithdrawError("관리자", to, quantity, memo, "invalid ERC20 address");
                    }
                    return new BaseResponse(ReturnCode.SUCCESS);
                }
                else
                {
                    Map<String, Object> searchParam2 = new HashMap<>();
                    searchParam2.put("pqPaymentType", PaymentQueueService.TRANSFER_TYPE_WITHDRAW);
                    searchParam2.put("pqUniqueId", uniqueId);

                    List<PaymentQueueVO> subList = paymentQueueService.selectBySearch(searchParam2);
                    if (subList.size() > 0) {
                        return new BaseResponse(ReturnCode.PRIOR_REQUEST_EXIST);
                    } else {
                        // db insert error
                        logger.error("failed to insert to payment queue. ");
                        return new BaseResponse(ReturnCode.INTERNAL_ERROR);
                    }
                }
            } catch (Exception subE) {
                logger.error(subE.toString());
                return new BaseResponse(ReturnCode.BAD_REQUEST);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            logger.error(e.toString());
            return new BaseResponse(ReturnCode.INTERNAL_ERROR).setExtraMessage(e.toString());
        }
    }

    @Override
    public ResTicketRequest ticketRequest(ReqUserDefaultAuth req)
    {
        try
        {
            if( ! customErc20Util.isValidSignature(req.getAddress(), req.getMessage(), req.getSignature()))
            {
                logger.warn("invalid request: " + req);
                return new ResTicketRequest(ReturnCode.BAD_REQUEST);
            }

            MemberVO memberVO = memberService.selectById(req.getAddress());
            if(memberVO == null)
            {
                memberVO = new MemberVO();
                memberVO.setMbId(req.getAddress());
                if( ! memberService.insert(memberVO))
                {
                    logger.error("failed to: memberService.insert");
                }
                memberVO = memberService.selectById(req.getAddress());
            }
            if(memberVO == null)
            {
                return new ResTicketRequest(ReturnCode.INTERNAL_ERROR);
            }

            CommonTicketVO commonTicketVO = commonTicketService.generate(memberVO.getMbIdx());
            if(commonTicketVO == null) {
                logger.error("failed to generate ticket");
                return new ResTicketRequest(ReturnCode.TICKET_GENERATE_FAILED);
            }

            ResTicketRequest resTicketRequest = new ResTicketRequest(ReturnCode.SUCCESS);
            resTicketRequest.setId("" + commonTicketVO.getTkUserId());
            resTicketRequest.setTicketId(commonTicketVO.getTkSign());
            return resTicketRequest;
        }
        catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            return new ResTicketRequest(ReturnCode.INTERNAL_ERROR);
        }
    }

    @Override
    public ResTokenInfo tokenInfoList(String symbol)
    {
        ResTokenInfo response = new ResTokenInfo();
        response.setData(tokenInfoService.selectBySearchForInfoApi(symbol));
        response.setReturnCode(ReturnCode.SUCCESS);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    @Override
    public ResTokenPrice tokenPriceList(String symbol)
    {
        ResTokenPrice response = new ResTokenPrice();
        response.setData(tokenInfoService.selectBySearchForPriceApi(symbol));
        response.setReturnCode(ReturnCode.SUCCESS);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    @Override
    public ResTokenChartInfo tokenChartList(String symbol)
    {
        ResTokenChartInfo response = new ResTokenChartInfo();
        response.setData(tokenInfoService.selectBySearchForChartApi(symbol));
        response.setReturnCode(ReturnCode.SUCCESS);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    @Override
    public ResAssetHistory assetHistory(ReqAssetHistory req)
    {
        Map<String, Object> parameter = new HashMap<>();

        if(StringUtils.isEmpty(req.getSearchStart()))
        {
            Date today = customComponentUtil.getDatabaseNow();
            Date startDate = CommonDateUtil.addDay(today, -7);
            SimpleDateFormat sdf = customComponentUtil.newSimpleDateFormatHalf();
            req.setSearchStart(sdf.format(startDate));
        }

        appendSearchTimeStringToMap(parameter, req.getSearchStart(), req.getSearchEnd());

        List<DailyProductSummaryVOForApi> list = dailyProductSummaryService.selectBySearchForApi(parameter);

        ResAssetHistory response = new ResAssetHistory();
        response.setData(list);
        response.setReturnCode(ReturnCode.SUCCESS);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    @Override
    public ResAssetHistoryConverted assetHistoryConverted(ReqAssetHistory req)
    {
        Map<String, Object> parameter = new HashMap<>();

        if(StringUtils.isEmpty(req.getSearchStart()))
        {
            Date today = customComponentUtil.getDatabaseNow();
            Date startDate = CommonDateUtil.addDay(today, -7);
            SimpleDateFormat sdf = customComponentUtil.newSimpleDateFormatHalf();
            req.setSearchStart(sdf.format(startDate));
        }

        appendSearchTimeStringToMap(parameter, req.getSearchStart(), req.getSearchEnd());

        ResAssetHistoryConverted response = new ResAssetHistoryConverted();

        {
            String targetSymbol = "USDT";
            parameter.put("dsTkSymbol", targetSymbol);
            List<DailyProductSummaryVOForApi> list = dailyProductSummaryService.selectBySearchForApi(parameter);
            response.setLfUSDT(convertAssetHistory(list));
            response.setDATE(convertAssetDate(list));
        }

        {
            String targetSymbol = "USDC";
            parameter.put("dsTkSymbol", targetSymbol);
            List<DailyProductSummaryVOForApi> list = dailyProductSummaryService.selectBySearchForApi(parameter);
            response.setLfUSDC(convertAssetHistory(list));
        }

        response.setReturnCode(ReturnCode.SUCCESS);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    String convertAssetHistory(List<DailyProductSummaryVOForApi> list)
    {
        String result = "";
        for(DailyProductSummaryVOForApi item : list)
        {
            if(StringUtils.isEmpty(result))
                result += "[";
            else
                result += ",";
            result += item.getSumAccumulatedTotal().toPlainString();
        }
        return result + "]";
    }

    String convertAssetDate(List<DailyProductSummaryVOForApi> list)
    {
        String result = "";
        for(DailyProductSummaryVOForApi item : list)
        {
            if(StringUtils.isEmpty(result))
                result += "[";
            else
                result += ",";
            result += "\'" + item.getDate() + "\'";
        }
        return result + "]";
    }


    @Override
    public ResTier tierList()
    {
        ResTier response = new ResTier();
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("mtState", 1);
        parameter.put("orderColumn", "mt_tier");
        parameter.put("order", "ASC");
        response.setData(memberTierService.selectBySearchForApi(parameter));
        response.setReturnCode(ReturnCode.SUCCESS);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    @Override
    public ResProductList productList(ReqProductList req)
    {
        try
        {
            Map<String, Object> parameter = new HashMap<>();

            if( ! StringUtils.isEmpty(req.getAddress()))
            {
                MemberVO memberVO = memberService.selectById(req.getAddress());
                if(memberVO != null)
                {
                    parameter.put("puMbIdx", memberVO.getMbIdx());
                }
            }

            ResProductList resProductList = new ResProductList(ReturnCode.SUCCESS);
            resProductList.setData(productInfoService.selectBySearchForApi(parameter));
            return resProductList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error(e.toString());
            ResProductList resProductList = new ResProductList(ReturnCode.INTERNAL_ERROR);
            resProductList.setExtraMessage(e.toString());
            return resProductList;
        }
    }

    @Override
    public PagingData selectPagingBySearchForTransaction(Map<String, Object> parameter)
    {
        return null;
    }

    @Override
    public PagingData selectPagingBySearchForNotice(Map<String, Object> parameter)
    {
        return noticeService.selectPagingBySearchForApi(parameter);
    }

    @Override
    public ResUserTransactionDetail userLogDetail(Long idx)
    {
        ResUserTransactionDetail response = new ResUserTransactionDetail();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String eosMessage = null;

        try
        {
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error(e.toString());
            eosMessage = e.toString();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(eosMessage);
        return response;
    }
}