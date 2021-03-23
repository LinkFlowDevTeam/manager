package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.TokenChartVOForApi;
import com.linkFlow.manager.common.model.vo.TokenInfoVO;
import com.linkFlow.manager.common.model.vo.TokenInfoVOForApi;
import com.linkFlow.manager.common.model.vo.TokenPriceVOForApi;
import com.test32.common.generic.GenericService;

import java.util.List;

public interface TokenInfoService extends GenericService<TokenInfoVO>
{
    String DEF_TOKEN_LF = "LF";
    String DEF_TOKEN_USDT = "USDT";
    String DEF_TOKEN_USDC = "USDC";


    Integer DEFAULT_GENERATION_ON = 1;


    int TOKEN_STATE_DISABLE = 0;
    int TOKEN_STATE_ENABLE = 1;
    int TOKEN_STATE_NO_DISPLAY = 2;


    int TICKER_STATE_ENABLE = 1;
    int CHART_STATE_ENABLE = 1;

    int LOCK_WITHDRAW_STATE_DISABLE = 0;

    int AUTO_WITHDRAW_STATE_ENABLE = 1;

    int MAX_TICKER_VALUE_LENGTH = 20;


    int ERC20_STATE_NONE = 0;
    int ERC20_STATE_REFRESH_REQUIRED = 1;


    String MARKET_NAME_CASHIEREST = "cashierest";
    String MARKET_NAME_BITSONIC = "bitsonic";
    String MARKET_NAME_BINANCE = "binance";
    String MARKET_NAME_COINMARKETCAP = "coinmarketcap";
    String MARKET_NAME_BIKI = "biki";
    String MARKET_NAME_COINONE = "coinone";



    String TOKEN_TYPE_ERC20 = "ERC20";


    List<TokenInfoVO> selectDefaultGenerationList();
    List<TokenInfoVO> selectEnabledList();
    List<TokenInfoVO> selectTkAddressNotNull();

    TokenInfoVO selectLfToken();
    TokenInfoVO selectBySymbol(String symbol);
    TokenInfoVO selectByErc20Address(String address);


    BaseResponse refreshErc20Point(Integer tkIdx);

    List<TokenInfoVOForApi> selectBySearchForInfoApi(String symbol);
    List<TokenPriceVOForApi> selectBySearchForPriceApi(String symbol);
    List<TokenChartVOForApi> selectBySearchForChartApi(String symbol);

    String substringIfRequired(String input);

    boolean refreshTokenPrice(Integer tokenIdx);
    boolean refreshTokenPrice(TokenInfoVO tokenInfoVO);
}