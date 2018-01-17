<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table>
<tr>
<th>量表-常模类型</th>
<th>添加时间</th>
<th>操作</th>
</tr>
<tr>
<c:forEach var="scalenormlog" items="${scaleNormLogList}" varStatus="i">
<tr>
<td>${scalename}</td>
<td>${scalenormlog.editTime}</td>
<td>
	<input	class="button-normal blue view"  type="button"
					value="查看" chref="${baseaction }/${salenormlog.areaid}/${scalenormlog.scaleid }/viewnorm.do" /> 
	<input	class="button-normal blue view"  type="button"
					value="修改" chref="${baseaction }/${salenormlog.areaid}/${scale.code }/viewnorm.do" /> 
	<input	class="button-normal blue view"  type="button"
					value="导入" chref="${baseaction }/${salenormlog.areaid}/${scale.code }/viewnorm.do" /> 
	<input	class="button-normal blue view"  type="button"
					value="导出" chref="${baseaction }/${salenormlog.areaid}/${scale.code }/viewnorm.do" /> 
</td>
</tr>
</c:forEach>
</table>

<script type="text/javascript">
$(function(){
	$("#goback").click(function(){
		debugger;
		var url ="${ctx }/systeminfo/sys/user/student.do";
		$("#content2").load(url); 
	   });
});

</script>