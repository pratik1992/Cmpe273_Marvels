$(document).ready(function()

{

	var interval = 1000 * 3; // where X is your every X minutes
	setInterval(ajax_call, interval);
	
});
	
	var ajax_call = function() {
		var url = 'http://localhost:8080/ServerSla/rest/applicationParameters';

		$.ajax({
			url : url,
			type : 'GET',
			contentType : "application/json",
			success : function(result) {
				var json = JSON.parse(result);
				 document.getElementById("maxthreads").innerHTML =
				 json.Maximum_Threads;
				 document.getElementById("threadbusy").innerHTML =
				 json.Current_Thread_Busy;
				 document.getElementById("requestCount").innerHTML =
			     json.Request_Count;
				 document.getElementById("concurrentthreads").innerHTML =
			     json.Current_Threads;
				 document.getElementById("latency").innerHTML =
				     json.Latency;
				 document.getElementById("throughput").innerHTML =
				     json.Throughput;
				//drawChartCPU(100 - (json.CPU.Utilization), json.CPU.Utilization);
				//drawChartMem(100 - (json.Memory.Utilization), json.Memory.Utilization);
				// alert(json.LastUpdateTime);
				// console.log(result);
			}
		});
		
		
		
		
	};
	
	
	



