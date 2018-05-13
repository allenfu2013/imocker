(function () {
    var app = angular.module('imocker-app');

    /********************************** Register Controller **************************************************/

    app.controller('RegisterCtrl', function ($scope, $rootScope, $http, $filter, $location) {

        $scope.registerReq = {
            id: null,
            tenantType: 1,
            tenantAbbrName: null,
            tenantDisplayName: null,
            email: null,
            loginName: null,
            loginPwd: null,
            nickName: null
        };

        $scope.register = function () {
            if ($scope.registerReq.tenantType == 1) {
                $scope.registerReq.tenantType = "DEFAULT";
            } else {
                $scope.registerReq.tenantType = "ORG";
            }

            $http.post("register", $scope.registerReq).success(function (ret) {
                if (ret.retCode == "00") {
                    console.info("done");
                } else {
                    alert(ret.retMsg);
                }
            });
        };

    });

}());