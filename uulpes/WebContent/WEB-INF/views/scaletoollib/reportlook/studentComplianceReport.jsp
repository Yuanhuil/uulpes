<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div id="filterContent">
		<%@include file="studentComplianceReportFilter.jsp"%>
	</div>
	
	<div id="studentComplianceReportTableCon">
	    <%@include file="studentComplianceReportTableCon.jsp"%>
	</div>
</div>
<script type="text/javascript">
$(function() {
	$("#studentComplianceReportForm").ajaxForm({
		//target : "#tablelistComplianceReport"
		target : "#studentComplianceReportTableCon"
	});
	/*$("#pagedivStuCompliance").jstlPaginator({
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
				target : "#tabs_studentReport",
			});
		},
	});*/
});
</script>