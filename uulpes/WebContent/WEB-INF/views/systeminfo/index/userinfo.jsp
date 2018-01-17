<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="userinfo" class="loginMessage">
    <shiro:user>
    	<a>欢迎您 </a><a><shiro:principal property="realname"/></a> |<a href="${ctx}/logout.do">退出</a>
    </shiro:user>
    <c:set var="currentuserid" scope="session"><shiro:principal property="id"/></c:set>
</div>