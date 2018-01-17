<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform"
	action="/pes/archivemanage/archiveTeacher/list.do" method="post"
	modelAttribute="entity">
	<div class="filterContent">
		<ul>
			<li><label class="name03">角色名称</label> <form:select
					path="roleId" cssClass="input_160">
					<form:option value="">请选择</form:option>
					<form:options items="${rolelist }" itemValue="id"
						itemLabel="roleName" />
				</form:select></li>
			<li><label class="name03">身份证号</label>
			<form:input path="sfzjh" cssClass="input_160" /></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">姓名</label>
				<form:input path="xm" cssClass="input_160"></form:input></li>
			<li><label class="name03">性别</label>
			<form:select path="xbm" cssClass="input_160">
					<form:option value="">请选择</form:option>
					<form:options items="${sexlist}" itemValue="id" itemLabel="name" />
				</form:select></li>

		</ul>
	</div>
	<div class="buttonContent">
			<input id="searchsuborgs" class="button-mid blue"
				type="submit" value="查询">
			<input id="clearform" class="button-mid blue" type="button"
				value="重置">
			<!-- <input type="button" id="setting" value="生成档案设置"
				class="button-mid blue" />	 -->
	</div>
</form:form>
<script type="text/javascript">
	$("#queryform").ajaxForm({
		target : "#list"
	});
	$("#clear").click(function() {
		$('#queryform').clearForm();
	});
	$("#setting").click(function() {
		var str = $(this).val();

		if (str == "生成档案设置") {
			$(this).attr("value", "关闭");
			$("#editDiv").attr("style", "display:block")
			$("#list").attr("style", "display:none")
		} else {
			$(this).attr("value", "生成档案设置");
			$("#list").attr("style", "display:block")
			$("#editDiv").attr("style", "display:none")
		}

	});
</script>