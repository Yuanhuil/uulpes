<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div id="filterContent">
		<%@include file="teacherTeamReportFilter.jsp"%>
	</div>
	<div id="tablelistTeacherTeamReport">
		<%@include file="teacherTeamReportTable.jsp"%>
	</div>
	<!-- <div id="pagedivTeaTeam"></div>-->
</div>
<script type="text/javascript">
$(function() {
	//$("#teacherTeamReportForm").ajaxForm({
		//target : "#tablelistTeacherTeamReport"
	//});
	//$("#pagedivTeaTeam").jstlPaginator({
		//showtotalPage : true,
		//showgotoPage : true,
		//currentPage : "${page.currentPage}",
		//totalPages : "${page.totalPage}",
		//totalNumbers : "${page.totalResult}",
		//onPageClicked : function(event, originalEvent, page) {
			//$("#teacherTeamReportForm").ajaxSubmit({
				//data : {
					//"currentPage" : page
				//},
				//target : "#tablelistTeacherTeamReport",
			//});
		//},
	//});
});
</script>