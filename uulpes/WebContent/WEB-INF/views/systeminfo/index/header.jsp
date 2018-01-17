<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="topBar" id="topBar">
<div class="leftLink"><a>设为首页 </a><a> | </a><a> 大众版</a></div>
 <%@include file="userinfo.jsp"%>
</div>
<div class="topHeader" id="topHeader">
<div class="logo">
<div class="logoPic">
<c:if test="${sessionScope.user != null }">
	<img src="${ctx}/themes/${sessionScope.user.theme}/images/logo.png" width="80" height="80"  alt=""/>
</c:if>
</div>
<div class="titleText">
<div class="mainTitle">心理健康教育信息化管理平台</div>
<div class="title">${user_org.name }</div>
</div>
</div>
<div class="alarmMessage"></div>
</div>
