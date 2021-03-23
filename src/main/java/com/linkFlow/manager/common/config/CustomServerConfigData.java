package com.linkFlow.manager.common.config;

public class CustomServerConfigData
{
    // datasource
    private String jdbcDriverClassName;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;

    // system config
    private Integer loginLevel = 0;
    private String eosEndpoint = "http://jungle2.cryptolions.io:80/";
    private String siteName;
    private String siteUrl;


    // management roll
    // main scheduler
    private Boolean isManageMainScheduler;
    private Boolean isManageLoginWebRequest;

    private Boolean isManageLfDeposit;
    private Boolean isManageProductPurchase;
    private Boolean isManageDefaultInterest;


    private Boolean isManageAutoDeposit;
    private Boolean isManageAutoWithdraw;
    private Boolean isManageDenyWithdraw;
    private Boolean isManageMultipleWithdraw;
    private Boolean isManageCommonWorkQueue;
    private Boolean isManageDailySummary;


    private Boolean isApiCheckTime;



    // Transfer history
    private Boolean isManageTransferFromHistory;
    private Boolean isManageDeleteSuccessHistory;
    private Boolean isManageDeleteUselessHistory;
    private Boolean isManageDeleteErrorHistory;

    // for ticket
    private Boolean isManageDeleteCommonTicket;
    private Integer ticketExpireSecond;

    // number cert
    private Boolean isManageDeleteNumberCert;
    private Integer numberCertExpireSecond;

    // log control
    private Boolean isPrintApiLog;
    private Boolean isPrintResponseLog;



    // custom ticker
    private Boolean isManageInterestFromCustomTicker;
    private Boolean isManageCustomTicker;


    // cashierest
    private Boolean isManageCashierestTicker;
    private Boolean isManageCashierestChart;

    // bitsonic
    private Boolean isManageBitsonicTicker;
    private Boolean isManageBitsonicChart;

    // binance
    private Boolean isManageBinanceTicker;
    private Boolean isManageBinanceChart;

    // biki
    private Boolean isManageBikiTicker;

    // coinone
    private Boolean isManageCoinOneTicker;
    private String urlCoinOne;

    // coinmarketcap
    private Boolean isManageCoinMarketCapTicker;
    private Boolean isManageCoinMarketCapChart;



    // EOS withdraw account
    private String withdrawAccountArray = "";
    private String withdrawKeyArray = "";

    // erc20 withdraw account
    private String erc20WithdrawAccount;
    private String erc20WithdrawKey;

    private String erc20MultipleWithdrawAccountArray;
    private String erc20MultipleWithdrawKeyArray;

    // config
    private String serverTag = "server1";
    private String serviceId = "linkFlow";
    private String defaultApiLang = "cn";

    private Integer userIdLength = 10;
    private Integer maxUserError = 5;

    // slack
    private String urlSlackPage;
    private String urlSlackWithdraw;


    // file upload
    private String uploadFilePath = "c:/linkFlow/upload/";
    private String releaseFilePath = "http://172.30.1.101:9094/linkFlow/upload/";


    // currency
    private Boolean isManageCurrencyApi;
    private String currencyApiUrl = "";
    private String currencyApiKey = "";


    // erc20
    private String etherscanEndpoint = "https://ropsten.etherscan.io/";
    private String etherscanEthPriceUrl = "";

    // infura
    private String infuraLogin;
    private String infuraUrl;
    private String infuraId;
    private String infuraSecret;

    // erc20
    private Boolean isManageErc20LogEnabled;
    private Boolean isManageErc20BlockNumber;
    private Boolean isManageErc20TransactionReceipt;
    private Boolean isManageErc20Deposit;
    private Boolean isManageErc20RefreshUserPoint;
    private Integer erc20TransactionProcessLimit;

    // delete
    private Boolean isManageErc20DeleteUselessTransaction;
    private Integer deleteUselessTransactionSecond;


    public String getJdbcDriverClassName()
    {
        return jdbcDriverClassName;
    }

    public void setJdbcDriverClassName(String jdbcDriverClassName)
    {
        this.jdbcDriverClassName = jdbcDriverClassName;
    }

