<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/joboverview"/>
<div class="tableContent">
	<table>
		<tr class="titleBg">
					<th><input class="checkbox01" type="checkbox"
			name="checkbox" id="checkid" disabled="disabled"></th>
					<th>标题</th>
					<th>时间</th>
					<th>活动类别</th>
					<th>活动类型</th>
					<th>操作</th>
				</tr>
			<c:forEach items="${list}" var="m">
				<tr>
					<td>
					<c:choose>
						<c:when test="${m.vipEvent == '1'}">
							<input class="checkbox01" type="checkbox" name="rowcheck" disabled="disabled">
						</c:when>
						<c:otherwise>
							<input class="checkbox01" type="checkbox" name="rowcheck" value="${m.id }#${m.tablename}">
						</c:otherwise>
					</c:choose>
					</td>
					<td>${m.title}</td>
					<td><fmt:formatDate value="${m.starttime}" type="both"/>至<fmt:formatDate value="${m.endtime}" type="both"/></td>
					<td>${m.activitycatalog}</td>
					<td>${m.activitytype}</td>
					<td>
						<span class="header02"> 
					<input class="button-small white view" type="button" chref="${baseaction }/${m.tablename}/${m.id }/view.do" value="查看"></span>
					</td>
				</tr>
			</c:forEach>
	</table>
</div>
<div id="pagediv"></div>
<script type="text/javascript">

	$(function() {
		$("#checkid").click(function() {
			$('input[type=checkbox]').prop('checked', $(this).prop('checked'));
		});
		$(".button-small.white.view").click(function() {
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function(response, status, xhr) {
				$("#viewdialog").dialog("open");
			});
		});
		$("#tagvipevent").click(function(){
			  var selectedData = [],noselectedData=[];
			  var selectRow = $("input[name='rowcheck']:checked"),
			      noselectRow = $("input[name='rowcheck']").not("input:checked");
			  if(selectRow.length === 0){
				  layer.open({content:"没有选择相关内容，请选择之后点击【标记为大事记】"});
				  return false;
			  }
			  selectRow.each(function(){
				  selectedData.push(this.value );
			  });
			  noselectRow.each(function(){
				  noselectedData.push(this.value);
			  });
			  $.ajax({
				  url:"${baseaction }/genvipevent.do",
				  data:{rowcheck:selectedData,
					    rownocheck:noselectedData},
				  success:function(data){
						layer.open({content:data});					  
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