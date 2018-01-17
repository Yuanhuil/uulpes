<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/consultcenter/analyze/list.do" commandName="analyze"
	method="post">
	<form:hidden path="schoolid" />
	<input type="hidden" name="filed" id="filed">
	<div class="filterContent">
		<ul>
			<li><label class="name03">角色</label> <form:select
					class="select_160" path="role" onchange="hideClass(this)">
					<option value="1">学生</option>
					<option value="2">老师</option>
				</form:select></li>

			<li><label class="name03">性别</label> <form:select
					class="select_160" path="sex">
					<option value>请选择</option>
					<option value="0">女</option>
					<option value="1">男</option>
				</form:select></li>
		</ul>
	</div>
	<c:if test="${fn:length(xdlist) > 1 }">
		<div class="filterContent" id="class">
			<ul>
				<li><label class="name03">学 段</label> <form:select
						path="groupid" cssClass="input_160">
						<form:option value="0">请选择</form:option>
						<form:options items="${xdlist}" itemValue="id" itemLabel="info" />
					</form:select></li>
				<form:hidden path="grade" />

				<li><label class="name03">年 级</label> <select id="nj"
					class="input_160">
						<option value=""></option>
				</select></li>
				<!-- <li><label class="name03">班 级</label><select id="bh"
					class="input_160">
						<option value=""></option>
				</select></li> -->
			</ul>
		</div>
	</c:if>
	<c:if test="${fn:length(xdlist) == 1 }">
		<div class="filterContent" id="class">
			<ul>
				<form:hidden path="groupid" />
				<form:hidden path="grade" />
				<form:hidden path="className" />
				<li><label class="name03">年 级</label> <select id="nj"
					class="input_160">
						<option value="">${warningInterveneS.grade}</option>
				</select></li>
				<%-- <li><label class="name03">班 级</label><select id="bh"
					class="input_160">
						<option value="">${warningInterveneS.className}</option>
				</select></li> --%>
			</ul>
		</div>
	</c:if>

	<div class="filterContent">
		
			<label class="name03">汇总项</label> <div style="float:left"><input type="checkbox" name=""
				value="consultationTypeId" onchange="initFiled(this)">咨询类型 <input
				type="checkbox" name="" value="consultationModeId"
				onchange="initFiled(this)"> 咨询方式<input type="checkbox"
				value="gradeid" onchange="initFiled(this)"> 年级
			<%-- <li><label class="name03">辅导结果</label> <form:select class="select_160" path="result">
					<option value>请选择</option>

				</form:select>
			</li>	 --%>
		</div>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">操作时间</label> <input type="text"
				name="beginDate" id="beginDate" class="input_160"></li>
			<li><label class="name03">至 </label> <input type="text"
				name="endDate" id="endDate" class="input_160"></li>
		</ul>
	</div>
	<div class="buttonContent">
		<input type="submit" id="query" name="query" class="button-small blue"
			value="查询" /> <input type="button" name="clear" id="clear"
			class="button-small blue" value="重置" />
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

	$("#groupid")
			.change(
					function() {
						$("#nj").empty();
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
										$("#nj")
												.append(
														"<option value=''>请选择</option>");
										$.each(data, function(i, k) {
											$("#nj").append(
													"<option value='" + k.gradeid + "'>"
															+ k.njmc
															+ "</option>");
										});
									}

								});
					});
	$("#nj")
			.change(
					function() {
						$("#bh").empty();
						var nj = $(this).val();
						$("#grade").attr("value",
								$(this).find("option:selected").text());
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

										$("#bh")
												.append(
														"<option value=''>请选择</option>");
										$.each(data, function(i, k) {
											$("#bh").append(
													"<option value='" + k.bh + "'>"
															+ k.bjmc
															+ "</option>");
										});
									}

								});
					});
	/* $("#bh").change(function() {
		$("#className").attr("value", $(this).find("option:selected").text());
	})
	 */
	function hideClass(obj) {
		var role = $(obj).val();
		if (role == 1) {
			$("#class").show();
			$("#class1").show();

		} else {
			$("#class").hide();
			$("#class1").hide();
		}

	}

	function initFiled(obj) {

		var val = $(obj).val();
		var field = $("#filed").val();
		if (obj.checked) {

			$("#filed").val(field + val + ",");
		} else {
			field = field.replace(val + ",", "");
			$("#filed").val(field);
		}
	}
</script>