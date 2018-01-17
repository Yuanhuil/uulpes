<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@ page import="java.util.*"%>
<table id="content" class="table table-hover">
	<thead>
		<tr>
			<th>咨询员</th>
			<th>姓名</th>
			<th>咨询日期</th>
			<th>咨询时间</th>
			<th>咨询方式</th>
			<th>咨询类型</th>
			<th>咨询状态</th>


			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>
				<td>${teacherid[m.teacherid]}</td>
				<td>${m.name}</td>
				<td><fmt:formatDate value="${m.date}" pattern="yyyy-MM-dd" />
				</td>
				<td>${m.timeid}:00</td>
				<td>
				<c:choose>

							<c:when test="${m.model==1}">
								电话咨询
       						</c:when>
							<c:when test="${m.model==2}">
								网上咨询
							</c:when>
							<c:when test="${m.model==3}">
								面对面咨询
       						</c:when>
							<c:when test="${m.model==4}">
								书信咨询
							</c:when>
							
							
						</c:choose>
				</td>
				<td>${type[m.type]}</td>
				<td>
				<c:choose>
							<c:when test="${m.status==0}">
            				    已预约
							</c:when>
							<c:when test="${m.status==1}">
								完成
       						</c:when>
							<c:when test="${m.status==2}">
								未完成
							</c:when>
							
							
						</c:choose>
				</td>


				<td><input class="button-small white seeValue"
					id="${m.id}"   value="查看" style="width:50px">
					<input class="button-small white start"
					id="${m.id}"   value="开始咨询" style="width:70px">
					<input class="button-small white delValue"
					id="${m.id}"   value="删除" style="width:50px"></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div id="pagediv"></div>
<script type="text/javascript">
	$(function() {
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
					target : "#list1",
				});
			},

		});
		
	});
	
		jQuery('.seeValue')
			.bind(
					'click',
					function() {
						$("#dialog-form1").dialog("open");
						var userid = jQuery(this).attr("id");
						
						var SOrT=jQuery(this).attr("SOrT");
						var h = '/pes/consultcenter/appointment/addOrUpdate.do?view=true&id='
								+ userid ;
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#dialog-form1").dialog("open");
						});
					});
		jQuery('.start')
			.bind(
					'click',
					function() {
						$("#dialog-form1").dialog("open");
						var userid = jQuery(this).attr("id");
						
						var SOrT=jQuery(this).attr("SOrT");
						var h = '/pes/consultcenter/record/add.do?id='
								+ userid ;
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#dialog-form1").dialog("open");
						});
					});
					
		jQuery('.delValue').bind(
			'click',
			function() {
				if (confirm('确定将此记录删除?')) {
					var id = jQuery(this).attr("id");

					var url = '/pes/consultcenter/appointment/delete.do?step=1&id=' + id
							+ '&model=' + jQuery('#model').val()
							+ '&teacherid=' + jQuery('#teacherid').val()
							+ '&status=' + jQuery('#status').val()
							+ '&name=' + jQuery('#name').val()
							+ '&type=' + jQuery('#type').val()
							+ '&beginDate=' + jQuery('#beginDate').val()
							+ '&endDate=' + jQuery('#endDate').val();

					divLoadUrl("list1", encodeURI(url));
				}
			});
</script>