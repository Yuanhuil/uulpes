<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="utf-8">
<title>Insert title here</title>
<style type="text/css">
body {
    margin: 0px auto;
    padding: 0px;
    text-align:center;
    width:80%;
	height:80%;
}
#container{
	width:100%;
	height:100%;
}
#queryDiv{
	text-align:left;
	margin-top:20px;
	margin-bottom:20px;
}
#tableDiv{
	border:2px solid #4d9ab0;
}
.table_comm{
    margin: 0 auto;
    padding: 0;
    width:90%;
	height:100%;
  	border: 2px solid #4d9ab0;
  	/**border-collapse: collapse;*/
  	border-spacing:0px;
  	border-collapse: collapse;
  	text-align: center;
}
.table_comm td{
	padding:5px;
   	border-bottom:1px solid #4d9ab0;
   	border-right:1px solid #4d9ab0;
}
.table_comm th{
	border-right:1px solid #4d9ab0;
   	border-bottom:1px solid #4d9ab0;
}
.table_comm thead{
	background: #ccc url(images/bar.gif) repeat-x left center;
}
.table_comm thead tr th{
	padding:5px;
}
.trcolor{
	background-color: rgb(239,246,255);
}
</style>
</head>

<body>
<div id="tableDiv">
<table class="table_comm" id="tableId">
   			<caption>量表列表</caption>
   			<thead>
	   		<tr>
		   		<th>量表编号</th>
		   		<th>量表名称</th>
		   		<th>量表简称</th>
		   		<th>创建时间</th>
	   		</tr>
   			</thead>
   			<tbody>
   			<c:forEach var="results" items="${list}">
   				<tr>
   					<td>${results.code}</td>
   					<td>${results.title}</td>
   					<td>${results.shortname}</td>
   					<td>${results.creationtime}</td>
   				</tr>
   			</c:forEach>
   			</tbody>
   		</table>
  </div>
</body>
</html>