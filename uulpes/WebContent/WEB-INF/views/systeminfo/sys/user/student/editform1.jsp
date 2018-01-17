<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${ctx}/systeminfo/sys/organization/sub/${parentorgid}/create.do" />
	</c:when>
	<c:when test="${op eq '查看'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="" />
	</c:when>
	<c:when test="${op eq '修改'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${ctx}/systeminfo/sys/organization/sub/${parentorgid}/${entity.id }update.do" />
	</c:when>
	<c:otherwise>
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${ctx}/systeminfo/sys/organization/sub/${parentorgid}/create.do" />
	</c:otherwise>
</c:choose>
<div id="suborgdialog" title="${empty op ? '新增' : op}下属组织机构">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity">
		<div class="filterContent">
			<ul>
				<li><label class="name03">机构层级</label> <form:select
						path="orgLevel" cssClass="dropBox01" items="${orglevelList}"  itemValue="id"  itemLabel="orglevelname"></form:select></li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><label class="name03">上级机构</label> <form:select
						path="provinceid" cssClass="dropBox01"></form:select></li>
				<li><form:select path="cityid" cssClass="dropBox01"></form:select>
					<form:select path="countyid" cssClass="dropBox01"></form:select></li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><label class="name03">机构名称</label>
				<form:input path="name" cssClass="input_160" ></form:input></li>
				<li><label class="name03">机构代码</label>
				<form:input path="code" cssClass="input_160" ></form:input></li>
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
					$("#suborgdialog").dialog("close");
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
			$("#suborgdialog").dialog("close");
		}
	};
	</c:when>
	<c:when test="${op eq '查看'}">
	buttonsOps = {
		"返回" : function() {
			$("#suborgdialog").dialog("close");
		}
	};
	</c:when>
	</c:choose>
	$("#suborgdialog").dialog({
		appendTo : "#editformdiv",
		autoOpen : false,
		modal : true,
		height : 250,
		width : 800,
		buttons : buttonsOps
	});
</script>