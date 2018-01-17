<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div class="tableContent">
	<table>
		<tr class="titleBg">
		    <th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkid" ></th>
			<th width="10%">学籍号</th>
			<th width="15%">真实姓名</th>
			<th width="35%">身份证号</th>
			<th width="10%">性别</th>
			<th width="30%">操作</th>
		</tr>
		<c:forEach var="item" items="${list}">
			<tr>
			    <td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${item.id }" />
				</td>
				<td>${item.xjh}</td>
				<td>${item.xm}</td>
				<td>${item.sfzjh}</td>
				<td>
				<c:if test="${item.xbm eq 1}">男</c:if>
				<c:if test="${item.xbm eq 2}">女</c:if></td>
				<td><span class="header02"> 
					
					<!-- <a style="color:blue;" href="/pes/archivemanage/archiveGraduateStudent/view.do?id=${item.id }" target="_blank">查看</a>-->
					<a style="color:blue;" href="javascript:void(0);" onclick="viewachive('${item.id }')" target="_blank">查看</a>
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
		
		$("#pagediv").jstlPaginator({
			showtotalPage:true,
			showgotoPage:true,
	        currentPage: "${page.currentPage}",
	        totalPages: "${page.totalPage}",
	        totalNumbers:"${page.totalResult}",
	        onPageClicked: function(event, originalEvent, page){
	        	$("#queryform").ajaxSubmit({
	        		data:{"currentPage":page},
					target : "#list",
				});
	        },
			
		});
	});
	jQuery('.view')
			.bind(
					'click',
					function() {
					debugger;
						
						var userid = jQuery(this).attr("id");
						var h = '/pes/archivemanage/archiveStudent/view.do?id='
								+ userid ;
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#tab-container").dialog("open");
						});
					});
	function viewachive(id){
		debugger;
		var achiveform = $("#achiveform"); 
			//var achiveform = $("#achiveform"); 
			//var achiveform = $("<form></form>"); 
			//var editDiv = $("#editDiv");
			//achiveform.append(editDiv);
			achiveform.attr('id',"achiveform");
			//achiveform.attr('action',"/pes/archivemanage/archiveGraduatedStudent/view.do");  
			achiveform.attr('target', '_blank');   
		    var id_input = $('<input type="hidden" name="id" />');  
		    id_input.attr('value', id);  
		    // 附加到Form  
		    achiveform.append(id_input);  
		    // 提交表单  
		    achiveform.submit();  
		    return false;  


	}
</script>