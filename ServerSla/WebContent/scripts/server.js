google.charts.load('current', {
		'packages' : [ 'corechart' ]
	});
	google.charts.setOnLoadCallback(drawChart);
	function drawChart() {


				/*$.ajax({
					url : 'http://localhost:8080/ServerMonitorService/rest/SystemStatus',
					type : 'GET',
					contentType : "application/json",
					success : function(result) {
						var json = JSON.parse(result);*/
		var data = new google.visualization.DataTable();
        data.addColumn('string', 'Topping');
        data.addColumn('number', 'Slices');
        data.addRows([
[ 'Parameters', 0 ],
[ 'Concurrent_threads',
		5 ],
[ 'Request_Count', 4 ],
[ 'Max_Threads', 4 ],
[ 'Threads_Busy', 2 ], 
          
        ]);
										

						var options = {
							title : 'Server Health',
							width:   500,
		                    height:  300
						};

						var chart = new google.visualization.PieChart(document
								.getElementById('piechart'));

						chart.draw(data, options);

						//drawChartAppliationParameters(json);

					/*}
				});*/
	}
