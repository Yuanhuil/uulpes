<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/workschedule/notice" />
<c:choose>
	<c:when test="${op eq '查看'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="" />
	</c:when>
	<c:when test="${op eq '审核'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/${entity.id}/audit.do" />
	</c:when>
	<c:when test="${op eq '下发'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/${entity.id}/send.do" />
	</c:when>
</c:choose>
<div id="viewdialog" title="${op}通知公告">
	<form:form action="${formaction}" method="post" id="viewForm"
		commandName="entity" enctype="multipart/form-data">
		<div class="contentTitle02">
			<a>${entity.title }</a>
		</div>
		<div class="contentTitle03">
			<a>${entity.authorName } | <fmt:formatDate pattern="yyyy年MM月dd日 " value="${entity.writeTime}" type="date"/> | ${entity.catname}</a>
		</div>
		<div class="content2">
		${entity.content }</div>
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
		<c:if test="${fn:length(entity.jobNoticeShareList) >0}" >
		<div class="content2">
		<h1>下发单位：</h1><br>
			<table>
				<c:forEach items="${entity.jobNoticeShareList}" var="a" varStatus="xh">
					<tr>
						<td width="5px">${xh.count }</td>
						<td>${a.orgname}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
		</c:if>
		<c:if test="${op eq '审核'}">
			<div class="content2">
			<table>
				<tr>
					<td width="9%">审核：</td>
					<td width="91%"><form:radiobutton path="state" value="3" /> 通过 <form:radiobutton
							path="state" value="4" /> 未通过</td>
				</tr>
				<tr>
					<td colspan="2">审核意见：</td>
				</tr>
				<tr>
					<td colspan="2"><form:textarea  path="audittext" cols="3" cssStyle="width:900px"></form:textarea></td>
				</tr>
			</table>
			</div>
		</c:if>
	</form:form>
</div>
<script type="text/javascript">
	$(function(){
		var buttonsOps = {};
		<c:choose>
		<c:when test="${op eq '审核'}">
		buttonsOps = {
				"确认": function(){
	        		if (!$("#viewForm").validationEngine('validate'))
	    				return false;
	    			$("#viewForm").ajaxSubmit({
	    				target : "#content2",
	    				success : function() {
	    					$("#viewdialog").dialog("close");
	    					layer.open({content:"保存成功!"});
	    				},
	    				error : function() {
	    					layer.open({content:"保存失败"});
	    				}
	    			});
	    			$("#viewForm").clearForm();
	        	},
	            "取消": function() {
	            	$( "#viewdialog" ).dialog( "close" );
	            }
		};
		</c:when>
		<c:when test="${op eq '查看'}">
			buttonsOps = {
				"关闭" : function() {
					$("#viewdialog").dialog("close");
				}
			};
		</c:when>
		<c:when test="${op eq '下发'}">
			buttonsOps = {
				"下发": function(){
		        		if (!$("#viewForm").validationEngine('validate'))
		    				return false;
		    			$("#viewForm").ajaxSubmit({
		    				target : "#content2",
		    				success : function() {
		    					$("#viewdialog").dialog("close");
		    					layer.open({content:"保存成功!"});
		    				},
		    				error : function() {
		    					layer.open({content:"保存失败"});
		    				}
		    			});
		    			$("#viewForm").clearForm();
		    			return false;
		        	},
			"取消" : function() {
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



