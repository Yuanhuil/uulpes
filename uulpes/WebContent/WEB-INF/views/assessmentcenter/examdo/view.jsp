<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!-- controller传到前台的分发量表列表 
<c:set var="examdos" value="${examdos}" />-->
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<td width="40%">量表名称</td>
			<td width="40%">截止日期</td>
			<td width="10%">操作</td>
		</tr>
		<c:forEach var="item" items="${examdos}">
			<tr>
				<td>${item.scalename}</td>
				<td>
					<fmt:formatDate value="${item.hiTime}" type="date" dateStyle="long"/>
				</td>
				<td>
					<span class="header02"> 
						<input class="button-small white examdostart" type="button" chref="${ctx}/assessmentcenter/examdo/${item.scaleId}/start.do" value="开始" />
					</span>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
<script type="text/javascript">
   $(function(){
	   $( ".button-small.white.examdostart" ).click(function(){
		   var h = $(this).attr("chref");
		   $("#content2").load(h);
	   });
   })
</script>