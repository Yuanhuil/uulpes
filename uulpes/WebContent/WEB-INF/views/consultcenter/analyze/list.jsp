<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">

	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>
				<c:forEach items="${m}" var="m1">
					<td>${m1}</td>
				</c:forEach>

				
			</tr>
		</c:forEach>
	</tbody>
</table>
<div id="pagediv"></div>



<script type="text/javascript">
	
</script>