var xmlhttp;
var studentlist;
var townSchoolArray = null;
// 量表设置变量
var currentScaleId;
var parentVisible = 1;
var teacherVisible = 1;
var studentVisible = 1;
var warningVisible = 0;
var haveLimit = 0;
var normid = -1;

function toXML(strxml) {
    try {
        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.loadXML(strxml);
    } catch (e) {
        var oParser = new DOMParser();
        xmlDoc = oParser.parseFromString(strxml, "text/xml");
    }
    return xmlDoc;
}

function convertURL(url) {
    var timetamp = (new Date()).valueOf();
    if (url.indexOf("?") >= 0) {
        url = url + "&t=" + timetamp;
    } else {
        url = url + "?t=" + timetamp;
    }
    return url;
}
function createXMLHttp() {
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
        if (xmlhttp.overrideMimeType) {
            xmlhttp.overrideMimeType("text/xml");
        }
    } else if (window.ActiveXObject) {
        var activexName = [ "MSXML2.XMLHTTP", "Microsoft.XMLHTTP" ];
        for (var i = 0; i < activexName.length; i++) {
            try {
                xmlhttp = new ActiveXObject(activexName[i]);
                break;
            } catch (e) {
            }
        }
    }
    if (!xmlhttp) {
        return;
    }
}

function strDateTime(str) {
    var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
    if (r == null)
        return false;
    var d = new Date(r[1], r[3] - 1, r[4]);
    return (d.getFullYear() == r[1] && (d.getUTCMonth() + 1) == r[3] && d.getDate() == r[4]);
}
function compdate(starttime, endtime) {
    var sT, sD, eT, eD;
    sT = starttime.split("-"); // 以"-"分割字符串，返回数组；
    eT = endtime.split("-");
    sD = sT[0] + '' + sT[1] + '' + sT[2];
    eD = eT[0] + '' + eT[1] + '' + eT[2];
    return eD - sD >= 0;
}
function clickInfo() {
    var starttime = $("#starttime");
    var endtime = $("#endtime");
    if (starttime.val() == '') {
        layer.open({
            content : '请填写开始时间\r\n'
        });
        starttime.focus();
    } else if (endtime.val() == '') {
        // alert("请填写结束时间\r\n");
        // EV_modeAlert('请填写结束时间味儿阿萨德Wie师大是打发给我发色我发啦沙发上刊登废物卡士大夫Wie副卡饭店为反馈撒分!');
        layer.open({
            content : '请填写结束时间!'
        });
        endtime.focus();
    } else if (!compdate(starttime.val(), endtime.val())) {
        // alert("结束时间必须大于等于开始时间！");
        layer.open({
            content : '结束时间必须大于等于开始时间!'
        });
        // endtime.focus();
    } else
        return true;
}
function infoNext() {
    debugger;
    if (clickInfo()) {
        $("#step1").removeClass().addClass("step1");
        $("#step2").removeClass().addClass("step2_sel");
        $("#info").css("display", "none");
        // alert(orgtype);
        if (orgtype == "1") {// 教委用户分发
            $("#colony").css("display", "none");
            $("#colony").css("display", "inline-table");
            // alert(orglevel);
            if (orglevel == 3)// 市级教委
            {
                //alert("市级教委");
                var orgLevelSel = document.getElementById("orgLevelSel");
                orgLevelSel.length = 0;
                option = new Option("请选择", "");
                orgLevelSel.options.add(option);
                option = new Option("区县", "1");
                orgLevelSel.options.add(option);
                option = new Option("市直属学校", "2");
                orgLevelSel.options.add(option);
                orgLevelSel.focus();
            }
            if (orglevel == 4)// 县级教委
            {
                //alert("县级教委");
                var orgLevel = $("#orgLevel");
                orgLevel.css("display", "none");
                $("#town").css("display", "table-row");
                //setTowns(countyid);// 获得区县下面所有乡镇
                setSchool(countyid);
            }
        }
        if (orgtype == 2) {// 学校用户分发
            // alert("学校用户分发");
            $("#colony").css("display", "inline-table");
            // $("#colony").css("display","none");
        }
        if (orgtype == 3) {// 陶老师工作站分发
            $("#colony").css("display", "none");
            $("#colony").css("display", "inline-table");
            // alert(orglevel);
            if (orglevel == 3)// 陶老师工作站总站
            {
                var orgLevelSel = document.getElementById("orgLevelSel");
                orgLevelSel.length = 0;
                option = new Option("请选择", "");
                orgLevelSel.options.add(option);
                option = new Option("区县", "1");
                orgLevelSel.options.add(option);
                option = new Option("市直属学校", "2");
                orgLevelSel.options.add(option);
                orgLevelSel.focus();
            }
            if (orglevel == 4)// 陶老师工作站分站
            {
                var orgLevel = $("#orgLevel");
                orgLevel.css("display", "none");
                $("#town").css("display", "table-row");
                setTowns(countyid);// 获得区县下面所有乡镇
            }
        }
    }
}

function setSchool(id) {
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getSchoolByOrg.do?areaid=" + id;
    var param = {
        "areaid" : id
    };
    $.post(url, param).success(function(result){
        debugger;
        schoolList = document.getElementById("schoolList");
        if(result!='[]'){
            var schools = jQuery.parseJSON(result);
            setSchoolListFromArray2(schools ,4);
        }
    });
}

function setSchoolListFromArray2(schools,a) {
    if (schools.length > 0) {
            deleteSchools();
            schoolList = document.getElementById("schoolList");
            for (var i = 0; i < schools.length; i++) {
                if (i % a == 0)
                    tr = schoolList.insertRow(i / a);
                if (i != schools.length - 1) {
                    if (schools[i].checked == true) {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    } else {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid'  onclick='schoolboxChange(this)' value="
                            + schools[i].id + ">" + schools[i].name;
                    }
                }else if (i % a == 0) {
                    if (schools[i].checked == true) {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    } else {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid'  onclick='schoolboxChange(this)'  value="
                                + schools[i].id + ">" + schools[i].name;
                    }
                    tr.insertCell(1).innerHTML = "&nbsp;";
                    tr.insertCell(2).innerHTML = "&nbsp;";
                    tr.insertCell(3).innerHTML = "&nbsp;";
                }else if (i % a == 1) {
                    if (schools[i].checked == true) {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    } else {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'   value="
                                + schools[i].id + ">" + schools[i].name;
                    }
                    tr.insertCell(2).innerHTML = "&nbsp;";
                    tr.insertCell(3).innerHTML = "&nbsp;";
                } else if (i % a == 2) {
                    if (schools[i].checked == true) {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    } else {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'   value="
                                + schools[i].id + ">" + schools[i].name;
                    }
                    tr.insertCell(3).innerHTML = "&nbsp;";
                }else{
                    if (schools[i].checked == true) {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    } else {
                        tr.insertCell(i % a).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'   value="
                                + schools[i].id + ">" + schools[i].name;
                    }
                }
            }
        }
}


