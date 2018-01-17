<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>
			
			<th>姓名</th>
			<th>性别</th>
			<th>身份证号</th>
			
			<th>预警级别</th>
			<th>干预方式</th>
			<th>干预结果</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>
				<td>${m.name}</td>
				<td>${m.sex==1?'男':'女'}</td>
				<td>${m.cardid}</td>
				
				<td><c:choose>
							<c:when test="${m.level==1}">
								一般危险
       						</c:when>
							<c:when test="${m.level==2}">
								中度危险
							</c:when>
							<c:when test="${m.level==3}">
            				 重度危险
							</c:when>
							<c:when test="${m.level==0}">
             				  正常
							</c:when>
						</c:choose></td>
				<td><c:choose>

							<c:when test="${m.type==1}">
								电话咨询
       						</c:when>
							<c:when test="${m.type==2}">
								网上咨询
							</c:when>
							<c:when test="${m.type==3}">
								面对面咨询
       						</c:when>
							<c:when test="${m.type==4}">
								书信咨询
							</c:when>
							
							
						</c:choose></td>
				<td>
				<c:choose>

							<c:when test="${m.result==1}">
								取消预警
       						</c:when>
							<c:when test="${m.result==2}">
								继续跟进
							</c:when>
							<c:when test="${m.result==3}">
								需要转介
       						</c:when>
							
							
							
						</c:choose>
				
				</td>
				
				

				<td>
				<input class="button-small white view1" id="${m.id}"
					type="button" value="查看" >
				<shiro:hasPermission name="consultcenter:warning:intervention:update">
				<input class="button-small white edit1" id="${m.id}"
					type="button" value="编辑" >
				</shiro:hasPermission>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div id="pagediv1"></div>



<script type="text/javascript">
$(function() {
	$("#pagediv1").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page) {
			$("#queryform1").ajaxSubmit({
				data : {
					"currentPage" : page
				},
				target : "#list1",
			});
		},

	});
	
});
jQuery('.view1').bind(
		'click',
		function() {
			var id = jQuery(this).attr("id");
			$("#dialog-form1").dialog("open");
			var h = '/pes/consultcenter/warningInterveneT/addOrUpdate.do?view=true&id='
				+ id;
			$('#editDiv').html();
			$('#editDiv').load(h, function() {
				$("#dialog-form1").dialog("open");
			});
		});
jQuery('.edit1').bind(
		'click',
		function() {
			var id = jQuery(this).attr("id");
			
			$("#dialog-form1").dialog("open");
			var h = '/pes/consultcenter/warningInterveneT/addOrUpdate.do?oldstatus=4&id='
				+ id;
			$('#editDiv').html();
			$('#editDiv').load(h, function() {
				$("#dialog-form1").dialog("open");
			});
			
		});


</script>