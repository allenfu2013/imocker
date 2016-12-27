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

            var url = "api/manage/list?pageNo=" + page + "&pageSize=" + pageSize +
                "&apiName=" + apiName + "&status=" + status;
            $http.get(url).success(function (ret) {
                if(ret.retCode=="00") {
                    callback && callback(ret.data);
                }
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
                    $scope.apiInfo.updatedBy = $rootScope.username;

                    if (!id) {
                        /*创建*/
                        $scope.apiInfo.createdBy = $rootScope.username;
                        $http.post("api/manage/add", $scope.apiInfo).success(function (ret) {
                            if(ret.retCode == "00") {
                                $("#api-manage-edit-modal").modal('hide');
                                $scope.getData(1);
                            } else {
                                alert("创建失败，请联系管理员");
                            }
                        });
                    } else {
                        /*编辑*/
                        $scope.apiInfo.createdAt = null;
                        $scope.apiInfo.updatedAt = null;
                        $http.post("api/manage/edit", $scope.apiInfo).success(function (ret) {
                            if (ret.retCode == "00") {
                                $("#api-manage-edit-modal").modal('hide');
                                $scope.getData(1);
                            } else {
                                alert("更新失败，请联系管理员");
                            }
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
                $scope.apiInfo = {};
                $rootScope.clearInputWarn();
                $http.get("api/manage/get-by-id?id=" + id).success(function (ret) {
                    if (ret.retCode == "00") {
                        $scope.apiInfo = ret.data;
                        $("#api-manage-edit-modal").modal();
                    } else {
                        alert("服务器异常, 请联系系统管理员");
                    }
                });
            }
        };

        $scope.delete = function (id) {
            if (!$rootScope.username) {
                $("#username").val("");
                $("#login-modal").modal();
            } else {
                if (confirm("确定执行?")) {
                    $http.delete("api/manage/delete" + id).success(function (ret) {
                        $scope.getData(1);
                    });
                }
            }
        };

    });

}());