<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/role"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th width="6%"><input class="checkbox01" type="checkbox"
				name="checkbox" id="checktitle"></th>
			<th width="25%">角色名称</th>
			<th width="20%">角色层级</th>
			<th width="15%">是否显示</th>
			<th width="30%">操作</th>
		</tr>
		<c:forEach var="role" items="${roleList }">
			<tr>
				<td width="6%"><input class="checkbox01" type="checkbox"
					name="subcheck" value="${role.id }"></td>
				<td>${role.roleName}</td>
				<td><c:if test="${role.org_level==1 }">全国</c:if> <c:if
						test="${role.org_level==2 }">省级</c:if> <c:if
						test="${role.org_level==3 }">地市级</c:if> <c:if
						test="${role.org_level==4 }">区县级</c:if> <c:if
						test="${role.org_level==5 }">乡镇级</c:if> <c:if
						test="${role.org_level==6 }">学校</c:if></td>
				<c:choose>
					<c:when test="${role.isShow==1 }">
						<td>是</td>
					</c:when>
					<c:otherwise>
						<td>否</td>
					</c:otherwise>
				</c:choose>
				<td><span class="header02">
					<!-- <input class="button-small white" id="${role.id }" type="button" value="修改" onclick="modifyRole('${role.id }');"> -->
					<shiro:hasPermission name="systeminfo:roleauth:update">
					<input class="button-small white edit" id="${role.id }" type="button" value="修改" 
					chref="${baseaction }/${role.id }/selectRole.do" /> </shiro:hasPermission>
					<c:if test="${role.systemlevel ne '1' }">
					<shiro:hasPermission name="systeminfo:roleauth:delete">
					<input class="button-small white del" id="${role.id }" type="button" value="删除" 
					chref="${baseaction }/${role.id }/deleteRole.do" /></shiro:hasPermission>
					</c:if>
					 
					</span></td>
			</tr>
		</c:forEach>
	</table>
</div>
<div id="pagediv"></div>

<script type="text/javascript">
$(function(){
	$( ".button-small.white.edit" ).click(function(){
		   var h = $(this).attr("chref");
		   $('#editformdiv').empty();
		   $('#editformdiv').load(h,function(){
			   $( "#editdiaglog" ).dialog("open");
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
	$("#pagediv").jstlPaginator({
		showtotalPage : true,
		showgotoPage : true,
		currentPage : "${page.currentPage}",
		totalPages : "${page.totalPage}",
		totalNumbers : "${page.totalResult}",
		onPageClicked : function(event, originalEvent, page) {
			$("#roleFilterForm").ajaxSubmit({
				data : {
					"currentPage" : page
				},
				target : "#tableContent",
			});
		},

	});
	
});
</script>