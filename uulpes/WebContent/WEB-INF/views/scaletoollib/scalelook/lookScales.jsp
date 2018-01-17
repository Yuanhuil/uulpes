<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tab-Container" id="showScaleTitle">
    <!--搜索条件区start（每行三列搜索条件，可以复制）-->
    <div id="filterContent">
        <%@include file="scalefilter.jsp"%>
    </div>
    <div id="tablelist">
        <%@include file="scaletable.jsp"%>
    </div>
    <!-- <div>量表个数：${scaleListSize}</div>-->
    <div id="scalenormcon"></div>
</div>
