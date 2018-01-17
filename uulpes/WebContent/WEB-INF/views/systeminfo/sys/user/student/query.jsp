<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="studentform" action="${baseaction}/student/search.do" method="post" modelAttribute="entity">
    <c:if test="${fn:length(xdlist) > 1 }">
        <div class="filterContent">
            <ul>
                <li>
                    <label class="name03">学段</label>
                    <form:select path="xd" cssClass="select_160" defaultvalue="0">
                        <form:option value="0">请选择</form:option>
                        <form:options items="${xdlist}" itemValue="id" itemLabel="info" />
                    </form:select>
                </li>
                <li>
                    <label class="name03">年级</label>
                    <form:select path="nj" cssClass="select_160"></form:select>
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
                    <form:select path="nj" cssClass="select_160">
                        <form:option value="">请选择</form:option>
                        <form:options items="${njlist}" itemValue="gradeid" itemLabel="njmc" />
                    </form:select>
                </li>
                <li>
                    <label class="name03">班级</label>
                    <form:select path="bjid" cssClass="select_160" />
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
                <form:select path="xbm" cssClass="select_160">
                    <form:option value="">请选择</form:option>
                    <form:options items="${sexlist}" itemValue="id" itemLabel="name" />
                </form:select>
            </li>
            <li>
                <label class="name03">身份证号</label>
                <form:input path="sfzjh" cssClass="input_160" type="text" />
            </li>
        </ul>
    </div>
    <div class="buttonContent">
        <div class="buttonLeft">
            <ul>
                <li>
                    <shiro:hasPermission name="systeminfo:user:student:create">
                        <input class="button-mid white" type="button" value="新增" chref="${baseaction}/student/add.do"
                            id="addstudent">
                    </shiro:hasPermission>
                </li>
                <li>
                    <input type="file" name="file" id="impfile" width="80%" style="display: none" />
                </li>
                <li>
                    <shiro:hasPermission name="systeminfo:user:student:create">
                        <input class="button-mid white" type="button" value="导入" id="stuimport">
                    </shiro:hasPermission>
                </li>
                <!-- <input	class="button-mid blue" type="button" value="导出" id="stuexport">-->
                <shiro:hasPermission name="systeminfo:user:student:update">
                    <li>
                        <input class="button-mid white" type="button" value="调班" id="changeclass">
                    </li>
                </shiro:hasPermission>
                <shiro:hasPermission name="systeminfo:user:student:delete">
                    <li>
                        <input class="button-mid white" type="button" value="删除" id="del" chref="">
                    </li>
                </shiro:hasPermission>
            </ul>
        </div>
        <div class="buttonRight">
            <ul>
                <li>
                    <input id="searchsuborgs" class="button-mid blue" type="submit" value="搜索">
                </li>
                <li>
                    <input id="clearform" class="button-mid blue" type="button" value="重置">
                </li>
            </ul>
        </div>
    </div>
</form:form>
<script type="text/javascript">
    var newclassid = null;
    function setNewClassId(newbjid) {
        newclassid = newbjid;
    }
    $(function() {
        $("#changeclass").click(function() {
            var selectedData = [];
            var selectRow = $("input[name='rowcheck']:checked");
            if (selectRow.length === 0) {
                layer.open({
                    content : "没有选择学生!"
                });
                return false;
            }
            debugger;
            var bjid = $("#bjid").val();
            if (bjid == '' || bjid == null) {
                layer.alert("为了保证所选择的学生属于一个班级,请选择班级查询学生后再操作");
                return;
            }
            var xd = $("#xd").val();
            var nj = $("#nj").val();
            var url = "${baseaction }/student/" + xd + "/" + nj + "/gotochangeclass.do";
            layer.open({
                area : [ '350px', '250px' ],
                btn : [ '确认', '取消' ],
                shadeClose : false,
                type : 2,
                content : url,
                yes : function(index) {
                    layer.close(index);
                    debugger;
                    var selectedData = [];
                    var selectRow = $("input[name='rowcheck']:checked");
                    selectRow.each(function() {
                        selectedData.push(this.value);
                    });
                    $('#content2').load("${baseaction }/student/changeclass.do", {
                        rowcheck : selectedData,
                        newclassid : newclassid
                    });
                },
                no : function() {

                }
            });
        });
        $("#del").click(function() {
            var selectedData = [];
            var selectRow = $("input[name='rowcheck']:checked");
            if (selectRow.length === 0) {
                layer.open({
                    content : "没有选择相关内容"
                });
                return false;
            }
            layer.confirm('确定要删除所选择记录内容吗?', {
                btn : [ '是', '否' ]
            }, function(index) {
                layer.close(index);
                selectRow.each(function() {
                    selectedData.push(this.value);
                });
                $('#content2').load("${baseaction }/student/delselected.do", {
                    rowcheck : selectedData
                });
            }, function(index) {
                layer.close(index);
            });
        });

        $("#clearform").click(function() {
            $("#studentform").clearForm();
        });

        $("#studentform").ajaxForm({
            target : "#tablelist"
        });

        $("#xd").change(function() {
            $("#nj").empty();
            var xd = $(this).val();
            if (xd == 0) {
                return;
            }
            $.ajax({
                url : "${baseaction}/student/getGrades.do",
                data : {
                    "xd" : xd
                },
                dataType : "json",
                type : "POST",
                success : function(data) {
                    $("#nj").append("<option value=''>请选择</option>");
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
                url : "${baseaction}/student/getClasses.do",
                data : {
                    "nj" : nj,
                    "xd" : xd
                },
                dataType : "json",
                type : "POST",
                success : function(data) {
                    $("#bjid").append("<option value='0'>请选择</option>");
                    $.each(data, function(i, k) {
                        $("#bjid").append("<option value='" + k.id + "'>" + k.bjmc + "</option>");
                    });
                }
            });
        });
        if ($("#addstudent") != null) {
            $("#addstudent").on("click", function() {
                var h = $(this).attr("chref");
                $("#editformdiv").html();
                $("#editformdiv").load(h, function() {
                    $("#editdialog").dialog("open");
                });
            });
        }
        //$("#stuimport").on("click", function(){
        //return  $("#impfile").click();
        //});
        $("#stuimport").on("click", function() {
            var url = "${baseaction}/student/redirectToImportStudent.do";
            $("#content2").load(url);
        });

        $("#impform").ajaxForm({
            target : "#content2"
        });

        $("#impfile").on("change", function(e) {
            debugger;
            $("#impform").ajaxSubmit({
                success : function() {
                    layer.open({
                        content : '导入数据成功！'
                    });
                },
                target : "#content2"
            });
        });

    });
</script>
