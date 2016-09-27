
var concepcion = angular.module("concepcion", ['ngRoute', 'ui.materialize', 'ui.rCalendar']);

concepcion.config(['$httpProvider', function($httpProvider) {
    // MAKE ALL REQUEST POST TYPE FORM URL
    $httpProvider.defaults.headers
    .post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
}]);

concepcion.config(function($routeProvider) {
    
    $routeProvider
    .when('/', {
        templateUrl: 'views/inicio.html'
    })
    .when('/registrar', {
        templateUrl: 'views/registrar.html'
    })
    
    .otherwise({
        templateUrl: 'views/404.html'
    });
});
