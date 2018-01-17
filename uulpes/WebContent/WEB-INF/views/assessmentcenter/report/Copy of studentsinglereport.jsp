<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人报告</title>
	<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
	<c:set var="baseaction" value="${pageContext.request.contextPath}/assessmentcenter/report"/>
	<style type="text/css">
		.img1{
			position:relative;
			<c:if test="${page.image1}">background-image: url(${baseaction}/reportchart.do?${page.image1});</c:if>
			background-position:center;
		}
	</style>

</head>
<body>
<div class="tableContent">
一、基本信息
<table border="1">
  <tr>
    <td>学校</td><td>${page.userinfo.xxmc} </td>
    <td>学号</td><td>${page.userinfo.xh}</td>
    <td>姓名</td><td>${page.userinfo.xm}</td>
  </tr>
  <tr>
    <td>账号</td><td>${page.username}</td>
    <td>性别</td><td>${page.userinfo.xb}</td>
    <td>民族</td><td>${page.userinfo.mz}</td>
  </tr>
  <tr>
    <td>年级</td><td>${page.userinfo.njmc} </td>
    <td>班级</td><td>${page.userinfo.bjmc} </td>
    <td>姓名拼音</td><td>${page.userinfo.xmpy}</td>
  </tr>
   <tr>
   <td>测评时间</td><td>${page.endTime}</td>
   </tr>
</table>

二、量表简介<br>
${page.scale.descn}
<br>
三、测评结果<br>
表1${page.scale.title} 量表测评结果一览表
<br>
<div align="center">
<table>
${page.dimScoreTable}
</table>
</div>
<div align="center">
	<c:if test="${not empty page.isImage1Display && page.isImage1Display==1}"><c:if test="${not empty page.image1}"><img src='${baseaction}/scalechart.do?${page.image1}'/></c:if></c:if>
	<c:if test="${not empty page.image2}"><img src='${baseaction}/reportchart.do?${page.image2}'/></c:if>
</div>
<br>
四、结果解释与指导建议
<br>
总评：
<c:if test="${not empty page.summarize}">${page.summarize}</c:if>
<br>
各维度的结果解释和指导建议
<br>
${page.dimsDescnTable}
</div>

</body>
</html>