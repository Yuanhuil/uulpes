<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!-- Modal -->
<c:set var="formaction" value="" scope="page"/>
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction"/>
		<c:set var="formaction" scope="page" value="${ctx}/systeminfo/sys/organization/ec/${ecteam.ecid }/team/create.do"/>
	</c:when>
	<c:when test="${op eq '查看'}">
		<c:remove var="formaction"/>
		<c:set var="formaction" scope="page" value=""/>
	</c:when>
	<c:when test="${op eq '修改'}">
		<c:remove var="formaction"/>
		<c:set var="formaction" scope="page" value="${ctx}/systeminfo/sys/organization/ec/team/${ecteam.id }/${ecteam.ecid }/update.do"/>
	</c:when>
	<c:otherwise>
		<c:remove var="formaction"/>
		<c:set var="formaction" scope="page" value="${ctx}/systeminfo/sys/organization/ec/${ecteam.ecid }/team/create.do"/>
	</c:otherwise>
</c:choose>
<div id="dialog-form" title="${empty op ? '新增' : op}心理队伍">
  <form:form id="editteamForm" method="post" commandName="ecteam" 
   action="${formaction}" >
  	  <div class="popDiv_content">
  			<ul>
  				<li class="list01">
  				 <form:radiobutton path="gzzw" value="1" cssClass="validate[required]"/>心理负责人
  				 <form:radiobutton path="gzzw" value="2" cssClass="validate[required]"/>心理工作人员
  				</li>
  				<li class="list01"><label class="name04">选择人员</label> <form:select path="ryid" cssClass="validate[required] chosen-select " style="width:350px;" data-placeholder="选择人员" >
  					<c:forEach var="item" items="${usersInOrg}">
  							<form:option value="${item.id}">${item.realname}${item.idcard}</form:option>
  					</c:forEach>
  				</form:select></li>
   				<li class="list01"> <label class="name04">个人简历</label> <form:textarea path="grjl" rows="4" cols="40" cssClass="validate[required]"/></li>
 			</ul>
 	  </div>
  </form:form>
</div>
<script type="text/javascript">
var buttonsOps = {};
<c:choose>
<c:when test="${empty op || op eq '新增' || op eq '修改'}">
	buttonsOps ={
		"保存": function(){
		 if(!$('#editteamForm').validationEngine('validate'))
			 return false;
  		 $("#editteamForm").ajaxSubmit({
  			 target: "#tabs_team",
  			 success:function(){
  				 $( "#dialog-form" ).dialog( "close" );
  				$("#dialog-job").dialog("close");
				$("#tabs_team").noty({
		            text        : "保存成功",
		            type        : "success",
		            timeout     : 2000,
		            closeWith   : ['click'],
		            layout      : 'topCenter',
		            theme       : 'defaultTheme',
		        });
  			 },
  			 error:function(){
  				$("#dialog-job").dialog("close");
				$("#tabs_team").noty({
		            text        : "保存失败",
		            type        : "error",
		            timeout     : 2000,
		            closeWith   : ['click'],
		            layout      : 'topCenter',
		            theme       : 'defaultTheme',
		        });
  			 }
  			 });
  		 $("#editteamForm").clearForm();
  		 return false;
  		},
    "取消": function() {
    	$( "#dialog-form" ).dialog( "close" );
    }
  		};
</c:when>
<c:when test="${op eq '查看'}">
	buttonsOps = {
			"返回": function() {
            	$( "#dialog-form" ).dialog( "close" );
            }		
	};
</c:when>
</c:choose>
	$(".chosen-select").chosen({});
	$( "#dialog-form" ).dialog({
		appendTo:"#addteamDiv",
    	autoOpen: false,
    	modal: true,
    	height: 300,
        width: 500,
        buttons: buttonsOps
  	});
</script>