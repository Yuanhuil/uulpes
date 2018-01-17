<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div id="filterContent">
		<%@include file="teacherdataimportpagefilter.jsp"%>
	</div>

	<div id="teacherdataimportpagetablecon">
		<%@include file="teacherdataimportpagetablecon.jsp"%>
	</div>
</div>
<script type="text/javascript">
$(function() {
	$("#pagediv1").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page) {
			$("#importFilterParamForm").ajaxSubmit({
				data : {
					"currentPage" : page
				},
				target : "#tabs_datamanager",
			});
		},

	});
});
</script>