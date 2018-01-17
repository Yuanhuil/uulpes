<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/taostation/workstation/sub"/>
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/create.do" />
	</c:when>
	<c:when test="${op eq '查看'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="" />
	</c:when>
	<c:when test="${op eq '修改'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/${entity.id}/update.do" />
	</c:when>
	<c:otherwise>
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/create.do" />
	</c:otherwise>
</c:choose>
<div id="editdialog" title="${empty op ? '新增' : op}工作站分站">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity">
		<form:hidden path="id"/>
		<form:hidden path="orgid"/>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">分站名称</label>
				<form:input path="name" cssClass="input_160 validate[required]" ></form:input></li>
				<li><label class="name04">区县</label>
				<form:select path="org.countyid" cssClass="select_160 validate[required]" >
					<form:option value="">请选择</form:option>
					<form:options items="${qxlist}" itemLabel="name" itemValue="code"/>
				</form:select></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">地址</label><form:input path="address" cssClass="input_160 validate[required]"></form:input></li>
			<li><label class="name04">联系电话</label><form:input path="telephone" cssClass="input_160 validate[required]"></form:input></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">邮箱</label>
				<form:input path="email" cssClass="input_160 validate[required,custom[email]]"></form:input></li>
				<li><label class="name04">联系人</label>
				<form:input path="contact" cssClass="input_160 validate[required]"></form:input></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">简介</label>
				<form:textarea path="introduce" cssClass="input_160 validate[required]" cssStyle="margin: 0px; height: 126px; width: 390px;"></form:textarea></li>
			</ul>
		</div>
	</form:form>
</div>
<script type="text/javascript">
	var buttonsOps = {};
	<c:choose>
	<c:when test="${empty op || op eq '新增' || op eq '修改'}">
	buttonsOps = {
		"保存" : function() {
			if (!$("#editForm").validationEngine('validate'))
				return false;
			$("#editForm").ajaxSubmit({
				target : "#content2",
				success : function() {
					$("#editdialog").dialog("close");
					layer.open({content:"保存成功!"});
				},
				error : function() {
					layer.open({content:"保存失败"});
				}
			});
			$("#editForm").clearForm();
			return false;
		},
		"取消" : function() {
			$("#editdialog").dialog("close");
		}
	};
	</c:when>
	<c:when test="${op eq '查看'}">
	buttonsOps = {
		"返回" : function() {
			$("#editdialog").dialog("close");
		}
	};
	</c:when>
	</c:choose>
	$("#editdialog").dialog({
		appendTo : "#editformdiv",
		autoOpen : false,
		modal : true,
		height : 400,
		width : 500,
		buttons : buttonsOps
	});
</script>