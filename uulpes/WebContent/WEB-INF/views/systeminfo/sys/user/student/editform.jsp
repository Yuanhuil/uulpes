<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/systeminfo/sys/user/student" />
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
<div id="editdialog" title="${empty op ? '新增' : op}学生信息">
    <form:form action="${formaction}" method="post" id="editForm" commandName="entity">
        <div class="filterContent-dlg">
            <ul>
                <li>
                    <label class="name04" width="60px">真实姓名</label>
                    <form:input path="xm" cssClass="input_160 validate[required]"></form:input>
                </li>
                <li>
                    <label class="name04">性别</label>
                    <form:select path="xbm" cssClass="select_160 validate[required]">
                        <form:option value="">选择性别</form:option>
                        <form:options items="${sexlist}" itemValue="id" itemLabel="name" />
                    </form:select>
                </li>
            </ul>
        </div>
        <div class="filterContent-dlg">
            <ul>
                <li>
                    <label class="name04">身份证号</label>
                    <c:choose>
                        <c:when test="${op eq '修改'}">
                            <form:input id="fromsfzjh" path="sfzjh" disabled="true"
                                cssClass="input_160 validate[required,ajax[ajaxIDCard],ajax[ajaxUserCheck]]"
                                data-prompt-position="inline"></form:input>
                        </c:when>
                        <c:otherwise>
                            <form:input id="fromsfzjh" path="sfzjh"
                                cssClass="input_160 validate[required,ajax[ajaxIDCard],ajax[ajaxUserCheck]]"
                                data-prompt-position="inline"></form:input>
                        </c:otherwise>
                    </c:choose>
                </li>
                <li>
                    <label class="name04">学号</label>
                    <form:input path="xh" cssClass="input_160 validate[required]"></form:input>
                </li>
            </ul>
        </div>
        <div class="filterContent-dlg">
            <ul>
                <li>
                    <label class="name04">学籍号</label>
                    <c:choose>
                        <c:when test="${op eq '修改'}">
                            <form:input id="fromxjh" path="xjh" disabled="true"
                                cssClass="input_160 validate[required,ajax[ajaxStudentXjh]]"
                                data-prompt-position="inline"></form:input>
                        </c:when>
                        <c:otherwise>
                            <form:input id="fromxjh" path="xjh"
                                cssClass="input_160 validate[required,ajax[ajaxStudentXjh]]"
                                data-prompt-position="inline"></form:input>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </div>
        <div class="filterContent-dlg">
            <ul>
                <li>
                    <label class="name04">年级名称</label>
                    <form:select id="sgradeId" path="gradeid" cssClass="select_160" onchange="getBj()">
                        <form:option value="">选择年级</form:option>
                        <form:options items="${gradeList }" itemValue="gradeid" itemLabel="njmc" />
                    </form:select>
                </li>
                <li>
                    <label class="name04">班级名称</label>
                    <form:select id="sclassId" path="classid" items="${classList }" itemValue="id" itemLabel="bjmc"
                        cssClass="select_160">
                    </form:select>
                </li>
            </ul>
        </div>
        <form:hidden path="accountId" />
    </form:form>
</div>
<script type="text/javascript">
    $(function() {
        $("#csrq").datepicker({ //绑定开始日期
            dateFormat : 'yymmdd',
            yearRange : "-20:+1",
            changeMonth : true, //显示下拉列表月份
            changeYear : true
        //显示下拉列表年份

        });
        $("#editForm").validationEngine({
            ajaxFormValidation : true,
            ajaxasync : false
        });
        var buttonsOps = {};
        <c:choose>
        <c:when test="${empty op || op eq '新增' || op eq '修改'}">
        buttonsOps = {
            "保存" : function() {
                if (!$("#editForm").validationEngine('validate'))
                    return false;
                $("#editForm").ajaxSubmit({
                    target : "#content2",
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
            modal : true,
            height : 320,
            width : 520,
            buttons : buttonsOps
        });
    });
    function getBj() {
        debugger;
        var gradeid = document.getElementById("sgradeId").value;
        if (gradeid == "-1") {
            document.getElementById("sclassId").innerHTML = "<option value='-1'>请选择</option>";
            return;
        }
        $.ajax({
            type : "POST",
            url : "../../ajax/getClassesByGradeId2.do",
            data : {
                "gradeid" : gradeid
            },
            success : function(msg) {
                debugger;
                if (msg != '[]') {
                    var callbackarray = jQuery.parseJSON(msg);
                    $("#sclassId").empty();
                    $("#sclassId").append("<option value='-1'>请选择</option>");
                    for (var i = 0; i < callbackarray.length; i++) {
                        var classSchool = callbackarray[i];
                        var classId = classSchool.id;
                        var bjmc = classSchool.bjmc;
                        $("#sclassId").append("<option value='"+classId+"'>" + bjmc + "</option>");
                    }
                } else {
                    document.getElementById("sclassId").innerHTML = "<option value='-1'>请选择</option>";
                }
            },
            error : function() {
                layer.open({
                    content : "根据年级展现班级出错!"
                });
            }
        });
    }
</script>