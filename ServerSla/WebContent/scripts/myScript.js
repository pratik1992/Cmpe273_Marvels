/**
 * http://usejsdoc.org/
 */
 $(document).ready(function(){
            $("button").click(function(){

                $.getJSON("externals/json_data.json", function(obj) {
                    $.each(obj, function (key, value) {
                    	//getDocument getElementById, make div
                        $("ul").append(
                                "<li>Threads:" + this['Threads']
                                + "</li><li>Utilization:" + this['Utilization']
                                + "</li><li>LogicalProcessors:" + this['LogicalProcessors']
                                + "</li><li>Model:" + this['Model']
                                + "</li><li>Vendor:" + this['Vendor']
                                + "</li><li>Clock:" + this['Clock']
                                + "</li><li>Processes:" + this['Processes']
                                + "</li></br>");

                    });
                })
            })
        })