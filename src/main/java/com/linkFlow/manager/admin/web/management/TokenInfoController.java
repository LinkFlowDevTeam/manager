package com.linkFlow.manager.admin.web.management;

import com.linkFlow.manager.admin.model.response.ResDecimal;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.TokenInfoVO;
import com.linkFlow.manager.common.service.TokenInfoService;
import com.linkFlow.manager.common.util.CustomErc20Util;
import com.test32.common.axisj.AxisjGridData;
import com.test32.common.axisj.AxisjGridDataConverter;
import com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcBalanceOf;
import com.test32.common.paging.PagingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping(value = "/management/tokenInfo")
public class TokenInfoController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CustomErc20Util customErc20Util;
    @Autowired private TokenInfoService tokenInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model)
    {
        return "/management/tokenInfo/tokenInfoList";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AxisjGridData getListForAxisjGrid(@RequestParam Map<String, Object> paramsMap)
    {
        paramsMap.put("orderColumn", "tk_idx");
        paramsMap.put("order", "DESC");

        PagingData pagingData = tokenInfoService.selectPagingBySearch(paramsMap);
        return AxisjGridDataConverter.Convert(pagingData);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model)
    {
        TokenInfoVO tokenInfoVO = new TokenInfoVO();
        model.addAttribute("modalTokenInfoVO", tokenInfoVO);
        model.addAttribute("pageMode", "create");
        return "management/tokenInfo/mTokenInfoCreate";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse create(@ModelAttribute TokenInfoVO tokenInfoVO)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        try
        {
            if(tokenInfoService.selectBySymbol(tokenInfoVO.getTkSymbol()) != null)
                returnCode = ReturnCode.DATA_ALREADY_EXISTS;
            else if(tokenInfoService.insert(tokenInfoVO))
                returnCode = ReturnCode.SUCCESS;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }


    @RequestMapping(value = "/modify/{idx}", method = RequestMethod.GET)
    public String modify(@PathVariable(value = "idx") Integer idx, Model model)
    {
        TokenInfoVO tokenInfoVO = tokenInfoService.select(idx);
        model.addAttribute("modalTokenInfoVO", tokenInfoVO);
        model.addAttribute("pageMode", "modify");
        return "management/tokenInfo/mTokenInfoCreate";
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse modify(@ModelAttribute TokenInfoVO tokenInfoVO)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        try
        {
            if(tokenInfoService.update(tokenInfoVO))
                returnCode = ReturnCode.SUCCESS;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    @RequestMapping(value = "/updateMinWithdrawAmount/{tkIdx}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updateMinWithdrawAmount(@PathVariable(value = "tkIdx") Integer tkIdx, @RequestParam(value = "data", required = false) String data)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            TokenInfoVO forUpdate = new TokenInfoVO();
            forUpdate.setTkIdx(tkIdx);
            forUpdate.setTkMinWithdrawAmount(data);
            if(tokenInfoService.update(forUpdate))
                returnCode = ReturnCode.SUCCESS;
            else
                logger.error("update failed");
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @RequestMapping(value = "/updateWithdrawFee/{tkIdx}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updateWithdrawFee(@PathVariable(value = "tkIdx") Integer tkIdx, @RequestParam(value = "data", required = false) String data)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            TokenInfoVO forUpdate = new TokenInfoVO();
            forUpdate.setTkIdx(tkIdx);
            forUpdate.setTkWithdrawFee(data);
            if(tokenInfoService.update(forUpdate))
                returnCode = ReturnCode.SUCCESS;
            else
                logger.error("update failed");
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @RequestMapping(value = "/updateByKey/{key}/{tkIdx}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updateWithdrawFee(@PathVariable(value = "key") String key, @PathVariable(value = "tkIdx") Integer tkIdx, @RequestParam(value = "data", required = false) String data)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            boolean isValidRequest = true;

            TokenInfoVO forUpdate = new TokenInfoVO();
            forUpdate.setTkIdx(tkIdx);

            switch (key)
            {
                case "tkMinWithdrawAmount":
                    forUpdate.setTkMinWithdrawAmount(data);
                    break;
                case "tkWithdrawFee":
                    forUpdate.setTkWithdrawFee(data);
                    break;
                case "tkGasFeeDepositKrw":
                    forUpdate.setTkGasFeeDepositKrw(data);
                    break;
                default:
                    isValidRequest = false;
                    logger.error("key not defined.  key: " + key);
                    break;
            }

            if(isValidRequest)
            {
                if(tokenInfoService.update(forUpdate))
                    returnCode = ReturnCode.SUCCESS;
                else
                    logger.error("update failed");
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @RequestMapping(value = "/toggleByKey/{key}/{tkIdx}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse toggleByKey(@PathVariable(value = "key") String key, @PathVariable(value = "tkIdx") Integer tkIdx)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            boolean isValidRequest = true;

            TokenInfoVO tokenInfoVO = tokenInfoService.select(tkIdx);

            TokenInfoVO forUpdate = new TokenInfoVO();
            forUpdate.setTkIdx(tkIdx);

            switch (key)
            {
                case "tkAutoWithdraw":
                {
                    if(1 == tokenInfoVO.getTkAutoWithdraw())
                        forUpdate.setTkAutoWithdraw(0);
                    else
                        forUpdate.setTkAutoWithdraw(1);
                    break;
                }
                case "tkLockPurchase":
                {
                    if(1 == tokenInfoVO.getTkLockPurchase())
                        forUpdate.setTkLockPurchase(0);
                    else
                        forUpdate.setTkLockPurchase(1);
                    break;
                }
                default:
                    isValidRequest = false;
                    logger.error("key not defined.  key: " + key);
                    break;
            }

            if(isValidRequest)
            {
                if(tokenInfoService.update(forUpdate))
                    returnCode = ReturnCode.SUCCESS;
                else
                    logger.error("update failed");
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }


    @RequestMapping(value = "/refreshErc20Point/{idx}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse refreshErc20Point(@PathVariable(value = "idx") Integer idx)
    {
        return tokenInfoService.refreshErc20Point(idx);
    }

    @RequestMapping(value = "/getEthPoint/{address}", method = RequestMethod.POST)
    @ResponseBody
    public ResDecimal getEthPoint(@PathVariable(value = "address") String address)
    {
        ResDecimal response = new ResDecimal();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            JsonRpcBalanceOf jsonRpcBalanceOf = customErc20Util.ethBalance(address);
            if(jsonRpcBalanceOf == null)
            {
                returnCode = ReturnCode.ERC20_API_ERROR;
                extraMessage = "response is null";
            }
            else
            {
                if(StringUtils.isEmpty(jsonRpcBalanceOf.getResult()))
                {
                    returnCode = ReturnCode.ERC20_API_ERROR;
                    extraMessage = "getResult is null";
                }
                else
                {
                    if("0x".equalsIgnoreCase(jsonRpcBalanceOf.getResult()))
                    {
                        logger.info("no balance");
                        returnCode = ReturnCode.SUCCESS;
                    }
                    else
                    {
                        BigDecimal amount = customErc20Util.convertHexToDecimalWithTokenInfo(jsonRpcBalanceOf.getResult(), 18);
                        logger.info(jsonRpcBalanceOf.getResult() + "  " + amount);
                        response.setAmount(amount);
                        returnCode = ReturnCode.SUCCESS;
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }
}