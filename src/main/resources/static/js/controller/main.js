(function () {
    var app = angular.module('imocker-app');

    /********************************** 首页 **************************************************/

    app.controller('MainController', function ($scope, $rootScope, $http, $location, $cookieStore) {
        var userInfo = $cookieStore.get("userInfo");
        $rootScope.userInfo = userInfo;

        $scope.loginRequest = {
            loginName: null,
            loginPwd: null
        };

        $scope.newPwd = null;
        $scope.confirmPwd = null;

        $scope.alertMsg = null;

        $scope.accessKeyList = null;

        $scope.activeWhen = function (value) {
            return value ? 'active' : '';
        };

        $scope.path = function () {
            return $location.url();
        };

        $scope.login = function () {

            if (!$scope.loginRequest.loginName) {
                $scope.alertMsg = "用户名不允许为空!";
                return;
            }

            if (!$scope.loginRequest.loginPwd) {
                $scope.alertMsg = "密码不允许为空!";
                return;
            }

            $http.post("login", $scope.loginRequest).success(function (ret) {
                if (ret.retCode == "00") {
                    $rootScope.userInfo = ret.data;
                    $cookieStore.put("userInfo", ret.data);
                    $location.path("/");
                } else {
                    $scope.alertMsg = "用户名或密码不正确!";
                }
            }).error(function (data, header, config, status) {
                //处理响应失败
                $scope.alertMsg = "服务器异常, 请联系系统管理员";
            });
        };

        $scope.logout = function () {
            $http.post("logout").success(function () {
                $rootScope.userInfo = null;
                $cookieStore.remove("userInfo");
                $location.path("/");
            }).error(function (data, header, config, status) {
                //处理响应失败
                alert("服务器异常, 请联系系统管理员");
            });
            ;
        };

        $scope.loginEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.login();
            }
        };

        $scope.changePwd = function () {
            if (!$scope.newPwd) {
                $scope.alertMsg = "密码不允许为空";
                return;
            }

            if (!$scope.confirmPwd) {
                $scope.alertMsg = "确认密码不允许为空";
                return;
            }

            if ($scope.newPwd != $scope.confirmPwd) {
                $scope.alertMsg = "两次密码输入不一致";
                return;
            }

            $http.post("tenants/users/change-password", {newPwd: $scope.newPwd}).success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.logout();
                }
            }).error(function (data, header, config, status) {
                //处理响应失败
                $scope.alertMsg = "服务器异常, 请联系系统管理员";
            });

        };

        $scope.showAccessKey = function () {
            $http.get("accesskeys").success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.accessKeyList = ret.data;
                    $("#accesskey-modal").modal();
                }
            }).error(function (data, header, config, status) {
                //处理响应失败
                alert("服务器异常, 请联系系统管理员");
            });
        };

        $scope.reset = function (id) {
            $http.post("accesskeys/reset/" + id).success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.showAccessKey();
                } else {
                    alert(ret.retMsg);
                }
            }).error(function (data, header, config, status) {
                //处理响应失败
                alert("服务器异常, 请联系系统管理员");
            });
        };

        $scope.lock = function (id) {
            $http.post("accesskeys/lock/" + id).success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.showAccessKey();
                } else {
                    alert(ret.retMsg);
                }
            }).error(function (data, header, config, status) {
                //处理响应失败
                alert("服务器异常, 请联系系统管理员");
            });
            ;
        };

        $scope.unlock = function (id) {
            $http.post("accesskeys/unlock/" + id).success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.showAccessKey();
                } else {
                    alert(ret.retMsg);
                }
            }).error(function (data, header, config, status) {
                //处理响应失败
                alert("服务器异常, 请联系系统管理员");
            });
            ;
        };

        $rootScope.checkStatus = true;

        $rootScope.validateInput = function (className) {
            $rootScope.checkStatus = true;
            $("." + className).each(function () {
                var value = $(this).val();
                if (value && value != '? object:null ?') {
                    $(this).removeClass("input-warn");
                } else {
                    $(this).addClass("input-warn");
                    $rootScope.checkStatus = false;
                }
            });
        };

        $rootScope.clearInputWarn = function () {
            $(".input-warn").each(function () {
                $(this).removeClass("input-warn");
            });
        };

    });

}());