<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/taostation/serviceStation"/>
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
<div id="editdialog" title="${empty op ? '新增' : op}流动服务站">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity">
		<form:hidden path="id"/>
		<div class="filterContent-dlg">
			<ul>
				<li><label>流动服务站名称</label> <form:input path="stationname" cssClass="input_160 validate[required]"></form:input></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
		<ul>
		<li><label class="name04">服务时间</label><form:input id="editformstarttime" path="starttime" cssClass="input_160"></form:input></li>
			<li><label class="name04">至</label><form:input id="editformendtime" path="endtime" cssClass="input_160"></form:input></li>
		</ul>
		</div>
		<div class="filterContent-dlg" id="formdzdiv">
			<ul>
				<li><label class="name04">服务地址</label>
				<select class="prov input_140" hidden="hidden"></select> 
				<form:select path="city" cssClass="city input_140"></form:select>
				<form:select path="county" cssClass="dist input_140 "></form:select>
				<form:select path="street" cssClass="street input_140 "></form:select>
			</ul>
		</div>
		<div class="filterContent-dlg">
		<ul>
			<li><label class="name04">受惠人群</label> <form:input path="benifitperson" cssClass="input_300"></form:input></li>
		</ul>
	</div>
		<div class="filterContent-dlg">
		<ul>
			<li><label>服务站工作人员</label><form:select path="workperson" cssClass="input_300" multiple="multiple">
			<form:options items="${persons }"/>
			</form:select></li>
		</ul>
	</div>
		
		<div class="filterContent">
			<ul>
				<li><label class="name04">最终目标</label>
				<form:textarea path="aim"></form:textarea></li>
			</ul>
		</div>
		
		
	</form:form>
</div>
<script type="text/javascript">
$(function(){
	$("#editdialog #workperson").chosen({});
	$("#formdzdiv").citySelect({
		prov:"32",
		city:"${entity.city}",
		dist:"${entity.county}",
		street:"${entity.street}",
		defaultprov:"32",
		nodata:"none"
	}); 
	var formstartDateTextBox = $("#editformstarttime");
	var formendDateTextBox = $("#editformendtime");
	$.timepicker.datetimeRange(
			formstartDateTextBox,
			formendDateTextBox,
			{
				minInterval: (1000*60*60), // 1hr
				dateFormat: 'yy-m-dd', 
				timeFormat: 'HH:mm',
				start: {}, // start picker options
				end: {} // end picker options					
			}
		);
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
			height : 500,
			width : 700,
			buttons : buttonsOps
		});
});

</script>