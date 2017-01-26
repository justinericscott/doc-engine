'use strict';

/** Angular.js Main Controller Declaration **/
var ctrlMain = angular.module('ctrlMain', []);

ctrlMain.controller('CommonFunctionsCtrl', [
    '$scope', '$window', '$location', function($scope, $window, $location) {
    	
    	$scope.cancel = function() {
    		$location.path("/doc-engine/");
    	};
    	
		$scope.clear = function() {
			$window.location.reload();
		};
    }                       
]);