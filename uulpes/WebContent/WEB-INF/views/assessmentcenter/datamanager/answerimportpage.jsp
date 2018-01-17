<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/scaletoollib/scalelook" />
<hr/>
<div align="right"  class="filterContent" style="margin-top:0px;margin-bottom:2px;">
<ul>
	<li style="float:right;">
	<!-- <form id="quickimportForm" style="display:inline;margin-right:10px;"><a href="javascript:void(0)" onclick="quickImportAnswer()" >快速答案录入</a></form>-->
	<form id="quickimportForm" style="display:inline;margin-right:10px;"><a href="javascript:void(0)" onclick="quickImportAnswer()" ></a></form>
	<form style="display:inline;" id="batchSubmit" action="../../assessmentcenter/datamanager/batchSubmit.do" method="post"><a href="javascript:void(0)" onclick="batchSubmit();">批量答案导入</a></form></li>
</ul>
</div>
<script type="text/javascript">
function batchSubmit(){
	debugger;
	$("#batchSubmit").ajaxSubmit({
		target : "#tabs_datamanager"
	});
}
function quickImportAnswer(){
	$("#quickimportForm").ajaxSubmit({
		target : "#tabs_datamanager"
	});
}
</script>