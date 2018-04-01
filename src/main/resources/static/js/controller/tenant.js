(function () {
    var app = angular.module('imocker-app');

    /********************************** API文档 **************************************************/

    app.controller('TenantCtrl', function ($scope, $rootScope, $http, $filter, $location) {

        $scope.tenant = {
            id: null,
            abbrName: null,
            displayName: null,
            email: null,
            phone: null
        }

        var tenantId = $location.url().substr(8);
        if (tenantId) {
            $http.get("tenants/" + tenantId).success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.tenant = ret.data;
                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        }


        $scope.saveTenant = function () {
            console.log($scope.tenant);

            var id = $scope.tenant.id;
            if (!id) {
                // 创建
                $http.post("tenants", $scope.tenant).success(function (ret) {
                    if (ret.retCode == "00") {
                        $location.path("/tenant/details/" + ret.data.id);
                    } else {
                        alert(ret.retMsg);
                    }
                });
            } else {
                // 更新
                $http.post("tenants/" + id, $scope.tenant).success(function (ret) {
                    if (ret.retCode == "00") {
                        $location.path("/tenant/details/" + ret.data.id);
                    } else {
                        alert(ret.retMsg);
                    }
                });
            }


        };

    });


    app.controller('TenantDetailsCtrl', function ($scope, $rootScope, $http, $filter, $location) {
        $scope.tenant = {
            id: null,
            abbrName: null,
            displayName: null,
            accessKey: null,
            email: null,
            phone: null
        }

        var tenantId = $location.url().substr(16);
        if (tenantId) {
            $http.get("tenants/" + tenantId).success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.tenant = ret.data;
                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        }

        $scope.editTenant = function () {
            $location.path("/tenant/" + $scope.tenant.id);
        }
    });
}());