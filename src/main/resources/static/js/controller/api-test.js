(function () {
    var app = angular.module('imocker-app');

    /********************************** API测试 **************************************************/

    app.controller('ApiTestCtrl', function ($scope, $rootScope, $http, $filter, $location, $window) {
        $scope.remoteCallInfo = {};
        $scope.remoteCallInfo.method = "GET";
        $scope.remoteCallInfo.postType = 1;
        $scope.paramList = [];

        $scope.certList = [];
        $scope.apiCert = {};

        $http.get("certs").success(function (ret) {
            if (ret.retCode == "00") {
                $scope.certList = ret.data;
                $scope.remoteCallInfo.certId = -1;
            } else {
                alert("服务器异常, 请联系系统管理员");
            }
        });

        var apiId = $location.url().substr(10);
        if (apiId) {
            $http.get("api-mocks/" + apiId).success(function (ret) {
                if (ret.retCode == "00") {
                    var data = ret.data;
                    $scope.remoteCallInfo.url = data.mockUrl;
                    $scope.remoteCallInfo.method = data.method;

                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        }

        $scope.addCert = function () {
            $("#api-cert-modal").modal();
        };

        $scope.send = function () {
            $rootScope.validateInput("inp-required");

            if ($scope.remoteCallInfo.postType == 2)
                $rootScope.validateInput("textarea-required");

            if ($rootScope.checkStatus) {
                var headerKeys = [];
                $(".header-key").each(function () {
                    headerKeys.push($(this).val());
                });

                var headerValues = [];
                $(".header-value").each(function () {
                    headerValues.push($(this).val());
                });

                var headers = [];
                for (var i = 0; i < headerKeys.length; i++) {
                    var headerKey = headerKeys[i];
                    var headerValue = headerValues[i];
                    headers.push({"key": headerKey, "value": headerValue});
                }

                var paramKeys = [];
                $(".param-key").each(function () {
                    paramKeys.push($(this).val());
                });

                var paramValues = [];
                $(".param-value").each(function () {
                    paramValues.push($(this).val());
                });

                var params = [];
                for (var j = 0; j < paramKeys.length; j++) {
                    var paramKey = paramKeys[j];
                    var paramValue = paramValues[j];
                    params.push({"key": paramKey, "value": paramValue});
                }

                $scope.remoteCallInfo.headers = headers;
                $scope.remoteCallInfo.params = params;

                $("#callResult").val("");
                $http.post("postman", $scope.remoteCallInfo).success(function (ret) {
                    if (ret.retCode == "00") {
                        $("#callResult").val(ret.data);
                    } else {
                        alert(ret.retMsg);
                    }
                });
            }
        };

        $scope.addHeaders = function () {
            var headerModel = "<div style='margin-top: 10px;display: flex'>" +
                "<input type='text' class='form-control header-key' style='width:200px;display: inline;' placeholder='key'>" +
                "<input type='text' class='form-control header-value' style='width:400px;margin-left:5px;display: inline;' placeholder='value'>" +
                "<button style='margin-left: 5px;' class='btn btn-default glyphicon glyphicon-minus header-minus'></button>" +
                "</div>";

            $("#headersContainer").append(headerModel);
            $(".header-minus").unbind("click");
            $(".header-minus").click(function () {
                $(this).parent().remove();
            });
        };

        $scope.addParams = function () {
            var paramTemplate = "<div style='margin-top: 10px;display: flex'>" +
                "<input type='text' class='form-control param-key' style='width:200px;display: inline;' placeholder='key'>" +
                "<input type='text' class='form-control param-value' style='width:400px;margin-left:5px;display: inline;' placeholder='value'>" +
                "<button style='margin-left: 5px;' class='btn btn-default glyphicon glyphicon-minus param-minus'></button>" +
                "</div>";

            $("#paramsContainer").append(paramTemplate);
            $(".param-minus").unbind("click");
            $(".param-minus").click(function () {
                $(this).parent().remove();
            });
        };

        $scope.deleteParam = function (target) {
            $(target).parent().remove();
        };

        $scope.deleteHeader = function (target) {
            $(target).parent().remove();
        };

        $scope.save = function () {

            $rootScope.validateInput("validate-inp-template");

            if ($rootScope.checkStatus) {

                var fd = new FormData();
                var clientStore = $("#clientStore")[0].files[0];
                var trustStore = $("#trustStore")[0].files[0];
                fd.append('clientStore', clientStore);
                fd.append('trustStore', trustStore);
                fd.append('clientKeyPwd', $scope.apiCert.clientKeyPwd);
                fd.append('trustKeyPwd', $scope.apiCert.trustKeyPwd);
                fd.append("certName", $scope.apiCert.certName);
                $http({
                    method: 'POST',
                    url: "certs",
                    data: fd,
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                }).success(function (ret) {
                    if (ret.retCode == "00") {
                        $window.location.reload();
                    } else {
                        alert(ret.retMsg);
                    }
                });
            }

        };

    });

}());