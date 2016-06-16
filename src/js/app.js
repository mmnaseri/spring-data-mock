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
            if ($element[0].parentNode.nodeName.toLowerCase() == 'pre') {
                angular.element($element[0].parentNode).addClass('hljs');
            } else {
                $element.addClass('inline');
            }
        }
    }
});
module.directive('tableOfContents', function ($templateCache, $rootScope) {
    $templateCache.put('$templates/tableOfContents.html', '<p><a href="#/readme#{{item.id}}">{{item.title}}</a><ul ng-if="item.children.length"><li ng-repeat="item in item.children" ng-include="\'$templates/tableOfContents.html\'"></li></ul></p>');
    return {
        restrict: "E",
        template: '<div class="table-of-contents"><ul><li ng-repeat="item in items" ng-include="\'$templates/tableOfContents.html\'"></li></ul></div>',
        replace: true,
        scope: {},
        link: function ($scope) {
            $scope.items = $rootScope.headers[0].children;
        }
    };
});

var HeaderDirective = function ($rootScope) {
    if (!$rootScope.headers) {
        $rootScope.headers = [];
    }
    var levelOf = function (node) {
        return parseInt(node.nodeName[1]);
    };
    var findContext = function (level, context) {
        if (!context.length) {
            return context;
        }
        if (level > context[context.length - 1].level) {
            return findContext(level, context[context.length - 1].children)
        }
        return context;
    };
    return {
        restrict: "E",
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
            var level = levelOf($element[0]);
            var context = findContext(level, $rootScope.headers);
            context.push({
                title: $element.text(),
                id: $element.attr('id'),
                level: level,
                children: []
            });
        }
    };
};

module.directive('h1', HeaderDirective);
module.directive('h2', HeaderDirective);
module.directive('h3', HeaderDirective);
module.directive('h4', HeaderDirective);
module.directive('h5', HeaderDirective);
module.directive('h6', HeaderDirective);
