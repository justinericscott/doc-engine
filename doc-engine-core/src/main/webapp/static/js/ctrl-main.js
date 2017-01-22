'use strict';

/** Angular.js Main Controller Declaration **/
var ctrlMain = angular.module('ctrlMain', []);

ctrlMain.controller('CommonFunctionsCtrl', [
    '$scope', '$window', '$location', function($scope, $window, $location) {
    	
    	$scope.cancel = function() {
    		$location.path("/");
    	};
    	
		$scope.clear = function() {
			$window.location.reload();
		};
    }                       
]);

/** USED FOR EXAMPLES ONLY - NOT TO BE USED IN REAL CODE **/

/** Simple Data Binding Example **/
ctrlMain.controller('personCtrl', function($scope) {
	$scope.firstName = "John", 
	$scope.lastName = "Doe",
	$scope.fullName = function() {
		return $scope.firstName + " " + $scope.lastName;
	}
});

/** Simple RESTful Database Consume Example **/
ctrlMain.controller('countCtrl', function($scope, $http) {
	$http.get("/examples/count").success(
		function(response) {
			$scope.count = response;
		}
	);	
});

