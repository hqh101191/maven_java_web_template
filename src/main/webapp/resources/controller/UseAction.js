/* global context, urlBase, nologin, adm_login_uri, angular */

var app = angular.module('UserActionApp', ['ui.bootstrap', "ui.select2"]);
app.controller('UserActionCtrl', function ($scope, $http, $filter) {
    $scope.reloadFilter = function (str) {
        $.blockUI({message: '<img src="' + context + '/images/indicator_48.gif" />'});
        $http({
            method: "GET",
            url: urlBase + "/user-action/rest/data",
            timeout: 10 * 1000,
            params: {
                crPage: $scope.crPage,
                user_name: $scope.user_name,
                table_action: $scope.table_action,
                action_type: $scope.action_type,
                result: $scope.result_act,
                maxRow: $scope.maxRow
            }
        }).then(function Succes(response) {
            if (response !== undefined && typeof response === "object") {
                $scope.result = response.data.result;
                if ($scope.result.code === nologin) {
                    location.href = context + adm_login_uri;
                } else {
                    $scope.datas = response.data.datas;
                    $scope.totalRow = response.data.totalRow;

                    if (!angular.isUndefined(str) && str !== '') {
                        $scope.result.message = str;
                    }
                    blink_text('callback_info', 5000);
                    $.unblockUI();
                }
            }
        }, function Error(response) {
            $.unblockUI();
            $scope.message = response.status;
        });
    };
    $scope.$watch('crPage + crPage', function () {
        $scope.reloadFilter();
    });
    $scope.pageChanged = function () {
        $scope.crPage = this.crPage;
    };
    $scope.updateMaxRow = function () {
        $scope.crPage = 1;
        $scope.reloadFilter();
    };
    $scope.reset = function () {
        location.reload();
    };
    $scope.formatDate = function (date) {
        if (!angular.isUndefined(date) && date !== null)
            return $filter('date')(new Date(date), 'dd/MM/yyyy');
        else
            return "";
    };
    $scope.formatDateTime = function (date) {
        if (!angular.isUndefined(date) && date !== null)
            return $filter('date')(new Date(date), 'dd/MM/yyyy HH:mm:ss');
        else
            return "";
    };
});