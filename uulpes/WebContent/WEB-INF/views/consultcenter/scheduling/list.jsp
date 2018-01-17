<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>
#tip {
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

#tip ul {
    margin: 0;
    padding: 0;
}

#tip ul li {
    font-family: 'Microsoft YaHei', 微软雅黑, 'Microsoft JhengHei', 宋体;
    font-size: 15px;
    list-style: none;
    padding-left: 40px;
}

#wrap {
    width: 800px;
    margin: 0 auto;
}

#external-teacher {
    float: left;
    width: 80px;
    margin-top: 10px;
    margin-left: 10px;
}

#external-events {
    float: left;
    width: 600px;
    margin-top: 9px;
}

#external-events h4 {
    font-size: 16px;
    margin-top: 0;
    padding-top: 1em;
}

#external-events .fc-event {
    cursor: pointer;
    width: 100px;
    display: inline-block;
    text-align: center;
    color: #757575;
    background-color: #f4f4f4;
    border: solid 1px #e5e5e5;
}

#external-events .fc-event:hover {
    background-color: #43c68d;
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
    margin: 10px 10px 10px 10px;
}

.teacher-size {
    height: 30px;
    width: 100px;
    line-height: 30px;
    overflow: hidden;
    text-align: center;
    margin-left: 20px;
}

.teacher-work {
    display: inline;
    font-size: 20px;
    font-weight: bold;
    color: #757575;
}

.div-class {
    float: left;
    margin-left: 10px;
    color: #43c68d;
    margin-top: 18px
}

.fc-toolbar {
    height: 40px;
    line-height: 40px;
    color: #f4f4f4;
    background-color: #369863;
    margin-top: 10px;
}

.fc-toolbar {
    text-align: center;
    margin-bottom: 0em;
}

.fc-day-header {
    background-color: #369863;
    border-color: red;
    color: #f4f4f4;
    line-height: 40px;
    text-align: left;
    padding: 0 6px;
}

.fc-center {
    display: inherit;
}

.fc-left {
    margin-left: 310px;
    margin-top: 8px;
}

.fc-right {
    margin-right: 278px;
    margin-top: 8px;
}

.fc-state-default {
    background: 0;
    border: 0px;
}

.fc-today {
    border: 1px solid red;
}

.fc-wed {
    border: 1px solid red;
}
</style>
<body>
    <div id='wrap'>
        <div id='external-teacher'>
            <li class='teacher-work'>值班教师</li>
        </div>
        <div id='external-events'>
            <c:forEach items="${teachers}" var="m">
                <div class='fc-event teacher-size'>${m.xm}</div>
            </c:forEach>
        </div>
        <div class="div-class">拖拽值班教师到对应日期进行排班</div>
        <div id='calendar'></div>
        <div style='clear: both'></div>
    </div>
</body>
<script>
	$(document).ready(function() {
		$('#external-events .fc-event').each(function() {
			$(this).data('event', {
				title: $.trim($(this).text()),
				stick: true
			});
			$(this).draggable({
				zIndex: 999,
				revert: true,
				revertDuration: 0
			});
		});
		$('#calendar').fullCalendar({
			header: {
				left: 'prev',
				center: 'title',
				right: 'next'
			},
			defaultDate: '${defaultDate}',
			lang:'zh-cn',
			editable: true,
			droppable: true,
			timeFormat: 'H:mm',
			events:${str},
			//单击事件项时触发 
	        eventClick: function(calEvent, jsEvent, view) {
	    		var h = '/pes/consultcenter/scheduling/addOrUpdate.do?teacherName='+calEvent.title+'&day='+calEvent.start.format()+'&id='+calEvent.id;
	    		$('#editDiv').html();
	    		$('#editDiv').load(encodeURI(h), function() {
	    			$("#dialog-form1").dialog("open");
	    		});
	        },
	        eventMouseover: function(event, jsEvent, view){
               showDetail(event, jsEvent);
            },
            eventMouseout: function(event, jsEvent, view){
                $('#tip').remove();
           },
		    eventAfterRender:function( event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ) {
		     if(event.oldTime==undefined){
		    	 var h = '/pes/consultcenter/scheduling/addOrUpdate.do?teacherName='+event.title+'&day='+event.start.format();
		    		$('#editDiv').html();
		    		$('#editDiv').load(encodeURI(h), function() {
		    			$("#dialog-form1").dialog("open");
		    		});
		     }
		    },
		});
		jQuery('.fc-next-button,.fc-prev-button').bind('click', function() {
         var view = $('#calendar').fullCalendar('getView');
       	 var h = '/pes/consultcenter/scheduling/list.do?beginDate='+view.start.format()+'&endDate='+view.end.format();
       	 $('#list').html();
    	 $('#list').load(h, function() {
    		});
        });

	});

	 function showDetail(obj, e){
        var str="";
        var eInfo = '<div id="tip"><ul>';
        eInfo += '<li class="message">'+'教师：'+ obj.title + '<br/> </li>';
        eInfo += '<li class="clock">' + '开始：'+obj.start.format().substring(11) +'</br>结束：'+obj.end.format().substring(11)+ '</li>';
        eInfo += '<li class="postmessage">' + str + '<br/> </li>';
        eInfo += '</ul></div>';
        $(eInfo).appendTo($('body'));
        $('#tip').css({"opacity":"0.4", "display":"none", "left":(e.pageX + 20) + "px", "top":(e.pageY + 10) + "px"}).fadeTo(600, 0.9);
        //鼠标移动效果
        $('.fc-event-inner').mousemove(function(e){
            $('#tip').css({'top': (e.pageY + 10), 'left': (e.pageX + 20)});
        });
    }
</script>