function clickOrgLevel() {
    debugger;
    var orgLevelSel = $("#orgLevelSel");
    if (orgtype == "1") {// 教委用户分发
        if (orglevel == 3)// 市级教委
        {
            if (orgLevelSel.val() == '1') { // 区县
                $("#county").css("display", "table-row");
                $("#schoolList").css("display", "none");
                $("#subschool").css("display", "none");
                $("#grade1").css("display", "none");
                $("#grade2").css("display", "none");
                $("#grade3").css("display", "none");
                // $("#objectType")[0].selectedIndex = 1;
                $("#objectType option[value='']").attr("selected", true);
                $("#gradepart option[value='0']").attr("selected", true);
                setCounty(cityid);
            }
            if (orgLevelSel.val() == '2') {// 市直属学校
                $("#county").css("display", "none");
                $("#subschool").css("display", "table-row");
                $("#city").css("display", "none");
                $("#county").css("display", "none");
                $("#town").css("display", "none");
                $("#partgrade").css("display", "none");
                $("#grade1").css("display", "none");
                $("#grade2").css("display", "none");
                $("#grade3").css("display", "none");
                $("#teacher").css("display", "none");
                // $("#objectType")[0].selectedIndex = 1;
                $("#objectType option[value='']").attr("selected", true);
                $("#gradepart option[value='0']").attr("selected", true);
                setSonSchool();
            }
        }

    }
    if (orgtype == "3") {// 陶老师工作站用户分发
        if (orglevel == 3)// 陶老师工作站总站
        {
            if (orgLevelSel.val() == '1') { // 区县
                $("#county").css("display", "table-row");
                $("#school").css("display", "none");
                setCounty(cityid);
            }
            if (orgLevelSel.val() == '2') {// 市直属学校
                $("#county").css("display", "none");
                $("#subschool").css("display", "table-row");
                $("#city").css("display", "none");
                $("#county").css("display", "none");
                $("#town").css("display", "none");
                $("#partgrade").css("display", "none");
                $("#grade1").css("display", "none");
                $("#grade2").css("display", "none");
                $("#grade3").css("display", "none");
                $("#teacher").css("display", "none");
                setSonSchool();
            }
        }

    }
}
function clickPartEdu() {
    var gradepart = document.getElementById("gradepart");
    // var gradepartSel = $("#gradepartSel");
    // alert(gradepartSel.options[gradepartSel.selectedIndex].text);

    var gradepart = gradepart.value;
    // alert(gradepart);
    if (gradepart == "1") {
        $("#grade1").css("display", "table-row");
        $("#grade2").css("display", "none");
        $("#grade3").css("display", "none");
    }
    if (gradepart == "2") {
        $("#grade1").css("display", "none");
        $("#grade2").css("display", "table-row");
        $("#grade3").css("display", "none");
    }
    if (gradepart == "3") {
        $("#grade1").css("display", "none");
        $("#grade2").css("display", "none");
        $("#grade3").css("display", "table-row");
    }
}
function clickArea() {
    setCounty(cityid);
}
// 根据乡镇获得学校
function clickTownSelect() {
    var townid = $("#townSel").val();
    // alert(townid);
    for (var i = 0; i < townSchoolArray.length; i++) {
        var townschool = townSchoolArray[i];
        if (townschool.id == townid) {
            if (townschool.schools == null)
                setSchoolList(townid);
            else {
                setSchoolListFromArray(townschool.schools);
            }
            break;
        }

    }

}
function setSchoolList(townid) {
    // createXMLHttp();
    // xmlhttp.onreadystatechange=function(){getSchoolByTownId(townid)};
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getSchoolByTownId.do?areaid=" + townid;
    var param = {
        'areaid' : townid
    };
    $.post(url, param, getSchoolByTownId(townid));
    // xmlhttp.open("POST",convertURL(url),true);
    // xmlhttp.send(null);

}
function getSchoolByTownId(townid) {
    // if (xmlhttp.readyState == 4) {
    // if (xmlhttp.status == 200) {
    return function(data) {
        debugger;

        // var schools = xmlhttp.responseXML.getElementsByTagName("schools");
        var dataXml = toXML(data);
        var schools = dataXml.getElementsByTagName("schools");
        deleteSchools();
        var ts;
        for (var i = 0; i < townSchoolArray.length; i++) {
            var townschool = townSchoolArray[i];
            if (townschool.id == townid) {
                ts = townschool;
                ts.schools = new Array(schools.length);
                break;
            }
        }
        schoolList = document.getElementById("schoolList");
        for (var i = 0; i < schools.length; i++) {
            var s = new Object();
            s.id = schools[i].childNodes[0].firstChild.nodeValue;
            s.name = schools[i].childNodes[1].firstChild.nodeValue;
            // s.checked = "undefined";
            ts.schools[i] = s;

            if (i % 3 == 0)
                tr = schoolList.insertRow(i / 3);
            if (i != schools.length - 1) {
                tr.insertCell(i % 3).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
            } else if (i % 3 == 0) {
                tr.insertCell(i % 3).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(1).innerHTML = "&nbsp;";
                tr.insertCell(2).innerHTML = "&nbsp;";
                // tr.insertCell(3).innerHTML = "&nbsp;";
                // tr.insertCell(4).innerHTML = "&nbsp;";
            } else if (i % 3 == 1) {
                tr.insertCell(i % 3).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(2).innerHTML = "&nbsp;";
                // tr.insertCell(3).innerHTML = "&nbsp;";
                // tr.insertCell(4).innerHTML = "&nbsp;";
            }// else if(i%3==2){
            // tr.insertCell(i%3).innerHTML = "<input type='checkbox'
            // id='schoolid' name='schoolid' onclick='schoolboxChange(this)'
            // value="+schools[i].childNodes[0].firstChild.nodeValue+">"+
            // schools[i].childNodes[1].firstChild.nodeValue;
            // tr.insertCell(3).innerHTML = "&nbsp;";
            // tr.insertCell(4).innerHTML = "&nbsp;";
            // }else if(i%5==3){
            // tr.insertCell(i%5).innerHTML = "<input type='checkbox'
            // id='schoolid' name='schoolid' onclick='schoolboxChange(this)'
            // value="+schools[i].childNodes[0].firstChild.nodeValue+">"+
            // schools[i].childNodes[1].firstChild.nodeValue;
            // tr.insertCell(4).innerHTML = "&nbsp;";
            // }
            else
                tr.insertCell(i % 3).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
        }
    }
    // }

    // }
}
function setSchoolListFromArray(schools) {
    if (schools.length > 0) {
        {
            deleteSchools();
            schoolList = document.getElementById("schoolList");
            for (var i = 0; i < schools.length; i++) {
                if (i % 5 == 0)
                    tr = schoolList.insertRow(i / 5);
                if (i != schools.length - 1) {
                    if (schools[i].checked == true)
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked' value="
                                + schools[i].id + ">" + schools[i].name;
                    else
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  value="
                                + schools[i].id + ">" + schools[i].name;
                } else if (i % 5 == 0) {
                    if (schools[i].checked == true)
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    else
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid'  onclick='schoolboxChange(this)' value="
                                + schools[i].id + ">" + schools[i].name;
                    tr.insertCell(1).innerHTML = "&nbsp;";
                    tr.insertCell(2).innerHTML = "&nbsp;";
                    tr.insertCell(3).innerHTML = "&nbsp;";
                    tr.insertCell(4).innerHTML = "&nbsp;";
                } else if (i % 5 == 1) {
                    if (schools[i].checked == true)
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    else
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid'  onclick='schoolboxChange(this)'  value="
                                + schools[i].id + ">" + schools[i].name;
                    tr.insertCell(2).innerHTML = "&nbsp;";
                    tr.insertCell(3).innerHTML = "&nbsp;";
                    tr.insertCell(4).innerHTML = "&nbsp;";
                } else if (i % 5 == 2) {
                    if (schools[i].checked == true)
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    else
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'   value="
                                + schools[i].id + ">" + schools[i].name;
                    tr.insertCell(3).innerHTML = "&nbsp;";
                    tr.insertCell(4).innerHTML = "&nbsp;";
                } else if (i % 5 == 3) {
                    if (schools[i].checked == true)
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'  checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    else
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'   value="
                                + schools[i].id + ">" + schools[i].name;
                    tr.insertCell(4).innerHTML = "&nbsp;";
                } else {
                    if (schools[i].checked == true)
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid' onclick='schoolboxChange(this)'   checked='checked'  value="
                                + schools[i].id + ">" + schools[i].name;
                    else
                        tr.insertCell(i % 5).innerHTML = "<input class='checkbox01' type='checkbox' id='schoolid' name='schoolid'  onclick='schoolboxChange(this)'  value="
                                + schools[i].id + ">" + schools[i].name;
                }
            }
        }

    }
}
function schoolboxChange(obj) {
    var orgid = obj.value;
    for (var i = 0; i < townSchoolArray.length; i++) {
        var townSchool = townSchoolArray[i];
        var schools = townSchool.schools;
        if (schools != null) {
            for (var j = 0; j < schools.length; j++) {
                if (schools[j].id == orgid) {
                    schools[j].checked = obj.checked;
                    break;
                }
            }
        }
    }
}
function setSonSchool() {
    // createXMLHttp();
    // xmlhttp.onreadystatechange=getSonSchool;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getSonSchools.do";
    $.post(url, null, getSonSchool());
    // xmlhttp.open("POST",convertURL(url),true);
    // xmlhttp.send(null);
}
function getSonSchool() {
    // if (xmlhttp.readyState == 4) {
    // if (xmlhttp.status == 200) {
    return function(data) {
        // var schools = xmlhttp.responseXML.getElementsByTagName("schools");
        var dataXml = toXML(data);
        var schools = dataXml.getElementsByTagName("schools");
        // alert(schools[0].childNodes[1].firstChild.nodeValue);
        deleteSubSchools();
        schoolList = document.getElementById("subschoolList");
        // alert(schoolList);
        for (var i = 0; i < schools.length; i++) {
            if (i % 5 == 0)
                tr = schoolList.insertRow(i / 5);
            if (i != schools.length - 1) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='subschoolid' name='subschoolid' style='vertical-align:middle;' value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
            } else if (i % 5 == 0) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='subschoolid' name='subschoolid' style='vertical-align:middle;' value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(1).innerHTML = "&nbsp;";
                tr.insertCell(2).innerHTML = "&nbsp;";
                tr.insertCell(3).innerHTML = "&nbsp;";
                tr.insertCell(4).innerHTML = "&nbsp;";
            } else if (i % 5 == 1) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='subschoolid' name='subschoolid' style='vertical-align:middle;' value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(2).innerHTML = "&nbsp;";
                tr.insertCell(3).innerHTML = "&nbsp;";
                tr.insertCell(4).innerHTML = "&nbsp;";
            } else if (i % 5 == 2) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='subschoolid' name='subschoolid' style='vertical-align:middle;' value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(3).innerHTML = "&nbsp;";
                tr.insertCell(4).innerHTML = "&nbsp;";
            } else if (i % 5 == 3) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='subschoolid' name='subschoolid' style='vertical-align:middle;' value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(4).innerHTML = "&nbsp;";
            } else
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='subschoolid' name='subschoolid' style='vertical-align:middle;' value="
                        + schools[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + schools[i].childNodes[1].firstChild.nodeValue;
        }
    }
    // }
}
function setCounty(id) {
    // createXMLHttp();
    // xmlhttp.onreadystatechange=getCounties;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getQuxian.do?areaid=" + id;
    // xmlhttp.open("POST",convertURL(url),true);
    // xmlhttp.send(null);
    var param = {
        'areaid' : id
    };
    $.post(url, param, getCounties());
}
function getCounties() {
    // if (xmlhttp.readyState == 4) {
    // if (xmlhttp.status == 200) {
    return function(data) {
        var dataXml = toXML(data);
        // var areas = xmlhttp.responseXML.getElementsByTagName("areas");
        var areas = dataXml.getElementsByTagName("areas");
        deleteCounties();
        for (var i = 0; i < areas.length; i++) {
            if (i % 5 == 0)
                tr = countyList.insertRow(i / 5);
            if (i != areas.length - 1) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='areaid' name='areaid' style='vertical-align:middle;' value="
                        + areas[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + areas[i].childNodes[1].firstChild.nodeValue;
            } else if (i % 5 == 0) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='areaid' name='areaid' style='vertical-align:middle;' value="
                        + areas[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + areas[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(1).innerHTML = "&nbsp;";
                tr.insertCell(2).innerHTML = "&nbsp;";
                tr.insertCell(3).innerHTML = "&nbsp;";
                tr.insertCell(4).innerHTML = "&nbsp;";
            } else if (i % 5 == 1) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='areaid' name='areaid' style='vertical-align:middle;' value="
                        + areas[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + areas[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(2).innerHTML = "&nbsp;";
                tr.insertCell(3).innerHTML = "&nbsp;";
                tr.insertCell(4).innerHTML = "&nbsp;";
            } else if (i % 5 == 2) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='areaid' name='areaid' style='vertical-align:middle;' value="
                        + areas[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + areas[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(3).innerHTML = "&nbsp;";
                tr.insertCell(4).innerHTML = "&nbsp;";
            } else if (i % 5 == 3) {
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='areaid' name='areaid' style='vertical-align:middle;' value="
                        + areas[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + areas[i].childNodes[1].firstChild.nodeValue;
                tr.insertCell(4).innerHTML = "&nbsp;";
            } else
                tr.insertCell(i % 5).innerHTML = "<input type='checkbox' id='areaid' name='areaid' style='vertical-align:middle;' value="
                        + areas[i].childNodes[0].firstChild.nodeValue
                        + ">"
                        + areas[i].childNodes[1].firstChild.nodeValue;
        }
    }

    // }
}
function setTowns(id) {
    // createXMLHttp();
    // xmlhttp.onreadystatechange=getTowns;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getTowns.do?areaid=" + id;
    // xmlhttp.open("POST",convertURL(url),true);
    // xmlhttp.send(null);
    var param = {
        "areaid" : id
    };
    $.post(url, param, getTowns());
}
function getTowns() {
    // if (xmlhttp.readyState == 4) {
    // if (xmlhttp.status == 200) {
    return function(data) {
        var dataXml = toXML(data);
        // var areas = xmlhttp.responseXML.getElementsByTagName("areas");
        var areas = dataXml.getElementsByTagName("areas");
        var townSel = document.getElementById("townSel");
        townSel.length = 0;
        // option=new Option("请选择","");
        // townSel.options.add(option);

        townSchoolArray = new Array(areas.length);

        for (var i = 0; i < areas.length; i++) {

            xValue = areas[i].childNodes[0].firstChild.nodeValue;
            xText = areas[i].childNodes[1].firstChild.nodeValue;
            console.log(xText);
            option = new Option(xText, xValue);

            var townSchool = new Object();
            townSel.options.add(option);
            townSchool.id = xValue;
            townSchool.name = xText;
            townSchool.schools = null;
            // townSchool.checked = "checked";
            townSchoolArray[i] = townSchool;
        }
    }
    // }
}
// 删除直属小学列表
function deleteSubSchools() {
    schoolList = document.getElementById("subschoolList");
    var rlen = schoolList.rows.length;
    for (var r = 0; r < rlen; r++) {
        schoolList.deleteRow(0);
    }
}
// 删除非直属小学列表
function deleteSchools() {
    schoolList = document.getElementById("schoolList");
    var rlen = schoolList.rows.length;
    for (var r = 0; r < rlen; r++) {
        schoolList.deleteRow(0);
    }
}
function deleteCounties() {
    countylist = document.getElementById("countyList");
    var rlen = countylist.rows.length;
    for (var r = 0; r < rlen; r++) {
        countylist.deleteRow(0);
    }
}
function infoNext1() {
    if (clickInfo()) {
        $("#info").css("display", "none");
        $("#colony").css("display", "inline-table");
    }
}
function colonyUp() {
    $("#colony").css("display", "none");
    $("#step1").removeClass().addClass("step1_sel");
    $("#step2").removeClass().addClass("step2");
}
function investTargetUp() {
    $("#info").css("display", "inline-table");
    $("#colony").css("display", "none");
    $("#step1").removeClass().addClass("step1_sel");
    $("#step2").removeClass().addClass("step2");
}
function scaleNext() {
    var taskname = $("#taskname");
    taskname.value = getfirstscalename();
    $("#scale").css("display", "none");
    $("#task").css("display", "inline-table");
    $("#step2").removeClass().addClass("step2");
    $("#step3").removeClass().addClass("step3_sel");
    $(".stepTwo").hide();
}

function getfirstscalename() {
    try {
        var box = document.getElementsByName("scaleId");
        var num = 0;
        for (var i = 0; i < box.length; i++) {
            if (box[i].checked) {
                return box[i].value;
            }
        }
        return "";
    } catch (e) {
        return "";
    }

}
function valiGetBoxNum(name) {
    try {
        var box = document.getElementsByName(name);
        var num = 0;
        for (var i = 0; i < box.length; i++) {
            if (box[i].checked) {
                num++;
            }
        }
        return num;
    } catch (e) {
        return 0;
    }

}

function clickColony() {
    var objectType = $("#objectType");
    // alert(objectType.val());
    if (objectType.val() == '') {
        layer.open({
            content : '请选择分发对象！\r\n'
        });
        objectType.focus();
    } else if (objectType.val() != 2) {
        var gradepart = $("#gradepart");
        if (gradepart.val() == 0) {
            layer.open({
                content : '请选择学段！\r\n'
            });
            gradepart.focus();
        } else {
            var num = valiGetBoxNum("gradeClassId");
            if (num < 1) {
                layer.open({
                    content : '请选择分发的班级！\r\n'
                });
            } else
                return true;
        }
    } else if (objectType.val() == 2) {
        var num = valiGetBoxNum("teacherRole");
        if (num < 1) {
            layer.open({
                content : '请选择分发的教师类型！\r\n'
            });
        } else
            return true;
    } else
        return true;
}
function clickEduColony() {
    if (orglevel == 3) {// 市教委
        var selected = false;
        var ol = $("#orgLevelSel").val();
        if (ol == '1') {// 区县
            $("input:checkbox[name='areaid']:checked").each(function() {
                selected = true;
            });
        }
        if (ol == '2') {
            $("input:checkbox[name='subschoolid']:checked").each(function() {
                selected = true;
            });
        }
        if (selected == false) {
            if (ol == '1')// 区县
                layer.open({
                    content : '请选择区县！\r\n'
                });
            if (ol == '2')
                layer.open({
                    content : '请选择直属学校！\r\n'
                });
            return false;
        } else
            return true;
    }
    if (orglevel == 4) {// 区县教委
        var selected = false;
        $("input:checkbox[name='schoolid']:checked").each(function() {
            selected = true;
        });
        if (selected == false) {
            layer.open({
                content : '请选择学校！\r\n'
            });
            return false;
        } else
            return true;
    }
    var objectType = $("#objectType");
    // alert(objectType.val());
    if (objectType.val() == '') {
        layer.open({
            content : '请选择分发对象！\r\n'
        });
        objectType.focus();
    } else if (objectType.val() != 2) {
        var gradepart = $("#gradepart");
        if (gradepart.val() == 0) {
            layer.open({
                content : '请选择学段！\r\n'
            });
            gradepart.focus();
        } else {
            var num;

            if (gradepart.val() == "1")
                num = valiGetBoxNum("nj1");
            if (gradepart.val() == "2")
                num = valiGetBoxNum("nj2");
            if (gradepart.val() == "3")
                num = valiGetBoxNum("nj3");
            // alert(num);
            if (num < 1) {
                layer.open({
                    content : '请选择分发的班级！\r\n'
                });
            } else
                return true;
        }
    } else if (objectType.val() == 2) {
        var num = valiGetBoxNum("teacherRole");
        if (num < 1) {
            layer.open({
                content : '请选择分发的教师类型！\r\n'
            });
        } else
            return true;
    } else
        return true;
}
function colonyNext() {
    if (clickColony()) {
        $("#step1").removeClass().addClass("step1");
        $("#step2").removeClass().addClass("step2_sel");
        $("#colony").css("display", "none");
        $("#scale").css("display", "inline-table");
        $("#info").hide();
        $(".step-one").hide();
        $(".step-two").show();
        $("#task").css("display", "inline-table");
        clickPartScale();
    }
}
function eduColonyNext() {
    if (clickEduColony()) {
        $("#step2").removeClass().addClass("step2");
        $("#step3").removeClass().addClass("step3_sel");
        $("#colony").css("display", "none");
        $("#scale").css("display", "inline-table");
        clickEduPartScale();
    }
}
function investTargetNext() {
    if (clickColony()) {
        $("#step2").removeClass().addClass("step2");
        $("#step3").removeClass().addClass("step3_sel");
        $("#colony").css("display", "none");
        $("#task").css("display", "inline-table");
    }
}
function eduInvestTargetNext() {
    if (clickEduColony()) {
        $("#step2").removeClass().addClass("step2");
        $("#step3").removeClass().addClass("step3_sel");
        $("#colony").css("display", "none");
        $("#task").css("display", "inline-table");
    }
}
function scaleUp() {
    $("#info").css("display","inline-table");
    $("#colony").css("display", "inline-table");
    $("#scale").css("display", "none");
    $("#step1").removeClass().addClass("step1_sel");
    $("#step2").removeClass().addClass("step2");
    $(".step-two").hide();
    $(".step-one").show();
}
function taskUp() {
    $("#info").css("display","inline-table");
    $("#colony").css("display", "inline-table");
    $("#scale").css("display", "none");
    $("#step1").removeClass().addClass("step1_sel");
    $("#step2").removeClass().addClass("step2");
    $("#task").css("display","none");
    $(".step-one").show();
    $(".step-two").hide();
}

function investTaskUp() {
    $("#colony").css("display", "inline-table");
    $("#task").css("display", "none");
    $("#step2").removeClass().addClass("step2_sel");
    $("#step3").removeClass().addClass("step3");
}
function clickRole() {
    var objectType = $("#objectType").val();
    if ("1" == objectType) {// ||"4"==objectType){
        hideall();
        $("#student").css("display", "table-row");
        $("#partgrade").css("display", "table-row");
    } else if ("2" == objectType) {
        hideall();
        $("#teacher").css("display", "table-row");
        $("#partgrade").css("display", "none");
        $("#stuent").css("display", "none");
        setSchoolTeacherRole();
    } else
        hideall();
}
function setSchoolTeacherRole() {
    debugger;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getTeacherRolesByOrglevel.do";
    // $.post(url,null,getEduTeacherRole());
    $.ajax({
        type : "POST",
        url : url,
        data : {
            orglevel : orglevel
        },
        dataType : "json",
        success : function(data) {
            debugger;
            var teacherRoleContainer = $("#teacherRoleContainer");
            teacherRoleContainer.html("");
            $.each(data, function(commentIndex, comment) {
                var rolename = comment.roleName;
                var roleid = comment.id;
                var htm = "<input class='checkbox01' type='checkbox' name='teacherRole' value='" + roleid + "' />";
                htm = htm + rolename + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                teacherRoleContainer.append(htm);

            });
        }
    });

}
function clickRoleUnit() {
    var objectType = $("#objectType").val();
    if ("1" == objectType) {// ||"4"==objectType){
        hideall();
        $("#student").css("display", "table-row");
        $("#partgrade").css("display", "table-row");
        $("#showStudent").show();
        $("#showTeacher").hide();
    } else if ("2" == objectType) {
        hideall();
        $("#showTeacher").show();
        $("#showStudent").hide();
        $("#partgrade").css("display", "none");
        $("#stuent").css("display", "none");
        setTeacherRoles();
    } else
        hideall();
    deleteStudent();
}
function clickObjectTypeEdu() {
    var objectType = $("#objectType").val();
    if ("1" == objectType) {// ||"4"==objectType){
        $("#partgrade").css("display", "table-row");
        $("#grade1").css("display", "none");
        $("#grade2").css("display", "none");
        $("#grade3").css("display", "none");
        $("#teacher").css("display", "none");
        $("#school").css("display", "none");
    }
    if ("2" == objectType) {// ||"4"==objectType){
        $("#partgrade").css("display", "none");
        $("#grade1").css("display", "none");
        $("#grade2").css("display", "none");
        $("#grade3").css("display", "none");
        $("#teacher").css("display", "table-row");
        $("#school").css("display", "none");
        setEduTeacherRoles();

    }

}
function setTeacherRoles() {
    // createXMLHttp();
    // xmlhttp.onreadystatechange=getTeacherRole;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getTeacherRoles.do";
    // xmlhttp.open("POST",convertURL(url),true);
    // xmlhttp.send(null);
    $.ajax({
        type : "POST",
        url : url,
        success : function(data) {
            var dataXml = toXML(data);
            var roles = dataXml.getElementsByTagName("roles");
            var teacherRole = document.getElementById("teacherRole");
            teacherRole.length = 0;
            option = new Option("请选择", "");
            teacherRole.options.add(option);
            for (var i = 0; i < roles.length; i++) {
                xValue = roles[i].childNodes[0].firstChild.nodeValue;
                xText = roles[i].childNodes[1].firstChild.nodeValue;
                console.log(xText);
                option = new Option(xText, xValue);
                teacherRole.options.add(option);
            }
        }
    });

}

$(function(){
    $("#showTeacher").hide();
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getTeacherRoles.do";
    $.ajax({
        type : "POST",
        url : url,
        success : function(data) {
            var dataXml = toXML(data);
            var roles = dataXml.getElementsByTagName("roles");
            var teacherRole = document.getElementById("teacherRole");
            teacherRole.length = 0;
            option = new Option("请选择", "");
            teacherRole.options.add(option);
            for (var i = 0; i < roles.length; i++) {
                xValue = roles[i].childNodes[0].firstChild.nodeValue;
                xText = roles[i].childNodes[1].firstChild.nodeValue;
                console.log(xText);
                option = new Option(xText, xValue);
                teacherRole.options.add(option);
            }
        }
    });
});

function getTeacherRole() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var roles = xmlhttp.responseXML.getElementsByTagName("roles");
            var teacherRole = document.getElementById("teacherRole");
            teacherRole.length = 0;
            option = new Option("请选择", "");
            teacherRole.options.add(option);
            for (var i = 0; i < roles.length; i++) {
                xValue = roles[i].childNodes[0].firstChild.nodeValue;
                xText = roles[i].childNodes[1].firstChild.nodeValue;
                console.log(xText);
                option = new Option(xText, xValue);
                teacherRole.options.add(option);
            }
        }

    }

}
function setEduTeacherRoles() {
    debugger;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getTeacherRolesByOrglevel.do";
    var ol = $("#orgLevelSel").val();
    if (ol == 1)
        ol = 6;
    else
        ol = 4;
    // $.post(url,null,getEduTeacherRole());
    $.ajax({
        type : "POST",
        url : url,
        data : {
            orglevel : ol
        },
        dataType : "json",
        success : function(data) {
            debugger;
            var teacherRoleContainer = $("#teacherRoleContainer");
            teacherRoleContainer.html("");
            $.each(data, function(commentIndex, comment) {
                var rolename = comment.roleName;
                var roleid = comment.id;
                var htm = "<input class='checkbox01' type='checkbox' name='teacherRole' value='" + roleid + "' />";
                htm = htm + rolename + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                teacherRoleContainer.append(htm);

            });
        }
    });

}
function getEduTeacherRole() {
    return function(data) {
        var dataXml = toXML(data);
        var roles = dataXml.getElementsByTagName("roles");
        var teacherRole = document.getElementById("teacherRole");
        teacherRole.length = 0;
        option = new Option("请选择", "");
        teacherRole.options.add(option);
        for (var i = 0; i < roles.length; i++) {
            xValue = roles[i].childNodes[0].firstChild.nodeValue;
            xText = roles[i].childNodes[1].firstChild.nodeValue;
            console.log(xText);
            option = new Option(xText, xValue);
            teacherRole.options.add(option);
        }
    }

}
function clickPart() {
    $("#gradeclass").css("display", "inline-table");
    var xueduan = $("#gradepart").val();
    getgclass(xueduan);
}
function getgclass(xueduan) {
    // createXMLHttp();
    // xmlhttp.onreadystatechange=getGradeClass;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getGradeClass.do?xueduan=" + xueduan;
    // xmlhttp.open("POST",convertURL(url),true);
    // xmlhttp.send(null);
    $
            .ajax({
                type : "POST",
                url : url,
                data : {
                    xueduan : xueduan
                },
                success : function(data) {
                    debugger;
                    var dataXml = toXML(data);
                    var grades = dataXml.getElementsByTagName("grades");
                    var gradeclass = document.getElementById("gradeclass");
                    var rlen = gradeclass.rows.length;
                    for (var r = 0; r < rlen; r++) {
                        gradeclass.deleteRow(0);
                    }
                    for (var i = 0; i < grades.length; i++) {
                        tr = gradeclass.insertRow(i);
                        var gid = grades[i].childNodes[0].firstChild.nodeValue;
                        tr.insertCell(0).innerHTML = grades[i].childNodes[1].firstChild.nodeValue
                                + "&nbsp;<input class='checkbox01'  type='checkbox' name='checkbox' id='ck_" + gid
                                + "' onclick='selectAllClass(" + gid + ");' >";

                        var gclasses = grades[i].getElementsByTagName("gradeclass");
                        var innerHTML = "&nbsp;";
                        for (var j = 0; j < gclasses.length; j++) {
                            innerHTML += "<input type='checkbox' style='vertical-align:middle;' id='gradeClassId' name='gradeClassId' value='"
                                    + gclasses[j].childNodes[0].firstChild.nodeValue
                                    + "'><label style='vertical-align:middle'>"
                                    + gclasses[j].childNodes[1].firstChild.nodeValue + "</label>&nbsp;";
                        }
                        tr.insertCell(1).innerHTML = innerHTML;
                        tr.cells[0].width = "80px";
                        tr.cells[1].id = "td_" + gid;
                    }
                }
            });

}
function selectAllClass(gradeid) {
    debugger;
    var id = "#td_" + gradeid;
    var ckid = "#ck_" + gradeid;
    $(id).find(':checkbox').prop('checked', $(ckid).prop('checked'));
}
function getGradeClass() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var grades = xmlhttp.responseXML.getElementsByTagName("grades");
            var gradeclass = document.getElementById("gradeclass");
            var rlen = gradeclass.rows.length;
            for (var r = 0; r < rlen; r++) {
                gradeclass.deleteRow(0);
            }
            for (var i = 0; i < grades.length; i++) {
                tr = gradeclass.insertRow(i);
                tr.insertCell(0).innerHTML = grades[i].childNodes[1].firstChild.nodeValue;
                var gclasses = grades[i].getElementsByTagName("gradeclass");
                var innerHTML = "&nbsp;";
                for (var j = 0; j < gclasses.length; j++) {
                    innerHTML += "<input type='checkbox' id='gradeClassId' name='gradeClassId' value='"
                            + gclasses[j].childNodes[0].firstChild.nodeValue + "'>"
                            + gclasses[j].childNodes[1].firstChild.nodeValue;
                }
                tr.insertCell(1).innerHTML = innerHTML;
            }
        }

    }
}
function clickPartScale() {
    var objectType = $("#objectType").val();
    var gradepart = $("#gradepart").val();
    getPartS(objectType, gradepart);
}
function clickEduPartScale() {
    debugger;
    var objectType = $("#objectType").val();
    var gradepart = $("#gradepart").val();
    // getS(objectType,gradeOrderId);
    getScaleByGradePart(objectType, gradepart);
}
function getPartS(objectType, gradepart) {
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getGradeScales.do?objectType=" + objectType + "&gradepart=" + gradepart;
    // xmlhttp.open("POST",convertURL(url),false);
    // xmlhttp.send(null);
    $.ajax({
        type : "POST",
        url : url,
        data : {
            objectType : $("#objectType").val(),
            gradeOrderId : $("#gradeOrderId").val()
        },
        dataType : "json",
        success : function(data) {
            var result;
            $('#scaletype').empty(); // 清空里面的所有内容
            $.each(data, function(commentIndex, comment) {
                if (commentIndex == 0) {
                    $('#scaletype').empty(); // 清空里面的所有内容
                    $('#scaletype').append("<option value='-1'>请选择</option>");
                    var scaletypeArray = comment.scaletype;
                    for (var i = 0; i < scaletypeArray.length; i++) {
                        var st = scaletypeArray[i];
                        var id = st.scaleTypeId;
                        var title = st.scaleTypeTitle;
                        $('#scaletype').append("<option value='" + id + "'>" + title + "</option>");
                        // var option=new Option(title,id);
                        // scaletype.options.add(option);
                    }

                }
                if (commentIndex == 1) {
                    $('#scalesource').empty(); // 清空里面的所有内容
                    $('#scalesource').append("<option value='-1'>请选择</option");
                    var scalesource = comment.scalesource;
                    for (var i = 0; i < scalesource.length; i++) {
                        var ss = scalesource[i];
                        var id = ss.scaleSourceId;
                        var title = ss.scaleSourceTitle;
                        // var option = new Option(title,id);
                        $('#scalesource').append("<option value='" + id + "'>" + title + "</option");
                    }

                }
                if (commentIndex == 2) {
                    debugger;
                    var scale = comment.scale;
                    $("#scalelist tbody").empty();
                    if ($("#objectType").val() == 1)
                        $("#scalelist").append(
                                "<tr><th><input type='checkbox' id='scaleAllCheckbox'  onclick='allScaleCheck()'></th>"
                                        + "<th>量表名称</th>" + "<th>英文简称</th>" + "<th>题目数量</th>" + "<th>量表显示名称2</th>"
                                        + "<th>量表类别</th>" + "<th>常模</th>" + "<th>量表查看权限</th>" + "<th>再测限制</th></tr>");
                    if ($("#objectType").val() == 2)
                        $("#scalelist").append(
                                "<tr><th><input type='checkbox' id='scaleAllCheckbox' onclick='allScaleCheck()'></th>"
                                        + "<th>量表名称</th>" + "<th>英文简称</th>" + "<th>题目数量</th>" + "<th>量表显示名称2</th>"
                                        + "<th>量表类别</th>" + "<th>常模</th>" + "<th>再测限制</th></tr>");
                    for (var i = 0; i < scale.length; i++) {
                        var s = scale[i];
                        var scaleid = s.scaleId;
                        var shortname = s.scaleShortName;
                        var source = s.scalesource;
                        var qnum = s.scaleQNum;
                        var title = s.scaleTitle;
                        var type = s.scaleType;
                        var showtitle = s.scaleShowTitle;
                        var checkvalue;
                        if ($("#objectType").val() == 1)
                            checkvalue = scaleid + "_1_1_1_0_0_-1";
                        if ($("#objectType").val() == 2)
                            checkvalue = scaleid + "_0_-1";
                        // var option = new Option(title,id);
                        if ($("#objectType").val() == 1)
                            $("#scalelist").append(
                                    "<tr id='tr_" + scaleid + "'  align='center'>"
                                            + "<td><input type='checkbox'  name='scaleId' value='" + checkvalue
                                            + "'></td>" + "<td>" + title + "</td>" + "<td>" + shortname + "</td>"
                                            + "<td>" + qnum + "</td>" + "<td>" + showtitle + "</td>" + "<td>" + type
                                            + "</td>" + "<td onclick='select_norm(" + scaleid + ");' >选择</td>"
                                            + "<td id='td_" + scaleid + "' onclick='set_report_permission(" + scaleid
                                            + ");' >设置</td>" + "<td onclick='set_test_limit(this," + scaleid
                                            + ");' >无</td>" + "</tr>");
                        if ($("#objectType").val() == 2)
                            $("#scalelist").append(
                                    "<tr id='tr_" + scaleid + "'  align='center'>"
                                            + "<td><input type='checkbox' name='scaleId' value='" + checkvalue
                                            + "'></td>" + "<td>" + title + "</td>" + "<td>" + shortname + "</td>"
                                            + "<td>" + qnum + "</td>" + "<td>" + showtitle + "</td>" + "<td>" + type
                                            + "</td>" + "<td onclick='select_norm(" + scaleid + ");' >选择</td>"
                                            + "<td onclick='set_test_limit(this," + scaleid + ");' >无</td>" + "</tr>");
                    }
                }

            });
            // $('#resText').html(html);
        }
    });

}

