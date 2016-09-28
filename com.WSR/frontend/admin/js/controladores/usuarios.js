concepcion.controller('ctrlUsuarios', function($scope, servConexion, $rootScope, $location, $timeout, $route, $routeParams) {
	$scope.insertarU = {};
	$scope.listo = false;

	$scope.cargarUsuarios = function() {
		servConexion.get('/Usuarios/listar')
			.then(function(datos) {
				$scope.usuarios = datos.usuarios;
				$scope.listo = true;
			})
			.catch(function(status) {
				console.error(status);
			});
	}

	$scope.cargarModificar = function(id) {
		$scope.listo = false;
		console.log(id);
		servConexion.get('/Usuarios/obtener/' + id)
			.then(function(datos) {
				$location.path('/usuarios/editar');
				$rootScope.actualizarU = datos.usuario;
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
			
			servConexion.post('/Usuarios/crearUsuario', datos)
				.then(function(datos) {
					if (!datos.usuario) {
						$rootScope.mensaje(datos.msg);
					} else {
						console.log(datos);
						$rootScope.mensaje('Agregado correctamente');
					}
					$timeout(function() {
						$location.path('/usuarios');
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


	
	$scope.modificar = function() {
		if ($rootScope.actualizarU.nombreUsuario && $rootScope.actualizarU.nombre && $rootScope.actualizarU.apellidos && $rootScope.actualizarU.email && $rootScope.actualizarU.activo) {
			datos = {
				id: $rootScope.actualizarU.id,
				nombre: $rootScope.actualizarU.nombre,
				apellidos: $rootScope.actualizarU.apellidos,
				nombreUsuario: $rootScope.actualizarU.nombreUsuario,
				email: $rootScope.actualizarU.email,
				activo: $rootScope.actualizarU.activo,
				roles: "rol"
			};
			
			if ($rootScope.actualizarU.contrasena) {
				datos.contrasena = $rootScope.actualizarU.contrasena;
			}
			
			console.log(datos);
			servConexion.put('/Usuarios/actualizar/' + datos.id, datos)
				.then(function(datos) {
					if (datos.status == 200) {
						$rootScope.mensaje(datos.message);
						$timeout(function() {
							$location.path('/usuarios');
						}, 400);
					} else {
						console.log(datos);
						$rootScope.mensaje(datos.message);
					}
				})
				.catch(function(status) {
					$rootScope.mensaje('¡Ocurrió un problema!, por favor intente de nuevo');
					$rootScope.iniciando = false;
				});
		} else {
			$rootScope.mensaje('Faltan datos, complete los datos requeridos');
		}
	}

	$scope.modalEliminar = function(id) {
		$scope.idEliminar = id;
		$('#modalEliminar').openModal({
			dismissible: false
		});
	}

	$scope.eliminar = function() {
		if ($scope.idEliminar) {
			
			servConexion.delete('/Usuarios/' + $scope.idEliminar, {
					
				})
				.then(function(datos) {
					if (datos.status == 200) {
						$rootScope.mensaje(datos.message);
					} else {
						$rootScope.mensaje(datos.message);
					}
					$route.reload();
				})
				.catch(function(status) {
					$rootScope.mensaje('¡Ocurrió un problema!, por favor intente de nuevo');
				});
		} else {
			$rootScope.mensaje('¡No fue posible elimnar el usuario!');
		}
	}

});