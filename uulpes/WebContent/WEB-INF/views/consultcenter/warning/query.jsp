<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/consultcenter/warning/list.do" commandName="warning"
	method="post">
	<form:hidden path="schoolid" />
	<div class="filterContent">
		<ul>
			<li><label class="name03">角色</label> <form:select
					class="select_160" path="role">
					<option value>请选择</option>
					<option value="1">学生</option>
					<option value="2">老师</option>
				</form:select></li>
			<li><label class="name03">量表id</label> <form:select
					class="select_160" path="scaleid">
					<option value>请选择</option>
				</form:select></li>
			<li><label class="name03">预警级别</label> <form:select
					class="select_160" path="level">
					<option value>请选择</option>
					<option value="0">一级预警</option>
					<option value="1">二级预警</option>
					<option value="2">三级预警</option>
				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">咨询类型</label> <form:select
					class="select_160" path="type">
					<option value>请选择</option>
					<form:options items="${consultTypes}" itemLabel="name"
						itemValue="id"></form:options>
				</form:select></li>

			<li><label class="name03">咨询方式</label> <form:select
					class="select_160" path="model">
					<option value>请选择</option>
					<form:options items="${consultationModels}" itemLabel="name"
						itemValue="id"></form:options>
				</form:select></li>
			<li><label class="name03">状态</label> <form:select
					class="select_160" path="status">
					<option value>请选择</option>

				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">操作时间</label> <input type="text"
				name="beginDate" id="beginDate" class="input_160"></li>
			<li><label class="name03">至</label> <input type="text"
				name="endDate" id="endDate" class="input_160"></li>
		</ul>
	</div>
	<div class="buttonContent">
		<input type="submit" id="query" name="query" value="查询"
				class="button-mid blue" /> <input type="button" name="clear"
				id="clear" value="重置" class="button-mid blue" />
	</div>
</form:form>
<script type="text/javascript">
	$("#queryform").ajaxForm({
		target : "#list"
	});
	$("#clear").click(function() {
		$('#queryform').clearForm();
	});

	$("#beginDate").datepicker(
			{ //绑定开始日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				showWeek : true, //显示星期	
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置结束日期的最小日期
					$("endDate").datepicker('option', 'minDate',
							new Date(dateText.replace('-', ',')));

				}
			});

	$("#endDate").datepicker(
			{ //设置结束绑定日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				showWeek : true, //显示星期	
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置开始日期的最大日期
					$('#beginDate').datepicker('option', 'maxDate',
							new Date(dateText.replace('-', ',')));
				}
			});
</script>