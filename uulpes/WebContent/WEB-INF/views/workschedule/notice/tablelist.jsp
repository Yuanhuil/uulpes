<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/notice"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
					<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
					<th>标题</th>
					<th>发布时间</th>
					<th>发布者</th>
					<th>下发单位</th>
					<th>类别</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			<c:forEach items="${list}" var="m">
				<tr>
					<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${m.id }" />
				</td>
					<td>${m.title}</td>
					<td><fmt:formatDate value="${m.writeTime}" type="date"/> </td>
					<td>${m.authorName}</td>
					<td>${m.depName}</td>
					<td>${m.catname}</td>
					<td>${m.staname}</td>
					<td><span class="header02"> 
					<input class="button-small white view" type="button" chref="${baseaction }/${m.id }/view.do" value="查看">
					<c:choose>
						<c:when test="${m.state eq '1' or m.state eq '4'}">
							<input class="button-small white edit" type="button" chref="${baseaction }/${m.id }/update.do" value="修改"> 
							<input class="button-small white del" type="button" chref="${baseaction }/${m.id }/delete.do"value="删除">
						</c:when>
						<c:when test="${m.state eq '2'}">
							<c:if test="${m.author eq currentuserid}">
								<input class="button-small white del" type="button" chref="${baseaction }/${m.id }/back.do" value="撤回">
							</c:if>
							<shiro:hasPermission name="workschedule:notice:audit">
									<input class="button-small white view" type="button" chref="${baseaction }/${m.id }/audit.do" value="审核">
							</shiro:hasPermission>
						</c:when>
						<c:when test="${m.state eq '5'}">
							<c:if test="${m.dep eq orgId }">
								<input class="button-small white view" type="button" chref="${baseaction }/${m.id }/send.do" value="下发">
							</c:if>
							<c:forEach items="${m.jobNoticeShareList }" var="s">
								<c:if test="${s.orgId eq orgId}">
									<input class="button-small white edit" type="button" chref="${baseaction }/${m.id }/send.do" value="下发">
								</c:if>
							</c:forEach>
						</c:when>
					</c:choose>
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
			$('#editformdiv').load(h, function(response, status, xhr) {
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