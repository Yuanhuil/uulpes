<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<script type="text/javascript">
		var orgtype = "${orgtype}";
		var orglevel = "${orglevel}";
		var cityid= "${cityid}";
		var countyid="${countyid}";
</script>

<div id="edudispense">
  <%@include file="edudispense.jsp"%>
</div>