    public String getJdbcUrl()
    {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl)
    {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUsername()
    {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername)
    {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword()
    {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword)
    {
        this.jdbcPassword = jdbcPassword;
    }

    public Integer getLoginLevel()
    {
        return loginLevel;
    }

    public void setLoginLevel(Integer loginLevel)
    {
        this.loginLevel = loginLevel;
    }

    public String getEosEndpoint()
    {
        return eosEndpoint;
    }

    public void setEosEndpoint(String eosEndpoint)
    {
        this.eosEndpoint = eosEndpoint;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public String getSiteUrl()
    {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl)
    {
        this.siteUrl = siteUrl;
    }

    public Boolean getManageMainScheduler()
    {
        return isManageMainScheduler;
    }

    public void setManageMainScheduler(Boolean manageMainScheduler)
    {
        isManageMainScheduler = manageMainScheduler;
    }

    public Boolean getManageLoginWebRequest()
    {
        return isManageLoginWebRequest;
    }

    public void setManageLoginWebRequest(Boolean manageLoginWebRequest)
    {
        isManageLoginWebRequest = manageLoginWebRequest;
    }

    public Boolean getManageLfDeposit()
    {
        return isManageLfDeposit;
    }

    public void setManageLfDeposit(Boolean manageLfDeposit)
    {
        isManageLfDeposit = manageLfDeposit;
    }

    public Boolean getManageProductPurchase()
    {
        return isManageProductPurchase;
    }

    public void setManageProductPurchase(Boolean manageProductPurchase)
    {
        isManageProductPurchase = manageProductPurchase;
    }

    public Boolean getManageDefaultInterest()
    {
        return isManageDefaultInterest;
    }

    public void setManageDefaultInterest(Boolean manageDefaultInterest)
    {
        isManageDefaultInterest = manageDefaultInterest;
    }

    public Boolean getManageAutoDeposit()
    {
        return isManageAutoDeposit;
    }

    public void setManageAutoDeposit(Boolean manageAutoDeposit)
    {
        isManageAutoDeposit = manageAutoDeposit;
    }

    public Boolean getManageAutoWithdraw()
    {
        return isManageAutoWithdraw;
    }

    public void setManageAutoWithdraw(Boolean manageAutoWithdraw)
    {
        isManageAutoWithdraw = manageAutoWithdraw;
    }

    public Boolean getManageDenyWithdraw()
    {
        return isManageDenyWithdraw;
    }

    public void setManageDenyWithdraw(Boolean manageDenyWithdraw)
    {
        isManageDenyWithdraw = manageDenyWithdraw;
    }

    public Boolean getManageMultipleWithdraw()
    {
        return isManageMultipleWithdraw;
    }

    public void setManageMultipleWithdraw(Boolean manageMultipleWithdraw)
    {
        isManageMultipleWithdraw = manageMultipleWithdraw;
    }

    public Boolean getManageCommonWorkQueue()
    {
        return isManageCommonWorkQueue;
    }

    public void setManageCommonWorkQueue(Boolean manageCommonWorkQueue)
    {
        isManageCommonWorkQueue = manageCommonWorkQueue;
    }

    public Boolean getManageDailySummary()
    {
        return isManageDailySummary;
    }

    public void setManageDailySummary(Boolean manageDailySummary)
    {
        isManageDailySummary = manageDailySummary;
    }

    public Boolean getApiCheckTime()
    {
        return isApiCheckTime;
    }

    public void setApiCheckTime(Boolean apiCheckTime)
    {
        isApiCheckTime = apiCheckTime;
    }

    public Boolean getManageTransferFromHistory()
    {
        return isManageTransferFromHistory;
    }

    public void setManageTransferFromHistory(Boolean manageTransferFromHistory)
    {
        isManageTransferFromHistory = manageTransferFromHistory;
    }

    public Boolean getManageDeleteSuccessHistory()
    {
        return isManageDeleteSuccessHistory;
    }

    public void setManageDeleteSuccessHistory(Boolean manageDeleteSuccessHistory)
    {
        isManageDeleteSuccessHistory = manageDeleteSuccessHistory;
    }

    public Boolean getManageDeleteUselessHistory()
    {
        return isManageDeleteUselessHistory;
    }

    public void setManageDeleteUselessHistory(Boolean manageDeleteUselessHistory)
    {
        isManageDeleteUselessHistory = manageDeleteUselessHistory;
    }

    public Boolean getManageDeleteErrorHistory()
    {
        return isManageDeleteErrorHistory;
    }

    public void setManageDeleteErrorHistory(Boolean manageDeleteErrorHistory)
    {
        isManageDeleteErrorHistory = manageDeleteErrorHistory;
    }

    public Boolean getManageDeleteCommonTicket()
    {
        return isManageDeleteCommonTicket;
    }

    public void setManageDeleteCommonTicket(Boolean manageDeleteCommonTicket)
    {
        isManageDeleteCommonTicket = manageDeleteCommonTicket;
    }

    public Integer getTicketExpireSecond()
    {
        return ticketExpireSecond;
    }

    public void setTicketExpireSecond(Integer ticketExpireSecond)
    {
        this.ticketExpireSecond = ticketExpireSecond;
    }

    public Boolean getManageDeleteNumberCert()
    {
        return isManageDeleteNumberCert;
    }

    public void setManageDeleteNumberCert(Boolean manageDeleteNumberCert)
    {
        isManageDeleteNumberCert = manageDeleteNumberCert;
    }

    public Integer getNumberCertExpireSecond()
    {
        return numberCertExpireSecond;
    }

    public void setNumberCertExpireSecond(Integer numberCertExpireSecond)
    {
        this.numberCertExpireSecond = numberCertExpireSecond;
    }

    public Boolean getPrintApiLog()
    {
        return isPrintApiLog;
    }

    public void setPrintApiLog(Boolean printApiLog)
    {
        isPrintApiLog = printApiLog;
    }

    public Boolean getPrintResponseLog()
    {
        return isPrintResponseLog;
    }

    public void setPrintResponseLog(Boolean printResponseLog)
    {
        isPrintResponseLog = printResponseLog;
    }

    public Boolean getManageInterestFromCustomTicker()
    {
        return isManageInterestFromCustomTicker;
    }

    public void setManageInterestFromCustomTicker(Boolean manageInterestFromCustomTicker)
    {
        isManageInterestFromCustomTicker = manageInterestFromCustomTicker;
    }

    public Boolean getManageCustomTicker()
    {
        return isManageCustomTicker;
    }

    public void setManageCustomTicker(Boolean manageCustomTicker)
    {
        isManageCustomTicker = manageCustomTicker;
    }

    public Boolean getManageCashierestTicker()
    {
        return isManageCashierestTicker;
    }

    public void setManageCashierestTicker(Boolean manageCashierestTicker)
    {
        isManageCashierestTicker = manageCashierestTicker;
    }

    public Boolean getManageCashierestChart()
    {
        return isManageCashierestChart;
    }

    public void setManageCashierestChart(Boolean manageCashierestChart)
    {
        isManageCashierestChart = manageCashierestChart;
    }

    public Boolean getManageBitsonicTicker()
    {
        return isManageBitsonicTicker;
    }

    public void setManageBitsonicTicker(Boolean manageBitsonicTicker)
    {
        isManageBitsonicTicker = manageBitsonicTicker;
    }

    public Boolean getManageBitsonicChart()
    {
        return isManageBitsonicChart;
    }

    public void setManageBitsonicChart(Boolean manageBitsonicChart)
    {
        isManageBitsonicChart = manageBitsonicChart;
    }

    public Boolean getManageBinanceTicker()
    {
        return isManageBinanceTicker;
    }

    public void setManageBinanceTicker(Boolean manageBinanceTicker)
    {
        isManageBinanceTicker = manageBinanceTicker;
    }

    public Boolean getManageBinanceChart()
    {
        return isManageBinanceChart;
    }

    public void setManageBinanceChart(Boolean manageBinanceChart)
    {
        isManageBinanceChart = manageBinanceChart;
    }

    public Boolean getManageBikiTicker()
    {
        return isManageBikiTicker;
    }

    public void setManageBikiTicker(Boolean manageBikiTicker)
    {
        isManageBikiTicker = manageBikiTicker;
    }

    public Boolean getManageCoinOneTicker()
    {
        return isManageCoinOneTicker;
    }

    public void setManageCoinOneTicker(Boolean manageCoinOneTicker)
    {
        isManageCoinOneTicker = manageCoinOneTicker;
    }

    public String getUrlCoinOne()
    {
        return urlCoinOne;
    }

    public void setUrlCoinOne(String urlCoinOne)
    {
        this.urlCoinOne = urlCoinOne;
    }

    public Boolean getManageCoinMarketCapTicker()
    {
        return isManageCoinMarketCapTicker;
    }

    public void setManageCoinMarketCapTicker(Boolean manageCoinMarketCapTicker)
    {
        isManageCoinMarketCapTicker = manageCoinMarketCapTicker;
    }

    public Boolean getManageCoinMarketCapChart()
    {
        return isManageCoinMarketCapChart;
    }

    public void setManageCoinMarketCapChart(Boolean manageCoinMarketCapChart)
    {
        isManageCoinMarketCapChart = manageCoinMarketCapChart;
    }

    public String getWithdrawAccountArray()
    {
        return withdrawAccountArray;
    }

    public void setWithdrawAccountArray(String withdrawAccountArray)
    {
        this.withdrawAccountArray = withdrawAccountArray;
    }

    public String getWithdrawKeyArray()
    {
        return withdrawKeyArray;
    }

    public void setWithdrawKeyArray(String withdrawKeyArray)
    {
        this.withdrawKeyArray = withdrawKeyArray;
    }

    public String getErc20WithdrawAccount()
    {
        return erc20WithdrawAccount;
    }

    public void setErc20WithdrawAccount(String erc20WithdrawAccount)
    {
        this.erc20WithdrawAccount = erc20WithdrawAccount;
    }

    public String getErc20WithdrawKey()
    {
        return erc20WithdrawKey;
    }

    public void setErc20WithdrawKey(String erc20WithdrawKey)
    {
        this.erc20WithdrawKey = erc20WithdrawKey;
    }

    public String getErc20MultipleWithdrawAccountArray()
    {
        return erc20MultipleWithdrawAccountArray;
    }

    public void setErc20MultipleWithdrawAccountArray(String erc20MultipleWithdrawAccountArray)
    {
        this.erc20MultipleWithdrawAccountArray = erc20MultipleWithdrawAccountArray;
    }

    public String getErc20MultipleWithdrawKeyArray()
    {
        return erc20MultipleWithdrawKeyArray;
    }

    public void setErc20MultipleWithdrawKeyArray(String erc20MultipleWithdrawKeyArray)
    {
        this.erc20MultipleWithdrawKeyArray = erc20MultipleWithdrawKeyArray;
    }

    public String getServerTag()
    {
        return serverTag;
    }

    public void setServerTag(String serverTag)
    {
        this.serverTag = serverTag;
    }

    public String getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(String serviceId)
    {
        this.serviceId = serviceId;
    }

    public String getDefaultApiLang()
    {
        return defaultApiLang;
    }

    public void setDefaultApiLang(String defaultApiLang)
    {
        this.defaultApiLang = defaultApiLang;
    }

    public Integer getUserIdLength()
    {
        return userIdLength;
    }

    public void setUserIdLength(Integer userIdLength)
    {
        this.userIdLength = userIdLength;
    }

    public Integer getMaxUserError()
    {
        return maxUserError;
    }

    public void setMaxUserError(Integer maxUserError)
    {
        this.maxUserError = maxUserError;
    }

    public String getUrlSlackPage()
    {
        return urlSlackPage;
    }

    public void setUrlSlackPage(String urlSlackPage)
    {
        this.urlSlackPage = urlSlackPage;
    }

    public String getUrlSlackWithdraw()
    {
        return urlSlackWithdraw;
    }

    public void setUrlSlackWithdraw(String urlSlackWithdraw)
    {
        this.urlSlackWithdraw = urlSlackWithdraw;
    }

    public String getUploadFilePath()
    {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath)
    {
        this.uploadFilePath = uploadFilePath;
    }

    public String getReleaseFilePath()
    {
        return releaseFilePath;
    }

    public void setReleaseFilePath(String releaseFilePath)
    {
        this.releaseFilePath = releaseFilePath;
    }

    public Boolean getManageCurrencyApi()
    {
        return isManageCurrencyApi;
    }

    public void setManageCurrencyApi(Boolean manageCurrencyApi)
    {
        isManageCurrencyApi = manageCurrencyApi;
    }

    public String getCurrencyApiUrl()
    {
        return currencyApiUrl;
    }

    public void setCurrencyApiUrl(String currencyApiUrl)
    {
        this.currencyApiUrl = currencyApiUrl;
    }

    public String getCurrencyApiKey()
    {
        return currencyApiKey;
    }

    public void setCurrencyApiKey(String currencyApiKey)
    {
        this.currencyApiKey = currencyApiKey;
    }

    public String getEtherscanEndpoint()
    {
        return etherscanEndpoint;
    }

    public void setEtherscanEndpoint(String etherscanEndpoint)
    {
        this.etherscanEndpoint = etherscanEndpoint;
    }

    public String getEtherscanEthPriceUrl()
    {
        return etherscanEthPriceUrl;
    }

    public void setEtherscanEthPriceUrl(String etherscanEthPriceUrl)
    {
        this.etherscanEthPriceUrl = etherscanEthPriceUrl;
    }

    public String getInfuraLogin()
    {
        return infuraLogin;
    }

    public void setInfuraLogin(String infuraLogin)
    {
        this.infuraLogin = infuraLogin;
    }

    public String getInfuraUrl()
    {
        return infuraUrl;
    }

    public void setInfuraUrl(String infuraUrl)
    {
        this.infuraUrl = infuraUrl;
    }

    public String getInfuraId()
    {
        return infuraId;
    }

    public void setInfuraId(String infuraId)
    {
        this.infuraId = infuraId;
    }

    public String getInfuraSecret()
    {
        return infuraSecret;
    }

    public void setInfuraSecret(String infuraSecret)
    {
        this.infuraSecret = infuraSecret;
    }

    public Boolean getManageErc20LogEnabled()
    {
        return isManageErc20LogEnabled;
    }

    public void setManageErc20LogEnabled(Boolean manageErc20LogEnabled)
    {
        isManageErc20LogEnabled = manageErc20LogEnabled;
    }

    public Boolean getManageErc20BlockNumber()
    {
        return isManageErc20BlockNumber;
    }

    public void setManageErc20BlockNumber(Boolean manageErc20BlockNumber)
    {
        isManageErc20BlockNumber = manageErc20BlockNumber;
    }

    public Boolean getManageErc20TransactionReceipt()
    {
        return isManageErc20TransactionReceipt;
    }

    public void setManageErc20TransactionReceipt(Boolean manageErc20TransactionReceipt)
    {
        isManageErc20TransactionReceipt = manageErc20TransactionReceipt;
    }

    public Boolean getManageErc20Deposit()
    {
        return isManageErc20Deposit;
    }

    public void setManageErc20Deposit(Boolean manageErc20Deposit)
    {
        isManageErc20Deposit = manageErc20Deposit;
    }

    public Boolean getManageErc20RefreshUserPoint()
    {
        return isManageErc20RefreshUserPoint;
    }

    public void setManageErc20RefreshUserPoint(Boolean manageErc20RefreshUserPoint)
    {
        isManageErc20RefreshUserPoint = manageErc20RefreshUserPoint;
    }

    public Integer getErc20TransactionProcessLimit()
    {
        return erc20TransactionProcessLimit;
    }

    public void setErc20TransactionProcessLimit(Integer erc20TransactionProcessLimit)
    {
        this.erc20TransactionProcessLimit = erc20TransactionProcessLimit;
    }

    public Boolean getManageErc20DeleteUselessTransaction()
    {
        return isManageErc20DeleteUselessTransaction;
    }

    public void setManageErc20DeleteUselessTransaction(Boolean manageErc20DeleteUselessTransaction)
    {
        isManageErc20DeleteUselessTransaction = manageErc20DeleteUselessTransaction;
    }

    public Integer getDeleteUselessTransactionSecond()
    {
        return deleteUselessTransactionSecond;
    }

    public void setDeleteUselessTransactionSecond(Integer deleteUselessTransactionSecond)
    {
        this.deleteUselessTransactionSecond = deleteUselessTransactionSecond;
    }
}