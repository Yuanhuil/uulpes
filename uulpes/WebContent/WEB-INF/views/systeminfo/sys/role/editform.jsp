<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>
.roleCheck {
	vertical-align: middle;
	margin-left: 10px;
	margin-right: 2px;
}

.roleli {
	margin-left: 60px;
	margin-top: 5px;
	margin-bottom: 5px;
}
</style>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/role" />
<c:set var="formaction" value="" scope="page" />
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction}/${parentId}/insertRole.do" />
	</c:when>
	<c:when test="${op eq '查看'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="" />
	</c:when>
	<c:when test="${op eq '修改'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction}/${entity.id }/modifyRole.do" />
	</c:when>
	<c:otherwise>
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction}/${parentId}/insertRole.do" />
	</c:otherwise>
</c:choose>
<div id="editdiaglog" title="${empty op ? '新增' : op}角色">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity">
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">角色层级</label> <form:select
						id="role_level" path="role_level" cssClass="select_160">
						<form:option value="0">请选择</form:option>
						<form:options items="${orglevels }" itemLabel="name"
							itemValue="id" />
					</form:select></li>
				<li><label class="name04">机构层级</label> <form:select
						id="org_level" path="org_level" cssClass="select_160">
						<form:option value="0">请选择</form:option>
						<form:options items="${orglevels }" itemLabel="name"
							itemValue="id" />
					</form:select></li>
				<li><label class="name04">机构类型</label> <form:select
						id="roleOrg" path="roleOrg" cssClass="select_160">
						<form:option value="">请选择</form:option>
						<form:option value="1">教委</form:option>
						<form:option value="2">学校</form:option>
						<form:option value="3">陶老师工作站</form:option>
					</form:select></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">角色名称</label> <form:input
						id="roleName" path="roleName" cssClass="input_160"></form:input></li>
				<li><label class="name04">角色别名</label> <form:input id="role"
						path="role" cssClass="input_160"></form:input></li>
				<li><label class="name04">是否显示</label> <form:select id="isShow"
						path="isShow" cssClass="select_160">
						<form:option value="1">是</form:option>
						<form:option value="2">否</form:option>
					</form:select></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">角色类别</label> <form:select
						id="roletypeflag" path="roletypeflag" cssClass="select_160">
						<form:option value="">请选择</form:option>
						<form:option value="1">学生</form:option>
						<form:option value="2">教师</form:option>
						<form:option value="3">家长</form:option>
						<form:option value="4">教委人员</form:option>
					</form:select></li>

				<li><label>是否管理员角色</label> <form:select id="isadmin"
						path="isadmin" cssClass="select_160">
						<form:option value="1">是</form:option>
						<form:option value="2">否</form:option>
					</form:select></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">角色描述</label> <form:input
						id="roleDesc" path="roleDesc" cssClass="input_300"></form:input></li>
			</ul>
		</div>
		<div>角色权限</div>
		<div id="rolePermission" class="role-dlg"></div>
	</form:form>
