(function () {
    var app = angular.module('imocker-app');

    /********************************** API测试 **************************************************/

    app.controller('ApiTestCtrl', function ($scope, $rootScope, $http, $filter) {
        $scope.apiInfo = {};
        $scope.remoteCallInfo = {};
        $scope.apiInfo.qaUrl = "http://localhost:8081/imocker/api/ping";
        $scope.apiInfo.method = "GET";
        $scope.postType = null;

        $scope.send = function () {
            $scope.remoteCallInfo.method = $scope.apiInfo.method;
            $scope.remoteCallInfo.url = $scope.apiInfo.qaUrl;

            console.log($scope.postType);

            /*$http.post("api/postman", $scope.remoteCallInfo).success(function (ret) {
                $("#callResult").val(ret);
            });*/
        };
    });

}());