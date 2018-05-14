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

        $scope.register = function () {
            if ($scope.tenantType == 1) {
                $scope.registerReq.tenantType = "DEFAULT";
            } else {
                $scope.registerReq.tenantType = "ORG";
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