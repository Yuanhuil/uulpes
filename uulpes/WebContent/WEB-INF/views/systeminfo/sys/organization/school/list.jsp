<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="schoolid" value="${entity.id }" scope="session"></c:set>
<c:set var="psychicteams" value="${entity.psychicyTeams}" />
<c:set var="psychicyJobs" value="${entity.psychicyJobs}" />
<div id="tab-container" class='tab-container'>
     <ul class='etabs hide-msg'>
        <li class='tab'>
            <a href="#tabs_baseinfo">基本信息</a>
        </li>
    </ul>
    <div class='panel-container'>
        <div id="baseinfodiv">
            <%@include file="listbaseinfo.jsp"%>
        </div>
    </div>
</div>
<div id="addteamDiv">
    <%@include file="addteam.jsp"%>
</div>
<div id="addjobDiv">
    <%@include file="addjob.jsp"%>
</div>
<script type="text/javascript">
    $("#baseinfoform").validationEngine();
    $('#tab-container').easytabs();
    $("#baseinfoform").ajaxForm({
        target : "#content2"
    });
</script>
