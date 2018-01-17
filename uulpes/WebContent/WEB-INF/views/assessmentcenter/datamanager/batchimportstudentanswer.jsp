<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx}/assessmentcenter/datamanager" />
<form:form id="dispenseform" name="importFilterParamForm" method="post" commandName="dataManageFilterParam"
    action="${baseaction}/downloadStudentAnswerTemp.do">
    <c:choose>
        <c:when test="${orgType=='2' }">
            <div class="filterContent">
                <ul>
                    <li>
                        <label class="name03">年级</label>
                        <form:select class="input_160" path="nj" onchange="getClassAccordingGrade();" id="nj">
                            <form:option value="-1" label="请选择" id="se_import"></form:option>
                            <form:options name="xdse_import" items="${gradeList }" itemValue="gradeid" itemLabel="njmc"></form:options>
                        </form:select>
                    </li>
                    <li>
                        <label class="name03">班级</label>
                        <form:select class="input_160" onchange="changeBj();" id="bj" path="bj">
                            <form:option value="-1">请选择</form:option>
                        </form:select>
                    </li>
                    <li>
                        <label class="name03">性别</label>
                        <form:select class="input_160" path="gender">
                            <form:option value="-1">请选择</form:option>
                            <form:option value="1">男</form:option>
                            <form:option value="2">女</form:option>
                        </form:select>
                    </li>
                </ul>
            </div>
            <div class="filterContent">
                <ul>
                    <li>
                        <label class="name03">量表类别</label>
                        <form:select class="input_160" id="scaleType" path="scaleTypeId"
                            onchange="selectDistinctScaleAcorStype()">
                            <form:option value="-1" label="请选择"></form:option>
                            <form:options items="${scaleTypes }" itemValue="id" itemLabel="name"></form:options>
                        </form:select>
                    </li>
                    <li>
                        <label class="name03">量表来源</label>
                        <select id="scaleSource" onchange="selectDistinctScaleBySource()" class="input_160">
                            <option value="-1">请选择</option>
                            <c:forEach var="scaleSource" items="${scaleSources }">
                                <option value="${scaleSource.id }">${scaleSource.name }</option>
                            </c:forEach>
                        </select>
                    </li>
                </ul>
                <ul>
                    <li>
                        <label class="name03">量表名称</label>
                        <form:select class="input_160" id="scaleName" path="scaleId">
                            <form:option value="-1">请选择</form:option>
                            <form:options items="${scaleList }" itemLabel="shortname" itemValue="id" />
                        </form:select>
                    </li>
                </ul>
            </div>
        </c:when>
    </c:choose>
    <div class="buttonContent">
        <input value="下载答题模板" class="button-small blue" type="button" onclick="downloadTemplate()">
        <input class="button-small blue" type="button" value="重置" /></input>
        </ul>
    </div>
</form:form>
<div></div>
<form id="answeruploadform" action="${baseaction}/uploadStudentAnswer.do" method="post">
    <div class="buttonContent">
        <label style="margin-right: 20px;">选择答题模板</label>
        <input class="input_600" type="file" name="file">
        <input class="button-small blue" type="button" value="批量导入" onclick="uploadStudentAnswer()" /></input>
    </div>
