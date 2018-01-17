<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/workschedule/plan" />
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
<div id="viewdialog" title="${op}工作计划">
    <form:form action="${formaction}" method="post" id="viewForm" commandName="entity">
        <div class="contentTitle02">
            <a>${entity.title }</a>
        </div>
        <div class="contentTitle03">
            <a>${entity.authorName }
                |
                <fmt:formatDate pattern="yyyy年MM月dd日 " value="${entity.writeTime}" type="date" />
            </a>
        </div>
        <div class="content2 viewnotedialog">
        <table >
            <tr>
                <td>${entity.content }</td>
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
        <c:if test="${fn:length(entity.jobPlanShareList) >0}">
            <div class="content2">
                <h1>下发单位：</h1>
                <br>
                <ul>
                    <c:forEach items="${entity.jobPlanShareList}" var="a" varStatus="xh">
                        <li class="viewnotedialog-li">
                            <td>${a.orgname}</td>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        <c:if test="${op eq '查看'}">
            <div class="content2 viewnotedialog">
                <table>
                    <tr>
                        <td width="9%">审核：</td>
                        <td width="91%">
                            <c:if test="${entity.state=='3' }">
                                                             通过
                    </c:if>
                            <c:if test="${entity.state=='4' }">
                                                             未通过
                    </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">审核意见：</td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <textarea cols="3" style="width: 823px">${entity.auditext}</textarea>
                        </td>
                    </tr>
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
                            <form:textarea path="auditext" style="width: 727px"></form:textarea>
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
