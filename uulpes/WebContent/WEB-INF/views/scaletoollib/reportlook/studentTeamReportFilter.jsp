<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link rel="stylesheet" type="text/css"
    href="../../themes/theme1/css/jquery.multiselect.css" />
<script type="text/javascript"
    src="../../js/jqueryplugin/multiselect/jquery.multiselect.min.js"></script>
<c:choose>
    <c:when test="${orgType=='2' }">
        <c:set var="stuTeamReportsurl"
            value="../../scaletoollib/reportlook/stuTeamReportsSchoolurl.do"></c:set>
    </c:when>
    <c:otherwise>
        <c:if test="${orgLevel==3 }">
            <c:set var="stuTeamReportsurl"
                value="../../scaletoollib/reportlook/stuTeamReportsEduCityurl.do"></c:set>
        </c:if>
        <c:if test="${orgLevel==4 }">
            <c:set var="stuTeamReportsurl"
                value="../../scaletoollib/reportlook/stuTeamReportsEduCountryurl.do"></c:set>
        </c:if>
    </c:otherwise>
</c:choose>
<form:form id="studentTeamReportForm" name="studentTeamReportForm"
    method="post" commandName="reportLookStudentFilterParam"
    action="${stuTeamReportsurl }" target="_blank">
    <c:choose>
        <c:when test="${orgType=='2'}">
            <div class="filterContent">
                <ul>
                    <li><label class="name03">学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;段</label>
                        <form:select class="select_160" id="xd"
                            path="xd" onchange="getNj('2');">
                            <form:option value="-1" id="se_export">请选择</form:option>
                            <form:options name="xdse_export"
                                items="${xdList }" itemLabel="xdmc"
                                itemValue="xd" />
                        </form:select></li>
                    <li><label class="name03">选择年级</label> <form:select
                            class="select_160" id="nj" path="nj"
                            onchange="queryBj();">
                            <form:option value="-1">请选择</form:option>
                        </form:select></li>
                    <li><label class="name03">选择班级</label> <select
                        class="multiselec_160" id="bj">
                    </select></li>
                </ul>
                <input id="bjarray" name="bjarray" type="hidden" />
            </div>

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
                            <select id="qx_or_xx" class="multiselec_160">
                        </select></li>
                        <li><label class="name03">学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;段</label>
                            <form:select class="select_160" id="xd"
                                path="xd" onchange="getNj('3');">
                                <form:option value="-1" id="se_export">请选择</form:option>
                                <form:option value="1">小学</form:option>
                                <form:option value="2">初中</form:option>
                                <form:option value="3">高中</form:option>
                            </form:select></li>
                        <li><label class="name03">年级名称</label> <form:select
                                id="nj" path="nj" class="select_160"
                                type="text" onchange="changeNj(3);">
                                <form:option value="-1">请选择</form:option>
                            </form:select></li>
                    </ul>
                    <input id="qxorxxarray" name="qxorxxarray"
                        type="hidden" />
                </div>

            </c:if>
            <c:if test="${orgLevel=='4'}">
                <div class="filterContent">
                    <ul>
                        <li><label class="name03">选择学校</label> <select
                            id="qx_or_xx" class="multiselec_160">
                                <c:forEach var="item"
                                    items="${schoolList}">
                                    <option value="${item.id}">${item.name}</option>
                                </c:forEach>
                        </select></li>
                        <li><label class="name03">学段</label> <form:select
                                class="select_160" id="xd" path="xd"
                                onchange="getNj('4');">
                                <form:option value="-1" id="se_export">请选择</form:option>
                                <form:option value="1">小学</form:option>
                                <form:option value="2">初中</form:option>
                                <form:option value="3">高中</form:option>
                            </form:select></li>
                        <li><label class="name03">年级名称</label> <form:select
                                id="nj" path="nj" class="select_160"
                                type="text" onchange="changeNj(4);">
                                <form:option value="-1">请选择</form:option>
                                <form:options items="${njList }"
                                    itemLabel="njmc" itemValue="njmc" />
                            </form:select></li>
                    </ul>
                    <input id="qxorxxarray" name="qxorxxarray"
                        type="hidden" />
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
    <div class="filterContent">
        <ul>
            <li><label class="name03">量表类别</label> <form:select
                    class="select_160" id="scaleType" path="scaleTypeId"
                    onchange="changeScaleTypeOrSource('${orgType}','${orgLevel }')">
                    <form:option value="-1" label="请选择"></form:option>
                </form:select></li>
            <li><label class="name03">量表来源</label> <form:select
                    class="select_160" id="scaleSource"
                    path="scaleSourceId"
                    onchange="changeScaleTypeOrSource('${orgType}','${orgLevel }')">
                    <form:option value="-1" label="请选择"></form:option>
                </form:select></li>
            <li><label class="name03">量表名称</label> <form:select
                    id="scaleName" path="scaleName" class="select_160">
                    <form:option value="-1">请选择</form:option>
                </form:select></li>
        </ul>
    </div>
    <div class="filterContent">
        <ul>
            <li><label class="name03">评测时间</label> <form:input
                    path="starttime" id="starttimeTeam"
                    class="input_160" type="text"></form:input></li>
            <li><label class="name03">至</label> <form:input
                    path="endtime" id="endtimeTeam" class="input_160"
                    type="text"></form:input></li>
        </ul>
    </div>
    <div class="buttonContent">
        <input id="exportTeamReport" class="button-small blue"
            type="button" value="生成团体报告"
            onclick="createTeamReport('${orgType}','${orgLevel}');" />
        <input class="button-small blue" type="button" value="重置"
            onclick="teamReset('${orgType}','${orgLevel}');" />
    </div>
