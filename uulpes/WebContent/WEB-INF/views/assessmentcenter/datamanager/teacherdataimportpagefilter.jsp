<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="importTeacherurl"
	value="../../assessmentcenter/datamanager/searchTImport.do"></c:set>
<form:form id="importFilterParamForm" name="importFilterParamForm"
	method="post" commandName="dataManageFilterParam"
	action="${importTeacherurl }">
	<div class="filterContent">
		<ul>
			<li><label class="name03">角色名称</label> <form:select id="roleid" path="teacherRole" cssClass="input_160" onchange="changeRole();">
						<form:option value="-1" id="se_import">请选择</form:option>
						<form:options name="xdse_import" items="${roleList }" itemLabel="roleName" itemValue="id" />
					</form:select></li>
			<li><label class="name03">性别</label> <form:select path="gender" cssClass="input_160">
					<form:option value="-1">请选择</form:option>
					<form:option value="1">男</form:option>
					<form:option value="2">女</form:option>
				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">量表类别</label> <form:select
					class="input_160" id="scaleType" path="scaleTypeId" onchange="selectDistinctScaleAcorStype()">
					<form:option value="-1" label="请选择"></form:option>
					<form:options items="${scaleTypes }" itemValue="id"
						itemLabel="name"></form:options>
				</form:select></li>
			<li><label class="name03">量表来源</label> <select id="scaleSource"  onchange="selectDistinctScaleBySource()" class="input_160">
					<option value="-1">请选择</option>
					<c:forEach var="scaleSource" items="${scaleSources }">
					<option value="${scaleSource.id }">${scaleSource.name }</option>
				</c:forEach>
				</select></li>
			<li><label class="name03">量表名称</label> <form:select
					id="scaleName" path="scaleId" cssClass="input_160">
					<form:option value="-1">请选择</form:option>
					<form:options items="${scaleList }" itemLabel="shortname"
						itemValue="id" />
				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li>
			    <label class="name03">评测时间</label> <form:input class="input_160" path="testStartTime" type="text" id="testStartTime"></form:input></li>
			  <li> <label class="name03">至</label>
			    <form:input class="input_160" id="testEndTime" path="testEndTime"></form:input></li>
		    <li><input class="button-small blue" type="button" value="查询" onclick="searchStudentDispatch();"/>
		    <input id="clearform" class="button-small blue" type="button" value="重置"	onclick="resetForm();" /></li>
		</ul>
	</div>
	<!-- <div class="buttonContent">
		<ul>
			<li><input class="button-small blue" type="button" value="查询" onclick="searchStudentDispatch();"/></li>
			<li><input class="button-small blue" type="button" value="重置"
				onclick="exportData();" /></li>
		</ul>
	</div>-->
