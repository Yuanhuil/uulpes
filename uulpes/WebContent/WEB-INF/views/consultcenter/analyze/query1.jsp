<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform_1" name="form"
	action="/pes/consultcenter/analyze/list1.do" commandName="analyze"
	method="post">
	<form:hidden path="schoolid" />
	<input type="hidden" name="filed" id="filed_1">
	<div class="filterContent">
		<ul>
			<li><label class="name03">角色</label> <form:select
					class="select_160" path="role" onchange="hideClass1(this)" id="role_1">
					<option value="1">学生</option>
					<option value="2">老师</option>
				</form:select></li>

			<li><label class="name03">性别</label> <form:select
					class="select_160" path="sex" id="sex_1">
					<option value>请选择</option>
					<option value="0">女</option>
					<option value="1">男</option>
				</form:select></li>
		</ul>
	</div>
	<c:if test="${fn:length(xdlist) > 1 }">
		<div class="filterContent" id="class_1">
			<ul>
				<li><label class="name03">学 段</label> <form:select
						path="groupid" cssClass="input_160" id="groupid_1">
						<form:option value="0">请选择</form:option>
						<form:options items="${xdlist}" itemValue="id" itemLabel="info" />
					</form:select></li>
				<form:hidden path="grade" id="grade_1"/>
				<%-- <form:hidden path="className" id="className_1"/> --%>
				<li><label class="name03">年 级</label> <select id="nj_1"
					class="input_160">
						<option value=""></option>
				</select></li>
				<!-- <li><label class="name03">班 级</label><select id="bh_1"
					class="input_160">
						<option value=""></option>
				</select></li> -->
			</ul>
		</div>
	</c:if>
	<c:if test="${fn:length(xdlist) == 1 }">
		<div class="filterContent" id="class_1">
			<ul>
				<form:hidden path="groupid" id="groupid_1"/>
				<form:hidden path="grade" id="grade_1"/>
			<%-- 	<form:hidden path="className" id="className_1"/> --%>
				<li><label class="name03">年 级</label> <select id="nj_1"
					class="input_160">
						<option value="">${warningInterveneS.grade}</option>
				</select></li>
				<%-- <li><label class="name03">班 级</label><select id="bh_1"
					class="input_160">
						<option value="">${warningInterveneS.className}</option>
				</select></li> --%>
			</ul>
		</div>
	</c:if>

	<div class="filterContent">
		<label class="name03">汇总项</label><div style="float:left"> <input type="checkbox"
				name="" value="result" onchange="initFiled_1(this)">干预结果
				<input type="checkbox" name="" value="LEVEL"
				onchange="initFiled_1(this)"> 预警级别
				<input type="checkbox" name="" value="type"
				onchange="initFiled_1(this)"> 干预方式
				<input type="checkbox" name="" value="dispose_type"
				onchange="initFiled_1(this)"> 处置方式
				
				
		<input type="checkbox" value="gradeid"
				onchange="initFiled_1(this)"> 年级</div>
			<%-- <li><label class="name03">辅导结果</label> <form:select class="select_160" path="result">
					<option value>请选择</option>

				</form:select>
			</li>	 --%>
		
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">操作时间</label> <input type="text"
				name="beginDate" id="beginDate_1" class="input_160"></li>
			<li><label class="name03">至 </label> <input type="text"
				name="endDate" id="endDate_1" class="input_160"></li>
		</ul>
	</div>
	<div class="buttonContent">
		<input type="submit" id="query_1" name="query"
				class="button-small blue" value="查询" /> <input type="button"
				name="clear" id="clear_1" class="button-small blue" value="重置" />
	</div>
</form:form>
<script type="text/javascript">
	$("#queryform_1").ajaxForm({
		target : "#list1"
	});
	$("#clear_1").click(function() {
		$('#queryform_1').clearForm();
	});

	$("#beginDate_1").datepicker(
			{ //绑定开始日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				showWeek : true, //显示星期	
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置结束日期的最小日期
					$("#endDate_1").datepicker('option', 'minDate',
							new Date(dateText.replace('-', ',')));

				}
			});

	$("#endDate_1").datepicker(
			{ //设置结束绑定日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				showWeek : true, //显示星期	
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置开始日期的最大日期
					$('#beginDate_1').datepicker('option', 'maxDate',
							new Date(dateText.replace('-', ',')));
				}
			});

	$("#groupid_1")
			.change(
					function() {
						$("#nj_1").empty();
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
										$("#nj_1")
												.append(
														"<option value=''>请选择</option>");
										$.each(data, function(i, k) {
											$("#nj_1").append(
													"<option value='" + k.gradeid + "'>"
															+ k.njmc
															+ "</option>");
										});
									}

								});
					});
	$("#nj_1")
			.change(
					function() {
						$("#bh_1").empty();
						var nj = $(this).val();
						$("#grade_1").attr("value",
								$(this).find("option:selected").text());
						var xd = $("#groupid_1").val();
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

										$("#bh_1")
												.append(
														"<option value=''>请选择</option>");
										$.each(data, function(i, k) {
											$("#bh_1").append(
													"<option value='" + k.bh + "'>"
															+ k.bjmc
															+ "</option>");
										});
									}

								});
					});
	/* $("#bh_1").change(function() {
		$("#className_1").attr("value", $(this).find("option:selected").text());
	}) */

	function hideClass1(obj) {
		var role = $(obj).val();
		if (role == 1) {
			$("#class_1").show();
			$("#class1_1").show();

		} else {
			$("#class_1").hide();
			$("#class1_1").hide();
		}

	}

	function initFiled_1(obj) {

		var val = $(obj).val();
		var field = $("#filed_1").val();
		if (obj.checked) {

			$("#filed_1").val(field + val + ",");
		} else {
			field = field.replace(val + ",", "");
			$("#filed_1").val(field);
		}
	}
</script>