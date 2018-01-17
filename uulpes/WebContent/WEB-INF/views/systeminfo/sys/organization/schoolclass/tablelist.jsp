<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/organization/schoolclass"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
					<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
					<th>班号</th>
					<th>班级名称</th>
					<th>建班年月</th>
					<th>所在学校</th>
					<th>所在年级</th>
					<th>操作</th>
				</tr>
			<c:forEach items="${list}" var="m">
				<tr>
					<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${m.id }" />
					</td>
					<td>${m.bh}</td>
					<td>${m.bjmc}</td>
					<td>${m.jbny}</td>
					<td>${m.xxmc}</td>
					<td>${m.njmc}</td>
					<td><span class="header02"> 
							<input class="button-small white edit" type="button" chref="${baseaction }/${m.id }/view.do" value="查看">
							<shiro:hasPermission name="systeminfo:schoolclass:update"><input class="button-small white edit" type="button" chref="${baseaction }/${m.id }/update.do" value="修改"> </shiro:hasPermission>
							<shiro:hasPermission name="systeminfo:schoolclass:delete"><input class="button-small white del" type="button" chref="${baseaction }/${m.id }/delete.do"value="删除"></shiro:hasPermission>
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
					layer.close(index);
					$('#content2').load(h);
				}, function(){
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