<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style>
.table-fixed{
	table-layout:fixed;
	font-size:13;
	border-collapse:collapse;
	margin:auto;
}
tr{
	background-color:#ffffff;
	line-height:36px;
	text-align:left;
	padding:10px;
	}
th{
	background-color:#f0f0f0;
	line-height:36px;
	text-align:left;
	padding:0 6px;
	}
td{
	background-color:#ffffff;
	line-height:36px;
	text-align:left;
	padding:0 6px;
	text-overflow: ellipsis; 
	/*white-space: nowrap; */
	overflow: hidden;
	}
.td-fixed{
	white-space:nowrap; 
	overflow:hidden; 
	text-overflow:ellipsis;
}
.titleBg{ background-color:#D4D4D4;}
</style>
<script type="text/javascript">
var normid=2;

</script>
</head>
<body>
<table border="1" class="table-fixed">
<tr>
<th>选择</th>
<th>常模名称</th>
<th>常模类型</th>
<th>创建单位</th>
</tr>
<c:forEach var="scalenorm" items="${scaleNormLogList}" varStatus="i">
<tr>
  <td><input name="radiogroup" type="radio" value="${scalenorm.id}"></td>
  <td>${scalenorm.name}</td>
  <td>
  <c:if test="${scalenorm.type==1}">系统自带常模
  </c:if>
  <c:if test="${scalenorm.type==2}">自定义常模
  </c:if>
  </td>
  <td>${scalenorm.orgname}</td>
</tr>
</c:forEach>
</table>
</body>
</html>
