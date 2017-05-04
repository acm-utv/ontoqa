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
		
		$scope.createGraph =false;
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
		
		$scope.viewGraph= function(){
			if($scope.createGraph == false){
				root = $rootScope.newAnswer.sltag.syntax[0];
				console.log($rootScope.newAnswer.sltag.syntax[0]);
				console.log(root);
				$rootScope.update(root);
			}
			console.log($scope.createGraph);
		}
		
		
		$rootScope.update= function(root){
			$scope.createGraph=true;
			
			var margin = {top: 40, right: 90, bottom: 50, left: 90},
		    width = 660 - margin.left - margin.right,
		    height = 500 - margin.top - margin.bottom;
			
			// declares a tree layout and assigns the size
			var treemap = d3.tree()
			    .size([width, height]);

			//  assigns the data to a hierarchy using parent-child relationships
			var nodes = d3.hierarchy(root);

			// maps the node data to the tree layout
			nodes = treemap(nodes);

			// append the svg obgect to the body of the page
			// appends a 'group' element to 'svg'
			// moves the 'group' element to the top left margin
			var svg = d3.select("body").append("svg")
			      .attr("width", width + margin.left + margin.right)
			      .attr("height", height + margin.top + margin.bottom),
			    g = svg.append("g")
			      .attr("transform",
			            "translate(" + margin.left + "," + margin.top + ")");

			// adds the links between the nodes
			var link = g.selectAll(".link")
			    .data( nodes.descendants().slice(1))
			  .enter().append("path")
			    .attr("class", "link")
			    .attr("d", function(d) {
			       return "M" + d.x + "," + d.y
			         + "C" + d.x + "," + (d.y + d.parent.y) / 2
			         + " " + d.parent.x + "," +  (d.y + d.parent.y) / 2
			         + " " + d.parent.x + "," + d.parent.y;
			       });

			// adds each node as a group
			var node = g.selectAll(".node")
			    .data(nodes.descendants())
			  .enter().append("g")
			    .attr("class", function(d) { 
			      return "node" + 
			        (d.children ? " node--internal" : " node--leaf"); })
			    .attr("transform", function(d) { 
			      return "translate(" + d.x + "," + d.y + ")"; });

			// adds the circle to the node
			node.append("circle")
			  .attr("r", 10);

			// adds the text to the node
			node.append("text")
			  .attr("dy", ".35em")
			  .attr("y", function(d) { return d.children ? -20 : 20; })
			  .style("text-anchor", "middle")
			  .text(function(d) { return d.data.name; });
			
//			var margin = {top: 20, right: 120, bottom: 20, left: 40},
//			width = 2000 - margin.right - margin.left,
//			height = 800 - margin.top - margin.bottom;
//			
//			var i = 0;
//
//			var tree = d3.layout.tree()
//			.size([height, width]);
//
//		var diagonal = d3.svg.diagonal()
//			.projection(function(d) { return [d.y, d.x]; });
//
//		var svg = d3.select("body").append("svg")
//			.attr("width", width + margin.right + margin.left)
//			.attr("height", height + margin.top + margin.bottom)
//		  .append("g")
//			.attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//
//		  // Compute the new tree layout.
//		  var nodes = tree.nodes(root).reverse(),
//			  links = tree.links(nodes);
//
//		  // Normalize for fixed-depth.
//		  nodes.forEach(function(d) { d.y = d.depth * 180; });
//
//		  // Declare the nodes…
//		  var node = svg.selectAll("g.node")
//			  .data(nodes, function(d) { return d.id || (d.id = ++i); });
//
//		  // Enter the nodes.
//		  var nodeEnter = node.enter().append("g")
//			  .attr("class", "node")
//			  .attr("transform", function(d) { 
//				  return "translate(" + d.y + "," + d.x + ")"; });
//
//		  nodeEnter.append("circle")
//			  .attr("r", 10)
//			  .style("fill", "#fff");
//
//		  nodeEnter.append("text")
//			  .attr("x", function(d) { 
//				  return d.children || d._children ? -13 : 13; })
//			  .attr("dy", ".35em")
//			  .attr("text-anchor", function(d) { 
//				  return d.children || d._children ? "end" : "start"; })
//			  .text(function(d) { return d.name; })
//			  .style("fill-opacity", 1);
//
//		  // Declare the links…
//		  var link = svg.selectAll("path.link")
//			  .data(links, function(d) { return d.target.id; });
//
//		  // Enter the links.
//		  link.enter().insert("path", "g")
//			  .attr("class", "link")
//			  .attr("d", diagonal);
//
		}
		
		
		
	}]);
	  
})();
	