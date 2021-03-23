package com.linkFlow.manager.api.web;

import com.linkFlow.manager.api.model.request.*;
import com.linkFlow.manager.api.model.response.*;
import com.linkFlow.manager.api.service.ApiService;
import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1")
@Slf4j
public class ApiV1Controller
{
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private ApiService apiService;

    private String getServiceId() { return configDataManager.getConfigData().getDefaultServerConfigData().getServiceId(); }
    private BaseResponse onApiRestricted() { return new BaseResponse(ReturnCode.API_RESTRICTED); }


    @Operation(summary = "시스템 정보", tags = { "common" } ) @RequestMapping(method = {RequestMethod.POST}, value = "/system/info") @ResponseBody public ResSystemInfo systemInfo() { return apiService.systemInfo(); }
    @Operation(summary = "Tier 정보", tags = { "common" } ) @RequestMapping(method = {RequestMethod.POST}, value = "/tier/info") @ResponseBody public ResTier tierList() { return apiService.tierList(); }
    @Operation(summary = "상품 리스트", tags = { "common" }) @RequestMapping(method = {RequestMethod.POST}, value = "/product/list") public ResProductList productList(@Valid @RequestBody ReqProductList req) { return apiService.productList(req); }

    @Operation(summary = "토큰별 정보(리스트)", tags = { "common" } ) @RequestMapping(method = {RequestMethod.POST}, value = "/token/info") @ResponseBody public ResTokenInfo tokenInfoList(@RequestParam(value = "symbol", required = false) String symbol) { return apiService.tokenInfoList(symbol); }
    @Operation(summary = "토큰별 가격(리스트)", tags = { "common" } ) @RequestMapping(method = {RequestMethod.POST}, value = "/token/price") @ResponseBody public ResTokenPrice tokenPriceList(@RequestParam(value = "symbol", required = false) String symbol) { return apiService.tokenPriceList(symbol); }
    @Operation(summary = "토큰별 차트(리스트)", tags = { "common" } ) @RequestMapping(method = {RequestMethod.POST}, value = "/token/chart") @ResponseBody public ResTokenChartInfo tokenChart(@RequestParam(value = "symbol", required = false) String symbol) { return apiService.tokenChartList(symbol); }

    @Operation(summary = "Asset History", tags = { "common" }) @RequestMapping(method = {RequestMethod.POST}, value = "/asset/history") public ResAssetHistory assetHistory(@Valid @RequestBody ReqAssetHistory req) { return apiService.assetHistory(req); }
    @Operation(summary = "Asset History", tags = { "common" }) @RequestMapping(method = {RequestMethod.POST}, value = "/asset/history/converted") public ResAssetHistoryConverted assetHistoryConverted(@Valid @RequestBody ReqAssetHistory req) { return apiService.assetHistoryConverted(req); }


    @Operation(summary = "유저 정보", tags = { "user" }) @RequestMapping(method = {RequestMethod.POST}, value = "/user/info" ) public ResUserInfo userInfo(@Valid @RequestBody ReqUserAddressOnly auth) { return apiService.userInfo(auth); }
    @Operation(summary = "유저 토큰", tags = { "user" }) @RequestMapping(method = {RequestMethod.POST}, value = "/user/token") public ResUserToken userToken(@Valid @RequestBody ReqUserAddressOnly auth) { return apiService.userToken(auth, null); }

    @Operation(summary = "유저 LF", tags = { "user" }) @RequestMapping(method = {RequestMethod.POST}, value = "/user/lf") public ResUserLf userLf(@Valid @RequestBody ReqUserAddressOnly auth) { return apiService.userLf(auth); }
    @Operation(summary = "유저 Product", tags = { "user" }) @RequestMapping(method = {RequestMethod.POST}, value = "/user/product") public ResUserProduct userProduct(@Valid @RequestBody ReqUserProduct req) { return apiService.userProduct(req); }



    @Operation(summary = "Cert 요청", tags = { "withdraw" }) @RequestMapping(method = {RequestMethod.POST}, value = "/cert/request") public ResCert ticketRequest(@Valid @RequestBody ReqCert req) { return apiService.certRequest(req);  }
    @Operation(summary = "출금 요청", tags = { "withdraw" }) @RequestMapping(method = {RequestMethod.POST}, value = "/user/withdraw") public BaseResponse userWithdraw(@Valid @RequestBody ReqUserWithdraw req) { return apiService.userWithdraw(req); }

}
