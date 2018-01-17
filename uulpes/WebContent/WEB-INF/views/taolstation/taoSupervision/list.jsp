<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>
			<th>时间</th>
			<th>督导类型</th>
			<th>接待者</th>
		
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>

				<td><fmt:formatDate value="${m.starttime}" pattern="yyyy-MM-dd hh:mm" /></td>
				<td><c:choose>

						<c:when test="${m.type==1}">
								电话咨询
       						</c:when>
						<c:when test="${m.type==2}">
								网上咨询
							</c:when>
						<c:when test="${m.type==3}">
								面对面咨询
       						</c:when>
						<c:when test="${m.type==4}">
								书信咨询
							</c:when>


					</c:choose></td>
				<td>${teacherid[m.teacherid]}</td>
				
				
				
				<td><input class="button-small white edit" id="${m.id}"
					type="button" value="编辑"> <input
					class="button-small white del" id="${m.id}" type="button"
					value="删除"></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div id="pagediv"></div>




<script type="text/javascript">
	jQuery('.edit').bind('click', function() {
		var id = jQuery(this).attr("id");
		var h = '/pes/taolstation/taoSupervision/addOrUpdate.do?id=' + id;
		$('#editDiv').html();
		$('#editDiv').load(h, function() {
			$("#dialog-form1").dialog("open");
		});

	});

	jQuery('.del').bind(
			'click',
			function() {
				if (confirm('确定将此记录删除?')) {
					var id = jQuery(this).attr("id");
					var url = '/pes/taolstation/taoSupervision/delete.do?id='
							+ id 
							+ '&teacherid1=' + jQuery('#teacherid1').val()
							+ '&visitor1=' + jQuery('#visitor1').val()
							
							+ '&starttime1=' + jQuery('#starttime1').val()
							+ '&endtime1=' + jQuery('#endtime1').val();
					divLoadUrl("list", encodeURI(url));
				}

			});
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