<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/taolstation/taoTrain/list.do" commandName="taoTrain"
	method="post">
	<div class="filterContent">
		<ul>
			<li><label class="name03">培训名称</label> <input type="text"
				class="input_160" id="name1" name="name"></li>
			<li><label class="name03">培训讲师</label> <form:select
					class="select_160" path="teacherid" id="teacherid1">
					<option value>请选择</option>
					<form:options items="${teachers}" itemLabel="xm" itemValue="id"></form:options>
				</form:select></li>
			
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			
			<li><label class="name03">操作时间</label> <input type="text"
				name="starttime" class="input_160" id="starttime1"></li>
			<li><label class="name03">至</label> <input type="text"
				name="endtime" class="input_160" id="endtime1"></li>
		</ul>
	</div>
	
	<div class="buttonContent">
		<input type="submit" id="query" name="query" value="查询"
				class="button-mid blue" /> <input type="button" name="clear"
				id="clear" value="重置" class="button-mid blue" /> <input
				type="button" id="add" value="添加" class="button-mid blue" />
	</div>
</form:form>
<script type="text/javascript">
	$("#queryform").ajaxForm({
		target : "#list"
	});
	$("#clear").click(function() {
		$('#queryform').clearForm();
	});

	$("#add").on("click", function() {
		$("#dialog-form1").dialog("open");
		var h = '/pes/taolstation/taoTrain/addOrUpdate.do';
		$('#editDiv').html();
		$('#editDiv').load(h, function() {
			$("#dialog-form1").dialog("open");
		});
	});

	$("#starttime1").datetimepicker({ //绑定开始日期
		dateFormat : 'yy-mm-dd',
		changeMonth : true, //显示下拉列表月份
		changeYear : true, //显示下拉列表年份
		showWeek : true, //显示星期	
		firstDay : "1", //设置开始为1号
		onSelect : function(dateText, inst) {
			//设置结束日期的最小日期
			$("#endtime1").datetimepicker("option", "minTime", dateText);
		}
	});

	$("#endtime1").datetimepicker({ //设置结束绑定日期
		dateFormat : 'yy-mm-dd',
		changeMonth : true, //显示下拉列表月份
		changeYear : true, //显示下拉列表年份
		showWeek : true, //显示星期	
		firstDay : "1", //设置开始为1号
		onSelect : function(dateText, inst) {
			//设置开始日期的最大日期
			$("#starttime1").datetimepicker("option", "maxTime", dateText);
		}
	});
</script>