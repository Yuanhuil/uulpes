<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>


<style>

.record_tip {
	position: absolute;
	width: 250px;
	max-width: 400px;
	text-align: left;
	padding: 4px;
	border: #87CEEB solid 7px;
	border-radius: 5px;
	background: #00BFFF;
	z-index: 1000;
	behavior: url('/css/css3/pie.htc');
}

.record_tip ul {
	margin: 0;
	padding: 0;
}

.record_tip ul li {
	font-family: 'Microsoft YaHei', 微软雅黑, 'Microsoft JhengHei', 宋体;
	font-size: 15px;
	list-style: none;
	padding-left: 40px;
}

#wrap {
	width: 800px;
	margin: 0 auto;
}



#external-events h4 {
	font-size: 16px;
	margin-top: 0;
	padding-top: 1em;
}

#external-events .fc-event {
	margin: 10px 0;
	cursor: pointer;
}

#external-events p {
	margin: 1.5em 0;
	font-size: 11px;
	color: #666;
}

#external-events p input {
	margin: 0;
	vertical-align: middle;
}

#calendar {
	float: right;
	max-width: 800px;
	max-height: 450px;
	margin: 10px 10px 0px 10px;
}
td{
	overflow:visible;
	}
</style>
</head>
<body>
	<div id='wrap'>
		<div style="float:right">双击添加预约  蓝色为可预约时间  绿色为已预约 橙色为已完成</div>
		

		<div id='calendar'></div>

		<div style='clear:both'></div>

	</div>
</body>

<script>

	$(document).ready(function() {


		/* initialize the external events
		-----------------------------------------------------------------*/

		$('#external-events .fc-event').each(function() {

			// store data so the calendar knows to render an event upon drop
			$(this).data('event', {
				title: $.trim($(this).text()), // use the element's text as the event title
				stick: true // maintain when user navigates (see docs on the renderEvent method)
			});

			// make the event draggable using jQuery UI
			$(this).draggable({
				zIndex: 999,
				revert: true,      // will cause the event to go back to its
				revertDuration: 0  //  original position after the drag
			});

		});


		/* initialize the calendar
		-----------------------------------------------------------------*/

		$('#calendar').fullCalendar({
			header: {
				left: 'prev',
				center: 'title',
				right: 'next'
			},
			allDaySlot:false,
			height: 450,
			lang:'zh-cn',
			defaultDate: '${defaultDate}',
			editable: true,
			droppable: true, // this allows things to be dropped onto the calendar
			timeFormat: 'H:mm',
			defaultView:'agendaWeek',
			eventLimit: true,
			minTime: '08:00',
			maxTime: '22:00',
			events:${str},
			//单击事件项时触发 
	        eventClick: function(calEvent, jsEvent, view) { 
	    		var h = '/pes/consultcenter/appointment/addOrUpdate.do?id='+calEvent.id;
	    		$('#editDiv').html();
	    		$('#editDiv').load(h, function() {
	    			$("#dialog-form1").dialog("open");
	    		});
	        },
	        eventMouseover: function(event, jsEvent, view){  
               showDetail(event, jsEvent);                   
          	},  
          	eventMouseout: function(event, jsEvent, view){  
                $('.record_tip').remove();  
           }, 
            
		});
		console.log('${beginDate}');
		jQuery('.fc-next-button,.fc-prev-button').bind('click', function() {
        	 var view = $('#calendar').fullCalendar('getView'); 
        	 var h = '/pes/consultcenter/appointment/list.do?beginDate='+view.start.format()+'&endDate='+view.end.format();
        	 $('#list').html();
        	 $('#list').load(h, function() {
	    		});
        		
        	
         });
		 
	});
	
	 function showDetail(obj, e){  
	  
        var str="";  
        var eInfo = '<div class="record_tip"><ul>';  
        eInfo += '<li class="message">'+'咨询员：'+ obj.tName + '<br/> </li>';  
        eInfo += '<li class="message">'+'学生：'+ obj.sName + '<br/> </li>';  
        eInfo += '<li class="message">'+'问题描述：'+ obj.describes + '<br/> </li>';  
        eInfo += '<li class="message">'+'联系方式：'+ obj.contact + '<br/> </li>';  
        eInfo += '<li class="message">'+'状态：'+ obj.status + '<br/> </li>';  
        eInfo += '<li class="clock">'  + '开始：'+obj.start.format().substring(11,19) +'</br>  结束：'+obj.realEndTime.substring(11,19)+ '</li>';  
       
        //eInfo += '<li>分类：' + obj.title + '</li>';  
        eInfo += '<li class="postmessage">' + str + '<br/> </li>';  
        eInfo += '</ul></div>';  
        $(eInfo).appendTo($('body'));  
        $('.record_tip').css({"opacity":"0.4", "display":"none", "left":(e.pageX + 20) + "px", "top":(e.pageY + 10) + "px"}).fadeTo(600, 0.9);  
        //鼠标移动效果  
        $('.fc-event-inner').mousemove(function(e){  
            $('.record_tip').css({'top': (e.pageY + 10), 'left': (e.pageX + 20)});  
        });  
    }     
	 
	
</script>