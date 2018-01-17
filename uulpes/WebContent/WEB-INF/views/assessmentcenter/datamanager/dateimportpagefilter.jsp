<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="importSchoolurl" value="../../assessmentcenter/datamanager/searchSImport.do"></c:set>
<form:form id="importFilterParamForm" name="importFilterParamForm" method="post" commandName="dataManageFilterParam" action="${importSchoolurl }">
	<c:choose>
		<c:when test="${orgType=='2' }">
			<div class="filterContent">
				<ul>
					<li><label class="name03">年级</label> <form:select class="input_160" path="nj" onchange="getClassAccordingGrade();" id="nj">
						<form:option value="-1" label="请选择" id="se_import" ></form:option>
						<form:options name="xdse_import" items="${gradeList }" itemValue="gradeid" itemLabel="njmc"></form:options>
						</form:select></li>
					<li><label class="name03">班级</label> <form:select class="input_160" 
							id="bj" path="bj" onchange="getScaleAccordingClassid();">
							<form:option value="-1">请选择</form:option>
							</form:select></li>
					<li><label class="name03">性别</label> 
					<form:select class="input_160" path="gender">  
			           <form:option value="-1">请选择</form:option>
			           <form:option value="1">男</form:option>
			           <form:option value="2">女</form:option>
			        </form:select>
					</li>
				</ul>
			</div>
			<div class="filterContent">
				<ul>
					<li><label class="name03">量表类别</label> <form:select
				class="input_160" id="scaleType" path="scaleTypeId" onchange="selectDistinctScaleAcorStype()">
					<form:option value="-1" label="请选择"></form:option>
					<form:options items="${scaleTypes }" itemValue="id" itemLabel="name"></form:options>
			</form:select></li>
			<li><label class="name03">量表来源</label> <select id="scaleSource"  onchange="selectDistinctScaleBySource()" class="input_160">
					<option value="-1">请选择</option>
					<c:forEach var="scaleSource" items="${scaleSources }">
					<option value="${scaleSource.id }">${scaleSource.name }</option>
				</c:forEach>
				</select></li>
				</ul>
				<ul>
					<li><label class="name03">量表名称</label> <form:select class="input_160" id="scaleName" path="scaleId">
						<form:option value="-1">请选择</form:option>
						<form:options items="${scaleList }" itemLabel="shortname" itemValue="id"/>
					</form:select></li>
				</ul>
			</div>
			<div class="filterContent">
				<ul>
					<li><label class="name03">姓名</label> <form:input
							path="name" class="input_160" type="text"></form:input></li>
	
				
					<li><label class="name03">身份证号</label> <form:input
							path="identify" class="input_160" type="text"></form:input></li>
					<li><input  class="button-small blue" type="button" onclick="searchStudentDispatch();"
						value="搜索">
					<input id="clearform" class="button-small blue" type="button"
						value="重置" onclick="resetForm();"></li>
				</ul>
				
			</div>

			<!-- <div class="filterContent">
				<ul>
					<li><label class="name03">评测时间</label> <form:input
							path="testStartTime" type="text" id="testStartTime" class="input_160"></form:input>至<form:input
							id="testEndTime" path="testEndTime" class="input_160"></form:input></li>
				</ul>
			</div>-->
			<!-- <div class="buttonContent">
				<ul>
					<li><input  class="button-small blue" type="button" onclick="searchStudentDispatch();"
						value="搜索"></li>
					<li><input id="clearform" class="button-small blue" type="reset"
						value="重置"></li>
				</ul>
	        </div>-->
		</c:when>
		<c:otherwise>
			<div class="filterContent">
			</div>
		</c:otherwise>
	</c:choose>
	
