(function () {

    var app = angular.module('imocker-app', ['ngCookies']);

    app.config(function ($routeProvider, $httpProvider) {
        $routeProvider.when('/', {
            templateUrl: 'tpls/home.html'
        }).when('/api-mock', {
            controller: 'ApiMockCtrl',
            templateUrl: 'tpls/api-mock-list.html'
        }).when('/api-doc', {
            controller: 'ApiDocCtrl',
            templateUrl: 'tpls/api-doc-edit.html'
        }).when('/api-test', {
            controller: 'ApiTestCtrl',
            templateUrl: 'tpls/api-test.html'
        }).when('/api-test/:id', {
            controller: 'ApiTestCtrl',
            templateUrl: 'tpls/api-test.html'
        }).when('/int-test', {
            controller: 'ApiIntTestCtrl',
            templateUrl: 'tpls/int-test.html'
        }).otherwise({
            templateUrl: 'tpls/404.html'
        });

        $httpProvider.interceptors.push('httpInterceptor');
    });

    app.factory('httpInterceptor', ['$q', '$injector', function ($q, $injector) {
        var httpInterceptor = {
            'responseError': function (response) {
                if (response.status == 401) {
                    alert("请先登录后再操作!");
                    return $q.reject(response);
                } else if (response.status == 403) {
                    alert("你没有权限进行此操作!");
                    return $q.reject(response);
                } else if (response.status == 404) {
                    alert("操作不存在!");
                    return $q.reject(response);
                } else if (response.status == 500) {
                    alert("系统错误，请联系管理员(付勇)处理!");
                }
            },
            'response': function (response) {
                return response;
            }
        };
        return httpInterceptor;
    }
    ]);

}());