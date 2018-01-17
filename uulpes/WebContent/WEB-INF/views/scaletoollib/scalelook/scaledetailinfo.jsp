<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
	<div id="tab-container" class='tab-container'>
		<ul class='etabs'>
			<li class='tab'><a href="#tabs_baseinfo">量表简介</a></li>
			<li class='tab'><a href="#tabs_questions">量表题本</a></li>
			<li class='tab'><a href="#tabs_dimension">量表维度</a></li>
		</ul>
		<div class='panel-container'>
			<div id="tabs_baseinfo">
				<%@include file="scaleinfo.jsp"%>
			</div>
			<div id="tabs_questions">
				<%@include file="scalequestions.jsp"%>
			</div>
			<div id="tabs_dimension">
				<%@include file="scaledim.jsp"%>
			</div>
		</div>
	</div>
<script type="text/javascript">
$('#tab-container').easytabs();
</script>