<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx}/scaletoollib/monitorprocess"/>
<script type="text/javascript">

</script>
<div style="text-align:center;margin:30px;">
<h1>分发结束~</h1> <!--  <a href="javascript:void(0)" chref="${baseaction}/listdispatchrecord.do" onclick="gotoMonitor('${baseaction}/listdispatchrecord.do');">查看监控进程</a>-->
</div>
<c:choose>
	<c:when test="${orgtype=='2' }">
<div>
<table>
<tr>
<th>量表名称</th>
<th>已分发人数</th>
</tr>
<c:forEach var="scale" items="${resultmsgMap.scaleMap}">
<c:set var="key"><c:out value="${scale.key}" /></c:set>
<c:set var="value"><c:out value="${scale.value}" /></c:set>
<tr>
<td>${value }</td>
<td>${resultmsgMap.statisMap[key]}</td>
</tr>
</c:forEach> 
</table>
</div>
<div id="dispensemsg" style="margin:10px;">
<c:forEach var="errormsg" items="${resultmsgMap.errorMsgList}">
<h4>${errormsg}</h4>
</c:forEach> 
</div>
</c:when>
<c:otherwise>

<div>
<table>
<tr>
<th>量表名称</th>
<th>限制再测人数</th>
</tr>
<c:forEach var="statis" items="${resultmsgMap.statisMap}">
<tr>
<td><c:out value="${statis.key}" /></td>
<td><c:out value="${statis.value}" /></td>
</tr>
</c:forEach> 
</table>
</div>
<div id="dispensemsg" style="margin:10px;">
<c:forEach var="errormsg" items="${resultmsgMap.errorMsgList}">
<h4>${errormsg}</h4>
</c:forEach> 
</div>

</c:otherwise>
</c:choose>
<script type="text/javascript">

function gotoMonitor(url) {
			debugger;
			var h = $(this).attr("chref");
			$('#content2').load(url);
}
</script>
