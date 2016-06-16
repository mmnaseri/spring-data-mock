/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/9/16)
 */
var module = angular.module("Site", ["ngRoute", "ui.bootstrap", "ui.bootstrap.tpls", "ui.router"]);
module.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('home', {
            url: "/home",
            templateUrl: "views/home.html"
        })
        .state('readme', {
            url: "/readme",
            templateUrl: "views/readme.html"
        })
        .state('roadmap', {
            url: "/roadmap",
            templateUrl: "views/roadmap.html"
        })
        .state('changelog', {
            url: "/changelog",
            templateUrl: "views/changelog.html"
        });
    $urlRouterProvider.otherwise("/home");
}]);
module.run(function ($rootScope, $location, $document) {
    $rootScope.$on('$viewContentLoading', function () {
        $rootScope.headers = [];
        $rootScope.location = $location.path();
    });
    var hash = "";
    setInterval(function () {
        if (hash != $location.hash()) {
            hash = $location.hash();
            if (!hash) {
                return;
            }
            var element = $document.find('#' + hash);
            if (!element.length) {
                return;
            }
            var top = element.offset().top;
            $('body').animate({
                scrollTop: top
            }, 100);
            element.addClass('highlighted');
            setTimeout(function () {
                element.removeClass('highlighted');
            }, 1000);
        }
    }, 100);
});
module.directive('table', function () {
    return {
        restrict: "E",
        link: function ($scope, $element) {
            $element.addClass('table');
        }
    }
});
module.directive('code', function () {
    return {
        restrict: "E",
        link: function ($scope, $element) {
            if ($element.get(0).parentNode.nodeName.toLowerCase() == 'pre') {
                angular.element($element.get(0).parentNode).addClass('hljs');
            } else {
                $element.addClass('inline');
            }
        }
    }
});
module.directive('tableOfContents', function ($templateCache, $rootScope, $location) {
    $templateCache.put('$templates/tableOfContents.html', '<p><a href="#{{location}}#{{item.id}}" target="_self">{{item.title}}</a><ul ng-if="item.children.length"><li ng-repeat="item in item.children" ng-include="\'$templates/tableOfContents.html\'"></li></ul></p>');
    return {
        restrict: "E",
        template: '<div class="table-of-contents"><ul><li ng-repeat="item in items" ng-include="\'$templates/tableOfContents.html\'"></li></ul></div>',
        replace: true,
        scope: {},
        link: function ($scope) {
            $scope.location = $location.url();
            $scope.items = $rootScope.headers;
        }
    };
});

var headerDirective = function (level) {
    var findContext = function (context) {
        if (!context.length) {
            return context;
        }
        if (level > context[context.length - 1].level) {
            return findContext(context[context.length - 1].children)
        }
        return context;
    };
    return function ($rootScope, $location) {
        return {
            restrict: "E",
            template: "<div class='header header-{{level}}'><span ng-transclude=''></span><a href='#{{location}}#{{id}}' class='pull-right'><i class='glyphicon glyphicon-link'></i></a></div>",
            replace: true,
            transclude: true,
            scope: {},
            link: function ($scope, $element) {
                var item = $element;
                var eligible = false;
                while (item.length) {
                    if (angular.isDefined(item.attr('ui-view'))) {
                        eligible = true;
                        break;
                    }
                    item = item.parent();
                }
                if (!eligible) {
                    return;
                }
                $scope.level = level;
                $scope.location = $location.url();
                var title = $scope.title = $element.text();
                var id = $scope.id = $element.attr('id');
                var context = findContext($rootScope.headers);
                context.push({
                    title: title,
                    id: id,
                    level: level,
                    children: []
                });
            }
        };
    }
};

module.directive('h1', headerDirective(1));
module.directive('h2', headerDirective(2));
module.directive('h3', headerDirective(3));
module.directive('h4', headerDirective(4));
module.directive('h5', headerDirective(5));
module.directive('h6', headerDirective(6));

module.directive('jumpTop', function () {
    return {
        restrict: "E",
        template: "<a class='jump-top' target='_self' href='#/readme'><i class='glyphicon glyphicon-chevron-up'></i></a>"
    };
});