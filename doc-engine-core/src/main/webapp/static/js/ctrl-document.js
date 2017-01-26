'use strcit';

var ctrlDocument = angular.module('ctrlDocument', []);
var document = [];
var body;

ctrlDocument.controller('CreateNewDocumentCtrl', [
    '$scope', '$http', function($scope, $http) {
    	
    	$scope.newDoc = {
    		projectId: null,
    		code: null
    	};
    	
    	$scope.start = function() {
    		var httpString = "/instance/create/projectId/"
    			+ $scope.newDoc.projectId + "/code/"
    			+ $scope.newDoc.code;
    		$http.post(httpString).success(
    			function( response ) {
					document = response;
    			}
			);
    	};
    	
    	$scope.isDocumentReady = function() {
    		if (document.length > 0) {
    			return true;
    		} else {
    			return false;
    		}
    	};
    }
]);

ctrlDocument.controller('GetCompiledDocumentInstanceCtrl', [
    '$scope', '$http', '$sce', function($scope, $http, $sce) {
    	$scope.getBody = function() {
    		return $sce.trustAsHtml(body);
    	};
            	
    	$scope.getDocumentBody = function() {
        	$http.get("/compiled/" + document.id).success(
				function( response ) {
					body = response;
				}
    		);    		
    	};
    	
    	$scope.getDoc = function() {
    		return document;
    	};
    	
    	$scope.isBodyReady = function() {
    		if (body != null) {
    			return true;
    		} else {
    			return false
    		}
    	};
    }
]);

ctrlDocument.controller('GetDocumentInstancesAllGridCtrl', [ 
 	'$scope', '$http', function($scope, $http) {

 		$scope.isSelected = false;
 		
 		/** Get All Document Instances Via REST **/
 		$http.get("/doc-engine/instance/instances").success(
 			function( response ) { 
 				$scope.gridOptions.data = response.instances;	
 			}
 		);

 		/** Grid Options To Display The REST Response **/
 		$scope.gridOptions = {
 			columnDefs: [
              	{ name: 'Project Number',	field: 'projectId',				width: '25%' },
              	{ name: 'Content Code',		field: 'content.contentCd',		width: '75%' }
 			],
 			enableFiltering: true,
 			enableRowHeaderSelection: false,
 			enableRowSelection: true,
 			multiSelect: false,
 			onRegisterApi: function( gridApi ) {
 				$scope.gridApi = gridApi;
 				$scope.gridApi.selection.on.rowSelectionChanged( $scope, function( row ) {
 					if ( row.isSelected ) {
 						$scope.isSelected = true;
 						document = row.entity;
 					} else {
 						$scope.isSelected = false;
 						document = null;
 					}					
 				});
 			}
 		};
 	}
]);