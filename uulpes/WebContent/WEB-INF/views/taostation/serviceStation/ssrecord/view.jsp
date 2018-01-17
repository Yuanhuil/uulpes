<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/taostation/serviceStation/ssrecord/${entity.ssid }" />
<div id="viewdialog" title="查看服务记录">
	<form:form action="${formaction}" method="post" id="viewForm"
		commandName="entity" enctype="multipart/form-data">
		<div class="contentTitle02">
			<a>${entity.title }</a>
		</div>
		<div class="contentTitle03">
			<a><fmt:formatDate pattern="yyyy年MM月dd日 " value="${entity.servicetime}" type="date"/></a>
		</div>
		<div class="content2">
		${entity.comment }</div>
		<c:if test="${fn:length(attachments) >0}">
		<div class="content2">
		<h1>附件：</h1><br>
			<table>
				<c:forEach items="${attachments}" var="a" varStatus="xh">
					<tr id="file_${a.jobattachment.id}">
						<td width="5px">${xh.count }</td>
						<td><a href="${a.jobattachment.savePath}" target="_blank">${a.jobattachment.name}</a></td>
					</tr>
				</c:forEach>
			</table>
		</div>
		</c:if>
	</form:form>
</div>
<script type="text/javascript">
	$(function(){
		var buttonsOps = {};
		<c:choose>
		<c:when test="${op eq '查看'}">
			buttonsOps = {
				"关闭" : function() {
					$("#viewdialog").dialog("close");
				}
			};
		</c:when>
		</c:choose>
		$("#viewdialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 500,
			width : 1000,
			buttons : buttonsOps
		});
	});
	
</script>



