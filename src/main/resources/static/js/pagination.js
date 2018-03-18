(function () {
    var app = angular.module('imocker-app');
    app.directive('customPagination', function () {
        return {
            restrict: "EA",
            replace: true,
            templateUrl: 'tpls/pagination.html',
            link: function (scope, ele, attrs) {
                scope.currentPage = 1;//当前页数
                scope.count = 0;//总条数
                scope.pageSize = attrs.size;//分页大小
                scope.totalPage = 1;//总页数
                scope.pages = [];//分页数组
                if (!scope[attrs.method]) {
                    throw new Error('load method is undefined');
                }
                scope.next = function () {
                    if (scope.currentPage < scope.totalPage) {
                        scope.currentPage++;
                        scope.getData();
                    }
                };
                scope.prev = function () {
                    if (scope.currentPage > 1) {
                        scope.currentPage--;
                        scope.getData();
                    }
                };
                //调用
                scope.getData = function (page) {
                    if (page == '>>>' || page == '<<<')
                        return;
                    page && (scope.currentPage = page);
                    scope[attrs.method](scope.currentPage, scope.pageSize, function (data) {
                        scope.totalPage = Math.ceil(data.total / scope.pageSize);
                        if (scope.currentPage > 1 && scope.currentPage < scope.totalPage) {
                            scope.pages = [];
                            if (scope.currentPage >= 4) {
                                scope.pages.push("<<<");
                            }
                            for (var i = 2; i > 0; i--) {
                                if (scope.currentPage - i > 0) {
                                    scope.pages.push(scope.currentPage - i);
                                }
                            }
                            scope.pages.push(scope.currentPage);

                            var index = scope.currentPage + 1;
                            for (var j = 1; j <= 2; j++) {
                                if (index <= scope.totalPage) {
                                    scope.pages.push(index);
                                    index++;
                                }
                            }
                            if (index <= scope.totalPage) {
                                scope.pages.push(">>>");
                            }
                        } else if (scope.currentPage == 1 && scope.totalPage > 1) {
                            scope.pages = [];
                            scope.pages.push(scope.currentPage);
                            var index = scope.currentPage + 1;
                            for (var i = 1; i <= 2; i++) {
                                if (index <= scope.totalPage) {
                                    scope.pages.push(index);
                                    index++;
                                }
                            }
                            if (index <= scope.totalPage) {
                                scope.pages.push(">>>");
                            }
                        } else if (scope.currentPage == scope.totalPage && scope.totalPage > 1) {
                            scope.pages = [];
                            if (scope.totalPage > 2) {
                                scope.pages.push("<<<");
                            }
                            for (var i = 2; i > 0; i--) {
                                if (scope.currentPage - i > 0) {
                                    scope.pages.push(scope.currentPage - i);
                                }
                            }
                            scope.pages.push(scope.currentPage);
                        }
                        scope.list = data.data;
                        scope.total = data.total;
                    });
                };
                scope.getData();
            }
        }
    });

}());