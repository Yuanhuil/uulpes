<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/taostation/volunteer"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
			<th>身份证号</th>
			<th>姓名</th>
			<th>性别</th>
			<th>学校</th>
			<th>类型</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
		<c:forEach var="item" items="${list}">
			<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${item.id }" />
				</td>
				<td>${item.idcard}</td>
				<td>${item.name}</td>
				<td><c:if test="${item.gender eq '1'}">男</c:if><c:if test="${item.gender eq '2'}">女</c:if> </td>
				<td>${item.orgname}</td>
				<td><c:if test="${item.type eq '1'}">热线咨询员</c:if><c:if test="${item.type eq '2'}">面谈咨询员</c:if></td>
				<td><c:if test="${item.status eq '1'}">正常</c:if><c:if test="${item.status eq '2'}">离职</c:if></td>
				<td><span class="header02"> 
					<input class="button-normal white edit" type="button" chref="${baseaction }/${item.id }/view.do" value="查看">
					<input class="button-normal white edit" type="button" chref="${baseaction }/${item.id }/update.do" value="修改"> 
					<input class="button-normal white del" type="button" chref="${baseaction }/${item.id }/delete.do"value="删除">
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
		$(".button-normal.white.edit").click(function() {
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function() {
				$("#editdialog").dialog("open");
			});
		});
		$(".button-normal.white.del").click(function() {
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