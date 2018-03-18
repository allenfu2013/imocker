(function () {
    var app = angular.module('imocker-app');

    /********************************** 首页 **************************************************/

    app.controller('MainController', function ($scope, $rootScope, $http, $location, $cookieStore) {
        var username = $cookieStore.get("username");
        $rootScope.username = username;
        $rootScope.uriPrefix = null;
        $scope.activeWhen = function (value) {
            return value ? 'active' : '';
        };

        $scope.path = function () {
            return $location.url();
        };

        $scope.showLoginModal = function () {
            $("#username").val("");
            $("#login-modal").modal();
        };

        $scope.login = function () {
            var username = $("#username").val();
            var password = $("#password").val();
            if (username) {
                $http.post("user/login", {username: username, password: password}).success(function (ret) {
                    if (ret.data == "00") {
                        $rootScope.username = username;
                        $cookieStore.put("username", username);
                        $("#login-modal").modal("hide");
                    } else {
                        alert("密码不正确!");
                    }
                }).error(function(data,header,config,status){
                    //处理响应失败
                    alert("服务器异常, 请联系系统管理员");
                });
            }
        };

        $scope.logout = function () {
            $http.post("user/logout").success(function () {
                $rootScope.username = null;
                $cookieStore.remove("username");
            }).error(function(data,header,config,status){
                //处理响应失败
                alert("服务器异常, 请联系系统管理员");
            });;
        };

        $scope.loginEnter = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.login();
            }
        };

        var checkSession = function () {
            $http.get("user/check-session").success(function (ret) {
                if(ret.retCode=="00") {
                    var sessionInfo = ret.data;
                    $rootScope.uriPrefix = sessionInfo.uriPrefix;
                    var user = sessionInfo.user;
                    if (user) {
                        $rootScope.username = user.username;
                    } else {
                        $rootScope.username = null;
                        $cookieStore.remove("username");
                    }
                }
            }).error(function(data,header,config,status){
                //处理响应失败
                alert("服务器异常, 请联系系统管理员");
            });;
        }

        checkSession();

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