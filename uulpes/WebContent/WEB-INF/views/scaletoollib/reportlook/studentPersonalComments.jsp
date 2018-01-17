<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div id="filterContent">
		<%@include file="studentPersonalCommentsFilter.jsp"%>
	</div>
	<div id="tablelistStudentPersonalComments">
		<%@include file="studentPersonalCommentsTable.jsp"%>
	</div>
	<div id="pagedivPersonalComments"></div>
</div>
<script type="text/javascript">
$(function() {
	$("#studentPersonalCommentsForm").ajaxForm({
		target : "#tablelistStudentPersonalComments"
	});
	$("#pagedivPersonalComments").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page3) {
			$("#studentPersonalCommentsForm").ajaxSubmit({
				data : {
					"currentPage" : page3
				},
				target : "#tabs_studentPersonalComments",
			});
		},
	});
});
</script>