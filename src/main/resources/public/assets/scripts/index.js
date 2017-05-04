(function() {

	var app= angular.module('App', []);
	
//	app.config(function($routeProvider) {
//	    $routeProvider
//	    .when("/", {
//	        templateUrl : "index.html"
//	    })
//	    
//	});
	app.controller('ControllerOntoQA',['$scope','$http', '$rootScope', function ($scope,$http, $rootScope) {
		
		var getAnswer= this;
		getAnswer.products=[];
		$rootScope.newAnswer ={
				question : '',
				answer: '',
				query:'',
				sltag: ''	
		 };
		
		$rootScope.newQuestion={
				question : ''
		}
		
		
	   
		$scope.sendQuestion= function(){
			
			var newQuestion= JSON.stringify($rootScope.newQuestion);
			console.log(newQuestion);
			$http({
			      url: 'http://localhost:8080/qa',
			      method: "POST",
			      data: newQuestion
			      }).then(function(response) {
			    	  console.log("Success");
			    	  getAnswer.products = response.data;
			    	  console.log(response.data);
			    	  $rootScope.newAnswer = getAnswer.products;
			    });
			
		};
		
	}]);
	  
})();
	