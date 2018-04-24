(function () {
    var app = angular.module('imocker-app');

    /********************************** API管理 **************************************************/

    app.controller('ApiMockCtrl', function ($scope, $rootScope, $http, $filter, $location, $window, $compile) {
        $scope.apiInfo = {
            apiName : null,
            method : null,
            mockUrl: null,
            retResult : null
        };
        $scope.list = [];

        $scope.load = function (page, size, callback) {
            var apiName = $("#apiName").val();
            var method = $("#method").val();
            var operator = $("#operator").val();
            var pageSize = $("#pageLimit").val();
            $scope.pageSize = pageSize;

            var url = "api-mocks/page-query?pageNo=" + page + "&pageSize=" + pageSize +
                "&apiName=" + apiName + "&method=" + method + "&operator=" + operator ;
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
            $("#operator").val("");
        };

        $scope.create = function () {
            if (!$rootScope.username) {
                $("#username").val("");
                $("#login-modal").modal();
            } else {
                $scope.apiInfo = {"method": "GET","contentType":""};
                $("#api-mock-edit-modal").modal();
            }
        };

        $scope.save = function () {
            var id = $scope.apiInfo.id;

            $rootScope.validateInput("validate-inp-template");

            if ($rootScope.checkStatus) {
                if (confirm("确定执行?")) {
                    $scope.apiInfo.updatedBy = $rootScope.username;

                    var conditions = [];

                    $(".condition-mock").each(function () {
                        var keyObj = $(this).find(".cond-key");
                        var condKey = $(keyObj).val();
                        var typeObj = $(this).find(".cond-type");
                        var condType = $(typeObj).val();
                        var expressionObj = $(this).find(".cond-expression");
                        var condExpression = $(expressionObj).val();
                        var valueObj = $(this).find(".cond-value");
                        var condValue = $(valueObj).val();
                        var retObj = $(this).find(".mock-ret-value");
                        var mockRetValue = $(retObj).val();

                        var condObj = {};
                        condObj.condKey = condKey;
                        condObj.condType = condType;
                        condObj.condExpression = condExpression;
                        condObj.condValue = condValue;
                        condObj.mockRetValue = mockRetValue;
                        conditions.push(condObj);
                    });

                    $scope.apiInfo.apiConditionList = conditions;

                    if (!id) {
                        /*创建*/
                        $scope.apiInfo.createdBy = $rootScope.username;
                        $scope.apiInfo.status = 1;
                        $http.post("api-mocks", $scope.apiInfo).success(function (ret) {
                            if (ret.retCode == "00") {
                                $("#api-mock-edit-modal").modal('hide');
                                $window.location.reload();
                            } else {
                                alert(ret.retMsg);
                            }
                        });
                    } else {
                        /*编辑*/
                        $scope.apiInfo.createdAt = null;
                        $scope.apiInfo.updatedAt = null;
                        $http.post("api-mocks/" + id, $scope.apiInfo).success(function (ret) {
                            if (ret.retCode == "00") {
                                $("#api-mock-edit-modal").modal('hide');
                                $window.location.reload();
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
                $http.get("api-mocks/" + id).success(function (ret) {
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
                    $http.delete("api-mocks/" + id).success(function (ret) {
                        if (ret.retCode == "00") {
                            $window.location.reload();
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
            $(document.body).append(form);
            // 提交表单
            form.submit();
        };


        var conditionTemplate = "<div class='condition-mock' style='display: flex;align-items: center;margin-top: 10px;'>"
            + "<input type='text' placeholder='参数名' style='width:200px;display: inline;' required='true' class='form-control validate-inp-template cond-key' />"
            + "<select class='form-control cond-type' style='width:150px;margin-left:5px;display: inline;'>"
            + "<option value='String'>String</option>"
            + "<option value='Integer'>Integer</option>"
            + "<option value='Long'>Long</option>"
            + "<option value='BigDecimal'>BigDecimal</option>"
            + "<option value='Boolean'>Boolean</option>"
            + "</select>"
            + "<select class='form-control cond-expression' style='width:150px;margin-left:5px;display: inline;'>"
            + "<option value='=='>等于</option>"
            + "<option value='>'>大于</option>"
            + "<option value='<'>小于</option>"
            + "<option value='>='>大于等于</option>"
            + "<option value='<='>小于等于</option>"
            + "<option value='!='>不等于</option>"
            + "</select>"
            + "<input type='text' placeholder='比较值' style='width:200px;margin-left:5px;display: inline;' required='true' class='form-control validate-inp-template cond-value' />"
            + "<textarea placeholder='返回值' style='margin-left:5px;width:400px;display: inline;' class='form-control mock-ret-value'></textarea>"
            + "<button style='margin-left: 5px;' ng-click='removeCondition($event.target)' class='btn btn-default glyphicon glyphicon-minus condition-minus'></button>"
            + "</div>";

        $scope.addCondition = function () {
            var $condition = $compile(conditionTemplate)($scope);
            $condition.appendTo($("#conditionContainer"));
        };

        $scope.removeCondition = function (target) {
            $(target).parent().remove();
        };

        $scope.cancel = function () {
            $window.location.reload();
        };

    });

}());