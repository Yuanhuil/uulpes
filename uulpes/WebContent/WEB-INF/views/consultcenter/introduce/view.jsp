<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>
table{
    margin-top: 20px;
}

tr {
    background-color: #ffffff;
    line-height: 30px;
    text-align: center;
    padding: 0px;
}

th {
    background-color: #b6b6b6;
    line-height: 30px;
    text-align: center;
    padding: 0px;
}

td {
    background-color: #ffffff;
    line-height: 30px;
    text-align: center;
    padding: 0px;
    font-size: 14px;
    color: #212121;
}

.nivo-main-image {
    width: 240px;
    height: 280px;
}

.work-time {
    margin-left: 30px;
    margin-top: 20px;
    font-size: 14px;
    color: #212121;
}

</style>
<div class="buttonContent">
    <ul class="center-ul">
        <li>
            <shiro:hasPermission name="consultcenter:centersetting:update">
                <button type="button" class="introduce-edit" id="edit">编辑</button>
            </shiro:hasPermission>
        </li>
    </ul>
</div>
<div class="mainContent01">
<form:form modelAttribute="introducePage">
    <div class="photoArea01">
        <div class="slider-wrapper theme-default">
            <div id="slider" class="nivoSlider" style="width:auto;height:auto;">
                <c:forEach items="${attachments}" var="m">
                        <img src="/pes/${m.savePath}/${m.saveName}${m.mineType}" width="480px" height="240px">
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="">
        <div class="mainDescription">
            <li class="introduce-name">${introducePage.introduce.name}</li>
            <li class="introduce-address">地址:${introducePage.introduce.address}</li>
            <li class="introduce-tel">电话:${introducePage.introduce.telphone}</li>
            <li class="introduce-tel">简介:${introducePage.introduce.content}</li>
        </div>
        <div>
            <ul>
                <li class="work-time">工作时间</li>
            </ul>
            <table>
                <tbody>
                   <tr>
                      <td style="display:${introducePage.introduceJobTimes[0].week<10?'table-cell':'none'}"><form:hidden path="introduceJobTimes[0].id"
            value="${introducePage.introduceJobTimes[0].id}" />
        <form:label path="introduceJobTimes[0].week" value="1">星期一</form:label></td>
                      <td style="display:${introducePage.introduceJobTimes[1].week<10?'table-cell':'none'}"><form:hidden path="introduceJobTimes[1].id"
            value="${introducePage.introduceJobTimes[1].id}" />
        <form:label path="introduceJobTimes[1].week" value="2">星期二</form:label></td>
                      <td style="display:${introducePage.introduceJobTimes[2].week<10?'table-cell':'none'}"><form:hidden path="introduceJobTimes[2].id"
            value="${introducePage.introduceJobTimes[2].id}" />
        <form:label path="introduceJobTimes[2].week" value="3">星期三</form:label></td>
                      <td style="display:${introducePage.introduceJobTimes[3].week<10?'table-cell':'none'}"><form:hidden path="introduceJobTimes[3].id"
            value="${introducePage.introduceJobTimes[3].id}" />
        <form:label path="introduceJobTimes[3].week" value="4">星期四</form:label></td>
                      <td style="display:${introducePage.introduceJobTimes[4].week<10?'table-cell':'none'}"><form:hidden path="introduceJobTimes[4].id"
            value="${introducePage.introduceJobTimes[4].id}" />
        <form:label path="introduceJobTimes[4].week" value="5">星期五</form:label></td>
                      <td style="display:${introducePage.introduceJobTimes[5].week<10?'table-cell':'none'}"><form:hidden path="introduceJobTimes[5].id"
            value="${introducePage.introduceJobTimes[5].id}" />
        <form:label path="introduceJobTimes[5].week" value="6">星期六</form:label></td>
                      <td style="display:${introducePage.introduceJobTimes[6].week<10?'table-cell':'none'}"><form:hidden path="introduceJobTimes[6].id"
            value="${introducePage.introduceJobTimes[6].id}" />
        <form:label path="introduceJobTimes[6].week" value="6">星期日</form:label></td>
                   </tr>
                   <tr>
                     <td style="display:${introducePage.introduceJobTimes[0].week<10?'table-cell':'none'}"><form:label path="introduceJobTimes[0].starttimeid"
                cssClass="validate[required,custom[name]]">${timeEnum[introducePage.introduceJobTimes[0].starttimeid]}--${timeEnum[introducePage.introduceJobTimes[0].endtimeid]}</form:label>
                     </td>
                     <td style="display:${introducePage.introduceJobTimes[1].week<10?'table-cell':'none'}"><form:label path="introduceJobTimes[1].starttimeid"
                cssClass="validate[required,custom[name]]">${timeEnum[introducePage.introduceJobTimes[1].starttimeid]}--${timeEnum[introducePage.introduceJobTimes[1].endtimeid]}</form:label>
                     </td>
                     <td style="display:${introducePage.introduceJobTimes[2].week<10?'table-cell':'none'}"><form:label path="introduceJobTimes[2].starttimeid"
                cssClass="validate[required,custom[name]]">${timeEnum[introducePage.introduceJobTimes[2].starttimeid]}--${timeEnum[introducePage.introduceJobTimes[2].endtimeid]} </form:label>
                     </td>
                     <td style="display:${introducePage.introduceJobTimes[3].week<10?'table-cell':'none'}">
                        <form:label path="introduceJobTimes[3].starttimeid"
                cssClass="validate[required,custom[name]]">${timeEnum[introducePage.introduceJobTimes[3].starttimeid]}--${timeEnum[introducePage.introduceJobTimes[3].endtimeid]}</form:label>
                     </td>
                     <td style="display:${introducePage.introduceJobTimes[4].week<10?'table-cell':'none'}">
                        <form:label path="introduceJobTimes[4].starttimeid"
                cssClass="validate[required,custom[name]]">${timeEnum[introducePage.introduceJobTimes[4].starttimeid]}--${timeEnum[introducePage.introduceJobTimes[4].endtimeid]} </form:label>
                     </td>
                     <td style="display:${introducePage.introduceJobTimes[5].week<10?'table-cell':'none'}">
                        <form:label path="introduceJobTimes[5].starttimeid"
                cssClass="validate[required,custom[name]]">${timeEnum[introducePage.introduceJobTimes[5].starttimeid]}--${timeEnum[introducePage.introduceJobTimes[5].endtimeid]} </form:label>
                     </td>
                     <td style="display:${introducePage.introduceJobTimes[6].week<10?'table-cell':'none'}">
                        <form:label path="introduceJobTimes[6].starttimeid"
                cssClass="validate[required,custom[name]]">${timeEnum[introducePage.introduceJobTimes[6].starttimeid]}--${timeEnum[introducePage.introduceJobTimes[6].endtimeid]} </form:label>
                     </td>
                   </tr>
                </tbody>
            </table>
        </div>
    </div>
    </form:form>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('#slider').nivoSlider();
    });
</script>
