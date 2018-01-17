<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<table>
  <tr class="titleBg">
	<th>活动类别</th>
	<th>活动类型</th>
	<th>计划名称</th>
	<th>计划时间</th>
	<th>制定者</th>
  </tr>
	<c:forEach var="m" items="${jobplanlist }">
	  <tr>
		<td>${m.activitycatalogname}</td>
		<td>${m.activitytypename}</td>
		<td><a class="titleLeft viewactivityplan" href="#" chref="${ctx }/workschedule/activityplan/${m.id }/view.do">${m.title}</a></td>
		<td>${m.termname}(${m.schoolyear})</td>
		<td>${m.authorName}</td>
	  </tr>
	</c:forEach>
</table>
	<div id="pagediv"></div>
</div>
<script type="text/javascript">
	$(function() {
		$(".titleLeft.viewactivityplan").click(function() {
			debugger;
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function(response, status, xhr) {
				$("#viewdialog").dialog("open");
			});
		});
		$("#pagediv").jstlPaginator({
			showtotalPage : true,
			showgotoPage : true,
			currentPage : "${page.currentPage}",
			totalPages : "${page.totalPage}",
			totalNumbers : "${page.totalResult}",
			onPageClicked : function(event, originalEvent, page) {
				$("#moreactivityplanqueryform").ajaxSubmit({
					data : {
						"currentPage" : page,
						"pagequery":"yes"
					},
					target : "#tablelist",
				});
			},

		});
	});

</script>
