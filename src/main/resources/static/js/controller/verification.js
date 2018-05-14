(function () {
    var app = angular.module('imocker-app');

    /********************************** Verification Controller **************************************************/

    app.controller('VerificationCtrl', function ($scope, $rootScope, $http, $filter, $location) {

        $scope.tenantType = "DEFAULT";
        $scope.applyStatus = "";
        $scope.type = null;

        $scope.list = [];

        $scope.load = function (page, size, callback) {

            var pageSize = $("#pageLimit").val();
            $scope.pageSize = pageSize;
            $scope.type = $scope.tenantType;

            var url = "tenants/page-query?pageNo=" + page + "&pageSize=" + pageSize +
                "&tenantType=" + $scope.tenantType + "&status=" + $scope.applyStatus;
            $http.get(url).success(function (ret) {
                if (ret.retCode == "00") {
                    callback && callback(ret.data);
                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        };

        $scope.search = function () {
            $scope.type = $scope.tenantType;
            $scope.getData(1);
        };

    });

}());