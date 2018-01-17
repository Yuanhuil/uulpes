<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<table>
  <tr class="titleBg">
	<th>学年</th>
	<th>学期</th>
	<th>活动总结名称</th>
	<th>活动类别</th>
	<th>活动类型</th>
  </tr>
	<c:forEach var="m" items="${jobsummarylist }">
	  <tr>
	    
		<td>${m.schoolyear}</td>
		<td>${m.term}</td>
		<td><a class="titleLeft viewactivitysummary" href="#" chref="${ctx }/workschedule/activitysummary/${m.id }/view.do">${m.title}</a></td>
		<td>${m.activitycatalog}</td>
		<td>${m.activitytype}</td>
	  </tr>
	</c:forEach>
</table>
	<div id="pagediv"></div>
</div>
<script type="text/javascript">
	$(function() {
		$(".titleLeft.viewactivitysummary").click(function() {
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
				$("#moreactivitysummaryqeryform").ajaxSubmit({
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
