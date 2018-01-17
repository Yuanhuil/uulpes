<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/systeminfo/sys/organization/schoolclass" />
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="${baseaction }/create.do" />
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
<div id="editdialog" title="${empty op ? '新增' : op}班级">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity" enctype="multipart/form-data">
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">班号</label> <form:input path="bh"
						cssClass="input_160"></form:input></li>
				<li><label class="name04">班级名称</label> <form:input path="bjmc"
						cssClass="input_160"></form:input></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
			<li>
				<label class="name04">建班年月</label> 
				<form:input path="jbny" cssClass="input_160"></form:input>
			</li>
			<li>
				<label class="name04">班级类型</label> 
				<form:select  path="bjlxm" cssClass="select_160">
					<form:option value="">请选择</form:option>
					<form:options items="${bjlxmlist }" itemValue="id" itemLabel="name"/>
				</form:select>
			</li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
			<li>
				<label class="name04">所在年级</label> 
				<form:select path="gradeid" cssClass="select_160">
					<form:option value="">请选择</form:option>
					<form:options items="${gradelist }" itemLabel="njmc" itemValue="gradeid"/>
				</form:select>
			</li>
			<li>
				<label class="name04">班主任</label> 
				<form:select path="bzraccountid" cssClass="select_160" >
					<form:option value="0">请选择</form:option>
					<form:options items="${teacherlist }" itemLabel="xm" itemValue="accountId"/>
				</form:select>
			</li>
			<li>
				<form:select path="bzrgh" cssClass="select_160" cssStyle="display:none">
					<form:option value="0">请选择</form:option>
					<form:options items="${teacherlist }" itemLabel="accountId" itemValue="gh"/>
				</form:select>
			</li>
			</ul>
		</div>
		
	</form:form>
</div>
<script type="text/javascript">
	$(function(){
		$("#bzraccountid").change(function(){
			var index = $("#bzraccountid").get(0).selectedIndex;
			$("#bzrgh").get(0).selectedIndex = index;
		});
		$("#jbny").datepicker(
				{ //绑定开始日期
					dateFormat : 'yymm',
					changeMonth : true, //显示下拉列表月份
					changeYear : true, //显示下拉列表年份
					firstDay : "1", //设置开始为1号
				});
		var buttonsOps = {};
		<c:choose>
		<c:when test="${empty op || op eq '新增' || op eq '修改'}">
		buttonsOps = {
				"保存": function(){
	        		if (!$("#editForm").validationEngine('validate'))
	    				return false;
	        		var jbny = $("#jbny").val();
	            	var length = jbny.length;
	            	if(length>6){
	            		layer.open({content:"建班年月输入错误!"});
	            		return false;
	            	}
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
			height : 300,
			width : 520,
			buttons : buttonsOps
		});
	});
	
</script>


