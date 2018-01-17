<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="tableContent">
	<table class="table-fixed">
		<tr class="titleBg">
			<th width="10%">操作人</th>
			<th width="40%">操作模块</th>
			<th width="30%">操作内容</th>
			<th width="20%">操作时间</th>
		</tr>
		<c:forEach var="syslog" items="${syslogList }">
			<tr>
				<td>${syslog.operator }</td>
				<td>${syslog.menuname}</td>
				<td>${syslog.content }</td>
				
				<td><fmt:formatDate value="${syslog.optime}" pattern="yyyy-MM-dd HH:mm:ss"/>   </td>
			</tr>
		</c:forEach>
	</table>
</div>
<div id="pagediv"></div>
<script type="text/javascript">
	$(function(){
		$("#pagediv").jstlPaginator({
			showtotalPage:true,
			showgotoPage:true,
	        currentPage: "${page.currentPage}",
	        totalPages: "${page.totalPage}",
	        totalNumbers:"${page.totalResult}",
	        onPageClicked: function(event, originalEvent, page){
	        	$("#syslogFilterForm").ajaxSubmit({
	        		data:{"currentPage":page},
					target : "#tableContent",
				});
	        },
			
		});
	});
</script>