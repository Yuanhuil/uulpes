<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/user/teacher" />
<div class="tableContent">
    <table class="table-fixed">
        <tr class="titleBg">
            <th width="3%"><input class="checkbox01"
                type="checkbox" name="checkbox" id="checkid"></th>
            <th width="19%" style="text-align: center;">用户名</th>
            <th width="7%" style="text-align: center;">姓名</th>
            <th width="5%" style="text-align: center;">性别</th>
            <th width="14%" style="text-align: center;">角色</th>
            <%-- <c:if test="${loadFlag eq true}">
                <th width="15%" style="text-align: center;">学校</th>
            </c:if> --%>
            <th width="15%" style="text-align: center;">学校</th>
            <th width="6%" style="text-align: center;">背景</th>
            <th width="25%" style="text-align: center;">操作</th>
        </tr>
        <c:forEach var="item" items="${list}">
            <tr>
                <td><input class="checkbox01" type="checkbox"
                    name="rowcheck" value="${item.id }" /></td>
                <td class="td-fixed" title="${item.username}">${item.username}</td>
                <td class="td-fixed" title="${item.xm}">${item.xm}</td>
                <td>${item.sexname}</td>
                <td class="td-fixed"><c:forEach var="t"
                        items="${item.teacherAuthList}">
					${t.rolename}
				</c:forEach></td>
                <%-- <c:if test="${loadFlag eq true}">
                    <td class="td-fixed" title="${item.xxmc}">${item.xxmc}</td>
                </c:if> --%>
                <td class="td-fixed" title="${item.xxmc}">${item.xxmc}</td>
                <td><a href="javascript:void(0);"
                    onclick="editTeacherBjxx('${baseaction }/${item.id }/editTeacherBjxx/1.do');">修改</a>
                </td>
                <td><span class="header02"> <input
                        class="button-normal white edit" type="button"
                        chref="${baseaction }/${item.id }/view.do"
                        value="查看"> <shiro:hasPermission
                            name="systeminfo:user:teacher:update">
                            <input class="button-normal white edit"
                                type="button"
                                chref="${baseaction }/${item.id }/update.do"
                                value="修改">
                        </shiro:hasPermission> <shiro:hasPermission
                            name="systeminfo:user:teacher:delete">
                            <input class="button-normal white del"
                                type="button"
                                chref="${baseaction }/${item.id }/delete.do"
                                value="删除">
                        </shiro:hasPermission> <shiro:hasPermission
                            name="systeminfo:user:teacher:authorize">
                            <input class="button-normal white auth"
                                type="button"
                                chref="${ctx }/systeminfo/sys/user/${item.accountId }/userauth.do"
                                value="授权">
                        </shiro:hasPermission> <input class="button-normal white resetpasswd"
                        type="button" chref="${item.id }" value="密码重置">
                </span></td>
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
        $(".button-normal.white.auth").click(function() {
            var h = $(this).attr("chref");
            $('#editformdiv').empty();
            $('#editformdiv').load(h, function() {
                $("#authdialog").dialog("open");
            });
        });
        $(".button-normal.white.del").click(function() {
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
                url : "/pes/systeminfo/sys/user/teacher/resetteacherpasswd.do",
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
                $("#queryform").ajaxSubmit({
                    data : {
                        "currentPage" : page
                    },
                    target : "#tablelist",
                });
            },

        });
    });
    function editTeacherBjxx(h) {
        debugger;
        $('#tablelist').load(h);
    }
</script>