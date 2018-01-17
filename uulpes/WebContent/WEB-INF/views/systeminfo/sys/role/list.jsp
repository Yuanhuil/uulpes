<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@include file="rolefilter.jsp"%>
<div class="tableContent" id="tableContent">
		<%@include file="roletable.jsp"%>
</div>
<div id="editformdiv">
</div>
<script type="text/javascript">
$("#syslogFilterForm").ajaxForm({
	target : "#tableContent"
});
</script>