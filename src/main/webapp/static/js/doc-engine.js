'use strict';

/* Angular.js Application Declaration */
var docEngine = angular.module( 
	'docEngine', 
	[
		'ngRoute',
		'ngResource',
		'ngAnimate',
		'ngSanitize',
		'ui.bootstrap',
		'ui.grid',
		'ui.grid.selection',
		'xeditable',
		'ctrlUtils',
		'ctrlTemplateLibrary',
		'ctrlTokenDictionary'
	]
);

/* Style Theme Config For XEditable */
docEngine.run(  
	function(editableOptions) {
	  editableOptions.theme = 'bs3';
	}
);

/* View Router */
docEngine.config( 
	[
		'$routeProvider',
		function($routeProvider) {
			$routeProvider
	    	.when( /* Login / Home Page */
    			'/', 
    			{
    				templateUrl: 'html/home/welcome.html'
				}
			)
	    	.when( /* Content Repository Routing */
    			'/template-library', 
    			{
    				templateUrl: 'html/library/template-library.html',
    				controller: 'DocumentSourceModalServiceCtrl'
				}
			)
	    	.when( /* Content Repository Routing */
    			'/template-library-sections', 
    			{
    				templateUrl: 'html/library/template-library-sections.html',
    				controller: 'DocumentSourceModalServiceCtrl'
				}
			)
	    	.when( /* Content Repository Routing */
    			'/template-library-clauses', 
    			{
    				templateUrl: 'html/library/template-library-clauses.html',
    				controller: 'DocumentSourceModalServiceCtrl'
				}
			)
	    	.when( /* Content Repository Routing */
    			'/template-library-paragraphs', 
    			{
    				templateUrl: 'html/library/template-library-paragraphs.html',
    				controller: 'DocumentSourceModalServiceCtrl'
				}
			)
	    	.when( /* Token Dictionary Routing */
    			'/token-dictionary', 
    			{
    				templateUrl: 'html/dictionary/token-dictionary.html',
    				controller: 'TokenDictionaryItemModalServiceCtrl'
				}
			)	    		
	    	/* Create A Document Routing */
	    		
	    	/* Existing Documents Routing */
				
			/* Default Behavior, Points Back To Welcome Page */
			.otherwise(
				{
					redirectTo: '/'
				}
			);
		}
	]
);