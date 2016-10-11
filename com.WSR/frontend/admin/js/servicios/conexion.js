concepcion.factory('servConexion', function ($q, $http, $rootScope) {
    
    $rootScope.wss = 'http://localhost:8000/app';
    return {

        get: function (url) {
            // we will return a promise .
            var deferred = $q.defer();
            $http.get($rootScope.wss + url)
            .success(function(data) {
            	
                deferred.resolve(data);
                
                
            })
            .error(function(error, status) {
                deferred.reject(status);
            });
            return deferred.promise;
        },

        post: function(url, datos)
        {
            var deferred = $q.defer();
            $http.post($rootScope.wss + url, datos)
            .success(function(data) {
            	 
                deferred.resolve(data);
              
               
            })
            .error(function(error, status) {
                console.warn("Error " + status);
                deferred.reject(status);
            });
            return deferred.promise;
        },
        put: function(url, datos)
        {
            var deferred = $q.defer();
            $http.put($rootScope.wss + url, datos)
            .success(function(data) {
            	
                deferred.resolve(data);
                
            })
            .error(function(error, status) {
                console.warn("Error " + status);
                deferred.reject(status);
            });
            return deferred.promise;
        },

        delete: function(url, datos)
        {
            var deferred = $q.defer();
            $http.delete($rootScope.wss + url, datos)
            .success(function(data) {
            	
                deferred.resolve(data);
            })
            .error(function(error, status) {
                console.warn("Error " + status);
                deferred.reject(status);
            });
            return deferred.promise;
        }

    };
});