<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@include file="/WEB-INF/views/common/import-css.jspf"%>
<%@include file="/WEB-INF/views/common/import-js.jspf"%>
<head>
<link href="/pes/css/report.css" rel="stylesheet" type="text/css">
<style type="text/css">
th {
    background-color: #ffffff;
    line-height: 20px;
    font-size: 12px;
}

td {
    background-color: #ffffff;
    line-height: 20px;
    text-align: left;
    padding: 0 6px;
    text-overflow: ellipsis;
    /*white-space: nowrap; */
    overflow: hidden;
    font-size: 12px;
}
</style>
</head>
<div id="tab-container" class='tab-container archive-title'>
    <ul class='etabs archive-msg'>
        <li class='tab table-archives'>
            <a href="#tabs1-html">个人基本信息</a>
        </li>
        <li class='tab table-archives'>
            <a id="testInfo" href="#tabs2-html">心理测评信息</a>
        </li>
        <li class='tab table-archives'>
            <a id="recordInfo" href="#tabs3-html">心理辅导信息</a>
        </li>
        <li class='tab table-archives'>
            <a id="interventionInfo" href="#tabs4-html">危机干预信息</a>
        </li>
    </ul>
    <div id="tabs1-html">
        <div id="personInfo">
            <%@include file="personInfo.jsp"%>
        </div>
    </div>
    <div id="tabs2-html">
        <div id="testInfoTab"></div>
    </div>
    <div id="tabs3-html">
        <div id="recordInfoTab"></div>
    </div>
    <div id="tabs4-html">
        <div id="interventionInfoTab"></div>
    </div>
</div>
<script type="text/javascript">
    $(function() {
        $('#tab-container').easytabs();
    })
    $("#testInfo").click(function() {

        var h = '/pes/archivemanage/archiveTeacher/teacherCompositeReport.do?resultId=2&userid=' + $("#userid").val();
        ;
        $('#testInfoTab').load(h);
    });

    $("#recordInfo").click(function() {

        var h = '/pes/archivemanage/archiveTeacher/teacherRecord.do?sfzjh=' + $("#sfzjh").val();
        ;
        $('#recordInfoTab').load(h);
    });
    $("#interventionInfo").click(function() {

        var h = '/pes/archivemanage/archiveTeacher/warningIntervene.do?status=4&sfzjh=' + $("#sfzjh").val();
        ;
        $('#interventionInfoTab').load(h);
    });
</script>