<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link rel="stylesheet" type="text/css" href="../../themes/theme1/css/jquery.multiselect.css" />
<script type="text/javascript" src="../../js/jqueryplugin/multiselect/jquery.multiselect.min.js"></script>
<c:choose>
	<c:when test="${orgType=='2' }">
		<c:set var="teaTeamReportsurl"
			value="../../scaletoollib/reportlook/teaTeamReportsSchoolurl.do"></c:set>
	</c:when>
	<c:otherwise>
	   <c:if test="${orgLevel=='3'}">
	   	<c:set var="teaTeamReportsurl"
			value="../../scaletoollib/reportlook/teaTeamReportsEduCityurl.do"></c:set>
	    </c:if>
	    <c:if test="${orgLevel=='4'}">
	    <c:set var="teaTeamReportsurl"
			value="../../scaletoollib/reportlook/teaTeamReportsEduCountryurl.do"></c:set>
		</c:if>
	</c:otherwise>
</c:choose>
<form:form id="teacherTeamReportForm" name="teacherTeamReportForm" method="post" 
commandName="reportLookTeacherFilterParam" action="${teaTeamReportsurl }"  target="_blank" >
<c:choose>
	<c:when test="${orgType=='2'}">
		<div class="filterContent">
		<ul>
			<li><label class="name03">角色名称</label> <form:select id="roleName" path="roleName" cssClass="select_160" onchange="changeRole('2','6');">
						<form:option value="-1">请选择</form:option>
						<form:option value="0">所有教师</form:option>
						<form:options items="${roleList }" itemLabel="roleName" itemValue="id" />
					</form:select></li>
			<li><label class="name03">量表来源</label> <form:select
						class="select_160" id="scaleSource" path="scaleSourceId">
						<form:option value="-1" label="请选择"></form:option>
						</form:select></li>
			<li><label class="name03">量表类别</label> <form:select
					    class="select_160" id="scaleType" path="scaleTypeId">
						<form:option value="-1" label="请选择"></form:option>
						</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">量表名称</label><form:select id="scaleName" path="scaleName" class="select_160">
						<form:option value="-1">请选择</form:option>
			</form:select></li>

		   <li><label class="name03">评测时间</label> <form:input path="starttime" id="starttimeTeam" cssClass="input_160"
				type="text"></form:input></li>
		   <li><label class="name03">至</label><form:input path="endtime" id="endtimeTeam" cssClass="input_160"
				type="text"></form:input></li>
		</ul>
	</div>
	</c:when>
	<c:otherwise>
	    <c:if test="${orgLevel==3}">
		<div class="filterContent">
				<ul>
					<li><label class="name03">机构层级</label> <select id="xzxs" name="xzxs"
								class="select_160" onchange="queryQXXXFun('${cityId }');">
									<option value="-1">请选择</option>
									<option value="1">区县</option>
									<option value="2">市直属学校</option>
					</select></li>
					<li><label id="qxorxx_label" class="name03">选择区县</label> <select id="qx_or_xx" 	class="multiselec_160" >
					</select></li>
				    <li><label class="name03">角色名称</label> <form:select id="roleName" path="roleName" cssClass="select_160"  onchange="changeRole('1','3');">
							<form:option value="-1">请选择</form:option>
							<form:option value="0">所有教师</form:option>
							<form:options items="${roleList }" itemLabel="roleName" itemValue="id" />
							</form:select></li>
			</ul>
		</div>
		<input id="qxorxxarray" name="qxorxxarray" type="hidden"/> 
	</c:if>
	<c:if test="${orgLevel==4}">
	<div class="filterContent">
	    <ul>
	      <li><label class="name03">选择学校</label> <select id="qx_or_xx"	class="multiselec_160" >
	          <c:forEach var="item" items="${schoolList}" >
            	  <option value="${item.id}">${item.name}</option>
       		  </c:forEach>
			</select>
		 </li>
		 <li><label class="name03">角色名称</label> <form:select id="roleName" path="roleName" cssClass="select_160"  onchange="changeRole('1','4');">
						<form:option value="-1">请选择</form:option>
						<form:option value="0">所有教师</form:option>
						<form:options items="${roleList }" itemLabel="roleName" itemValue="id" />
					</form:select></li>
	    </ul>
	 </div>
	 </c:if>
	 <div class="filterContent">
		<ul>
			<li><label class="name03">量表类别</label> <form:select
					class="select_160" id="scaleType" path="scaleTypeId" onchange="changeScaleTypeOrSource('${orgType}','${orgLevel }')">
						<form:option value="-1" label="请选择"></form:option>
						</form:select></li>
			<li><label class="name03">量表来源</label> <form:select
						class="select_160" id="scaleSource" path="scaleSourceId" onchange="changeScaleTypeOrSource('${orgType}','${orgLevel }')">
						<form:option value="-1" label="请选择"></form:option>
						</form:select></li>
			<li><label class="name03">量表名称</label><form:select id="scaleName" path="scaleName" class="select_160">
						<form:option value="-1">请选择</form:option>
			</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
		   <li><label class="name03">评测时间</label> <form:input path="starttime" id="starttimeTeam" cssClass="input_160"
				type="text"></form:input></li>
		   <li><label class="name03">至</label><form:input path="endtime" id="endtimeTeam" cssClass="input_160"
				type="text"></form:input></li>
		</ul>
	</div>
	 <input id="qxorxxarray" name="qxorxxarray" type="hidden"/> 
	</c:otherwise>
