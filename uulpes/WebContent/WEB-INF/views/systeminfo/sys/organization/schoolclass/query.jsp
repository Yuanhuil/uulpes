<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/organization/schoolclass" />
<form:form id="queryform" name="form"
	action="${baseaction}/querydata.do" method="post"
	modelAttribute="entity">
	<div class="filterContent">
		<ul>
			<li>
				<label class="name03">年级</label> 
				<form:select path="gradeid" cssClass="input_100">
					<form:option value="">请选择</form:option>
					<form:options items="${gradelist }" itemLabel="njmc" itemValue="gradeid"/>
				</form:select>
			</li>
		</ul>
	</div>


<div class="buttonContent">
<form id="impform" action="${baseaction}/import.do" method="post" enctype="multipart/form-data">
	    <div class="buttonLeft">
			<ul>
				<li><shiro:hasPermission name="systeminfo:schoolclass:create"><input class="button-mid white" type="button" value="新增" chref="${baseaction}/add.do" id="add"></shiro:hasPermission></li>
				<li><input type="file" name="file" id="impfile" width="80%" style="display:none" /></li>
				<li><shiro:hasPermission name="systeminfo:schoolclass:create"><input class="button-mid white" type="button" value="导入" id="classimport" ></shiro:hasPermission> </li>
				<li><shiro:hasPermission name="systeminfo:schoolclass:delete"><input class="button-mid white" type="button" value="删除" id="del" chref=""></shiro:hasPermission>  </li> 
			</ul>
		</div>
</form> 
		<div class="buttonRight">
			<ul>
			<li><input id="searchform" class="button-mid blue" type="submit"
				value="搜索"></li>
			<li><input id="clearform" class="button-mid blue" type="button"
				value="重置"></li>
			</ul>
		</div>
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
				layer.close(index);
				selectRow.each(function() {
				selectedData.push(this.value);
				});
				$('#content2').load("${baseaction }/delselected.do", {rowcheck : selectedData});
			}, function(index) {
				layer.close(index);
			});

		});
		$("#queryform").ajaxForm({
			target : "#tablelist"
		});
		$("#clearform").click(function() {
			$("#queryform").clearForm();
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

		$("#classimport").on("click", function() {
			//return $("#impfile").click();
			var url="${baseaction}/redirectToImportClass.do";
			$("#content2").load(url);
		});

		$("#impform").ajaxForm({
			target : "#content2"
		});

		$("#impfile").on("change", function(e) {
			$("#impform").submit();
		});
	});
</script>