</div>
<script type="text/javascript">
	var rrp = '${empty rrp ? null : rrp}';
	var allmenus = jQuery.parseJSON('${empty allmenus ? null : allmenus}');
	console.log(allmenus);
	$(function() {
		//先给页面添加权限
		var ulstr = "<ul id='tree'>";
		for (var i = 0; i < allmenus.length; i++) {
			var children = allmenus[i].children;
			ulstr = ulstr
					+ "<li class='roleli' ><span style='vertical-align:middle;' id='"+allmenus[i].id+"'>"
					+ allmenus[i].name
					+ "</span><input type='checkbox' class='roleCheck' level='1' isleaf='" + allmenus[i].isleaf + "' name='1_"+allmenus[i].id+"_2'/><span style='vertical-align:middle;'>添加</span>"
					+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + allmenus[i].isleaf + "' name='1_"+allmenus[i].id+"_4'/><span style='vertical-align:middle;'>删除</span>"
					+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + allmenus[i].isleaf + "' name='1_"+allmenus[i].id+ "_3'/><span style='vertical-align:middle;'>修改</span>"
					+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + allmenus[i].isleaf + "' name='1_"+allmenus[i].id+ "_5'/><span style='vertical-align:middle;'>仅查看</span>"
					+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + allmenus[i].isleaf + "' name='1_"+allmenus[i].id+"_6'/><span style='vertical-align:middle;'>审核</span>"
					+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + allmenus[i].isleaf + "' name='1_"+allmenus[i].id+"_7'/><span style='vertical-align:middle;'>可授权</span>"
					+ "<input type='checkbox' class='roleCheck' level='1' isleaf='" + allmenus[i].isleaf + "' name='1_"+allmenus[i].id+"_8'/><span style='vertical-align:middle;'>标记大事记</span>";
			if (allmenus[i].isleaf != 1) {
				ulstr = ulstr + "<ul>";
			}

			for (var j = 0; j < children.length; j++) {
				var children2 = children[j].children;
				ulstr = ulstr
						+ "<li  class='roleli'><span style='vertical-align:middle;' id='"+children[j].id+"'>"
						+ children[j].name
						+ "</span><input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+allmenus[i].id+"_"+children[j].id+"_2'/><span style='vertical-align:middle;'>添加</span>"
						+ "<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+allmenus[i].id+"_"+children[j].id+"_4'/><span style='vertical-align:middle;'>删除</span>"
						+ "<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+allmenus[i].id+"_"+children[j].id+"_3'/><span style='vertical-align:middle;'>修改</span>"
						+ "<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+allmenus[i].id+"_"+children[j].id+"_5'/><span style='vertical-align:middle;'>仅查看</span>"
						+ "<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+allmenus[i].id+"_"+children[j].id+"_6'/><span style='vertical-align:middle;'>审核</span>"
						+ "<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+allmenus[i].id+"_"+children[j].id+"_7'/><span style='vertical-align:middle;'>可授权</span>"
						+ "<input type='checkbox' class='roleCheck' level='2' isleaf='" + children[j].isleaf + "' name='1_"+allmenus[i].id+"_"+children[j].id+"_8'/><span style='vertical-align:middle;'>标记大事记</span>";
				if (children[j].isleaf != 1) {
					ulstr = ulstr + "<ul>";
				}
				for (var k = 0; k < children2.length; k++) {
					ulstr = ulstr
							+ "<li class='roleli'><span style='vertical-align:middle;' id='"+children2[k].id+"'>"
							+ children2[k].name
							+ "</span><input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+allmenus[i].id+"_"+children[j].id+"_"+children2[k].id+"_2'/><span style='vertical-align:middle;'>添加</span>"
							+ "<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+allmenus[i].id+"_"+children[j].id+"_"+children2[k].id+"_4'/><span style='vertical-align:middle;'>删除</span>"
							+ "<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+allmenus[i].id+"_"+children[j].id+"_"+children2[k].id+"_3'/><span style='vertical-align:middle;'>修改</span>"
							+ "<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+allmenus[i].id+"_"+children[j].id+"_"+children2[k].id+"_5'/><span style='vertical-align:middle;'>仅查看</span>"
							+ "<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+allmenus[i].id+"_"+children[j].id+"_"+children2[k].id+"_6'/><span style='vertical-align:middle;'>审核</span>"
							+ "<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+allmenus[i].id+"_"+children[j].id+"_"+children2[k].id+"_7'/><span style='vertical-align:middle;'>可授权</span>"
							+ "<input type='checkbox' class='roleCheck' level='3' isleaf='"+children2[k].isleaf+"' name='1_"+allmenus[i].id+"_"+children[j].id+"_"+children2[k].id+"_8'/><span style='vertical-align:middle;'>标记大事记</span></li>";
				}
				if (children[j].isleaf != 1) {
					ulstr = ulstr + "</ul>";
				}
				ulstr = ulstr + "</li>";
			}
			if (allmenus[i].isleaf != 1) {
				ulstr = ulstr + "</ul>";
			}
			ulstr = ulstr + "</li>";
		}
		$("#rolePermission").html(ulstr);
		var idsofcheckbox = new Array();
		var allofcheckbox = new Array();
		if (typeof (rrp) !== "undefined" && rrp != null && rrp !== "") {
			rrp = jQuery.parseJSON(rrp);
			for (var j = 0; j < rrp.length; j++) {
				var ress = rrp[j].resId;
				var perms = rrp[j].permIds;
				if (perms.indexOf(",") != -1) {
					var permarray = perms.split(",");
					for (var k = 0; k < permarray.length; k++) {
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
				checkParents(cname);
			});
		}
		$('#tree').abixTreeList();
		//下面开始扫描所有的checkbox标签，一样的则添加上勾选
		function checkParents(cname) {
			var thisnode = $("[name=" + cname + "]");
			var arraycnname = cname.split("_");
			var cid = arraycnname[arraycnname.length - 2]
					+ arraycnname[arraycnname.length - 1];
			var isleaf = thisnode.attr("isleaf");
			var level = thisnode.attr("level");
			var parentid = "";
			for (var i = 0; i < arraycnname.length - 1; i++) {
				parentid = parentid == "" ? arraycnname[i] : parentid + "_"
						+ arraycnname[i];
			}
			var access = arraycnname[arraycnname.length - 1]; //2,3,4,6,7
			//如果还是叶子节点的话
			if (idsofcheckbox.indexOf(cid) != -1 && isleaf == "1") {
				thisnode.prop('checked', true);
				if (level === "3") {
					//如果是3级，则把判断2级父菜单状态，半选或全选
					var parentname = arraycnname[0] + "_" + arraycnname[1]
							+ "_" + arraycnname[2];
					var checkcnt = $("[name^='" + parentname + "_'][name$='_"
							+ access + "']:checked").length;
					var childcnt = $("[name^='" + parentname + "_'][name$='_"
							+ access + "']").length;
					if (checkcnt == childcnt - 1 && checkcnt > 0) {
						$("[name=" + parentname + "_" + access + "]").prop(
								"indeterminate", false);
						$("[name=" + parentname + "_" + access + "]").prop(
								"checked", true);
					} else if ((checkcnt < childcnt) && (checkcnt > 0)) {
						$("[name=" + parentname + "_" + access + "]").prop(
								"indeterminate", true);
					}
					//如果是3级，判断1级父菜单状态，半选或全选
					var grandpaname = arraycnname[0] + "_" + arraycnname[1];
					var gcheckcnt = $("[name^='" + grandpaname + "_'][name$='_"
							+ access + "']:checked").length;
					var gchildcnt = $("[name^='" + grandpaname + "_'][name$='_"
							+ access + "']").length;
					var ghalfcheckcnt = $("[name^='" + grandpaname
							+ "_'][name$='_" + access + "']:indeterminate").length;
					if (gcheckcnt == gchildcnt - 1 && gcheckcnt > 0) {
						$("[name=" + grandpaname + "_" + access + "]").prop(
								"indeterminate", false);
						$("[name=" + grandpaname + "_" + access + "]").prop(
								"checked", true);
					} else if (((gcheckcnt < gchildcnt) && (gcheckcnt > 0))
							|| ghalfcheckcnt > 0) {
						$("[name=" + grandpaname + "_" + access + "]").prop(
								"indeterminate", true);
					}
				}

				//如果是2级，判断1级父菜单状态，半选或全选
				if (level === "2") {
					var parentname = arraycnname[0] + "_" + arraycnname[1];
					var checkcnt = $("[name^=" + parentname + "][name$='_"
							+ access + "']:checked").length;
					var childcnt = $("[name^=" + parentname + "][name$='_"
							+ access + "']").length;
					if (checkcnt == childcnt - 1 && checkcnt > 0) {
						$("[name=" + parentname + "_" + access + "]").prop(
								"indeterminate", false);
						$("[name=" + parentname + "_" + access + "]").prop(
								"checked", true);
					} else if ((checkcnt < childcnt) && (checkcnt > 0)) {
						$("[name=" + parentname + "_" + access + "]").prop(
								"indeterminate", true);
					}
				}
			}
		}

		$(".roleCheck")
				.on(
						"click",
						function() {
							debugger;
							var cname = $(this).attr("name");
							var arraycnname = cname.split("_");
							var level = $(this).attr("level");
							var parentid = "";
							for (var i = 0; i < arraycnname.length - 1; i++) {
								parentid = parentid == "" ? arraycnname[i]
										: parentid + "_" + arraycnname[i];
							}
							var access = arraycnname[arraycnname.length - 1]; //2,3,4,6,7
							if ($(this).prop('checked')) {
								if (level === "3") {
									//如果是3级，则把判断2级父菜单状态，半选或全选
									var parentname = arraycnname[0] + "_"
											+ arraycnname[1] + "_"
											+ arraycnname[2];
									var checkcnt = $("[name^=" + parentname
											+ "_][name$='_" + access
											+ "'][level=3]:checked").length;
									var childcnt = $("[name^=" + parentname
											+ "_][name$='_" + access
											+ "'][level=3]").length;
									if (checkcnt == childcnt && checkcnt > 0) {
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"indeterminate", false);
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"checked", true);
									} else if ((checkcnt < childcnt)
											&& (checkcnt > 0)) {
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"indeterminate", true);
									}
									//如果是3级，判断1级父菜单状态，半选或全选
									var grandpaname = arraycnname[0] + "_"
											+ arraycnname[1];
									var gcheckcnt = $("[name^=" + grandpaname
											+ "_][name$='_" + access
											+ "'][level=2]:checked").length;
									var gchildcnt = $("[name^=" + grandpaname
											+ "_][name$='_" + access
											+ "'][level=2]").length;
									var ghalfcheckcnt = $("[name^='"
											+ grandpaname + "_'][name$='_"
											+ access + "']:indeterminate").length;
									if (gcheckcnt == gchildcnt && gcheckcnt > 0) {
										$(
												"[name=" + grandpaname + "_"
														+ access + "]").prop(
												"indeterminate", false);
										$(
												"[name=" + grandpaname + "_"
														+ access + "]").prop(
												"checked", true);
									} else if (((gcheckcnt < gchildcnt) && (gcheckcnt > 0))
											|| ghalfcheckcnt > 0) {
										$(
												"[name=" + grandpaname + "_"
														+ access + "]").prop(
												"indeterminate", true);
									}
								}

								//如果是2级，判断1级父菜单状态，半选或全选
								if (level === "2") {
									var parentname = arraycnname[0] + "_"
											+ arraycnname[1];
									var name = parentname + "_"
											+ arraycnname[2];
									var checkcnt = $("[name^=" + parentname
											+ "][name$='_" + access
											+ "'][level=2]:checked").length;
									var childcnt = $("[name^=" + parentname
											+ "][name$='_" + access
											+ "'][level=2]").length;
									if (checkcnt == childcnt && checkcnt > 0) {
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"indeterminate", false);
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"checked", true);
									} else if ((checkcnt < childcnt)
											&& (checkcnt > 0)) {
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"indeterminate", true);
									}
									//所有孩子节点都check上
									$(
											"[name^='" + name + "_'][name$='_"
													+ access + "']").prop(
											"checked", true);
								}

								//如果是1级，把所有孩子节点check上
								if (level == "1") {
									var name = arraycnname[0] + "_"
											+ arraycnname[1];
									$(
											"[name^='" + name + "_'][name$='_"
													+ access + "']").prop(
											"checked", true);
								}
							} else {
								if (level === "3") {
									//如果是3级，则把判断2级父菜单状态，半选或取消check
									var parentname = arraycnname[0] + "_"
											+ arraycnname[1] + "_"
											+ arraycnname[2];
									var checkcnt = $("[name^=" + parentname
											+ "_][name$='_" + access
											+ "'][level=3]:checked").length;
									var childcnt = $("[name^=" + parentname
											+ "_][name$='_" + access
											+ "'][level=3]").length;
									if (checkcnt == 0) {
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"indeterminate", false);
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"checked", false);
									} else if ((checkcnt < childcnt)
											&& (checkcnt > 0)) {
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"indeterminate", true);
									}
									//如果是3级，判断1级父菜单状态，半选或全选
									var grandpaname = arraycnname[0] + "_"
											+ arraycnname[1];
									var gcheckcnt = $("[name^=" + grandpaname
											+ "_][name$='_" + access
											+ "'][level=2]:checked").length;
									var gchildcnt = $("[name^=" + grandpaname
											+ "_][name$='_" + access
											+ "'][level=2]").length;
									if (gcheckcnt == 0) {
										$(
												"[name=" + grandpaname + "_"
														+ access + "]").prop(
												"indeterminate", false);
										$(
												"[name=" + grandpaname + "_"
														+ access + "]").prop(
												"checked", false);
									} else if ((gcheckcnt < gchildcnt)
											&& (gcheckcnt > 0)) {
										$(
												"[name=" + grandpaname + "_"
														+ access + "]").prop(
												"indeterminate", true);
									}
								}

								//如果是2级，判断1级父菜单状态，半选或全选
								if (level === "2") {
									var parentname = arraycnname[0] + "_"
											+ arraycnname[1];
									var name = parentname + "_"
											+ arraycnname[2];
									//所有孩子节点都取消check
									$(
											"[name^='" + name + "_'][name$='_"
													+ access + "']").prop(
											"checked", false);
									var checkcnt = $("[name^=" + parentname
											+ "][name$='_" + access
											+ "']:checked").length;
									var childcnt = $("[name^=" + parentname
											+ "][name$='_" + access + "']").length;
									if (checkcnt == 0) {
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"indeterminate", false);
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"checked", false);
									} else if ((checkcnt < childcnt)
											&& (checkcnt > 0)) {
										$(
												"[name=" + parentname + "_"
														+ access + "]").prop(
												"indeterminate", true);
									}
								}

								//如果是1级，把所有孩子节点check上
								if (level == "1") {
									var name = arraycnname[0] + "_"
											+ arraycnname[1];
									$(
											"[name^='" + name + "_'][name$='_"
													+ access + "']").prop(
											"checked", false);
									$(
											"[name^='" + name + "_'][name$='_"
													+ access + "']").prop(
											"indeterminate", false);
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
				$(".roleCheck:checked[isleaf='1']")
						.each(
								function() {
									var resperm = $(this).attr("name");
									var arraycnname = resperm.split("_");
									var resid = arraycnname[arraycnname.length - 2];
									var perids = arraycnname[arraycnname.length - 1];
									if (newPermResourceArray[resid]) {
										newPermResourceArray[resid] = newPermResourceArray[resid]
												+ "," + perids;
									} else {
										newPermResourceArray[resid] = perids;
									}

								});
				$("#editForm").ajaxSubmit({
					target : "#content2",
					data : {
						"perm_resource" : JSON.stringify(newPermResourceArray),
					},
					success : function() {
						$("#editdiaglog").dialog("close");
						layer.open({
							content : "保存成功!"
						});
					},
					error : function() {
						layer.open({
							content : "保存失败"
						});
					}
				});
				$("#editForm").clearForm();
				return false;
			},
			"取消" : function() {
				$("#editdiaglog").dialog("close");
			}
		};
		</c:when>
		<c:when test="${op eq '查看'}">
		buttonsOps = {
			"关闭" : function() {
				$("#editdiaglog").dialog("close");
			}
		};
		</c:when>
		</c:choose>
		$("#editdiaglog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 600,
			width : 800,
			buttons : buttonsOps
		});
	});
</script>