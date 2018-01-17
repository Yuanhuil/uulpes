<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>
			<th>发件人</th>
			<th>收件人</th>
			<th>标题</th>
			<th>时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>
				<td><a>${fromid[m.fromid]}</a></td>
				<td><a>${toid[m.toid]}</a></td>
				<td><a>${m.title}</a></td>
				<td><a><fmt:formatDate value="${m.sendDate}"
							pattern="yyyy-MM-dd   hh:mm:ss" /> </a></td>

				<td>
				<shiro:hasPermission name="consultcenter:email:update">
				<input class="button-small white edit" id="${m.id}"
					type="button" value="编辑" status="${m.emailstatus}"> 
				</shiro:hasPermission>
				<shiro:hasPermission name="consultcenter:email:delete">
					<input	class="button-small white del" id="${m.id}" type="button"
					value="删除" status="${m.emailstatus}">
				</shiro:hasPermission>
				 <input
					class="button-small white sent" id="${m.id}" type="button"
					value="发送" status="${m.emailstatus}"> <input
					class="button-small white view" id="${m.id}" type="button"
					value="查看" status="${m.emailstatus}" ${accountid}  >
					<c:choose>
							<c:when test="${m.toid==accountid}">
            				<input
					class="button-small white reply" id="${m.id}" type="button"
					value="回复" status="${m.emailstatus}"></td>
							</c:when>
							
							
						</c:choose>
				
					
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
	$(document).ready(function() {
		jQuery('.edit').each(function(i, item) {
			var status = parseInt(jQuery(this).attr("status"));
			console.log(status > 0);
			if (status > 0) {
				jQuery(this).remove();
			}
		});
		jQuery('.sent').each(function(i, item) {
			var status = parseInt(jQuery(this).attr("status"));
			if (status > 0) {
				jQuery(this).remove();
			}
		});
		jQuery('.view').each(function(i, item) {
			var status = parseInt(jQuery(this).attr("status"));
			if (status == 0) {
				jQuery(this).remove();
			}
		});
	});

	jQuery('.edit').bind('click', function() {

		var id = jQuery(this).attr("id");
		var h = '/pes/consultcenter/email/addOrUpdate.do?id=' + id;
		$('#editDiv').html();
		$('#editDiv').load(h, function() {
			$("#dialog-form1").dialog("open");
		});
	});
	jQuery('.sent').bind('click', function() {

		var id = jQuery(this).attr("id");
		var h = '/pes/consultcenter/email/addOrUpdate.do?id=' + id;
		$('#editDiv').html();
		$('#editDiv').load(h, function() {
			$("#dialog-form1").dialog("open");
		});

	});
	jQuery('.view')
			.bind(
					'click',
					function() {

						var id = jQuery(this).attr("id");
						var h = '/pes/consultcenter/email/addOrUpdate.do?view=true&id='
								+ id;
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#dialog-form1").dialog("open");
						});

					});
		jQuery('.reply')
			.bind(
					'click',
					function() {

						var id = jQuery(this).attr("id");
						var h = '/pes/consultcenter/email/reply.do?id='
								+ id;
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
					var url = '/pes/consultcenter/email/del.do?id=' + id
							+ '&title1=' + jQuery('#title1').val()
							+ '&beginDate=' + jQuery('#beginDate').val()
							+ '&endDate=' + jQuery('#endDate').val();

					divLoadUrl("list", encodeURI(url));
					alert("删除成功!");
				}
			});
</script>