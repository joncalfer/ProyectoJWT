concepcion.factory('servSesion', function($q, $rootScope, $location, servConexion, $sessionStorage) {

    return {
        // We would cache the permission for the session,
        //to avoid roundtrip to server
        //for subsequent requests

        verificarSesion: function() {
            if ($sessionStorage.usuario) {
                $rootScope.usuario = $sessionStorage.usuario;
                $rootScope.autenticado = true;
            } 
        },

        ingresar: function(sesion) {
            servConexion.post('/auth/iniciarSesion', {
                    nombreUsuario: sesion.nombreUsuario,
                    contrasena: sesion.contrasena
                })
                .then(function(data) {
                    if (data.usuario) {
                        /*Asignando el usuario al rootScope para mostar en la interfaz*/
                        $rootScope.usuario = data.usuario;
                        $rootScope.autenticado = true;

                        /*Guardando el usuario en la sesion*/
                        $sessionStorage.usuario = data.usuario;
                        $location.path('/');
                    } else {
                        console.log(data.message);
                        $rootScope.estado = data.message;
                        $rootScope.iniciando = false;
                    }
                })
                .catch(function(status) {
                    console.error(status);
                    $rootScope.estado = 'Error del Servidor';
                    $rootScope.iniciando = false;
                });
        }
        
        

    };
});