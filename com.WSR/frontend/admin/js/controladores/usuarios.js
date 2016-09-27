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
		servConexion.get('/usuarios/' + id)
			.then(function(datos) {
				$location.path('/usuarios/editar');
				$rootScope.actualizarU = datos;
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
			
			servConexion.post('/Usuarios', datos)
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

	/*Modificar*/

	

	
	$scope.modificar = function() {
		if ($rootScope.actualizarU.telefono && $rootScope.actualizarU.nombre && $rootScope.actualizarU.apellidos && $rootScope.actualizarU.cedula && $rootScope.actualizarU.correo && (($rootScope.actualizarU.servicios.length > 0 && $rootScope.actualizarU.especialidades.length > 0) || $rootScope.actualizarU.tipo == 0)) {
			datos = {
				id: $rootScope.actualizarU.id,
				nombre: $rootScope.actualizarU.nombre,
				apellidos: $rootScope.actualizarU.apellidos,
				cedula: $rootScope.actualizarU.cedula,
				correo: $rootScope.actualizarU.correo,
				sexo: $rootScope.actualizarU.sexo,
				telefono: $rootScope.actualizarU.telefono,
				tipo: $rootScope.actualizarU.tipo
			};
			console.log($rootScope.actualizarU.tipo);
			if ($rootScope.actualizarU.tipo == 1) {
				datos.especialidades = $scope.actualizarU.especialidades;
				datos.servicios = $scope.actualizarU.servicios;
			}
			if ($rootScope.actualizarU.contrasena) {
				datos.contrasena = $rootScope.actualizarU.contrasena;
			}
			if ($rootScope.actualizarU.imagen) {
				datos.imagen = $rootScope.actualizarU.imagen.data;
			}
			console.log(datos);
			servConexion.put('/usuarios', datos)
				.then(function(datos) {
					if (datos.msg == true) {
						$rootScope.mensaje('Cambios guardados');
						$timeout(function() {
							$location.path('/usuarios');
						}, 400);
					} else {
						console.log(datos);
						$rootScope.mensaje(datos.msg);
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
			console.log($scope.idEliminar);
			servConexion.delete('/usuarios', {
					params: {
						id: $scope.idEliminar
					}
				})
				.then(function(datos) {
					if (datos.msg != true) {
						$rootScope.mensaje(datos.msg);
					} else {
						$rootScope.mensaje('Eliminado correctamente');
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