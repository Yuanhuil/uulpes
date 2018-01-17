<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="studentsdispatched">
    <!--搜索条件区start（每行三列搜索条件，可以复制）-->
    <div id="filterContent">
        <%@include file="studentsdispatchfilter.jsp"%>
    </div>
    <div id="tableliststudent">
        <%@include file="studentstable.jsp"%>
    </div>
    <div id="pagediv1"></div>
</div>
<script type="text/javascript">
    $(function() {
        $("#pagediv1").jstlPaginator({
            showtotalPage : true,
            showgotoPage : true,
            currentPage : "${page.currentPage}",
            totalPages : "${page.totalPage}",
            totalNumbers : "${page.totalResult}",
            onPageClicked : function(event, originalEvent, page) {
                $("#dispatcherFilterParamStudent").ajaxSubmit({
                    data : {
                        "currentPage" : page
                    },
                    target : "#tabs_dispatch",
                });
            },

        });
    });
</script>