function createTable(scalelist, title, scales) {
    td = scalelist.insertRow(-1).insertCell(0);
    td.style.textAlign = "center";
    td.colSpan = "3";
    td.className = "left_title_3";
    td.innerHTML = title;
    for (var i = 0; i < scales.length; i++) {
        if (i % 3 == 0)
            tr = scalelist.insertRow(-1);
        if (i != scales.length - 1) {
            tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='scaleId' name='scaleId' value="
                    + scales[i].childNodes[0].firstChild.nodeValue + ">" + scales[i].childNodes[1].firstChild.nodeValue;
        } else if (i % 3 == 0) {
            tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='scaleId' name='scaleId' value="
                    + scales[i].childNodes[0].firstChild.nodeValue + ">" + scales[i].childNodes[1].firstChild.nodeValue;
            tr.insertCell(1).innerHTML = "&nbsp;";
            tr.insertCell(2).innerHTML = "&nbsp;";
        } else if (i % 3 == 1) {
            tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='scaleId' name='scaleId' value="
                    + scales[i].childNodes[0].firstChild.nodeValue + ">" + scales[i].childNodes[1].firstChild.nodeValue;
            tr.insertCell(2).innerHTML = "&nbsp;";
        } else
            tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='scaleId' name='scaleId' value="
                    + scales[i].childNodes[0].firstChild.nodeValue + ">" + scales[i].childNodes[1].firstChild.nodeValue;
    }
}
function getPartScales() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var scales = xmlhttp.responseXML.getElementsByTagName("scales");
            var inScales = xmlhttp.responseXML.getElementsByTagName("inScales");
            var selfScales = xmlhttp.responseXML.getElementsByTagName("selfScales");
            var scalelist = document.getElementById("scalelist");
            var rlen = scalelist.rows.length;
            for (var r = 0; r < rlen; r++) {
                scalelist.deleteRow(0);
            }
            if (scales.length > 0) {
                var title = '心检量表';
                createTable(scalelist, title, scales);
            }
            if (inScales.length > 0) {
                var title = '赠送量表';
                createTable(scalelist, title, inScales);
            }
            if (selfScales.length > 0) {
                var title = '自定义量表';
                createTable(scalelist, title, selfScales);
            }
        }

    }
}
function clickScale() {
    var objectType = $("#objectType").val();
    var gradeOrderId = $("#gradeOrderId").val();
    getS(objectType, gradeOrderId);
}

