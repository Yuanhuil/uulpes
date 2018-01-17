<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:choose>
		<c:when test="${orgType=='2' }">
			<c:set var="exportTeacherurl"
				value="../../assessmentcenter/datamanager/exportteacheranswerinsch.do"></c:set>
		</c:when>
		<c:otherwise>
			<c:if test="${orgLevel==3 }">
				<c:set var="exportTeacherurl" value="../../assessmentcenter/datamanager/exportteacheranswerinedu.do"></c:set>
			</c:if>
			<c:if test="${orgLevel==4 }">
				<c:set var="exportTeacherurl" value="../../assessmentcenter/datamanager/exportteacheranswerinedu.do"></c:set>
			</c:if>
		</c:otherwise>
</c:choose>
<form:form id="exportFilterParamForm" name="exportFilterParamForm" method="post" commandName="dataManageFilterParam" action="${exportTeacherurl }">
	<c:choose>
		<c:when test="${orgType=='2' }">
			<div class="filterContent">
				<ul>
					<li><label class="name03">角色名称</label> <form:select id="roleName" path="roleName" cssClass="select_160" onchange="changeRole('2','6');">
						<form:options items="${roleList }" itemLabel="roleName" itemValue="id" />
					</form:select></li>
					<li><label class="name03">量表来源</label> <form:select
						class="select_160" id="scaleSource" path="scaleSourceId" onchange="changeScaleTypeOrSource('${orgType}','${orgLevel }')">
						<form:option value="-1" label="请选择"></form:option>
						</form:select></li>
					<li><label class="name03">量表类别</label> <form:select
					    class="select_160" id="scaleType" path="scaleTypeId" onchange="changeScaleTypeOrSource('${orgType}','${orgLevel }')">
						<form:option value="-1" label="请选择"></form:option>
						</form:select></li>
				</ul>
			</div>
			<div class="filterContent">
				<ul>
					<li><label class="name03">量表名称</label><form:select id="scaleName" path="scaleId" class="select_160">
								<form:option value="-1">请选择</form:option>
					</form:select></li>
				   <li><label class="name03">评测时间</label> <form:input class="input_160"
						path="testStartTime" type="text" id="testStartTime"></form:input></li>
					<li><label class="name03">至</label><form:input class="input_160"
						id="testEndTime" path="testEndTime"></form:input></li>
				</ul>
			</div>
		</c:when>
		<c:otherwise>
			<c:if test="${orgLevel==3}">
				<div class="filterContent">
					<ul>
					<li><label class="name03">机构层级</label> <select id="xzxs" name="xzxs"
								class="input_160" onchange="queryQXXXFun('${cityId }');">
									<option value="-1">请选择</option>
									<option value="1">区县</option>
									<option value="2">市直属学校</option>
					</select></li>
					<li><label class="name03">选择区县</label> <select id="qx_or_xx"  name="qx_or_xx"	class="select_160" onclick="changeQxOrSchool('1','3')" >
					</select></li>
				    <li><label class="name03">角色名称</label> <form:select id="roleName" path="roleName" cssClass="select_160"  onchange="changeRole('1','3');">
							<form:option value="-1">请选择</form:option>
							<form:options items="${roleList }" itemLabel="roleName" itemValue="id" />
							</form:select></li>
			</ul>
			</div>
		</c:if>
		<c:if test="${orgLevel==4}">
			<div class="filterContent">
		    <ul>
		      <li><label class="name03">选择学校</label> <select id="qx_or_xx"	class="select_160" >
		          <c:forEach var="item" items="${schoolList}" >
	            	  <option value="${item.name}">${item.name}</option>
	       		  </c:forEach>
				</select>
			 </li>
			 <li><label class="name03">角色名称</label> <form:select id="roleName" path="roleName" cssClass="select_160"  onchange="changeRole('1','4');">
							<form:option value="-1">请选择</form:option>
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
			<li><label class="name03">量表名称</label><form:select id="scaleName" path="scaleId" class="select_160">
						<form:option value="-1">请选择</form:option>
			</form:select></li>
		</ul>
	</div>
		<div class="filterContent">
			<ul>
			   <li><label class="name03">评测时间</label> <form:input class="input_160"
					path="testStartTime" type="text" id="testStartTime"></form:input></li>
				<li><label class="name03">至</label><form:input class="input_160"
					id="testEndTime" path="testEndTime"></form:input></li>
			</ul>
		</div>
	</c:otherwise>
</c:choose>
	<div class="filterContent4">
		<ul>
		    <li><input class="button-mid blue" id="selectBkBtn" value="导出背景信息" type="button" onclick="selectBk()"/></li>
			<!-- <li><label class="name03">选择背景</label>
			 <input id="backgroundChk" type="checkbox" onclick="selectBk()" /></li>-->
		</ul>
	</div>
	<div id="backgroud"><%@include file="attrform.jsp"%></div>
	<div class="buttonContent">

		    <input class="button-small blue" type="button" value="导出数据" onclick="exportData('${orgType}','${orgLevel}');"/>
	
			<input class="button-small blue" id="clearform"  type="button" value="重置" />
	</div>
