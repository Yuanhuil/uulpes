<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>

			<th>姓名</th>
			<th>性别</th>
			<th>身份证号</th>
			<th>年级</th>
			<th>班级</th>
			<th>预警时间</th>
			<th>量表名称</th>
			<!-- <th>题目数量</th>-->
			<th>典型个案</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>
				<td>${m.xm}</td>
				<td>${m.xbm==1?'男':'女'}</td>
				<td>${m.sfzjh}</td>
				<td>${m.njmc}</td>
				<td>${m.bjmch}</td>
				<td><fmt:formatDate value="${m.okTime}"
							pattern="yyyy-MM-dd" /> </td>
				<td>${m.title}</td>
				<!-- <td>${m.questionnum}</td>-->
				<td><c:choose>
							<c:when test="${m.warningGrade==1}">
								<img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
       						</c:when>
							<c:when test="${m.warningGrade==2}">
								<img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
								<img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
							</c:when>
							<c:when test="${m.warningGrade==3}">
            				  <img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
            				  <img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
            				  <img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
							</c:when>
							<c:when test="${m.warningGrade==0}">
             				  正常
							</c:when>
						</c:choose></td>




				<td><input class="button-small white add" id="${m.id}"
					type="button" value="预警" iswarnsure="${m.iswarnsure}"><input
					class="button-small white ignore" id="${m.id}" type="button"
					value="忽略" iswarnsure="${m.iswarnsure}"></td>
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
					target : "#list",
				});
			},

		});

	});
	jQuery('.add,.ignore').each(function(i, item) {
		var status = parseInt(jQuery(this).attr("iswarnsure"));
		if (status != 0) {
			jQuery(this).remove();
		}
	});

/* 	jQuery('.add')
			.bind(
					'click',
					function() {
						var id = jQuery(this).attr("id");
						var url = '/pes/consultcenter/warningStudent/add.do?iswarnsure=2&id='
								+ id
								+ '&iswarnsure1='
								+ jQuery('#iswarnsure').val()
								+ '&warningGrade1='
								+ jQuery('#warningGrade').val()
								+ '&xbm1='
								+ jQuery('#xbm').val()
								+ '&xm1='
								+ jQuery('#xm').val()
								+ '&beginDate='
								+ jQuery('#beginDate').val()
								+ '&endDate='
								+ jQuery('#endDate').val();
						divLoadUrl("list", encodeURI(url));
					}); */
					
jQuery('.add').bind(
		'click',
		function() {
			var id = jQuery(this).attr("id");
			
			$("#dialog-form1").dialog("open");
			var h = '/pes/consultcenter/warningInterveneS/add.do?intervene_show=none&id='
				+ id;
			$('#editDiv').html();
			$('#editDiv').load(h, function() {
				$("#dialog-form1").dialog("open");
			});
			
		});
	jQuery('.ignore')
			.bind(
					'click',
					function() {
						var id = jQuery(this).attr("id");
						var url = '/pes/consultcenter/warningStudent/updateIswarnsure.do?iswarnsure=1&id='
								+ id
								+ '&iswarnsure1='
								+ jQuery('#iswarnsure').val()
								+ '&warningGrade1='
								+ jQuery('#warningGrade').val()
								+ '&xbm1='
								+ jQuery('#xbm').val()
								+ '&xm1='
								+ jQuery('#xm').val()
								+ '&beginDate='
								+ jQuery('#beginDate').val()
								+ '&endDate='
								+ jQuery('#endDate').val()
						divLoadUrl("list", encodeURI(url));
					});
</script>