function set_report_permission(scaleid) {
    debugger;
    currentScaleId = scaleid;
    var td_id = "#td_" + scaleid;
    var td = $(td_id)[0];
    var top = getTop(td);
    var tb = document.getElementById("scalelist");
    var scrollTop = tb.parentNode.scrollTop;
    var left = getLeft(td);
    $("#scaleSetDiv").css({
        position : "absolute"
    });
    top = top - scrollTop;// 这里将滚动条滚动偏移量算上
    $("#scaleSetDiv").css("left", left + "px");
    $("#scaleSetDiv").css("top", top + "px");
    $("#scaleSetDiv").css("display", "block");

    var ck = getCurrentScaleCheckbox(scaleid);
    var vars = ck.value.split("_");
    var parentvisible = vars[1];
    $("#ppLb").html(vars[1] == 1 ? "ON" : "OFF");
    $("#tpLb").html(vars[2] == 1 ? "ON" : "OFF");
    $("#spLb").html(vars[3] == 1 ? "ON" : "OFF");
    $("#warningSelect").val(vars[4]);
    // ck.value =
    // currentScaleId+"_"+parentVisible+"_"+teacherVisible+"_"+studentVisible+"_"+warningVisible+"_"+haveLimit;
    // var id = e.target.id;
    // var temp = id.split("_");
    // var scaleid = temp[1];
}

