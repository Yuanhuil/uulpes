<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="importTeacherurl" value="../../assessmentcenter/datamanager/searchTImport.do"></c:set>
<form:form id="importFilterParamForm" name="importFilterParamForm" method="post" commandName="dataManageFilterParam" action="${importTeacherurl }">
		<c:choose>
		<c:when test="${orgType=='2' }">
			<div class="filterContent">
				<ul>
					<li><label class="name03">教师角色</label> <form:select path="teacherRole"  id="teacherRole">
						<form:option value="-1" label="请选择"></form:option>
						<form:options items="${roleList }" itemValue="id" itemLabel="roleName"></form:options>
						</form:select></li>
					<li><label class="name03">性别</label> 
					<form:select path="gender">  
			           <option value="-1">请选择</option>
			           <option value="1">男</option>
			           <option value="2">女</option>
			        </form:select>
					</li>
				</ul>
			</div>
			<div class="filterContent">
				<ul>
					<li><label class="name03">量表类别</label> <form:select
				class="input_160" id="scaleType" path="scaleTypeId">
					<form:option value="-1" label="请选择"></form:option>
					<form:options items="${scaleTypes }" itemValue="name" itemLabel="name"></form:options>
			</form:select></li>
				</ul>
				<ul>
					<li><label class="name03">量表名称</label> <form:select id="scaleName" path="scaleId">
						<form:option value="-1">请选择</form:option>
						<form:options items="${scaleList }" itemLabel="shortname" itemValue="id"/>
					</form:select></li>
				</ul>
			</div>
			<div class="filterContent">
				<ul>
					<li><label class="name03">姓名</label> <form:input
							path="name" class="input_160" type="text"></form:input></li>
				</ul>
				<ul>
					<li><label class="name03">身份证号</label> <form:input
							path="identify" class="input_160" type="text"></form:input></li>
				</ul>
			</div>
			<div class="buttonContent">
		<ul>
			<li><input  class="button-small blue" type="button" onclick="searchTeacherDispatch();"
				value="搜索"></li>
			<li><input id="clearform" class="button-small blue" type="reset"
				value="重置"></li>
		</ul>
	</div>
		</c:when>
		<c:otherwise>
			<div class="filterContent">
			</div>
		</c:otherwise>
	</c:choose>
</form:form>
<script type="text/javascript">
function changeTeacherRole(node){
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

function searchTeacherDispatch(){
	$("#importFilterParamForm").ajaxSubmit({
		target : "#tabs_dataimport"
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
	 });
</script>