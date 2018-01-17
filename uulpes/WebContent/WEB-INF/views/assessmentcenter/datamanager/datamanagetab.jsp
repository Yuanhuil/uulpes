<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="tab-container" class='tab-container'>
		<ul class='etabs'>
		<c:if test="${orgType=='2' }">
			<li class='tab'><a href="../../assessmentcenter/datamanager/dataImportUrl.do" data-target="#tabs_datamanager">答案导入</a></li>
		</c:if>
			<li class='tab'><a href="../../assessmentcenter/datamanager/dataExportUrl.do" data-target="#tabs_datamanager">答案导出</a></li>
		<c:if test="${orgType=='1' }">
			<li class='tab'><a href="../../assessmentcenter/datamanager/exportAllData.do" data-target="#tabs_datamanager">导出所有</a></li>
		</c:if>
		</ul>
		<div class='panel-container'>
			<div id="tabs_datamanager">
			</div>
		</div>
	</div>
<script type="text/javascript">
$('#tab-container').easytabs({cache:false});
</script>