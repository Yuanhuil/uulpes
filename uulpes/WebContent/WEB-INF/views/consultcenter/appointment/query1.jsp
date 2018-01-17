<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/consultcenter/appointment/listView.do"
	commandName="appointmentView" method="post">
	<form:hidden path="schoolid" />
	<div class="filterContent">
		<ul>
			<li><label class="name03">咨询方式</label> <form:select path="model"
					class="select_160">
					<option value>请选择</option>
					<form:options items="${consultationModels}" itemLabel="name"
						itemValue="id"></form:options>
				</form:select></li>
			<li><label class="name03">咨询员</label> <form:select
					path="teacherid" class="select_160">
					<option value>请选择</option>
					<form:options items="${teachers}" itemLabel="xm" itemValue="id"></form:options>
				</form:select></li>
			<li><label class="name03">状态</label> <form:select path="status"
					class="select_160">
					<option value>请选择</option>
					<form:options items="${appointmentState}" itemLabel="name"
						itemValue="state"></form:options>

				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">姓名</label> <input type="text" id="name"
				name="name" class="input_160"></li>
			<li><label class="name03">咨询类型</label> <form:select path="type"
					class="select_160">
					<option value>请选择</option>
					<form:options items="${consultTypes}" itemLabel="name"
						itemValue="id"></form:options>
				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>

			<li><label class="name03">操作时间</label> <input type="text"
				name="beginDate" class="input_160" id="beginDate"></li>
			<li><label class="name03">至 </label> <input type="text"
				name="endDate" id="endDate" class="input_160"></li>
		</ul>
	</div>

	<div class="buttonContent">
		<input type="submit" id="query" name="query"
				class="button-mid blue" value="查询" /> <input type="button"
				name="clear" id="clear" class="button-mid blue" value="重置" />
	</div>
</form:form>
<script type="text/javascript">
	$("#queryform").ajaxForm({
		target : "#list1"
	});
	$("#clear").click(function() {
		$('#queryform').clearForm();
	});
	$("#showView").on("click", function() {
		$('#query').click();
	});

	$("#beginDate").datepicker({ //绑定开始日期
		dateFormat : 'yy-mm-dd',
		changeMonth : true, //显示下拉列表月份
		changeYear : true, //显示下拉列表年份
		showWeek : true, //显示星期	
		firstDay : "1", //设置开始为1号
		onSelect : function(dateText, inst) {
			//设置结束日期的最小日期
			$("#endDate").datepicker("option", "minDate", dateText);
		}
	});

	$("#endDate").datepicker({ //设置结束绑定日期
		dateFormat : 'yy-mm-dd',
		changeMonth : true, //显示下拉列表月份
		changeYear : true, //显示下拉列表年份
		showWeek : true, //显示星期	
		firstDay : "1", //设置开始为1号
		onSelect : function(dateText, inst) {
			//设置开始日期的最大日期
			$("#beginDate").datepicker("option", "maxDate", dateText);
		}
	});
</script>