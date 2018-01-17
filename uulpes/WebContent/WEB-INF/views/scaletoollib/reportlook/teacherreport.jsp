<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="tab-container" class='tab-container'>
    <ul class='etabs'>
        <c:if test="${orgType=='2' }">
            <li class='tab'><a
                href="../../scaletoollib/reportlook/teacherPersonalReport.do"
                data-target="#tabs_teacherReport">个人报告</a></li>
            <li class='tab'><a
                href="../../scaletoollib/reportlook/teacherComplianceReport.do"
                data-target="#tabs_teacherReport">个人复合报告</a></li>
            <li class='tab'><a
                href="../../scaletoollib/reportlook/teacherTeamReport.do"
                data-target="#tabs_teacherReport">团体报告</a></li>
        </c:if>
        <c:if test="${orgType=='1' }">
            <li><a
                href="../../scaletoollib/reportlook/teacherTeamReport.do"
                data-target="#tabs_teacherReport"></a></li>
        </c:if>
    </ul>
    <div class='panel-container'>
        <div id="tabs_teacherReport"></div>
    </div>
</div>
<script type="text/javascript">
    $('#tab-container').easytabs({
        cache : false
    });
    $(function() {
        $("#dispatchTime").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
        $("#testStratTime").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
        $("#testEndTime").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
        $("#dispatchTime2").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
        $("#testStratTime2").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
        $("#testEndTime2").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
    });
</script>