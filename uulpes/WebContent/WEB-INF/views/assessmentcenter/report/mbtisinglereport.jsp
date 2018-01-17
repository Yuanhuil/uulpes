<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/report.css" rel="stylesheet" type="text/css">
<title>个人报告</title>
	<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
	<c:set var="baseaction" value="${pageContext.request.contextPath}/assessmentcenter/report"/>
</head>
<body>
<div class="title" ><h1>【${page.userinfo.xm}】【${page.scale.title}】个人测评报告</h1></div>
<div class="description" ><h2>报告编号：区县编号+学校编号+学号+中杠+测评次序</h2></div>
<div class="P_title" ><h3>一、基本信息</h3></div>
<div class="table01" ><table width="80%" border="1" cellspacing="1" cellpadding="1">
  <tr>
    <th width="15%" scope="col"><strong>学校</strong></th>
    <th colspan="3" scope="col">${page.userinfo.xxmc}</th>
    <th width="20%" rowspan="7" scope="col"></th>
  </tr>
  <tr>
    <td><strong>身份证号</strong></td>
    <td colspan="3">${page.userinfo.sfzjh}</td>
  </tr>
  <tr>
    <td><strong>姓名</strong></td>
    <td width="20%">${page.userinfo.xm}</td>
    <td width="15%"><strong>工号</strong></td>
    <td width="20%">${page.userinfo.gh }</td>
    </tr>
  <tr>
    <td><strong>姓名拼音</strong></td>
    <td>${page.userinfo.xmpy}</td>
    <td><strong>性别</strong></td>
    <td>${page.userinfo.xb }</td>
  </tr>

  <tr>
    <td><strong>民族</strong></td>
    <td>${page.userinfo.mz}</td>
    <td><strong>年龄</strong></td>
    <td>${page.userinfo.age}</td>
    </tr>
  <tr>
    <td><strong>测评时间</strong></td>
    <td colspan="3">${page.endTime}</td>
  </tr>
</table>
</div>
<div class="P_title" >
<h3>二、量表简介</h3>
<p>${page.scale.descn}</p>
</div>
<div class="P_title" ><h3>三、测评结果</h3></div>
<!-- <div class="table01">
${page.dimScoreTable}
</div>-->
<!-- <div class="chart">
	<c:if test="${not empty page.isImage1Display && page.isImage1Display==1}"><c:if test="${not empty page.image1}"><img src='${baseaction}/scalechart.do?${page.image1}'/></c:if></c:if>
	<c:if test="${not empty page.image2}"><img src='${baseaction}/reportchart.do?${page.image2}'/></c:if>
</div>-->

		<div class="table01">
			${page.dimScoreTable}
		</div>
	    <div class="chart">
			<c:if test="${not empty page.isImage1Display && page.isImage1Display==1}"><c:if test="${not empty page.image1}"><div><img src='${baseaction}/scalechart.do?${page.image1}'/></div></c:if></c:if>
			<c:if test="${not empty page.image2}"><div><img src='${baseaction}/reportchart.do?${page.image2}'/></div></c:if>
			<c:if test="${not empty page.image3}"><div><img src='${baseaction}/reportchart.do?${page.image3}'/></div></c:if>
		</div>

<div class="P_title" >
<h3>四、结果解释与指导建议</h3>
<c:if test="${not empty page.mbtiDesripter}"><p>结果解释：${page.mbtiDesripter}</p></c:if>
<c:if test="${not empty page.mbtiAdvice}"><p>指导建议：${page.mbtiAdvice}</p></c:if>
</div>


</body>
</html>