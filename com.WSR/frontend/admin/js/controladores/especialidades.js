concepcion.controller('ctrlEspecialidades', function($scope, servConexion, $rootScope, $location, $timeout, $route, $routeParams) {
	$scope.insertarE = {};
	$scope.listo = false;
	

	$scope.cargarEspecialidadesE = function() {
		servConexion.get('/especialidades?filtro=todosActivos')
			.then(function(datos) {
				$scope.especialidades = datos;
				$scope.listo = true;
			})
			.catch(function(status) {
				console.error(status);
			});
	}

	$scope.cargarModificarE = function(id) {
		$scope.listo = false;
		servConexion.get('/especialidades/' + id)
			.then(function(datos) {
				$location.path('/especialidades/editar');
				$rootScope.actualizarE = datos;
				$scope.listo = true;
			})
			.catch(function(status) {
				console.error(status);
			});
	}

	$scope.agregarE = function() {
		console.log("holi");
		console.log($scope.insertarE);
		if ($scope.insertarE.nombre && $scope.insertarE.descripcion && $scope.insertarE.imagen.data) {
			datos = {
				nombre: $scope.insertarE.nombre,
				descripcion: $scope.insertarE.descripcion,
				imagen: $scope.insertarE.imagen.data
				
			};
			
			servConexion.post('/especialidades', datos)
				.then(function(datos) {
					if (datos.msg) {
						$rootScope.mensaje(datos.msg);
					} else {
						console.log(datos);
						$rootScope.mensaje('Agregado correctamente');
					}
					$timeout(function() {
						$location.path('/especialidades');
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

	
	$scope.modificarE = function() {
		if ($rootScope.actualizarE.descripcion && $rootScope.actualizarE.nombre) {
			datos = {
				id: $rootScope.actualizarE.id,
				nombre: $rootScope.actualizarE.nombre,
				descripcion: $rootScope.actualizarE.descripcion
			};
			
			if ($rootScope.actualizarE.imagen) {
				datos.imagen = $rootScope.actualizarE.imagen.data;
			}
			console.log(datos);
			servConexion.put('/especialidades', datos)
				.then(function(datos) {
					if (datos.msg == true) {
						$rootScope.mensaje('Cambios guardados');
						$timeout(function() {
							$location.path('/especialidades');
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

	$scope.eliminarE = function() {
		if ($scope.idEliminar) {
			console.log($scope.idEliminar);
			servConexion.delete('/especialidades', {
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
			$rootScope.mensaje('¡No fue posible elimnar la especialidad!');
		}
	}

});