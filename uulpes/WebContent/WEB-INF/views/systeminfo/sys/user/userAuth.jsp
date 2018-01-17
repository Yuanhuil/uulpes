<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>

.roleCheck{
  vertical-align:middle;
  margin-left: 10px;
  margin-right: 2px;
}
.roleli{
	margin-left:60px;
	margin-top: 5px;
	margin-bottom: 5px;
}

</style>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/systeminfo/sys/user"/>
<form:form id="editForm" action="${baseaction }/${user.id }/userperm.do">
<div id="authdialog" title="授权给${user.realname }">
	<div id="rolePermission">
		
	</div>
</div>
</form:form>
<script type="text/javascript">
var rrp = '${empty rrp ? null : rrp}';
var urp = '${empty urp ? null : urp}';
$(function(){
	//先给页面添加权限
	var ulstr = "<ul id='tree'>";
	for ( var i = 0; i < menus.length; i++) {
		var children = menus[i].children;
		ulstr = ulstr
				+ "<li  class='roleli'><span style='vertical-align:middle;'  id='"+menus[i].id+"'>"+ menus[i].name
				+ "</span><input type='checkbox' class='roleCheck' level='1' isleaf='" + menus[i].isleaf + "' name='1_"+menus[i].id+"_2'/><span style='vertical-align:middle;'>添加</span>"
				+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + menus[i].isleaf + "' name='1_"+menus[i].id+"_4'/><span style='vertical-align:middle;'>删除</span>"
				+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + menus[i].isleaf + "' name='1_"+menus[i].id+ "_3'/><span style='vertical-align:middle;'>修改</span>"
				+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + menus[i].isleaf + "' name='1_"+menus[i].id+"_6'/><span style='vertical-align:middle;'>审核</span>"
				+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + menus[i].isleaf + "' name='1_"+menus[i].id+"_7'/><span style='vertical-align:middle;'>可授权</span>";
		if(menus[i].isleaf !=1){
			ulstr = ulstr +"<ul>";
		}
		for ( var j = 0; j < children.length; j++) {
			var children2 = children[j].children;
			ulstr = ulstr
					+ "<li class='roleli'><span style='vertical-align:middle;'  id='"+children[j].id+"'>"+ children[j].name
					+ "</span><input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+menus[i].id+"_"+children[j].id+"_2'/><span style='vertical-align:middle;'>添加</span>"
					+ "<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+menus[i].id+"_"+children[j].id+"_4'/><span style='vertical-align:middle;'>删除</span>"
					+"<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+menus[i].id+"_"+children[j].id+"_3'/><span style='vertical-align:middle;'>修改</span>"
					+"<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+menus[i].id+"_"+children[j].id+"_6'/><span style='vertical-align:middle;'>审核</span>"
					+"<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+menus[i].id+"_"+children[j].id+"_7'/><span style='vertical-align:middle;'>可授权</span>";
			if(children[j].isleaf !=1){
				ulstr = ulstr +"<ul>";
			}
			for ( var k = 0; k < children2.length; k++) {
				ulstr = ulstr
						+ "<li class='roleli'><span style='vertical-align:middle;' id='"+children2[k].id+"'>"+ children2[k].name
						+ "</span><input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+menus[i].id+"_"+children[j].id+"_"+children2[k].id+"_2'/><span style='vertical-align:middle;'>添加</span>"
						+"<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+menus[i].id+"_"+children[j].id+"_"+children2[k].id+"_4'/><span style='vertical-align:middle;'>删除</span>"
						+"<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+menus[i].id+"_"+children[j].id+"_"+children2[k].id+"_3'/><span style='vertical-align:middle;'>修改</span>"
						+"<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+menus[i].id+"_"+children[j].id+"_"+children2[k].id+"_6'/><span style='vertical-align:middle;'>审核</span>"
						+"<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+menus[i].id+"_"+children[j].id+"_"+children2[k].id+"_7'/><span style='vertical-align:middle;'>可授权</span></li>";
			}
			if(children[j].isleaf !=1){
				ulstr = ulstr + "</ul>";
			}
			ulstr = ulstr + "</li>";
		}
		if(menus[i].isleaf !=1){
			ulstr = ulstr + "</ul>";
		}
		ulstr = ulstr + "</li>";
	}
	$("#rolePermission").html(ulstr);
	var idsofcheckbox = new Array();
	var allofcheckbox = new Array();
	if(typeof(rrp) !== "undefined" && rrp !=null && rrp !== ""){
		rrp = jQuery.parseJSON(rrp);
		for ( var j = 0; j < rrp.length; j++) {
			var ress = rrp[j].resId;
			var perms = rrp[j].permIds;
			if (perms.indexOf(",")!=-1) {
				var permarray = perms.split(",");
				for ( var k = 0; k < permarray.length; k++) {
					var perm = permarray[k];
					//资源和操作字符串并成一个字符串
					var resperm = ress + perm;
					idsofcheckbox.push(resperm);
					allofcheckbox.push(resperm);
				}
			} else {
				var resperm = ress + perms;
				idsofcheckbox.push(resperm);
				allofcheckbox.push(resperm);
			}
		}
		$(".roleCheck").each(function() {
			var cname = $(this).attr("name");
			checkParents(cname,1);
		});
	}
	if(typeof(urp) !== "undefined" && urp !=null && urp !== ""){
		urp = jQuery.parseJSON(urp);
		for ( var j = 0; j < urp.length; j++) {
			var ress = urp[j].resId;
			var perms = urp[j].permIds;
			if (perms.indexOf(",")!=-1) {
				var permarray = perms.split(",");
				for ( var k = 0; k < permarray.length; k++) {
					var perm = permarray[k];
					//资源和操作字符串并成一个字符串
					var resperm = ress + perm;
					idsofcheckbox.push(resperm);
					allofcheckbox.push(resperm);
				}
			} else {
				var resperm = ress + perms;
				idsofcheckbox.push(resperm);
				allofcheckbox.push(resperm);
			}
		}
		$(".roleCheck").each(function() {
			var cname = $(this).attr("name");
			checkParents(cname,0);
		});
	}
	$('#tree').abixTreeList();
	//下面开始扫描所有的checkbox标签，一样的则添加上勾选
	function checkParents(cname,flag){
		var thisnode = $("[name="+cname+"]");
		var arraycnname = cname.split("_");
		var cid = arraycnname[arraycnname.length-2]+arraycnname[arraycnname.length-1];
		var isleaf = thisnode.attr("isleaf");
		var level = thisnode.attr("level");
		var parentid = "";
		for(var i=0;i<arraycnname.length-1;i++){
			parentid = parentid==""?arraycnname[i]:parentid+"_"+arraycnname[i];
		}
		var access = arraycnname[arraycnname.length-1];   //2,3,4,6,7
		//如果还是叶子节点的话
		if (idsofcheckbox.indexOf(cid) != -1&&isleaf=="1"){
			thisnode.prop('checked', true);
			if(flag === 1)
				thisnode.prop('disabled','disabled');
			if(level==="3"){
				//如果是3级，则把判断2级父菜单状态，半选或全选
				var parentname = arraycnname[0] + "_" + arraycnname[1] + "_" + arraycnname[2];
				var checkcnt = $("[name^='"+parentname+"_'][name$='_"+access+"']:checked").length;
				var childcnt = $("[name^='"+parentname+"_'][name$='_"+access+"']").length;
				if( checkcnt == childcnt-1 && checkcnt>0){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",false);
					$("[name="+parentname + "_"+access+"]").prop("checked",true);
					$("[name="+parentname + "_"+access+"]").prop("disabled",'disabled');
				}else if((checkcnt < childcnt) && (checkcnt >0)){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",true);
					$("[name="+parentname + "_"+access+"]").prop("disabled",'disabled');
				}
				//如果是3级，判断1级父菜单状态，半选或全选
				var grandpaname = arraycnname[0] + "_" + arraycnname[1];
				var gcheckcnt = $("[name^='"+grandpaname+"_'][name$='_"+access+"']:checked").length;
				var gchildcnt = $("[name^='"+grandpaname+"_'][name$='_"+access+"']").length;
				if( gcheckcnt == gchildcnt-1 && gcheckcnt>0){
					$("[name="+grandpaname + "_"+access+"]").prop("indeterminate",false);
					$("[name="+grandpaname + "_"+access+"]").prop("checked",true);
					$("[name="+grandpaname + "_"+access+"]").prop('disabled','disabled');
				}else if((gcheckcnt < gchildcnt) && (gcheckcnt >0)){
					$("[name="+grandpaname + "_"+access+"]").prop("indeterminate",true);
					$("[name="+grandpaname + "_"+access+"]").prop('disabled','disabled');
				}
			}
			
			//如果是2级，判断1级父菜单状态，半选或全选
			if(level==="2"){
				var parentname = arraycnname[0] + "_" + arraycnname[1];
				var checkcnt = $("[name^="+parentname+"][name$='_"+access+"']:checked").length;
				var childcnt = $("[name^="+parentname+"][name$='_"+access+"']").length;
				if( checkcnt == childcnt-1 && checkcnt>0){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",false);
					$("[name="+parentname + "_"+access+"]").prop("checked",true);
					$("[name="+parentname + "_"+access+"]").prop('disabled','disabled');
				}else if((checkcnt < childcnt) && (checkcnt >0)){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",true);
					$("[name="+parentname + "_"+access+"]").prop('disabled','disabled');
				}
			}
		}
	}
	

	$(".roleCheck").on("click", function() {
		var cname = $(this).attr("name");
		var arraycnname = cname.split("_");
		var level = $(this).attr("level");
		var parentid = "";
		for(var i=0;i<arraycnname.length-1;i++){
			parentid = parentid==""?arraycnname[i]:parentid+"_"+arraycnname[i];
		}
		var access = arraycnname[arraycnname.length-1];   //2,3,4,6,7
		if ($(this).prop('checked')){
			if(level==="3"){
				//如果是3级，则把判断2级父菜单状态，半选或全选
				var parentname = arraycnname[0] + "_" + arraycnname[1] + "_" + arraycnname[2];
				var checkcnt = $("[name^="+parentname+"_][name$='_"+access+"'][level=3]:checked").length;
				var childcnt = $("[name^="+parentname+"_][name$='_"+access+"'][level=3]").length;
				if( checkcnt == childcnt && checkcnt>0){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",false);
					$("[name="+parentname + "_"+access+"]").prop("checked",true);
				}else if((checkcnt < childcnt) && (checkcnt >0)){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",true);
				}
				//如果是3级，判断1级父菜单状态，半选或全选
				var grandpaname = arraycnname[0] + "_" + arraycnname[1];
				var gcheckcnt = $("[name^="+grandpaname+"_][name$='_"+access+"'][level=2]:checked").length;
				var gchildcnt = $("[name^="+grandpaname+"_][name$='_"+access+"'][level=2]").length;
				if( gcheckcnt == gchildcnt && gcheckcnt>0){
					$("[name="+grandpaname + "_"+access+"]").prop("indeterminate",false);
					$("[name="+grandpaname + "_"+access+"]").prop("checked",true);
				}else if((gcheckcnt < gchildcnt) && (gcheckcnt >0)){
					$("[name="+grandpaname + "_"+access+"]").prop("indeterminate",true);
				}
			}
			
			//如果是2级，判断1级父菜单状态，半选或全选
			if(level==="2"){
				var parentname = arraycnname[0] + "_" + arraycnname[1];
				var name = parentname + "_" + arraycnname[2];
				var checkcnt = $("[name^="+parentname+"][name$='_"+access+"'][level=2]:checked").length;
				var childcnt = $("[name^="+parentname+"][name$='_"+access+"'][level=2]").length;
				if( checkcnt == childcnt && checkcnt>0){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",false);
					$("[name="+parentname + "_"+access+"]").prop("checked",true);
				}else if((checkcnt < childcnt) && (checkcnt >0)){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",true);
				}
				//所有孩子节点都check上
				$("[name^='"+name+"_'][name$='_"+access+"']").prop("checked",true);
			}
			
			//如果是1级，把所有孩子节点check上
			if(level == "1"){
				var name = arraycnname[0] + "_" + arraycnname[1];
				$("[name^='"+name+"_'][name$='_"+access+"']").prop("checked",true);
			}
		}else{
			if(level==="3"){
				//如果是3级，则把判断2级父菜单状态，半选或取消check
				var parentname = arraycnname[0] + "_" + arraycnname[1] + "_" + arraycnname[2];
				var checkcnt = $("[name^="+parentname+"_][name$='_"+access+"'][level=3]:checked").length;
				var childcnt = $("[name^="+parentname+"_][name$='_"+access+"'][level=3]").length;
				if( checkcnt==0){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",false);
					$("[name="+parentname + "_"+access+"]").prop("checked",false);
				}else if((checkcnt < childcnt) && (checkcnt >0)){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",true);
				}
				//如果是3级，判断1级父菜单状态，半选或全选
				var grandpaname = arraycnname[0] + "_" + arraycnname[1];
				var gcheckcnt = $("[name^="+grandpaname+"_][name$='_"+access+"'][level=2]:checked").length;
				var gchildcnt = $("[name^="+grandpaname+"_][name$='_"+access+"'][level=2]").length;
				if( gcheckcnt==0){
					$("[name="+grandpaname + "_"+access+"]").prop("indeterminate",false);
					$("[name="+grandpaname + "_"+access+"]").prop("checked",false);
				}else if((gcheckcnt < gchildcnt) && (gcheckcnt >0)){
					$("[name="+grandpaname + "_"+access+"]").prop("indeterminate",true);
				}
			}
			
			//如果是2级，判断1级父菜单状态，半选或全选
			if(level==="2"){
				var parentname = arraycnname[0] + "_" + arraycnname[1];
				var name = parentname + "_" + arraycnname[2];
				//所有孩子节点都取消check
				$("[name^='"+name+"_'][name$='_"+access+"']").prop("checked",false);
				var checkcnt = $("[name^="+parentname+"][name$='_"+access+"']:checked").length;
				var childcnt = $("[name^="+parentname+"][name$='_"+access+"']").length;
				if( checkcnt==0){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",false);
					$("[name="+parentname + "_"+access+"]").prop("checked",false);
				}else if((checkcnt < childcnt) && (checkcnt >0)){
					$("[name="+parentname + "_"+access+"]").prop("indeterminate",true);
				}
			}
			
			//如果是1级，把所有孩子节点check上
			if(level == "1"){
				var name = arraycnname[0] + "_" + arraycnname[1];
				$("[name^='"+name+"_'][name$='_"+access+"']").prop("checked",false);
				$("[name^='"+name+"_'][name$='_"+access+"']").prop("indeterminate",false);
			}
		}
	});
	
	//-------------------------------------
	var buttonsOps = {};
	var newPermResourceArray = {};
		<c:choose>
		<c:when test="${empty op || op eq '新增' || op eq '修改'}">
		buttonsOps = {
			"保存" : function() {
				if (!$("#editForm").validationEngine('validate'))
					return false;
				$(".roleCheck:checked[isleaf='1'][disabled!='disabled']").each(
						function() {
							var resperm = $(this).attr("name");
							var arraycnname = resperm.split("_");
							var resid = arraycnname[arraycnname.length - 2];
							var perids = arraycnname[arraycnname.length - 1];
							if(newPermResourceArray[resid]){
								newPermResourceArray[resid] = newPermResourceArray[resid] + "," + perids;
							}else{
								newPermResourceArray[resid] = perids;
							}
							
				});
				debugger;
				$("#editForm").ajaxSubmit({
					data:{
						 "perm_resource":JSON.stringify(newPermResourceArray),
					},
					success : function(data) {
						$("#editdiaglog").dialog("close");
						layer.open({content:data});
					},
					error : function() {
						$("#editdiaglog").dialog("close");
						layer.open({content:"保存失败"});
					}
				});
				$("#editForm").clearForm();
				return false;
			},
			"取消" : function() {
				$("#authdialog").dialog("close");
			}
		};
		</c:when>
		</c:choose>
		$("#authdialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 500,
			width : 700,
			buttons : buttonsOps
		});
});

</script>