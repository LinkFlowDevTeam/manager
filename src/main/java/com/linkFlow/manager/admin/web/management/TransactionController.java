package com.linkFlow.manager.admin.web.management;

import com.linkFlow.manager.common.model.vo.TransactionVO;
import com.linkFlow.manager.common.service.TransactionService;
import com.test32.common.axisj.AxisjGridData;
import com.test32.common.axisj.AxisjGridDataConverter;
import com.test32.common.paging.PagingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/management/transaction")
public class TransactionController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model)
    {
        return "/management/transaction/transactionList";
    }

    @RequestMapping(value = "/format", method = RequestMethod.GET)
    public String customFormatList(Model model)
    {
        return "/management/transaction/transactionList2";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AxisjGridData getListForAxisjGrid(@RequestParam Map<String, Object> paramsMap)
    {
        if( ! paramsMap.containsKey("orderColumn"))
        {
            paramsMap.put("orderColumn", "tr_idx");
            paramsMap.put("order", "DESC");
        }

        PagingData pagingData = transactionService.selectPagingBySearch(paramsMap);
        return AxisjGridDataConverter.Convert(pagingData);
    }

    @RequestMapping(value = "/info/{idx}", method = RequestMethod.GET)
    public String info(@PathVariable(value = "idx") Long idx, Model model)
    {
        TransactionVO transactionVO = transactionService.select(idx);
        model.addAttribute(transactionVO);
        model.addAttribute("pageMode", "info");
        return "management/transaction/mTransactionInfo";
    }
}