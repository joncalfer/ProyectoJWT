concepcion.controller('ctrlUsuarios', function(apiService, $scope, $rootScope, $filter) {
	$scope.insertarU = {};
	

	$scope.cargarUsuarios = function() {
		apiService.getFromApi('')
			.then(function(datos) {
				$scope.usuarios = datos.usuarios;
				$scope.listo = true;
			})
			.catch(function(status) {
				console.error(status);
			});
	}

	

	$scope.agregar = function() {

		console.log($scope.insertarU);
		if ($scope.insertarU.nombre && $scope.insertarU.apellidos && $scope.insertarU.correo && $scope.insertarU.contrasena) {
			datos = {
				nombre: $scope.insertarU.nombre,
				apellidos: $scope.insertarU.apellidos,
				nombreUsuario: $scope.insertarU.nombreUsuario,
				correo: $scope.insertarU.correo,
				contrasena: $scope.insertarU.contrasena,
				roles: "rol",
				
			};
			
			apiService.postToApi('/registrar', datos)
				.then(function(datos) {
					if (!datos.usuario) {
						$rootScope.mensaje(datos.msg);
					} else {
						console.log(datos);
						$rootScope.mensaje('Agregado correctamente');
					}
					$timeout(function() {
						$location.path('/');
					}, 800);
				})
				.catch(function(status) {
					$rootScope.mensaje('¡Ocurrió un problema!, por favor intente de nuevo');
					$rootScope.iniciando = false;
				});
		} else {
			$rootScope.mensaje('Faltan datos, complete los datos requeridos');
		}
	}


});