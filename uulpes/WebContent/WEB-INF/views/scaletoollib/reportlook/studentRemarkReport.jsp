<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div id="filterContent">
		<%@include file="studentRemarkReportFilter.jsp"%>
	</div>
	<div id="studentRemarkReportTableCon">
	    <%@include file="studentRemarkReportTableCon.jsp"%>
	</div>
</div>
<script type="text/javascript">
$(function() {
	$("#studentRemarkReportForm").ajaxForm({
		target : "#tablelistRemarkReport"
	});
	/*$("#pagedivStuRemark").jstlPaginator({
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
				target : "#tabs_studentReport",
			});
		},
	});*/
});
</script>