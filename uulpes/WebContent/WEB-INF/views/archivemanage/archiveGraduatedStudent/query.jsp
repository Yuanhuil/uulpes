<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/archivemanage/archiveGraduatedStudent/list.do"
	commandName="entity" method="post">
		<div class="filterContent">
			<ul>
				<li><label class="name03">毕业年份</label> <form:select  path="graduateyear"
						cssClass="input_160" defaultvalue="0">
						<form:option value="0">请选择</form:option>
						<c:forEach items="${graduateyearList}" var="graduationyear">
						
							<form:option value="${graduationyear}" ></form:option>
						</c:forEach>
					</form:select></li>
				<!-- <li><label class="name03">年 级</label> <form:select path="nj"
						cssClass="input_160" defaultvalue="">-->

					</form:select></li>
				<li><label class="name03">班 级</label> <form:select path="bh"
						cssClass="input_160" defaultvalue="">

					</form:select></li>
			</ul>
		</div>
	
	<div class="filterContent">
		<ul>
			<li><label class="name03">姓 名</label> <form:input path="xm"
					cssClass="input_160" /></li>
			<li><label class="name03">性&nbsp;&nbsp;别</label> <form:select
					path="xbm" cssClass="input_160">
					<form:option value="">请选择</form:option>
					<form:options items="${sexlist}" itemValue="id" itemLabel="name" />
				</form:select></li>


		</ul>
	</div>






	<div class="buttonContent">
		<input type="submit" id="query" name="query" value="查询"
			class="button-mid blue" /> <input type="button" name="clear"
			id="clear" value="重置" class="button-mid blue" />  <input
			type="button" id="settingButton" value="生成档案设置" class="button-mid blue" /> 

	</div>
	
</form:form>

<script type="text/javascript">
$(function() { 
	var hasSetted = false;
	$("#queryform").ajaxForm({
		target : "#list"
	});

	$("#clear").click(function() {
		$('#queryform').clearForm();
	});
	
	$("#graduateyear").change(function(){
			$("#bh").empty();
			var graduateyear = $(this).val();
			$.ajax({
				url : "/pes/archivemanage/archiveGraduatedStudent/getClassListByGraduateYear.do",
				data : {
					"graduateyear" : graduateyear
				},
				type : "POST",
				success : function(data) {
					debugger;
					$("#bh").append(
									"<option value=''>请选择</option>");
					$.each(data, function(i, k) {
						$("#bh").append(
								"<option value='" + k.bh + "'>"
										+ k.bjmc
										+ "</option>");
					});
				},
				error : function(jqXHR, textStatus,
						errorThrown) {
					
					layer.open({content:'错误: ' + jqXHR.responseText});
				}

			});
	});
	
	
	$("#settingButton").click(function() {
		var str = $(this).val();
		if (str == "生成档案设置") {
			if(hasSetted==true){
				$("#settingButton").attr("value", "关闭档案设置");
				$("#editDiv").attr("style", "display:block");
				$("#list").attr("style", "display:none");
				return;
			}
			var settingform = $('<form></form>');  
			settingform.attr('id',"settingform");
			settingform.attr('action',"/pes/archivemanage/archiveGraduatedStudent/archiveSetting.do");  
			settingform.ajaxSubmit({
				target : "#setting",
				success : function(msg) {
					debugger;
					$("#settingButton").attr("value", "关闭档案设置");
					$("#editDiv").attr("style", "display:block");
					$("#list").attr("style", "display:none");
					hasSetted = true;
					//$('#settingform').remove();
				},
				error : function() {  
			         layer.open({content:'加载页面背景字段时出错!'});  
			      }
			});
		} else {
			$(this).attr("value", "生成档案设置");
			$("#list").attr("style", "display:block");
			$("#editDiv").attr("style", "display:none");
		}

	});
});
</script>