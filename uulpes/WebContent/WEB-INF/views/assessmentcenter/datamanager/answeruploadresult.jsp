<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div style="width:100%; height:220px; overflow:auto; border:1px solid #000000;">

<c:forEach var="result" items="${resultMsgList}">
   <p>${result}</p>
</c:forEach>

</div>