</form:form>
<script type="text/javascript">
function queryClassAccoridngXd(node){
	var xueduan=node.value;
	$.ajax({
		   type: "POST",
		   url: "../../scaletoollib/monitorprocess/queryClassAccoridngXd.do",
		   data: {
			   "xueduan":xueduan
			   },
		   success: function(msg){
			   if(msg){
					var callbackarray=jQuery.parseJSON(msg);
					var htmlstr="";
					for(var i=0;i<callbackarray.length;i++){
						var bhmc = callbackarray[i];
						var bh = bhmc.split("@")[0];
						var bjmc = bhmc.split("@")[1];
						htmlstr= htmlstr+"<option value='"+bh+"'>"+bjmc+"</option>";
					}
					var selectnode=document.getElementById("sclassId");
					selectnode.innerHTML=htmlstr;
			   }else{
				   layer.open({content:'根据学段调用班级出错!'});
			   }
		   },
	     error: function()
	     {  	
	   	    layer.open({content:'根据学段调用班级出错'});
	   	 }
		}); 
}
function changeRole(){
	debugger;
	var roleid=document.getElementById("roleid").value;
	if(roleid=="-1"){
		var scaleType = document.getElementById("scaleType");
		scaleType.selectedIndex = 0;
		var scaleSource = document.getElementById("scaleSource");
		scaleSource.selectedIndex = 0;

	}
	debugger;
	$.ajax({
		   type: "POST",
		   url: "../../ajax/selectDistinctScales.do",
		   data: {
			   "roleid":roleid,
			   "typeid":-1,
			   "sourceid":-1
			   },
		   success: function(msg){
			   if(msg!='[]'){
				   debugger;
					var callbackarray=jQuery.parseJSON(msg);
					var htmlstr="";
					$("#scaleName").empty();
					$("#scaleName").append("<option value='-1'>请选择</option>");
					//var htmlstr="";
					for(var i=0;i<callbackarray.length;i++){
						var scaleinfo = callbackarray[i];
						var scaleid = scaleinfo.id;
						var scalename = scaleinfo.shortname;
						$("#scaleName").append("<option value='"+scaleid+"'>"+scalename+"</option>");
					}
			   }else{
					document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
			   }
		   },
	     error: function()
	     {  	
	    
	   	 }
		}); 
}
function selectDistinctScaleAcorStype(){
	debugger;
	var roleid=document.getElementById("roleid").value;
	var typeid=document.getElementById("scaleType").value;
	if(typeid=="-1"){
		document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	debugger;
	$.ajax({
		   type: "POST",
		   url: "../../ajax/selectDistinctScaleAcorStype.do",
		   data: {
			   "roleid":roleid,
			   "typeid":typeid
			   },
		   success: function(msg){
			   if(msg!='[]'){
				   debugger;
					var callbackarray=jQuery.parseJSON(msg);
					var htmlstr="";
					$("#scaleName").empty();
					$("#scaleName").append("<option value='-1'>请选择</option>");
					//var htmlstr="";
					for(var i=0;i<callbackarray.length;i++){
						var scaleinfo = callbackarray[i];
						var scaleid = scaleinfo.id;
						var scalename = scaleinfo.shortname;
						$("#scaleName").append("<option value='"+scaleid+"'>"+scalename+"</option>");
					}
			   }else{
					document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
			   }
		   },
	     error: function()
	     {  	
	  
	   	 }
		}); 
}
function selectDistinctScaleBySource(){
	debugger;
	var roleid=document.getElementById("roleid").value;
	var sourceid=document.getElementById("scaleSource").value;
	if(sourceid=="-1"){
		//document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	debugger;
	$.ajax({
		   type: "POST",
		   url: "../../ajax/selectDistinctScaleBySource.do",
		   data: {
			   "roleid":roleid,
			   "sourceid":sourceid
			   },
		   success: function(msg){
			   if(msg!='[]'){
				   debugger;
					var callbackarray=jQuery.parseJSON(msg);
					var htmlstr="";
					$("#scaleName").empty();
					$("#scaleName").append("<option value='-1'>请选择</option>");
					//var htmlstr="";
					for(var i=0;i<callbackarray.length;i++){
						var scaleinfo = callbackarray[i];
						var scaleid = scaleinfo.id;
						var scalename = scaleinfo.shortname;
						$("#scaleName").append("<option value='"+scaleid+"'>"+scalename+"</option>");
					}
			   }else{
					document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
			   }
		   },
	     error: function()
	     {  	
	    	// layer.open({content:'不存在该量表来源的量表!'});
	   	 }
		}); 
}

function searchStudentDispatch(){
	$("#importFilterParamForm").ajaxSubmit({
		//target : "#tabs_datamanager",
		target : "#teacherdataimportpagetablecon",
		success : function() {
			/*var xxddsa = document.getElementsByName("xdse_import");
			for ( var i = 0; i < xxddsa.length; i++) {
				xxddsa[i].removeAttribute("selected");
			}
			document.getElementById("se_import").setAttribute(
					"selected", "selected");*/
		}
	});
}
function resetForm(){
	debugger;
	$("#importFilterParamForm")[0].reset();
	//document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
}
$(function() {  
	 $( "#testStartTime" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
	 $( "#testEndTime" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
	 $("#clearform").click(function(){
			$("#importFilterParamForm").clearForm();
	 });
});
</script>