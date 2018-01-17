<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/workschedule/activityrecord/research" />
<c:choose>
    <c:when test="${op eq '查看'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page" value="" />
    </c:when>
    <c:when test="${op eq '审核'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page" value="${baseaction }/${entity.id}/audit.do" />
    </c:when>
    <c:when test="${op eq '下发'}">
        <c:remove var="formaction" />
        <c:set var="formaction" scope="page" value="${baseaction }/${entity.id}/send.do" />
    </c:when>
</c:choose>
<div id="viewdialog" title="${op}教科研记录">
    <form:form action="${formaction}" method="post" id="viewForm" commandName="entity">
        <div class="contentTitle02">
            <a>${entity.title }</a>
        </div>
        <div class="contentTitle03">
            <a>${entity.schoolyear }学年度第${entity.term }学期活动记录表</a>
        </div>
         <div class="content2 viewnotedialog">
        <table>
            <tr>
                <td style="width: 15%">
                    <h1>时间:</h1>
                </td>
                <td colspan="3">
                    <fmt:formatDate pattern="yyyy年MM月dd日 HH时mm分" value="${entity.starttime}" type="both" />
                    至
                    <fmt:formatDate pattern="yyyy年MM月dd日 HH时mm分" value="${entity.endtime}" type="both" />
                </td>
            </tr>
            <tr>
                <td>
                    <h1>主题:</h1>
                </td>
                <td colspan="3">${entity.subject }</td>
            </tr>
            <tr>
                <td>
                    <h1>主要人员:</h1>
                </td>
                <c:if test="${!empty entity.level}">
                    <td>${entity.partinpersons }</td>
                    <td style="width: 15%">
                        <h1>研究阶段:</h1>
                    </td>
                    <td>${entity.levelname }</td>
                </c:if>
                <c:if test="${!empty entity.num}">
                    <td>${entity.partinpersons }</td>
                    <td style="width: 15%">
                        <h1>培训人数:</h1>
                    </td>
                    <td>${entity.num }</td>
                </c:if>
                <c:if test="${empty entity.num && empty entity.level }">
                    <td colspan="3">${entity.partinpersons }</td>
                </c:if>
            </tr>
            <tr>
                <td>
                    <h1>是否发表刊物:</h1>
                </td>
                <td>
                    <c:if test="${entity.ispublish eq false}">否</c:if>
                    <c:if test="${entity.ispublish eq true}">是</c:if>
                </td>
                <td style="width: 15%">
                    <h1>发表刊物:</h1>
                </td>
                <td>${entity.publishbooks }</td>
            </tr>
            <tr>
                <td>
                    <h1>是否获奖:</h1>
                </td>
                <td>
                    <c:if test="${entity.isreward eq false}">否</c:if>
                    <c:if test="${entity.isreward eq true}">是</c:if>
                </td>
                <td style="width: 15%">
                    <h1>获奖情况:</h1>
                </td>
                <td>${entity.rewards }</td>
            </tr>
            <tr>
                <td>
                    <h1>内容详情:</h1>
                </td>
                <td colspan="3">${entity.content }</td>
            </tr>
        </table>
        </div>
        <c:if test="${fn:length(attachments) >0}">
         <div class="content2 viewnotedialog">
            <h1>附件：</h1>
            <table>
                <c:forEach items="${attachments}" var="a">
                    <tr id="file_${a.jobattachment.id}">
                        <td>
                            <a href="${a.jobattachment.savePath}" target="_blank">${a.jobattachment.name}</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            </div>
        </c:if>
        <c:if test="${op eq '审核'}">
            <div class="content4">
                <table>
                    <tr>
                        <td width="9%">审核：</td>
                        <td width="91%">
                            <form:radiobutton path="state" value="3" />
                            通过
                            <form:radiobutton path="state" value="4" />
                            未通过
                        </td>
                    </tr>
                    <tr>
                        <td>审核意见：</td>
                        <td>
                            <form:textarea path="auditext"></form:textarea>
                        </td>
                    </tr>
                </table>
            </div>
        </c:if>
    </form:form>
</div>
<script type="text/javascript">
    $(function() {
        var buttonsOps = {};
        <c:choose>
        <c:when test="${op eq '审核'}">
        buttonsOps = {
            "确认" : function() {
                if (!$("#viewForm").validationEngine('validate'))
                    return false;
                $("#viewForm").ajaxSubmit({
                    target : "#content2",
                    success : function() {
                        $("#viewdialog").dialog("close");
                        $("#content2").noty({
                            text : "保存成功",
                            type : "success",
                            timeout : 2000,
                            closeWith : [ 'click' ],
                            layout : 'topCenter',
                            theme : 'defaultTheme',
                        });
                    },
                    error : function() {
                        $("#content2").noty({
                            text : "保存失败",
                            type : "error",
                            timeout : 2000,
                            closeWith : [ 'click' ],
                            layout : 'topCenter',
                            theme : 'defaultTheme',
                        });
                    }
                });
                $("#viewForm").clearForm();
            },
            "取消" : function() {
                $("#viewdialog").dialog("close");
            }
        };
        </c:when>
        <c:when test="${op eq '查看'}">
        buttonsOps = {
            "返回" : function() {
                $("#viewdialog").dialog("close");
            }
        };
        </c:when>
        </c:choose>
        $("#viewdialog").dialog({
            appendTo : "#editformdiv",
            autoOpen : false,
            modal : true,
            height : 700,
            width : 900,
            position: { my: "top", at: "top", of: "#topHeader" },
            buttons : buttonsOps
        });
    });
</script>
