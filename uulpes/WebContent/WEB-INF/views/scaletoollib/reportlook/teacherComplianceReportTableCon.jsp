<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="tablelistComplianceReport">
		<%@include file="teacherComplianceReportTable.jsp"%>
	</div>
	<div id="pagedivTeaCompliance"></div>
<script type="text/javascript">
$(function() {
	$("#pagedivTeaCompliance").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page2) {
			$("#teacherComplianceReportForm").ajaxSubmit({
				data : {
					"currentPage" : page2
				},
				target : "#teacherComplianceReportTableCon",
			});
		},
	});
});
</script>