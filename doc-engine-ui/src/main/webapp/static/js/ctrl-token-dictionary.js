'use strict';

/** Angular.js Controller Declaration **/
var ctrlTokenDictionary = angular.module('ctrlTokenDictionary', []);
var isSaved = false;
var isSelected = false;
var selectedToken = null;
var updateUi = function(window) {
	if (isSaved) {
		window.location.reload();
		isSaved = false;
	}
}

/** Get And Display All Token Dictionary Items **/
ctrlTokenDictionary.controller('GetTokenDictionaryItemAllGridCtrl', [ 
	'$scope', '$http', '$window', function($scope, $http, $window) {
		
		/** Get All Dictionary Items Via REST **/
		$http.get("/dictionary/items").success(
			function( response ) { 
				if (response.data != null) {
					$scope.gridOptions.data = response;	
				} else {
					$scope.isEmpty = true;
				}				
			}
		);

		/** Grid Options To Display The HTTP Response **/
		$scope.gridOptions = {
			columnDefs: [
	             { name: 'Name', 			field: 'tokenName' },
	             { name: 'Alternate Value', field: 'altText' },
	             { name: 'Description', 	field: 'tokenDescription' },
	             { name: 'Task Captured', 	field: 'taskName' }
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
						selectedToken = row.entity;
					} else {
						isSelected = false;
						selectedToken = null;
					}
				});
			}
		};
		
		/** Determines If A Token Has Been Selected **/
		$scope.isSelected = function () {
			return isSelected;
		};
	}
]);

/** Modal Service Config For Token Dictionary Item Details **/
ctrlTokenDictionary.controller('TokenDictionaryItemModalServiceCtrl', [
   '$scope', '$modal', '$window', function ($scope, $modal, $window) {
		/** Opens The Modal View **/
		$scope.open = function () {
		    var modalInstance = $modal.open({
		    	templateUrl: 'token-detail.html',
		    	controller: 'TokenDictionaryItemModalViewCtrl',
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

/** Modal Instance Config For Token Dictionary Item Details **/
ctrlTokenDictionary.controller('TokenDictionaryItemModalViewCtrl', [ 
	'$scope', '$modal', '$modalInstance', 
	function ($scope, $modal, $modalInstance) {
			
		$scope.selectedToken = selectedToken;

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
		    	templateUrl: 'token-edit.html',
		    	controller: 'TokenDictionaryItemModalEditCtrl',
		    	size: 'lg'
		    });
		};	
		
		/** Determines If Current Token Has Alternate Text To Display **/
		$scope.hasAltText = function () {
			var hasAltText = false;
			if ($scope.selectedToken !== null) {
				if ($scope.selectedToken.altText !== "" &&
					$scope.selectedToken.altText !== null) {
					hasAltText = true;
				}
			}
			return hasAltText;
		};
		
		/** Determines If Current Token Has A Description To Display **/
		$scope.hasDescription = function () {
			var hasDescription = false;
			if ($scope.selectedToken !== null) {
				if ($scope.selectedToken.tokenDescription !== "" &&
					$scope.selectedToken.tokenDescription !== null) {
					hasDescription = true;
				}
			}
			return hasDescription;
		};
		
		/** Determines If Current Token Has Award Lease Definitions **/
		$scope.isAwardedLease = function () {
			var isAwardedLease = false;
			if ($scope.selectedToken !== null) {
				if ($scope.selectedToken.awardedEntity !== "") {
					isAwardedLease = true;
				}			
			}
			return isAwardedLease;
		};

		/** Determines If Current Token Has Proposed Lease Definitions **/
		$scope.isProposedLease = function () {
			var isProposedLease = false;
			if ($scope.selectedToken !== null) {
				if ($scope.selectedToken.proposedEntity !== "") {
					isProposedLease = true;
				}			
			}
			return isProposedLease;
		};

		/** Determines If Current Token Has RLP Definitions **/
		$scope.isRlp = function () {
			var isRlp = false;
			if ($scope.selectedToken !== null) {
				if ($scope.selectedToken.rlpEntity !== "") {
					isRlp = true;
				}			
			}
			return isRlp;
		};
	}
]);

/** Modal Instance Config For Token Dictionary Item Details Edit **/
ctrlTokenDictionary.controller('TokenDictionaryItemModalEditCtrl', [ 
	'$scope', '$modalInstance', '$http', 
	function ($scope, $modalInstance, $http) {

		$scope.tokenToEdit = angular.copy(selectedToken);
	
		/** Cancels The Modal Edit View **/
		$scope.cancel = function () {
			$scope.itemEditForm.$cancel();
			$modalInstance.dismiss('cancel');
		};
		
		/** Saves The Form **/
		$scope.save = function () {
			$http.post('/dictionary/items', $scope.tokenToEdit);
			selectedToken = $scope.tokenToEdit;
			isSaved = true;
			$modalInstance.close('ok');
		};
	}
]);