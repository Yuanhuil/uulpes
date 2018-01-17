<%@ tag pageEncoding="UTF-8" description="显示操作成功/失败的消息，内容为:message/error" %>
<%@ attribute name="successMessage" type="java.lang.String" required="false" description="成功消息" %>
<%@ attribute name="errorMessage" type="java.lang.String" required="false" description="失败消息" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty successMessage}">
    <c:set var="message" value="${successMessage}"/>
</c:if>
<c:if test="${not empty errorMessage}">
    <c:set var="error" value="${errorMessage}"/>
</c:if>
<c:if test="${not empty message}">
<script>
	$("#content2").noty({
            text        : "${message}",
            type        : "success",
            timeout     : 2000,
            closeWith   : ['click'],
            layout      : 'topCenter',
            theme       : 'defaultTheme',
        });
</script>

</c:if>
<c:if test="${not empty error}">
   <script>
	$("#content2").noty({
            text        : "${error}",
            type        : "error",
            timeout     : 2000,
            closeWith   : ['click'],
            layout      : 'topCenter',
            theme       : 'defaultTheme',
        });
</script>
</c:if>