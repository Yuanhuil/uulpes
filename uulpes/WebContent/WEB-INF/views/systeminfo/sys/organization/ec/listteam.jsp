<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="buttonContent">
	<shiro:hasPermission name="systeminfo:organization:organization:create">
	<input id="addteams" class="button-mid white" type="button"
			value="添加心理队伍"
			chref="${ctx}/systeminfo/sys/organization/ec/${orgid}/team/add.do"></input>
	</shiro:hasPermission>
</div>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th width="15%">姓名</th>
			<th width="55%">简介</th>
			<th width="30%">操作</th>
		</tr>
		<c:forEach var="item" items="${ecpsychicteams}">
			<tr>
				<td>${item.xm}</td>
				<td>${item.grjl}</td>
				<td><span class="header02"> <input
						class="button-normal white teamviewedit" type="button"
						chref="${ctx}/systeminfo/sys/organization/ec/team/${item.id }/view.do"
						value="查看"> 
						<shiro:hasPermission name="systeminfo:organization:organization:update">
							<input class="button-normal white teamviewedit" type="button"
							chref="${ctx}/systeminfo/sys/organization/ec/team/${item.id }/update.do"
							value="修改">	 
        				</shiro:hasPermission>
        				<shiro:hasPermission name="systeminfo:organization:organization:delete"> 
	        				<input class="button-normal white teamdel"
							type="button"
							chref="${ctx}/systeminfo/sys/organization/ec/${orgid}/team/${item.id }/delete.do"
							value="删除">  
        				</shiro:hasPermission> 
				</span></td>
			</tr>
		</c:forEach>
	</table>
	<%-- <shiro:hasPermission name="systeminfo:organization:update">
   			
        </shiro:hasPermission> --%>
</div>
<script type="text/javascript">
   $(function(){
	   if($( "#addteams" ) != null){
		   $( "#addteams" ).button().on( "click", function() {
			   var h = $(this).attr("chref");
			   $('#addteamDiv').html();
			   $('#addteamDiv').load(h,function(){
				   $( "#dialog-form" ).dialog("open");
			   });
		 	}); 
	   }
	 //此处有bug 在input中不能使用href属性，因此将href改为chref
		$( ".button-normal.white.teamviewedit" ).click(function(){
			   var h = $(this).attr("chref");
			   $('#addteamDiv').empty();
			   $('#addteamDiv').load(h,function(){
				   $( "#dialog-form" ).dialog("open");
			   });
		   });
		   $( ".button-normal.white.teamdel" ).click(function(){
			   var h = $(this).attr("chref");
			   layer.confirm('确定要删除该记录内容吗?', {
					  btn: ['是','否']
					}, function(index){
			   			$('#tabs_team').load(h);
			   			layer.close(index);
					}, function(index){
						layer.close(index);
					});
		   });
   })
   
   </script>