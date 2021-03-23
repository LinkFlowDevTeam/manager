package com.linkFlow.manager.admin.web.management;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.define.OperatorState;
import com.linkFlow.manager.common.model.define.OperatorType;
import com.linkFlow.manager.common.model.vo.OperatorVO;
import com.linkFlow.manager.common.service.OperatorService;
import com.linkFlow.manager.common.util.SecurityComponentUtil;
import com.test32.common.axisj.AxisjGridData;
import com.test32.common.axisj.AxisjGridDataConverter;
import com.test32.common.paging.PagingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping(value = "/management/operator")
public class OperatorController
{
    @Autowired
    private SecurityComponentUtil securityComponentUtil;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private OperatorService operatorService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/changeLoginPassword", method = RequestMethod.GET)
    public String changeLoginPassword()
    {
        return "operator/mLoginOperatorChangePassword";
    }

    @RequestMapping(value = "/changeLoginPassword", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse changeLoginPassword(@RequestParam(value = "before", required = false) String before, @RequestParam(value = "after", required = false) String after)
    {
        BaseResponse response = new BaseResponse();
        OperatorVO operatorVO = securityComponentUtil.getLoginOperatorRenewed();

        String currentPrior = operatorVO.getOpPwd();

        // 어드민이 아니면 제한
        if( ! securityComponentUtil.isSystemAdmin())
        {
            if( ! passwordEncoder.matches(before, currentPrior))
            {
                response.setReturnCode(ReturnCode.PASSWORD_NOT_MATCH);
                response.setDescription(response.getReturnCode().getMessage());
                return response;
            }
            if(after.length() < 8)
            {
                response.setReturnCode(ReturnCode.PASSWORD_LENGTH_SHORT);
                response.setDescription(response.getReturnCode().getMessage() + " - minimum length is 8");
                return response;
            }
        }

        operatorVO.setOpPwd(after);
        if( ! operatorService.update(operatorVO))
        {
            logger.error("failed to update operator");
            logger.error(operatorVO.toString());
            response.setReturnCode(ReturnCode.INTERNAL_ERROR);
            response.setDescription(response.getReturnCode().getMessage());
            return response;
        }

        response.setReturnCode(ReturnCode.SUCCESS);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('RL_SYSTEM_ADMIN')")
    public String showOperatorList(Model model)
    {
        model.addAttribute("types", OperatorType.values());
        model.addAttribute("states", OperatorState.values());
        return "/management/operator/operatorList";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('RL_SYSTEM_ADMIN')")
    public AxisjGridData getOperatorListForAxisjGrid(@RequestParam(value = "pageNo", required = true) String pageNo, @RequestParam(value = "pageSize", required = true) String pageSize, @RequestParam Map<String, Object> paramsMap)
    {
        logger.debug("pageNo:" + pageNo + ", pageSize:" + pageSize + ", " +  paramsMap.toString());
        PagingData pagingData = operatorService.selectPagingBySearch(paramsMap);
        return AxisjGridDataConverter.Convert(pagingData);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('RL_SYSTEM_ADMIN')")
    public String create(Model model)
    {
        OperatorVO operatorVO = new OperatorVO();
        model.addAttribute(operatorVO);
        model.addAttribute("state", OperatorState.values());
        model.addAttribute("type", OperatorType.values());
        model.addAttribute("pageMode", "create");
        return "management/operator/mOperatorCreate";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('RL_SYSTEM_ADMIN')")
    public BaseResponse create(@ModelAttribute OperatorVO operatorVO)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        try
        {
            operatorVO.setOpType(OperatorType.AGENT);
            operatorVO.setOpLevel(1);
            operatorService.insert(operatorVO);
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
    @PreAuthorize("hasAuthority('RL_SYSTEM_ADMIN')")
    public String modifyChild(@PathVariable(value = "idx") int idx, Model model)
    {
        OperatorVO operatorVO = operatorService.select(idx);
        operatorVO.setOpPwd("");
        model.addAttribute("previousOpType", operatorVO.getOpType());
        model.addAttribute("state", OperatorState.values());
        model.addAttribute("type", OperatorType.values());

        model.addAttribute(operatorVO);
        model.addAttribute("pageMode", "modify");
        return "management/operator/mOperatorCreate";
    }

    @RequestMapping(value = "/modify/{opIdx}", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('RL_SYSTEM_ADMIN')")
    public BaseResponse modifyChild(@PathVariable(value = "opIdx") int opIdx, @ModelAttribute @Valid OperatorVO operator)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        try
        {
            // 업데이트 안시킬것들
            operator.setOpParentIdx(null);
            operator.setOpLevel(null);
            operator.setOpId(null);
            operator.setOpPoint(null);

            operatorService.update(operator);
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

    @RequestMapping(value = "/delete/{idx}")
    @ResponseBody
    @PreAuthorize("hasAuthority('RL_SYSTEM_ADMIN')")
    public BaseResponse deleteOperator(@PathVariable(value = "idx") int idx)
    {
        BaseResponse response = new BaseResponse();
        operatorService.delete(idx);
        response.setReturnCode(ReturnCode.SUCCESS);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }
}