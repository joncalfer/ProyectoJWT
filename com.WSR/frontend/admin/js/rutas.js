
var roles = { secretaria: '0',
              medico: '1'
            };

concepcion.config(function($routeProvider) {
    $routeProvider
    .when('/', {
        title: 'Principal',
        templateUrl: 'vistas/panel.principal.html',
        //authorized: [roles.secretaria, roles.medico]
    })
    .when('/ingreso', {
        title: 'Iniciar Sesión',
        templateUrl: 'vistas/ingresar.html'
    })
   
    // usuarios del sistema
    .when('/usuarios', {
        title: 'Usuarios',
        menu: 'usuarios',
        templateUrl: 'vistas/usuarios/lista.html',
        //authorized: [roles.secretaria]
    })
    
    
    .when('/especialidades', {
        title: 'Especialidades',
        menu: 'especialidades',
        templateUrl: 'vistas/especialidades/lista.html',
        //authorized: [roles.secretaria]
    })
    .when('/especialidades/nuevo', {
        title: 'Agregar Especialidad',
        menu: 'especialidades',
        templateUrl: 'vistas/especialidades/nuevo.html',
        //authorized: [roles.secretaria]
    })
    .when('/especialidades/editar', {
        title: 'Modificar Especialidad',
        menu: 'especialidades',
        templateUrl: 'vistas/especialidades/editar.html',
        //authorized: [roles.secretaria]
    })
    .when('/servicios', {
        title: 'Servicios',
        menu: 'servicios',
        templateUrl: 'vistas/servicios/lista.html',
        //authorized: [roles.secretaria]
    })
    .when('/servicios/nuevo', {
        title: 'Agregar Servicio',
        menu: 'servicios',
        templateUrl: 'vistas/servicios/nuevo.html',
        //authorized: [roles.secretaria]
    })
    .when('/servicios/editar', {
        title: 'Modificar Servicio',
        menu: 'servicios',
        templateUrl: 'vistas/servicios/editar.html',
        //authorized: [roles.secretaria]
    })
    .when('/servicios', {
        title: 'Servicios',
        menu: 'servicios',
        templateUrl: 'vistas/servicios/lista.html',
        //authorized: [roles.secretaria]
    })
    .when('/servicios/nuevo', {
        title: 'Agregar Servicio',
        menu: 'servicios',
        templateUrl: 'vistas/servicios/nuevo.html',
        //authorized: [roles.secretaria]
    })
    .when('/servicios/editar', {
        title: 'Modificar Servicio',
        menu: 'servicios',
        templateUrl: 'vistas/servicios/editar.html',
        //authorized: [roles.secretaria]
    })
   
    .when('/usuarios/nuevo', {
        title: 'Agregar Usuario',
        menu: 'usuarios',
        templateUrl: 'vistas/usuarios/nuevo.html',
        //authorized: [roles.secretaria]
    })
    .when('/usuarios/editar', {
        title: 'Modificar Usuario',
        menu: 'usuarios',
        templateUrl: 'vistas/usuarios/editar.html',
        //authorized: [roles.secretaria]
    })
    .when('/usuarios/:cedula/ver', {
        title: 'Ver Usuario',
        menu: 'usuarios',
        templateUrl: 'vistas/user.detail.html',
        //authorized: [roles.secretaria]
    })
     // rutas especiales
    .when('/prohibido', {
        title: '401',
        templateUrl: 'vistas/401.html'
    })
    .otherwise({
        title: '404',
		templateUrl: 'vistas/404.html'
	});
});