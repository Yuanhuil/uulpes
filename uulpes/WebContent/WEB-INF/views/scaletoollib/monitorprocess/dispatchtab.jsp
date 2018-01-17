<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="tab-container" class='tab-container'>
		<ul class='etabs'>
			<li class='tab'><a href="../../scaletoollib/monitorprocess/taskmanageOfStudent.do" data-target="#tabs_dispatch">学生分发记录</a></li>
			<li class='tab'><a href="../../scaletoollib/monitorprocess/taskmanageOfTeacher.do" data-target="#tabs_dispatch">教师分发记录</a></li>
		</ul>
		<div class='panel-container'>
			<div id="tabs_dispatch">

			</div>

		</div>
	</div>
<script type="text/javascript">
$('#tab-container').easytabs({cache:false});
</script>