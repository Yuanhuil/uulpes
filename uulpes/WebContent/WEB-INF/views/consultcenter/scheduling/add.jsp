<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="编辑">
	<form:form id="editForm"
		action="/pes/consultcenter/scheduling/save.do" method="post"
		commandName="scheduling">
		<form:hidden path="schoolid" />
		<form:hidden path="id" />
		<form:hidden path="teacherid" />
		<table>
			<tr>
				<td>咨询教师：</td>
				<td><input value="${teacherName}" readonly /></td>
			</tr>
			<tr>
				<td>日期：</td>
				<td><form:input path="date" readonly="true" /></td>
			</tr>

			<tr>
				<td>开始时间：</td>
				<td><form:select id="startTimeId" path="" name="startTimeId"
					oldValue="${startTimeId==0?'16':startTimeId}" onchange="checkSETimeValue()">
						<option value="">请选择</option>
						<!-- <option value="8">08:00</option>
						<option value="9">09:00</option>
						<option value="10">10:00</option>
						<option value="11">11:00</option>
						<option value="12">12:00</option>
						<option value="13">13:00</option>
						<option value="14">14:00</option>
						<option value="15">15:00</option>
						<option value="16">16:00</option>
						<option value="17">17:00</option> -->
						<form:options items="${timeEnum }" itemValue="value" class="itemTime"
						itemLabel="info"></form:options>
				</form:select></td>
			</tr>
			<tr>
				<td>结束时间：</td>
				<td><form:select id="endTimeId" path="" name="endTimeId"
					oldValue="${endTimeId}" onchange="checkSETimeValue()">
						<option value="">请选择</option>
						<!-- <option value="8">08:00</option>
						<option value="9">09:00</option>
						<option value="10">10:00</option>
						<option value="11">11:00</option>
						<option value="12">12:00</option>
						<option value="13">13:00</option>
						<option value="14">14:00</option>
						<option value="15">15:00</option>
						<option value="16">16:00</option>
						<option value="17">17:00</option> -->
						<form:options items="${timeEnum }" itemValue="value" class="itemTime"
						itemLabel="info"></form:options>
				</form:select></td>
			</tr>
			<tr>
				<td>有效时间：</td>
				<td><select name="dateType" id="dateType"
					onchange="checkDateType()">
						<option value="today" selected="selected">今天</option>
						<option value="everyWeek">每周</option>
						<option value="everyDay">每天</option>
				</select></td>
			</tr>
			<tr >
				<td class="endDatePid" style="visibility:hidden">结束日期：</td>
				<td class="endDatePid" style="visibility:hidden"><input id="endDate1" type="txt" name="endDate" /></td>
			</tr>
		</table>
	</form:form>
</div>
<script type="text/javascript">
	$("#dialog-form1")
			.dialog(
					{
						appendTo : "#editDiv",
						autoOpen : false,
						modal : true,
						height : 400,
						width : 600,
						buttons : {
							"删除" : function() {
								$("#dialog-form1").dialog("close");
								$("#editForm")
										.attr("action",
												'/pes/consultcenter/scheduling/delete.do');
								$("#editForm").ajaxSubmit({
									target : "#list",
									success : function() {
										$("#dialog-form1").dialog("close");
										layer.open({content:"删除成功!"});
									},
									error : function() {
										layer.open({content:"删除失败"});
									}
								});
								return false;
							},
							"保存" : function() {
								$("#dialog-form1").dialog("close");
								$("#editForm").ajaxSubmit({
									target : "#list",
									success : function() {
										$("#dialog-form1").dialog("close");

										layer.open({content:"保存成功!"});
									},
									error : function() {
										layer.open({content:"保存失败"});
									}
								});
								return false;
							},
							"取消" : function() {
								$("#dialog-form1").dialog("close");
							}
						},
					});

	$("#add_form").ajaxForm({
		target : "#list"
	});

	$("#endDate1").datepicker({ //设置结束绑定日期
		dateFormat : 'yy-mm-dd',
		changeMonth : true, //显示下拉列表月份
		changeYear : true, //显示下拉列表年份
		showWeek : true, //显示星期	
		firstDay : "1", //设置开始为1号
		onSelect : function(dateText, inst) {
			//设置开始日期的最大日期
			$("#date").datepicker("option", "maxDate", dateText);
		}
	});

	function checkDateType() {
		var value = $("#dateType").val();
		if (value == 'today') {
			$(".endDatePid").attr("style", "visibility:hidden");
		} else {
			$(".endDatePid").attr("style", "");

		}

	}
	function checkSETimeValue() {

		var startTimeId = $("#startTimeId").val();
		var endTimeId = $("#endTimeId").val();
		if (parseInt(startTimeId) >= parseInt(endTimeId)) {
			$(this).find('option').each(function(i, item) {
				var name = $(item).attr("value");
				if (name === "") {
					$(item).attr("selected", "selected");
				}
			});
			layer.open({content:"开始时间必须小于结束时间"});
		}
	}
	select2selected("startTimeId");
	select2selected("endTimeId");
</script>