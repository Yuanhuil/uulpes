<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style type="text/css">
.webuploader-pick {
    background-color: #0095cd;
    margin: -10px 0px;
}

.tableContent {
    width: 100%;
    height: 399px;
    text-align: center;
    float: initial;
    margin-top: -5px;
}
</style>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/workschedule/activityplan" />
<c:choose>
    <c:when test="${op eq '新增'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page" value="${baseaction }/create.do" />
    </c:when>
    <c:when test="${op eq '查看'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page" value="" />
    </c:when>
    <c:when test="${op eq '修改'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page" value="${baseaction }/${entity.id}/update.do" />
    </c:when>
    <c:otherwise>
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page" value="${baseaction }/create.do" />
    </c:otherwise>
</c:choose>
<div id="editdialog" title="${empty op ? '新增' : op}活动计划">
    <form:form action="${formaction}" method="post" id="editForm" commandName="entity" enctype="multipart/form-data">
        <div id="activityPlanContent" class="filterContent">
            <div id="editTitle" class="notice-title">
                <ul class="activityplan-dialog-ul">
                    <li class="activityplan-dialog-li">
                        <label>标题名称</label>
                        <form:input id="formtitle" path="title" cssClass="input_160"></form:input>
                    </li>
                    <li class="activityplan-dialog-li">
                        <label>学年</label>
                        <form:input path="schoolyear" cssClass="input_160" readonly="true"></form:input>
                    </li>
                    <li class="activityplan-dialog-li">
                        <label>学期</label>
                        <form:select path="term" cssClass="select_160">
                            <form:option value="">请选择</form:option>
                            <form:options items="${schoolterm }" itemValue="id" itemLabel="name" />
                        </form:select>
                    </li>
                </ul>
                <ul class="activityplan-dialog-ul">
                    <li class="activityplan-dialog-li">
                        <label>活动类别</label>
                        <form:select id="editactivitycatalog" path="activitycatalog" data-placeholder="选择活动类别"
                            cssClass="select_160">
                            <form:option value="">请选择</form:option>
                            <form:options items="${plancatalog }" itemValue="id" itemLabel="name" />
                        </form:select>
                    </li>
                    <li class="activityplan-dialog-li">
                        <label>活动类型</label>
                        <form:select id="editactivitytype" path="activitytype" data-placeholder="选择活动类型"
                            cssClass="select_160">
                            <form:option value="">请选择</form:option>
                            <form:options items="${plantype}" itemValue="id" itemLabel="name" />
                        </form:select>
                    </li>
                    <li class="activityplan-dialog-li">
                        <div id="uploader" class="wu-example">
                            <div class="btns">
                                <div id="picker">添加附件</div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        <div id="activityPlanEditor" class="notice-editor activityPlanEditor">
            <form:textarea path="content" cols="80" class="initialization" rows="8"></form:textarea>
        </div>
        <div id="editFileList" class="tableContent edit-filelist">
            <%@include file="../filelist.jsp"%>
        </div>
        <div id="editActivityPlanNext" class="editdialog-next">
            <div class='next-step'>
                <button type="button" role="button" class="notice-next" onclick="showActivityPlanNextAction();">
                    下一步</button>
            </div>
        </div>
        </div>
    </form:form>
</div>
<script type="text/javascript">
	$(function(){
		$.getScript("/pes/js/jobfileupload.js");
		$("#editactivitycatalog").change(function(){
			var p1=$(this).children("option:selected").val();
			$.ajax({
				url:'${baseaction}/getType.do',
				data:{'catid':p1},
				dataType:'json',
				success:function(data){
					$("#editactivitytype option").remove();
					$("#editactivitytype").append("<option value=''>请选择</option>");
					$.each(data,function(index,item){
						$("#editactivitytype").append("<option value='"+ item.id+ "'>" + item.name + "</option>");
					});
				},
				error:function(){
					console.log("发生错误");
				}
			});
		});
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
		$("#activityPlanEditor").hide();
	});
	function showActivityPlanNextAction() {
		var buttonsOps = {};
		<c:choose>
		<c:when test="${empty op || op eq '新增' || op eq '修改' || op eq '审核'}">
		buttonsOps = {
			"上一步": function() {
                $("#editTitle").show();
                $("#editFileList").show();
                $("#editActivityPlanNext").show();
                $("textarea#content").ckeditorGet().destroy();
                $("#activityPlanEditor").hide();
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
	        //<c:if test="${empty entity.state || entity.state eq '1' || entity.state eq '4'}">
			"提交": function(){
    			if (!$("#editForm").validationEngine('validate'))
	    				return false;
    			$("#editForm").ajaxSubmit({
    				target : "#content2",
    				data:{"state":2},
    				success : function() {
    					$("#editdialog").dialog("close");
    					layer.open({content:"保存成功!"});
    				},
    				error : function() {
    					layer.open({content:"保存失败"});
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
    				<c:if test="${op ne '审核'}">
    				data:{"state":1},
    				</c:if>
    				success : function() {
    					$("#editdialog").dialog("close");
    					layer.open({content:"保存成功!"});
    				},
    				error : function() {
    					layer.open({content:"保存失败"});
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
		<c:when test="${op eq '查看'}">
			buttonsOps = {
				"返回" : function() {
					$("#editdialog").dialog("close");
				}
			};
		</c:when>
		<c:when test="${op eq '下发'}">
			buttonsOps = {
				"下发": function(){
		        		if (!$("#editForm").validationEngine('validate'))
		    				return false;
		    			$("#editForm").ajaxSubmit({
		    				target : "#tablelist",
		    				success : function() {
		    					$("#editdialog").dialog("close");
		    					layer.open({content:"保存成功!"});
		    				},
		    				error : function() {
		    					layer.open({content:"保存失败"});
		    				}
		    			});
		    			$("#editForm").clearForm();
		    			return false;
		        	},
			"取消" : function() {
				$("#editdialog").dialog("close");
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
		$("#activityPlanEditor").show();
		$("#editTitle").hide();
		$("#editFileList").hide();
		$("#editActivityPlanNext").hide();
	}
</script>