</form:form>
<script type="text/javascript">
    var qxvs, bjvs;
    $(function() {
        $("#starttimeTeam").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
        $("#endtimeTeam").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
        bjvs = $("#bj").multiselect({
            checkAllText : "全选",
            uncheckAllText : '全不选'
        });
        qxvs = $("#qx_or_xx").multiselect({
            checkAllText : "全选",
            uncheckAllText : '全不选',
            selectedList : 4,
        });

        $(".multiselec").css("width", "154px");
    });

    function downloadreport() {
        var action = $("#studentTeamReportForm").attr("action");
        action = action + "?download=yes";
        $("#studentTeamReportForm").attr("action", action);
        $("#studentTeamReportForm").submit();
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
    function queryBj() {

        var nj = document.getElementById("nj").value;
        if (nj == -1) {
            $("#bj").html("<option value='-1'>请选择</option>");
            return;
        }
        var xd = document.getElementById("xd").value;
        if (xd == -1) {
            return;
        }
        $.ajax({
            type : "POST",
            url : "../../ajax/queryBJAndScaleFromExamresultStudentInSchool.do",
            data : {
                "xd" : xd,
                "nj" : nj,
                "bj" : '-1'
            },
            success : function(msg) {
                debugger;
                var bj = $("#bj");
                var scaleselect = $("#scaleName");
                var callbackarray = jQuery.parseJSON(msg);
                var bjarray = callbackarray.ersList;
                var scaleList = callbackarray.scaleList;
                //var bjarray = jQuery.parseJSON(msg);
                if (bjarray.length > 0) {
                    var optionstr = "";
                    for (var i = 0; i < bjarray.length; i++) {
                        var bjid = bjarray[i].bj;
                        var bjmc = bjarray[i].bjmc;
                        optionstr = optionstr + "<option value="+bjid+">" + bjmc + "</option>";
                    }
                    bj.html(optionstr);
                    bjvs.multiselect('refresh');
                } else {
                    bj.html("");
                    bj.html("<option value='-1'>请选择</option>");
                }

                if (scaleList.length > 0) {
                    var optionstr1 = "<option value='-1'>请选择</option>";
                    for (var i = 0; i < scaleList.length; i++) {
                        var scale = scaleList[i];
                        var scaleid = scale.code;
                        var scalename = scale.title;

                        optionstr1 = optionstr1 + "<option value="+scaleid+">" + scalename + "</option>";
                    }
                    scaleselect.html(optionstr1);
                } else {
                    scaleselect.html("");
                    scaleselect.html("<option value='-1'>请选择</option>");
                }
                var scaleTypeMap = callbackarray.scaleTypeMap;
                if (scaleList.length > 0) {
                    $("#scaleType").empty();
                    $("#scaleType").append("<option value='-1'>请选择</option>");
                    for ( var typeid in scaleTypeMap) {//用javascript的for/in循环遍历对象的属性 
                        var typename = scaleTypeMap[typeid];
                        $("#scaleType").append("<option value='"+typeid+"'>" + typename + "</option>");
                    }
                } else {
                    $("#scaleType").html("");
                    $("#scaleType").html("<option value='-1'>请选择</option>");
                }
                var scaleSourceMap = callbackarray.scaleSourceMap;
                $("#scaleSource").empty();
                $("#scaleSource").append("<option value='-1'>请选择</option>");
                for ( var sourceid in scaleSourceMap) {//用javascript的for/in循环遍历对象的属性 
                    var sourcename = scaleSourceMap[sourceid];
                    $("#scaleSource").append("<option value='"+sourceid+"'>" + sourcename + "</option>");
                }

            },
            error : function() {
                layer.open({
                    content : "调用出现错误，查找班级失败"
                });
            }
        });
    }
    function changeScaleTypeOrSource(orgType, orgLevel) {
        debugger;
        var xd = document.getElementById("xd").value;
        if (xd == '-1') {
            layer.open({
                content : '请选择学段!'
            });
            return;
        }

        var nj = document.getElementById("nj").value;
        if (nj == '-1') {
            layer.open({
                content : '请选择年级!'
            });
            return;
        }
        var countyidOrSchoolid = null;
        if (orgType == '2')
            countyidOrSchoolid = null;
        else
            countyidOrSchoolid = document.getElementById("qx_or_xx").value;
        var xzxs = null;
        if (orgLevel == 3)
            xzxs = document.getElementById("xzxs").value;

        var scaletype = document.getElementById("scaleType").value;
        var scalesource = document.getElementById("scaleSource").value;
        $.ajax({
            type : "POST",
            url : "../../scaletoollib/reportlook/getEduScaleInfoFromExamdoStudent1.do",
            data : {
                "countyidOrSchoolid" : countyidOrSchoolid,
                "xzxs" : xzxs,
                "xd" : xd,
                "nj" : nj,
                "scaletype" : scaletype,
                "scalesource" : scalesource
            },
            success : function(msg) {
                debugger;
                if (msg != '[]') {
                    var callbackarray = jQuery.parseJSON(msg);
                    var scaleList = callbackarray.scaleList;

                    $("#scaleName").empty();
                    $("#scaleName").append("<option value='-1'>请选择</option>");
                    for (var i = 0; i < scaleList.length; i++) {
                        var scale = scaleList[i];
                        var scaleid = scale.code;
                        var scalename = scale.title;
                        $("#scaleName").append("<option value='"+scaleid+"'>" + scalename + "</option>");
                    }
                } else {

                }
            },
            error : function() {
            }
        });

    }
    function changeNj(orgLevel) {
        debugger;
        var countyidOrSchoolid = null;
        if (orgLevel == 2)
            countyidOrSchoolid = null;
        else
            countyidOrSchoolid = document.getElementById("qx_or_xx").value;
        var nj = document.getElementById("nj").value;
        if (nj == '-1') {
            $("#scaleType").empty();
            $("#scaleType").append("<option value='-1'>请选择</option>");
            $("#scaleSource").empty();
            $("#scaleSource").append("<option value='-1'>请选择</option>");
            $("#scaleName").empty();
            $("#scaleName").append("<option value='-1'>请选择</option>");
            return;
        }
        var xzxs = null;
        if (orgLevel == 3)
            xzxs = document.getElementById("xzxs").value;
        var xd = document.getElementById("xd").value;
        if (xzxs == '1') {

        }
        $.ajax({
            type : "POST",
            url : "../../scaletoollib/reportlook/getEduScaleInfoFromExamdoStudent.do",
            data : {
                "countyidOrSchoolid" : countyidOrSchoolid,
                "xzxs" : xzxs,
                "xd" : xd,
                "nj" : nj
            },
            success : function(msg) {
                debugger;
                if (msg != '[]') {
                    var callbackarray = jQuery.parseJSON(msg);
                    var scaleList = callbackarray.scaleList;

                    $("#scaleName").empty();
                    $("#scaleName").append("<option value='-1'>请选择</option>");
                    for (var i = 0; i < scaleList.length; i++) {
                        var scale = scaleList[i];
                        var scaleid = scale.code;
                        var scalename = scale.title;
                        $("#scaleName").append("<option value='"+scaleid+"'>" + scalename + "</option>");
                    }

                    var scaleTypeMap = callbackarray.scaleTypeMap;
                    $("#scaleType").empty();
                    $("#scaleType").append("<option value='-1'>请选择</option>");
                    for ( var typeid in scaleTypeMap) {//用javascript的for/in循环遍历对象的属性 
                        var typename = scaleTypeMap[typeid];
                        $("#scaleType").append("<option value='"+typeid+"'>" + typename + "</option>");
                    }
                    var scaleSourceMap = callbackarray.scaleSourceMap;
                    $("#scaleSource").empty();
                    $("#scaleSource").append("<option value='-1'>请选择</option>");
                    for ( var sourceid in scaleSourceMap) {//用javascript的for/in循环遍历对象的属性 
                        var sourcename = scaleSourceMap[sourceid];
                        $("#scaleSource").append("<option value='"+sourceid+"'>" + sourcename + "</option>");
                    }
                } else {
                    document.getElementById("nj").innerHTML = "<option value='-1'>请选择</option>";
                    document.getElementById("scaleType").innerHTML = "<option value='-1'>请选择</option>";
                    document.getElementById("scaleSource").innerHTML = "<option value='-1'>请选择</option>";
                    document.getElementById("scaleName").innerHTML = "<option value='-1'>请选择</option>";
                }
            },
            error : function() {
                layer.open({
                    content : "根据年级展现班级出错!"
                });
            }
        });

    }
    function getBJ1() {
        var njmc = document.getElementById("nj").value;
        if (njmc == -1) {
            $("#bj").html("<option value='-1'>请选择</option>");
            $("#bjcontainer").html("");
            return;
        }
        $.ajax({
            type : "POST",
            url : "../../scaletoollib/reportlook/queryDistinctBJFromExamresultStudentByNJ.do",
            data : {
                "njmc" : njmc
            },
            success : function(msg) {
                var bj = $("#bjcontainer");
                var bjarray = jQuery.parseJSON(msg);
                if (bjarray.length > 0) {
                    var optionstr = "<div style='text-align:left;'>";
                    for (var i = 0; i < bjarray.length; i++) {
                        var bjj = bjarray[i].bj;
                        var bjmc = bjarray[i].bjmc;
                        optionstr = optionstr
                                + "<span style='margin-left:10px;'><input type='checkbox' name='bjarray' id='"+bjj+"' value='"+bjmc+"'><label for='"+bjj+"'>"
                                + bjmc + "</label></span>";
                        if ((i + 1) % 3 == 0) {
                            optionstr = optionstr + "</div>";
                            optionstr = optionstr + "<div style='text-align:left;'>";
                        }
                    }
                    optionstr = optionstr + "</div>";
                    bj.html(optionstr);
                } else {
                    bj.html("");
                }
            },
            error : function() {
                layer.open({
                    content : "调用出现错误，查找班级失败"
                });
            }
        });
    }
    function queryQXXXFun(cityId) {
        debugger;
        var xzxs = document.getElementById("xzxs").value;
        if (xzxs == "-1") {
            document.getElementById("xzqh").innerHTML = "<option value='-1'>请选择</option>";
            return;
        }
        if (xzxs == "1") {
            $("#qxorxx_label").text("选择区县");
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
                        //$("#qx_or_xx").append("<option value='-1'>请选择</option>");
                        for (var i = 0; i < callbackarray.length; i++) {
                            var quxian = callbackarray[i];
                            var code = quxian.code;
                            var name = quxian.name;
                            $("#qx_or_xx").append("<option value='"+code+"'>" + name + "</option>");
                        }
                        qxvs.multiselect('refresh');
                    } else {
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
                        $("#qx_or_xx").append("<option value='-1'>请选择</option>");
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
    function queryQXXXFun1(cityId) {
        $("#qx_or_xxcontainer").html("");
        var xzxs = document.getElementById("xzxs").value;
        if (xzxs == "-1") {
            $("#qx_or_xxcontainer").html("");
            return;
        }
        if (xzxs == "1") {
            $("#qxOrxx").html("区县");
            $.ajax({
                type : "POST",
                url : "../../ajax/getQuxianStr.do",
                data : {
                    "cityId" : cityId
                },
                success : function(msg) {
                    if (msg != '[]') {
                        var callbackarray = jQuery.parseJSON(msg);
                        $("#qx_or_xxcontainer").html("");
                        var optionstr = "<div style='text-align:left;'>";
                        for (var i = 0; i < callbackarray.length; i++) {
                            var quxian = callbackarray[i];
                            var code = quxian.code;
                            var name = quxian.name;
                            optionstr = optionstr
                                    + "<span style='margin-left:10px;'><input type='checkbox' name='qxarray' id='"+code+"' value='"+name+"'><label for='"+code+"'>"
                                    + name + "</label></span>";
                            if ((i + 1) % 2 == 0) {
                                optionstr = optionstr + "</div>";
                                optionstr = optionstr + "<div style='text-align:left;'>";
                            }
                        }
                        optionstr = optionstr + "</div>";
                        $("#qx_or_xxcontainer").html(optionstr);
                    } else {
                        $("#qx_or_xxcontainer").html("");
                    }
                },
                error : function() {
                    layer.open({
                        content : "根据年级展现班级出错!"
                    });
                }
            });
        } else {
            $("#qxOrxx").html("直属学校");
            $.ajax({
                type : "POST",
                url : "../../ajax/getSonSchoolsStr.do",
                data : {
                    "cityId" : cityId
                },
                success : function(msg) {
                    if (msg != '[]') {
                        var callbackarray = jQuery.parseJSON(msg);
                        $("#qx_or_xxcontainer").html("");
                        var optionstr = "<div style='text-align:left;'>";
                        for (var i = 0; i < callbackarray.length; i++) {
                            var classSchool = callbackarray[i];
                            var classId = classSchool.id;
                            var name = classSchool.name;
                            optionstr = optionstr
                                    + "<span style='margin-left:10px;'><input type='checkbox' name='zsxxarray' id='"+classId+"' value='"+name+"'><label for='"+classId+"'>"
                                    + name + "</label></span>";
                            if ((i + 1) % 2 == 0) {
                                optionstr = optionstr + "</div>";
                                optionstr = optionstr + "<div style='text-align:left;'>";
                            }
                        }
                        optionstr = optionstr + "</div>";
                        $("#qx_or_xxcontainer").html(optionstr);
                    } else {
                        $("#qx_or_xxcontainer").html("");
                    }
                },
                error : function() {
                    layer.open({
                        content : "根据年级展现班级出错!"
                    });
                }
            });
        }
    }

    function openTeamReport() {

        var w = window.open();
        $.ajax({
            type : 'POST',
            async : false,
            url : '${stuTeamReportsurl }',
            error : function() {
                w.location = "www.baidu.com";
            },
            success : function(res) {
                layer.open({
                    content : res
                });

            }
        });

    }
    function teamReset(orgType, orgLevel) {
        $("#studentTeamReportForm")[0].reset();
        if (orgType == '2') {
            $("#nj").html("<option value='-1'>请选择</option>");
            $("#bj").html("<option value='-1'>请选择</option>");
            $("#bj").val('');
        } else {
            if (orgLevel == '3') {
                $("#qx_or_xx").html("<option value='-1'>请选择</option>");
                $("#nj").html("<option value='-1'>请选择</option>");
            }
            if (orgLevel == '4') {
                $("#nj").html("<option value='-1'>请选择</option>");
            }
        }
    }
    function createTeamReport(orgType, orgLevel) {
        debugger;
        var scale = document.getElementById("scaleName").value;
        if (scale == '-1') {
            layer.open({
                content : "请选择量表!"
            });
            return;
        }
        if (orgType == '2') {
            if ($("#bj")) {
                var bjarrayJson = JSON.stringify($("#bj").multiselect("getChecked").map(function() {
                    return this.value;
                }).get());
                $("#bjarray").val(bjarrayJson);
            }
        } else {
            if (orgLevel == '3') {
                if ($("#qx_or_xx")) {
                    var qxOrxxJson = JSON.stringify($("#qx_or_xx").multiselect("getChecked").map(function() {
                        return this.value;
                    }).get());
                    $("#qxorxxarray").val(qxOrxxJson);
                }
            }
            if (orgLevel == '4') {
                if ($("#qx_or_xx")) {
                    var qxOrxxJson = JSON.stringify($("#qx_or_xx").multiselect("getChecked").map(function() {
                        return this.value;
                    }).get());
                    $("#qxorxxarray").val(qxOrxxJson);
                }
            }
        }
        var starttimeTeam = document.getElementById("starttimeTeam").value;
        var endtimeTeam = document.getElementById("endtimeTeam").value;
        if (starttimeTeam == '' || starttimeTeam == null) {
            layer.open({
                content : '请输入开始时间!'
            });
            return;
        }
        if (endtimeTeam == '' || endtimeTeam == null) {
            layer.open({
                content : '请输入终止时间!'
            });
            return;
        }
        $("#studentTeamReportForm").submit();
    }
</script>