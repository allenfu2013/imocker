(function () {
    var app = angular.module('imocker-app');

    /********************************** API测试 **************************************************/

    app.controller('ApiTestCtrl', function ($scope, $rootScope, $http, $filter) {
        $scope.apiInfo = {};
        $scope.remoteCallInfo = {};
        $scope.apiInfo.qaUrl = "http://localhost:8081/imocker/api/ping";
        $scope.apiInfo.method = "GET";
        $scope.postType = "1";

        $scope.send = function () {
            $scope.remoteCallInfo.method = $scope.apiInfo.method;
            $scope.remoteCallInfo.url = $scope.apiInfo.qaUrl;

            $http.post("api/postman", $scope.remoteCallInfo).success(function (ret) {
                $("#callResult").val(angular.toJson(ret));
            });
        };

        $scope.addHeaders = function () {
            console.log("add headers");
            var headerModel = "<div style='margin-top: 10px;'><input type='text' class='form-control' style='width:200px;display: inline;' placeholder='key'><input type='text' class='form-control' style='width:200px;margin-left:5px;display: inline;' placeholder='value'> <button class='btn btn-default glyphicon glyphicon-minus header-minus'></button></div>";
            $("#headersContainer").append(headerModel);
            $(".header-minus").unbind("click");
            $(".header-minus").click(function () {
                $(this).parent().remove();
            });
        };

        $scope.addParams = function ($compile) {
            console.log("add params");
            var paramTemplate = "<div style='margin-top: 10px;'><input type='text' class='form-control' style='width:200px;display: inline;' placeholder='key'><input type='text' class='form-control' style='width:200px;margin-left:5px;display: inline;' placeholder='value'> <button class='btn btn-default glyphicon glyphicon-minus param-minus'></button></div>";
            $("#paramsContainer").append(paramTemplate);
            $(".param-minus").unbind("click");
            $(".param-minus").click(function () {
                $(this).parent().remove();
            });
        };


    });

}());