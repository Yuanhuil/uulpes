<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/user"/>
<%@include file="query.jsp"%>

<div id="tablelist">
	<%@include file="tablelist.jsp"%>
</div>
<div id="editformdiv">

</div>
<script type="text/javascript">
$(function(){
	$("#userform").validationEngine();
	$("#suborgform").ajaxForm({
		target : "#tablelist"
	});
});
</script>
