<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

	<div id="tablelistPersonalReport">
		<%@include file="studentPersonalReportTable.jsp"%>
	</div>
	<div id="pagedivStuPersonalReport"></div>
<script type="text/javascript">
$(function() {
	$("#pagedivStuPersonalReport").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page1) {
			$("#studentPersonalReportForm").ajaxSubmit({
				data : {
					"currentPage" : page1
				},
				target : "#studentPersonalReportTableCon",
			});
		},

	});
});
</script>