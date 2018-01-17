<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<td width="8%">量表编号</td>
			<td width="10%">量表名称</td>
			<td width="18%">量表简称</td>
			<td width="5%">创建时间</td>
		</tr>
		<c:forEach var="item" items="${list}">
   				<tr>
   					<td>${item.code}</td>
   					<td>${item.title}</td>
   					<td>${item.shortname}</td>
   					<td>${item.creationtime}</td>
   					<td><span class="header02"> 
					
				</span></td>
   				</tr>
   			</c:forEach>
	</table>
	
</div>
<div>

<input type="submit" value="download"/>
</form>

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
	        	$("#suborgform").ajaxSubmit({
	        		data:{"currentPage":page},
					target : "#tablelist",
				});
	        },
			
		});
	});
</script>