(function() {
    'use strict';

    angular
        .module('kanHomeApp')
        .config(stateConfig)
        .config(mapConfig)
        .run(['$rootScope', '$state', '$timeout', '$localStorage', '$location', '$cookieStore', 'Principal', 
        	function ($rootScope, $state, $timeout, $localStorage, $location, $cookieStore, Principal) {
        		$rootScope.$on('$stateChangeStart', function(e, toState, toParams, fromState, fromParams) {
//        			var isAuthenticated = $localStorage.isAuthenticate || false;
//        			console.log('$stateChangeStart ' + isAuthenticated);
//        			if (isAuthenticated) {
//        				return;
//        			} else {
//        				console.log(toState);
//        				if (toState.url == '/' || toState.url == '/home') {
//        					e.preventDefault();
//            				$state.go('signin');
//        				} else {
//        					$location.path(toState.url);
//        				}
//        			}
            });
        	}
        ]);

    stateConfig.$inject = ['$stateProvider'];
    mapConfig.$inject = ['uiGmapGoogleMapApiProvider'];

    function mapConfig(uiGmapGoogleMapApiProvider) {
    	uiGmapGoogleMapApiProvider.configure({
            key: 'AIzaSyAgGcn_JYRAYLrXVaidr3uLCk2GxfHOFX0'
            //v: '3.20', //defaults to latest 3.X anyhow
        });
    }
    
    function stateConfig($stateProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        });
    }
})();
