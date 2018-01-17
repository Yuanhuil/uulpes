<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="teaComplianceReportsurl" value="../../scaletoollib/reportlook/searchTeaComplianceReportsurl.do"></c:set>
<form:form id="teacherComplianceReportForm" name="teacherComplianceReportForm" method="post" commandName="reportLookTeacherFilterParam" action="${teaComplianceReportsurl }">
	<div class="filterContent4">
		<ul>
			<li><label class="name03">角色名称</label> <form:select id="roleName" path="roleid" cssClass="input_100">
						<form:option value="-1">请选择</form:option>
						<form:options items="${exList }" itemLabel="roleName" itemValue="roleId" />
					</form:select></li>
			<li><label class="name03">姓&nbsp;&nbsp;&nbsp;&nbsp;名</label> <form:input id="username" path="username" type="text" cssClass="input_100"></form:input></li>
			<li><label class="name03">性&nbsp;&nbsp;&nbsp;&nbsp;别</label> 
				<form:select path="gender" cssClass="input_100">
						<form:option value="-1">请选择</form:option>
						<form:option value="1">男</form:option>
						<form:option value="2">女</form:option>
				</form:select>
			</li>
			<li><label class="name03">身份证号</label> <form:input id="identiyId" path="identiyId" type="text" cssClass="input_140"></form:input></li>
		</ul>
	</div>
	
	<div class="buttonContent">
			<input class="button-small blue"
			type="button" value="查询" onclick="searchTCom();"/>
			<input class="button-small blue"
			type="button" value="重置" onclick="teacherComplianceReportReset();" />
	</div>
</form:form>
<script type="text/javascript">
function teacherComplianceReportReset(){
	$("#teacherComplianceReportForm").reset();
}

function searchTCom(){
	$("#teacherComplianceReportForm").ajaxSubmit({
		target : "#teacherComplianceReportTableCon",
		success:function(){
			/* var xxddsa = document.getElementsByName("xdse2");
			for(var i=0;i<xxddsa.length;i++){
				xxddsa[i].removeAttribute("selected");
			}
			document.getElementById("se2").setAttribute("selected","selected"); */
		}
	});
}
</script>