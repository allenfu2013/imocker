(function () {
    var app = angular.module('imocker-app');

    /********************************** API测试 **************************************************/

    app.controller('ApiTestCtrl', function ($scope, $rootScope, $http, $filter) {
        $scope.apiInfo = {};
        $scope.remoteCallInfo = {};
        $scope.apiInfo.method = "GET";
        $scope.postType = "1";
        $scope.headerList = [];
        $scope.paramList = [];

        $scope.send = function () {
            $scope.remoteCallInfo.method = $scope.apiInfo.method;
            $scope.remoteCallInfo.url = $scope.apiInfo.qaUrl;

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
            $http.post("api/postman", $scope.remoteCallInfo).success(function (ret) {
                $("#callResult").val(angular.toJson(ret));
            });
        };

        $scope.addHeaders = function () {
            console.log("add headers");
            var headerModel = "<div style='margin-top: 10px;'><input type='text' class='form-control header-key' style='width:200px;display: inline;' placeholder='key'><input type='text' class='form-control header-value' style='width:200px;margin-left:5px;display: inline;' placeholder='value'> <button class='btn btn-default glyphicon glyphicon-minus header-minus'></button></div>";
            $("#headersContainer").append(headerModel);
            $(".header-minus").unbind("click");
            $(".header-minus").click(function () {
                $(this).parent().remove();
            });
        };

        $scope.addParams = function () {
            console.log("add params");
            var paramTemplate = "<div style='margin-top: 10px;'><input type='text' class='form-control param-key' style='width:200px;display: inline;' placeholder='key'><input type='text' class='form-control param-value' style='width:200px;margin-left:5px;display: inline;' placeholder='value'> <button class='btn btn-default glyphicon glyphicon-minus param-minus'></button></div>";
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

    });

}());