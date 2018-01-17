<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/organization/sub/ec"/>
<c:set var="formaction" value="" scope="page" />
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction}/${parentId}/create.do" />
	</c:when>
	<c:when test="${op eq '查看'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="" />
	</c:when>
	<c:when test="${op eq '修改'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction}/${parentId}/${entity.id }/update.do" />
	</c:when>
	<c:otherwise>
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction}/${parentId}/create.do" />
	</c:otherwise>
</c:choose>
<div id="suborgdialog" title="${empty op ? '新增' : op}下属机构">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity">
		<form:hidden path="orgid"/>
		<form:hidden path="org.id"/>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">机构层级</label> <form:select
						path="org.orgLevel" cssClass="select_140" >
						<form:option value="">请选择..</form:option>
						<form:options items="${orglevelList}"  itemValue="id"  itemLabel="name"/>
						</form:select></li>
			</ul>
		</div>
		<div class="filterContent-dlg" id="add_dzdiv">
			<ul>
				<li><label class="name04">行政区划</label> <form:select	path="org.provinceid" cssClass="prov select_140" ></form:select></li>
				<li><label class="name04">市</label><form:select path="org.cityid" cssClass="city select_140" ></form:select></li>
				<li><label class="name04">区县</label><form:select path="org.countyid" cssClass="dist select_140" ></form:select></li>
				<li><label class="name04">街道乡镇</label><form:select path="org.townid" cssClass="street select_140" ></form:select></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">机构名称</label>
				<form:input path="jwmc" cssClass="input_140" ></form:input></li>
				<li><label class="name04">机构代码</label>
				<form:input path="jwdm" cssClass="input_140" ></form:input></li>
				<li><label class="name04">联系电话</label>
				<form:input path="lxdh" cssClass="input_140" ></form:input></li>
			</ul>
		</div>
	</form:form>
</div>
<script type="text/javascript">

	$("#add_dzdiv").citySelect({
		prov:"${entity.org.provinceid}",
		city:"${entity.org.cityid}",
		dist:"${entity.org.countyid}",
		street:"${entity.org.townid}",
		defaultprov:"${entity.org.provinceid}",
		defaultcity:"${entity.org.cityid}",
	    defaultdist:"${entity.org.countyid}",
	    defaultstreet:"${entity.org.townid}",
		nodata:"none"
	}); 
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
		height : 280,
		width : 850,
		buttons : buttonsOps
	});
</script>