/* global context, urlBase, nologin, adm_login_uri, success, angular */

var app = angular.module('recycleAccountApp', ['ui.bootstrap', 'ui.select2']);
app.controller('recycleAccountCtrl', function ($scope, $http, $filter) {
    $scope.reloadFilter = function (str) {
        $.blockUI({message: '<img src="' + context + '/images/indicator_48.gif" />'});
        $http({
            method: "POST",
            url: urlBase + "/account/rest/recycle-data",
            params: {crPage: $scope.crPage, key: $scope.key, phone: $scope.phone, email: $scope.email, status: $scope.status, maxRow: $scope.maxRow}
        }).then(function Succes(resp) {
            if (resp !== undefined && typeof resp === "object") {
                $scope.result = resp.data.result;
                if ($scope.result.code === nologin) {
                    location.href = context + adm_login_uri;
                } else {
                    $scope.datas = resp.data.datas;
                    $scope.totalRow = resp.data.totalRow;

                    if (!angular.isUndefined(str) && str !== '') {
                        $scope.result.message = str;
                    }
                    blink_text('callback_info', 5000);
                    $.unblockUI();
                }
            }
        }, function Error(response) {
            $.unblockUI();
            $scope.message = response.message;
        });
    };
    $scope.$watch('crPage + crPage', function () {
        $scope.reloadFilter();
    });
    $scope.reset = function () {
        location.reload();
    };
    $scope.pageChanged = function () {
        $scope.crPage = this.crPage;
    };
    $scope.deleteforEver = function (id) {
        $("#result_model").text('');
        jConfirm('<b style="color:red">Bạn chắc chắn muốn xóa vĩnh viễn Tài Khoản này?</b>', 'Confirm', function (r) {
            if (r) {
                $http({
                    method: "POST",
                    url: urlBase + "/account/rest/deleteForEver",
                    params: {id: id}
                }).then(function Succes(resp) {
                    if (resp !== undefined && typeof resp === "object") {
                        $scope.result = resp.data.result;
                        if ($scope.result.code === nologin) {
                            location.href = context + adm_login_uri;
                        } else {
                            if ($scope.result.code === success) {
                                $scope.reloadFilter($scope.result.message);
                            }
                        }
                    }
                }, function Error(response) {
                    $scope.message = response.message;
                });
            } else
                console.log('Cai noi gi the')
        });
    };
    $scope.undoDelete = function (id) {
        jConfirm('<b style="color:red">Bạn chắc chắn muốn khôi phục Tài Kho4ản này?</b>', 'Confirm', function (r) {
            if (r) {
                $http({
                    method: "POST",
                    url: urlBase + "/account/rest/undo-account",
                    params: {id: id}
                }).then(function Succes(resp) {
                    if (resp !== undefined && typeof resp === "object") {
                        $scope.result = resp.data.result;
                        if ($scope.result.code === nologin) {
                            location.href = context + adm_login_uri;
                        } else {
                            if ($scope.result.code === success) {
                                $scope.reloadFilter($scope.result.message);
                            }
                        }
                    }
                }, function Error(response) {
                    $scope.message = response.message;
                });
            }
        });
    };
    $scope.formatDateTime = function (date) {
        if (!angular.isUndefined(date) && date !== null)
            return $filter('date')(new Date(date), 'dd/MM/yyyy HH:mm:ss')
        else
            return "";
    };
});