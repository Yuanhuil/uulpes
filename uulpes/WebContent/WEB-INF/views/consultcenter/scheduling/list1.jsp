<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@ page import="java.util.*" %> 
<table id="content" class="table table-hover">
			<thead>
				<tr>
					<th>时间</th>
					<th>老师</th>
					<th>操作</th>
				</tr>
			</thead>
		<tbody>
			<c:forEach items="${list}" var="m">
				<tr>
					<td><fmt:formatDate value="${m.date}" pattern="yyyy-MM-dd"  /></td>
					<td>${m.teacherid}</td>
					<td><input class="button-small white edit1" id="${m.id}"
					type="button" value="编辑"> <input
					class="button-small white del1" id="${m.id}" type="button"
					value="删除">
					<input
					class="button-small white addRecord1" id="${m.id}" type="button"
					value="添加记录"></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div id="pagediv"></div>
	
	
	
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
				if(confirm('确定将此记录删除?')){
					var id = jQuery(this).attr("id");
					divLoadUrl("list1",
							'/pes/consultcenter/team/delete.do?id='
									+ id );
				}
				
			});
	
	
	$(".addRecord1").on("click", function() {
		$("#dialog-form1").dialog("open");
		var id = jQuery(this).attr("id");
		var h = '/pes/consultcenter/teamRecord/addOrUpdate.do?teamid='+id;
		$('#editDiv').html();
		$('#editDiv').load(h, function() {
			$("#dialog-form1").dialog("open");
		});
	});
	$(function() {
		$("#pagediv").jstlPaginator({
			showtotalPage : true,
			showgotoPage : true,
			currentPage : "${page.currentPage}",
			totalPages : "${page.totalPage}",
			totalNumbers : "${page.totalResult}",
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