// 获取元素的纵坐标
function getTop(td) {
    var offset = td.offsetTop;
    // var offset = td.offset().top;
    if (td.offsetParent != null)
        offset += getTop(td.offsetParent);
    return offset;
}
// 获取元素的横坐标
function getLeft(td) {
    var offset = td.offsetLeft;
    ;
    if (td.offsetParent != null)
        offset += getLeft(td.offsetParent);
    return offset;
}
function set_test_limit(td, scaleid) {
    debugger;
    currentScaleId = scaleid;
    if (td.textContent == "有") {
        td.textContent = "无";
        haveLimit = 0;
    } else {
        td.textContent = "有";
        haveLimit = 1;
    }
    var trid = "#tr_" + scaleid;
    var ck = $(trid).find('td:eq(0)').find(':checkbox')[0];
    if ($("#objectType").val() == 1)
        ck.value = currentScaleId + "_" + parentVisible + "_" + teacherVisible + "_" + studentVisible + "_"
                + warningVisible + "_" + haveLimit + "_" + normid;
    if ($("#objectType").val() == 2)
        ck.value = currentScaleId + "_" + haveLimit + "_" + normid;
    clearReportSet();
}
function getS(objectType, gradeOrderId) {
    // createXMLHttp();
    // xmlhttp.onreadystatechange=getScales;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getScales.do?gradeOrderId=" + gradeOrderId;
    // xmlhttp.open("POST",convertURL(url),false);
    // xmlhttp.send(null);
    $.ajax({
        type : "POST",
        url : url,
        data : {
            objectType : $("#objectType").val(),
            gradeOrderId : $("#gradeOrderId").val()
        },
        dataType : "json",
        success : function(data) {
            var result;
            $('#scaletype').empty(); // 清空里面的所有内容
            $.each(data, function(commentIndex, comment) {
                debugger;
                if (commentIndex == 0) {
                    $('#scaletype').empty(); // 清空里面的所有内容
                    $('#scaletype').append("<option value='-1'>请选择</option>");
                    var scaletypeArray = comment.scaletype;
                    for (var i = 0; i < scaletypeArray.length; i++) {
                        var st = scaletypeArray[i];
                        var id = st.scaleTypeId;
                        var title = st.scaleTypeTitle;
                        $('#scaletype').append("<option value='" + id + "'>" + title + "</option>");
                        // var option=new Option(title,id);
                        // scaletype.options.add(option);
                    }

                }
                if (commentIndex == 1) {
                    $('#scalesource').empty(); // 清空里面的所有内容
                    $('#scalesource').append("<option value='-1'>请选择</option>");
                    var scalesource = comment.scalesource;
                    for (var i = 0; i < scalesource.length; i++) {
                        var ss = scalesource[i];
                        var id = ss.scaleSourceId;
                        var title = ss.scaleSourceTitle;
                        // var option = new Option(title,id);
                        $('#scalesource').append("<option value='" + id + "'>" + title + "</option>");
                    }

                }
                if (commentIndex == 2) {
                    debugger;
                    var scale = comment.scale;
                    $("#scalelist tbody").empty();
                    if ($("#objectType").val() == 1)
                        $("#scalelist").append(
                                "<tr><th><input type='checkbox' id='scaleAllCheckbox'  onclick='allScaleCheck()'></th>"
                                        + "<th>量表名称</th>" + "<th>英文简称</th>" + "<th>题目数量</th>" + "<th>量表显示名称</th>"
                                        + "<th>量表类别</th>" + "<th>常模</th>" + "<th>量表查看权限</th>" + "<th>再测限制</th></tr>");
                    if ($("#objectType").val() == 2)
                        $("#scalelist").append(
                                "<tr><th><input type='checkbox' id='scaleAllCheckbox'  onclick='allScaleCheck()'></th>"
                                        + "<th>量表名称</th>" + "<th>英文简称</th>" + "<th>题目数量</th>" + "<th>量表显示名称</th>"
                                        + "<th>量表类别</th>" + "<th>常模</th>" + "<th>再测限制</th></tr>");
                    for (var i = 0; i < scale.length; i++) {
                        var s = scale[i];
                        var scaleid = s.scaleId;
                        var shortname = s.scaleShortName;
                        var source = s.scalesource;
                        var qnum = s.scaleQNum;
                        var title = s.scaleTitle;
                        var type = s.scaleType;
                        var showtitle = s.scaleShowTitle;
                        var checkvalue;
                        if ($("#objectType").val() == 1)
                            checkvalue = scaleid + "_1_1_1_0_0_-1";
                        if ($("#objectType").val() == 2)
                            checkvalue = scaleid + "_0_-1";
                        // var option = new Option(title,id);
                        if ($("#objectType").val() == 1)
                            $("#scalelist").append(
                                    "<tr id='tr_" + scaleid + "'  align='center'>"
                                            + "<td><input type='checkbox' name='scaleId' value='" + checkvalue
                                            + "'></td>" + "<td>" + title + "</td>" + "<td>" + shortname + "</td>"
                                            + "<td>" + qnum + "</td>" + "<td>" + showtitle + "</td>" + "<td>" + type
                                            + "</td>" + "<td  onclick='select_norm(" + scaleid + ");' >选择</td>"
                                            + "<td id='td_" + scaleid + "' onclick='set_report_permission(" + scaleid
                                            + ");' >设置</td>"

                                            + "<td onclick='set_test_limit(this," + scaleid + ");' >无</td>" + "</tr>");
                        if ($("#objectType").val() == 2)
                            $("#scalelist").append(
                                    "<tr id='tr_" + scaleid + "'  align='center'>"
                                            + "<td><input type='checkbox' name='scaleId' value='" + checkvalue
                                            + "'></td>" + "<td>" + title + "</td>" + "<td>" + shortname + "</td>"
                                            + "<td>" + qnum + "</td>" + "<td>" + showtitle + "</td>" + "<td>" + type
                                            + "</td>" + "<td  onclick='select_norm(" + scaleid + ");' >选择</td>"
                                            + "<td onclick='set_test_limit(this," + scaleid + ");' >无</td>" + "</tr>");
                    }
                }

            });
            // $('#resText').html(html);
        }
    });
}
function getScaleByGradePart(objectType, gradepart) {
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getScales.do";
    $.ajax({
        type : "POST",
        url : url,
        data : {
            objectType : $("#objectType").val(),
            gradepart : $("#gradepart").val()
        },
        dataType : "json",
        success : function(data) {
            var result;
            $('#scaletype').empty(); // 清空里面的所有内容
            $.each(data, function(commentIndex, comment) {
                debugger;
                if (commentIndex == 0) {
                    $('#scaletype').empty(); // 清空里面的所有内容
                    $('#scaletype').append("<option value='-1'>请选择</option>");
                    var scaletypeArray = comment.scaletype;
                    for (var i = 0; i < scaletypeArray.length; i++) {
                        var st = scaletypeArray[i];
                        var id = st.scaleTypeId;
                        var title = st.scaleTypeTitle;
                        $('#scaletype').append("<option value='" + id + "'>" + title + "</option>");
                    }

                }
                if (commentIndex == 1) {
                    $('#scalesource').empty(); // 清空里面的所有内容
                    $('#scalesource').append("<option value='-1'>请选择</option>");
                    var scalesource = comment.scalesource;
                    for (var i = 0; i < scalesource.length; i++) {
                        var ss = scalesource[i];
                        var id = ss.scaleSourceId;
                        var title = ss.scaleSourceTitle;
                        // var option = new Option(title,id);
                        $('#scalesource').append("<option value='" + id + "'>" + title + "</option>");
                    }
                }
                if (commentIndex == 2) {
                    debugger;
                    var scale = comment.scale;
                    $("#scalelist tbody").empty();
                    if ($("#objectType").val() == 1)
                        $("#scalelist").append(
                                "<tr><th><input type='checkbox' id='scaleAllCheckbox'  onclick='allScaleCheck()'></th>"
                                        + "<th>量表名称</th>" + "<th>英文简称</th>" + "<th>题目数量</th>" + "<th>量表显示名称</th>"
                                        + "<th>量表类别</th>" + "<th>常模</th>" + "<th>量表查看权限</th>" + "<th>再测限制</th></tr>");
                    if ($("#objectType").val() == 2)
                        $("#scalelist").append(
                                "<tr><th><input type='checkbox' id='scaleAllCheckbox'  onclick='allScaleCheck()'></th>"
                                        + "<th>量表名称</th>" + "<th>英文简称</th>" + "<th>题目数量</th>" + "<th>量表显示名称</th>"
                                        + "<th>量表类别</th>" + "<th>常模</th>" + "<th>再测限制</th></tr>");
                    for (var i = 0; i < scale.length; i++) {
                        var s = scale[i];
                        var scaleid = s.scaleId;
                        var shortname = s.scaleShortName;
                        var source = s.scalesource;
                        var qnum = s.scaleQNum;
                        var title = s.scaleTitle;
                        var type = s.scaleType;
                        var showtitle = s.scaleShowTitle;
                        var checkvalue;
                        if ($("#objectType").val() == 1)
                            checkvalue = scaleid + "_1_1_1_0_0_-1";
                        if ($("#objectType").val() == 2)
                            checkvalue = scaleid + "_0_-1";
                        // var option = new Option(title,id);
                        if ($("#objectType").val() == 1)
                            $("#scalelist").append(
                                    "<tr id='tr_" + scaleid + "'  align='center'>"
                                            + "<td><input type='checkbox' name='scaleId' value='" + checkvalue
                                            + "'></td>" + "<td>" + title + "</td>" + "<td>" + shortname + "</td>"
                                            + "<td>" + qnum + "</td>" + "<td>" + showtitle + "</td>" + "<td>" + type
                                            + "</td>" + "<td onclick='select_norm(" + scaleid + ");' >选择</td>"
                                            + "<td id='td_" + scaleid + "' onclick='set_report_permission(" + scaleid
                                            + ");' >设置</td>" + "<td onclick='set_test_limit(this," + scaleid
                                            + ");' >无</td>" + "</tr>");
                        if ($("#objectType").val() == 2)
                            $("#scalelist").append(
                                    "<tr id='tr_" + scaleid + "'  align='center'>"
                                            + "<td><input type='checkbox' name='scaleId' value='" + checkvalue
                                            + "'></td>" + "<td>" + title + "</td>" + "<td>" + shortname + "</td>"
                                            + "<td>" + qnum + "</td>" + "<td>" + showtitle + "</td>" + "<td>" + type
                                            + "</td>" + "<td onclick='select_norm(" + scaleid + ");' >选择</td>"
                                            + "<td onclick='set_test_limit(this," + scaleid + ");' >无</td>" + "</tr>");
                    }
                }

            });
        }
    });
}
function getS1(objectType, gradeOrderId) {
    createXMLHttp();
    xmlhttp.onreadystatechange = getScales;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getScales.do?objectType=" + objectType + "&gradeOrderId=" + gradeOrderId;
    // alert(url);
    xmlhttp.open("POST", convertURL(url), false);
    xmlhttp.send(null);
}

