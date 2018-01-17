<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:choose>
	<c:when test="${orgType=='2' }">
		<c:set var="dispatchSurl" value="../../scaletoollib/monitorprocess/schoolAdminsearchSDispatched.do"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="dispatchSurl" value="../../scaletoollib/monitorprocess/eduAdminsearchSDispatched.do"></c:set>
	</c:otherwise>
</c:choose>
<form:form id="dispatcherFilterParamStudent" name="dispatcherFilterParam" method="post" commandName="dispatcherFilterParam" action="${dispatchSurl }">
	<c:choose>
		<c:when test="${orgType!='2' }">
			<div class="filterContent4">
				<ul>
					<li><label class="name03">分发时间</label> <form:input class="input_100"
							path="dispatchTimeStart" type="text" id="dispatchTimeStart"></form:input></li>
					<li><label class="name03">至</label><form:input class="input_100"
							id="dispatchTimeEnd" path="dispatchTimeEnd"></form:input></li>
					<li><label class="name03">评测期限</label> <form:input class="input_100"
							id="testStratTime" path="testStartTime"></form:input></li>
					<li><label class="name03">至</label><form:input class="input_100"
							id="testEndTime" path="testEndTime"></form:input></li>

				</ul>
			</div>
			<div class="filterContent4">
				<ul>
					
					<li><label class="name03">年级名称</label> <form:select
							path="sgradeId" class="select_100">
						<form:option value="-1">请选择</form:option>
						<form:options items="${gradeAllList }" itemLabel="title" itemValue="nj"/>	
					</form:select></li>
					<li><label class="name03">进展状态</label> <form:select
							path="progressStatus" class="select_100" type="text">
								<form:option value="-1">请选择</form:option>
								<form:option value="1">未开始</form:option>
								<form:option value="2">进行中</form:option>
								<form:option value="3">已结束</form:option>
							</form:select></li>
					<c:if test="${orgLevel==3 }">
						<li><label class="name03">行政区划</label> <select
							id="xzxs" class="select_100" onchange="queryQXXXFun('${cityId }');">
								<option value="-1">请选择</option>
								<option value="1">区县</option>
								<option value="2">市直属学校</option>
							</select></li>
							<li><label class="name03">区县/学校</label> <form:select
							path="xzqh" id="xzqh" class="select_100" type="text">
								<form:option value="-1">请选择</form:option>
							</form:select></li>
					</c:if>
					<c:if test="${orgLevel==4 }">
						<li><label class="name03">行政区划</label> <form:select
							id="xzqh" path="xzqh" class="input_100">
								<form:option value="-1">请选择</form:option>
								<form:options items="${districtList }" itemLabel="name" itemValue="code"/>
							</form:select></li>
						<li><label class="name03">任务名称</label> <form:input
							path="taskKeywords" class="input_100" type="text"></form:input></li>
					</c:if>
				</ul>
			</div>
			<c:if test="${orgLevel==3 }">
			<div class="filterContent">
				<ul>
					<li><label class="name03">任务名称</label> <form:input
							path="taskKeywords" class="input_160" type="text"></form:input></li>
				</ul>
			</div>
			</c:if>
			<div class="buttonContent">
				<input	class="button-small blue" type="button" value="搜索"
						onclick="eduAdminsearchSDispatched();" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="filterContent4">
				<ul>
					<li><label class="name03">分发时间</label> <form:input
							path="dispatchTimeStart" type="text" id="dispatchTimeStart" cssClass="input_100"></form:input></li>
					<li><label class="name03">至</label><form:input
							id="dispatchTimeEnd" path="dispatchTimeEnd" cssClass="input_100"></form:input></li>	
					<li><label class="name03">评测期限</label> <form:input
							id="testStratTime" path="testStartTime" cssClass="input_100"></form:input></li>
					<li><label class="name03">至</label><form:input
						id="testEndTime" path="testEndTime" cssClass="input_100"></form:input></li>
				</ul>
			</div>
			<div class="filterContent4">
				<ul>
					<li><label class="name03">任务名称</label> <form:input
							path="taskKeywords" class="select_100" type="text"></form:input></li>
					<li><label class="name03">进展状态</label> <form:select
							path="progressStatus" class="select_100" type="text">
								<form:option value="-1">请选择</form:option>
								<form:option value="1">未开始</form:option>
								<form:option value="2">进行中</form:option>
								<form:option value="3">已结束</form:option>
							</form:select></li>
					<li><label class="name03">年级名称</label> 
						<form:select path="sgradeId" id="sgradeId" class="select_100" onchange="getClassAccordingGrade();">
							<form:option value="-1" label="请选择"></form:option>
							<form:options items="${gradeList }" itemValue="gradeid" itemLabel="njmc"></form:options>
						</form:select>
					</li>
					<li><label class="name03">班级名称</label> 
					<form:select id="sclassId" path="sclassId" class="select_100">
							<form:option value="-1">请选择</form:option>
						</form:select></li>
				</ul>
			</div>
			<div class="buttonContent">
				<input	class="button-small blue" type="button" value="搜索"
						onclick="schoolAdminSearchDispatch();" />
			</div>
		</c:otherwise>
	</c:choose>
