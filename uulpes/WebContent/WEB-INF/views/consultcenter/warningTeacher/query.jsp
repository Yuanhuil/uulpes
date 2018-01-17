<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform1" name="form"
	action="/pes/consultcenter/warningTeacher/list.do"
	commandName="warningTeacher" method="post">

	<div class="filterContent">
		<ul>
			<li><label class="name03">角色名称</label> <form:select
					path="roleId" cssClass="dropBox01 input_160">

					<option value>请选择</option>
					<form:options items="${rolelist }" itemValue="id"
						itemLabel="roleName"></form:options>
				</form:select></li>
			<li><label class="name03">姓名</label> <form:input
					class="input_160" path="xm" /></li>

			<li><label class="name03">性别</label> <form:select path="xbm"
					class="input_160">
					<option value>请选择</option>
					<option value="0">女</option>
					<option value="1">男</option>
				</form:select></li>

		</ul>
	</div>
	<div class="filterContent">
		<ul>

			<li><label class="name03">量表名称</label> <form:select
					path="scaleId" class="input_160">
					<option value>请选择</option>

				</form:select></li>
			<li><label class="name03">处理结果</label> <form:select
					path="iswarnsure" class="input_160" defaultValue="0">
					<option value="0">未处理</option>
					<option value="1">忽略</option>
					<option value="2">预警</option>

				</form:select></li>
			<li><label class="name03">典型个案</label> <form:select
					path="warningGrade" class="input_160">
					<option value>请选择</option>
					<form:options items="${warningLever}" itemLabel="name"
						itemValue="state"></form:options>
				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">操作时间</label> <input type="text"
				class="input_160" name="beginDate" id="beginDate1"></li>
			<li><label class="name03">至</label> <input type="text"
				name="endDate" class="input_160" id="endDate1"></li>

		</ul>
	</div>
	<div class="buttonContent">
		<input type="submit" id="query1" name="query" value="查询"
				class="button-mid blue" /> <input type="button" name="clear"
				id="clear1" value="重置" class="button-mid blue" />
	</div>
</form:form>
<script type="text/javascript">
	$("#queryform1").ajaxForm({
		target : "#list1"
	});
	$("#clear1").click(function() {
		$('#queryform1').clearForm();
	});

	$("#beginDate1").datepicker(
			{ //绑定开始日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				showWeek : true, //显示星期	
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置结束日期的最小日期
					$("#endDate1").datepicker('option', 'minDate',
							new Date(dateText.replace('-', ',')));

				}
			});

	$("#endDate1").datepicker(
			{ //设置结束绑定日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				showWeek : true, //显示星期	
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置开始日期的最大日期
					$('#beginDate1').datepicker('option', 'maxDate',
							new Date(dateText.replace('-', ',')));
				}
			});
</script>