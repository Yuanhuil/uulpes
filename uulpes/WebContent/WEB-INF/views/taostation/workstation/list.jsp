<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="baseinfoform" modelAttribute="entity"
	action="${ctx}/taostation/workstation/${entity.id}/update.do" method="post">
	<form:hidden path="orgid" />
	<div class="rightDiv1" style="text-align:center;">
		<ul>
			<li><label class="name03">名称</label> <form:input path="name"
					cssClass="input_300 validate[required]"></form:input></li>
			<li><label class="name03">区县</label>
			<form:select path="org.countyid" cssClass="input_300 validate[required]">
				<form:option value="">请选择</form:option>
				<form:options items="${qxlist }" itemLabel="name" itemValue="code"/>
			</form:select></li>
			<li><label class="name03">地址</label>
			<form:input path="address" cssClass="input_300 validate[required]"></form:input></li>
			<li><label class="name03">联系人</label>
			<form:input path="contact" cssClass="input_300 validate[required]"></form:input></li>
			<li><label class="name03">联系电话</label>
			<form:input path="telephone" cssClass="input_300 validate[required]"></form:input></li>
			<li><label class="name03">邮箱</label> <form:input path="email"
					cssClass="input_300 validate[required,custom[email]]"></form:input></li>
			<li><label class="name03">简介</label>
			<form:textarea path="introduce" cssClass="validate[required]" style="width:300px;height:200px;"></form:textarea></li>
			<li><label class="name03"></label><input type="submit"
				class="button-big blue" value="修改"></li>
		</ul>
	</div>
</form:form>
<script>
	$(function(){
		$("#baseinfoform").validationEngine();
		$("#baseinfoform").ajaxForm({
			target : "#content2"
		});
	});
</script>