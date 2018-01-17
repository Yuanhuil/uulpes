<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style type="text/css">
.webuploader-pick {
    background-color: #0095cd;
}

.tableContent {
    width: 99.9%;
    max-height: 486px;
    text-align: center;
    float: initial;
    margin-top: -1px;
}

.chosen-container-multi .chosen-choices {
    overflow-y: auto;
    height: 61px !important;
}

.chosen-container-multi .chosen-choices li.search-choice {
    max-width: 117px;
}

.ui-dialog .ui-dialog-titlebar-close {
    margin: -10px 16px 0 0;
}
</style>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/workschedule/notice" />
<c:choose>
    <c:when test="${op eq '新增'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page"
            value="${baseaction }/create.do" />
    </c:when>
    <c:when test="${op eq '修改'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page"
            value="${baseaction }/${entity.id}/update.do" />
    </c:when>
    <c:otherwise>
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page"
            value="${baseaction }/create.do" />
    </c:otherwise>
</c:choose>
<div id="editdialog" title="${empty op ? '新增' : op}通知公告">
    <form:form action="${formaction}" method="post" id="editForm"
        commandName="entity">
        <form:hidden path="dep" />
        <div class="filterContent">
            <div id="editTitle" class="notice-title">
                <div class="noticedialog-maindiv">
                    <ul>
                        <li class="noticedialog-li"><label>标题名称</label>
                            <form:input id="formtitle" path="title"
                                cssClass="input_160 validate[required]"></form:input>
                        </li>
                        <li class="noticedialog-li"><label>类别</label>
                            <form:select path="catalog"
                                cssClass="select_160 validate[required]">
                                <form:option value="">请选择</form:option>
                                <form:options items="${cataloglist }"
                                    itemValue="id" itemLabel="name" />
                            </form:select></li>
                        <li
                            class="noticedialog-li noticedialog-shareitem"><label>是否共享</label>
                            <form:select path="share"
                                cssClass="select_160 validate[required]">
                                <form:option value="">请选择</form:option>
                                <form:options items="${isno }"
                                    itemValue="id" itemLabel="name" />
                            </form:select></li>
                        <li class="add-newnotice-uploadflie">
                            <div id="uploader" class="wu-example">
                                <div class="btns">
                                    <div id="picker">添加附件</div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <c:if test="${! empty subOrgs }">
                    <div class="noticedialog-subdiv">
                        <label class="noticedialog-label">下发单位</label>
                        <form:select path="org_ids" items="${subOrgs }"
                            cssClass="select_160"
                            cssStyle="width:600px;" itemValue="id"
                            itemLabel="name" multiple="multiple"></form:select>
                    </div>
                </c:if>
            </div>
            <div id="editor" class="notice-editor">
                <form:textarea path="content" cols="40" rows="8"
                    cssClass="validate[required]"></form:textarea>
            </div>
            <div id="editFileList" class="tableContent edit-filelist">
                <%@include file="../filelist.jsp"%>
            </div>
            <div id="editNoticeNext" class="editdialog-next">
                <div class='next-step'>
                    <button type="button" role="button"
                        class="notice-next"
                        onclick="showNoticeNextAction();">下一步</button>
                </div>
            </div>
        </div>
    </form:form>
</div>
<script type="text/javascript">
	$(function(){
		$.getScript("/pes/js/jobfileupload.js");
	 	$("#org_ids").chosen({
	 	    width: "744px",
	 	    disable_search_threshold: 10,
	 	    no_results_text: "没有结果匹配",
	 	    placeholder_text_multiple: "请选择单位",
	 	    display_selected_options: false
	 	});
	 	if ($(".noticedialog-subdiv").is(":visible")) {
	 	    $(".tableContent").css("max-height","369px");
	 	}
		var buttonsOps = {};
		$("#editdialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 700,
			width : 900,
			position: { my: "top", at: "top", of: "#topHeader" },
			buttons : buttonsOps
		});
 	 	$("#editor").hide();
		$("#editNoticePrev").hide();
	});
	function showNoticeNextAction() {
	    var buttonsOps = {};
		<c:choose>
		<c:when test="${empty op || op eq '新增' || op eq '修改'}">
		buttonsOps = {
			"上一步": function() {
        		$("#editTitle").show();
        		$("#editFileList").show();
        		$("#editNoticeNext").show();
				$("textarea#content").ckeditorGet().destroy();
    			$("#editor").hide();
        	  	var buttonsOps = {};
        		$("#editdialog").dialog({
        			appendTo : "#editformdiv",
        			autoOpen : false,
        			modal : true,
        			height : 700,
        			width : 900,
        			buttons : buttonsOps
        		});
            },
	        <c:if test="${empty entity.state || entity.state eq '1' || entity.state eq '4'}">
				"提交": function(){
	        		if (!$("#editForm").validationEngine('validate'))
	    				return false;
	    			$("#editForm").ajaxSubmit({
	    				target : "#content2",
	    				data:{"state":2},
	    				success : function() {
	    					$("#editdialog").dialog("close");
	    					$("#content2").noty({
	    			            text        : "保存成功",
	    			            type        : "success",
	    			            timeout     : 2000,
	    			            closeWith   : ['click'],
	    			            layout      : 'topCenter',
	    			            theme       : 'defaultTheme',
	    			        });
	    				},
	    				error : function() {
	    					$("#content2").noty({
	    			            text        : "保存失败",
	    			            type        : "error",
	    			            timeout     : 2000,
	    			            closeWith   : ['click'],
	    			            layout      : 'topCenter',
	    			            theme       : 'defaultTheme',
	    			        });
	    				}
	    			});
	    			$("#editForm").clearForm();
	        	},
	        	</c:if>
				"保存": function(){
	        		if (!$("#editForm").validationEngine('validate'))
	    				return false;
	    			$("#editForm").ajaxSubmit({
	    				target : "#content2",
	    				data:{"state":1},
	    				success : function() {
	    					$("#editdialog").dialog("close");
	    					$("#content2").noty({
	    			            text        : "保存成功",
	    			            type        : "success",
	    			            timeout     : 2000,
	    			            closeWith   : ['click'],
	    			            layout      : 'topCenter',
	    			            theme       : 'defaultTheme',
	    			        });
	    				},
	    				error : function() {
	    					$("#content2").noty({
	    			            text        : "保存失败",
	    			            type        : "error",
	    			            timeout     : 2000,
	    			            closeWith   : ['click'],
	    			            layout      : 'topCenter',
	    			            theme       : 'defaultTheme',
	    			        });
	    				}
	    			});
	    			$("#editForm").clearForm();
	    			return false;
	        	},
	            "取消": function() {
	            	$( "#editdialog" ).dialog( "close" );
	            }
		};
		</c:when>
		</c:choose>
		$("#editdialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 700,
			width : 900,
			buttons : buttonsOps
		});
		$("textarea#content").ckeditor();
		$("#editTitle").hide();
		$("#editFileList").hide();
		$("#editNoticeNext").hide();
	  	$("#editor").show();
	}
</script>
