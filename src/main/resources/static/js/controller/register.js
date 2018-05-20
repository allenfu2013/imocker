(function () {
    var app = angular.module('imocker-app');

    /********************************** Register Controller **************************************************/

    app.controller('RegisterCtrl', function ($scope, $rootScope, $http, $filter, $location) {

        $scope.tenantType = 1;
        $scope.registerStatus = false;

        $scope.registerReq = {
            id: null,
            tenantType: null,
            tenantAbbrName: null,
            tenantDisplayName: null,
            email: null,
            loginName: null,
            loginPwd: null,
            nickName: null
        };

        $scope.alertMsg = null;

        $scope.register = function () {
            if ($scope.tenantType == 1) {
                $scope.registerReq.tenantType = "DEFAULT";

                if (!$scope.registerReq.loginName) {
                    $scope.alertMsg = "用户名不允许为空！";
                    return;
                }

                if (!$scope.registerReq.loginPwd) {
                    $scope.alertMsg = "密码不允许为空！";
                    return;
                }

                if (!$scope.registerReq.nickName) {
                    $scope.alertMsg = "昵称不允许为空！";
                    return;
                }
            } else {
                $scope.registerReq.tenantType = "ORG";

                if (!$scope.registerReq.tenantAbbrName) {
                    $scope.alertMsg = "组织简称不允许为空！";
                    return;
                }

                if (!$scope.registerReq.tenantDisplayName) {
                    $scope.alertMsg = "组织名称不允许为空！";
                    return;
                }
            }

            if (!$scope.registerReq.email) {
                $scope.alertMsg = "邮箱不允许为空！";
                return;
            }


            $http.post("register", $scope.registerReq).success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.registerStatus = true;
                } else {
                    alert(ret.retMsg);
                }
            });
        };

    });

}());