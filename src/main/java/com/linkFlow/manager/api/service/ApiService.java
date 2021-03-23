package com.linkFlow.manager.api.service;

import com.linkFlow.manager.api.model.request.*;
import com.linkFlow.manager.api.model.response.*;
import com.linkFlow.manager.common.model.BaseResponse;
import com.test32.common.paging.PagingData;

import java.util.Map;

public interface ApiService
{
    ResSystemInfo systemInfo();
    ResCert certRequest(ReqCert req);

    ResTicketRequest ticketRequest(ReqUserDefaultAuth req);


    ResUserInfo userInfo(ReqUserAddressOnly auth);
    ResUserToken userToken(ReqUserAddressOnly auth, ReqTokenSymbol req);
    ResUserLf userLf(ReqUserAddressOnly auth);
    ResUserProduct userProduct(ReqUserProduct req);


    ResUserInfo userInfo(ReqUserAfterAuth auth);
    ResUserToken userToken(ReqUserAfterAuth auth, ReqTokenSymbol req);


    BaseResponse userWithdraw(ReqUserWithdraw req);


    ResTier tierList();

    ResProductList productList(ReqProductList req);

    ResTokenInfo tokenInfoList(String symbol);
    ResTokenPrice tokenPriceList(String symbol);
    ResTokenChartInfo tokenChartList(String symbol);

    ResAssetHistory assetHistory(ReqAssetHistory req);
    ResAssetHistoryConverted assetHistoryConverted(ReqAssetHistory req);



    ResUserTransactionDetail userLogDetail(Long idx);

    PagingData selectPagingBySearchForTransaction(Map<String, Object> parameter);
    PagingData selectPagingBySearchForNotice(Map<String, Object> parameter);

}