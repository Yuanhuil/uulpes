<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/user/ecuser"/>
<form:form id="queryform" action="${baseaction}/search.do" method="post"  modelAttribute="entity">
	<div class="filterContent">
		<ul>
			<li><label class="name03">机构层级</label> <form:select path="orgLevel" cssClass="select_140">
			<form:option value="">请选择</form:option>
			<form:options items="${orglevelList}"  itemValue="id"  itemLabel="name"/>
			</form:select></li>
			<li><label class="name03">机构名称</label> <form:input path="name" cssClass="input_140"></form:input></li>
			<li><label class="name03">角色名称</label> <select id="roleid" name="roleid" class="select_140">
			<option value=0>请选择</option>
			<c:forEach var ="m" items="${rolelist}">
				<option value="${m.id }">${m.roleName }</option>
			</c:forEach>
			</select></li>
		</ul>
	</div>
	<div class="filterContent1" id="dzdiv">
		<ul>
			<li><label class="name03">行政区划</label>
			<form:select path="provinceid" cssClass="prov select_140" style="float:left;"></form:select>
				<form:select path="cityid" cssClass="city select_140"  style="float:left;margin-left:10px;"></form:select>
				<form:select path="countyid" cssClass="dist select_140" style="float:left;margin-left:10px;"></form:select>
				<form:select path="townid" cssClass="street select_140" style="float:left;margin-left:10px;"></form:select>
		</ul>
	</div>
</form:form>
	<div class="buttonContent">
	    <div class="buttonLeft">
			<ul><form id="impform" action="${baseaction}/import.do" method="post" enctype="multipart/form-data">
				<li><shiro:hasPermission name="systeminfo:user:ecuser:create"><input class="button-mid white" type="button" value="新增" chref="${baseaction}/add.do" id="add"></shiro:hasPermission></li>
				<li><input type="file" name="file" id="impfile" width="80%" style="display:none" /></li>
				<li><shiro:hasPermission name="systeminfo:user:ecuser:create"><input class="button-mid white" type="button" value="导入" id="import"></shiro:hasPermission> </li>
				<li><shiro:hasPermission name="systeminfo:user:ecuser:delete"><input class="button-mid white" type="button" value="删除" id="del" chref=""> </shiro:hasPermission></li> 
				</form>
			</ul>
		</div>
		<div class="buttonRight">
			<ul>
			<li><input id="searchsuborgs" class="button-mid blue" type="button"
				value="搜索"></li>
			<li><input id="clearform" class="button-mid blue" type="button"
				value="重置"></li>
			</ul>
		</div>
	</div>	
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
			}, function() {
				layer.closeAll();
				selectRow.each(function() {
					selectedData.push(this.value);
				});
				$('#content2').load("${baseaction }/delselected.do", {
					rowcheck : selectedData
				});
			}, function() {
			});
		});
		$("#dzdiv").citySelect({
			prov : "${entity.provinceid}",
			city : "${entity.cityid}",
			dist : "${entity.countyid}",
			street : "${entity.townid}",
			defaultprov : "${currprov}",
			nodata : "none"
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
		//	return  $("#impfile").click();
		//});
		$("#import").on("click", function() {
			var url = "${baseaction}/redirectToImportEcuser.do";
			$("#content2").load(url);
		});
		$("#searchsuborgs").click(function() {
			$("#queryform").submit();
		});
		$("#clearform").click(function() {
			$("#queryform").clearForm();
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
