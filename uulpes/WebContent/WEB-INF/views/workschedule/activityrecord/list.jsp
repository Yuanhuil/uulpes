<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/activityrecord" />
<div id="tab-container" class='tab-container'>
	<ul class='etabs'>
		<li class='tab'><a href="${baseaction }/course/list.do" data-target="#psycourse">心理课</a></li>
		<li class='tab'><a href="${baseaction }/act/list.do" data-target="#psycourse">心理活动</a></li>
		<li class='tab'><a href="${baseaction }/research/list.do" data-target="#psycourse">心理教科研</a></li>
	</ul>
	<div class='panel-container'>
		<div id="psycourse"></div>
	</div>
</div>
<script type="text/javascript">
	$(function(){
		$('#tab-container').easytabs({cache:false});
	});
</script>