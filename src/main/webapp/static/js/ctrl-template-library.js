'use strict';

/* Angular.js Controller Declaration */
var ctrlTemplateLibrary = angular.module('ctrlTemplateLibrary', []);
var isSaved = false;
var isSelected = false;
var selection = null;
var updateUi = function(window) {
	if (isSaved) {
		window.location.reload();
		isSaved = false;
	}
}

/* Get and display all Document sources */
ctrlTemplateLibrary.controller(
	'GetDocumentSourcesAllGridCtrl', 
	[ 
		'$scope', 
		'$http', 
		'$window', 
		function($scope, $http, $window) {
			$http.get( /* Get all Document Sources via REST */
				"/doc-engine/content/document/documents"
			).success(
				function(response) {
					selection = null;
					$scope.gridOptions.data = response.documents;	
				}
			);
			$scope.gridOptions = { /* Grid options to display the REST response */
				columnDefs: [
					{ name: 'Content Code',	field: 'contentCd', 	width: '25%' },
					{ name: 'Name',			field: 'name', 			width: '15%' },
					{ name: 'Description',	field: 'description', 	width: '60%' }
				],
				/* enableFiltering: true, */
				enableRowHeaderSelection: false,
				enableRowSelection: true,
				multiSelect: false,
				onRegisterApi: function(gridApi) {
					$scope.gridApi = gridApi;
					$scope.gridApi.selection.on.rowSelectionChanged(
						$scope, 
						function(row) {
							if (row.isSelected) {
								isSelected = true;
								selection = row.entity;
							} else {
								isSelected = false;
								selection = null;
							}					
						}
					);
				}
			};
			$scope.isSelected = function () {
				return isSelected;
			};
		}
	]
);

/* Get and display all Section sources */
ctrlTemplateLibrary.controller(
	'GetSectionSourcesForDocumentGridCtrl',
	[
		'$scope',
		'$http',
		'$window',
		function($scope, $http, $window) {
			$http.get( /* Get all Section Sources for a specific Document via REST */
					"/doc-engine/content/document/children/id/" + selection.id
			).success(
				function(response) { 
					selection = null;
					$scope.gridOptions.data = response.sections;	
				}
			);
			$scope.gridOptions = { /* Grid options to display the REST response */
				columnDefs: [
					{ name: 'Content Code',	field: 'contentCd', 	width: '25%' },
					{ name: 'Name',			field: 'name', 			width: '15%' },
					{ name: 'Description',	field: 'description', 	width: '60%' }
				],
				/* enableFiltering: true, */
				enableRowHeaderSelection: false,
				enableRowSelection: true,
				multiSelect: false,
				onRegisterApi: function(gridApi) {
					$scope.gridApi = gridApi;
					$scope.gridApi.selection.on.rowSelectionChanged(
						$scope, 
						function(row) {
							if (row.isSelected) {
								isSelected = true;
								selection = row.entity;
							} else {
								isSelected = false;
								selection = null;
							}					
						}
					);
				}
			};
			$scope.isSelected = function () {
				return isSelected;
			};
		}
	]
)

/* Get and display all Clause sources */
ctrlTemplateLibrary.controller(
	'GetClauseSourcesForSectionGridCtrl',
	[
		'$scope',
		'$http',
		'$window',
		function($scope, $http, $window) {
			$http.get( /* Get all Clause Sources for a specific Section via REST */
					"/doc-engine/content/section/children/id/" + selection.id
			).success(
				function(response) { 
					$scope.gridOptions.data = response.clauses;	
				}
			);
			$scope.gridOptions = { /* Grid options to display the REST response */
				columnDefs: [
					{ name: 'Content Code',	field: 'contentCd', 	width: '25%' },
					{ name: 'Name',			field: 'name', 			width: '15%' },
					{ name: 'Description',	field: 'description', 	width: '60%' }
				],
				/* enableFiltering: true, */
				enableRowHeaderSelection: false,
				enableRowSelection: true,
				multiSelect: false,
				onRegisterApi: function(gridApi) {
					$scope.gridApi = gridApi;
					$scope.gridApi.selection.on.rowSelectionChanged(
						$scope, 
						function(row) {
							if (row.isSelected) {
								isSelected = true;
								selection = row.entity;
							} else {
								isSelected = false;
								selection = null;
							}					
						}
					);
				}
			};
			$scope.isSelected = function () {
				return isSelected;
			};
		}
	]
)