</c:choose>

	<input id="download" name="download" type="hidden"/> 
	<div class="buttonContent">
		<input class="button-small blue"
					type="button" value="生成团体报告" onclick="createTeamReport('${orgType}','${orgLevel}','no');" />
		<!-- <input class="button-small blue"
					type="button" value="下载团体报告" onclick="createTeamReport('${orgType}','${orgLevel}','yes');" />-->
					<input class="button-small blue"
					type="button" value="重置" onclick="teamReset();" />
		</div>

</form:form>
<script type="text/javascript">
var qxvs;
$(function() {
	$("#starttimeTeam").datepicker(
			{ //绑定开始日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				firstDay : "1" //设置开始为1号
				});
	$("#endtimeTeam").datepicker(
			{ //绑定开始日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				firstDay : "1" //设置开始为1号
				});
	qxvs = $("#qx_or_xx").multiselect({
		checkAllText: "全选", 
		uncheckAllText: '全不选'
	}); 
	
	$(".multiselec").css("width","154px");
	qxvs.multiselect('refresh');
});	 
function teamReset(){
	$("#teacherTeamReportForm")[0].reset();
	if(orgType=='2'){

	}
	else{
		if(orgLevel=='3'){
			$("#qx_or_xx").html("<option value='-1'>请选择</option>");
			$("#roleName").html("<option value='-1'>请选择</option>");
		}
		if(orgLevel=='4'){
			$("#roleName").html("<option value='-1'>请选择</option>");
		}
	}
}
function changeScaleTypeOrSource(orgType,orgLevel) {
	debugger;
	var roleid = document.getElementById("roleName").value;
	var countyidOrSchoolid =null;
	if(orgType=='2')
		countyidOrSchoolid =null;
	else  
		countyidOrSchoolid= document.getElementById("qx_or_xx").value;
	var xzxs = null;
	if(orgLevel==3)
	   xzxs = document.getElementById("xzxs").value;

	var scaletype=document.getElementById("scaleType").value;
	var scalesource=document.getElementById("scaleSource").value;
	$.ajax({
		 type: "POST",
		 url: "../../scaletoollib/reportlook/getScaleFromExamResultTeacher1.do",
		 data:{
			     "countyidOrSchoolid":countyidOrSchoolid,
			 	 "xzxs":xzxs,
				 "roleid":roleid,
				 "scaletype":scaletype,
				 "scalesource":scalesource
			 },
			   success: function(msg){
				   debugger;
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);
						var scaleList = callbackarray.scaleList;
						
						$("#scaleName").empty();
						$("#scaleName").append("<option value='-1'>请选择</option>");
						for(var i=0;i<scaleList.length;i++){
							var scale = scaleList[i];
							var scaleid = scale.code;
							var scalename = scale.title;
							$("#scaleName").append("<option value='"+scaleid+"'>"+scalename+"</option>");
						}
				   }else{
						
				   }
			   },
		     error: function()
		     {  	
		    	 //layer.open({content:"根据年级展现班级出错!"});
		   	 }
			}); 

}
function queryQXXXFun(cityId){
	debugger;
	
	var xzxs=document.getElementById("xzxs").value;
	if(xzxs=="-1"){
		//document.getElementById("qx_or_xx").innerHTML="<option value='-1'>请选择</option>";
		$("#qx_or_xx").empty();
		qxvs.multiselect('refresh');
		document.getElementById("roleName").innerHTML="<option value='-1'>请选择</option>";
		document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
		document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
		document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	var isSonSchool="";
	if(xzxs=="1") isSonSchool = "no";
	if(xzxs=="2") isSonSchool ="yes";
	if(xzxs=="1"){
		$("#qxorxx_label").text("选择区县");
		$.ajax({
			   type: "POST",
			   url: "../../scaletoollib/reportlook/getTeacherExamdoQuxianStr.do",
			   data: {
				   "countyidOrSchoolid":null,
				   "cityId":cityId,
				   "isSonSchool":isSonSchool
				   },
			   success: function(msg){
				   debugger;
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);
						var qxArray = callbackarray.districtList;
						var roleArray = callbackarray.roleList;
						$("#qx_or_xx").empty();
						//$("#qx_or_xx").append("<option value='-1'>请选择</option>");
						for(var i=0;i<qxArray.length;i++){
							var quxian = qxArray[i];
							var code = quxian.code;
							var name = quxian.name;
							$("#qx_or_xx").append("<option value='"+code+"'>"+name+"</option>");
						}
						qxvs.multiselect('refresh');
						
						for(var i=0;i<roleArray.length;i++){
							var role = roleArray[i];
							var roleid = role.id;
							var rolename = role.roleName;
							$("#roleName").append("<option value='"+roleid+"'>"+rolename+"</option>");
						}
						
				   }else{
					   //$("#qx_or_xx").append("<option value='-1'>请选择</option>");
					   qxvs.multiselect('refresh');
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:"出错!"});
		   	 }
			}); 
	}else{
		$("#qxorxx_label").text("选择学校");
		$.ajax({
			   type: "POST",
			   url: "../../scaletoollib/reportlook/getTeacherExamdoQuxianStr.do",
			   data: {
				   "countyidOrSchoolid":null,
				   "cityId":cityId,
				   "isSonSchool":isSonSchool
				   },
			   success: function(msg){
				   if(msg!='[]'){
					   var callbackarray=jQuery.parseJSON(msg);
						var schoolArray = callbackarray.schoolList;
						var roleArray = callbackarray.roleList;
						$("#qx_or_xx").empty();
						//$("#qx_or_xx").append("<option value='-1'>请选择</option>");
						for(var i=0;i<schoolArray.length;i++){
							var school = schoolArray[i];
							var id = school.id;
							var name = school.name;
							$("#qx_or_xx").append("<option value='"+id+"'>"+name+"</option>");
						}
						qxvs.multiselect('refresh');
						
						for(var i=0;i<roleArray.length;i++){
							var role = roleArray[i];
							var roleid = role.id;
							var rolename = role.roleName;
							$("#roleName").append("<option value='"+roleid+"'>"+rolename+"</option>");
						}
						
				   }else{
						document.getElementById("qx_or_xx").innerHTML="<option value='-1'>请选择</option>";
				   }
				   qxvs.multiselect('refresh');
			   },
		     error: function()
		     {  	
		    	 layer.open({content:"查询直属学校出错!"});
		   	 }
			}); 
	}
	
}
function changeRole(orgType,orgLevel){
	debugger;
	var roleid = document.getElementById("roleName").value;
	if(roleid=="-1"){
		document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
		document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
		document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	var isSonSchool="no";
	if(orgLevel==3){
		xzxs=document.getElementById("xzxs").value;
		if(xzxs=='2')isSonSchool = "yes";
	}
		$.ajax({
			   type: "POST",
			   url: "../../scaletoollib/reportlook/getScaleFromExamResultTeacher.do",
			   data: {
				   "countyidOrSchoolid":null,
				   "orgType":orgType,
				   "orgLevel":orgLevel,
				   "roleid":roleid,
				   "isSonSchool":isSonSchool
				   },
			   success: function(msg){
				   debugger;
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);
						var scaleList = callbackarray.scaleList;
						
						$("#scaleName").empty();
						$("#scaleName").append("<option value='-1'>请选择</option>");
						for(var i=0;i<scaleList.length;i++){
							var scale = scaleList[i];
							var scaleid = scale.code;
							var scalename = scale.title;
							$("#scaleName").append("<option value='"+scaleid+"'>"+scalename+"</option>");
						}
						
						var scaleTypeMap = callbackarray.scaleTypeMap;
						$("#scaleType").empty();
						$("#scaleType").append("<option value='-1'>请选择</option>");
						for(var typeid in scaleTypeMap){//用javascript的for/in循环遍历对象的属性 
							var typename = scaleTypeMap[typeid]; 
							$("#scaleType").append("<option value='"+typeid+"'>"+typename+"</option>");
						} 
						var scaleSourceMap = callbackarray.scaleSourceMap;
						$("#scaleSource").empty();
						$("#scaleSource").append("<option value='-1'>请选择</option>");
						for(var sourceid in scaleSourceMap){//用javascript的for/in循环遍历对象的属性 
							var sourcename = scaleSourceMap[sourceid]; 
							$("#scaleSource").append("<option value='"+sourceid+"'>"+sourcename+"</option>");
						} 
				   }else{
						document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
						document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
						document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:"根据年级展现班级出错!"});
		   	 }
			}); 
}
function createTeamReport(orgType,orgLevel,download){
	debugger;
	var scale = document.getElementById("scaleName").value;
	if(scale=='-1'){
		layer.open({content:"请选择量表!"});
		return;
	}
	var starttimeTeam = document.getElementById("starttimeTeam").value;
	var endtimeTeam = document.getElementById("endtimeTeam").value;
	if(starttimeTeam==''||starttimeTeam==null){
		layer.open({content:'请输入开始时间!'});
		return;
	}
	if(endtimeTeam==''||endtimeTeam==null){
		layer.open({content:'请输入终止时间!'});
		return;
	}
	if(orgType=='2'){
	}
	else{
		if(orgLevel=='3'){
			if($("#qx_or_xx")){
				var qxOrxxJson = JSON.stringify($("#qx_or_xx").multiselect("getChecked").map(function(){
					   return this.value;    
				}).get());
				$("#qxorxxarray").val(qxOrxxJson);
			}
		}
		if(orgLevel=='4'){
			if($("#qx_or_xx")){
				var qxOrxxJson = JSON.stringify($("#qx_or_xx").multiselect("getChecked").map(function(){
					   return this.value;    
				}).get());
				$("#qxorxxarray").val(qxOrxxJson);
			}
		}
	}
	$("#download").val(download);
	$("#teacherTeamReportForm").submit();
}
</script>