<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="buttonContent">
<shiro:hasPermission name="systeminfo:organization:organization:create">
	<input id="addjobs" class="button-mid white" type="button"
			value="添加主要工作"
			chref="${ctx}/systeminfo/sys/organization/ec/${orgid}/job/add.do"></input>
</shiro:hasPermission>
</div>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th width="20%">工作任务</th>
			<th width="50%">简介</th>
			<th width="30%">操作</th>
		</tr>
		<c:forEach var="item" items="${psychicyJobs}">
			<tr>
				<td>${item.rwmc}</td>
				<td>${item.rwjl}</td>
				<td><span class="header02"> 
					<input class="button-normal white jobviewedit" type="button" chref="${ctx}/systeminfo/sys/organization/ec/job/${item.id }/view.do" value="查看">
					<shiro:hasPermission name="systeminfo:organization:organization:update">
						<input class="button-normal white jobviewedit" type="button" chref="${ctx}/systeminfo/sys/organization/ec/job/${item.id }/update.do" value="修改"> 
					</shiro:hasPermission>
					<shiro:hasPermission name="systeminfo:organization:organization:delete">
						<input class="button-normal white jobdel" type="button" chref="${ctx}/systeminfo/sys/organization/ec/${orgid}/job/${item.id }/delete.do"value="删除">
					</shiro:hasPermission>
				</span></td>
			</tr>
		</c:forEach>
	</table>
</div>
<script>
	$(function() {
		if($( "#addjobs" ) != null){
			   $( "#addjobs" ).button().on( "click", function() {
				   var h = $(this).attr("chref");
				   $('#addjobDiv').html();
				   $('#addjobDiv').load(h,function(){
					   $( "#dialog-job" ).dialog("open");
				   });
			 	}); 
		   }
		//此处有bug 在input中不能使用href属性，因此将href改为chref
		$(".button-normal.white.jobviewedit").click(function() {
			var h = $(this).attr("chref");
			$('#addjobDiv').empty();
			$('#addjobDiv').load(h, function() {
				$("#dialog-job").dialog("open");
			});
		});
		$(".button-normal.white.jobdel").click(function() {
			var h = $(this).attr("chref");
			layer.confirm('确定要删除该记录内容吗?', {
				  btn: ['是','否']
				}, function(index){
					$('#tabs_job').load(h);
					layer.close(index);
				}, function(index){
					layer.close(index);
				});
		});
	})
</script>