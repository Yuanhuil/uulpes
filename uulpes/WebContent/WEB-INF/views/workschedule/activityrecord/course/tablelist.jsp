<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/activityrecord/course"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
					<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
					<th width="15%">标题</th>
					<th width="20%">时间</th>
					<th width="6%">级别</th>
					<th width="8%">类型</th>
					<th width="18%">主讲人/组织老师</th>
					<th width="10%">节数/次数</th>
					<th width="20%">操作</th>
				</tr>
			<c:forEach items="${list}" var="m">
				<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${m.id }" />
				</td>
					<td>${m.title}</td>
					<td><fmt:formatDate value="${m.startactivitytime}" type="date"/>至<fmt:formatDate value="${m.endactivitytime}" type="date"/></td>
					<td>${m.level}</td>
					<td>${m.typename}</td>
					<td>${m.speaker}</td>
					<td>${m.num}</td>
					<td><span class="header02"> 
							<input class="button-small white view" type="button" chref="${baseaction }/${m.id }/view.do" value="查看">
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
	$(".button-small.white.view").click(function() {
		var h = $(this).attr("chref");
		$('#editformdiv').empty();
		$('#editformdiv').load(h, function(response, status, xhr) {
			$("#viewdialog").dialog("open");
		});
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