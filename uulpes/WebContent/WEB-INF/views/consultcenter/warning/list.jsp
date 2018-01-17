<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>
			<th>姓名</th>
			<th>量表名称</th>
			<th>咨询方式</th>
			<th>咨询类型</th>
			<th>状态</th>
			<th>预警类型</th>
			<th>预警级别</th>
			<th>时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>
				<td>${m.pid}</td>
				<td>${m.scaleid}</td>
				<td>${m.model}</td>
				<td>${m.type}</td>
				<td>${m.status}</td>
				<td>${m.warningType}</td>
				<td>${m.level}</td>
				<td><fmt:formatDate value="${m.date}"
							pattern="yyyy-MM-dd" /> </td>

				<td><input class="button-small white edit" id="${m.id}"
					type="button" value="编辑" >
					<input class="button-small white del" id="${m.id}"
					type="button" value="删除" >
					<input class="button-small white view" id="${m.id}"
					type="button" value="查看" > </td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div id="pagediv"></div>



<script type="text/javascript">
$(function() {
	$("#pagediv").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page) {
			$("#queryform").ajaxSubmit({
				data : {
					"currentPage" : page
				},
				target : "#list",
			});
		},

	});
	
});

</script>