function getScales() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var scales = xmlhttp.responseXML.getElementsByTagName("scales");
            var inScales = xmlhttp.responseXML.getElementsByTagName("inScales");
            var selfScales = xmlhttp.responseXML.getElementsByTagName("selfScales");
            var scalelist = document.getElementById("scalelist");
            var rlen = scalelist.rows.length;
            for (var r = 0; r < rlen; r++) {
                scalelist.deleteRow(0);
            }

            createTable(scalelist, "", selfScales);

        }

    }
}
function getScales1() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var scales = xmlhttp.responseXML.getElementsByTagName("scales");
            var inScales = xmlhttp.responseXML.getElementsByTagName("inScales");
            var selfScales = xmlhttp.responseXML.getElementsByTagName("selfScales");
            var scalelist = document.getElementById("scalelist");
            var rlen = scalelist.rows.length;
            for (var r = 0; r < rlen; r++) {
                scalelist.deleteRow(0);
            }
            if (scales.length > 0) {
                var title = '心检量表';
                createTable(scalelist, title, scales);
            }
            if (inScales.length > 0) {
                var title = '赠送量表';
                createTable(scalelist, title, inScales);
            }
            if (selfScales.length > 0) {
                var title = '自定义量表';
                createTable(scalelist, title, selfScales);
            }
        }

    }
}
function dispense() {
    // alert(window.document.body.innerHTML);
    var num = valiGetBoxNum("scaleId");
    if (num < 1) {
        layer.open({
            content : '请选择要分发的量表！\r\n'
        });
    } else {
        // form1.action=url;
        form1.submit();
    }
}
function hideall() {
    $("#student").css("display", "none");
    $("#teacher").css("display", "none");
    $("#partgrade").css("display", "none");
}

String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

