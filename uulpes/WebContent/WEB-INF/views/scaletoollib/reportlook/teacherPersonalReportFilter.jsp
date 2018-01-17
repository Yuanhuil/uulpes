<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="teacherPersonalReporturl" value="../../scaletoollib/reportlook/searchTeacherPersonalReporturl.do"></c:set>
<form:form id="teacherPersonalReportForm" name="teacherPersonalReportForm" method="post" commandName="reportLookTeacherFilterParam" action="${teacherPersonalReporturl }">
	<div class="filterContent">
				<ul>
					<li><label class="name03">角色名称</label> <form:select id="roleid" path="roleid" cssClass="select_160">
						<form:option value='-1'>请选择</form:option>
						<form:options items="${exList }" itemLabel="roleName" itemValue="roleId" />
					</form:select></li>
					<li><label class="name03">量表名称</label> <form:select id="scaleId" path="scaleId" cssClass="select_160">
						<form:option value="-1">请选择</form:option>
						<form:options items="${scaleList }" itemLabel="shortname" itemValue="id"/>
					</form:select></li>
					<li><label class="name03">典型个案</label> <form:select id="warningLevel" path="warningLevel" cssClass="select_160">
							<form:option value="-1">请选择</form:option>
							<form:option value="1">一级</form:option>
							<form:option value="2">二级</form:option>
							<form:option value="3">三级</form:option>
					</form:select></li>
				</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</label><form:input id="username" path="username" type="text" cssClass="input_160"></form:input></li>
			<li><label class="name03">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</label> <form:select path="gender" cssClass="select_160">
						<form:option value="-1">请选择</form:option>
						<form:option value="1">男</form:option>
						<form:option value="2">女</form:option>
			</form:select></li>
			<li><label class="name03">无效问卷</label> <form:select path="validVal" class="select_160">
				<form:option value="-1">请选择</form:option>
				<form:option value="1">有效</form:option>
				<form:option value="0">无效</form:option>
			</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">评测时间</label> <form:input path="starttime" id="starttime" class="input_160"
				type="text"></form:input></li><li><label class="name03">至</label><form:input path="endtime" id="endtime" class="input_160"
				type="text"></form:input></li>
		</ul>
	</div>
	<div class="buttonContent">
	   <div class="buttonLeft">
		<ul>
			<li><shiro:hasPermission name="assessmentcenter:checkreport:teacherreport:delete"><input class="button-mid white" type="button" onclick="delSelectedTeacherPersonalReports();" value="删除" /></shiro:hasPermission></li>
			<li><input class="button-mid white" type="button" onclick="downloadSelectedTeacherPersonalReports('${examresultStudent.studentId}');" value="下载" /></li>
		</ul>
		</div>
		<div class="buttonRight">
		<ul>
		 	<li><input class="button-mid blue" type="button" onclick="searchTeacherPersonal();" value="查询" /></li>
			<li><input class="button-mid blue" type="button" value="重置" onclick="teacherPersonalReset();" /></li>
		</ul>
		</div>
	</div>

</form:form>
<script type="text/javascript">

$(function() {  
	 $( "#starttime" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
	 $( "#endtime" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
});

function teacherPersonalReset(){
	$("#teacherPersonalReportForm")[0].reset();
}

function searchTeacherPersonal(){
	$("#teacherPersonalReportForm").ajaxSubmit({
		//target : "#tabs_teacherReport",
		target : "#teacherPersonalReportTableCon",
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