"use strict";

var Echo=Echo || {};

Echo.sendMessage=function(){
	
	
	var echo=$('#echo');
	var message=echo.val();
	if(message !=''){
		Echo.socket.send(mesage);
		echo.val('');
	}
}

Echo.connect=function(host){
	if('WebSocket' in window){
		Echo.socket=new WebSocket(host);
	}else{
		console.log('Error:WebSocket is not supported by this browser');
		return ;
	}


Echo.socket.onopen=function(){
	console.log("Info: Connection opened");
	$('#echo').keydown(function (evt){
		if(evt.keyCode==13){
			Echo.sendMessage();
		}
	});
};


Echo.socket.onclose=function(){
	console.log("Info: Connection opened");
	console.log("Connection Closed");
};

Echo.socket.onmessage=function(message){
	console.log("Message"+message.data);
	
	$('#echoBack').text(message.data);
	
};
};



Echo.initialize=function(){
	var ep="/webscoket/echo";
	
	if(window.location.protocol=="http"){
		Echo.connect('ws://'+window.location.host+ep);
	}
	else {
		
	}
};