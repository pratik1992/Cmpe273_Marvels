$(document).ready(function()

{
	var interval = 1000 * 3; // where X is your every X minutes
	setInterval(ajax_call, interval);
});
var ajax_call = function() {
	var url = 'http://localhost:8080/ServerSla/rest/SystemStatus';

	$.ajax({
		url : url,
		type : 'GET',
		contentType : "application/json",
		success : function(result) {
			var json = JSON.parse(result);
			// document.getElementById("lastUpdateTime").innerHTML =
			// json.LastUpdateTime;
			// document.getElementById("MemUtilization").innerHTML =
			// json.Memory.Utilization;
			drawChartCPU(100 - (json.CPU.Utilization), json.CPU.Utilization);
			drawChartMem(100 - (json.Memory.Utilization), json.Memory.Utilization);
			 document.getElementById("logicalprocessors").innerHTML =
				 json.CPU.LogicalProcessors;
				 document.getElementById("vendor").innerHTML =
				 json.CPU.Vendor;
				 document.getElementById("clock").innerHTML =
			     json.CPU.Clock;
				 document.getElementById("processes").innerHTML =
			     json.CPU.Processes;
				 document.getElementById("threads").innerHTML =
				     json.CPU.Threads;
			
			// alert(json.LastUpdateTime);
			// console.log(result);
		}
	});
	
	
};
/**
 * http://usejsdoc.org/
 */
// Load the Visualization API and the corechart package.
google.charts.load('current', {
	'packages' : [ 'corechart' ]
});

// Set a callback to run when the Google Visualization API is loaded.
google.charts.setOnLoadCallback(drawChartCPU);
google.charts.setOnLoadCallback(drawChartMem);
// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawChartCPU(free, utilized) {

	// Create the data table.
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Topping');
	data.addColumn('number', 'Slices');
	data.addRows([ [ 'Free', free ], [ 'Utilized', utilized ],

	]);

	// Set chart options
	var options = {
		'title' : 'CPU',
		'width' : 400,
		'height' : 300
	};

	// Instantiate and draw our chart, passing in some options.
	var chart = new google.visualization.PieChart(document
			.getElementById('chart_div_CPU'));
	chart.draw(data, options);
}
function drawChartMem(free, utilized) {

	// Create the data table for Anthony's pizza.
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Topping');
	data.addColumn('number', 'Slices');
	data.addRows([ [ 'Utilization', utilized ], [ 'Free', free ],

	]);

	// Set options for Anthony's pie chart.
	var options = {
		title : 'MEMORY',
		width : 400,
		height : 300
	};

	// Instantiate and draw the chart for Anthony's pizza.
	var chart = new google.visualization.PieChart(document
			.getElementById('chart_div_mem'));
	chart.draw(data, options);
}