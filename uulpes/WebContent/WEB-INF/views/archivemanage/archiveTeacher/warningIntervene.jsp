<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>


<div class="title" style="float:none">
	<h1>心理干预记录</h1>
</div>
<div class="P_title">

	<c:forEach var="m" items="${list}">

		<div class="table01">
			<table width="100%" border="1" cellspacing="1" cellpadding="1">
				<tr>
					<th width="20%" scope="col">日期</th>
					<th width="15%" scope="col">教师</th>
					<th width="15%" scope="col">预警级别</th>
					<th width="15%" scope="col">干预方式</th>
					<th width="15%" scope="col">干预结果</th>
					<th width="20%" scope="col">操作</th>

				</tr>

				<tr>
					<td  width="20%"><fmt:formatDate value="${m.interveneTime}" pattern="yyyy-MM-dd" /></td>
					<td width="15%">${m.disposePerson}</td>
					<td width="15%"><c:choose>
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
				<td width="15%"><c:choose>

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
				<td width="15%"><c:choose>

							<c:when test="${m.result==1}">
								取消预警
       						</c:when>
							<c:when test="${m.result==2}">
								继续跟进
							</c:when>
							<c:when test="${m.result==3}">
								需要转介
       						</c:when>
							
							
							
						</c:choose></td>
					<td width="20%"><input class="button-small white detail" id="${m.id}"
						type="button" value="展开"></td>

				</tr>


			</table>
			<div class="content" id="content${m.id}" cols="80" rows="10" style="display:none">${m.process}</div>
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
</html>

