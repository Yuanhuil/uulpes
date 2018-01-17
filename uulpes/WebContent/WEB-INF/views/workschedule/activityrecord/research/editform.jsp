<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style type="text/css">
.webuploader-pick {
    background-color: #0095cd;
    margin-top: 3px;
    margin-right: 88px;
    vertical-align: bottom;
}

.tableContent {
    width: 100%;
    max-height: 300px;
    text-align: center;
    float: initial;
    margin-top: -5px;
    height: 234px;
}
</style>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction"
    value="${ctx }/workschedule/activityrecord/research" />
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
<div id="editdialog" title="${empty op ? '新增' : op}心理教科研">
    <form:form action="${formaction}" method="post" id="editForm"
        commandName="entity" enctype="multipart/form-data">
        <div id="activityrecordresearch" class="filterContent">
            <div id="editTitle" class="notice-title">
                <ul class="activityplan-dialog-ul">
                    <div class="activityrecordresearch-maindiv">
                        <li class="activityplan-dialog-li"><label>标题</label>
                            <form:input id="formtitle" path="title"
                                cssClass="input_160"></form:input></li>
                        <li class="activityplan-dialog-li"><label>活动类型</label>
                            <form:select path="activitytype"
                                cssClass="select_160">
                                <form:option value="">请选择</form:option>
                                <form:options items="${plantype}"
                                    itemLabel="name" itemValue="id" />
                            </form:select></li>
                    </div>
                    <div class="activityrecordresearch-subdiv">
                        <li id="yjjd" class="activityplan-dialog-li"><label>研究阶段</label>
                            <form:select path="level"
                                cssClass="select_160">
                                <form:option value="">请选择</form:option>
                                <form:options items="${researchStep }"
                                    itemValue="id" itemLabel="name" />
                            </form:select></li>
                        <li id="pxrs" class="activityplan-dialog-li"><label>培训人数</label>
                            <form:input path="num" cssClass="input_160"></form:input>
                        </li>
                    </div>
                </ul>
                <ul class="activityplan-dialog-ul">
                    <li class="activityplan-dialog-li"><label>主题</label>
                        <form:input path="subject" cssClass="input_160"></form:input>
                    </li>
                    <li class="activityplan-dialog-li"><label>时间</label>
                        <form:input path="starttime"
                            cssClass="input_160"></form:input></li>
                    <li class="activityplan-dialog-li"><label>至</label>
                        <form:input path="endtime" cssClass="input_160"></form:input></li>
                </ul>
                <ul class="activityplan-dialog-ul">
                    <li
                        class="activityplan-dialog-li"><label>发表情况</label>
                        <form:select path="ispublish"
                            cssClass="select_160 validate[required]">
                            <form:option value="">请选择</form:option>
                            <form:option value="1">是</form:option>
                            <form:option value="0">否</form:option>
                        </form:select></li>
                    <li class="activityplan-dialog-li"><label>发表刊物</label>
                        <form:input path="publishbooks"
                            cssClass="input_160"></form:input></li>
                    <li style="text-align: left"
                        class="activityplan-dialog-li"><label>主要人员</label>
                        <form:input path="partinpersons"
                            cssClass="input_160"></form:input></li>
                </ul>
                <ul class="activityplan-dialog-ul">
                      <li
                        class="activityplan-dialog-li activityrecordresearch-radio"><label>获奖情况</label>
                        <form:select path="isreward"
                            cssClass="select_160 validate[required]">
                            <form:option value="">请选择</form:option>
                            <form:option value="1">是</form:option>
                            <form:option value="0">否</form:option>
                        </form:select></li>
                    <li class="activityplan-dialog-li"><label>奖项</label>
                        <form:input path="rewards" cssClass="input_160"></form:input>
                    </li>
                </ul>
                <ul class="activityplan-dialog-ul">
                    <li
                        class="activityplan-dialog-li activityrecordresearch-radio"><label>是否共享</label>
                        <form:select path="share"
                            cssClass="select_160 validate[required]">
                            <form:option value="">请选择</form:option>
                            <form:option value="1">是</form:option>
                            <form:option value="0">否</form:option>
                        </form:select></li>
                    <li class="activityplan-dialog-li">
                        <div id="uploader" class="wu-example">
                            <div class="btns">
                                <div id="picker">选择文件</div>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <div id="activityRecordResearchEditor"
                class="notice-editor activityRerocd-Editor">
                <form:textarea path="content" cols="80" rows="8"
                    class="initialization"></form:textarea>
            </div>
            <div id="editFileList" class="tableContent edit-filelist">
                <%@include file="../../filelist.jsp"%>
            </div>
            <div id="editActivityRecordResearchNext"
                class="editdialog-next">
                <div class='next-step'>
                    <button type="button" role="button"
                        class="notice-next"
                        onclick="showActivityRerocdResearchAction();">
                        下一步</button>
                </div>
            </div>
        </div>
    </form:form>
</div>
<script type="text/javascript">
    $(function() {
        $.getScript("/pes/js/jobfileupload.js");
        $("#pxrs").hide();
        $("#yjjd").hide();
        $("#activitytype").change(function() {
            if ($("#activitytype").val() === "10") {
                $("#pxrs").show();
                $("#yjjd").hide();
            } else if ($("#activitytype").val() === "8") {
                $("#yjjd").show();
                $("#pxrs").hide();
            } else {
                $("#pxrs").hide();
                $("#yjjd").hide();
            }
        });
        if ($("#activitytype").val() === "10") {
            $("#pxrs").show();
            $("#yjjd").hide();
        }
        if ($("#activitytype").val() === "8") {
            $("#yjjd").show();
            $("#pxrs").hide();
        }
        var startDateTextBox = $("#starttime");
        var endDateTextBox = $("#endtime");
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
        $("#activityRecordResearchEditor").hide();
    });
    function showActivityRerocdResearchAction() {
        var buttonsOps = {};
        <c:choose>
        <c:when test="${empty op || op eq '新增' || op eq '修改'}">
        buttonsOps = {
            "上一步" : function() {
                $("#editTitle").show();
                $("#editFileList").show();
                $("#editActivityRecordResearchNext").show();
                $("textarea#content").ckeditorGet().destroy();
                $("#activityRecordResearchEditor").hide();
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
                    target : "#psycourse",
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
        $("#activityRecordResearchEditor").show();
        $("#editTitle").hide();
        $("#editFileList").hide();
        $("#editActivityRecordResearchNext").hide();
    }
</script>
