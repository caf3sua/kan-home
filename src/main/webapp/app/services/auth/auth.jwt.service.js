(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .factory('AuthServerProvider', AuthServerProvider);

    AuthServerProvider.$inject = ['$http', '$localStorage', '$sessionStorage', '$q', '$mdToast', '$translate'];

    function AuthServerProvider ($http, $localStorage, $sessionStorage, $q, $mdToast, $translate) {
        var service = {
            getToken: getToken,
            login: login,
            loginWithToken: loginWithToken,
            storeAuthenticationToken: storeAuthenticationToken,
            logout: logout
        };

        return service;

        function getToken () {
            return $localStorage.authenticationToken || $sessionStorage.authenticationToken;
        }

        function login (credentials) {
        	// MD5 + Base64
        	var passMd5 = $.md5(credentials.password);
        	var encryptPass = $.base64.btoa(passMd5);

            var data = {
                username: credentials.username,
                password: encryptPass,
                rememberMe: credentials.rememberMe
            };
            return $http.post('api/authenticate', data).success(authenticateSuccess).error(authenticateError);

            function authenticateSuccess (data, status, headers) {
                var bearerToken = headers('Authorization');
                if (angular.isDefined(bearerToken) && bearerToken.slice(0, 7) === 'Bearer ') {
                    var jwt = bearerToken.slice(7, bearerToken.length);
                    service.storeAuthenticationToken(jwt, credentials.rememberMe);
                    return jwt;
                }
            }
            
            function authenticateError(data, status, headers) {
            	console.log('authenticateError:' + JSON.stringify(data));
            	$mdToast.show(
          		      $mdToast.simple()
          		        .textContent('Đăng nhập thất bại, Làm ơn kiểm tra lại tên và mật khẩu')
          		        //.position(pinTo )
          		        .hideDelay(5000)
            	);
            }
        }

        function loginWithToken(jwt, rememberMe) {
            var deferred = $q.defer();

            if (angular.isDefined(jwt)) {
                this.storeAuthenticationToken(jwt, rememberMe);
                deferred.resolve(jwt);
            } else {
                deferred.reject();
            }

            return deferred.promise;
        }

        function storeAuthenticationToken(jwt, rememberMe) {
            if(rememberMe){
                $localStorage.authenticationToken = jwt;
            } else {
                $sessionStorage.authenticationToken = jwt;
            }
        }

        function logout () {
            delete $localStorage.authenticationToken;
            delete $sessionStorage.authenticationToken;
        }
    }
})();
