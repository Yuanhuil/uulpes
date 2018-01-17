<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div id="filterContent">
		<%@include file="studentTeamReportFilter.jsp"%>
	</div>
	<div id="tableliststudentTeamReport">
		<%@include file="studentTeamReportTable.jsp"%>
	</div>
	<!-- <div id="pagedivStuTeamReport"></div>-->
</div>
<script type="text/javascript">
//$(function() {
	//$("#pagedivStuTeamReport").jstlPaginator({
		//showtotalPage : true,
		//showgotoPage : true,
		//currentPage : "${page.currentPage}",
		//totalPages : "${page.totalPage}",
		//totalNumbers : "${page.totalResult}",
		//onPageClicked : function(event, originalEvent, page4) {
		//	$("#studentTeamReportForm").ajaxSubmit({
				//data : {
					//"currentPage" : page4
				//},
				//target : "#tabs_studentTeamReport",
			//});
		//},

	//});
//});
</script>