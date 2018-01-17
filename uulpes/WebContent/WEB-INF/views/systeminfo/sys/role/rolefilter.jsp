<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/role"/>
<form id="roleFilterForm" name="roleFilterForm" method="post"
	action="${baseaction }/searchRole.do">
	<div class="filterContent">
		<ul>
			<li><label class="name03">机构层级 </label> <select
				id="organizationlevel" name="org_level" class="select_160">
					<option value="0">请选择</option>
					<c:forEach var="orglevel" items="${orglevels }">
						<option value="${orglevel.id }">${orglevel.name }</option>
					</c:forEach>
			</select></li>
			<li><label class="name03">角色名称</label> <select id="rolename"
				name="id" class="select_160">
					<option value="0">请选择</option>
					<c:forEach var="role" items="${allRole }">
						<option value="${role.id }">${role.roleName }</option>
					</c:forEach>
			</select></li>
		</ul>
	</div>
	<div class="buttonContent">
		<!--  <input class="button-mid white" type="button" value="删除" onclick="deleteAll();"> -->
		<input class="button-mid white" type="button" value="搜索" id="search" />
		<shiro:hasPermission name="systeminfo:roleauth:create">
		<input class="button-mid white" type="button" value="新增" id="addrole" 
		chref="${baseaction }/alreadyAddRole.do" /></shiro:hasPermission>
	</div>

	<input type="hidden" name="pageNumber" value="1"></input>
</form>
<script>
$(function(){
	if($( "#addrole" ) != null){
		   $( "#addrole" ).on( "click", function() {
			   var h = $(this).attr("chref");
			   $("#editformdiv").html();
			   $("#editformdiv").load(h,function(){
				   $( "#editdiaglog" ).dialog("open");
			   });
		 	}); 
	}
});

function alreadyAddRole(){
	$.ajax({
		   type: "POST",
		   url: "../sys/alreadyAddRole.do",
		   success: function(msg){
			  var optionstr = "";
			  var levelorg = jQuery.parseJSON(msg);
			  for(var i=0;i<levelorg.length;i++){
				  var option="<option value='"+levelorg[i].id+"'>"+levelorg[i].name+"</option>";
				  optionstr = optionstr+option;
				  $("#role_level").html(optionstr);
				  $("#org_level").html(optionstr);
			  }
			  $("#roleName").val("");
			  $("#roleAlias").val("");
			  $("#role").val("");
			   addRole();
		   },
	     error: function()
	     {  	
	   	    layer.open({content:"调用出现错误，删除失败"});
	   	 }
		}); 
}
	$('#search').click(function() {
		var options = {
			target : "#tableContent"
		};
		roleFilterForm.pageNumber.value = 1;
		roleFilterForm.action = "${baseaction }/searchRole.do";
		$('#roleFilterForm').ajaxSubmit(options);
		return false;
	});
	function deleteAll() {
		var checkarray = document.getElementsByName("subcheck");
		var roleids = "";
		for ( var i = 0; i < checkarray.length; i++) {
			if (checkarray[i].checked) {
				roleids = roleids + checkarray[i].value + ",";
			}
		}
		if (roleids == "") {
			layer.open({content:"没有选中角色"});
			return;
		}
		var roleids = roleids.substring(0, roleids.length - 1);
		if (confirm("您确认是否想要批量删除该角色，删除后可能用户资源会受影响！")) {
			$
					.ajax({
						type : "POST",
						url : "${baseaction }/deleteRole.do",
						data : {
							"roleIds" : roleids
						},
						success : function(msg) {
							if (msg != "ok") {
								layer.open({content:"删除失败！"});
							} else {
								layer.open({content:"删除成功"});
								var checkarray = document
										.getElementsByName("subcheck");
								for ( var i = 0; i < checkarray.length; i++) {
									if (checkarray[i].checked) {
										$(checkarray[i]).parent().parent()
												.remove();
										i--;
									}
								}
							}
						},
						error : function() {
							layer.open({content:"调用出现错误，删除失败"});
						}
					});
		}
	}
</script>