// 学段改变时，得到年级
function changePart() {
    var gradepart = $("#gradepart").val();
    getGrade(gradepart);
}
function getGrade(gradepart) {
    createXMLHttp();
    xmlhttp.onreadystatechange = getGrades;
    var url = "../ajax/getGrades?gradepart=" + gradepart;
    xmlhttp.open("POST", convertURL(url), false);
    xmlhttp.send(null);
}
// 得到年级
function getGrades() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var grades = xmlhttp.responseXML.getElementsByTagName("grades");
            var gradeOrderId = document.getElementById("gradeOrderId");
            deleteStudent();
            gradeOrderId.length = 0;
            for (var i = 0; i < grades.length; i++) {
                var xValue = grades[i].childNodes[0].firstChild.nodeValue;
                var xText = grades[i].childNodes[1].firstChild.nodeValue;
                var option = new Option(xText, xValue);
                gradeOrderId.options.add(option);
            }
        }
    }
}
// 年级改变时，得到班级
function changeGrade() {
    var gradeOrderId = $("#gradeOrderId").val();
    getGClass(gradeOrderId);
}
function getGClass(gradeOrderId) {
    // createXMLHttp();
    // xmlhttp.onreadystatechange=getClass;
    var localObj = window.location;

    var contextPath = localObj.pathname.split("/")[1];

    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;

    var server_context = basePath;
    var url = server_context + "/ajax/getClassesByGradeId.do?gradeOrderId=" + gradeOrderId;
    // xmlhttp.open("POST",convertURL(url),false);
    // xmlhttp.send(null);
    $.ajax({
        type : "POST",
        url : url,
        data : {
            gradeOrderId : gradeOrderId
        },
        success : function(data) {
            debugger;
            var dataXml = toXML(data);
            var i, xValue, xText, option;
            deleteStudent();
            var classes = dataXml.getElementsByTagName("classes");
            var classOrderId = document.getElementById("classId");
            classOrderId.length = 0;
            for (i = 0; i < classes.length; i++) {
                xValue = classes[i].childNodes[0].firstChild.nodeValue;
                xText = classes[i].childNodes[1].firstChild.nodeValue;
                console.log(xText);
                option = new Option(xText, xValue);
                classOrderId.options.add(option);
            }
        }
    });
}
function getClass() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {

            var i, xValue, xText, option;
            deleteStudent();
            var classes = xmlhttp.responseXML.getElementsByTagName("classes");
            var classOrderId = document.getElementById("classId");
            classOrderId.length = 0;
            for (i = 0; i < classes.length; i++) {
                xValue = classes[i].childNodes[0].firstChild.nodeValue;
                xText = classes[i].childNodes[1].firstChild.nodeValue;
                console.log(xText);
                option = new Option(xText, xValue);
                classOrderId.options.add(option);
            }
        }
    }
}
// 年级改变时，得到班级
function changeClass() {
    var gradeOrderId = $("#gradeOrderId").val();
    var classId = $("#classId").val();
    debugger;
    if (classId == "-1") {
        deleteStudent();
        return;
    }
    getStudent(gradeOrderId, classId);
}
function getStudent(gradeOrderId, classId) {
    // createXMLHttp();
    // xmlhttp.onreadystatechange=getStudents;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getStudents.do?classId=" + classId + "&gradeOrderId=" + gradeOrderId;
    // xmlhttp.open("POST",convertURL(url),true);
    // xmlhttp.send(null);
    $
            .ajax({
                type : "POST",
                url : url,
                data : {
                    classId : classId,
                    gradeOrderId : gradeOrderId
                },
                success : function(data) {
                    var dataXml = toXML(data);
                    var students = dataXml.getElementsByTagName("students");
                    deleteStudent();
                    for (var i = 0; i < students.length; i++) {
                        if (i % 5 == 0)
                            tr = studentlist.insertRow(i / 5);
                        if (i != students.length - 1) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='studentId' name='studentId' value="
                                    + students[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + students[i].childNodes[1].firstChild.nodeValue + "</label>";
                        } else if (i % 5 == 0) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='studentId' name='studentId' value="
                                    + students[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + students[i].childNodes[1].firstChild.nodeValue + "</label>";
                            tr.insertCell(1).innerHTML = "&nbsp;";
                            tr.insertCell(2).innerHTML = "&nbsp;";
                            tr.insertCell(3).innerHTML = "&nbsp;";
                            tr.insertCell(4).innerHTML = "&nbsp;";
                        } else if (i % 5 == 1) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='studentId' name='studentId' value="
                                    + students[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + students[i].childNodes[1].firstChild.nodeValue + "</label>";
                            tr.insertCell(2).innerHTML = "&nbsp;";
                            tr.insertCell(3).innerHTML = "&nbsp;";
                            tr.insertCell(4).innerHTML = "&nbsp;";
                        } else if (i % 5 == 2) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='studentId' name='studentId' value="
                                    + students[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + students[i].childNodes[1].firstChild.nodeValue + "</label>";
                            tr.insertCell(3).innerHTML = "&nbsp;";
                            tr.insertCell(4).innerHTML = "&nbsp;";
                        } else if (i % 5 == 3) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='studentId' name='studentId' value="
                                    + students[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + students[i].childNodes[1].firstChild.nodeValue + "</label>";
                            tr.insertCell(4).innerHTML = "&nbsp;";
                        } else
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='studentId' name='studentId' value="
                                    + students[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + students[i].childNodes[1].firstChild.nodeValue + "</label>";
                    }
                }
            });
}

function getStudents() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var students = xmlhttp.responseXML.getElementsByTagName("students");
            deleteStudent();
            for (var i = 0; i < students.length; i++) {
                if (i % 3 == 0)
                    tr = studentlist.insertRow(i / 3);
                if (i != students.length - 1) {
                    tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='studentId' name='studentId' value="
                            + students[i].childNodes[0].firstChild.nodeValue + ">"
                            + students[i].childNodes[1].firstChild.nodeValue;
                } else if (i % 3 == 0) {
                    tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='studentId' name='studentId' value="
                            + students[i].childNodes[0].firstChild.nodeValue + ">"
                            + students[i].childNodes[1].firstChild.nodeValue;
                    tr.insertCell(1).innerHTML = "&nbsp;";
                    tr.insertCell(2).innerHTML = "&nbsp;";
                } else if (i % 3 == 1) {
                    tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='studentId' name='studentId' value="
                            + students[i].childNodes[0].firstChild.nodeValue + ">"
                            + students[i].childNodes[1].firstChild.nodeValue;
                    tr.insertCell(2).innerHTML = "&nbsp;";
                } else
                    tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='studentId' name='studentId' value="
                            + students[i].childNodes[0].firstChild.nodeValue + ">"
                            + students[i].childNodes[1].firstChild.nodeValue;
            }
        }
    }
}

function deleteStudent() {
    studentlist = document.getElementById("studentlist");
    var rlen = studentlist.rows.length;
    for (var r = 0; r < rlen; r++) {
        studentlist.deleteRow(0);
    }
}

// 年级改变时，得到班级
function changeTeacher() {
    $("#student").css("display", "table-row");
    var teacherRole = $("#teacherRole").val();
    getTeacher(teacherRole);
}
function getTeacher(teacherRole) {
    // createXMLHttp();
    // xmlhttp.onreadystatechange = getTeachers;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getTeachers.do?teacherRole=" + teacherRole;
    // xmlhttp.open("POST",convertURL(url),true);
    // xmlhttp.send(null);
    $
            .ajax({
                type : "POST",
                url : url,
                data : {
                    teacherRole : teacherRole
                },
                success : function(data) {
                    var dataXml = toXML(data);
                    var teachers = dataXml.getElementsByTagName("teachers");
                    deleteStudent();
                    for (var i = 0; i < teachers.length; i++) {
                        if (i % 5 == 0)
                            tr = studentlist.insertRow(i / 5);
                        if (i != teachers.length - 1) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='teacherId' name='teacherId' value="
                                    + teachers[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + teachers[i].childNodes[1].firstChild.nodeValue + "</label>";
                        } else if (i % 5 == 0) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='teacherId' name='teacherId' value="
                                    + teachers[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + teachers[i].childNodes[1].firstChild.nodeValue + "</label>";
                            tr.insertCell(1).innerHTML = "&nbsp;";
                            tr.insertCell(2).innerHTML = "&nbsp;";
                            tr.insertCell(3).innerHTML = "&nbsp;";
                            tr.insertCell(4).innerHTML = "&nbsp;";
                        } else if (i % 5 == 1) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='teacherId' name='teacherId' value="
                                    + teachers[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + teachers[i].childNodes[1].firstChild.nodeValue + "</label>";
                            tr.insertCell(2).innerHTML = "&nbsp;";
                            tr.insertCell(3).innerHTML = "&nbsp;";
                            tr.insertCell(4).innerHTML = "&nbsp;";
                        } else if (i % 5 == 2) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='teacherId' name='teacherId' value="
                                    + teachers[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + teachers[i].childNodes[1].firstChild.nodeValue + "</label>";
                            tr.insertCell(3).innerHTML = "&nbsp;";
                            tr.insertCell(4).innerHTML = "&nbsp;";
                        } else if (i % 5 == 3) {
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='teacherId' name='teacherId' value="
                                    + teachers[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + teachers[i].childNodes[1].firstChild.nodeValue + "</label>";
                            tr.insertCell(4).innerHTML = "&nbsp;";
                        } else
                            tr.insertCell(i % 5).innerHTML = "<input type='checkbox' style='vertical-align:middle;' id='teacherId' name='teacherId' value="
                                    + teachers[i].childNodes[0].firstChild.nodeValue
                                    + "><label style='vertical-align:middle;'>"
                                    + teachers[i].childNodes[1].firstChild.nodeValue + "</label>";
                    }
                }
            });
}

function getTeachers() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var teachers = xmlhttp.responseXML.getElementsByTagName("teachers");
            deleteStudent();
            for (var i = 0; i < teachers.length; i++) {
                if (i % 3 == 0)
                    tr = studentlist.insertRow(i / 3);
                if (i != teachers.length - 1) {
                    tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='teacherId' name='teacherId' value="
                            + teachers[i].childNodes[0].firstChild.nodeValue + ">"
                            + teachers[i].childNodes[1].firstChild.nodeValue;
                } else if (i % 3 == 0) {
                    tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='teacherId' name='teacherId' value="
                            + teachers[i].childNodes[0].firstChild.nodeValue + ">"
                            + teachers[i].childNodes[1].firstChild.nodeValue;
                    tr.insertCell(1).innerHTML = "&nbsp;";
                    tr.insertCell(2).innerHTML = "&nbsp;";
                } else if (i % 3 == 1) {
                    tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='teacherId' name='teacherId' value="
                            + teachers[i].childNodes[0].firstChild.nodeValue + ">"
                            + teachers[i].childNodes[1].firstChild.nodeValue;
                    tr.insertCell(2).innerHTML = "&nbsp;";
                } else
                    tr.insertCell(i % 3).innerHTML = "<input type='checkbox' id='teacherId' name='teacherId' value="
                            + teachers[i].childNodes[0].firstChild.nodeValue + ">"
                            + teachers[i].childNodes[1].firstChild.nodeValue;
            }
        }

    }
}

