<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="tablelistComplianceReport">
		<%@include file="studentComplianceReportTable.jsp"%>
	</div>
	<div id="pagedivStuCompliance"></div>
<script type="text/javascript">
$(function() {
	$("#pagedivStuCompliance").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page2) {
			$("#studentComplianceReportForm").ajaxSubmit({
				data : {
					"currentPage" : page2
				},
				target : "#studentComplianceReportTableCon",
			});
		},
	});
});
</script>