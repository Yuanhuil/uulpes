<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form" action="/pes/archivemanage/archiveStudent/list.do" commandName="entity"
    method="post">
    <c:if test="${fn:length(xdlist) > 1 }">
        <div class="filterContent">
            <ul>
                <li>
                    <label class="name03">学段</label>
                    <form:select path="xd" cssClass="input_160" defaultvalue="0">
                        <form:option value="0">请选择</form:option>
                        <form:options items="${xdlist}" itemValue="id" itemLabel="info" />
                    </form:select>
                </li>
                <li>
                    <label class="name03">年级</label>
                    <form:select path="nj" cssClass="input_160" defaultvalue="0">
                    </form:select>
                </li>
                <li>
                    <label class="name03">班级</label>
                    <form:select path="bjid" cssClass="input_160" defaultvalue="0">
                    </form:select>
                </li>
            </ul>
        </div>
    </c:if>
    <c:if test="${fn:length(xdlist) == 1 }">
        <div class="filterContent">
            <ul>
                <form:hidden path="xd" />
                <li>
                    <label class="name03">年级</label>
                    <form:select path="nj" cssClass="input_160" defaultvalue="0">
                        <form:option value="0">请选择</form:option>
                        <form:options items="${njlist}" itemLabel="njmc"
                        itemValue="gradeid"></form:options>
                    </form:select>
                </li>
                <li>
                    <label class="name03">班级</label>
                    <form:select path="bjid" cssClass="input_160" defaultvalue="0">
                        <form:option value="0">请选择</form:option>
                    </form:select>
                </li>
            </ul>
        </div>
    </c:if>
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">姓名</label>
                <form:input path="xm" cssClass="input_160" />
            </li>
            <li>
                <label class="name03">性别</label>
                <form:select path="xbm" cssClass="input_160">
                    <form:option value="">请选择</form:option>
                    <form:options items="${sexlist}" itemValue="id" itemLabel="name" />
                </form:select>
            </li>
        </ul>
    </div>
    <div class="buttonContent">
        <input type="submit" id="query" name="query" value="查询" class="button-mid blue" />
        <input type="button" name="clear" id="clear" value="重置" class="button-mid blue" />
        <input type="button" id="settingButton" value="生成档案设置" class="button-mid blue" style="display: none"/>
    </div>
</form:form>
<script type="text/javascript">
    var hasSetted = false;
    $("#queryform").ajaxForm({
        target : "#list"
    });
    $("#clear").click(function() {
        $('#queryform').clearForm();
    });
    $("#xd").change(function() {
        $("#nj").empty();
        var xd = $(this).val();
        if (xd == 0) {
            return;
        }
        $.ajax({
            url : "/pes/systeminfo/sys/user/student/getGrades.do",
            data : {
                "xd" : xd
            },
            dataType : "json",
            type : "POST",
            success : function(data) {
                $("#nj").append("<option value='0'>请选择</option>");
                $.each(data, function(i, k) {
                    $("#nj").append("<option value='" + k.gradeid + "'>" + k.njmc + "</option>");
                });
            },
            error : function(jqXHR, textStatus, errorThrown) {
                layer.open({
                    content : '错误: ' + jqXHR.responseText
                });
            }
        });
    });
    $("#nj").change(function() {
        $("#bjid").empty();
        var nj = $(this).val();
        var xd = $("#xd").val();
        $.ajax({
            url : "/pes/systeminfo/sys/user/student/getClasses.do",
            data : {
                "nj" : nj,
                "xd" : xd
            },
            dataType : "json",
            type : "POST",
            success : function(data) {
                debugger;
                $("#bjid").append("<option value='0'>请选择</option>");
                $.each(data, function(i, k) {
                    $("#bjid").append("<option value='" + k.id + "'>" + k.bjmc + "</option>");
                });
            },
            error : function(jqXHR, textStatus, errorThrown) {
                layer.open({
                    content : '错误: ' + jqXHR.responseText
                });
            }
        });
    });
    $("#settingButton").click(function() {
        debugger;
        var str = $(this).val();
        if (str == "生成档案设置") {
            if (hasSetted == true) {
                $("#settingButton").attr("value", "关闭档案设置");
                $("#editDiv").attr("style", "display:block");
                $("#list").attr("style", "display:none");
                return;
            }
            var settingform = $('<form></form>');
            settingform.attr('id', "settingform");
            settingform.attr('action', "/pes/archivemanage/archiveStudent/archiveSetting.do");
            settingform.ajaxSubmit({
                target : "#setting",
                success : function(msg) {
                    debugger;
                    $("#settingButton").attr("value", "关闭档案设置");
                    $("#editDiv").attr("style", "display:block");
                    $("#list").attr("style", "display:none");
                    hasSetted = true;
                    //$('#settingform').remove();
                },
                error : function() {
                    layer.open({
                        content : '加载页面背景字段时出错!'
                    });
                }
            });
        } else {
            $(this).attr("value", "生成档案设置");
            $("#list").attr("style", "display:block");
            $("#editDiv").attr("style", "display:none");
        }
    });
</script>