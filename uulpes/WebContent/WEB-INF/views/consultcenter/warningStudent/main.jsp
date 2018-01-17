<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="tab-container" class='tab-container'>

	<ul class='etabs'>
		<li class='tab'><a href="#tabs1-html">学生群体</a></li>
		<li class='tab'><a href="#tabs1-js" id="teacher_tab">教师群体</a></li>

	</ul>
	<div id="tabs1-html">
		<%@include file="query.jsp"%>
		<div id="list">
			<%@include file="list.jsp"%>
		</div>
	</div>
	<div id="tabs1-js"></div>

</div>
<div id="editDiv">
</div>
<script type="text/javascript">
	$('#tab-container').easytabs();
	$("#teacher_tab").click(
			function() {
				divLoadUrl("tabs1-js",
						'/pes/consultcenter/warningTeacher/main.do');
			});
</script>