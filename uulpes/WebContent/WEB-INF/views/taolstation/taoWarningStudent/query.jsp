<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/taolstation/taoWarningStudent/list.do"
	commandName="taoWarningStudent" method="post">
	<!--	<c:if test="${fn:length(xdlist) > 1 }">
		<div class="filterContent">
			<ul>
				<li><label class="name03">学 段</label> <form:select
						path="groupid" cssClass="dropBox01 input_160" items="${xdlist}"
						itemValue="id" itemLabel="info"></form:select></li>
				<li><label class="name03">年 级</label> <form:select path="grade"
						cssClass="dropBox01 input_160"></form:select></li>
				<li><label class="name03">班 级</label> <form:select
						path="className" cssClass="dropBox01 input_160" /></li>
			</ul>
		</div>
	</c:if>
	<c:if test="${fn:length(xdlist) == 1 }">
		<div class="filterContent">
			<ul>
				<form:hidden path="xd" />
				<li><label class="name03">年 级</label> <form:select path="grade"
						cssClass="dropBox01 input_160" items="${njlist}" itemValue="nj"
						itemLabel="njmc"></form:select></li>
				<li><label class="name03">班 级</label> <form:select
						path="className" cssClass="dropBox01 input_160" /></li>
			</ul>
		</div>
	</c:if>-->
	<div class="filterContent">
		<ul>

			<li><label class="name03">姓名</label> <form:input path="name"
					id="name1" class="input_160" /></li>
			<li><label class="name03">性别</label> <form:select path="sex"
					id="sex1" class="input_160">
					<option value>请选择</option>
					<option value="0">女</option>
					<option value="1">男</option>
				</form:select></li>
			<li><label class="name03">预警级别</label> <form:select path="level"
					id="level1" class="input_160">
					<option value>请选择</option>
					<form:options items="${warningLever}" itemLabel="name"
						itemValue="state"></form:options>
				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">操作时间</label> <input type="text"
				name="beginDate" class="input_160" id="beginDate"></li>
			<li><label class="name03">至</label> <input type="text"
				name="endDate" class="input_160" id="endDate"></li>

		</ul>
	</div>
	<div class="buttonContent">
		<input type="button" id="add" value="添加"
				class="button-mid blue" /> <input type="submit" id="query"
				name="query" value="查询" class="button-mid blue" /> <input
				type="button" name="clear" id="clear" value="重置"
				class="button-mid blue" />
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
					$("#endDate").datepicker('option', 'minDate',
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
	$(function() {
		$(".dropBox01").chosen({});
		$("#groupid")
				.change(
						function() {
							$("#grade").empty();
							var xd = $(this).val();
							$
									.ajax({
										url : "${baseaction}/pes/systeminfo/sys/user/student/getGrades.do",
										data : {
											"xd" : xd
										},
										dataType : "json",
										type : "POST",
										success : function(data) {
											$("#grade").chosen("destroy");
											$.each(data, function(i, k) {
												$("#grade").append(
														"<option value='" + k.nj + "'>"
																+ k.njmc
																+ "</option>");
											});
											$("#grade").chosen({})
										}

									});
						});
		$("#grade")
				.change(
						function() {
							$("#className").empty();
							var nj = $(this).val();
							var xd = $("#groupid").val();
							$
									.ajax({
										url : "${baseaction}/pes/systeminfo/sys/user/student/getClasses.do",
										data : {
											"nj" : nj,
											"xd" : xd
										},
										dataType : "json",
										type : "POST",
										success : function(data) {
											$("#className").chosen("destroy");
											$.each(data, function(i, k) {
												$("#className").append(
														"<option value='" + k.bh + "'>"
																+ k.bjmc
																+ "</option>");
											});
											$("#className").chosen({})
										}

									});
						});
	})
</script>