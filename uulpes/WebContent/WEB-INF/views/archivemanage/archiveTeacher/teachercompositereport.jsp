<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<style>
h1,h2,h3,h4,h5{margin: 20px 0px 20px 0px}

</style>
<div class="title" style="float:none">
	<h1>个人测评报告</h1>
</div>

<div class="P_title" >


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
 <c:if test="${not empty result.imageUrl}"><div class="chart"><img src='/pes/assessmentcenter/report/reportchart.do?${result.imageUrl}'/></div></c:if>
</c:forEach>
</div>

</html>

