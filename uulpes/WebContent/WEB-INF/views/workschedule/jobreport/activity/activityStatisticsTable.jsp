<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tableContent">
    <table class="titleBg">
        <tr>
            <th>下属学校</th>
            <c:forEach var="dictionary" items="${activitycatalogs}" >
                <th>${dictionary.name}</th>
            </c:forEach>
        </tr>
        <c:forEach var="entry" items="${resultTable}" >
            <tr>
                <c:forEach var="list" items="${entry.value}" >
                    <td>${list}</td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
</div>