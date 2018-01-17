<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/user/student" />
<div class="tableContent">
    <table class="table-fixed">
        <tr class="titleBg">
            <th width="3%"><input class="checkbox01" type="checkbox" name="checkbox" id="checkid"></th>
            <th width="25%" style="text-align: center;">用户名</th>
            <!-- <th width="8%">学籍号</th>-->
            <th width="9%" style="text-align: center;">姓名</th>
            <!-- <th width="18%">身份证号</th>-->
            <th width="5%" style="text-align: center;">性别</th>
            <th width="10%" style="text-align: center;">年级</th>
            <th width="10%" style="text-align: center;">班级</th>
            <th width="6%" style="text-align: center;">背景</th>
            <th width="25%" style="text-align: center;">操作</th>
        </tr>
        <c:if test="${empty list }">没有符合条件的查询结果</c:if>
        <c:if test="${!empty list }"></c:if>
        <c:forEach var="item" items="${list}">
            <tr>
                <td>
                    <input class="checkbox01" type="checkbox" name="rowcheck" value="${item.id }" />
                </td>
                <td class="td-fixed" title="${item.username}">${item.username}</td>
                <!-- <td><td>${item.xjh}</td>-->
                <td class="td-fixed" title="${item.xm}">${item.xm}</td>
                <!-- <td>${item.sfzjh}</td>-->
                <td>
                    <c:if test="${item.xbm eq 1}">男</c:if>
                    <c:if test="${item.xbm eq 2}">女</c:if>
                </td>
                <td>${item.njmc}</td>
                <td>${item.bjmch}</td>
                <td>
                    <a href="javascript:void(0);"
                        onclick="editStudentBjxx('${baseaction }/${item.id }/editStudentBjxx/1.do');">修改</a>
                </td>
                <td>
                    <span class="header02"> <input class="button-normal white edit" type="button"
                            chref="${baseaction }/${item.id }/view.do" value="查看"> <shiro:hasPermission
                            name="systeminfo:user:student:update">
                            <input class="button-normal white edit" type="button"
                                chref="${baseaction }/${item.id }/update.do" value="修改">
                        </shiro:hasPermission> <shiro:hasPermission name="systeminfo:user:student:delete">
                            <input class="button-normal white del" type="button"
                                chref="${baseaction }/${item.id }/delete.do" value="删除">
                        </shiro:hasPermission> <input class="button-normal white resetpasswd" type="button" chref="${item.id }" value="密码重置">
                    </span>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div id="pagediv"></div>
<script type="text/javascript">
    $(function() {
        $("#checkid").click(function() {
            $('input[type=checkbox]').prop('checked', $(this).prop('checked'));
        });
        $(".button-normal.white.edit").click(function() {
            var h = $(this).attr("chref");
            $('#editformdiv').empty();
            $('#editformdiv').load(h, function() {
                $("#editdialog").dialog("open");
            });
        });

        $(".button-normal.white.del").click(function() {
            debugger;
            var h = $(this).attr("chref");
            layer.confirm('确定要删除该记录内容吗?', {
                btn : [ '是', '否' ]
            }, function(index) {
                $('#content2').load(h);
                layer.close(index);
            }, function(index) {
                layer.close(index);
            });
        });
        $(".button-normal.white.resetpasswd").click(function() {
            var id = $(this).attr("chref");
            $.ajax({
                type : "POST",
                url : "/pes/systeminfo/sys/user/student/resetstudentpasswd.do",
                data : {
                    id : id
                },
                success : function(msg) {
                    layer.open({
                        content : "已成功重置密码"
                    });
                },
                error : function() {
                    layer.open({
                        content : "无法重置密码"
                    });
                }
            });
        });
        $("#pagediv").jstlPaginator({
            showtotalPage : true,
            showgotoPage : true,
            currentPage : "${page.currentPage}",
            totalPages : "${page.totalPage}",
            totalNumbers : "${page.totalResult}",
            onPageClicked : function(event, originalEvent, page) {
                $("#studentform").ajaxSubmit({
                    data : {
                        "currentPage" : page
                    },
                    target : "#tablelist",
                });
            },

        });
    });
    function editStudentBjxx(h) {
        debugger;
        $('#tablelist').load(h);
    }
</script>