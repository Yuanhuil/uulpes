<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@include file="query.jsp"%>

<div id="tablelist">
	<%@include file="tablelist.jsp"%>
</div>
<div id="pagediv"></div>
<div id="editformdiv">
</div>
<div id="checkScaleDialog" style="display: none;">
	<!-- <%@include file="view.jsp"%> -->
</div>
<script type="text/javascript">
$(function(){
	$("#investform").validationEngine();
	$("#investform").ajaxForm({
		target : "#tablelist"
	});
	$("#pagediv").jstlPaginator({
		showtotalPage:true,
		showgotoPage:true,
        currentPage: "${page.currentPage}",
        totalPages: "${page.totalPage}",
        totalNumbers:"${page.totalResult}",
        onPageClicked: function(event, originalEvent, page){
        	$("#suborgform").ajaxSubmit({
        		data:{"currentPage":page},
				target : "#tablelist",
			});
        }
	});
});
</script>
