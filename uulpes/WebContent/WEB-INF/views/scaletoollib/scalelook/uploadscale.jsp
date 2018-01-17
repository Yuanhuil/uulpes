<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form id="subform"
	action="${pageContext.request.contextPath}/assessmentcenter/scalemanager/importscale.do"
	method="post" enctype="multipart/form-data">
	<input type="file" name="file" /> <input type="button" value="上 传" id="submitbutton"/>
</form>
<script type="text/javascript">
	$('#submitbutton').click(function(){
		var options={
				target : "#content2"
		};
        $('#subform').ajaxSubmit(options);
        return false;
	});
</script>
