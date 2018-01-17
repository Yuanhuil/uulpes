<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.5.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/js_index.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/table.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/pagetoolbar.css">

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

</style>
<script type="text/javascript">

var url =  "${page.url}";


</script>
</head>
  
<body onload="inintTable()">
<div id="container">
  <div id="content">
  	<div id="queryDiv">
  		<form id="form1" action="page.do?currentPage=1&pageSize=${page.pageSize}" method="post">
  			<fieldset>
  			<legend><label>查询</label></legend>
  			<span>型号:</span>
  			<input type="text" id="pcName" name="pcName" value="${computer.pcName}"/>
  			<input type="submit" id="submit" value="submit" />
  			</fieldset>
  		</form>
  	</div>
   	<div id="tableDiv">
   		<table class="table_comm" id="tableId">
   			<caption>table</caption>
   			<thead>
	   		<tr>
		   		<th>seqid</th>
		   		<th>pcName</th>
		   		<th>productDate</th>
		   		<th>price</th>
		   		<th>pcNumber</th>
	   		</tr>
   			</thead>
   			<tbody>
   			<c:forEach var="results" items="${list}">
   				<tr>
   					<td>${results.accountId}</td>
   					<td>${results.username}</td>
   					<td>${results.password}</td>
   					<td>${results.password}</td>
   					<td>${results.password}</td>
   				</tr>
   			</c:forEach>
   			</tbody>
   		</table>
   		<div id="pageToolBarDiv">
   		${page.pageStr}
   			<!-- <span>共</span>
   			<input type="text" id="pageCount" size="1" value="${page.totalPage}" disabled="disabled">
   			<span>页</span>
   			<span>每页</span>
   			<input type="text" id="pageSize" size="1" value="${page.pageSize}"/>
   			<span>条</span>
   			<span>共</span>
   			<input type="text" id="recordCount" size="1" value="${page.totalResult}" disabled="disabled"/>
   			<span>记录;</span>
   			
   			<a id="firstPage" href="#">首页</a>
   			<a id="previousPage" href="#">前一页</a>
   			<span>第</span>
   			<input type="text" id="currentPage" size="1" value="${page.currentPage}"/>
   			<span>页</span>
   			<input id="goPage" class="hidBtn" type="button" value="go"/>
   			<a id="nextPage" href="#">后一页</a>
   			<a id="endPage" href="#">末页</a>
-->
   		</div><!-- pageToolBarDiv end -->
   	</div><!-- tableDiv end -->
  </div><!-- content end -->
  <div id="clean"></div>
</div><!-- container end -->
<div id=pageContainer></div>


</body>
<script type="text/javascript">
	var pageStr = '${page.pageStr}';
</script>
</html>
