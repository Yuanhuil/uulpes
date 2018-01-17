<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div id="filterContent">
		<%@include file="scalenormfilter.jsp" %>
	</div>
	<div id="scalenormtableCon">
	    <%@include file="scalenormtableCon.jsp"%>
	</div>
	<div id="editformdiv">
	</div>
</div>
<script type="text/javascript">
$(function() {
	$("#scalenormform").ajaxForm({
		target : "#scalenormtableCon"
	});
});
</script>