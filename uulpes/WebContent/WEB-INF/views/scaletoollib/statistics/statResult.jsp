<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<style>
	.step{
		width:25%; height:40px; background-repeat:no-repeat; float:left; line-height:40px; text-align:center;
	}
</style>
<c:set var="statformurl" value="../scaletoollib/statistics/getDistricts.do"></c:set>
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div class="stepControl">
		<c:if test="${sessionScope.stepcnt>=1}"><c:choose><c:when test="${stepshow==0}"><div class="step" cur="0">第一步</div></c:when><c:otherwise><div class="step">第一步</div></c:otherwise></c:choose></c:if>
		<c:if test="${sessionScope.stepcnt>=2}"><c:choose><c:when test="${stepshow==1}"><div class="step" cur="1">第二步</div></c:when><c:otherwise><div class="step">第二步</div></c:otherwise></c:choose></c:if>
		<c:if test="${sessionScope.stepcnt>=3}"><c:choose><c:when test="${stepshow==2}"><div class="step" cur="2">第三步</div></c:when><c:otherwise><div class="step">第三步</div></c:otherwise></c:choose></c:if>
		<c:if test="${sessionScope.stepcnt>=4}"><c:choose><c:when test="${stepshow==3}"><div class="step" cur="3">第四步</div></c:when><c:otherwise><div class="step">第四步</div></c:otherwise></c:choose></c:if>
    </div>
    <div class="tableContent" style="height:600px">
	    <div class="singleForm">
	    	<c:choose>
	    	<c:when test="${fn:length(results.data)>0}">
	       <c:forEach var="res" items="${results.data}" varStatus="resstatus" >
	          <c:forEach var="title" items="${res.titles}" varStatus="titlestatus" >
	          	  <label class="name04" style="width:100%;text-align:center">${title}</label>
	          	  <c:choose>
		   		  <c:when test="${fn:length(res.datas)>0}">
			   		  <table>
						<tr>
			   		  		<c:forEach var="titles" items="${res.dataTitles}" varStatus="dtstatus" >
								<td>${titles}</td>
							</c:forEach>
						</tr>
						<c:forEach var="data" items="${res.datas}" varStatus="datastatus" >
							<tr>
								<c:forEach var="col" items="${data}" varStatus="colstatus" >
									<td>${col}</td>
								</c:forEach>
							</tr>
						</c:forEach>
					  </table>
				</c:when>
				<c:otherwise>
					<div style="text-align:left">没有${res.dataTitles}的分析数据</div>
				</c:otherwise>
				</c:choose>
				  <c:forEach var="explain" items="${res.explains}" varStatus="datastatus" >
				  	<p style="text-align:left"> ${explain} </p>
				  </c:forEach>
			  </c:forEach>
			</c:forEach>
			</c:when>
			<c:otherwise>
				<div style="text-align:left">没有符合条件的分析结果</div>
			</c:otherwise>
			</c:choose>
	    </div>
	</div>
</div>
<script type="text/javascript">
	
	$(function(){
		debugger;
		//高亮步骤
		$(".step").each(function(index){
			$(this).css("background-image","url(../../themes/"+theme+"/images/step"+(index+1)+"_normal.png)");	
		});
		$(".step[cur]").each(function(index){
			$(this).css("background-image","url(../../themes/"+theme+"/images/step"+(Number($(this).attr("cur"))+1)+"_hover.png)");	
		});
	})
</script>