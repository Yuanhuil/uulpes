<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"   prefix="fn" %>

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

<div style="text-align:center;">
<table border="1" class="table-fixed">

			<tr class="titleBg">
				<td></td>
				<td></td>
				<c:forEach items="${dimlist}" var="dim">
				<td colspan="2">${dim.title }</td>
				</c:forEach>
			</tr>
			<tr class="titleBg">
				<td>年级</td>
				<td>性别</td>
				<c:forEach items="${dimlist}" var="dim">
				<td>均值</td>
				<td>标准差</td>
				</c:forEach>
			</tr>
			<c:forEach items="${normMap}" var="entry">
				<tr>
				<c:set var="grade" value="${fn:split(entry.key, '_')}" />
				    <td>${grade[0]}</td>

					
				<c:choose>
				  <c:when test="${grade[1] == '1'}">
				    <td>男</td>
				  </c:when>
				  <c:when test="${grade[1] == '2'}">
				    <td>女</td>
				  </c:when>
				  <c:otherwise> 
				     <td></td>
				  </c:otherwise>
				</c:choose>
					
					<c:forEach items="${dimlist}" var="dim">
					<c:set var="wid" value="${dim.title}" />
					<c:set var="no" value="${entry.value}" />
						<td>${no[wid].m}</td>
						<td>${no[wid].sd }</td>
					</c:forEach>
				</tr>
			</c:forEach>	

</table>
</div>
