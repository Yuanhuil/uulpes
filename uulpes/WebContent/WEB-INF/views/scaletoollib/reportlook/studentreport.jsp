<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="tab-container" class='tab-container'>
    <ul class='etabs'>
        <c:if test="${orgType=='2' }">
        <c:choose>
		   <c:when test="${typeflag== '3'}">  
		        <li class='tab'><a
                href="../../scaletoollib/reportlook/studentPersonalReport.do"
                data-target="#tabs_studentReport">个人报告</a></li>       
		   </c:when>
		   <c:otherwise> 
		     <li class='tab'><a
                href="../../scaletoollib/reportlook/studentPersonalReport.do"
                data-target="#tabs_studentReport">个人报告</a></li>
            <li class='tab'><a
                href="../../scaletoollib/reportlook/studentComplianceReport.do"
                data-target="#tabs_studentReport">个人复合报告</a></li>
            <li class='tab'><a
                href="../../scaletoollib/reportlook/studentTeamReport.do"
                data-target="#tabs_studentReport">团体报告</a></li>
            <li class='tab'><a
                href="../../scaletoollib/reportlook/studentRemarkReport.do"
                data-target="#tabs_studentReport">个人评语</a></li>
		   </c:otherwise>
		</c:choose>
            
        </c:if>
        
        <c:if test="${orgType=='1' }">
            <li><a
                href="../../scaletoollib/reportlook/studentTeamReport.do"
                data-target="#tabs_studentReport"></a></li>
        </c:if>
    </ul>
    <div class='panel-container'>
        <div id="tabs_studentReport"></div>

    </div>
</div>
<script type="text/javascript">
    $('#tab-container').easytabs({
        cache : false
    });
    $(function() {
    });
</script>