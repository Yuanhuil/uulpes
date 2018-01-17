<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/organization/sub/ec"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
			<th width="14%">机构代码</th>
			<th width="15%">机构名称</th>
			<c:if test="${currentOrglevel eq 1 }">
			<th width="15%">行政区划</th>
			</c:if>
			<th width="10%">联系电话</th>
			<th width="16%">操作</th>
		</tr>
		<c:forEach var="item" items="${list}">
			<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${item.id }" />
				</td>
				<td>${item.jwdm}</td>
				<td>${item.jwmc}</td>
				<c:if test="${currentOrglevel eq 1 }">
					<td></td>
				</c:if>
				<td>${item.lxdh}</td>
				<td><span class="header02"> 
					<c:choose>
						<c:when test="${item.org.locked eq '1' }">
							<input class="button-small white lock" type="button" chref="${baseaction }/${item.id }/${item.orgid}/unlock.do" value="解锁">
						</c:when>
						<c:otherwise>
							<input class="button-small white lock" type="button" chref="${baseaction }/${item.id }/${item.orgid}/lock.do" value="锁定">
						</c:otherwise>
					</c:choose>
					
					<shiro:hasPermission name="systeminfo:organization:suborganization:update">
					<input class="button-small white edit" type="button" chref="${baseaction }/${item.id }/update.do" value="修改"> 
					</shiro:hasPermission>
					<shiro:hasPermission name="systeminfo:organization:suborganization:delete">
					<input class="button-small white del" type="button" chref="${baseaction }/${item.id }/delete.do"value="删除">
					</shiro:hasPermission>
				</span></td>
			</tr>
		</c:forEach>
	</table>
	
</div>
<div id="pagediv"></div>
<script type="text/javascript">
	$(function(){
		$("#checkid").click(function() {
			$('input[type=checkbox]').prop('checked', $(this).prop('checked'));
		});
		$( ".button-small.white.edit" ).click(function(){
			   var h = $(this).attr("chref");
			   $('#editformdiv').empty();
			   $('#editformdiv').load(h,function(){
				   $( "#suborgdialog" ).dialog("open");
			   });
		   });
		   $( ".button-small.white.del" ).click(function(){
			   var h = $(this).attr("chref");
			   layer.confirm('确定要删除该记录内容吗?', {
					  btn: ['是','否']
					}, function(index){
						$('#content2').load(h);
						layer.close(index);
					}, function(index){
						layer.close(index);
					});
		   });
		   $( ".button-small.white.lock" ).click(function(){
			   var h = $(this).attr("chref");
			   $('#content2').load(h);
		   });
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
