(function() {

	var app= angular.module('App', ['ngRoute']);
	
	app.config(function($routeProvider) {
	    $routeProvider
	    .when("/", {
	        templateUrl : "index.html"
	    })
	    
	});
	
	app.controller('ControllerA',['$scope','$http', '$rootScope', function ($scope,$http, $rootScope) {
		
		
		var getAnswer= this;
		getAnswer.products=[];
		$rootScope.newAnswer ={
				question : '',
				answer: '',
				log: '',
				query:'',
				graph: ''	
		 };
		
//		/*Get */
//		$http.get('/answer').success(function(data) {
//			//console.log(JSON.stringify(data, null, 2));
//			getAnswer.products=data;
//		});
		
	   
		$scope.sendQuestion= function(){
			
			var newQuestion= JSON.stringify($rootScope.newAnswer.question);
			
			$http({
			      url: '/qa',
			      method: "POST",
			      data: newQuestion
			      }).then(function(response) {
			    	  console.log("Success");
			    	  getAnswer.products = response.data;
			    });
			
		};
		
	}]);
	  
})();
	