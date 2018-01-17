<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

	<div id="tablelistPersonalReport">
		<%@include file="teacherPersonalReportTable.jsp"%>
	</div>
	<div id="pagedivTeaPersonal"></div>
<script type="text/javascript">
$(function() {
	$("#pagedivTeaPersonal").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page1) {
			$("#teacherPersonalReportForm").ajaxSubmit({
				data : {
					"currentPage" : page1
				},
				target : "#teacherPersonalReportTableCon",
			});
		},

	});
});
</script>