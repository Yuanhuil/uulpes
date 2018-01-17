<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/user/ecuser"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
			<th width="8%">真实姓名</th>
			<th width="8%">用户名</th>
			<th width="18%">身份证号</th>
			<th width="5%">性别</th>
			<th width="20%">机构名称</th>
			<th width="18%">角色名称</th>
			<th width="27%">操作</th>
		</tr>
		<c:forEach var="item" items="${list}">
			<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${item.id }" />
				</td>
				<td>${item.xm}</td>
				<td>${item.username}</td>
				<td>${item.sfzjh}</td>
				<td><c:if test="${item.xbm eq 1}"> 男</c:if><c:if test="${item.xbm eq 2}"> 女</c:if></td>
				<td>${item.orgName}</td>
				<td>${item.rolename}</td>
				<td><span class="header02"> 
				<shiro:hasPermission name="systeminfo:user:ecuser:update">
					<input class="button-small white edit" type="button" chref="${baseaction }/${item.id }/update.do" value="修改"> 
				</shiro:hasPermission>
				<shiro:hasPermission name="systeminfo:user:ecuser:delete">
					<input class="button-small white del" type="button" chref="${baseaction }/${item.id }/delete.do" value="删除">
				</shiro:hasPermission>
				<shiro:hasPermission name="systeminfo:user:ecuser:authorize">
					<input class="button-normal white auth" type="button" chref="${ctx }/systeminfo/sys/user/${item.accountId }/userauth.do" value="授权">
				</shiro:hasPermission>
				<input class="button-normal white resetpasswd" type="button" chref="${item.id }" value="密码重置">
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
		$(".button-small.white.edit").click(function() {
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function() {
				$("#editdialog").dialog("open");
			});
		});
		$(".button-normal.white.auth").click(function() {
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function() {
				$("#authdialog").dialog("open");
			});
		});
		$(".button-small.white.del").click(function() {
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
		$(".button-normal.white.resetpasswd").click(function() {
			var id = $(this).attr("chref");
			$.ajax({
				type : "POST",
				url : "/pes/systeminfo/sys/user/ecuser/resetecuserpasswd.do",
				data : {
					id:id
				},
				success : function(msg) {
					layer.open({content:"已成功重置密码"});
				},
				error : function() {
					layer.open({content:"无法重置密码"});
				}
			});
		});
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
					target : "#tablelist",
				});
			},

		});
	});
</script>