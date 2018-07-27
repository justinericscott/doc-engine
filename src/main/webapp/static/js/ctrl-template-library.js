'use strict';

/** Angular.js Controller Declaration **/
var ctrlTemplateLibrary = angular.module('ctrlTemplateLibrary', []);
var isSaved = false;
var isSelected = false;
var selectedDoc = null;
var updateUi = function(window) {
	if (isSaved) {
		window.location.reload();
		isSaved = false;
	}
}

/** Get And Display All Document Sources **/
ctrlTemplateLibrary.controller('GetDocumentSourcesAllGridCtrl', [ 
	'$scope', '$http', '$window', function($scope, $http, $window) {
		
		/** Get All Document Sources Via REST **/
		$http.get("/doc-engine/content/contents").success(
			function( response ) { 
				$scope.gridOptions.data = response.contents;	
			}
		);

		/** Grid Options To Display The REST Response **/
		$scope.gridOptions = {
			columnDefs: [
         		{ name: 'Content Code',	field: 'contentCd', 	width: '25%' },
             	{ name: 'Name',			field: 'name', 			width: '15%' },
         		{ name: 'Body',			field: 'body',		 	width: '60%' }
			],
			enableFiltering: true,
			enableRowHeaderSelection: false,
			enableRowSelection: true,
			multiSelect: false,
			onRegisterApi: function( gridApi ) {
				$scope.gridApi = gridApi;
				$scope.gridApi.selection.on.rowSelectionChanged( $scope, function( row ) {
					if ( row.isSelected ) {
						isSelected = true;
						selectedDoc = row.entity;
					} else {
						isSelected = false;
						selectedDoc = null;
					}					
				});
			}
		};
		
		
		/** Determines If A Document Has Been Selected **/
		$scope.isSelected = function () {
			return isSelected;
		};
	}
]);

/** Modal Service Config For Document Source Details **/
ctrlTemplateLibrary.controller('DocumentSourceModalServiceCtrl', [
   '$scope', '$modal', '$window', function ($scope, $modal, $window) {
		/** Opens The Modal View **/
		$scope.open = function () {
		    var modalInstance = $modal.open({
		    	templateUrl: 'template-detail.html',
		    	controller: 'DocumentSourceModalViewCtrl',
		    	size: 'lg'
		    });
		    
		    modalInstance.result.then(function(result) {
		    	updateUi($window);
		    }, function(result) {
				updateUi($window);
		    });
		};	
	}
]);

/** Modal Instance Config For Document Source Details **/
ctrlTemplateLibrary.controller('DocumentSourceModalViewCtrl', [ 
	'$scope', '$modal', '$modalInstance', 
	function ($scope, $modal, $modalInstance) {

		$scope.selectedDoc = selectedDoc;
		
		/** Dismisses The Modal View **/
		$scope.cancel = function () {
			$modalInstance.dismiss('cancel');
		};
		
		/** Closes The Modal View **/
		$scope.close = function () {
			$modalInstance.close('ok');
		};
		
		/** Opens The Modal Edit View **/
		$scope.edit = function () {
		    var modalInstance = $modal.open({
		    	templateUrl: 'template-edit.html',
		    	controller: 'DocumentSourceModalEditCtrl',
		    	size: 'lg'
		    });
		};
	}
]);

/** Modal Instance Config For Document Source Details Edit **/
ctrlTemplateLibrary.controller('DocumentSourceModalEditCtrl', [ 
	'$scope', '$modalInstance', '$http', 
	function ($scope, $modalInstance, $http) {

		$scope.docToEdit = angular.copy(selectedDoc);
	
		/** Cancels The Modal Edit View **/
		$scope.cancel = function () {
			$scope.docEditForm.$cancel();
			$modalInstance.dismiss('cancel');
		};
		
		/** Saves The Form **/
		$scope.save = function () {
			$http.put('/content/document', $scope.docToEdit);
			selectedDoc = $scope.docToEdit;
			isSaved = true;
			$modalInstance.close('ok');
		};
	}
]);