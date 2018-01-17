<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="scaleformurl" value="../../scaletoollib/scalelook/searchScales.do"></c:set>
<form:form id="scaleFilterParam" name="scaleFilterParam" method="post" commandName="scaleFilterParam"
    action="${scaleformurl }">
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">量表来源</label>
                <form:select class="input_160" id="scaleSource" path="scaleSourceId">
                    <form:option value="-1" label="请选择"></form:option>
                    <form:options items="${scaleSources }" itemValue="name" itemLabel="name"></form:options>
                </form:select>
            </li>
            <li>
                <label class="name03">量表类别</label>
                <form:select class="input_160" id="scaleType" path="scaleTypeId">
                    <form:option value="-1" label="请选择"></form:option>
                    <form:options items="${scaleTypes }" itemValue="name" itemLabel="name"></form:options>
                </form:select>
            </li>
            <li>
                <label class="name03">量表名称</label>
                <form:input path="scaleId" class="input_160" type="text" onkeydown='if(event.keyCode==13) return false;'></form:input>
            </li>
        </ul>
    </div>
    <!--搜索条件区end（每行三列搜索条件，可以复制）-->
    <!--搜索条件区start（每行三列搜索条件，可以复制）-->
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">适用人群</label>
                <form:select class="input_160" id="applicable" path="applicablePerson">
                    <form:option value="-1" label="请选择"></form:option>
                    <form:options items="${xd }" itemValue="info" itemLabel="info"></form:options>
                </form:select>
            </li>
            <li>
                <label class="name03">是否预警</label>
                <form:select class="input_160" id="isWarn" path="isWarn">
                    <form:option value="-1" label="请选择 "></form:option>
                    <form:option value="false" label="否 "></form:option>
                    <form:option value="true" label="是"></form:option>
                </form:select>
            </li>
        </ul>
    </div>
    <div class="buttonContent">
        <div class="buttonLeft">
            <ul>
                <li>
                    <shiro:hasPermission name="assessmentcenter:checkscale:create">
                        <input class="button-mid white" type="button" value="量表导入" onclick="importScale();" />
                    </shiro:hasPermission>
                </li>
            </ul>
        </div>
        <div class="buttonRight">
            <ul>
                <li>
                    <input class="button-mid blue" type="button" value="搜索" onclick="searchScales();" />
                </li>
            </ul>
        </div>
    </div>
</form:form>
<script type="text/javascript">
    function importScale() {
        var url = "../../scaletoollib/scalelook/redirectToImportScale.do";
        $("#content2").load(url);
    }
    function searchScales() {
        debugger;
        $("#scaleFilterParam").ajaxSubmit({
            target : "#content2"
        });
    }
</script>