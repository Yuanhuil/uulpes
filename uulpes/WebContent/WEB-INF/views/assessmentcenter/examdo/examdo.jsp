<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!-- controller传到前台的分发量表列表 -->
<c:set var="scale" value="${scale}" scope="session" />
<div class="tableContent">
	<ul>
		<li>
			第${qnum+1}题
		</li>
		<li>
			${title}
		</li>
		<li>
			${descn}
		</li>
		
	</ul>
	<ul>
		<c:if test="${choiceMode==0}">
			<c:forEach var="option" items="${optionMap}"> 
				<li>
					 <c:if test="${option.key!=''}">
			    	 	<input type="radio" name="${qnum}" value="${option.value.value}" />${option.key}: ${option.value.title}
			    	 </c:if>
			   	</li>
		    </c:forEach>
		</c:if>
	</ul>
	<ul>
		<li>
			<span class="header02"> 
				<input class="button-small white nextpage" type="button" chref="${ctx}/assessmentcenter/examdo/${scale.code}/${qnum+1}.do" value="下一题">
			</span>
		</li>
	</ul>
</div>
<script type="text/javascript">
   $(function(){
	   $( ".button-small.white.nextpage" ).click(function(){
		   var h = $(this).attr("chref");
		   $("#content2").load(h);
	   });
   })
</script>