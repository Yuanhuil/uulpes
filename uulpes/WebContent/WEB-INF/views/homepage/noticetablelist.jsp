<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<table>
  <tr class="titleBg">
	<th>标题</th>
	<th>发布时间</th>
	<th>发布者</th>
	<th>下发单位</th>
	<th>类别</th>
  </tr>
	<c:forEach var="m" items="${jobnoticelist }">
	  <tr>
	    <td><a class="titleLeft viewnotice" href="#"  chref="${ctx }/workschedule/notice/${m.id }/view.do">${m.title}</a></td>
		<td><fmt:formatDate value="${m.writeTime}" type="date"/> </td>
		<td>${m.authorName}</td>
		<td>${m.depName}</td>
		<td>${m.catname}</td>
	  </tr>
	</c:forEach>
</table>
	<div id="pagediv"></div>
</div>
<script type="text/javascript">
	$(function() {
		$(".titleLeft.viewnotice").click(function() {
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
				debugger;
				$("#morenoticequeryform").ajaxSubmit({
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
