<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/taostation/allowanceSetting"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
			<th>服务形式</th>
			<th>单价/元</th>
			<th>单位</th>
			<th>基数</th>
			<th>操作</th>
		</tr>
		<c:forEach var="item" items="${list}">
			<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${item.id }" />
				</td>
				<td>${item.servicetype}</td>
				<td>${item.unitprice}</td>
				<td>${item.unit}</td>
				<td>${item.basenum}</td>
				<td><span class="header02"> 
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