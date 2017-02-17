(function () {
    var app = angular.module('imocker-app');

    /********************************** API管理 **************************************************/

    app.controller('ApiMockCtrl', function ($scope, $rootScope, $http, $filter, $location) {
        $scope.apiInfo = {};
        $scope.list = [];

        $scope.load = function (page, size, callback) {
            var apiName = $("#apiName").val();
            var method = $("#method").val();
            var pageSize = $("#pageLimit").val();
            $scope.pageSize = pageSize;

            var url = "api/manage/list?pageNo=" + page + "&pageSize=" + pageSize +
                "&apiName=" + apiName + "&method=" + method;
            $http.get(url).success(function (ret) {
                if (ret.retCode == "00") {
                    callback && callback(ret.data);
                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        };

        $scope.search = function () {
            $scope.getData(1);
        };

        $scope.reset = function () {
            $("#apiName").val("");
            $("#method").val("");
        };

        $scope.create = function () {
            if (!$rootScope.username) {
                $("#username").val("");
                $("#login-modal").modal();
            } else {
                $scope.apiInfo = {"method": "GET"};
                $("#api-mock-edit-modal").modal();
            }
        };

        $scope.createWithDoc = function () {
            $location.path("/api-doc");
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
                        $scope.apiInfo.status = 1;
                        $http.post("api/manage/add", $scope.apiInfo).success(function (ret) {
                            if (ret.retCode == "00") {
                                $("#api-mock-edit-modal").modal('hide');
                                $scope.getData(1);
                            } else {
                                alert(ret.retMsg);
                            }
                        });
                    } else {
                        /*编辑*/
                        $scope.apiInfo.createdAt = null;
                        $scope.apiInfo.updatedAt = null;
                        $http.post("api/manage/edit", $scope.apiInfo).success(function (ret) {
                            if (ret.retCode == "00") {
                                $("#api-mock-edit-modal").modal('hide');
                                $scope.getData(1);
                            } else {
                                alert(ret.retMsg);
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
                        $("#api-mock-edit-modal").modal();
                    } else {
                        alert(ret.retMsg);
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
                    $http.delete("api/manage/delete/" + id).success(function (ret) {
                        if (ret.retCode == "00") {
                            $scope.getData(1);
                        } else {
                            alert(ret.retMsg);
                        }
                    });
                }
            }
        };

        $scope.callMockApi = function (url, method) {
            // 创建Form
            var form = $('<form></form>');
            // 设置属性
            form.attr('action', url);
            form.attr('method', method);
            form.attr('target', '_blank');
            // 提交表单
            form.submit();
        };

    });

}());