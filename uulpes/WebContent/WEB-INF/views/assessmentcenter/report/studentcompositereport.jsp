<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>">
  <title></title>
  <c:set var="baseaction" value="${pageContext.request.contextPath}/assessmentcenter/report"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <link href="css/report.css" rel="stylesheet" type="text/css">
     <style type="text/css">
		
	</style>
</head>
<body>

<div class="title" ><h1>【${page.xm}】个人复合测评报告</h1></div>
<!-- <div class="description" ><h2>报告编号：区县编号+学校编号+学号+中杠+测评次序</h2></div>-->
<div class="P_title" ><h3>一、基本信息</h3></div>
<div class="table01" ><table width="80%" border="1" cellspacing="1" cellpadding="1">
  <tr>
    <th width="15%" scope="col"><strong>学校</strong></th>
    <th colspan="3" scope="col">${page.xxmc}</th>
    <th width="20%" rowspan="6" scope="col"></th>
  </tr>
  <tr>
    <td><strong>身份证号</strong></td>
    <td colspan="3">${page.sfzjh}</td>
  </tr>
  <tr>
    <td><strong>姓名</strong></td>
    <td width="20%">${page.xm}</td>
    <td width="15%"><strong>学号</strong></td>
    <td width="20%">${page.xh}</td>
    </tr>
  <tr>
    <td><strong>姓名拼音</strong></td>
    <td>${page.xmpy}</td>
    <td><strong>年级</strong></td>
    <td>${page.njmc}</td>
    </tr>
  <tr>
    <td><strong>性别</strong></td>
    <td>${page.xb }</td>
    <td><strong>班级</strong></td>
    <td>${page.bjmc}</td>
    </tr>
  <tr>
    <td><strong>民族</strong></td>
    <td>${page.mz}</td>
    <td><strong>年龄</strong></td>
    <td>${page.age}</td>
    </tr>
</table>
</div>
<div class="P_title" >
<h3>二、总体测评情况</h3>

<c:forEach var="result" items="${page.resultlist}">

<h3>${result.scaletitle }</h3>
<h3>测评时间:${result.testtime}次序:第${result.testnum}次</h3>
	<div class="table01">
	  <table width="100%" border="1" cellspacing="1" cellpadding="1">
		 <tr>
		    <th width="40%" scope="col">维度</th>
		    <th width="30%" scope="col">原始得分</th>
		    <th width="30%" scope="col">T分</th>
		  </tr>
		  <c:forEach var="scorerow" items="${result.scoretable}">
			  <tr>
			    <td>${scorerow.dimtitle}</td>
			    <td>${scorerow.rawscore}</td>
			    <td>${scorerow.tscore}</td>
			  </tr>
		  </c:forEach>
		  
	  </table>
	</div>

	<h4>结果解释：</h4> 
  <p>${result.scaledesc }</p>
  <p>${result.summarize }</p>
 <c:if test="${not empty result.imageUrl}"><div class="chart"><img src='${baseaction}/reportchart.do?${result.imageUrl}'/></div></c:if>
</c:forEach>
</div>
</body>
</html>

