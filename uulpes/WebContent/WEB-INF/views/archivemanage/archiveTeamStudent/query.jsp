<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:choose>
    <c:when test="${orgType=='2' }">
        <c:set var="stuTeamReportsurl"
            value="/pes/archivemanage/archiveTeamStudent/archiveinschool.do"></c:set>
    </c:when>
    <c:otherwise>
        <c:if test="${orgLevel==3 }">
            <c:set var="stuTeamReportsurl"
                value="/pes/archivemanage/archiveTeamStudent/archiveincity.do"></c:set>
        </c:if>
        <c:if test="${orgLevel==4 }">
            <c:set var="stuTeamReportsurl"
                value="/pes/archivemanage/archiveTeamStudent/archiveincounty.do"></c:set>
        </c:if>
    </c:otherwise>
</c:choose>
<form:form id="queryform" name="form" action="${stuTeamReportsurl }"
    commandName="analyze" method="post" target="_blank">
    <c:choose>
        <c:when test="${orgType=='2'}">
            <c:if test="${fn:length(xdlist) > 1 }">
                <div class="filterContent" id="class">
                    <ul>
                        <li><label class="name03">学段</label> <form:select
                                path="groupid" cssClass="input_160"
                                onchange="queryNj(2)">
                                <form:option value="">请选择</form:option>
                                <form:options items="${xdlist}"
                                    itemValue="id" itemLabel="info" />
                            </form:select></li>
                        <form:hidden path="grade" />
                        <form:hidden path="bj" />
                        <li><label class="name03">年级</label> <select
                            id="nj" name="nj" class="input_160"
                            onchange="queryBj();">
                                <option value=""></option>
                        </select></li>
                        <li><label class="name03">班级</label><select
                            id="bjid" class="input_160">
                                <option value=""></option>
                        </select></li>
                    </ul>
                </div>
            </c:if>
            <c:if test="${fn:length(xdlist) == 1 }">
                <div class="filterContent" id="class">
                    <ul>
                        <form:hidden path="groupid" value="${xdlist[0].id}" />
                        <form:hidden path="grade" />
                        <form:hidden path="bj" />
                        <li><label class="name03">年级</label> <form:select
                            path="nj" class="input_160"
                            onchange="queryBj();">
                                    <form:option value="">请选择</form:option>
                                <c:forEach var="item"
                                    items="${njlist}">
                                    <form:option value="${item.gradeid}">${item.njmc}</form:option>
                                </c:forEach>
                        </form:select></li>
                        <li><label class="name03">班级</label><select
                            id="bjid" class="input_160">
                                <option value=""></option>
                        </select></li>
                    </ul>
                </div>
            </c:if>
            <input id="bjarray" name="bjarray" type="hidden" />
        </c:when>
        <c:otherwise>
            <c:if test="${orgLevel=='3'}">
                <div class="filterContent">
                    <ul>
                        <li><label class="name03">机构层级</label> <select
                            id="xzxs" name="xzxs" class="select_160"
                            onchange="queryQXXXFun('${cityId }');">
                                <option value="-1">请选择</option>
                                <option value="1">区县</option>
                                <option value="2">市直属学校</option>
                        </select></li>
                    </ul>
                </div>
                <div class="filterContent">
                    <ul>
                        <li><label id="qxorxx_label" class="name03">选择区县</label>
                            <form:select id="qx_or_xx" path="qx_or_xx"
                                cssClass="select_160"
                                onchange="queryXd();">
                            </form:select></li>
                        <li><label class="name03">学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;段</label>
                        <form:select class="select_160" path="groupid"
                                onchange="queryNj(3);">
                                <form:option value="" id="se_export">请选择</form:option>
                                <form:option value="1">小学</form:option>
                                <form:option value="2">初中</form:option>
                                <form:option value="3">高中</form:option>
                            </form:select></li>
                        <li><label class="name03">年级名称</label> <form:select
                                id="nj" path="nj" class="select_160"
                                type="text">
                                <form:option value="">请选择</form:option>

                            </form:select></li>
                    </ul>
                </div>
                <input id="qxorxxarray" name="qxorxxarray" type="hidden" />
            </c:if>
            <c:if test="${orgLevel=='4'}">
                <div class="filterContent">
                    <ul>
                        <li><label class="name03">选择学校</label> <form:select
                                id="qx_or_xx" path="qx_or_xx"
                                class="select_160" onchange="queryXd();">
                                <form:option value="">请选择</form:option>
                                <c:forEach var="item"
                                    items="${schoolList}">
                                    <form:option value="${item.id}">${item.name}</form:option>
                                </c:forEach>
                            </form:select></li>
                        <li><label class="name03">学段</label>
                        <form:select class="select_160" path="groupid"
                                onchange="queryNj(4);">
                                <form:option value="" id="se_export">请选择</form:option>
                                <form:option value="1">小学</form:option>
                                <form:option value="2">初中</form:option>
                                <form:option value="3">高中</form:option>
                            </form:select></li>
                        <li><label class="name03">年级名称</label> <form:select
                                id="nj" path="nj" class="select_160"
                                type="text">
                                <form:option value="">请选择</form:option>
                                <form:options items="${njList }"
                                    itemLabel="njmc" itemValue="njmc" />
                            </form:select></li>
                    </ul>
                </div>
                <input id="qxorxxarray" name="qxorxxarray" type="hidden" />
            </c:if>
        </c:otherwise>
    </c:choose>
    <div class="filterContent">
        <ul>
            <li><label class="name03">评测时间</label> <form:input id="starttime"
                    path="starttime" class="input_160" type="text"></form:input></li>
            <li><label class="name03">至</label>
            <form:input id="endtime" path="endtime" class="input_160" type="text"></form:input></li>
        </ul>
    </div>
    <div class="buttonContent">
        <input type="button" id="query" name="query"
            class="button-small blue" value="查询"
            onclick="createTeamReport('${orgType}','${orgLevel}');" /> <input
            type="button" name="clear" id="clear"
            class="button-small blue" value="重置"
            onclick="teamReset('${orgType}','${orgLevel}');" /> <input
            type="submit" id="subBut" style="display: none" />
    </div>
