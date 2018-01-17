<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/taostation/serviceStation"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
			<th>流动服务站名称</th>
			<th>服务时间</th>
			<th>服务地址</th>
			<th>受惠人群</th>
			<th>服务站工作人员</th>
			<th>操作</th>
		</tr>
		<c:forEach var="item" items="${list}">
			<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${item.id }" />
				</td>
				<td>${item.stationname}</td>
				<td><fmt:formatDate value="${item.starttime}" type="both"/>至<fmt:formatDate value="${item.endtime}" type="both"/></td>
				<td>${item.address}</td>
				<td>${item.benifitperson}</td>
				<td>${item.workperson}</td>
				<td><span class="header02"> 
					<input class="button-normal white edit" type="button" chref="${baseaction }/${item.id }/view.do" value="查看">
					<input class="button-normal white edit" type="button" chref="${baseaction }/${item.id }/update.do" value="修改"> 
					<input class="button-normal white addrecord" type="button" chref="${baseaction }/${item.id }/addrecord.do" value="编辑记录">
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
		$(".button-normal.white.addrecord").click(function() {
			var h = $(this).attr("chref");
			$('#firstpagecontent').load(h);
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