<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>
			<th>咨询时间</th>
			<th>咨询方式</th>
			<th>咨询类型</th>
			<th>咨询对象</th>
			<th>姓名</th>
			<th>咨询员</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>

				<td><fmt:formatDate value="${m.data}" pattern="yyyy-MM-dd" /></td>
				<td><c:choose>

						<c:when test="${m.consultationmodeid==1}">
								电话咨询
       						</c:when>
						<c:when test="${m.consultationmodeid==2}">
								网上咨询
							</c:when>
						<c:when test="${m.consultationmodeid==3}">
								面对面咨询
       						</c:when>
						<c:when test="${m.consultationmodeid==4}">
								书信咨询
							</c:when>


					</c:choose></td>
				<td>${consultationtypeid[m.consultationtypeid]}</td>
				<td>${m.objtype==1?"教师":"学生"}</td>
				<td>${m.username}</td>
				<td>${teacherid[m.teacherid]}</td>
				<td>
				  <shiro:hasPermission name="consultcenter:record:personcoach:update">
				    <input class="button-small white edit" id="${m.id}"
					 type="button" value="编辑">
				  </shiro:hasPermission>
				  <shiro:hasPermission name="consultcenter:record:personcoach:delete">
				 	  <input class="button-small white del" id="${m.id}" type="button" value="删除">
				  </shiro:hasPermission>
			    </td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div id="pagediv"></div>




<script type="text/javascript">
	jQuery('.edit').bind('click', function() {
		var id = jQuery(this).attr("id");
		var h = '/pes/consultcenter/record/addOrUpdate.do?id=' + id;
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
					var url = '/pes/consultcenter/record/delete.do?id='
							+ id + '&consultationtypeid1='
							+ jQuery('#consultationtypeid').val()
							+ '&consultationmodeid1='
							+ jQuery('#consultationmodeid').val()
							+ '&teacherid1=' + jQuery('#teacherid').val()
							+ '&username1=' + jQuery('#username').val()
							+ '&objtype1=' + jQuery('#objtype').val()
							+ '&beginDate=' + jQuery('#beginDate').val()
							+ '&endDate=' + jQuery('#endDate').val();
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