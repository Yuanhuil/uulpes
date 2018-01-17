<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx}/assessmentcenter/invest"/>
<div style="text-align:center;margin:30px;">
<h1>问卷已成功分发~</h1>  <a href="javascript:void(0)" onclick='goback("${baseaction}/list.do")'>返回问卷列表</a>
</div>
<div id="dispensemsg" style="margin:10px;">
<c:forEach var="resultmsg" items="${resultmsglist}">
<h4>${resultmsg}</h4>
</c:forEach> 
</div>
<script type="text/javascript">
  function goback(url){
	   $("#content2").empty();
	   $("#content2").load(url);
  }
</script>