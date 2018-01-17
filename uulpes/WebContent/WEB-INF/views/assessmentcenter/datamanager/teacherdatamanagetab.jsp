<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="tab-container" class='tab-container'>
		<ul class='etabs'>
		<c:if test="${orgType=='2' }">
			<li class='tab'><a href="../../assessmentcenter/datamanager/dataTImportUrl.do" data-target="#tabs_datamanager">数据导入</a></li>
		</c:if>
			<li class='tab'><a href="../../assessmentcenter/datamanager/dataTExportUrl.do" data-target="#tabs_datamanager">数据导出</a></li>
		</ul>
		<div class='panel-container'>
			<div id="tabs_datamanager">
			</div>
		</div>
	</div>
<script type="text/javascript">
$('#tab-container').easytabs({cache:false});
</script>