<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="tablelist">
<c:if test="${showchosen eq true }">
	<input name="c" value="1" id="ec" type="radio"/>区县教委
	<input name="c" value="2" id="school" type="radio"/>市直属学校
</c:if>
<div class="tableContent">
	<c:if test="${chart }">
		${resultTable }
	</c:if>
	<c:if test="${!chart }">
		<label>无查询结果</label>
	</c:if>
</div>
</div>
<script type="text/javascript">
$(function(){
	$("input:radio[name='c']").change(function(){
		var s = $("input:radio[name='c']:checked").val();
		$("#reportdiv").load("${ctx }/workschedule/jobreport/unit/list.do",{"queryOrgtype":s},function(){
			if(s === '1')
				$("#ec").attr("checked","checked");
			else
				$("#school").attr("checked","checked");
		});
	});
});
</script>




