<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<c:if test="${typeflag!='3' }">
		<div id="filterContent">
			<%@include file="studentPersonalReportFilter.jsp" %>
		</div>
	</c:if>

	
	<div id="studentPersonalReportTableCon">
	    <%@include file="studentPersonalReportTableCon.jsp"%>
	</div>
	
</div>
<script type="text/javascript">
$(function() {
	$("#studentPersonalReportForm").ajaxForm({
		target : "#studentPersonalReportTableCon"
	});
	/*$("#pagedivStuPersonalReport").jstlPaginator({
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
				target : "#tabs_studentReport",
			});
		},

	});*/
});
</script>