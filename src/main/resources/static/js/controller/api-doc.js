(function () {
    var app = angular.module('imocker-app');

    /********************************** API文档 **************************************************/

    app.controller('ApiDocCtrl', function ($scope, $rootScope, $http, $filter, $location, $compile) {

        $scope.apiDoc = {
            project: null,
            apiName: null,
            apiDesc: null,
            apiMethod: null,
            apiHeaders: [],
            apiParameters: [],
            apiParamExample: null,
            apiResponseBodies: [],
            apiResponseExample: null,
            apiErrors: [],
            testUrl: null
        };

        var apiDocId = $location.url().substr(14);
        if (apiDocId) {
            $http.get("manage/api-docs/" + apiDocId).success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.apiDoc = ret.data;
                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        }

        var headerTemplate = "<div class='api-header' style='display: flex;align-items: center;margin-top: 10px;'>"
            + "<input type='text' class='form-control header-key' style='width:200px;display: inline;' placeholder='key'>"
            + "<input type='text' class='form-control header-value' style='width:400px;margin-left:5px;display: inline;' placeholder='value'>"
            + "<button style='margin-left: 5px;' ng-click='removeHeader($event.target)' class='btn btn-default glyphicon glyphicon-minus'></button>"
            + "<button style='margin-left: 5px;' ng-click='addHeaderAfter($event.target)' class='btn btn-default glyphicon glyphicon-plus'></button>"
            + "</div>";

        $scope.addHeaders = function () {
            var $header = $compile(headerTemplate)($scope);
            $header.appendTo($("#headersContainer"));
        };

        $scope.addHeaderAfter = function (target) {
            var $header = $compile(headerTemplate)($scope);
            $header.insertAfter($(target).parent());
        };

        $scope.removeHeader = function (target) {
            $(target).parent().remove();
        };

        var paramTemplate = "<div class='api-param' style='display: flex;align-items: center;margin-top: 10px;'>"
            + "<input type='text' class='form-control param-key' style='width:200px;display: inline;' placeholder='参数名称'>"
            + "<select class='form-control param-type' style='width:100px;margin-left:5px;display: inline;'>"
            + "<option value='String'>String</option>"
            + "<option value='Integer'>Integer</option>"
            + "<option value='Long'>Long</option>"
            + "<option value='BigDecimal'>BigDecimal</option>"
            + "<option value='Boolean'>Boolean</option>"
            + "<option value='Object'>Object</option>"
            + "<option value='Array'>Array</option>"
            + "</select>"
            + "<select class='form-control param-required' style='width:100px;margin-left:5px;display: inline;'>"
            + "<option value='1'>必须</option>"
            + "<option value='0'>非必须</option>"
            + "</select>"
            + "<input type='text' class='form-control param-parent' style='width:250px;margin-left:5px;display: inline;' placeholder='父元素'>"
            + "<input type='text' class='form-control param-desc' style='width:250px;margin-left:5px;display: inline;' placeholder='参数说明'>"
            + "<button style='margin-left: 5px;' ng-click='removeParam($event.target)' class='btn btn-default glyphicon glyphicon-minus'></button>"
            + "<button style='margin-left: 5px;' ng-click='addParamAfter($event.target)' class='btn btn-default glyphicon glyphicon-plus'></button>"
            + "</div>";

        $scope.addParams = function () {
            var $param = $compile(paramTemplate)($scope);
            $param.appendTo($("#paramsContainer"));
        };

        $scope.addParamAfter = function (target) {
            var $param = $compile(paramTemplate)($scope);
            $param.insertAfter($(target).parent());
        };

        $scope.removeParam = function (target) {
            $(target).parent().remove();
        };

        var responseTemplate = "<div class='api-response' style='display: flex;align-items: center;margin-top: 10px;'>"
            + "<input type='text' class='form-control response-key' style='width:200px;display: inline;' placeholder='参数名称'>"
            + "<select class='form-control response-type' style='width:100px;margin-left:5px;display: inline;'>"
            + "<option value='String'>String</option>"
            + "<option value='Integer'>Integer</option>"
            + "<option value='Long'>Long</option>"
            + "<option value='BigDecimal'>BigDecimal</option>"
            + "<option value='Boolean'>Boolean</option>"
            + "<option value='Object'>Object</option>"
            + "<option value='Array'>Array</option>"
            + "</select>"
            + "<select class='form-control response-required' style='width:100px;margin-left:5px;display: inline;'>"
            + "<option value='1'>必须</option>"
            + "<option value='0'>非必须</option>"
            + "</select>"
            + "<input type='text' class='form-control response-parent' style='width:250px;margin-left:5px;display: inline;' placeholder='父元素'>"
            + "<input type='text' class='form-control response-desc' style='width:250px;margin-left:5px;display: inline;' placeholder='参数说明'>"
            + "<button style='margin-left: 5px;' ng-click='removeResponse($event.target)' class='btn btn-default glyphicon glyphicon-minus'></button>"
            + "<button style='margin-left: 5px;' ng-click='addResponseAfter($event.target)' class='btn btn-default glyphicon glyphicon-plus'></button>"
            + "</div>";

        $scope.addResponse = function () {
            var $response = $compile(responseTemplate)($scope);
            $response.appendTo($("#responseContainer"));
        };

        $scope.addResponseAfter = function (target) {
            var $response = $compile(responseTemplate)($scope);
            $response.insertAfter($(target).parent());
        };

        $scope.removeResponse = function (target) {
            $(target).parent().remove();
        };

        var errorTemplate = "<div class='api-error' style='display: flex;align-items: center;margin-top: 10px;'>"
            + "<input type='text' class='form-control http-status' style='width:200px;display: inline;' placeholder='http status'>"
            + "<input type='text' class='form-control business-code' style='width:200px;margin-left:5px;display: inline;' placeholder='business code'>"
            + "<input type='text' class='form-control error-msg' style='width:200px;margin-left:5px;display: inline;' placeholder='error message'>"
            + "<button style='margin-left: 5px;' ng-click='removeError($event.target)' class='btn btn-default glyphicon glyphicon-minus'></button>"
            + "<button style='margin-left: 5px;' ng-click='addErrorAfter($event.target)' class='btn btn-default glyphicon glyphicon-plus'></button>"
            + "</div>";

        $scope.addError = function () {
            var $error = $compile(errorTemplate)($scope);
            $error.appendTo($("#errorContainer"));
        };

        $scope.addErrorAfter = function () {
            var $error = $compile(errorTemplate)($scope);
            $error.insertAfter($(target).parent());
        };

        $scope.removeError = function (target) {
            $(target).parent().remove();
        };

        $scope.addMock = function () {
            $("#conditionContainer").empty();
            $("#api-doc-mock-edit-modal").modal();
        };

        $scope.addCondition = function () {
            var conditionTemplate = ""
                + "<tr>"
                + "<td>"
                + "<select id='method' class='form-control'>"
                + "<option value='name'>name</option>"
                + "<option value='cardNo'>cardNo</option>"
                + "<option value='phone'>phone</option>"
                + "</select>"
                + "</td>"
                + "<td>"
                + "<select id='' class='form-control'>"
                + "<option value='>'>大于</option>"
                + "<option value='='>等于</option>"
                + "<option value='<'>小于</option>"
                + "<option value='>='>大于等于</option>"
                + "<option value='<='>小于等于</option>"
                + "<option value='!='>不等于</option>"
                + "</select>"
                + "</td>"
                + "<td>"
                + "<input id='apiNameEdit' type='text' placeholder='' required='true' class='form-control validate-inp-template' />"
                + "</td>"
                + "<td>"
                + "<button class='btn btn-default glyphicon glyphicon-minus condition-minus'></button>"
                + "</td>"
                + "</tr>";

            $("#conditionContainer").append(conditionTemplate);

            $(".condition-minus").unbind("click");
            $(".condition-minus").click(function () {
                $(this).parent().parent().remove();
            });
        };

        $scope.saveDoc = function () {
            if (!$rootScope.username) {
                $("#username").val("");
                $("#login-modal").modal();
                return;
            }

            $rootScope.validateInput("validate-inp-template");
            if ($rootScope.checkStatus) {

                var headers = [];

                $(".api-header").each(function () {
                    var keyObj = $(this).find(".header-key");
                    var headerKey = $(keyObj).val();
                    var valueObj = $(this).find(".header-value");
                    var headerValue = $(valueObj).val();

                    var headerObj = {};
                    headerObj.headerKey = headerKey;
                    headerObj.headerValue = headerValue;
                    headers.push(headerObj);
                });

                $scope.apiDoc.apiHeaders = headers;

                var params = [];
                $(".api-param").each(function () {
                    var keyObj = $(this).find(".param-key");
                    var paramKey = $(keyObj).val();
                    var typeObj = $(this).find(".param-type");
                    var paramType = $(typeObj).val();
                    var requiredObj = $(this).find(".param-required");
                    var paramRequired = $(requiredObj).val();
                    var parentObj = $(this).find(".param-parent");
                    var paramParent = $(parentObj).val();
                    var descObj = $(this).find(".param-desc");
                    var paramDesc = $(descObj).val();

                    var paramObj = {};
                    paramObj.paramKey = paramKey;
                    paramObj.paramType = paramType;
                    paramObj.paramRequired = paramRequired;
                    paramObj.paramParent = paramParent;
                    paramObj.paramDesc = paramDesc;
                    params.push(paramObj);
                });
                $scope.apiDoc.apiParameters = params;

                var responses = [];
                $(".api-response").each(function () {
                    var keyObj = $(this).find(".response-key");
                    var responseKey = $(keyObj).val();
                    var typeObj = $(this).find(".response-type");
                    var responseType = $(typeObj).val();
                    var requiredObj = $(this).find(".response-required");
                    var responseRequired = $(requiredObj).val();
                    var parentObj = $(this).find(".response-parent");
                    var responseParent = $(parentObj).val();
                    var descObj = $(this).find(".response-desc");
                    var responseDesc = $(descObj).val();

                    var responseObj = {};
                    responseObj.responseKey = responseKey;
                    responseObj.responseType = responseType;
                    responseObj.responseRequired = responseRequired;
                    responseObj.responseParent = responseParent;
                    responseObj.responseDesc = responseDesc;
                    responses.push(responseObj);
                });
                $scope.apiDoc.apiResponseBodies = responses;

                var errors = [];
                $(".api-error").each(function () {
                    var statusObj = $(this).find(".http-status");
                    var httpStatus = $(statusObj).val();
                    var codeObj = $(this).find(".business-code");
                    var businessCode = $(codeObj).val();
                    var msgObj = $(this).find(".error-msg");
                    var errorMsg = $(msgObj).val();

                    var errorObj = {};
                    errorObj.httpStatus = httpStatus;
                    errorObj.businessCode = businessCode;
                    errorObj.errorMsg = errorMsg;
                    errors.push(errorObj);
                });
                $scope.apiDoc.apiErrors = errors;
                $scope.apiDoc.createdBy = $rootScope.username;
                $scope.apiDoc.updatedBy = $rootScope.username;

                var apiDocId = $scope.apiDoc.id;
                if (!apiDocId) {
                    //创建
                    $http.post("manage/api-docs", $scope.apiDoc).success(function (ret) {
                        if (ret.retCode == "00") {
                            $location.path("/api-docs/" + ret.data.id);
                        } else {
                            alert(ret.retMsg);
                        }
                    });
                } else {
                    //编辑
                    $http.post("manage/api-docs/" + apiDocId, $scope.apiDoc).success(function (ret) {
                        if (ret.retCode == "00") {
                            $location.path("/api-docs/" + ret.data.id);
                        } else {
                            alert(ret.retMsg);
                        }
                    });
                }



            }

        };
    });

    app.controller('ApiDocDetailsCtrl', function ($scope, $rootScope, $http, $filter, $location) {
        var apiDocId = $location.url().substr(10);
        if (apiDocId) {
            $http.get("manage/api-docs/" + apiDocId).success(function (ret) {
                if (ret.retCode == "00") {
                    $scope.apiDoc = ret.data;
                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        }

        $scope.apiDoc = {};
    });

    app.controller('ApiDocListCtrl', function ($scope, $rootScope, $http, $filter, $location) {
        $scope.apiDoc = {};
        $scope.list = [];

        $scope.load = function (page, size, callback) {
            var project = $("#project").val();
            var apiName = $("#apiName").val();
            var operator = $("#operator").val();
            var pageSize = $("#pageLimit").val();
            $scope.pageSize = pageSize;

            var url = "manage/api-docs/page-query?pageNo=" + page + "&pageSize=" + pageSize +
                "&project=" + project + "&apiName=" + apiName + "&operator=" + operator ;
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
            $("#project").val("");
            $("#apiName").val("");
            $("#operator").val("");
        };

        $scope.createDoc = function () {
            if (!$rootScope.username) {
                $("#username").val("");
                $("#login-modal").modal();
            } else {
                $location.path("/api-doc-edit");
            }

        };

        $scope.edit = function (id) {
            $location.path("/api-doc-edit/" + id);
        };

        $scope.show = function (id) {
            $http.get("manage/api-docs/" + id).success(function (retObj) {
                if (retObj.retCode == "00") {
                    $scope.apiDoc = retObj.data;
                    $("#api-docs-details-modal").modal();
                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        };

        $scope.delete = function (id) {
            if (!$rootScope.username) {
                $("#username").val("");
                $("#login-modal").modal();
            } else {
                if (confirm("确定执行?")) {
                    $http.delete("manage/api-docs/" + id).success(function (ret) {
                        if (ret.retCode == "00") {
                            $scope.getData(1);
                        } else {
                            alert(ret.retMsg);
                        }
                    });
                }
            }
        };

        $scope.showDocPage = function () {
            $location.path("/api-docs-directory/");
        };
    });

    app.controller('ApiDocDirectoryCtrl', function ($scope, $rootScope, $http, $filter, $location) {
        $scope.apiDocList = {};
        $scope.apiDoc = {};

        var project = $location.url().substr(20);
        $http.get("manage/api-docs?project=" + project).success(function (retList) {
            if (retList.retCode == "00") {
                $scope.apiDocList = retList.data;
                if ($scope.apiDocList.length > 0) {
                    var apiDocId = $scope.apiDocList[0].id;
                    $http.get("manage/api-docs/" + apiDocId).success(function (retObj) {
                        if (retObj.retCode == "00") {
                            $scope.apiDoc = retObj.data;
                        } else {
                            alert("服务器异常, 请联系系统管理员");
                        }
                    });
                }
            } else {
                alert("服务器异常, 请联系系统管理员");
            }
        });

        $scope.show = function (id) {
            $(this).addClass("active");
            console.log($(this));
            $http.get("manage/api-docs/" + id).success(function (retObj) {
                if (retObj.retCode == "00") {
                    $scope.apiDoc = retObj.data;
                } else {
                    alert("服务器异常, 请联系系统管理员");
                }
            });
        }
    });

}());