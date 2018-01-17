<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/user/teacher" />
<form:form id="queryform" action="${baseaction}/search.do" method="post"
	modelAttribute="entity">
	<div class="filterContent">
		<ul>
			<li><label class="name03">角色名称</label> <form:select
					path="roleId" cssClass="select_160">
					<form:option value="">请选择</form:option>
					<form:options items="${rolelist }" itemValue="id"
						itemLabel="roleName" />
				</form:select></li>
			<li><label class="name03">身份证号</label>
			<form:input path="sfzjh" cssClass="input_160" /></li>
			<c:if test="${sonschoolist !=null}">
				<li><label class="name03">学校</label> <select class="select_160"
					id="schoolorgid" name="schoolorgid">
						<option value="">请选择</option>
						<c:forEach var="m" items="${sonschoolist }">
							<option value="${m.id }">${m.name }</option>
						</c:forEach>
				</select></li>
			</c:if>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">姓名</label>
				<form:input path="xm" cssClass="input_160"></form:input></li>
			<li><label class="name03">性别</label>
			<form:select path="xbm" cssClass="select_160">
					<form:option value="">请选择</form:option>
					<form:options items="${sexlist}" itemValue="id" itemLabel="name" />
				</form:select></li>

		</ul>
	</div>
</form:form>

<div class="buttonContent">
	<div class="buttonLeft">
		<ul>
			<form id="impform" action="${baseaction}/import.do" method="post"
				enctype="multipart/form-data">
				<li><shiro:hasPermission name="systeminfo:user:teacher:create"><input class="button-mid white" type="button" value="新增"
					chref="${baseaction}/add.do" id="add">
				<li><input type="file" name="file" id="impfile"
					style="display:none" /></shiro:hasPermission></li>
				<li><shiro:hasPermission name="systeminfo:user:teacher:create"><input class="button-mid white" type="button" value="导入"
					id="import"></shiro:hasPermission></li>
				<!-- <li><input class="button-mid white" type="button" value="导出" id="export"></li> -->
				<li><shiro:hasPermission name="systeminfo:user:teacher:delete"><input class="button-mid white" type="button" value="删除"
					id="del" chref=""></shiro:hasPermission></li>
			</form>
		</ul>
	</div>
	<div class="buttonRight">
		<ul>
			<li><input id="searchsuborgs" class="button-mid blue"
				type="button" value="搜索"></li>
			<li><input id="clearform" class="button-mid blue" type="button"
				value="重置"></li>
		</ul>
	</div>
</div>

<script type="text/javascript">
	$(function() {
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

		$("#searchsuborgs").click(function() {
			$("#queryform").submit();
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

		//$("#import").on("click",function(){
		//return  $("#impfile").click();
		//});
		$("#import").click(function() {
			debugger;
			var url = "${baseaction}/redirectToImportTeacher.do";
			$("#content2").load(url);
		});
		$("#impform").ajaxForm({
			target : "#content2"
		});

		$("#impfile").on("change", function(e) {
			$("#impform").ajaxSubmit({
				success : function(v) {
					layer.open({
						content : '导入数据成功！'
					});
				},
				target : "#content2"
			});
		});
	});
</script>
