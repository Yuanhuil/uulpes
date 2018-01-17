<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/activitysummary/create"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
					<th><input class="checkbox01" type="checkbox"
			name="checkbox" id="checkid" ></th>
					<th>时间</th>
					<th>标题</th>
					<th>活动类别</th>
					<th>活动类型</th>
				</tr>
			<c:forEach items="${list}" var="m">
				<tr>
					<td><input class="checkbox01" type="checkbox" name="rowcheck" value="${m.id }#${m.tablename}"></td>
					<td><fmt:formatDate value="${m.starttime}" type="date"/>至<fmt:formatDate value="${m.endtime}" type="date"/></td>
					<td>${m.title}</td>
					<td>${m.activitycatalog}</td>
					<td>${m.activitytype}</td>
				</tr>
			</c:forEach>
	</table>
	<input id="nextstep" class="button-small red" type="button"
				value="生成工作总结"></li>
</div>

<div id="pagediv"></div>
<script type="text/javascript">
$(function(){
		$("#checkid").click(function(){
			$('input[type=checkbox]').prop('checked', $(this).prop('checked'));
		});
		$("#nextstep").click(function(){
			  var selectedData = [];
			  var selectRow = $("input[name='rowcheck']:checked");
			  if(selectRow.length === 0){
				  layer.open({content:"没有选择相关内容，请选择之后点击【生成工作总结】"});
				  return false;
			  }
			  selectRow.each(function(){
				  selectedData.push(this.value );
			  });
			  $("#queryform").ajaxSubmit({
				  	url:"${baseaction }/select2.do",
					data : {
						"rowcheck" : selectedData
					},
					target : "#select2",
					success: function(){
						$("#select1").hide();
						$("#select_step1").addClass("step1").removeClass("step1_sel");
						$("#select_step2").addClass("step2_sel").removeClass("step2");
					}
				});
		});
	});
</script>