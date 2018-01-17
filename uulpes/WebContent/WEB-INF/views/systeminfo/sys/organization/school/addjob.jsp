<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!-- Modal -->
<c:set var="jobformaction" value="" scope="page"/>
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="jobformaction"/>
		<c:set var="jobformaction" scope="page" value="${ctx}/systeminfo/sys/organization/school/${job.schoolid }/job/create.do"/>
	</c:when>
	<c:when test="${op eq '查看'}">
		<c:remove var="jobformaction"/>
		<c:set var="jobformaction" scope="page" value=""/>
	</c:when>
	<c:when test="${op eq '修改'}">
		<c:remove var="jobformaction"/>
		<c:set var="jobformaction" scope="page" value="${ctx}/systeminfo/sys/organization/school/job/${job.id }/${job.schoolid }/update.do"/>
	</c:when>
	<c:otherwise>
		<c:remove var="jobformaction"/>
		<c:set var="jobformaction" scope="page" value="${ctx}/systeminfo/sys/organization/school/${job.schoolid }/job/create.do"/>
	</c:otherwise>
</c:choose>
<div id="dialog-job" title="${empty op ? '新增' : op}主要工作">
  <form:form id="editjobForm" method="post" commandName="job"
  action="${jobformaction}">
	  <div class="filterContent-dlg">
			<ul>
				<li><label class="name04">任务名称</label> <form:input path="rwmc" cssClass="input_160 validate[required]"/></li>
				<li> <label class="name04">任务简历</label> <form:textarea path="rwjl" rows="10" cssClass="validate[required]"/></li>
			</ul>
	  </div>
  </form:form>
</div>
<script type="text/javascript">
	var buttonsOps = {};
	<c:choose>
	<c:when test="${empty op || op eq '新增' || op eq '修改'}">
	buttonsOps = {
		"保存" : function() {
			if(!$('#editjobForm').validationEngine('validate'))
				 return false;
			$("#editjobForm").ajaxSubmit({
				target : "#tabs_job",
				success : function() {
					$("#dialog-job").dialog("close");
					layer.open({content:"保存成功!"});
				},
				error : function() {
					layer.open({content:"保存失败"});
				}
			});
			$("#editjobForm").clearForm();
			return false;
		},
		"取消" : function() {
			$("#dialog-job").dialog("close");
		}
	};
	</c:when>
	<c:when test="${op eq '查看'}">
	buttonsOps = {
		"返回" : function() {
			$("#dialog-job").dialog("close");
		}
	};
	</c:when>
	</c:choose>
	$("#dialog-job").dialog({
		appendTo : "#addjobDiv",
		autoOpen : false,
		modal : true,
		height : 300,
		width : 500,
		buttons : buttonsOps
	});
</script>