</form:form>
<script type="text/javascript">
debugger;
  changeRole('${orgType}','${orgLevel }');

function selectBk() {
	var bgform = $('<form></form>');  
	bgform.attr('id',"bgform");
    bgform.attr('action',"../../assessmentcenter/datamanager/showSelectTeacherAttrForm.do");  
    bgform.attr('method', 'post');
    
    bgform.ajaxSubmit({
		target : "#backgroud",
		success : function(msg) {
			$("#selectBkBtn").css("display","none");
		},
		error : function() {  
	         layer.open({content:'加载页面背景字段时出错!'});  
	      } 
	});
}
function changeQxOrSchool(orgType,orgLevel){
	debugger;
	var countyidOrSchoolid = null;
	if(orgType!='2')countyidOrSchoolid = document.getElementById("qx_or_xx").value;
	if(countyidOrSchoolid=="-1"){
		document.getElementById("roleName").innerHTML="<option value='-1'>请选择</option>";
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
			   url: "../../scaletoollib/reportlook/getRoleInfoFromExamResultTeacher.do",
			   data: {
				   "countyidOrSchoolid":countyidOrSchoolid,
				   "isSonSchool":isSonSchool
				   },
			   success: function(msg){
				   debugger;
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);	
						$("#roleName").empty();
						$("#roleName").append("<option value='-1'>请选择</option>");
						for(var i=0;i<callbackarray.length;i++){
							var role = callbackarray[i];
							var roleid = role.id;
							var rolename = role.roleName;
							$("#roleName").append("<option value='"+roleid+"'>"+rolename+"</option>");
						}
				   }else{
					    document.getElementById("roleName").innerHTML="<option value='-1'>请选择</option>";
						document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
						document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
						document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:'出错!'});
		   	 }
			}); 
}
function changeRole(orgType,orgLevel){
	debugger;
	var countyidOrSchoolid = null;
	if(orgType!='2')countyidOrSchoolid = document.getElementById("qx_or_xx").value;
	var roleid = document.getElementById("roleName").value;
	//if(roleid=="-1"){
	//	document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
	//	document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
	//	document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
	//	return;
	//}
	var isSonSchool="no";
	if(orgLevel==3){
		xzxs=document.getElementById("xzxs").value;
		if(xzxs=='2')isSonSchool = "yes";
	}
		$.ajax({
			   type: "POST",
			   url: "../../scaletoollib/reportlook/getScaleFromExamResultTeacher.do",
			   data: {
				   "countyidOrSchoolid":countyidOrSchoolid,
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
		    	 layer.open({content:'出错!'});
		   	 }
			}); 
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
		//qxvs.multiselect('refresh');
		document.getElementById("roleName").innerHTML="<option value='-1'>请选择</option>";
		document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
		document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
		document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	var isSonSchool="";
	if(xzxs=="1") isSonSchool = "no";
	if(xzxs=="2") isSonSchool ="yes";
		$.ajax({
			   type: "POST",
			   url: "../../scaletoollib/reportlook/getTeacherExamdoQuxianStr.do",
			   data: {
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
						$("#qx_or_xx").append("<option value='-1'>请选择</option>");
						for(var i=0;i<qxArray.length;i++){
							var quxian = qxArray[i];
							var code = quxian.code;
							var name = quxian.name;
							$("#qx_or_xx").append("<option value='"+code+"'>"+name+"</option>");
						}
						//qxvs.multiselect('refresh');
						
						for(var i=0;i<roleArray.length;i++){
							var role = roleArray[i];
							var roleid = role.id;
							var rolename = role.roleName;
							$("#roleName").append("<option value='"+roleid+"'>"+rolename+"</option>");
						}
						
				   }else{
					   $("#qx_or_xx").append("<option value='-1'>请选择</option>");
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:'出错!'});
		   	 }
			}); 
	
}
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
function exportData(orgType,orgLevel){
	debugger;
	var roleid = document.getElementById("roleName").value;
	var scaleid = document.getElementById("scaleName").value;
	var testStartTime = document.getElementById("testStartTime").value;
	var testEndTime = document.getElementById("testEndTime").value;
	if(roleid=='-1'||roleid==null){
		layer.open({content:'请选择教师角色!'});
		return;
	}
	if(scaleid=='-1'||scaleid==null){
		layer.open({content:'请选量表!'});
		return;
	}
	if(testStartTime==''||testStartTime==null){
		layer.open({content:'请输入开始时间!'});
		return;
	}
	if(testEndTime==''||testEndTime==null){
		layer.open({content:'请输入终止时间!'});
		return;
	}
	$("#exportFilterParamForm").submit();
}
function searchStudentDispatch(){
	$("#exportFilterParamForm").ajaxSubmit({
		target : "#tabs_datamanager"
	});
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
			$("#exportFilterParamForm").clearForm();
	 });
});
</script>