<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@include file="query.jsp"%>

<div id="tablelist">
	<%@include file="tablelist.jsp"%>
</div>

<div id="editformdiv">
</div>
<script type="text/javascript">
$(function(){
	$("#suborgform").validationEngine();
	$("#suborgform").ajaxForm({
		target : "#tablelist"
	});
});
</script>
