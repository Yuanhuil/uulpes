<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>

			<th>姓名</th>
			<th>性别</th>
			<th>身份证号</th>
			<th>年级</th>
			<th>班级</th>
			<th>预警时间</th>
			<th>预警级别</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>
				<td><a>${m.name}</a></td>
				<td><a>${m.sex==1?'男':'女'}</a></td>
				<td><a>${m.cardid}</a></td>
				<td><a>${m.grade}</a></td>
				<td><a>${m.className}</a></td>
				<td><a><fmt:formatDate value="${m.warningTime}"
							pattern="yyyy-MM-dd" /> </a></td>
				<td><a> <c:choose>
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
						</c:choose></a></td>



				<td><input class="button-small white view" id="${m.id}"
					type="button" value="查看"> <input
					class="button-small white edit" id="${m.id}" type="button"
					value="编辑"> <input class="button-small white update"
					id="${m.id}" type="button" value="干预记录"> <input
					class="button-small white del" id="${m.id}" type="button"
					value="删除"></td>
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
/* 	jQuery('.update')
			.bind(
					'click',
					function() {
						var id = jQuery(this).attr("id");
						var url = '/pes/consultcenter/warningInterveneS/save.do?status=4&oldstatus=2&&id='
								+ id
								+ '&name1='
								+ jQuery('#name1').val()
								+ '&sex1='
								+ jQuery('#sex1').val()
								+ '&level1='
								+ jQuery('#level1').val()
						divLoadUrl("list", encodeURI(url));
					}); */
					
					
	jQuery('.update').bind(
		'click',
		function() {
			var id = jQuery(this).attr("id");
			
			$("#dialog-form1").dialog("open");
			var h = '/pes/consultcenter/warningInterveneS/addOrUpdate.do?status=4&oldstatus=2&id='
				+ id;
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

						$("#dialog-form1").dialog("open");
						var h = '/pes/consultcenter/warningInterveneS/addOrUpdate.do?intervene_show=none&view=true&id='
								+ id;
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#dialog-form1").dialog("open");
						});

					});
	jQuery('.edit')
			.bind(
					'click',
					function() {
						var id = jQuery(this).attr("id");

						$("#dialog-form1").dialog("open");
						var h = '/pes/consultcenter/warningInterveneS/addOrUpdate.do?intervene_show=none&id='
								+ id;
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#dialog-form1").dialog("open");
						});

					});
	jQuery('.del')
			.bind(
					'click',
					function() {
						var id = jQuery(this).attr("id");
						var url = '/pes/consultcenter/warningInterveneS/delete.do?id='
								+ id
								+ '&name1='
								+ jQuery('#name1').val()
								+ '&sex1='
								+ jQuery('#sex1').val()
								+ '&level1='
								+ jQuery('#level1').val();
						divLoadUrl("list", encodeURI(url));

					});
</script>