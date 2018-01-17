<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:choose>
	<c:when test="${orgType=='2' }">
		<c:set var="dispatchSurl" value="../../scaletoollib/monitorprocess/schoolAdminsearchTDispatched.do"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="dispatchSurl" value="../../scaletoollib/monitorprocess/eduAdminsearchTDispatched.do"></c:set>
	</c:otherwise>
</c:choose>
<form:form id="dispatcherFilterParamTeacher" name="dispatcherFilterParam" method="post" commandName="dispatcherFilterParam" action="${dispatchSurl }">
	<c:choose>
		<c:when test="${orgType!='2' }">
			<div class="filterContent4">
				<ul>
					<li><label class="name03">分发时间</label> <form:input  class="input_100" 
							path="dispatchTimeStart" type="text" id="dispatchTimeStart2"></form:input></li>
							<li><label class="name03">至</label><form:input  class="input_100" 
							id="dispatchTimeEnd2" path="dispatchTimeEnd"></form:input></li>
				    <li><label class="name03">评测期限</label> <form:input  class="input_100" 
							id="testStratTime2" path="testStartTime"></form:input></li>
					<li><label class="name03">至</label><form:input  class="input_100" 
							id="testEndTime2" path="testEndTime"></form:input></li>
				</ul>
			</div>
			<div class="filterContent4">
				<ul>
					<li><label class="name03">任务名称</label> <form:input
							path="taskKeywords" class="input_100" type="text"></form:input></li>
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
							id="xzqh" path="xzqh" class="select_100">
								<form:option value="-1">请选择</form:option>
								<form:options items="${districtList }" itemLabel="name" itemValue="code"/>
							</form:select></li>
						<li><label class="name03">角色名称</label> <form:select
							path="roleName" class="select_100" type="text">
								<form:option value="-1">请选择</form:option>
								<form:options items="${roleList }" itemLabel="roleName" itemValue="id"/>
							</form:select></li>
					</c:if>
				</ul>
			</div>
			<c:if test="${orgLevel==3 }">
			<div class="filterContent">
				<ul>
						<li><label class="name03">角色名称</label> <form:select
							path="roleName" class="select_160" type="text">
								<form:option value="-1">请选择</form:option>
								<form:options items="${roleList }" itemLabel="roleName" itemValue="id"/>
							</form:select></li>
				</ul>
			</div>
		  </c:if>	
			<div class="buttonContent">
				<input
						class="button-small blue" type="button" value="搜索"
						onclick="searchTeacherDispatch();" />
			</div>
		</c:when>
		<c:otherwise>
			<div class="filterContent4">
				<ul>
					<li><label class="name03">分发时间</label> <form:input
							path="dispatchTimeStart" type="text" id="dispatchTimeStart2" cssClass="input_100"></form:input></li>
					<li><label class="name03">至</label><form:input
							id="dispatchTimeEnd2" path="dispatchTimeEnd" cssClass="input_100"></form:input></li>
					<li><label class="name03">评测期限</label> <form:input
							id="testStratTime2" path="testStartTime" cssClass="input_100"></form:input></li>
					<li><label class="name03">至</label><form:input
							id="testEndTime2" path="testEndTime" cssClass="input_100"></form:input></li>
				</ul>
			</div>
			<div class="filterContent4">
				<ul>
					<li><label class="name03">任务名称</label> <form:input
							path="taskKeywords" class="input_100" type="text"></form:input></li>
					<li><label class="name03">进展状态</label> <form:select
							path="progressStatus" class="select_100" type="text">
								<form:option value="-1">请选择</form:option>
								<form:option value="1">未开始</form:option>
								<form:option value="2">进行中</form:option>
								<form:option value="3">已结束</form:option>
							</form:select></li>
					<li><label class="name03">角色名称</label> <form:select
							path="roleid" class="select_100" type="text">
								<form:option value="-1">请选择</form:option>
								<form:options items="${roleList }" itemLabel="roleName" itemValue="id"/>
							</form:select></li>
				</ul>
			</div>
			<div class="buttonContent">
			    <input  class="button-small blue" type="button" value="搜索"
						onclick="searchTeacherDispatch();" />
			</div>
		</c:otherwise>
	</c:choose>
</form:form>
<script type="text/javascript">
$(function() {  
	 $( "#dispatchTimeStart2" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
	 $( "#dispatchTimeEnd2" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
	 $( "#testStratTime2" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
	 $( "#testEndTime2" ).datepicker(
			 { //绑定开始日期
				 dateFormat : 'yy-mm-dd',
				 changeMonth : true, //显示下拉列表月份
				 changeYear : true, //显示下拉列表年份
				 firstDay : "1" //设置开始为1号
				 });
	 });
function searchTeacherDispatch(){
	$("#dispatcherFilterParamTeacher").ajaxSubmit({
		target : "#tabs_dispatch"
	});
}
</script>