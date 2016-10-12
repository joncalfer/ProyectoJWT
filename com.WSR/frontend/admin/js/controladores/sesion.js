/*Encargado de ingresar y salir de la aplicacion*/
concepcion.controller('ctrlSesion', function(servSesion, $rootScope, $scope, $location, $sessionStorage) {
    $rootScope.iniciando = false;
    $scope.sesion = {
    	nombreUsuario: null,
    	contrasena: null
    };

	setTimeout(function() {
		if ($("input").hasClass("ng-dirty") && $("input[type=password]")) {
			$("label[for=password]").addClass("active");
		}
	}, 100);

	$scope.iniciarSesion = function () {
		if ($scope.sesion.nombreUsuario && $scope.sesion.contrasena) {
			$rootScope.iniciando = true;
			console.log('Ingresando');
			servSesion.ingresar($scope.sesion);
		} else {
			$rootScope.estado = 'Ingrese sus datos';
		}
	}

	$scope.cerrarSesion = function () {
		delete $sessionStorage.usuario;
		$rootScope.usuario = null;
    	$rootScope.autenticado = false;
    	$rootScope.iniciando = false;
    	$rootScope.loginResult = '';
    	servSesion.cerrarSesion($scope.sesion);
		console.log('Saliendo');
		$location.path('/ingreso');
	}
});