<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="threeangleurl" value="../../assessmentcenter/threeangle/searchthreeanglelist.do"></c:set>
<form:form id="threeangleForm" name="importFilterParamForm" method="post" commandName="dataManageFilterParam" action="${threeangleurl }">

			<div class="filterContent">
				<ul>
					<li><label class="name03">姓名</label> <form:input
							path="name" class="input_160" type="text"></form:input></li>
	
				
					<li><label class="name03">性别</label> 
					<form:select class="input_160" path="gender">  
			           <form:option value="-1">请选择</form:option>
			           <form:option value="1">男</form:option>
			           <form:option value="2">女</form:option>
			        </form:select>
					</li>
				<li><label class="name03">身份证号</label> <form:input
							path="sfzjh" class="input_160" type="text"></form:input></li>
				</ul>
				
			</div>
			<div class="buttonContent">
				<input  class="button-small blue" type="submit"  value="搜索">
				<input id="clearform" class="button-small blue" type="reset"
						value="重置">
			</div>
	
</form:form>
<script type="text/javascript">

$(function() {  
	 //$( "#testStartTime" ).datepicker();
	 //$( "#testEndTime" ).datepicker();
	$("#threeangleForm").ajaxForm({
		target : "#threeAngleTableCon"
	});
	 });

</script>