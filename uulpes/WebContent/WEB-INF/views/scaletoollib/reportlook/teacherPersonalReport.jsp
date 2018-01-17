<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div id="filterContent">
		<%@include file="teacherPersonalReportFilter.jsp"%>
	</div>
	
	<div id="teacherPersonalReportTableCon">
		<%@include file="teacherPersonalReportTableCon.jsp"%>
	</div>
</div>
<script type="text/javascript">
$(function() {
	$("#teacherPersonalReportForm").ajaxForm({
		//target : "#tablelistTeacherPersonalReport"
		target : "#teacherPersonalReportTableCon"
	});
	/*$("#pagedivTeaPersonal").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page) {
			$("#teacherPersonalReportForm").ajaxSubmit({
				data : {
					"currentPage" : page
				},
				target : "#tabs_teacherReport",
			});
		},

	});*/
});
</script>