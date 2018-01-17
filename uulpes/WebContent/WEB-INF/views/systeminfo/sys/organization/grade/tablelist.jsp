<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div>
	<table>
		<tr class="titleBg">
					<th>序号</th>
					<th>年级名称</th>
					<th>学制</th>
				</tr>
			<c:forEach items="${list}" var="m" varStatus="xh">
				<tr>
					<td>${xh.count }</td>
					<td>${m.njmc}</td>
					<td>${m.xz}</td>
				</tr>
			</c:forEach>
	</table>
</div>
