package com.linkFlow.manager.admin.web.management;

import com.linkFlow.manager.common.model.vo.PaymentQueueVO;
import com.linkFlow.manager.common.service.PaymentQueueService;
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
@RequestMapping(value = "/management/paymentQueue")
public class PaymentQueueController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private PaymentQueueService paymentQueueService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model)
    {
        return "/management/paymentQueue/paymentQueueList";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AxisjGridData getListForAxisjGrid(@RequestParam Map<String, Object> paramsMap)
    {
        if( ! paramsMap.containsKey("orderColumn") &&  ! paramsMap.containsKey("order")) { paramsMap.put("orderColumn", "pq_idx"); paramsMap.put("order", "DESC"); }
        PagingData pagingData = paymentQueueService.selectPagingBySearch(paramsMap);
        return AxisjGridDataConverter.Convert(pagingData);
    }

    @RequestMapping(value = "/info/{idx}", method = RequestMethod.GET)
    public String info(@PathVariable(value = "idx") Long idx, Model model)
    {
        PaymentQueueVO paymentQueueVO = paymentQueueService.select(idx);
        model.addAttribute(paymentQueueVO);
        model.addAttribute("pageMode", "info");
        return "management/paymentQueue/mPaymentQueueInfo";
    }
}