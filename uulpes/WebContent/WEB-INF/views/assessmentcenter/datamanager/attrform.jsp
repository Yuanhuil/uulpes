<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="currentCol" value="${0}"/>
<c:set var="colNum" value="${4}"/>
<table>
<tr>
<c:forEach var="attr" items="${attr_list}">
<td>
<c:choose>
  <c:when test="${attr.field.enableReq}">
    <input type='checkbox' name="attrIds" checked disabled />
  </c:when>
  <c:otherwise>
    <input type='checkbox' id="attrIds" name="attrIds" value="${attr.id}"/>
  </c:otherwise>
</c:choose>
</td><td>${attr.label}</td>
<c:set var="currentCol" value="${currentCol+1}"/>
<c:if test="${currentCol==colNum}"><c:set var="currentCol" value="${0}"/></tr><tr></c:if>
</c:forEach>
<c:if test="${currentCol!=0&&currentCol < colNum}" >
<c:forEach begin="${currentCol}" end="${colNum-1}" step="1">
<td>&nbsp;</td><td>&nbsp;</td>
</c:forEach>
</c:if>
</tr>	
</table>