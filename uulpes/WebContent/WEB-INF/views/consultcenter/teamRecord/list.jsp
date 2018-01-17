<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<table id="content" class="table table-hover">
    <thead>
        <tr>
            <th>团队名称</th>
            <th>咨询类型</th>
            <th>时间</th>
            <th>咨询员</th>
            <th>操作</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${list}" var="m">
            <tr>
                <td>${teamid[m.teamid]}</td>
                <td>${consultationtypeid[m.consultationtypeid]}</td>
                <td>
                    <fmt:formatDate value="${m.begintime}" pattern="yyyy-MM-dd" />
                    至
                    <fmt:formatDate value="${m.endtime }" type="date" />
                </td>
                </td>
                <td>${teacherid[m.teacherid]}</td>
                <td>
                    <input class="button-small white edit" id="${m.id}" type="button" value="编辑">
                    <input class="button-small white del" id="${m.id}" type="button" value="删除">
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<div id="pagediv"></div>
<script type="text/javascript">
    $(function() {
        $("#pagediv").jstlPaginator({
            showtotalPage : true,
            showgotoPage : true,
            currentPage : "${page.currentPage}",
            totalPages : "${page.totalPage}",
            totalNumbers : "${page.totalResult}",
            onPageClicked : function(event, originalEvent, page) {
                $("#queryform").ajaxSubmit({
                    data : {
                        "currentPage" : page
                    },
                    target : "#list",
                });
            },

        });

    });
    jQuery('.edit').bind('click', function() {
        var id = jQuery(this).attr("id");
        var h = '/pes/consultcenter/teamRecord/addOrUpdate.do?id=' + id;
        $('#editDiv').html();
        $('#editDiv').load(h, function() {
            $("#dialog-form1").dialog("open");
        });

    });

    jQuery('.del').bind(
            'click',
            function() {
                if (confirm('确定将此记录删除?')) {
                    var id = jQuery(this).attr("id");
                    divLoadUrl("list", '/pes/consultcenter/teamRecord/delete.do?id=' + id + '&consultationtypeid1='
                            + jQuery('#consultationtypeid').val() + '&consultationmodeid1='
                            + jQuery('#consultationmodeid').val() + '&teacherid1=' + jQuery('#teacherid').val()

                            + '&beginDate=' + jQuery('#beginDate').val() + '&endDate=' + jQuery('#endDate').val());
                }

            });
</script>