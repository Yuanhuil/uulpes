<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/archivemanage/archiveTeamTeacher/view.do"
	commandName="analyze" method="post" target="_blank">
	<form:hidden path="schoolid" />
	<input type="hidden" name="filed" id="filed">

	

	<div class="filterContent">
		<ul>
		<li><label class="name03">角色名称</label> <form:select  path="role" cssClass="select_160"  onchange="changeRole('1','3');">
							
							<form:options items="${rolelist }" itemLabel="roleName" itemValue="id" />
							</form:select></li>
			<li><label class="name03">开始时间</label> <input type="text"
				name="beginDate" id="beginDate" class="input_160"></li>
			<li><label class="name03">结束时间 </label> <input type="text"
				name="endDate" id="endDate" class="input_160"></li>
		</ul>
	</div>
	<div class="buttonContent">
		<input type="button" id="query" name="query"
				class="button-small blue" value="查询" /> <input type="button"
				name="clear" id="clear" class="button-small blue" value="重置" />
		<input type="submit" id="subBut" style="display:none" />
	</div>
</form:form>
<script type="text/javascript">
	$("#clear").click(function() {
		$('#queryform').clearForm();
	});
	$("#query").click(function() {
	
		var groupid = $("#groupid").val();
		var nj = $("#nj").val();

		if (groupid == 0) {
			layer.open({content:"请选择学段"});
			return;
		}
		debugger;
		if (nj == "") {
			layer.open({content:"请选择年级"});
			return;
		}
		$('#subBut').click();
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
	}) */

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