</form:form>
<script type="text/javascript">
function getClassAccordingGrade(){
	debugger;
	var gradeid=document.getElementById("nj").value;
	if(gradeid=="-1"){
		document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	$.ajax({
		   type: "POST",
		   url: "../../ajax/getClassesAndScaleTypeByGradeId.do",
		   data: {
			   "gradeid":gradeid
			   },
		   success: function(msg){
			   debugger;
			   if(msg!='[]'){
					var callbackarray=jQuery.parseJSON(msg);
					var htmlstr="";
					var classarray=callbackarray.classList;
					$("#bj").empty();
					$("#bj").append("<option value='-1'>请选择</option>");
					//var htmlstr="";
					for(var i=0;i<classarray.length;i++){
						var classSchool = classarray[i];
						var classId = classSchool.id;
						var bjmc = classSchool.bjmc;
						$("#bj").append("<option value='"+classId+"'>"+bjmc+"</option>");
						//htmlstr= htmlstr+"<option value='"+classId+"'>"+bjmc+"</option>";
					}
					/* var scaletypeArray=callbackarray.scaletypeList;
					//var htmlstr="";
					$("#scaleType").empty();
					$("#scaleType").append("<option value='-1'>请选择</option>");
					for(var i=0;i<scaletypeArray.length;i++){
						var scaletype = scaletypeArray[i];
						var typeId = scaletype.id;
						var title = scaletype.name;
						$("#scaleType").append("<option value='"+typeId+"'>"+title+"</option>");
					} */
					//var selectnode=document.getElementById("scaleType");
					//selectnode.innerHTML=htmlstr;
					var scalearray=callbackarray.scaleList;
					$("#scaleName").empty();
					$("#scaleName").append("<option value='-1'>请选择</option>");
					for(var i=0;i<scalearray.length;i++){
						var scaleinfo = scalearray[i];
						var scaleid = scaleinfo.id;
						var scalename = scaleinfo.shortname;
						$("#scaleName").append("<option value='"+scaleid+"'>"+scalename+"</option>");
					}
					
			   }else{
					/* document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
					document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>"; */
			   }
		   },
	     error: function()
	     {  	
	    	 layer.open({content:'根据年级展现班级出错!'});
	   	 }
		}); 
}
function getScaleAccordingClassid(){
	debugger;
	var classid = document.getElementById("bj").value;
	if(classid=="-1"){
		document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	var gradeid=document.getElementById("nj").value;
	$.ajax({
		   type: "POST",
		   url: "../../ajax/getScaleTypeByClassId.do",
		   data: {
			   "bjid":classid
			   },
		   success: function(msg){
			   debugger;
			   if(msg!='[]'){
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
					/* document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
					document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>"; */
			   }
		   },
	     error: function()
	     {  	
	    	 layer.open({content:'根据年级展现班级出错!'});
	   	 }
		}); 
}
function getClassAccordingGrade1(){
	var gradeid=document.getElementById("nj").value;
	if(gradeid=="-1"){
		document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	$.ajax({
		   type: "POST",
		   url: "../../ajax/getClassesAndScaleTypeByGradeId.do",
		   data: {
			   "gradeid":gradeid
			   },
		   success: function(msg){
			   if(msg!='[]'){
					var callbackarray=jQuery.parseJSON(msg);
					var htmlstr="";
					var classarray=callbackarray.classList;
					$("#bj").empty();
					$("#bj").append("<option value='-1'>请选择</option>");
					//var htmlstr="";
					for(var i=0;i<classarray.length;i++){
						var classSchool = classarray[i];
						var classId = classSchool.id;
						var bjmc = classSchool.bjmc;
						var bjid = ${dataManageFilterParam.bj};
						if(classId==bjid){
							$("#bj").append("<option value='"+classId+"' selected='true'>"+bjmc+"</option>");
						}else{
							$("#bj").append("<option value='"+classId+"'>"+bjmc+"</option>");
						}
						
						//htmlstr= htmlstr+"<option value='"+classId+"'>"+bjmc+"</option>";
					}
					/* var scaletypeArray=callbackarray.scaletypeList;
					//var htmlstr="";
					$("#scaleType").empty();
					$("#scaleType").append("<option value='-1'>请选择</option>");
					for(var i=0;i<scaletypeArray.length;i++){
						var scaletype = scaletypeArray[i];
						var typeId = scaletype.id;
						var title = scaletype.name;
						$("#scaleType").append("<option value='"+typeId+"'>"+title+"</option>");
					} */
					//var selectnode=document.getElementById("scaleType");
					//selectnode.innerHTML=htmlstr;
					
					
			   }else{
					/* document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
					document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>"; */
			   }
		   },
	     error: function()
	     {  	
	    	 layer.open({content:'根据年级展现班级出错!'});
	   	 }
		}); 
}


function selectDistinctScaleAcorStype(){
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
	    	 layer.open({content:'不存在该量表类型的量表!'});
	   	 }
		}); 
}
function selectDistinctScaleBySource(){
	var gradeid=document.getElementById("nj").value;
	var bjid=document.getElementById("bj").value;
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
			   "gradeid":gradeid,
			   "bjid":bjid,
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
	    	 layer.open({content:'不存在该量表来源的量表!'});
	   	 }
		}); 
}
function changeScaleType(){
	var typeid=document.getElementById("scaleType").value;
	if(typeid=="-1"){
		document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	$.ajax({
		   type: "POST",
		   url: "../../ajax/getScaleByType.do",
		   data: {
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
						var scaleid = scaleinfo.code;
						var scalename = scaleinfo.title;
						$("#scaleName").append("<option value='"+scalename+"'>"+scalename+"</option>");
					}
					
			   }else{
					document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
			   }
		   },
	     error: function()
	     {  	
	    	 layer.open({content:'不存在该量表类型的量表!'});
	   	 }
		}); 
}
function searchStudentDispatch(){
	$("#importFilterParamForm").ajaxSubmit({
		//target : "#tabs_datamanager",
		target : "#dateimportpagetableCon",
		success:function() {
			//getClassAccordingGrade1();
		}
	});
}
function resetForm(){
	debugger;
	$("#importFilterParamForm")[0].reset();
	document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
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
	 });

</script>