function clickUnit() {
    var objectType = $("#objectType");
    if (objectType.val() == '') {
        layer.open({
            content : '请选择分发对象！\r\n'
        });
        objectType.focus();
    } else if (objectType.val() != 2) {
        var gradepart = $("#gradepart");
        if (gradepart.val() == 0) {
            layer.open({
                content : '请选择学段！\r\n'
            });
            gradepart.focus();
        } else {
            var gradeOrderId = $("#gradeOrderId");
            if (gradeOrderId.val() == 0) {
                layer.open({
                    content : '请选择年级！\r\n'
                });
                gradeOrderId.focus();
            } else {
                var classId = $("#classId");
                if (classId.val() == 0) {
                    layer.open({
                        content : '请选择班级！\r\n'
                    });
                    classId.focus();
                } else {
                    var num = valiGetBoxNum("studentId");
                    if (num < 1) {
                        layer.open({
                            content : '请选择分发的学生！\r\n'
                        });
                    } else
                        return true;
                }
            }
        }
    } else if (objectType.val() == 2) {
        var teacherRole = $("#teacherRole");
        if (teacherRole.val() == 0) {
            layer.open({
                content : '请选择教师类型！\r\n'
            });
            teacherRole.focus();
        } else {
            var num = valiGetBoxNum("teacherId");
            if (num < 1) {
                layer.open({
                    content : '请选择分发的教师！\r\n'
                });
            } else
                return true;
        }
    } else
        return true;
}
function clickStat() {
    var objectType = $("#objectType");
    if (objectType.val() == '') {
        layer.open({
            content : '请选择分发对象！\r\n'
        });
        objectType.focus();
    } else if (objectType.val() != 2) {
        var gradepart = $("#gradepart");
        if (gradepart.val() == 0) {
            layer.open({
                content : '请选择学段！\r\n'
            });
            gradepart.focus();
        } else {
            var gradeOrderId = $("#gradeOrderId");
            if (gradeOrderId.val() == 0) {
                layer.open({
                    content : '请选择年级！\r\n'
                });
                gradeOrderId.focus();
            } else
                return true;
        }
    } else if (objectType.val() == 2) {
        return true;
    } else
        return true;
}

function unitNext() {
    if(clickInfo()) {
        if (clickUnit()) {
            $("#step1").removeClass().addClass("step1");
            $("#step2").removeClass().addClass("step2_sel");
            $("#colony").css("display", "none");
            $("#scale").css("display", "inline-table");
            $("#info").css("display","none");
            $(".step-one").hide();
            $(".step-two").show();
            $("#task").css("display", "inline-table");
            clickScale();
        }
    }
}
function clickScaleStat() {
    var objectType = $("#objectType").val();
    var gradeOrderId = $("#gradeOrderId").val();
    getSStat(objectType, gradeOrderId);
}
function getSStat(objectType, gradeOrderId) {
    createXMLHttp();
    xmlhttp.onreadystatechange = getScalesStat;
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/getScales.do?objectType=" + objectType + "&gradeOrderId=" + gradeOrderId;
    // alert(url);
    xmlhttp.open("POST", convertURL(url), true);
    xmlhttp.send(null);
}

function getScalesStat() {
    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var scales = xmlhttp.responseXML.getElementsByTagName("scales");
            var inScales = xmlhttp.responseXML.getElementsByTagName("inScales");
            var selfScales = xmlhttp.responseXML.getElementsByTagName("selfScales");
            var scalelist = document.getElementById("scalelist");
            var rlen = scalelist.rows.length;
            for (var r = 0; r < rlen; r++) {
                scalelist.deleteRow(0);
            }
            if (scales.length > 0) {
                var title = '心检量表';
                createTable(scalelist, title, scales);
            }
            if (inScales.length > 0) {
                var title = '赠送量表';
                createTable(scalelist, title, inScales);
            }
            if (selfScales.length > 0) {
                var title = '自定义量表';
                createTable(scalelist, title, selfScales);
            }
        }

    }
}

function statNext() {
    // alert(window.document.body.innerHTML);
    if (clickStat()) {
        $("#colony").css("display", "none");
        $("#scale").css("display", "inline-table");
        clickScaleStat();
    }
}

// ///////////////////////////////////////////////////////////////////////////////////////////////////////

function setPP() {
    if (document.getElementById("ppLb").innerHTML == "ON") {
        document.getElementById("ppLb").innerHTML = "OFF";
        parentVisible = 0;
    } else {
        document.getElementById("ppLb").innerHTML = "ON";
        parentVisible = 1;
    }
}
function setTP() {
    if (document.getElementById("tpLb").innerHTML == "ON") {
        document.getElementById("tpLb").innerHTML = "OFF";
        teacherVisible = 0;
    } else {
        document.getElementById("tpLb").innerHTML = "ON";
        teacherVisible = 0;
    }
}
function setSP() {
    if (document.getElementById("spLb").innerHTML == "ON") {
        document.getElementById("spLb").innerHTML = "OFF";
        studentVisible = 0;
    } else {
        document.getElementById("spLb").innerHTML = "ON";
        studentVisible = 0;
    }
}
function cancelScaleSet() {
    $('#scaleSetDiv').css("display", "none");
}
function saveScaleSet() {
    // 获得当前量表记录所在的checkbox
    var ck = getCurrentScaleCheckbox(currentScaleId);
    warningVisible = $("#warningSelect").val();
    ck.value = currentScaleId + "_" + parentVisible + "_" + teacherVisible + "_" + studentVisible + "_"
            + warningVisible + "_" + haveLimit + "_" + normid;
    $('#scaleSetDiv').css("display", "none");
    clearReportSet();

}
function getCurrentScaleCheckbox(scaleid) {
    var tdid = "#td_" + scaleid;
    var ck = $(tdid).parent().find('td:eq(0)').find(':checkbox')[0];
    return ck;
}
function clearReportSet() {
    parentVisible = 1;
    teacherVisible = 1;
    studentVisible = 1;
    warningVisible = 0;
    haveLimit = 0;
    normid = -1;
}

function searchScale() {
    // alert($("#iswarning").val());
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
    var server_context = basePath;
    var url = server_context + "/ajax/searchScale.do";
    debugger;
    $.ajax({
        type : "POST",
        url : url,
        data : {
            objectType : $("#objectType").val(),
            gradepart : $("#gradepart").val(),
            gradeOrderId : $("#gradeOrderId").val(),
            scaletype : $("#scaletype").val(),
            scalesource : $("#scalesource").val(),
            iswarning : $("#iswarning").val(),
            scalename : $("#scalename").val()
        },
        dataType : "json",
        success : function(data) {
            debugger;
            $("#scalelist tbody").empty();
            if ($("#objectType").val() == 1)
                $("#scalelist").append(
                        "<tr><th><input type='checkbox' id='scaleAllCheckbox' onclick='allScaleCheck()'></th>"
                                + "<th>量表名称</th>" + "<th>英文简称</th>" + "<th>题目数量</th>" + "<th>量表显示名称</th>"
                                + "<th>量表类别</th>" + "<th>常模</th>" + "<th>量表查看权限</th>" + "<th>再测限制</th></tr>");
            if ($("#objectType").val() == 2)
                $("#scalelist").append(
                        "<tr><th><input type='checkbox' id='scaleAllCheckbox'  onclick='allScaleCheck()'></th>"
                                + "<th>量表名称</th>" + "<th>英文简称</th>" + "<th>题目数量</th>" + "<th>量表显示名称</th>"
                                + "<th>量表类别</th>" + "<th>常模</th>" + "<th>再测限制</th></tr>");

            $.each(data.scale, function(i, scale) {
                // for(var i=0;i<scale.length;i++){
                // var s = scale[i];
                var s = scale;
                var scaleid = s.scaleId;
                var shortname = s.scaleShortName;
                var source = s.scalesource;
                var qnum = s.scaleQNum;
                var title = s.scaleTitle;
                var type = s.scaleType;
                var showtitle = s.scaleTitle;
                var checkvalue;
                if ($("#objectType").val() == 1)
                    checkvalue = scaleid + "_1_1_1_0_0_-1";
                if ($("#objectType").val() == 2)
                    checkvalue = scaleid + "_0_-1";
                if ($("#objectType").val() == 1)
                    $("#scalelist").append(
                            "<tr id='tr_" + scaleid + "'  align='center'>"
                                    + "<td><input type='checkbox' name='scaleId' value='" + checkvalue + "'></td>"
                                    + "<td>" + title + "</td>" + "<td>" + shortname + "</td>" + "<td>" + qnum + "</td>"
                                    + "<td>" + showtitle + "</td>" + "<td>" + type + "</td>"
                                    + "<td onclick='select_norm(" + scaleid + ");' >选择</td>" + "<td id='td_" + scaleid
                                    + "' onclick='set_report_permission(" + scaleid + ");' >设置</td>"
                                    + "<td onclick='set_test_limit(this," + scaleid + ");' >无</td>" + "</tr>");
                if ($("#objectType").val() == 2)
                    $("#scalelist").append(
                            "<tr id='tr_" + scaleid + "'  align='center'>"
                                    + "<td><input type='checkbox' name='scaleId' value='" + checkvalue + "'></td>"
                                    + "<td>" + title + "</td>" + "<td>" + shortname + "</td>" + "<td>" + qnum + "</td>"
                                    + "<td>" + showtitle + "</td>" + "<td>" + type + "</td>"
                                    + "<td onclick='select_norm(" + scaleid + ");' >选择</td>"
                                    + "<td onclick='set_test_limit(this," + scaleid + ");' >无</td>" + "</tr>");

            });
        }
    });

}

function allScaleCheck() {
    debugger;
    var scaleallcheck = document.getElementById("scaleAllCheckbox");
    var checkarray = document.getElementsByName("scaleId");
    if (scaleallcheck.checked) {
        for (var i = 0; i < checkarray.length; i++) {
            checkarray[i].checked = true;
        }
    } else {
        for (var i = 0; i < checkarray.length; i++) {
            checkarray[i].checked = false;
        }
    }
}

function select_norm(scaleid) {
    var url = "../../assessmentcenter/scaledispense/normlist.do?scaleid=" + scaleid;
    layer.open({
        area : [ '500px', '400px' ],
        type : 2,
        btn : [ '确认', '取消' ],
        content : url,
        yes : function(index, layero) {
            debugger;
            var normid = $(window.frames["layui-layer-iframe1"].document)
                    .find('input:radio[name="radiogroup"]:checked').val();

            window.parent.setSelectedNormid(scaleid, normid);
            layer.close(index); // 如果设定了yes回调，需进行手工关闭
        }
    });
}
function setSelectedNormid(scaleid, norm_id) {
    normid = norm_id;
    var trid = "#tr_" + scaleid;
    var ck = $(trid).find('td:eq(0)').find(':checkbox')[0];
    if ($("#objectType").val() == 1)
        ck.value = currentScaleId + "_" + parentVisible + "_" + teacherVisible + "_" + studentVisible + "_"
                + warningVisible + "_" + haveLimit + "_" + norm_id;
    if ($("#objectType").val() == 2)
        ck.value = currentScaleId + "_" + haveLimit + "_" + normid;
}
