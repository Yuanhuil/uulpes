<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="编辑">
    <form:form id="editForm" method="post" commandName="record" action="/pes/consultcenter/record/save.do">
        <form:hidden path="id" />
        <div class="filterContent">
            <ul>
                <li>
                    <label class="name04">咨询类型</label>
                    <form:select class="input_160" path="consultationtypeid">
                        <form:options items="${consultTypes }" itemValue="id" itemLabel="name"></form:options>
                    </form:select>
                </li>
                <li>
                    <label class="name04">咨询对象</label>
                    <form:select class="input_160" id="objecttype" path="objtype" items="${typeEnum}" itemLabel="info"
                        itemValue="value" onchange="setXhLabel();">
                    </form:select>
                </li>
                <li>
                    <form:label path="data" class="name04">时间</form:label>
                    <form:input path="data" class="input_160 date" />
                </li>
            </ul>
        </div>
        <div class="filterContent">
            <ul>
                <li>
                    <form:label path="username" class="name04">昵称</form:label>
                    <form:input path="username" id="xm" class="input_160" />
                </li>
                <li>
                    <label class="name04">咨询员</label>
                    <form:select path="teacherid" class="select_160" items="${teachers}" itemLabel="xm" itemValue="id">
                    </form:select>
                </li>
                <li>
                    <form:label path="sfzjh" class="name04">身份证号</form:label>
                    <form:input path="sfzjh" class="input_160" />
                </li>
            </ul>
        </div>
        <div class="filterContent">
            <ul>
                <li>
                    <form:label path="xh" id="xhlabel" class="name04">工号</form:label>
                    <form:input path="xh" class="input_160" />
                </li>
                <li><label class="name04">咨询方式</label>
                      <form:select class="input_160" path="consultationmodeid"
                      items="${consultationModels}" itemLabel="name" itemValue="id"></form:select>
                 </li>
            </ul>
        </div>
        <div class="filterContent">
            <ul>
                <div id="showStudent">
                    <li>
                        <label class="name04">学 段</label>
                        <select id="xd" path="xd" defaultvalue="0" class="input_160">
                        </select>
                    </li>
                    <li>
                        <label class="name04">年 级</label>
                        <select id="nj" name="nj" class="input_160">
                            <option value=""></option>
                        </select>
                    </li>
                    <li>
                        <label class="name04">班 级</label>
                        <select id="bjid" class="input_160">
                            <option value="0"></option>
                        </select>
                    </li>
            </ul>
        </div>
        <div class="buttonContent" id="selectStudent">
            <input type="button" onclick="selectStudent()" value="查询" class="button-mid blue" />
        </div>
        <div class="filterContent" id="showStudent" style.display="none">
            <table id="studenthide">
                <thead>
                    <th style="text-align: center">学号</th>
                    <th style="text-align: center">姓名</th>
                    <th style="text-align: center">性别</th>
                    <th style="text-align: center">年级</th>
                    <th style="text-align: center">班级</th>
                    <th style="text-align: center">操作</th>
                </thead>
                <tbody id="studentMsg" style="text-align: center">
                </tbody>
            </table>
        </div>
        <div>
            <form:textarea path="describes" id="content" cols="80" rows="10"></form:textarea>
        </div>
    </form:form>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $("#showStudent").hide();
        $("#selectStudent").hide();
        $("#studenthide").hide();
    });
    $("#dialog-form1").dialog({
        appendTo : "#editDiv",
        autoOpen : false,

        height : 600,
        width : 900,
        modal : false,
        buttons : {
            "保存" : function() {
                $("#dialog-form1").dialog("close");
                $("#editForm").ajaxSubmit({
                    target : "#list",
                    data : {
                        'consultationtypeid1' : jQuery('#consultationtypeid').val(),
                        'consultationmodeid1' : jQuery('#consultationmodeid').val(),
                        'teacherid1' : jQuery('#teacherid').val(),
                        'username1' : jQuery('#username').val(),
                        'objtype1' : jQuery('#objtype').val(),
                        'beginDate' : jQuery('#beginDate').val(),
                        'endDate' : jQuery('#endDate').val(),
                    },
                    success : function() {
                        $("#dialog-form1").dialog("close");
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
                return false;
            },
            "取消" : function() {
                $("#dialog-form1").dialog("close");
            }
        },
    });

    $("#editForm").ajaxForm({
        target : "#list"
    });

    $(".date").datepicker({
        dateFormat : 'yy-mm-dd', //更改时间显示模式
        showAnim : "slide", //显示日历的效果slide、fadeIn、show等
        changeMonth : true, //是否显示月份的下拉菜单，默认为false
        changeYear : true, //是否显示年份的下拉菜单，默认为false
        showWeek : true, //是否显示星期,默认为false
    //	showButtonPanel : true, //是否显示取消按钮，并含有today按钮，默认为false
    //	closeText : 'close', //设置关闭按钮的值
    //yearRange:'2010:2012',	//显示可供选择的年份
    });

    $("textarea#content").ckeditor({height: '220px'});
    function setXhLabel() {
        var objtype = $("#objecttype").val();
        var tt = document.getElementById("objecttype").value;
        if (objtype == 1) {
            $("#xhlabel").text("工号");
            $("#showStudent").hide();
            $("#selectStudent").hide();
        } else {
            $("#xhlabel").text("学号");
            $("#showStudent").show();
            $("#selectStudent").show();
            $.ajax({
                url : "/pes/systeminfo/sys/user/student/getXd.do",
                data : {},
                dataType : "json",
                type : "POST",
                success : function(data) {
                    var msg = "<option value=''>请选择</option>";
                    $.each(data, function(i, k) {
                        debugger;
                        var xdname;
                        if (k == 1)
                            xdname = "小学";
                        if (k == 2)
                            xdname = "初中";
                        if (k == 3)
                            xdname = "高中";
                        msg += "<option value='"+k+"'>" + xdname + "</option>";
                    });
                    $("#xd").html(msg);
                }
            });
        }
    }

    $("#xd").change(function() {
        $("#nj").empty();
        var xd = $("#xd").val();
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

    function selectStudent() {
        var sid = 0;
        var xd = $("#xd").val();
        var nj = $("#nj").val();
        var bjid = $("#bjid").val();
        if (xd != null && xd != "") {
            $.ajax({
                url : "/pes/systeminfo/sys/user/student/recordSearch.do",
                data : {
                    "xd" : xd,
                    "nj" : nj,
                    "sbjid" : bjid,
                    "id" : sid
                },
                dataType : "json",
                type : "POST",
                success : function(data) {
                    if(data == null || data == ""){
                        $("#studenthide").hide();
                        alert("没有该查询条件的学生信息");
                    }else{
                        var msg = "";
                        for (var i = 0; i < data.length; i++) {
                            msg += "<tr>";
                            msg += "<td style='text-align: center'>" + data[i].xh + "</td>";
                            msg += "<td style='text-align: center'>" + data[i].xm + "</td>";
                            if (data[i].xbm == 1) {
                                msg += "<td style='text-align: center'>男</td>";
                            } else {
                                msg += "<td style='text-align: center'>女</td>";
                            }
                            msg += "<td style='text-align: center'>" + data[i].njmc + "</td>";
                            msg += "<td style='text-align: center'>" + data[i].bjmch + "</td>";
                            msg += "<td style='text-align: center'><input  class='button-small white edit' type='button' onclick='addStudent("
                                    + data[i].id + ")' value='添加' /></td>";
                            msg += "</tr>";
                        }
                        $("#studenthide").show();
                        $("#studentMsg").html(msg);
                    }
                },
                error : function(jqXHR, textStatus, errorThrown) {
                    layer.open({
                        content : '错误: ' + jqXHR.responseText
                    });
                }
            });
        } else {
            alert("请选择学段进行查询")
        }
    }

    function addStudent(x) {
        var xd = 0;
        var nj = "";
        var bjid = 0;
        $.ajax({
            url : "/pes/systeminfo/sys/user/student/recordSearch.do",
            data : {
                "xd" : xd,
                "nj" : nj,
                "sbjid" : bjid,
                "id" : x
            },
            dataType : "json",
            type : "POST",
            success : function(data) {
                $("#xm").val(data[0].xm);
                $("#sfzjh").val(data[0].sfzjh);
                $("#xh").val(data[0].xh);
                $("#studenthide").hide();
            },
            error : function(jqXHR, textStatus, errorThrown) {
                layer.open({
                    content : '错误: ' + jqXHR.responseText
                });
            }
        });
    }
</script>