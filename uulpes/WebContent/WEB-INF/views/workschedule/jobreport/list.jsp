<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/jobreport" />
<div id="tab-container" class='tab-container'>
     <ul class='etabs'>
        <c:if test="${orgtype==2}">
            <c:if test="${orgtype ne '3' }">
               <li class='tab'><a href="${baseaction }/activity/list.do" data-target="#reportdiv">活动汇总</a></li>
            </c:if>
            <li class='tab'><a href="${baseaction }/plan/list.do" data-target="#reportdiv">计划汇总</a></li>
        </c:if>
        <c:if test="${orgtype==1}">
            <li class='tab'><a href="${baseaction }/unit/list.do" data-target="#reportdiv">单位汇总</a></li>
            <c:if test="${orgtype ne '3' }">
               <li class='tab'><a href="${baseaction }/activity/list.do" data-target="#reportdiv">活动汇总</a></li>
            </c:if>
            <li class='tab'><a href="${baseaction }/plan/list.do" data-target="#reportdiv">计划汇总</a></li>
        </c:if>
    </ul>
    <div class='panel-container'>
        <div id="reportdiv"></div>
    </div>
</div>
<script type="text/javascript">
    $(function(){
        $('#tab-container').easytabs({cache:false});
    });
</script>