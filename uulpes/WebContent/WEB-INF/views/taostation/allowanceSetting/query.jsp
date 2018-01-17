<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/taostation/allowanceSetting"/>
<form:form id="queryform" action="${baseaction}/search.do" method="post"  modelAttribute="entity">
	<div class="filterContent">
		<ul>
			<li><label class="name04">服务类型</label> <form:input path="servicetype" cssClass="input_160"></form:input></li>
		</ul>
	</div>
	<div class="buttonContent">
	<div class="buttonLeft">
	<ul>
				<li><input class="button-mid white" type="button" value="新增" chref="${baseaction}/add.do" id="add"></li>
				<li><input class="button-mid white" type="button" value="删除" id="del" chref="">  </li>
	</ul></div>	
	<div class="buttonRight">	<ul>
			<li><input id="searchsuborgs" class="button-mid blue" type="submit"
				value="搜索"></li>
			<li><input id="clearform" class="button-mid blue" type="button"
				value="重置"></li>
			
		</ul></div>
	</div>
	</form:form>
<script type="text/javascript">
	$(function(){
		
	$("#del").click(function() {
			var selectedData = [];
			var selectRow = $("input[name='rowcheck']:checked");
			if (selectRow.length === 0) {
				layer.open({
					content : "没有选择相关内容"
				});
				return false;
			}
			layer.confirm('确定要删除所选择记录内容吗?', {
				btn : [ '是', '否' ]
			}, function(index) {
				selectRow.each(function() {
					selectedData.push(this.value);
				});
				$('#content2').load("${baseaction }/delselected.do", {
					rowcheck : selectedData
				});
				layer.close(index);
			}, function(index) {
				layer.close(index);
			});
		});
		$("#clearform").click(function() {
			$("#queryform").clearForm();
		});
		$("#queryform").ajaxForm({
			target : "#tablelist"
		});
		if ($("#add") != null) {
			$("#add").on("click", function() {
				var h = $(this).attr("chref");
				$("#editformdiv").html();
				$("#editformdiv").load(h, function() {
					$("#editdialog").dialog("open");
				});
			});
		}
	});
</script>
