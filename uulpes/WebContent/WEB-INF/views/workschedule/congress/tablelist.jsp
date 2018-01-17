<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/congress"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
					<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
					<th>会议主题</th>
					<th>时间</th>
					<th>主持人</th>
					<th>记录人</th>
					<th>地点</th>
					<th>操作</th>
				</tr>
			<c:forEach items="${list}" var="m">
				<tr>
					<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${m.id }" />
				</td>
					<td>${m.subject}</td>
					<td><fmt:formatDate value="${m.starttime}" type="both"/>至<fmt:formatDate value="${m.endtime}" type="both"/></td>
					<td>${m.emcee}</td>
					<td>${m.recordperson}</td>
					<td>${m.place}</td>
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