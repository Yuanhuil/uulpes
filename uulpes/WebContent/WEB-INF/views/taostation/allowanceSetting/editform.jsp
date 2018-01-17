<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/taostation/allowanceSetting"/>
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
<div id="editdialog" title="${empty op ? '新增' : op}服务津贴">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity">
		<form:hidden path="id"/>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">服务形式</label> <form:input path="servicetype" cssClass="input_160 validate[required]"></form:input></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">单价/元</label> <form:input path="unitprice" cssClass="input_160 validate[required]"></form:input></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">单位</label> <form:input path="unit" cssClass="input_160 validate[required]"></form:input></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">基数</label> <form:input path="basenum" cssClass="input_160 validate[required]"></form:input></li>
			</ul>
		</div>
	</form:form>
</div>
<script type="text/javascript">
$(function(){
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
			"关闭" : function() {
				$("#editdialog").dialog("close");
			}
		};
		</c:when>
		<c:when test="${op eq '查看'}">
		buttonsOps = {
			"关闭" : function() {
				$("#editdialog").dialog("close");
			}
		};
		</c:when>
		</c:choose>
		$("#editdialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 300,
			width : 300,
			buttons : buttonsOps
		});
});

</script>