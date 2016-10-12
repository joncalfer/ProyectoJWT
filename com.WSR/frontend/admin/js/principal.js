var concepcion = angular.module("concepcion", ['ngRoute', 'ngRoleAuth', 'ngStorage', 'naif.base64', 'ui.materialize', 'ng-file-model']);


concepcion.config(['$httpProvider', function($httpProvider) {   
    $httpProvider.interceptors.push('httpRequestInterceptor');
}]);

concepcion.factory('httpRequestInterceptor', ['$sessionStorage','$location', '$rootScope',function ($sessionStorage,$location, $rootScope){
  return {
    request: function (config) {
      if ($sessionStorage.usuario) {
        config.headers['AuthToken'] = $sessionStorage.usuario.token;
        config.headers['Accept'] = 'application/json;odata=verbose';
      } else {
        $rootScope.autenticado = false;
        $location.path('/ingreso');
      }
        return config;
    }
  };
}]);

concepcion.run(['$route', '$rootScope', '$location', 'AuthService', 'servSesion', function($route, $rootScope, $location, authService, servSesion) {
    /*Verificar el estado de la sesion para no perder la ruta al recargar*/
    servSesion.verificarSesion();
    $rootScope.mensaje = function(msj) {
        Materialize.toast(msj, 2500);
    }
    $(function() {

        var barra = true;
        if ($(window).width() > 992) {
            barra = false;
        }

        $('.button-collapse').sideNav({
            edge: 'left', // Choose the horizontal origin
            closeOnClick: barra // Closes side-nav on <a> clicks, useful for Angular/Meteor
        });
    });
    /*Se sobreescribe el metodo de la libreria para adecuarlo a la forma que se necesesita*/
    authService.getRole = function() {
        if ($rootScope.usuario) {
            return $rootScope.usuario.tipo + "";
        } else {
            return '-1';
        }
    };

    $rootScope.$on('$routeChangeSuccess', function(next, current) {
        $rootScope.menuActivo = $route.current.menu;
        cambiarTitulo($route.current.title, $rootScope);
    });

    $rootScope.$on('nra.access_denied', function(event, args) {
        if ($rootScope.usuario) {
            $rootScope.route401 = args;
        } else {
            $location.path('/ingreso');
        }
    });

}]);

/*Cambiar titulo en base a la ruta*/
function cambiarTitulo(rutaActual, rootScope) {
    if (rutaActual) {
        rootScope.tituloPag = rutaActual;
    } else {
        rootScope.tituloPag = "";
    }
}