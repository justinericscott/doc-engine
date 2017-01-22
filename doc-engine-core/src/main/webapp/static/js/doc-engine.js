'use strict';

/** Angular.js Application Declaration **/
var docEngine = angular.module('docEngine', [
	'ngRoute',
	'ngResource',
	'ngAnimate',
	'ngSanitize',
	'ui.bootstrap',
	'ui.grid',
	'ui.grid.selection',
	'xeditable',
    'ctrlMain',
    'ctrlTemplateLibrary',
    'ctrlTokenDictionary',
	'ctrlDocument'
]);

/** Style Theme Config For XEditable **/
docEngine.run( function( editableOptions ) {
	  editableOptions.theme = 'bs3';
});

/** View Router **/
docEngine.config(['$routeProvider',
    function($routeProvider) {
    	$routeProvider
    	
    	/** Login / Home Page **/
    	.when('/', {
    		templateUrl: 'html/home/welcome.html'})
    		
    	/** Content Repository Routing **/
    	.when('/content-repository', {
    		templateUrl: 'html/content-repo/content-repository-home.html'})
    	.when('/import-export', {
    		templateUrl: 'html/content-repo/ix/import-export.html'})
    	.when('/inclusion-logic', {
    		templateUrl: 'html/content-repo/logic/inclusion-logic.html'})
    	.when('/template-library', {
    		templateUrl: 'html/content-repo/library/template-library.html',
    		controller: 'DocumentSourceModalServiceCtrl'})
    	.when('/token-dictionary', {
    		templateUrl: 'html/content-repo/dictionary/token-dictionary.html',
    		controller: 'TokenDictionaryItemModalServiceCtrl'})
    		
    	/** Create A Document Routing **/
    	.when('/create-a-document', {
    		templateUrl: 'html/doc-central/create-a-doc/create-a-document.html'})
    		
    	/** Existing Documents Routing **/
    	.when('/document-central', {
    		templateUrl: 'html/doc-central/doc-central-home.html'})
    	.when('/edit-document', {
    		templateUrl: 'html/doc-central/editor/edit-document.html'})
    	.when('/enter-document-data', {
    		templateUrl: 'html/doc-central/data-entry/enter-document-data.html'})
    	.when('/revision-history', {
    		templateUrl: 'html/doc-central/history/revision-history.html'})
    	.when('/share-print-download', {
    		templateUrl: 'html/doc-central/comms/share-print-download.html'})
			
		/** Default Behavior, Points Back To Welcome Page **/
		.otherwise({ redirectTo: '/' });
    }
]);

docEngine.controller('SecurityCtrl', [
	'$rootScope', '$scope', '$http', '$location', '$route', 
	function($rootScope, $scope, $http, $location, $route) {
	
		$scope.tab = function(route) {
			return $route.current && route === $route.current.controller;
		};
	
		var authenticate = function(callback) {
			$http.get('user').success(function(data) {
				if (data.name) {
					console.log("Found user data. Name: " + data.name);
					$rootScope.authenticated = true;
				} else {
					console.log("Did not find user data.");
//					Change back to false.
					$rootScope.authenticated = true;
				}
				callback && callback();
			}).error(function() {
//				Change back to false.
				$rootScope.authenticated = true;
				callback && callback();
			});
		};
	
		authenticate();
	
		$scope.credentials = {};
		
		$scope.login = function() {
			$http.post('login', $.param($scope.credentials), {
				headers : {
					"content-type" : "application/x-www-form-urlencoded"
				}
			}).success(function(data) {
				authenticate(function() {
					if ($rootScope.authenticated) {
						console.log("Login succeeded");
						$location.path("/");
						$scope.error = false;
						$rootScope.authenticated = true;
					} else {
						console.log("Login failed with redirect")
						$location.path("/login");
						$scope.error = true;
//						Change back to false.
						$rootScope.authenticated = true;
					}
				});
			}).error(function(data) {
				console.log("Login failed");
				$location.path("/login");
				$scope.error = true;
//				Change back to false.
				$rootScope.authenticated = true;
			});
		};
	
		$scope.logout = function() {
			$http.post('logout', {}).success(function() {
				console.log("Logout successful");
				$location.path("/");
//				Change back to false.
				$rootScope.authenticated = true;
			}).error(function(data) {
				console.log("Logout failed");
//				Change back to false.
				$rootScope.authenticated = true;
			});
		};	
	}
]);