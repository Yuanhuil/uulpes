<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>

			<th>姓名</th>
			<th>性别</th>
			<th>身份证号</th>

			<th>预警时间</th>
			<th>预警级别</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>
				<td>${m.name}</td>
				<td>${m.sex==1?'男':'女'}</td>
				<td>${m.cardid}</td>

				<td><fmt:formatDate value="${m.warningTime}"
							pattern="yyyy-MM-dd" /> </td>
				<td><c:choose>
							<c:when test="${m.level==1}">
								一般危险
       						</c:when>
							<c:when test="${m.level==2}">
								中度危险
							</c:when>
							<c:when test="${m.level==3}">
            				 重度危险
							</c:when>
							<c:when test="${m.level==0}">
             				  正常
							</c:when>
						</c:choose></td>



				<td><input class="button-small white view1" id="${m.id}"
					type="button" value="查看">
					<shiro:hasPermission name="consultcenter:warning:autowarning:update">
					 <input  class="button-small white edit1" id="${m.id}" type="button"  value="编辑">
					</shiro:hasPermission>
					
					 <input class="button-small white update1"
					id="${m.id}" type="button" value="干预记录">
					<shiro:hasPermission name="consultcenter:warning:autowarning:delete">
					 <input	class="button-small white del1" id="${m.id}" type="button"	value="删除">
					</shiro:hasPermission>
			    </td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div id="pagediv1"></div>



<script type="text/javascript">
	$(function() {
		$("#pagediv1").jstlPaginator({
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
	/* jQuery('.update1')
			.bind(
					'click',
					function() {
						var id = jQuery(this).attr("id");
						var url = '/pes/consultcenter/warningInterveneT/save.do?status=4&id='
								+ id
								+ '&name1='
								+ jQuery('#name1').val()
								+ '&sex1='
								+ jQuery('#sex1').val()
								+ '&level1='
								+ jQuery('#level1').val();
						divLoadUrl("list1", encodeURI(url));
					}); */
					
	jQuery('.update1').bind(
		'click',
		function() {
			var id = jQuery(this).attr("id");
			
			$("#dialog-form1").dialog("open");
			var h = '/pes/consultcenter/warningInterveneT/addOrUpdate.do?status=4&oldstatus=2&id='
				+ id;
			$('#editDiv').html();
			$('#editDiv').load(h, function() {
				$("#dialog-form1").dialog("open");
			});
			
		});				
					
					
	jQuery('.edit1')
			.bind(
					'click',
					function() {
						var id = jQuery(this).attr("id");

						$("#dialog-form1").dialog("open");
						var h = '/pes/consultcenter/warningInterveneT/addOrUpdate.do?intervene_show=none&id='
								+ id;
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#dialog-form1").dialog("open");
						});

					});

	jQuery('.view1')
			.bind(
					'click',
					function() {
						var id = jQuery(this).attr("id");

						$("#dialog-form1").dialog("open");
						var h = '/pes/consultcenter/warningInterveneT/addOrUpdate.do?view=true&intervene_show=none&id='
								+ id;
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#dialog-form1").dialog("open");
						});

					});
	jQuery('.del1')
			.bind(
					'click',
					function() {
						var id = jQuery(this).attr("id");
						var url = '/pes/consultcenter/warningInterveneT/delete.do?id='
								+ id
								+ '&name1='
								+ jQuery('#name1').val()
								+ '&sex1='
								+ jQuery('#sex1').val()
								+ '&level1='
								+ jQuery('#level1').val()
								+ '&roleName1='
								+ jQuery('#roleName1').val();
						divLoadUrl("list1", encodeURI(url));

					});
</script>