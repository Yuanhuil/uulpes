<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/workschedule/activityplan" />
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
<div id="viewactivityplandialog" title="${op}活动计划">
	<form:form action="${formaction}" method="post" id="viewForm"
		commandName="entity">
		<div class="contentTitle02">
			<a>${entity.title }</a>
		</div>
		<div class="contentTitle03">
			<a>${entity.authorName } | ${entity.schoolyear }&nbsp;${entity.termname }</a>
		</div>
		<table>
			<tr>
				<td style="width:10%"><h1>活动类别:</h1></td>
				<td>${entity.activitycatalogname }</td>
				<td style="width:10%"><h1>活动类型:</h1></td>
				<td>${entity.activitytypename }</td>
			</tr>
			<tr>
				<td style="width:10%"><h1>工作目标:</h1></td>
				<td colspan="3">${entity.target }</td>
			</tr>
			<tr>
				<td style="width:10%"><h1>指导思想:</h1></td>
				<td colspan="3">${entity.ideology }</td>
			</tr>
			<tr>
				<td style="width:10%"><h1>工作重点:</h1></td>
				<td colspan="3">${entity.jobmainpoint }</td>
			</tr>
			<tr>
				<td style="width:10%"><h1>工作内容:</h1></td>
				<td colspan="3">${entity.content }</td>
			</tr>
			<tr>
				<td style="width:10%"><h1>工作步骤以及时间安排:</h1></td>
				<td colspan="3">${entity.arrange }</td>
			</tr>
			<tr>
				<td style="width:10%"><h1>保障措施:</h1></td>
				<td colspan="3">${entity.guarantee }</td>
			</tr>
		</table>
		<c:if test="${fn:length(attachments) >0}">
		<h1>附件:</h1>
			<table>
				<c:forEach items="${attachments}" var="a">
					<tr id="file_${a.jobattachment.id}">
						<td><a href="${a.jobattachment.savePath}" target="_blank">${a.jobattachment.name}</a></td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
		<c:if test="${op eq '审核'}">
			<table>
				<tr>
					<td width="9%">审核：</td>
					<td width="91%"><form:radiobutton path="state" value="3" /> 通过 <form:radiobutton
							path="state" value="4" /> 未通过</td>
				</tr>
				<tr>
					<td>审核意见：</td>
					<td><form:textarea  path="auditext"></form:textarea></td>
				</tr>
			</table>
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
	    					$("#viewactivityplandialog").dialog("close");
	    					$("#content2").noty({
	    			            text        : "保存成功",
	    			            type        : "success",
	    			            timeout     : 2000,
	    			            closeWith   : ['click'],
	    			            layout      : 'topCenter',
	    			            theme       : 'defaultTheme',
	    			        });
	    				},
	    				error : function() {
	    					$("#content2").noty({
	    			            text        : "保存失败",
	    			            type        : "error",
	    			            timeout     : 2000,
	    			            closeWith   : ['click'],
	    			            layout      : 'topCenter',
	    			            theme       : 'defaultTheme',
	    			        });
	    				}
	    			});
	    			$("#viewForm").clearForm();
	        	},
	            "取消": function() {
	            	$( "#viewactivityplandialog" ).dialog( "close" );
	            }
		};
		</c:when>
		<c:when test="${op eq '查看'}">
			buttonsOps = {
				"返回" : function() {
					$("#viewactivityplandialog").dialog("close");
				}
			};
		</c:when>
		</c:choose>
		$("#viewactivityplandialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 800,
			width : 1024,
			buttons : buttonsOps
		});
	});
	
</script>


