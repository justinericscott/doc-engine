'use strict';

/* Angular.js Main Controller Declaration */
var ctrlUtils = angular.module( 
	'ctrlUtils', 
	[]
);

ctrlUtils.controller(
	'CommonsCtrl', 
	[
		'$scope', 
		'$window', 
		'$location', 
		function(
			$scope, 
			$window, 
			$location
		) {
			$scope.cancel = function() {
				$location.path("/");
			};
			$scope.clear = function() {
				$window.location.reload();
			};
		}       
	]
);