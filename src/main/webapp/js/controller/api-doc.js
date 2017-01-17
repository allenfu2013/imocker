(function () {
    var app = angular.module('imocker-app');

    /********************************** API文档 **************************************************/

    app.controller('ApiDocCtrl', function ($scope, $rootScope, $http, $filter) {

        $scope.addHeaders = function () {
            var headerTemplate = ""
                + "<div style='margin-top: 10px;'>"
                + "<input type='text' class='form-control header-key' style='width:200px;display: inline;' placeholder='key'>"
                + "<input type='text' class='form-control header-value' style='width:200px;margin-left:5px;display: inline;' placeholder='value'>"
                + "<input type='text' class='form-control header-desc' style='width:200px;margin-left:5px;display: inline;' placeholder='说明'>"
                + "<button style='margin-left: 5px;' class='btn btn-default glyphicon glyphicon-minus header-minus'></button>"
                + "</div>";
            $("#headersContainer").append(headerTemplate);
            $(".header-minus").unbind("click");
            $(".header-minus").click(function () {
                $(this).parent().remove();
            });
        };

        $scope.addParams = function() {
            var paramTemplate = ""
                + "<div style='margin-top: 10px;'>"
                + "<input type='text' class='form-control' style='width:200px;display: inline;' placeholder='参数名称'>"
                + "<select class='form-control' style='width:100px;margin-left:5px;display: inline;'>"
                + "<option value='Integer'>数字</option>"
                + "<option value='String'>字符串</option>"
                + "<option value='Boolean'>布尔</option>"
                + "</select>"
                + "<select class='form-control' style='width:100px;margin-left:5px;display: inline;'>"
                + "<option value='1'>必须</option>"
                + "<option value='0'>非必须</option>"
                + "</select>"
                + "<input type='text' class='form-control' style='width:250px;margin-left:5px;display: inline;' placeholder='参数说明'>"
                + "<button style='margin-left: 5px;' class='btn btn-default glyphicon glyphicon-minus param-minus'></button>"
                + "</div>";
            $("#paramsContainer").append(paramTemplate);
            $(".param-minus").unbind("click");
            $(".param-minus").click(function () {
                $(this).parent().remove();
            });
        };
    });

}());