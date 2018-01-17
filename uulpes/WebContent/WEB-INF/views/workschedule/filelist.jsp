<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="edit-FileListTable">
    <table id="filelisttable">
        <tr>
            <th style="display: none"></th>
            <th class="filelisttable-title">名称</th>
            <th class="filelisttable-title notice-filelisttable-operator">操作</th>
        </tr>
        <tr class="tmp" style="display: none">
            <td style="display: none"></td>
            <td class="filelisttable-list-name"></td>
            <td class="filelisttable-list-operator"></td>
        </tr>
        <c:forEach items="${attachments}" var="a">
            <tr id="file_${a.jobattachment.id}">
                <td>
                    <a href="${a.jobattachment.savePath}" target="_blank">${a.jobattachment.name}</a>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${a.jobattachment.showfirstpage eq '1'}">
                            <input type="button" value="取消放置首页" class="old button-small white firstpage"
                                href="${baseaction }/file/${entity.id}/${a.jobattachment.id}/firstpage.do" />
                        </c:when>
                        <c:otherwise>
                            <input type="button" value="放置首页" class="old button-small white firstpage"
                                href="${baseaction }/file/${entity.id}/${a.jobattachment.id}/firstpage.do" />
                        </c:otherwise>
                    </c:choose>
                    <input type="button" value="删除" class="old button-small white filedel"
                        href="${baseaction }/file/${entity.id}/${a.jobattachment.id}/del.do" />
                </td>
            </tr>
        </c:forEach>
    </table>
</div>