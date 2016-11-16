(function () {
    var app = angular.module('imocker-app');

    /********************************** API管理 **************************************************/

    app.controller('ApiManageCtrl', function ($scope, $rootScope, $http, $filter) {
        $scope.apiInfo = {};
        $scope.list = [];

        $scope.load = function (page, size, callback) {
            var apiName = $("#apiName").val();
            var status = $("#status").val();
            var pageSize = $("#pageLimit").val();
            $scope.pageSize = pageSize;

            var url = "rest/admin/api-manage/list?pageNo=" + page + "&pageSize=" + pageSize +
                "&apiName=" + apiName + "&status=" + status;
            $http.get(url).success(function (data) {
                callback && callback(data);
            });
        };

        $scope.search = function () {
            $scope.getData(1);
        };

        $scope.reset = function () {
            $("#apiName").val("");
            $("#status").val("");
        };

        $scope.create = function () {
            if (!$rootScope.username) {
                $("#username").val("");
                $("#login-modal").modal();
            } else {
                $scope.apiInfo = {};
                $("#api-manage-edit-modal").modal();
            }
        };

        $scope.save = function () {
            var id = $scope.apiInfo.id;

            $rootScope.validateInput("validate-inp-template");

            if ($rootScope.checkStatus) {
                if (confirm("确定执行?")) {
                    $scope.notifyTemplate.updatedBy = $rootScope.username;

                    if (!id) {
                        /*创建*/
                        $scope.apiInfo.createdBy = $rootScope.username;
                        $http.post("rest/admin/api-manage/add", $scope.apiInfo).success(function (data) {
                            $("#api-manage-edit-modal").modal('hide');
                            $scope.getData(1);
                        });
                    } else {
                        /*编辑*/
                        $scope.apiInfo.createdAt = null;
                        $scope.apiInfo.updatedAt = null;
                        $http.post("rest/admin/api-manage/edit/" + id, $scope.apiInfo).success(function (data) {
                            $("#api-manage-edit-modal").modal('hide');
                            $scope.getData(1);
                        });
                    }
                }
            }
        };

        $scope.edit = function (id) {
            if (!$rootScope.username) {
                $("#username").val("");
                $("#login-modal").modal();
            } else {
                $scope.notifyTemplate = {};
                $rootScope.clearInputWarn();
                $http.get("rest/admin/api-manage/get/" + id).success(function (data) {
                    $scope.apiInfo = data;
                    $("#api-manage-edit-modal").modal();
                });
            }
        };

        $scope.delete = function (id) {
            if (!$rootScope.username) {
                $("#username").val("");
                $("#login-modal").modal();
            } else {
                if (confirm("确定执行?")) {
                    $http.delete("rest/admin/api-manage/delete/" + id).success(function (data) {
                        $scope.getData(1);
                    });
                }
            }
        };

    });

}());