</form>
<div></div>
<div id="dataloading"></div>
<div id="uploadresult"></div>
<script type="text/javascript">
    function downloadTemplate() {
        if (!$("#nj") || $("#nj").val() == "-1") {
            alert("年级不能为空!");
        } else if (!$("#scaleName") || $("#scaleName").val() == "-1") {
            alert("量表名称不能为空!");
        } else {
            $("#dispenseform").submit();
        }
    }

    function uploadStudentAnswer() {
        $("#dataloading").css("display", "block");
        $("#answeruploadform").ajaxSubmit({
            target : "#uploadresult",
            success : function(r) {
                $("#dataloading").css("display", "none");
                if (r.status == 'error') {

                } else if (r.status == 'success') {

                }
            },
            error : function(XmlHttpRequest, textStatus, errorThrown) {
                $("#dataloading").css("display", "none");
            }
        });
    }
    function getClassAccordingGrade() {
        debugger;
        var typeOptions = document.getElementById("scaleType").options;
        typeOptions[0].selected = true;
        var sourceOptions = document.getElementById("scaleSource").options;
        sourceOptions[0].selected = true;
        var gradeid = document.getElementById("nj").value;
        //if(gradeid=="-1"){
        document.getElementById("bj").innerHTML = "<option value='-1'>请选择</option>";
        //return;
        //}
        $.ajax({
            type : "POST",
            url : "../../ajax/getClassesAndScaleTypeByGradeId.do",
            data : {
                "gradeid" : gradeid
            },
            success : function(msg) {
                if (msg != '[]') {
                    var callbackarray = jQuery.parseJSON(msg);
                    var htmlstr = "";
                    var classarray = callbackarray.classList;
                    $("#bj").empty();
                    $("#bj").append("<option value='-1'>请选择</option>");
                    //var htmlstr="";
                    for (var i = 0; i < classarray.length; i++) {
                        var classSchool = classarray[i];
                        var classId = classSchool.id;
                        var bjmc = classSchool.bjmc;
                        $("#bj").append("<option value='"+classId+"'>" + bjmc + "</option>");
                        //htmlstr= htmlstr+"<option value='"+classId+"'>"+bjmc+"</option>";
                    }
                    /* var scaletypeArray=callbackarray.scaletypeList;
                    //var htmlstr="";
                    $("#scaleType").empty();
                    $("#scaleType").append("<option value='-1'>请选择</option>");
                    for(var i=0;i<scaletypeArray.length;i++){
                    	var scaletype = scaletypeArray[i];
                    	var typeId = scaletype.id;
                    	var title = scaletype.name;
                    	$("#scaleType").append("<option value='"+typeId+"'>"+title+"</option>");
                    } */
                    //var selectnode=document.getElementById("scaleType");
                    //selectnode.innerHTML=htmlstr;
                    var scaleList = callbackarray.scaleList;
                    $("#scaleName").empty();
                    $("#scaleName").append("<option value='-1'>请选择</option>");
                    for (var i = 0; i < scaleList.length; i++) {
                        var scaleinfo = scaleList[i];
                        var scaleid = scaleinfo.id;
                        var scalename = scaleinfo.shortname;
                        $("#scaleName").append("<option value='"+scaleid+"'>" + scalename + "</option>");
                    }

                } else {
                    /* document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
                    document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>"; */
                }
            },
            error : function() {
                layer.open({
                    content : '根据年级展现班级出错!'
                });
            }
        });
    }
    function changeBj() {
        var bjid = document.getElementById("bj").value;
        $.ajax({
            type : "POST",
            url : "../../ajax/selectDistinctScaleByGradeId.do",
            data : {
                "bjid" : bjid
            },
            success : function(msg) {
                if (msg != '[]') {
                    debugger;
                    var callbackarray = jQuery.parseJSON(msg);
                    var htmlstr = "";
                    $("#scaleName").empty();
                    $("#scaleName").append("<option value='-1'>请选择</option>");
                    //var htmlstr="";
                    for (var i = 0; i < callbackarray.length; i++) {
                        var scaleinfo = callbackarray[i];
                        var scaleid = scaleinfo.id;
                        var scalename = scaleinfo.shortname;
                        $("#scaleName").append("<option value='"+scaleid+"'>" + scalename + "</option>");
                    }
                } else {
                    document.getElementById("scaleName").innerHTML = "<option value='-1'>请选择</option>";
                }
            },
            error : function() {
                layer.open({
                    content : '不存在改班级的分发量表!'
                });
            }
        });
    }
    function selectDistinctScaleBySource() {
        debugger;
        var gradeid = document.getElementById("nj").value;
        var bjid = document.getElementById("bj").value;
        var sourceid = document.getElementById("scaleSource").id;
        if (sourceid == "-1") {
            //document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
            return;
        }
        debugger;
        $.ajax({
            type : "POST",
            url : "../../ajax/selectDistinctScaleBySource.do",
            data : {
                "gradeid" : gradeid,
                "bjid" : bjid,
                "sourceid" : sourceid
            },
            success : function(msg) {
                if (msg != '[]') {
                    debugger;
                    var callbackarray = jQuery.parseJSON(msg);
                    var htmlstr = "";
                    $("#scaleName").empty();
                    $("#scaleName").append("<option value='-1'>请选择</option>");
                    //var htmlstr="";
                    for (var i = 0; i < callbackarray.length; i++) {
                        var scaleinfo = callbackarray[i];
                        var scaleid = scaleinfo.id;
                        var scalename = scaleinfo.shortname;
                        $("#scaleName").append("<option value='"+scaleid+"'>" + scalename + "</option>");
                    }
                } else {
                    document.getElementById("scaleName").innerHTML = "<option value='-1'>请选择</option>";
                }
            },
            error : function() {
                layer.open({
                    content : '不存在该量表类型的量表!'
                });
            }
        });
    }
    function selectDistinctScaleAcorStype() {
        debugger;
        var gradeid = document.getElementById("nj").value;
        var bjid = document.getElementById("bj").value;
        var typeid = document.getElementById("scaleType").value;
        if (typeid == "-1") {
            //document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
            return;
        }
        debugger;
        $.ajax({
            type : "POST",
            url : "../../ajax/selectDistinctScaleAcorStype.do",
            data : {
                "gradeid" : gradeid,
                "bjid" : bjid,
                "typeid" : typeid
            },
            success : function(msg) {
                if (msg != '[]') {
                    debugger;
                    var callbackarray = jQuery.parseJSON(msg);
                    var htmlstr = "";
                    $("#scaleName").empty();
                    $("#scaleName").append("<option value='-1'>请选择</option>");
                    //var htmlstr="";
                    for (var i = 0; i < callbackarray.length; i++) {
                        var scaleinfo = callbackarray[i];
                        var scaleid = scaleinfo.id;
                        var scalename = scaleinfo.shortname;
                        $("#scaleName").append("<option value='"+scaleid+"'>" + scalename + "</option>");
                    }
                } else {
                    document.getElementById("scaleName").innerHTML = "<option value='-1'>请选择</option>";
                }
            },
            error : function() {
                layer.open({
                    content : '不存在该量表类型的量表!'
                });
            }
        });
    }
    function selectDistinctScaleBySource() {
        var gradeid = document.getElementById("nj").value;
        var bjid = document.getElementById("bj").value;
        var sourceid = document.getElementById("scaleSource").value;
        if (sourceid == "-1") {
            //document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
            return;
        }
        debugger;
        $.ajax({
            type : "POST",
            url : "../../ajax/selectDistinctScaleBySource.do",
            data : {
                "gradeid" : gradeid,
                "bjid" : bjid,
                "sourceid" : sourceid
            },
            success : function(msg) {
                if (msg != '[]') {
                    debugger;
                    var callbackarray = jQuery.parseJSON(msg);
                    var htmlstr = "";
                    $("#scaleName").empty();
                    $("#scaleName").append("<option value='-1'>请选择</option>");
                    //var htmlstr="";
                    for (var i = 0; i < callbackarray.length; i++) {
                        var scaleinfo = callbackarray[i];
                        var scaleid = scaleinfo.id;
                        var scalename = scaleinfo.shortname;
                        $("#scaleName").append("<option value='"+scaleid+"'>" + scalename + "</option>");
                    }
                } else {
                    document.getElementById("scaleName").innerHTML = "<option value='-1'>请选择</option>";
                }
            },
            error : function() {
                layer.open({
                    content : '不存在该量表来源的量表!'
                });
            }
        });
    }

    function changeScaleType() {
        var typeid = document.getElementById("scaleType").value;
        if (typeid == "-1") {
            document.getElementById("scaleName").innerHTML = "<option value='-1'>请选择</option>";
            return;
        }
        $.ajax({
            type : "POST",
            url : "../../ajax/getScaleByType.do",
            data : {
                "typeid" : typeid
            },
            success : function(msg) {
                if (msg != '[]') {
                    debugger;
                    var callbackarray = jQuery.parseJSON(msg);
                    var htmlstr = "";
                    $("#scaleName").empty();
                    $("#scaleName").append("<option value='-1'>请选择</option>");
                    //var htmlstr="";
                    for (var i = 0; i < callbackarray.length; i++) {
                        var scaleinfo = callbackarray[i];
                        var scaleid = scaleinfo.code;
                        var scalename = scaleinfo.title;
                        $("#scaleName").append("<option value='"+scalename+"'>" + scalename + "</option>");
                    }

                } else {
                    document.getElementById("scaleName").innerHTML = "<option value='-1'>请选择</option>";
                }
            },
            error : function() {
                layer.open({
                    content : '不存在该量表类型的量表!'
                });
            }
        });
    }
</script>