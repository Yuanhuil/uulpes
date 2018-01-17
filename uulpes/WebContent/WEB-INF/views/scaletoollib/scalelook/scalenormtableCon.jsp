<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/scaletoollib/scalelook" />
<table>
<tr>
<th>常模名称</th>
<th>修订者</th>
<th>修订时间</th>
<th>创建时间</th>
<th>操作</th>
</tr>
<tr>
<c:forEach var="scalenormlog" items="${scaleNormLogList}" varStatus="i">
<tr id="tr_${scalenormlog.id }">
<td>${scalenormlog.name}</td>
<td>${scalenormlog.editer}</td>
<td>${scalenormlog.edittime}</td>
<td>${scalenormlog.createtime}</td>
<td>
	<input	class="button-normal blue view"  type="button"
					value="查看" chref="${baseaction }/${scalenormlog.scaleId }/${scalenormlog.id}/viewnorm.do"  /> 
	<input	class="button-normal blue export"  type="button"
					value="导出" chref="${baseaction }/${scalenormlog.scaleId }/${scalenormlog.id}/exportnorm.do" /> 
	<c:if test="${oplevel==orglevel}">
	<input	class="button-normal blue delete"  type="button" scaleid="${scalenormlog.scaleId}" normid="${scalenormlog.id }"
					value="删除" chref="${baseaction }/deletenorm.do" />
	</c:if>
</td>
</tr>
</c:forEach>
</table>

<script>
$(function(){
	$(".button-normal.blue.view").click(function() {
		debugger;
		var url = $(this).attr("chref");
		layer.open({area: ['700px','500px'],type: 2,title:'常模表', content:url});
	});
	$(".button-normal.blue.export").click(function() {
		var h = $(this).attr("chref");
		document.getElementById("ifile").src=h;
	});
	$(".button-normal.blue.delete").click(function() {
		if(confirm("警告:删除常模将影响量表测试，确认删除？")){
			debugger;
			var url = $(this).attr("chref");
			var scaleid = $(this).attr("scaleid");
			var normid=$(this).attr("normid");
			$.ajax({
		      type: "POST",
		      data:{
		    	scaleid:scaleid,
		    	normid:normid
		      },
		      url: url,
		      success: function(data){
		    	  debugger;
		    	  if(data=="success")
		 		    $("#tr_"+normid).remove(); 
		      }
		   });
		}
	});
});
function viewscalenorm(normid,scaleid) {
	debugger;
	var url = "../../scaletoollib/scalelook/viewnorm.do?normid="
			+ normid+"&scaleid="+scaleid;
	layer.open({area: '700px',type: 2, content:url});
}
</script>