<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }//assessmentcenter/scalemanager" />
<c:choose>
	<c:when test="${op eq '新建'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="${baseaction }/${scaleid }/importscalenorm.do" />
	</c:when>
	<c:when test="${op eq '导入'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="${baseaction }/${scaleid }/importscalenorm.do" />
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
		<c:set var="formaction" scope="page" value="${baseaction }/create.do" />
	</c:otherwise>
</c:choose>
<div id="editdialog" title="${empty op ? '导入' : op}常模">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity" enctype="multipart/form-data">
	<table>
	  <tr>
	  
	    <td>量表名称 </td>
	    <td><input type="text" ></td>
	    <td rowspan="5" width="180" style="vertical-align: top;">导入提醒：</td>
	  </tr>
	  <tr>
	    <td>常模名称 </td>
	    <td><input type="text" ></td>
	  </tr>
	  <tr>
	    <td>修订次数 </td>
	    <td><input type="text" ></td>
	  </tr>
	   <tr>
	    <td>修订时间 </td>
	    <td><input type="text" ></td>
	  </tr>
	   <tr>
	    <td>修 订 人 </td>
	    <td><input type="text" ></td>
	  </tr>
	  <tr>
	  <td>选择常模文件</td>
	    <td colspan="2">
	   <input 	class="input_300" type="file" name="file">
	    </td>
	  </tr>
	</table>
		
	</form:form>
</div>
<script type="text/javascript">
	$(function(){
		var buttonsOps = {};
		<c:choose>
		<c:when test="${empty op || op eq '导入' || op eq '修改'}">
		buttonsOps = {
				"导入": function(){
	        		if (!$("#editForm").validationEngine('validate'))
	    				return false;
	    			$("#editForm").ajaxSubmit({
	    				target : "#content2",
	    				success : function() {
	    					$("#editdialog").dialog("close");
	    					layer.open({content:"成功导入常模!"});
	    				},
	    				error : function() {
	    					layer.open({content:"导入常模失败!"});
	    				}
	    			});
	    			$("#editForm").clearForm();
	    			return false;
	        	},
	            "取消": function() {
	            	$( "#editdialog" ).dialog( "close" );
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
			height : 350,
			width : 520,
			buttons : buttonsOps
		});
	});
	
</script>


