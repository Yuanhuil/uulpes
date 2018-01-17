<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/activitysummary"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
					<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
					<th>学年</th>
					<th>学期</th>
					<th>活动总结名称</th>
					<th>活动类别</th>
					<th>活动类型</th>
					<th>生成时间</th>
					<th>操作</th>
				</tr>
			<c:forEach items="${list}" var="m">
				<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${m.id }" />
				</td>
					<td>${m.schoolyear}</td>
					<td>${m.term}</td>
					<td>${m.title}</td>
					<td>${m.activitycatalog}</td>
					<td>${m.activitytype}</td>
					<td><fmt:formatDate value="${m.createTime}" type="date"/></td>
					<td><span class="header02"> 
							<input class="button-small white edit" type="button" chref="${baseaction }/${m.id }/view.do" value="查看">
							<input class="button-small white edit" type="button" chref="${baseaction }/${m.id }/update.do" value="修改"> 
							<input class="button-small white del" type="button" chref="${baseaction }/${m.id }/delete.do"value="删除">
				</span></td>
				</tr>
			</c:forEach>
	</table>
</div>
<div id="pagediv"></div>
<script type="text/javascript">
$(function(){
	$("#checkid").click(function() {
		$('input[type=checkbox]').prop('checked', $(this).prop('checked'));
	});
		$(".button-small.white.edit").click(function() {
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function(response,status,xhr) {
				$("#editdialog").dialog("open");
			});
		});
		$(".button-small.white.del").click(function() {
			var h = $(this).attr("chref");
			layer.confirm('确定要删除该记录内容吗?', {
				  btn: ['是','否']
				}, function(index){
					$('#content2').load(h);
					layer.close(index);
				}, function(index){
					layer.close(index);
				});
		});

		$("#pagediv").jstlPaginator({
			showtotalPage : true,
			showgotoPage : true,
			currentPage : "${page.currentPage}",
			totalPages : "${page.totalPage}",
			totalNumbers : "${page.totalResult}",
			onPageClicked : function(event, originalEvent, page) {
				$("#queryform").ajaxSubmit({
					data : {
						"currentPage" : page
					},
					target : "#tablelist",
				});
			},

		});
	});
</script>