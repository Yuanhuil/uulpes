<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
			<th width="10%">真实姓名</th>
			<th width="18%">身份证号</th>
			<th width="5%">性别</th>
			<th width="30%">角色名称</th>
			<th width="20%">操作</th>
		</tr>
		<c:forEach var="item" items="${list}">
			<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${item.id }" />
				</td>
				<td>${item.xm}</td>
				<td>${item.sfzjh}</td>
				<td>${item.sexname}</td>
				<td>
				<c:forEach var="t" items="${item.teacherAuthList}">
					${t.rolename} 
				</c:forEach>
				</td>
				<td><span class="header02"> 
					<a style="color:blue;" href="/pes/archivemanage/archiveTeacher/view.do?id=${item.id }" target="_blank">查看</a>
					<%-- <a style="color:blue;" href="/pes/archivemanage/archiveTeacher/view.do?id=${item.id }&download=yes" target="_blank">查看</a>-->
					
					<%-- <input class="button-normal white download" type="button" id="${item.id }" value="下载"> --%>
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
		$(".button-normal.white.edit").click(function() {
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function() {
				$("#editdialog").dialog("open");
			});
		});
		$(".button-normal.white.del").click(function() {
			var h = $(this).attr("chref");
			$('#content2').load(h);
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
					target : "#list",
				});
			},

		});
	});
</script>