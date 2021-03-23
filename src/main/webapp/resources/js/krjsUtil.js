var krjsUtil = {
    isMobileDevice: function () {
        var UserAgent = navigator.userAgent;
        if (UserAgent.match(/iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson/i) != null || UserAgent.match(/LG|SAMSUNG|Samsung/) != null) {
            return true;
        } else {
            return false;
        }
    },
    checkDeviceTypeMobile: function () {
        var isDeviceMobile = false;
        (function (a) {
            if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) isDeviceMobile = true;
        })(navigator.userAgent || navigator.vendor || window.opera);
        return isDeviceMobile;
    },
    checkInputTypeMobile: function () {
        var isInputMobile = false;
        var UserAgent = navigator.userAgent;
        if (UserAgent.match(/iPhone|iPad|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson/i) != null || UserAgent.match(/LG|SAMSUNG|Samsung/) != null)
            isInputMobile = true;
        return isInputMobile;
    },
    fillZero: function (obj, len) {
        obj = '0000000000000000' + obj;
        return obj.substring(obj.length - len);
    },
    getFormattedPointString: function (point, fixedPoint) {
        if (point === 0 || point) {
            if (fixedPoint > 0) {
                var split = ('' + point).split(".");
                var cvtBase = split[0];
                if(split.length === 2) {
                    split[1] = split[1].substr(0, fixedPoint + 1);
                    cvtBase = cvtBase + '.' + split[1];
                }
                var cvt = Number(cvtBase).toFixed(fixedPoint);
                var cvtSplit = ('' + cvt).split(".");
                var postfix;
                if(split.length === 2) {
                    if(split[1].length >= fixedPoint) {
                        postfix = cvtSplit[1];
                    }
                    else {
                        postfix = (split[1] + '00000000000000000000').substr(0, fixedPoint);
                    }
                }
                else {
                    postfix = cvtSplit[1];
                }
                return (new Intl.NumberFormat().format(cvtSplit[0])) + '.' + postfix;
            }
            else {
                var split = ('' + point).split(".");
                return (new Intl.NumberFormat().format(split[0]));
            }
        }
        else {
            return '';
        }
    },
    daysBetween: function (date1, date2) {
        let ONE_DAY = 1000 * 60 * 60 * 24;
        const differenceMs = Math.abs(date1 - date2);
        return Math.round(differenceMs / ONE_DAY);
    },
    daysInMonth(m, y) { // m is 0 indexed: 0-11
        switch (m) {
            case 1 :
                return (y % 4 == 0 && y % 100) || y % 400 == 0 ? 29 : 28;
            case 8 : case 3 : case 5 : case 10 :
            return 30;
            default :
                return 31
        }
    },
    removeRougeChar: function (convertString) {
        if (convertString.substring(0, 1) === ",") {
            return convertString.substring(1, convertString.length)
        }
        return convertString;
    },
    convertToFixedFloat: function (n, digits) {
        if (digits >= 0) return parseFloat(n.toFixed(digits));
        digits = Math.pow(10, digits);
        var t = Math.round(n * digits) / digits;
        return parseFloat(t.toFixed(0));
    },
    addFloatMethod: function (a, b) {
        //return ((a * 10000) + (b * 10000)) / 10000;
        //return ((Number(a) * 10000).toFixed(0) + (Number(b) * 10000).toFixed(0)) / 10000;
        return (Math.round(Number(a) * 10000000) + Math.round(Number(b) * 10000000)) / 10000000;
    },
    subFloatMethod: function (a, b) {
        //return ((a * 10000) - (b * 10000)) / 10000;
        //return ((Number(a) * 10000).toFixed(0) - (Number(b) * 10000).toFixed(0)) / 10000;
        return (Math.round(Number(a) * 10000000) - Math.round(Number(b) * 10000000)) / 10000000;
    },
    convertPagedAxgridData: function (res) {
        if (res && res.page) {
            res.page.currentPage = res.page.pageNo - 1;
            res.page.totalElements = res.page.totalRecordSize;
            res.page.totalPages = res.page.pageCount;
        }
        return res;
    },
    convertPagedHyperionData: function (res, pageNo, pageSize) {
        if (res && res.total) {
            if(res.page === undefined)
                res.page = {};

            if(res.actions)
                res.list = res.actions;

            res.page.pageNo = pageNo;
            res.page.pageSize = pageSize;

            res.page.currentPage = res.page.pageNo - 1;
            res.page.totalElements = res.total.value;

            res.page.totalPages = 1;
            if (res.page.totalElements > 1) {
                res.page.totalPages = Math.floor((res.page.totalElements - 1) / res.page.pageSize) + 1;
            }
        }
        return res;
    },
    processCommonGridPageInit: function() {
        $('#searchBox input[type=text]').keypress(function (e) { if (e.keyCode === 13) { fnObj.search(); return false; } });
        {
            let refGridPagingSize = $('#gridPagingSize');
            refGridPagingSize.change(function () {
                let currentPagingSize = Number($(this).val());
                if (currentPagingSize === 20) fnObj.grid.gridFirst.setHeight(635);
                else if (currentPagingSize === 50) fnObj.grid.gridFirst.setHeight(1480);
                else if (currentPagingSize === 100) fnObj.grid.gridFirst.setHeight(2880);
                else if (currentPagingSize === 200) fnObj.grid.gridFirst.setHeight(5680);
                else if (currentPagingSize === 1000) fnObj.grid.gridFirst.setHeight(28800);
                else fnObj.grid.gridFirst.setHeight(31.75 * currentPagingSize);
                fnObj.search();
            });
            if (krjsUtil.isMobileDevice() === true) refGridPagingSize.hide();
        }
    },
    addPageObjectForAx5ui: function (arrayObject, pageNo, pageSize) {
        var res = {};
        if (arrayObject)
            res.list = arrayObject; else
            res.list = [];
        if (pageNo === undefined || pageNo == null)
            pageNo = 1;
        if (pageSize === undefined || pageSize == null)
            pageSize = res.list.length;
        if (pageSize === 0)
            pageSize = 1;
        res.page = {};
        res.page.currentPage = pageNo - 1;
        res.page.totalElements = res.list.length;
        res.page.totalPages = 1;
        if (res.list.length > 1)
            res.page.totalPages = Math.floor((res.list.length - 1) / pageSize) + 1;
        return res;
    },
    requestServer: function (type, accept, isAsync, url, data, onSuccess, onError, isShowErrorMessage) {
        $.ajax({
            crossOrigin : true,
            type: type,
            url: url,
            headers: {'Accept': accept},
            cache: false,
            async: isAsync,
            data: data,
            success: function (response) {
                krjsUtil.showTilesBodyLoading(false);
                if (onSuccess)
                    onSuccess(response);
                return response;
            },
            error: function (request, status, error) {
                krjsUtil.showTilesBodyLoading(false);
                var errorInfo = "code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error;
                console.log(request);
                console.log(errorInfo);
                if (isShowErrorMessage === true)
                    krjsUtil.showWarnSwal('error', errorInfo);
                if (onError)
                    onError(errorInfo);
                return errorInfo;
            }
        });
    },
    requestForAx5ui: function (type, accept, isAsync, url, data, targetGrid, onSuccess, onError, isShowErrorMessage) {
        $.ajax({
            type: type,
            url: url,
            headers: {'Accept': accept},
            cache: false,
            async: isAsync,
            data: data,
            beforeSend: function(){
                headerSrcObj.loadingbarCount++;
                if(headerSrcObj.loadingbarCount > 0)
                    $('#mloader').show();//
            },
            success: function (response) {
                krjsUtil.showTilesBodyLoading(false);
                if (targetGrid) {
                    targetGrid.setData([]);
                    targetGrid.setData(krjsUtil.convertPagedAxgridData(response));
                }
                if (onSuccess)
                    onSuccess(response);

                headerSrcObj.loadingbarCount--;
                if(headerSrcObj.loadingbarCount <= 0)
                    $('#mloader').hide();//

                return response;
            },
            error: function (request, status, error) {
                krjsUtil.showTilesBodyLoading(false);
                var errorInfo = "code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error;
                console.log(request);
                console.log(errorInfo);
                if (isShowErrorMessage === true)
                    krjsUtil.showWarnSwal('error', errorInfo);
                if (onError)
                    onError(errorInfo);

                headerSrcObj.loadingbarCount--;
                if(headerSrcObj.loadingbarCount <= 0)
                    $('#mloader').hide();//

                return errorInfo;
            }
        });
    },
    requestForAx5uiAndHyperion: function (type, accept, isAsync, url, data, targetGrid, onSuccess, onError, isShowErrorMessage, pageNo, pageSize) {
        $.ajax({
            type: type,
            url: url,
            headers: {'Accept': accept},
            cache: false,
            async: isAsync,
            data: data,
            success: function (response) {
                krjsUtil.showTilesBodyLoading(false);
                if (targetGrid) {
                    targetGrid.setData([]);
                    targetGrid.setData(krjsUtil.convertPagedHyperionData(response, pageNo, pageSize));
                }
                else
                    krjsUtil.convertPagedHyperionData(response, pageNo, pageSize);
                if (onSuccess)
                    onSuccess(response);
                return response;
            },
            error: function (request, status, error) {
                krjsUtil.showTilesBodyLoading(false);
                var errorInfo = "code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error;
                console.log(request);
                console.log(errorInfo);
                if (isShowErrorMessage === true)
                    krjsUtil.showWarnSwal('error', errorInfo);
                if (onError)
                    onError(errorInfo);
                return errorInfo;
            }
        });
    },
    showSuccessSwal: function (title, message) {
        swal({
            title: title,
            text: message,
            type: "success",
            showCancelButton: false,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'OK',
            closeOnConfirm: true,
            cancelButtonText: 'No'
        });
    },
    showInfoSwal: function (title, message) {
        swal({
            title: title,
            text: message,
            type: "info",
            showCancelButton: false,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'OK',
            closeOnConfirm: true,
            cancelButtonText: 'No'
        });
    },
    showWarnSwal: function (title, message) {
        swal({
            title: title,
            text: message,
            type: "warning",
            showCancelButton: false,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'OK',
            closeOnConfirm: true,
            cancelButtonText: 'No'
        });
    },
    showErrorSwal: function (title, message) {
        swal({
            title: title,
            text: message,
            type: "error",
            showCancelButton: false,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'OK',
            closeOnConfirm: true,
            cancelButtonText: 'No'
        });
    },
    cloneTargetObj: function (obj) {
        if (obj === null || typeof(obj) !== 'object')
            return obj;
        var copy = obj.constructor();
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) {
                copy[attr] = obj[attr];
            }
        }
        return copy;
    },
    showTilesBodyLoading: function (isShow) {
        var targetObject = $('#kr_tiles_body_loading');
        if (targetObject) {
            if (isShow === true)
                targetObject.show(); else
                targetObject.hide();
        }
    },
    getCurrentTimeHHMMSS: function () {
        var now = new Date();
        return now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds();
    },
    setCharAt: function (str, index, chr) {
        if (index > str.length - 1) return str;
        return str.substr(0, index) + chr + str.substr(index + 1);
    },
    shuffleArray: function (a) {
        var i;
        for (i = a.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [a[i], a[j]] = [a[j], a[i]];
        }
        return a;
    },
    isStorageAvailable: function (type) {
        try {
            var storage = window[type], x = '__storage_test__';
            storage.setItem(x, x);
            storage.removeItem(x);
            return true;
        }
        catch (e) {
            console.warn('cannot user browser storage: ' + type);
            return false;
        }
    },
    getAvailableBrowserStorage: function (type) {
        if (this.isStorageAvailable(type) === true) return type;
        if (this.isStorageAvailable('localStorage') === true) return 'localStorage';
        if (this.isStorageAvailable('sessionStorage') === true) return 'sessionStorage';
        return null;
    },
    writeBrowserStorageData: function (type, key, data) {
        if (type === null) {
            console.log('invalid requestType: ' + type);
            return false;
        }
        switch (type) {
            case'localStorage':
                localStorage.setItem(key, data);
                break;
            case'sessionStorage':
                sessionStorage.setItem(key, data);
                break;
            default:
                console.log('invalid requestType: ' + type);
                break;
        }
    },
    getBrowserStorageData: function (type, key) {
        if (type === null) {
            console.log('invalid requestType: ' + type);
            return false;
        }
        switch (type) {
            case'localStorage':
                return localStorage.getItem(key);
            case'sessionStorage':
                return sessionStorage.getItem(key);
            default:
                console.log('invalid requestType: ' + type);
                return null;
        }
    },
};
var nonLoginEos = {
    isMainNet: true, isPrintConsole: true, init(isMainNet) {
        if (isMainNet) {
            nonLoginEos.isMainNet = isMainNet;
            numHttpEos.isMainNet = isMainNet;
        }
    }, getInfo: function (c_success, c_error) {
        numHttpEos.getInfo(c_success, c_error);
    }, getAccount: function (accountName, c_success, c_error) {
        numHttpEos.getAccount({account_name: accountName}, c_success, c_error);
    }, getCurrencyBalance: async function (code, account, symbol, c_success, c_error) {
        let sendObj = {code: code, account: account, symbol: symbol};
        numHttpEos.getCurrencyBalance(sendObj, c_success, c_error);
    }, getTableRows: function (code, scope, table, lowerBound, c_success, c_error) {
        let sendObj = {code: code, scope: scope, table: table, json: true};
        if (lowerBound)
            sendObj.lower_bound = lowerBound;
        numHttpEos.getTableRows(sendObj, c_success, c_error);
    }, getActions: function (accountName, pos, offset, c_success, c_error) {
        let sendObj = {account_name: accountName, pos: pos, offset: offset};
        numHttpEos.getActions(sendObj, c_success, c_error);
    },
};
var numHttpEos = {
    isMainNet: true, isKylin: true, isPrintConsole: false, getEosChainId: function (isMainNet) {
        if (isMainNet === true) {
            return 'aca376f206b8fc25a6ed44dbdc66547c36c6c33e3a119ffbeaef943642f0e906';
        }
        else {
            if (numHttpEos.isKylin === true) return '5fff1dae8dc8e2fc4d5b23b2c7665c97f9e9d8edf2b6485a86ba311c25639191'; else return 'e70aaab8997e1dfce58fbfac80cbbb8fecec7b99cf982a9444273cbc64c41473';
        }
    }, getEosNetworkConfig: function (isMainNet) {
        if (isMainNet === true) {
            return {
                blockchain: 'eos',
                protocol: 'https',
                host: 'mainnet.eoscannon.io',
                //host: 'user-api.eoseoul.io',
                port: '443',
                chainId: numHttpEos.getEosChainId(isMainNet)
            };
        }
        else {
            if (numHttpEos.isKylin === true) {
                return {
                    blockchain: 'eos',
                    protocol: 'https',
                    //host: 'kylin.meet.one',
                    //host: 'kylin.eos.dfuse.io',
                    host: 'kylin.eos.dfuse.io',
                    port: '443',
                    chainId: numHttpEos.getEosChainId(isMainNet)
                };
            }
            else {
                return {
                    blockchain: 'eos',
                    protocol: 'https',
                    host: 'jungle2.cryptolions.io',
                    port: '443',
                    chainId: numHttpEos.getEosChainId(isMainNet)
                };
            }
        }
    }, init(isMainNet) {
        if (isMainNet) {
            nonLoginEos.isMainNet = isMainNet;
            numHttpEos.isMainNet = isMainNet;
        }
    }, apiRequest(api, data, c_success, c_error) {
        let network = numHttpEos.getEosNetworkConfig(numHttpEos.isMainNet);
        let apiEndpoint = network.protocol + '://' + network.host + ':' + network.port;
        if (apiEndpoint.endsWith('/'))
            apiEndpoint = apiEndpoint.substr(0, apiEndpoint.length - 1);
        $.ajax({
            url: apiEndpoint + api,
            type: 'POST',
            async: true,
            cache: false,
            data: JSON.stringify(data),
            success: function (result) {
                if (numHttpEos.isPrintConsole === true) console.log(result);
                if (c_success)
                    c_success(result);
            },
            error: function (error) {
                if (numHttpEos.isPrintConsole === true) console.error(error);
                if (c_error)
                    c_error(error);
            },
        });
    }, getInfo: function (c_success, c_error) {
        this.apiRequest('/v1/chain/get_info', {}, c_success, c_error);
    }, getAccount: function (sendObj, c_success, c_error) {
        this.apiRequest('/v1/chain/get_account', sendObj, c_success, c_error);
    }, getCurrencyBalance: async function (sendObj, c_success, c_error) {
        this.apiRequest('/v1/chain/get_currency_balance', sendObj, c_success, c_error);
    }, getTableRows: function (sendObj, c_success, c_error) {
        this.apiRequest('/v1/chain/get_table_rows', sendObj, c_success, c_error);
    }, getTableRowsFull: function (sendObj, lowerBoundKey, baseResult, c_success, c_error) {
        numHttpEos.getTableRows(sendObj, function (data) {
            if (baseResult === null) {
                baseResult = data;
            }
            else {
                for (let i = 0; i < data.rows.length; i++) {
                    baseResult.rows.push(data.rows[i]);
                }
            }
            if (data.more === false) {
                baseResult.more = false;
                if (c_success) c_success(baseResult);
            }
            else {
                console.log('requesting more data.  lower_bound:' + sendObj.lower_bound);
                let lastRow = baseResult.rows[baseResult.rows.length - 1];
                for (let attr in lastRow) {
                    if (lowerBoundKey == attr) {
                        sendObj.lower_bound = Number(lastRow[attr]) + 1;
                        break;
                    }
                }
                numHttpEos.getTableRowsFull(sendObj, lowerBoundKey, baseResult, c_success, c_error);
            }
        }, function (error) {
            if (c_error) c_error(error);
        });
    },

};