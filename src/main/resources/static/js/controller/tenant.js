(function () {
    var app = angular.module('imocker-app');

    /********************************** Tenant Controller **************************************************/

    app.controller('TenantCtrl', function ($scope, $rootScope, $http, $filter, $location, $window) {

        $scope.tenant = {
            id: null,
            tenantType: 1,
            abbrName: null,
            displayName: null,
            email: null
        };

        $scope.loginNameReq = "";
        $scope.nickNameReq = "";
        $scope.emailReq = "";

        $scope.tenantUser = {
            loginName: null,
            nickName: null,
            email: null
        };

        $scope.list = [];

        $scope.alertMsg = null;

        $http.get("tenants").success(function (ret) {
            if (ret.retCode == "00") {
                $scope.tenant = ret.data;
            } else {
                alert("服务器异常, 请联系系统管理员");
            }
        });

        $scope.load = function (page, size, callback) {
            var pageSize = $("#pageLimit").val();
            $scope.pageSize = pageSize;

            var url = "tenants/users/page-query?pageNo=" + page + "&pageSize=" + pageSize +
                "&loginName=" + $scope.loginNameReq + "&nickName=" + $scope.nickNameReq + "&email=" + $scope.emailReq;
            $http.get(url).success(function (ret) {
                if (ret.retCode == "00") {
                    callback && callback(ret.data);
                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        };

        $scope.search = function () {
            $scope.getData(1);
        };

        $scope.showCreateUserModal = function () {
            $("#tenant-user-modal").modal();
        };

        $scope.createUser = function () {

            if (!$scope.tenantUser.loginName) {
                $scope.alertMsg = "用户名不允许为空";
                return;
            }

            if (!$scope.tenantUser.nickName) {
                $scope.alertMsg = "昵称不允许为空";
                return;
            }

            if (!$scope.tenantUser.email) {
                $scope.alertMsg = "邮箱不允许为空";
                return;
            }

            $http.post("tenants/users", $scope.tenantUser).success(function (ret) {
                if (ret.retCode == "00") {
                    $window.location.reload();
                } else {
                    alert(ret.retMsg);
                }
            });
        };

        $scope.saveTenant = function () {

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

}());