</form:form>
<script type="text/javascript">
function getClassAccordingGrade(){
	var gradeid=document.getElementById("sgradeId").value;
	if(gradeid=="-1"){
		document.getElementById("sclassId").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	$.ajax({
		   type: "POST",
		   url: "../../ajax/getClassesByGradeId2.do",
		   data: {
			   "gradeid":gradeid
			   },
		   success: function(msg){
			   if(msg!='[]'){
					var callbackarray=jQuery.parseJSON(msg);
					$("#sclassId").empty();
					$("#sclassId").append("<option value='-1'>请选择</option>");
					for(var i=0;i<callbackarray.length;i++){
						var classSchool = callbackarray[i];
						var classId = classSchool.id;
						var bjmc = classSchool.bjmc;
						$("#sclassId").append("<option value='"+classId+"'>"+bjmc+"</option>");
					}
			   }else{
					document.getElementById("sclassId").innerHTML="<option value='-1'>请选择</option>";
			   }
		   },
	     error: function()
	     {  	
	    	 layer.open({content:"根据年级展现班级出错!"});
	   	 }
		}); 
}
function schoolAdminSearchDispatch(){
	$("#dispatcherFilterParamStudent").ajaxSubmit({
		target : "#tabs_dispatch"
	});
}
$(function() {  
	 $( "#dispatchTimeStart" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
	 $( "#dispatchTimeEnd" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
	 $( "#testStratTime" ).datepicker(
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

function queryQXXXFun(cityId){
	var xzxs=document.getElementById("xzxs").value;
	if(xzxs=="-1"){
		document.getElementById("xzqh").innerHTML="<option value='-1'>请选择</option>";
		return;
	}
	if(xzxs=="1"){
		$.ajax({
			   type: "POST",
			   url: "../../ajax/getQuxianStr.do",
			   data: {
				   "cityId":cityId
				   },
			   success: function(msg){
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);
						$("#xzqh").empty();
						$("#xzqh").append("<option value='-1'>请选择</option>");
						for(var i=0;i<callbackarray.length;i++){
							var quxian = callbackarray[i];
							var code = quxian.code;
							var name = quxian.name;
							$("#xzqh").append("<option value='"+code+"'>"+name+"</option>");
						}
				   }else{
						document.getElementById("xzqh").innerHTML="<option value='-1'>请选择</option>";
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:"根据年级展现班级出错!"});
		   	 }
			}); 
	}else{
		$.ajax({
			   type: "POST",
			   url: "../../ajax/getSonSchoolsStr.do",
			   data: {
				   "cityId":cityId
				   },
			   success: function(msg){
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);
						$("#xzqh").empty();
						$("#xzqh").append("<option value='-1'>请选择</option>");
						for(var i=0;i<callbackarray.length;i++){
							var classSchool = callbackarray[i];
							var classId = classSchool.id;
							var name = classSchool.name;
							$("#xzqh").append("<option value='"+classId+"'>"+name+"</option>");
						}
				   }else{
						document.getElementById("xzqh").innerHTML="<option value='-1'>请选择</option>";
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:"根据年级展现班级出错!"});
		   	 }
			}); 
	}
}
</script>