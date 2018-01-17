<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>
			<th>创建时间</th>
			<th>团队名称</th>
			<th>团队类型</th>
			<th>团队人数</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${listTeam}" var="m">
			<tr>
				<td><fmt:formatDate value="${m.createtime}"
						pattern="yyyy-MM-dd" /></td>
				<td>${m.name}</td>
				<td>${m.teamtype==1?"教师团体":"学生团体"}</td>
				<td>${m.personnum}</td>
				<td>
				<shiro:hasPermission name="consultcenter:record:teamcoach:update">
				    <input class="button-small white edit1" id="${m.id}"
					type="button" value="编辑">
				</shiro:hasPermission> 
				<shiro:hasPermission name="consultcenter:record:teamcoach:delete">
				    <input	class="button-small white del1" id="${m.id}" type="button"
					value="删除">
				</shiro:hasPermission>
				<shiro:hasPermission name="consultcenter:record:teamcoach:create">
					 <input class="button-small white addRecord1"
					id="${m.id}" type="button" value="添加记录">
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div id="pagediv1"></div>



<script type="text/javascript">
	jQuery('.edit1').bind('click', function() {
		var id = jQuery(this).attr("id");
		var h = '/pes/consultcenter/team/addOrUpdate.do?id=' + id;
		$('#editDiv').html();
		$('#editDiv').load(h, function() {
			$("#dialog-form1").dialog("open");
		});

	});

	jQuery('.del1').bind(
			'click',
			function() {
				if (confirm('确定将此记录删除?')) {
					var id = jQuery(this).attr("id");
					var url = '/pes/consultcenter/team/delete.do?id='
							+ id + '&teamtype1=' + jQuery('#teamtype').val()

							+ '&name1=' + jQuery('#name1').val()
							+ '&personnum1=' + jQuery('#personnum1').val()

					divLoadUrl("list1", encodeURI(url));
				}

			});

	$(".addRecord1")
			.button()
			.on(
					"click",
					function() {
						$("#dialog-form1").dialog("open");
						var id = jQuery(this).attr("id");
						var h = '/pes/consultcenter/teamRecord/addOrUpdate.do?teamid='
								+ id;
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#dialog-form1").dialog("open");
						});
					});
	$(function() {
		$("#pagediv1").jstlPaginator({
			showtotalPage : true,
			showgotoPage : true,
			currentPage : "${page1.currentPage}",
			totalPages : "${page1.totalPage}",
			totalNumbers : "${page1.totalResult}",
			onPageClicked : function(event, originalEvent, page) {
				$("#queryform1").ajaxSubmit({
					data : {
						"currentPage" : page
					},
					target : "#list1",
				});
			},

		});

	});
</script>