concepcion.factory('apiService', function($q, $http, $httpParamSerializer, $rootScope) {
    $rootScope.wss = 'http://localhost:8000/app/auth';
    return {

        getFromApi: function(url) {
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

        postToApi: function(url, datos) {
            var deferred = $q.defer();
            $http.post($rootScope.wss + url, $httpParamSerializer(datos))
                .success(function(data) {
                    deferred.resolve(data);
                })
                .error(function(error) {
                    deferred.reject(error);
                });
            return deferred.promise;
        }

    };
});