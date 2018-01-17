<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.List" %>
<%@page import="com.njpes.www.entity.baseinfo.enums.OrganizationType" %>
<%@page import="com.njpes.www.entity.baseinfo.organization.Organization" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="stuPersonalReporturl" value="../../scaletoollib/reportlook/searchStuPersonalReporturl.do"></c:set>
<form:form id="studentPersonalReportForm" name="studentPersonalReportForm" method="post" commandName="reportLookStudentFilterParam" action="${stuPersonalReporturl }">
<c:choose>
		<c:when test="${orgType=='2' }">
			<div class="filterContent">
				<ul>
					<li><label class="name03">学段</label> <form:select id="xd" class="select_160" path="xd" onchange="getNj();">
						<form:option value="-1" id="se">请选择</form:option>
						<form:options name="xdse" items="${xdList }" itemLabel="xdmc" itemValue="xd"/>
					</form:select></li>
					<li><label class="name03">年级名称</label> <form:select id="nj" class="select_160" path="nj" onchange="getBj();">
						<form:option value="-1">请选择</form:option>
					</form:select></li>
					<li><label class="name03">班级名称</label> <form:select id="bj" class="select_160"  path="bj">
						<form:option value="-1">请选择</form:option>
					</form:select></li>
					
				</ul>
			</div>
			<div class="filterContent">
				<ul>
				    <li><label class="name03">量表名称</label> <form:select id="scaleName" class="select_160"  path="scaleId">
						<form:option value="-1">请选择</form:option>
						<form:options items="${scaleList }" itemLabel="shortname" itemValue="id"/>
					</form:select></li>
					<li><label class="name03">姓名</label><form:input id="username" class="input_160"  path="username" type="text"></form:input></li>
					<!-- <li><label class="name03">性别</label>
					<form:select path="gender"  class="select_100"  >
						<form:option value="-1">请选择</form:option>
						<form:option value="1">男</form:option>
						<form:option value="2">女</form:option>
					</form:select></li>-->
					<li><label class="name03">典型个案</label><form:select id="warningLevel" class="select_160"  path="warningLevel">
							<form:option value="-1">请选择</form:option>
							<form:option value="1">一级</form:option>
							<form:option value="2">二级</form:option>
							<form:option value="3">三级</form:option>
					</form:select></li>
				</ul>
			</div>
		</c:when>
		<c:otherwise>
			<div class="filterContent">
				只有学校管理员才能看到该页！
			</div>
		</c:otherwise>
	</c:choose>
	<div class="filterContent">
		<ul>
			<li><label class="name03">无效问卷</label> <form:select path="validVal" class="select_160">
						<form:option value="-1">请选择</form:option>
						<form:option value="1">有效</form:option>
						<form:option value="0">无效</form:option>
					</form:select></li>
			<li><label class="name03">评测时间</label> <form:input path="starttime" id="starttime" class="input_160"
				type="text"></form:input></li>
			<li><label class="name03">至</label><form:input path="endtime" id="endtime" class="input_160"
				type="text"></form:input></li>

		</ul>
	</div>
	<div class="buttonContent">
	   <div class="buttonLeft">
		<ul>
			<li><shiro:hasPermission name="assessmentcenter:checkreport:studentreport:delete"><input class="button-mid white" type="button" onclick="delSelectedStudentPersonalReports();" value="删除" /></shiro:hasPermission></li>
			<li><input class="button-mid white" type="button" onclick="downloadSelectedStudentPersonalReports('${examresultStudent.studentId}');" value="下载" /></li>
		</ul>
		</div>
		<div class="buttonRight">
		<ul>
		 	<li><input class="button-mid blue" type="button" onclick="searchStudentExamresult();" value="查询" /></li>
			<li><input class="button-mid blue" type="button" value="重置" onclick="personalReset();" /></li>
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

function personalReset(){
	$("#studentPersonalReportForm")[0].reset();
	$("#nj").html("<option value='-1'>请选择</option>");
	$("#bj").html("<option value='-1'>请选择</option>");
}

function getNj(){
	var xd = document.getElementById("xd").value;
	if(xd==-1){
		$("#nj").html("<option value='-1'>请选择</option>");
		$("#bj").html("<option value='-1'>请选择</option>");
		return;
	}
	$.ajax({
		   type: "POST",
		   url: "../../scaletoollib/reportlook/queryDistinctNJFromExamresultStudent.do",
		   data: {
			   "xd":xd,
			   "xzxs":null
			   },
		   success: function(msg){
			   var nj = $("#nj");
				var njarray = jQuery.parseJSON(msg);
				if(njarray.length>0){
					var optionstr="<option value='-1'>请选择</option>";
					for(var i=0;i<njarray.length;i++){
						var njmc = njarray[i].nj;
						optionstr = optionstr+"<option value="+njmc+">"+njmc+"级</option>";
					}
					nj.html(optionstr);
				}else{
					nj.html("");
					nj.html("<option value='-1'>请选择</option>");
				}
		   },
	     error: function()
	     {  	
	   	    layer.open({content:"调用出现错误，查找年级失败"});
	   	 }
		});
}
function searchStudentExamresult(){
	$("#studentPersonalReportForm").ajaxSubmit({
		target : "#studentPersonalReportTableCon",
		success:function(){
			//var xxddsa = document.getElementsByName("xdse");
			//for(var i=0;i<xxddsa.length;i++){
				//xxddsa[i].removeAttribute("selected");
			//}
			//document.getElementById("se").setAttribute("selected","selected");
		}
	});
}
function getBj(){
	
	var nj = document.getElementById("nj").value;
	if(nj==-1){
		$("#bj").html("<option value='-1'>请选择</option>");
		return;
	}
	var xd = document.getElementById("xd").value;
	if(xd==-1){
		return;
	}
	$.ajax({
		   type: "POST",
		   url: "../../scaletoollib/reportlook/queryDistinctBJFromExamresultStudent.do",
		   data: {
			   "xd":xd,
			   "nj":nj
			   },
		   success: function(msg){
			   var bj = $("#bj");
				var bjarray = jQuery.parseJSON(msg);
				if(bjarray.length>0){
					var optionstr="<option value='-1'>请选择</option>";
					for(var i=0;i<bjarray.length;i++){
						var bjj = bjarray[i].bj;
						var bjmc = bjarray[i].bjmc;
						optionstr = optionstr+"<option value="+bjj+">"+bjmc+"</option>";
					}
					bj.html(optionstr);
				}else{
					bj.html("");
					bj.html("<option value='-1'>请选择</option>");
				}
		   },
	     error: function()
	     {  	
	   	    layer.open({content:"调用出现错误，查找班级失败"});
	   	 }
		});
}
</script>