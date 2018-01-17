<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="编辑">
	<form:form id="editForm"
		action="/pes/consultcenter/appointment/save.do" method="post"
		commandName="appointment">
		<form:hidden path="id" />
		<input name="beginDate" type="hidden" value="${beginDate}">
		<input name="endDate" type="hidden" value="${endDate}">
		<table>
			<tr><td>
				咨询员：</td><td>${teacherName}</td></tr>
			<tr><td>
				日期：</td> <td><fmt:formatDate value="${scheduling.date}"
						pattern="yyyy-MM-dd" /></td></tr>
			
			<tr><td>
				时间：</td><td>
					${timeEnum[scheduling.timeid]}--${timeEnum[endtimeid]}
			</td></tr>

			<tr><td>
				姓名：</td><td>
				<form:input path="name" class="input_160"/>
			</td></tr>
			<tr><td>
				咨询方式：</td><td>
				<form:select path="model" items="${consultationModels}"
					itemLabel="name" itemValue="id" class="select_160"></form:select>
			</td></tr>
			<tr><td>
				咨询类型：</td><td>
				<form:select path="type" items="${consultTypes}" itemLabel="name"
					itemValue="id" class="select_160"></form:select>
			</td></tr>
			<tr><td>
				问题描述：</td><td>
				<form:textarea path="describes" />
			</td></tr>
			<tr><td>
				联系方式：</td><td>
				<form:input path="contact" class="input_160"/>
			</td></tr>
			<tr><td>
				状态：</td><td>
				<form:select path="status" items="${appointmentState}"
					itemLabel="name" itemValue="state" class="select_160"></form:select>
			</td></tr>
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
						height : 500,
						width : 800,
						buttons : {
						<c:if test="${empty view || view eq false}">
							"删除" : function() {
								$("#dialog-form1").dialog("close");
								$("#editForm")
										.attr("action",
												'/pes/consultcenter/appointment/delete.do');
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
							</c:if>
							"取消" : function() {
								$("#dialog-form1").dialog("close");
							}
						},
					});

	$("#add_form").ajaxForm({
		target : "#list"
	});

	select2selected("startTimeId");
	select2selected("endTimeId");
</script>