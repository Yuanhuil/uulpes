<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

 <form id="subform" action="${pageContext.request.contextPath}/assessmentcenter/scalemanager/importscale.do"   method="post" enctype="multipart/form-data">  
	<table width="80%">
	  <tr>
	    <td>下载量表导入模板：</td>
	    <td><a href="">下载</a></td>
	  </tr>
	  <tr>
	    <td>选择量表模板文件：</td>
	    <td><input type="file" name="file" width="80%" />
	        <input type="submit" value="上 传" />
	    </td>
	  </tr>
	</table>
   </form>
    <form id="subform1" action="${pageContext.request.contextPath}/assessmentcenter/scalemanager/importscalequestion.do"   method="post" enctype="multipart/form-data">  
	<table width="80%">
	  <tr>
	    <td>选择量表题本文件：</td>
	    <td><input type="file" name="file1" width="80%" />
	        <input type="submit" value="上 传" />
	    </td>
	  </tr>
	</table>
   </form>
    <div id="tablelist">
	    <%@include file="scaleimportresult.jsp"%>
	</div>
	<div id="tablelist1">
	 
	</div>
<script type="text/javascript">
$(function(){
	$("#subform").ajaxForm({
		target : "#tablelist"
	});
	$("#subform1").ajaxForm({
		target : "#tablelist1"
	});
});
</script>