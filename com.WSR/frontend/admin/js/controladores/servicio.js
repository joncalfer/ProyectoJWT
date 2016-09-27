concepcion.controller('ctrlServicios', function($scope, servConexion, $rootScope, $location, $timeout, $route, $routeParams) {
	$scope.insertarS = {};
	$scope.listo = false;
	

	$scope.cargarServicioS = function() {
		servConexion.get('/servicios')
			.then(function(datos) {
				$scope.servicios = datos;
				$scope.listo = true;
				
			})
			.catch(function(status) {
				console.error(status);
			});
	}

	$scope.cargarModificarS = function(id) {
		$scope.listo = false;
		servConexion.get('/servicios/' + id)
			.then(function(datos) {
				$location.path('/servicios/editar');
				$rootScope.actualizarS = datos;
				$scope.listo = true;
			})
			.catch(function(status) {
				console.error(status);
			});
	}

	$scope.agregarS = function() {
		
		console.log($scope.insertarS);
		if ($scope.insertarS.nombre && $scope.insertarS.descripcion && $scope.insertarS.imagen.data) {
			datos = {
				nombre: $scope.insertarS.nombre,
				descripcion: $scope.insertarS.descripcion,
				imagen: $scope.insertarS.imagen.data
				
			};
			
			servConexion.post('/servicios', datos)
				.then(function(datos) {
					if (datos.msg) {
						$rootScope.mensaje(datos.msg);
					} else {
						console.log(datos);
						$rootScope.mensaje('Agregado correctamente');
					}
					$timeout(function() {
						$location.path('/servicios');
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

	
	$scope.modificarS = function() {
		if ($rootScope.actualizarS.descripcion && $rootScope.actualizarS.nombre) {
			datos = {
				id: $rootScope.actualizarS.id,
				nombre: $rootScope.actualizarS.nombre,
				descripcion: $rootScope.actualizarS.descripcion
			};
			
			if ($rootScope.actualizarS.imagen) {
				datos.imagen = $rootScope.actualizarS.imagen.data;
			}
			console.log(datos);
			servConexion.put('/servicios', datos)
				.then(function(datos) {
					if (datos.msg == true) {
						$rootScope.mensaje('Cambios guardados');
						$timeout(function() {
							$location.path('/servicios');
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

	$scope.eliminarS = function() {
		if ($scope.idEliminar) {
			console.log($scope.idEliminar);
			servConexion.delete('/servicios', {
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