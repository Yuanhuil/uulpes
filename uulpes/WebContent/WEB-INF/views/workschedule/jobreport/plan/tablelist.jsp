<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tableContent">
	<c:if test="${chart }">
		${resultTable }
		<%-- <img alt="image" src="${chartURL }"> --%>
	</c:if>
	<c:if test="${!chart }">
		<label>查询无结果</label>
	</c:if>
</div>

