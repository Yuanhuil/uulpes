<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style type="text/css">
.webuploader-pick {
    background-color: #0095cd;
    margin-top: 3px;
    margin-left: 130px;
    vertical-align: bottom;
}

.tableContent {
    width: 100%;
    max-height: 374px;
    text-align: center;
    float: initial;
    margin-top: -5px;
    height: 290px;
}
</style>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction"
    value="${ctx }/workschedule/activityrecord/course" />
<c:choose>
    <c:when test="${op eq '新增'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page"
            value="${baseaction }/create.do" />
    </c:when>
    <c:when test="${op eq '查看'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page" value="" />
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
<div id="editdialog" title="${empty op ? '新增' : op}心理课">
    <form:form action="${formaction}" method="post" id="editForm"
        commandName="entity" enctype="multipart/form-data">
        <div id="activityrecordcourse" class="filterContent">
            <div id="editTitle" class="notice-title">
                <ul class="activityplan-dialog-ul">
                    <li class="activityplan-dialog-li"><label>标题</label>
                        <form:input id="formtitle" path="title"
                            cssClass="input_160 validate[required]"></form:input>
                    </li>
                    <li class="activityplan-dialog-li"><label>活动类型</label>
                        <form:select path="activitytype"
                            cssClass="select_160 validate[required]">
                            <form:option value="">请选择</form:option>
                            <form:options items="${plantype}"
                                itemLabel="name" itemValue="id" />
                        </form:select></li>
                    <li class="activityplan-dialog-li"><label>级别</label>
                        <form:select path="level"
                            cssClass="select_160 validate[required]">
                            <form:option value="">请选择</form:option>
                            <form:options items="${levellist}"
                                itemLabel="name" itemValue="id" />
                        </form:select></li>
                </ul>
                <ul class="activityplan-dialog-ul">
                    <li class="activityplan-dialog-li"><label
                        id="editform_zjr">主讲人</label> <form:input
                            path="speaker"
                            cssClass="input_160 validate[required]"></form:input>
                    </li>
                    <li class="activityplan-dialog-li"><label>对象</label>
                        <form:input path="audience"
                            cssClass="input_160 validate[required]"></form:input>
                    </li>
                    <li class="activityplan-dialog-li"><label
                        id="editform_js">节数</label> <form:input
                            path="num"
                            cssClass="input_160 validate[required]"></form:input>
                    </li>
                </ul>
                <ul class="activityplan-dialog-ul">
                    <li class="activityplan-dialog-li"><label>时间</label>
                        <form:input path="startactivitytime"
                            cssClass="input_160 validate[required]"></form:input>
                    </li>
                    <li class="activityplan-dialog-li"><label>至</label>
                        <form:input path="endactivitytime"
                            cssClass="input_160 validate[required]"></form:input>
                    </li>
                    <li class="activityplan-dialog-li"><label>目标</label>
                        <form:input path="target"
                            cssClass="input_160 validate[required]"></form:input>
                    </li>
                </ul>
                <ul class="activityplan-dialog-ul">
                    <li
                        class="activityplan-dialog-li activityplancourse-share"><label>是否共享</label>
                        <form:select path="share"
                            cssClass="select_160 validate[required]">
                            <form:option value="">请选择</form:option>
                            <form:option value="1">是</form:option>
                            <form:option value="0">否</form:option>
                        </form:select></li>
                    <li class="activityplan-dialog-li">
                        <div id="uploader" class="wu-example">
                            <div class="btns">
                                <div id="picker">添加附件</div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <div id="activityRecordCourseEditor"
                class="notice-editor activityRerocd-Editor">
                <form:textarea path="content" cols="80" rows="8"
                    class="initialization"></form:textarea>
            </div>
            <div id="editFileList" class="tableContent edit-filelist">
                <%@include file="../../filelist.jsp"%>
            </div>
            <div id="editActivityRecordCourseNext"
                class="editdialog-next">
                <div class='next-step'>
                    <button type="button" role="button"
                        class="notice-next"
                        onclick="showActivityRerocdCourseAction();">
                        下一步</button>
                </div>
            </div>
        </div>
    </form:form>
</div>
<script type="text/javascript">
    $(function() {
        $.getScript("/pes/js/jobfileupload.js");
        $("#activitytype").change(function() {
            if ($("#activitytype").val() === '3') {
                $("#editform_zjr").html("组织老师");
                $("#editform_js").html("次数");
            } else {
                $("#editform_zjr").html("主讲人");
                $("#editform_js").html("节数");
            }
        });
        var startDateTextBox = $("#startactivitytime");
        var endDateTextBox = $("#endactivitytime");
        $.timepicker.datetimeRange(startDateTextBox, endDateTextBox, {
            minInterval : (1000 * 60 * 60), // 1hr
            dateFormat : 'yy-m-dd',
            timeFormat : 'HH:mm',
            start : {}, // start picker options
            end : {}
        // end picker options
        });
        var buttonsOps = {};
        $("#editdialog").dialog({
            appendTo : "#editformdiv",
            autoOpen : false,
            modal : false,
            height : 700,
            width : 900,
            position: { my: "top", at: "top", of: "#topHeader" },
            buttons : buttonsOps
        });
        $("#activityRecordCourseEditor").hide();
    });
    function showActivityRerocdCourseAction() {
        var buttonsOps = {};
        <c:choose>
        <c:when test="${empty op || op eq '新增' || op eq '修改'}">
        buttonsOps = {
            "上一步" : function() {
                $("#editTitle").show();
                $("#editFileList").show();
                $("#editActivityRecordCourseNext").show();
                $("textarea#content").ckeditorGet().destroy();
                $("#activityRecordCourseEditor").hide();
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
            "保存" : function() {
                if (!$("#editForm").validationEngine('validate'))
                    return false;
                $("#editForm").ajaxSubmit({
                    target : "#content2",
                    beforeSerialize : function() {
                        if ($("#files").val() === "") {
                            $("#files").remove();
                        }
                    },
                    success : function() {
                        $("#editdialog").dialog("close");
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
                $("#editdialog").dialog("close");
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
        </c:choose>
        $("#editdialog").dialog({
            appendTo : "#editformdiv",
            autoOpen : false,
            modal : false,
            height : 700,
            width : 900,
            buttons : buttonsOps
        });
        $("textarea#content").ckeditor();
        $("#activityRecordCourseEditor").show();
        $("#editTitle").hide();
        $("#editFileList").hide();
        $("#editActivityRecordCourseNext").hide();
    }
</script>
