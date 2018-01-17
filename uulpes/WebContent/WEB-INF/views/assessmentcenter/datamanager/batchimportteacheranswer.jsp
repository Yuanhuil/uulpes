<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx}/assessmentcenter/datamanager" />
<form id="dispenseform" action="${baseaction}/downloadTeacherAnswerTemp.do" method="post">
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">角色名称</label>
                <select name="roleid" id="roleid" class="input_160" onchange="changeRole();">
                    <option value="-1">所有教师</option>
                    <c:forEach var="role" items="${roleList }">
                        <option value="${role.id }">${role.roleName }</option>
                    </c:forEach>
                </select>
            </li>
        </ul>
    </div>
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">量表类别</label>
                <select id="scaleType" name="scaleType" onchange="selectDistinctScaleAcorStype();" class="input_160">
                    <option value="-1">请选择</option>
                    <c:forEach var="scaletype" items="${scaleTypes}">
                        <option value="${scaletype.id}">${scaletype.name}</option>
                    </c:forEach>
                </select>
            </li>
            <li>
                <label class="name03">量表来源</label>
                <select id="scaleSource" onchange="selectDistinctScaleBySource();" class="input_160">
                    <option value="-1">请选择</option>
                    <c:forEach var="scaleSource" items="${scaleSources }">
                        <option value="${scaleSource.id }">${scaleSource.name }</option>
                    </c:forEach>
                </select>
            </li>
            <li>
                <label class="name03">量表名称</label>
                <select id="scaleName" name="scaleName" class="input_160">
                    <option value="-1">请选择</option>
                    <options items="${scaleList }" itemLabel="shortname" itemValue="id" />
                </select>
            </li>
        </ul>
    </div>
    <div class="buttonContent">
        <input value="下载答题模板" class="button-small blue" type="button" onclick="downloadTemplate()">
        <input class="button-small blue" type="button" value="重置" /></input>
    </div>
</form>
<div></div>
<form id="answeruploadform" action="${baseaction}/uploadTeacherAnswer.do" method="post">
    <div class="buttonContent">
        <label style="margin-right: 20px;">选择答题模板</label>
        <input class="input_600" type="file" name="file">
        <input class="button-small blue" type="button" value="批量导入" onclick="uploadTeacherAnswer()" /></input>
    </div>
</form>
<div></div>
<div id="dataloading"></div>
<div id="uploadresult"></div>
<script type="text/javascript">
    function downloadTemplate() {
        if (!$("#scaleName") || $("#scaleName").val() == "-1") {
            alert("量表名称不能为空!");
        } else {
            $("#dispenseform").submit();
        }
    }

    $(document).ready(function() {
        //queryScalesForTeacher();
    });
    function changeRole() {
        var typeOptions = document.getElementById("scaleType").options;
        typeOptions[0].selected = true;
        var sourceOptions = document.getElementById("scaleSource").options;
        sourceOptions[0].selected = true;

        var roleid = document.getElementById("roleid").value;
        if (roleid == "-1") {

        }
        $.ajax({
            type : "POST",
            url : "../../ajax/selectDistinctScaleByRoleId.do",
            data : {
                "roleid" : roleid
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
    function uploadTeacherAnswer() {
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
    function selectDistinctScaleAcorStype() {
        debugger;
        var roleid = document.getElementById("roleid").value;
        var typeid = document.getElementById("scaleType").value;
        var sourceid = document.getElementById("scaleSource").value;
        if (typeid == "-1") {
            //document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
            return;
        }
        debugger;
        $.ajax({
            type : "POST",
            url : "../../ajax/selectDistinctScaleAcorStype.do",
            data : {
                "gradeid" : null,
                "bjid" : null,
                "roleid" : roleid,
                "typeid" : typeid,
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
    function selectDistinctScaleBySource() {
        debugger;
        var roleid = document.getElementById("roleid").value;
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
                "roleid" : roleid,
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

    function queryScalesForTeacher() {
        var typeid = document.getElementById("scaleType").value;
        var sourceid = document.getElementById("scaleSource").value;
        $.ajax({
            type : "POST",
            url : "../../ajax/queryScales.do",
            data : {
                "gradeid" : 14,
                "typeid" : typeid,
                "sourceid" : sourceid
            },
            success : function(msg) {
                if (msg != '[]') {
                    var callback = jQuery.parseJSON(msg);
                    var htmlstr = "";
                    debugger;
                    var scaleArray = callback;
                    $("#scaleName").empty();
                    $("#scaleName").append("<option value='-1'>请选择</option>");
                    for (var i = 0; i < scaleArray.length; i++) {
                        var scale = scaleArray[i];
                        var typeId = scale.code;
                        var title = scale.title;
                        $("#scaleName").append("<option value='"+typeId+"'>" + title + "</option>");
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
    function queryScales() {
        debugger;
        var typeid = document.getElementById("scaleType").value;
        var sourceid = document.getElementById("scaleSource").value;
        //if(typeid=="-1"){
        //document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
        //return;
        //}
        $.ajax({
            type : "POST",
            url : "../../ajax/queryScales.do",
            data : {
                "gradeid" : 14,
                "typeid" : typeid,
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
                        var scaleid = scaleinfo.code;
                        var scalename = scaleinfo.title;
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
</script>