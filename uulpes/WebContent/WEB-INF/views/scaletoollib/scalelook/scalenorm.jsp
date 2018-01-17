<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/scaletoollib/scalelook" />
<div id="tab-container" class='tab-container'>
		<ul class='etabs'>	
			
			<c:if test="${userlevel =='6' }">
			<li class='tab'><a href="${baseaction }/${scaleid }/6/2/scalenormloglist.do" data-target="#tabs_norm">学校常模</a></li>
			</c:if>
			<c:if test="${userlevel =='3' }">
			<li class='tab'><a href="${baseaction }/${scaleid }/4/2/scalenormloglist.do" data-target="#tabs_norm">区县常模</a></li>
			<li class='tab'><a href="${baseaction }/${scaleid }/3/2/scalenormloglist.do" data-target="#tabs_norm">市级常模</a></li>
			</c:if>
			<c:if test="${userlevel =='4' }">
			<li class='tab'><a href="${baseaction }/${scaleid }/4/2/scalenormloglist.do" data-target="#tabs_norm">区县常模</a></li>
			</c:if>
			<li class='tab'><a href="${baseaction }/${scaleid }/1/2/scalenormloglist.do" data-target="#tabs_norm">全国常模</a></li>
		</ul>
		<div class='panel-container'>
			<div id="tabs_norm"></div>
			
		</div>
	</div>
<script type="text/javascript">
$('#tab-container').easytabs({cache:false});
$(function(){
	$("#goback").click(function(){
		debugger;
		var url ="${ctx }/systeminfo/sys/user/student.do";
		$("#content2").load(url); 
	   });
});

</script>

