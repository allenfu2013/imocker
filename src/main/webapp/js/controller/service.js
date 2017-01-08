(function () {
    var app = angular.module('imocker-app');

    /********************************** 服务 **************************************************/

    app.service('serviceDemo', function ($q, $http) {
        this.serviceData = [];

        this.getServiceData = function () {
            var deferred = $q.defer();
            var url = "";
            return $http.get(url).then(function (data) {
                this.serviceData = data;
                return $q.when(data);
            }, function (data) {
                return $q.reject(data);
            });
        }
    });

}());