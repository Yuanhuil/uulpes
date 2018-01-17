<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<c:set var="baseaction" value="${pageContext.request.contextPath}/assessmentcenter/report"/>
  <title></title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <link href="css/report.css" rel="stylesheet" type="text/css">
     <style type="text/css">
		
	</style>
</head>
<body>
<div class="title" ><h1>【${page.scale.title}】量表团体测评报告</h1></div>

<div class="description" ><h2>${page.startDate}---${page.endDate}</h2></div>
<div class="P_title" >
<h3>一、量表简介</h3>
<p>${page.scale.descn}</p>
</div>
<div class="P_title" >
  <h3>二、测评对象</h3>
  <div class="table01">
	${page.wholeStaticTable}
  </div>
</div>

<div class="P_title" >
<h3>三、测评结果</h3>
	<div class="table01">
		${page.rootDimWarningStatTable}
	</div>
</div>
<div class="chart">
  <img src='${baseaction}/reportchart.do?${page.rootChartUrl}'/>
</div>
<div class="P_title" >
<h3>四、分析结果</h3>
<c:forEach var="descW0" items="${page.descListW0}">
<p>${descW0}</p>
</c:forEach>
  <c:if test="${!page.scale.warningOrNot}"><p>&nbsp;&nbsp;&nbsp;&nbsp;${page.desc5 }</p></c:if>
  <c:if test="${!page.scale.warningOrNot}"><p>&nbsp;&nbsp;&nbsp;&nbsp;${page.desc4 }</p></c:if>
  <p>&nbsp;&nbsp;&nbsp;&nbsp;${page.desc3 }</p>
  <p>&nbsp;&nbsp;&nbsp;&nbsp;${page.desc2 }</p>
  <p>&nbsp;&nbsp;&nbsp;&nbsp;${page.desc1 }</p>
</div>

</body>

</html>