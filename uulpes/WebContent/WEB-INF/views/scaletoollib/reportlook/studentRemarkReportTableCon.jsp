<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="tablelistRemarkReport">
		<%@include file="studentRemarkReportTable.jsp"%>
	</div>
	<div id="pagedivStuRemark"></div>
<script type="text/javascript">
$(function() {
	$("#pagedivStuRemark").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page2) {
			$("#studentRemarkReportForm").ajaxSubmit({
				data : {
					"currentPage" : page2
				},
				target : "#studentRemarkReportTableCon",
			});
		},
	});
});
</script>