/* Get and display all Clause sources */
ctrlTemplateLibrary.controller(
	'GetParagraphSourcesForClauseGridCtrl',
	[
		'$scope',
		'$http',
		'$window',
		function($scope, $http, $window) {
			$http.get( /* Get all Paragraph Sources for a specific Clause via REST */
					"/doc-engine/content/clause/children/id/" + selection.id
			).success(
				function(response) { 
					selection = null;
					$scope.gridOptions.data = response.paragraphs;	
				}
			);
			$scope.gridOptions = { /* Grid options to display the REST response */
				columnDefs: [
					{ name: 'Content Code',	field: 'contentCd', 	width: '25%' },
					{ name: 'Name',			field: 'name', 			width: '15%' },
					{ name: 'Description',	field: 'description', 	width: '60%' }
				],
				/* enableFiltering: true, */
				enableRowHeaderSelection: false,
				enableRowSelection: true,
				multiSelect: false,
				onRegisterApi: function(gridApi) {
					$scope.gridApi = gridApi;
					$scope.gridApi.selection.on.rowSelectionChanged(
						$scope, 
						function(row) {
							if (row.isSelected) {
								isSelected = true;
								selection = row.entity;
							} else {
								isSelected = false;
								selection = null;
							}					
						}
					);
				}
			};
			$scope.isSelected = function () {
				return isSelected;
			};
		}
	]
)

/* Modal Service config for Document Source details */
ctrlTemplateLibrary.controller( 
	'DocumentSourceModalServiceCtrl', 
	[
		'$scope', 
		'$modal', 
		'$window', 
		function ($scope, $modal, $window) {
			$scope.open = function () { /* Opens the modal view */
				var modalInstance = $modal.open(
					{
						templateUrl: 'template-detail.html',
						controller: 'DocumentSourceModalViewCtrl',
						size: 'lg'
					}
				);
				modalInstance.result.then(
		    		function(result) {
	    				updateUi($window);
	    			}, 
	    			function(result) {
	    				updateUi($window);
	    			}
    			);
			};	
		}
	]
);

/* Modal Instance config for Document Source details */
ctrlTemplateLibrary.controller( 
	'DocumentSourceModalViewCtrl', 
	[ 
		'$scope', 
		'$modal', 
		'$modalInstance',
		'$http',
		function ($scope, $modal, $modalInstance, $http) {
			$scope.selection = selection;
			$scope.cancel = function () {
				$modalInstance.dismiss('cancel');
			};
			$scope.close = function () {
				$modalInstance.close('ok');
			};
			$scope.edit = function () {
				$modalInstance.close('ok');
				var modalInstance = $modal.open(
					{
						templateUrl: 'template-edit.html',
						controller: 'DocumentSourceModalEditCtrl',
						size: 'lg'
					}
				);
			};
			$scope.back = function () { /* Opens the modal view */
				var modalInstance = $modal.open(
					{
						templateUrl: 'template-detail.html',
						controller: 'DocumentSourceModalViewCtrl',
						size: 'lg'
					}
				);
				modalInstance.result.then(
		    		function(result) {
	    				updateUi($window);
	    			}, 
	    			function(result) {
	    				updateUi($window);
	    			}
    			);
			};	
		}
	]
);

/* Modal Instance config for Document Source details edit */
ctrlTemplateLibrary.controller(
	'DocumentSourceModalEditCtrl', 
	[ 
		'$scope', 
		'$modalInstance', 
		'$http', 
		function ($scope, $modalInstance, $http) {
			$scope.docToEdit = angular.copy(selection);
			$scope.cancel = function () { /* Cancels the Modal edit view */
				$scope.docEditForm.$cancel();
				$modalInstance.dismiss('cancel');
			};
			$scope.save = function () { /* Saves the form */
				$http.put(
					'/content/document', 
					$scope.docToEdit
				);
				selection = $scope.docToEdit;
				isSaved = true;
				$modalInstance.close('ok');
			};
		}
	]
);