</form:form>
<script type="text/javascript">
    var qxvs;
    var jgcj;
    $("#bjid").change(function() {
        $("#bj").val($(this).val());
    });
    function createTeamReport(orgType, orgLevel) {
    	var starttime = document.getElementById("starttime").value;
        var endtime = document.getElementById("endtime").value;
        if (starttime == '' || starttime == null) {
            layer.open({
                content : '请输入开始时间!'
            });
            return;
        }
        if (endtime == '' || endtime == null) {
            layer.open({
                content : '请输入终止时间!'
            });
            return;
        }
        if (orgType == '2') {
            var groupid = $("#groupid").val();
            var nj = $("#nj").val();

            if (groupid == 0) {
                layer.open({
                    content : '请选择学段'
                });
                return;
            }
            debugger;
            if (nj == "") {
                layer.open({
                    content : '请选择年级'
                });
                return;
            }
            var selectedbj = $("#bj").val();
            var bjArray = new Array(); ///数组
            if (selectedbj != '')
                bjArray[0] = selectedbj;
            var bjJson = JSON.stringify(bjArray);
            $("#bjarray").val(bjJson);
        } else {
            if (orgLevel == '3') {
                var xzxs = $("#xzxs").val();
                if (xzxs == '') {
                    layer.open({
                        content : '请选择机构层级'
                    });
                    return;
                }
                var groupid = $("#groupid").val();
                if (groupid == '') {
                    layer.open({
                        content : '请选择学段'
                    });
                    return;
                }
                var selectedschool = $("#qx_or_xx").val();
                var xxArray = new Array(); ///数组
                if (selectedschool != '')
                    xxArray[0] = selectedschool;
                var qxOrxxJson = JSON.stringify(xxArray);
                $("#qxorxxarray").val(qxOrxxJson);
            }
            if (orgLevel == '4') {
                var groupid = $("#groupid").val();
                if (groupid == '') {
                    layer.open({
                        content : '请选择学段'
                    });
                    return;
                }
                debugger;
                var selectedschool = $("#qx_or_xx").val();
                var xxArray = new Array(); ///数组
                if (selectedschool != '')
                    xxArray[0] = selectedschool;
                var qxOrxxJson = JSON.stringify(xxArray);
                $("#qxorxxarray").val(qxOrxxJson);
            }
        }
        $('#subBut').click();
    }

    $("#starttime").datepicker({ //绑定开始日期
        dateFormat : 'yy-mm-dd',
        changeMonth : true, //显示下拉列表月份
        changeYear : true, //显示下拉列表年份
        showWeek : true, //显示星期
        firstDay : "1", //设置开始为1号
        onSelect : function(dateText, inst) {
            //设置结束日期的最小日期
            $("#endDate").datepicker('option', 'minDate', new Date(dateText.replace('-', ',')));

        }
    });

    $("#endtime").datepicker({ //设置结束绑定日期
        dateFormat : 'yy-mm-dd',
        changeMonth : true, //显示下拉列表月份
        changeYear : true, //显示下拉列表年份
        showWeek : true, //显示星期
        firstDay : "1", //设置开始为1号
        onSelect : function(dateText, inst) {
            //设置开始日期的最大日期
            $('#beginDate').datepicker('option', 'maxDate', new Date(dateText.replace('-', ',')));
        }
    });

    function queryNj(orgLevel) {
        debugger;
        if (orgLevel == 3) {
            var xzxs = $("#xzxs").val();
            if (xzxs == '1') {//区县
                $("#nj").empty();
                var xd = $("#groupid").val();
                if (xd == '') {
                    $("#nj").html("<option value=''>请选择</option>");
                }
                if (xd == 1) {
                    $("#nj").append("<option value=''>请选择</option>");
                    $("#nj").append("<option value='1'>一年级</option>");
                    $("#nj").append("<option value='2'>二年级</option>");
                    $("#nj").append("<option value='3'>三年级</option>");
                    $("#nj").append("<option value='4'>四年级</option>");
                    $("#nj").append("<option value='5'>五年级</option>");
                    $("#nj").append("<option value='6'>六年级</option>");
                }
                if (xd == 2) {
                    $("#nj").append("<option value=''>请选择</option>");
                    $("#nj").append("<option value='7'>一年级</option>");
                    $("#nj").append("<option value='8'>二年级</option>");
                    $("#nj").append("<option value='9'>三年级</option>");
                    $("#nj").append("<option value='10'>四年级</option>");
                }
                if (xd == 3) {
                    $("#nj").append("<option value=''>请选择</option>");
                    $("#nj").append("<option value='11'>一年级</option>");
                    $("#nj").append("<option value='12'>二年级</option>");
                    $("#nj").append("<option value='13'>三年级</option>");
                }
                return;
            }
            if (xzxs == '2') {
                var schoolid = $("#qx_or_xx").val();
                if (schoolid == '') {
                    $("#nj").empty();
                    var xd = $("#groupid").val();
                    if (xd == '') {
                        $("#nj").html("<option value=''>请选择</option>");
                    }
                    if (xd == 1) {
                        $("#nj").append("<option value=''>请选择</option>");
                        $("#nj").append("<option value='1'>一年级</option>");
                        $("#nj").append("<option value='2'>二年级</option>");
                        $("#nj").append("<option value='3'>三年级</option>");
                        $("#nj").append("<option value='4'>四年级</option>");
                        $("#nj").append("<option value='5'>五年级</option>");
                        $("#nj").append("<option value='6'>六年级</option>");
                    }
                    if (xd == 2) {
                        $("#nj").append("<option value=''>请选择</option>");
                        $("#nj").append("<option value='7'>一年级</option>");
                        $("#nj").append("<option value='8'>二年级</option>");
                        $("#nj").append("<option value='9'>三年级</option>");
                        $("#nj").append("<option value='10'>四年级</option>");
                    }
                    if (xd == 3) {
                        $("#nj").append("<option value=''>请选择</option>");
                        $("#nj").append("<option value='11'>一年级</option>");
                        $("#nj").append("<option value='12'>二年级</option>");
                        $("#nj").append("<option value='13'>三年级</option>");
                    }
                    return;
                }
            }
        }
        if (orgLevel == 4) {
            debugger;
            var schoolid = $("#qx_or_xx").val();
            if (schoolid == '') {
                $("#nj").empty();
                var xd = $("#groupid").val();
                if (xd == '') {
                    $("#nj").html("<option value=''>请选择</option>");
                }
                if (xd == 1) {
                    $("#nj").append("<option value=''>请选择</option>");
                    $("#nj").append("<option value='1'>一年级</option>");
                    $("#nj").append("<option value='2'>二年级</option>");
                    $("#nj").append("<option value='3'>三年级</option>");
                    $("#nj").append("<option value='4'>四年级</option>");
                    $("#nj").append("<option value='5'>五年级</option>");
                    $("#nj").append("<option value='6'>六年级</option>");
                }
                if (xd == 2) {
                    $("#nj").append("<option value=''>请选择</option>");
                    $("#nj").append("<option value='7'>一年级</option>");
                    $("#nj").append("<option value='8'>二年级</option>");
                    $("#nj").append("<option value='9'>三年级</option>");
                    $("#nj").append("<option value='10'>四年级</option>");
                }
                if (xd == 3) {
                    $("#nj").append("<option value=''>请选择</option>");
                    $("#nj").append("<option value='11'>一年级</option>");
                    $("#nj").append("<option value='12'>二年级</option>");
                    $("#nj").append("<option value='13'>三年级</option>");
                }
                return;
            }
        }
        $("#nj").empty();
        var xd = $("#groupid").val();
        if (xd == '') {
            $("#nj").html("<option value=''>请选择</option>");
            $("#bj").html("<option value=''>请选择</option>");
        }
        var schoolid = $("#qx_or_xx").val();
        $.ajax({
            url : "${baseaction}/pes/systeminfo/sys/user/student/getGrades.do",
            data : {
                "xd" : xd,
                "schoolid" : schoolid
            },
            dataType : "json",
            type : "POST",
            success : function(data) {
                $("#nj").append("<option value=''>请选择</option>");
                $.each(data, function(i, k) {
                    $("#nj").append("<option value='" + k.gradeid + "'>" + k.njmc + "</option>");
                });
            }

        });

    }
    function getNj(orgLevel) {
        debugger;
        var xd = document.getElementById("xd").value;
        if (xd == -1) {
            $("#nj").html("<option value='-1'>请选择</option>");
            return;
        }
        var xzxs = '0';
        if (orgLevel == '3')
            xzxs = document.getElementById("xzxs").value;
        $.ajax({
            type : "POST",
            url : "../../scaletoollib/reportlook/queryDistinctNJFromExamresultStudent.do",
            data : {
                "xd" : xd,
                "xzxs" : xzxs
            },
            success : function(msg) {
                debugger;
                var nj = $("#nj");
                var njarray = jQuery.parseJSON(msg);
                if (njarray.length > 0) {
                    var optionstr = "<option value='-1'>请选择</option>";
                    for (var i = 0; i < njarray.length; i++) {
                        var njid = njarray[i].nj;
                        optionstr = optionstr + "<option value="+njid+">" + njid + "级</option>";
                    }
                    nj.html(optionstr);
                } else {
                    nj.html("");
                    nj.html("<option value='-1'>请选择</option>");
                }
            },
            error : function() {
                layer.open({
                    content : "调用出现错误，查找年级失败"
                });
            }
        });
    }

    function hideClass(obj) {
        var role = $(obj).val();
        if (role == 1) {
            $("#class").show();
            $("#class1").show();

        } else {
            $("#class").hide();
            $("#class1").hide();
        }

    }

    function initFiled(obj) {

        var val = $(obj).val();
        var field = $("#filed").val();
        if (obj.checked) {

            $("#filed").val(field + val + ",");
        } else {
            field = field.replace(val + ",", "");
            $("#filed").val(field);
        }
    }
    function queryQXXXFun(cityId) {
        debugger;

        var xzxs = document.getElementById("xzxs").value;
        jgcj = xzxs;//机构层级
        if (xzxs == "-1") {
            document.getElementById("qx_or_xx").innerHTML = "<option value=''>请选择</option>";
            $("#groupid").val("");
            document.getElementById("nj").innerHTML = "<option value=''>请选择</option>";
            return;
        }
        if (xzxs == "1") {
            $("#qxorxx_label").text("选择区县");
            $("#groupid").val("");
            document.getElementById("nj").innerHTML = "<option value=''>请选择</option>";
            $.ajax({
                type : "POST",
                url : "../../scaletoollib/reportlook/getStudentExamdoQuxianStr.do",
                data : {
                    "cityId" : cityId
                },
                success : function(msg) {
                    debugger;
                    if (msg != '[]') {
                        var callbackarray = jQuery.parseJSON(msg);
                        $("#qx_or_xx").empty();
                        $("#qx_or_xx").append("<option value=''>请选择</option>");
                        for (var i = 0; i < callbackarray.length; i++) {
                            var quxian = callbackarray[i];
                            var code = quxian.code;
                            var name = quxian.name;
                            $("#qx_or_xx").append("<option value='"+code+"'>" + name + "</option>");
                        }
                        qxvs.multiselect('refresh');
                    } else {
                        //$("#qx_or_xx").append("<option value='-1'>请选择</option>");
                        qxvs.multiselect('refresh');
                    }
                },
                error : function() {
                    layer.open({
                        content : "出错!"
                    });
                }
            });
        } else {
            $("#qxorxx_label").text("选择学校");
            $("#groupid").val("");
            document.getElementById("nj").innerHTML = "<option value=''>请选择</option>";
            $.ajax({
                type : "POST",
                url : "../../ajax/getStudentExamdoSonSchools.do",
                data : {
                    "cityId" : cityId
                },
                success : function(msg) {
                    if (msg != '[]') {
                        var callbackarray = jQuery.parseJSON(msg);
                        $("#qx_or_xx").empty();
                        $("#qx_or_xx").append("<option value=''>请选择</option>");
                        for (var i = 0; i < callbackarray.length; i++) {
                            var classSchool = callbackarray[i];
                            var classId = classSchool.id;
                            var name = classSchool.name;
                            $("#qx_or_xx").append("<option value='"+classId+"'>" + name + "</option>");
                        }
                    } else {
                        document.getElementById("qx_or_xx").innerHTML = "<option value='-1'>请选择</option>";
                    }
                    qxvs.multiselect('refresh');

                },
                error : function() {
                    layer.open({
                        content : "查询直属学校出错!"
                    });
                }
            });
        }
    }
    function queryXd() {
        if (jgcj == '1')
            return;
        var schoolid = $("#qx_or_xx").val();
        if (schoolid == '') {
            $("#groupid").empty();
            $("#groupid").append("<option value=''>请选择</option>");
            $("#groupid").append("<option value='1'>小学</option>");
            $("#groupid").append("<option value='2'>初中</option>");
            $("#groupid").append("<option value='3'>高中</option>");
        }
        $.ajax({
            url : "${baseaction}/pes/systeminfo/sys/user/student/getXd.do",
            data : {
                "schoolid" : schoolid
            },
            dataType : "json",
            type : "POST",
            success : function(data) {
                debugger;
                $("#groupid").empty();

                $("#groupid").append("<option value=''>请选择</option>");
                $.each(data, function(i, k) {
                    debugger;
                    var xdname;
                    if (k == 1)
                        xdname = "小学";
                    if (k == 2)
                        xdname = "初中";
                    if (k == 3)
                        xdname = "高中";
                    $("#groupid").append("<option value='" + k + "'>" + xdname + "</option>");
                });

            }

        });
    }
    function queryBj() {
        debugger;
        $("#bjid").empty();
        var nj = $("#nj").val();
        if (nj == '') {
            $("#bjid").html("<option value=''>请选择</option>");
            return;
        }
        var xd = $("#groupid").val();
        $.ajax({
            url : "${baseaction}/pes/systeminfo/sys/user/student/getClasses.do",
            data : {
                "nj" : nj,
                "xd" : xd
            },
            dataType : "json",
            type : "POST",
            success : function(data) {

                $("#bjid").append("<option value=''>请选择</option>");
                $.each(data, function(i, k) {
                    $("#bjid").append("<option value='" + k.id + "'>" + k.bjmc + "</option>");
                });
            }

        });
    }
    function teamReset(orgType, orgLevel) {
        $("#queryform")[0].reset();
        if (orgType == '2') {
            $("#nj").html("<option value=''>请选择</option>");
            $("#bj").html("<option value=''>请选择</option>");
        } else {
            if (orgLevel == '3') {
                $("#qx_or_xx").html("<option value=''>请选择</option>");
                $("#nj").html("<option value=''>请选择</option>");
            }
            if (orgLevel == '4') {

                $("#nj").html("<option value=''>请选择</option>");
            }
        }
    }
</script>