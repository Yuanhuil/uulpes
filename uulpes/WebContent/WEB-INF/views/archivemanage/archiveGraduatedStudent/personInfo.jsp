<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="" class="P_title">
	<input type="text" id="userid" value="${entity.id}" style="display:none">
	<input type="text" id="sfzjh" value="${entity.sfzjh}" style="display:none">
		
	<c:set var="currentCol" value="${0}"/>
	<c:set var="colNum" value="${3}"/>
	<table style="font-size:12px;">
	<tr>
			    <td>学号</td>
				<td>${entity.xh}</td>
				
				<td>年级名称
				</td>
				<td>${entity.njmc}</td>
				 <td>班级名称</td>
				<td>${entity.bjmch}</td>
				
	</tr>
	<tr>
	<c:forEach var="attr" items="${attr_list}" varStatus="i">
	<td>${attr.label}</td>
	<td>
		<c:choose>
		<c:when test="${attr.type=='text'||attr.type=='date'}">
			${attr.value}
		</c:when>
		<c:when test="${attr.type=='select' || attr.type=='yesno'|| attr.type=='range'}">

			<c:forEach items="${attr.optionList}" var="option" varStatus="status">
			  <c:if test="${attr.value==option.value }">
		        ${option.title}
		        </c:if>
		    </c:forEach>
		</c:when>
		</c:choose>
	</td>
	<c:set var="currentCol" value="${currentCol+1}"/>
	<c:if test="${currentCol==colNum}"><c:set var="currentCol" value="${0}"/></tr><tr></c:if>
	</c:forEach>
	<c:if test="${currentCol!=0&&currentCol < colNum}" >
	<c:forEach begin="${currentCol}" end="${colNum-1}" step="1">
	<td>&nbsp;</td><td>&nbsp;</td>
	</c:forEach>
	</c:if>
	</tr>	
	</table>	
	
</div>
