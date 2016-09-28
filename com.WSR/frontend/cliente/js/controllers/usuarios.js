concepcion.controller('ctrlUsuarios', function(apiService, $scope, $rootScope, $filter, $timeout, $location) {
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

	

	$scope.registrar = function() {

		
		if ($scope.insertarU.nombre && $scope.insertarU.apellidos && $scope.insertarU.email && $scope.insertarU.contrasena) {
			datos = {
				nombre: $scope.insertarU.nombre,
				apellidos: $scope.insertarU.apellidos,
				nombreUsuario: $scope.insertarU.nombreUsuario,
				email: $scope.insertarU.email,
				contrasena: $scope.insertarU.contrasena,
				roles: "rol",
				
			};
			
			apiService.postToApi('/registrar', datos)
				.then(function(datos) {
					if (datos.status == 200) {
						 Materialize.toast(datos.message, 800);
					} else {
						
						Materialize.toast(datos.message, 800);
					}
					$timeout(function() {
						$location.path('/');
					}, 800);
				})
				.catch(function(status) {
					Materialize.toast('¡Ocurrió un problema!, por favor intente de nuevo', 2500);
					
					
				});
		} else {
			Materialize.toast('Faltan datos, complete los datos requeridos');
		}
	}


});