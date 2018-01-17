<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tableContent1">
	<ul id="resourceTree" class="ztree"></ul>
</div>
<div class="rightDiv1" style="height:600px;">
	<iframe id="listFrame" name="listFrame" width="100%" height="100%"
		frameborder="0" scrolling="auto"></iframe>
</div>
<script type="text/javascript">
	$(function() {
		var urlPrefix = "${ctx}/systeminfo/sys/resource";
		var addUrl = urlPrefix + "/ajax/{id}/appendChild.do", renameUrl = urlPrefix
				+ "/ajax/{id}/rename.do?newName={newName}", moveUrl = urlPrefix
				+ "/ajax/{sourceId}/{targetId}/{moveType}/move.do", removeUrl = urlPrefix
				+ "/ajax/{id}/delete.do";
		//用户权限
		var permission = $.extend({
			create : false,
			update : false,
			remove : false,
			move : false
		}, <njpes:treePermission resourceIdentity="systeminfo:resource"/>);
		var setting = {
			async : {
				enable : false
			},
			view : {
				addHoverDom : permission.create ? addHoverDom : null,
				removeHoverDom : permission.create ? removeHoverDom : null,
				selectedMulti : false
			},
			edit : {
				enable : true,
				editNameSelectAll : true,
				showRemoveBtn : permission.remove ? function(treeId, treeNode) {
					return !treeNode.root;
				} : null,
				showRenameBtn : permission.update,
				removeTitle : "移除",
				renameTitle : "重命名",
				drag : {
					isMove : permission.move,
					isCopy : false,
					prev : drop,
					inner : drop,
					next : drop
				}
			},
			data : {
				simpleData : {
					enable : true
				}
			},
			callback : {
				beforeRemove : function(treeId, treeNode) {
					return confirm("确认删除吗？")
				},
				beforeRename : beforeRename,
				onRemove : onRemove,
				onRename : onRename,
				onDrop : onDrop,
				onClick : function(event, treeId, treeNode, clickFlag) {
					$('#listFrame').attr('src',
							urlPrefix + "/" + treeNode.id + "/update.do");
				}
			}

		};
		function drop(treeId, nodes, targetNode) {
			if (!targetNode || !targetNode.getParentNode()) {
				return false;
			}
			for ( var i = 0, l = nodes.length; i < l; i++) {
				if (nodes[i].root === true) {
					return false;
				}
			}
			return true;
		}
		function addHoverDom(treeId, treeNode) {
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_" + treeNode.id).length > 0)
				return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.id
					+ "' title='添加子节点' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_" + treeNode.id);
			if (btn)
				btn.bind("click", function(e) {
					onAdd(e, treeId, treeNode);
					return false;
				});
		}
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_" + treeNode.id).unbind().remove();
		}

		function beforeRename(treeId, treeNode, newName) {
			var oldName = treeNode.name;
			if (newName.length == 0) {
				$.app.alert({
					message : "节点名称不能为空。"
				});
				return false;
			}
			if (!confirm("确认重命名吗？")) {
				var zTree = $.fn.zTree.getZTreeObj(treeId);
				zTree.cancelEditName(treeNode.name);
				return false;
			}
			return true;
		}
		
		 /**
	     * 重命名结束
	     * @param e
	     * @param treeId
	     * @param treeNode
	     */
	    function onRename(e, treeId, treeNode) {
	        var url = renameUrl.replace("{id}", treeNode.id).replace("{newName}",treeNode.name);
	        $.getJSON(url, function (data) {
	        	$.unblockUI();
	        });
	    }
		 
	    /**
	     * 重命名结束
	     * @param e
	     * @param treeId
	     * @param treeNode
	     */
	    function onRemove(e, treeId, treeNode) {
	        var url = removeUrl.replace("{id}", treeNode.id);
	        $.getJSON(url, function (data) {
	        	$.unblockUI();
	        });
	    }
	    /**
	     * 移动结束
	     * @param event
	     * @param treeId
	     * @param treeNodes
	     * @param targetNode
	     * @param moveType
	     * @param isCopy
	     */
	    function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy) {
	        if(!targetNode || treeNodes.length == 0) {
	            return;
	        }
	        var sourceId = treeNodes[0].id;
	        var targetId = targetNode.id;
	        var moveType = moveType;
	        var url = moveUrl.replace("{sourceId}", sourceId).replace("{targetId}", targetId).replace("{moveType}", moveType);
	        $.getJSON(url, function (newNode) {
	        	$.unblockUI();
	        });
	    }
	    
	    /**
	     * 添加新节点
	     * @param e
	     * @param treeId
	     * @param treeNode
	     */
	    function onAdd(e, treeId, treeNode) {
	        var url = addUrl.replace("{id}", treeNode.id);
	        $.getJSON(url, function(newNode) {
	            var node = { id:newNode.id, pId:newNode.pId, name:newNode.name, iconSkin:newNode.iconSkin, open: true,
	                click : newNode.click, root :newNode.root,isParent:newNode.isParent};
	            var zTree = $.fn.zTree.getZTreeObj("resourceTree");
	            var newNode = zTree.addNodes(treeNode, node)[0];
//	            zTree.selectNode(newNode);
	            $("#" + newNode.tId + "_a").click();
	        });
	    }
	    var zNodes = [
			             <c:forEach items='${trees}' var='m'>
			             { id:${m.id}, pId:${m.pId}, name:'${m.name}', iconSkin:'${m.iconSkin}', open: true, root : ${m.root},isParent:${m.isParent}},
			             </c:forEach>
			         ];
	    $.fn.zTree.init($("#resourceTree"), setting, zNodes);
	});
</script>