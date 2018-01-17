<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
	<div id="anwserimportpage.jsp">
	    <%@include file="teacheranswerimportpage.jsp"%>
	</div>
	<div id="tableliststudent">
		<%@include file="teacherdataimportpagetable.jsp"%>
	</div>
	<div id="pagediv1"></div>
<script type="text/javascript">
$(function() {
	$("#pagediv1").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page1) {
			$("#importFilterParamForm").ajaxSubmit({
				data : {
					"currentPage" : page1
				},
				target : "#teacherdataimportpagetablecon",
			});
		},

	});
});
</script>