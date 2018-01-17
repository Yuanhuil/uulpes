<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>


<div class="title" style="float:none">
	<h1>心理辅导记录</h1>
</div>
<div class="P_title">
	<div class="table01">
		<table style="font-size:12px;">
			<tr>
				<th width="20%" scope="col">日期</th>
				<th width="20%" scope="col">咨询教师</th>
				<th width="20%" scope="col">咨询方式</th>
				<th width="20%" scope="col">咨询类型</th>
				<!-- <th width="20%" scope="col">操作</th> -->
			</tr>
		</table>
	</div>
	<c:forEach var="m" items="${list}">

		<div class="table01">
			<table width="100%" border="1" cellspacing="1" cellpadding="1" style="font-size:12px;">


				<tr>
					<td width="20%"><fmt:formatDate value="${m.data}"
							pattern="yyyy-MM-dd" /></td>
					<td width="20%">${teacherid[m.teacherid]}</td>
					<td width="20%"><c:choose>

							<c:when test="${m.consultationmodeid==1}">
								电话咨询
       						</c:when>
							<c:when test="${m.consultationmodeid==2}">
								网上咨询
							</c:when>
							<c:when test="${m.consultationmodeid==3}">
								面对面咨询
       						</c:when>
							<c:when test="${m.consultationmodeid==4}">
								书信咨询
							</c:when>


						</c:choose></td>
					<td width="20%">${consultationtypeid[m.consultationtypeid]}</td>
					<%-- <td width="20%"><input class="button-small white detail"
						id="${m.id}" type="button" value="展开"></td> --%>

				</tr>


			</table>
			<div class="content" id="content${m.id}" style="display:none">${m.describes}</div>
		</div>




	</c:forEach>
</div>
<script>
	//$(".content").ckeditor();
	$(".detail").click(function() {

		var str = $(this).val();
		var id = $(this).attr("id");
		if (str == "展开") {
			$(this).attr("value", "隐藏");
			$("#content" + id).attr("style", "display:block")
		} else {
			$(this).attr("value", "展开");
			$("#content" + id).attr("style", "display:none")
		}
	});
</script>

