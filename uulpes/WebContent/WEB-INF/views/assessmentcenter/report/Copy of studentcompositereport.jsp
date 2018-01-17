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
  <title></title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <link href="css/report.css" rel="stylesheet" type="text/css">
     <style type="text/css">
		table{
			background-color:#52B1CD;
			font-size: 14px;
			line-height:22px;
		}
		table tr{
		    background-color:#fff;
		}
		.left_title_2 {
			color:#000;
			font-weight:bold;
			line-height:20px;
			text-align:right;
		}
		.STYLE3 {
			font-size: 24pt;
			font-weight: bold;
			line-height:50px;
		}
	</style>
</head>
<body class="man_zone">

一、基本信息
<table>
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
    <td>年级</td><td>${page.userinfo.nj} </td>
    <td>班级</td><td>${page.userinfo.bj} </td>
    <td>姓名拼音</td><td>${page.userinfo.xmpy}</td>
  </tr>
   <tr>
   <td>测评时间</td><td></td>
   </tr>
</table>

二、各维度分值一览
<table>
	<tr>
		<td><br><c:forEach var="item" items="${page.tables}">${item}</c:forEach>
		</td>
	</tr>    
</table>
</body>
</html>

