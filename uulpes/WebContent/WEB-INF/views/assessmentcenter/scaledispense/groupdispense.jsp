<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<script src="${ctx}/js/jquerydispense.js"></script>
<style type="text/css">
.scalepanel {
    border: none;
    width: 300px;
    height: 220px;
    float: right;
    background-color: #FFFFFF;
    border: solid 1px #B8B8B8;
    display: none;
    border-radius: 10px;
}

.dataloading {
    display: none;
    width: 300px;
    height: 200px;
    background: url(../../images/dataloading.gif) no-repeat center;
    margin: auto;
}
</style>
<script type="text/javascript">
    function startdispense() {
        layer.open({
            content : "startdispense"
        });
        var num = valiGetBoxNum("scaleId");
        if (num < 1) {
            layer.open({
                content : "请选择要分发的量表！\r\n"
            });
        } else {
            $("#dispenseform").submit();
        }
    }
</script>
<c:set var="baseaction" value="${ctx}/assessmentcenter/scaledispense" />
<div class="stepControl">
    <div id="step1" class="step1_sel">第一步</div>
    <div id="step2" class="step2">第二步</div>
</div>
<form id="dispenseform" action="${baseaction}/groupdistribute.do" method="post">
    <table class="table_style table-time" id="info">
        <tr>
            <td class="start-time table-width">
                <li class="li-time">
                    开始时间
                    <label>
                        <input class="scaletime" type="text" id="starttime" name="starttime" value="" />
                    </label>
                </li>
                <li class="endtime">
                    结束时间
                    <label>
                        <input class="scaletime" type="text" id="endtime" name="endtime" value="" />
                    </label>
                </li>
            </td>
        </tr>
    </table>
    <table class="table-object" id="colony">
        <tr>
            <td width="9%" calss="distribute-object">分发对象</td>
            <td>
                <label>
                    <select class="select_160" id="objectType" name="objectType" onChange="clickRole()">
                        <option value="">请选择</option>
                        <option value="1">学生</option>
                        <option value="2">教师</option>
                    </select>
                </label>
            </td>
        </tr>
        <tr id="partgrade" class="tr-hide">
            <td width="9%" calss="distribute-object">学段</td>
            <td>
                <label>
                    <select class="select_160" id="gradepart" name="gradepart" onChange="clickPart()">
                        <option value="0">请选择</option>
                        <c:forEach items="${xdlist}" var="xd">
                            <option value="${xd}"><c:choose>
                                    <c:when test="${xd=='1'}">小学</c:when>
                                    <c:when test="${xd=='2'}">初中</c:when>
                                    <c:when test="${xd=='3'}">高中</c:when>
                                    <c:otherwise>中职</c:otherwise>
                                </c:choose></option>
                        </c:forEach>
                    </select>
                </label>
            </td>
        </tr>
        <tr id="student" class="tr-hide">
            <td width="9%">&nbsp;</td>
            <td width="87%">
                <table id="gradeclass">
                </table>
            </td>
        </tr>
        <tr id="teacher" class="tr-hide">
            <td width="9%" calss="distribute-object">
                全选&nbsp;&nbsp;
                <input class="checkbox01" type="checkbox" name="checkbox" id="checkid">
            </td>
            <td width="87%" id="teacherRoleContainer">
                <input class="checkbox01" type="checkbox" name="teacherRole" value="24" />
                领导&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input class="checkbox01" type="checkbox" name="teacherRole" value="32" />
                年级主任&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input class="checkbox01" type="checkbox" name="teacherRole" value="21" />
                心理老师&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input class="checkbox01" type="checkbox" name="teacherRole" value="23" />
                班主任&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input class="checkbox01" type="checkbox" name="teacherRole" value="29" />
                任课老师 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input class="checkbox01" type="checkbox" name="teacherRole" value="20" />
                学校管理员
            </td>
        </tr>
    </table>
    <div class="step-one">
        <input type="button" class="green" value="下一步" onClick="colonyNext()">
    </div>
    <table width="98%" border="0" align="center" cellpadding="0" cellspacing="1" class="table_style" id="scale"
        style="display: none">
        <tr>
            <td>
                <table width="98%" border="0" align="center" id="scalefilter">
                    <tr>
                        <td width="18%" class="start-time">量表来源:</td>
                        <td width="25%">
                            <select class="select_160" id="scalesource">
                            </select>
                        </td>
                        <td width="18%" class="start-time">量表类别:</td>
                        <td width="25%">
                            <select class="select_160" id="scaletype">
                            </select>
                        </td>
                        <td width="14%" rowspan="2" class="start-time">
                            <input class="button-big blue" type="button" value="查找量表" onclick="searchScale()"
                                style="height: 30px;"></input>
                        </td>
                    </tr>
                    <tr>
                        <td width="15%" class="start-time">是否预警:</td>
                        <td width="20%">
                            <select class="select_160" id="iswarning">
                                <option value="-1">请选择</option>
                                <option value="true">预警</option>
                                <option value="false">不预警</option>
                            </select>
                        </td>
                        <td class="start-time">量表名称:</td>
                        <td>
                            <input class="input_160" width="100" type="text" id="scalename" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <div class="div-scale">
                    <table class="table-scalelist" id="scalelist">
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </td>
        </tr>
    </table>
    <table class="table_style table-name" id="task">
        <tr>
            <td width="8%" class="start-time">任务名称</td>
            <td width="86%">
                <input class="input_260" type="text" id="taskname" name="taskname" />
            </td>
        </tr>
    </table>
    <div class="step-two">
        <td colspan="2" class="start-time">
            <input type="button" value="上一步" class="green" onClick="taskUp()" />
            <input type="button" class="green" value="分发" onclick="startdispense()" />
    </div>
</form>
<div id="dataloading" class="dataloading"></div>
<div id="scaleSetDiv" class="scalepanel">
    <table id="scalesetTable" calss="table-view">
        <tr>
            <td class="view-permissions">家长查看权限</td>
            <td>
                <label id="ppLb" onclick="setPP()">ON</label>
            </td>
        </tr>
        <tr>
            <td class="view-permissions">班主任查看权限</td>
            <td>
                <label id="tpLb" onclick="setTP()">ON</label>
            </td>
        </tr>
        <tr>
            <td class="view-permissions">学生查看权限</td>
            <td>
                <label id="spLb" onclick="setSP()">ON</label>
            </td>
        </tr>
        <tr>
            <td class="view-permissions">预警量表查看权限</td>
            <td>
                <select id="warningSelect">
                    <option value="0">请选择</option>
                    <option value="1">一级</option>
                    <option value="2">二级</option>
                    <option value="3">三级</option>
                </select>以下可查看
            </td>
        </tr>
        <tr>
            <td colspan="2" class="start-time">
                <input type="button" value="确定" onclick="saveScaleSet()">
                <input type="button" value="取消" onclick="cancelScaleSet()">
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
    $(function() {
        $("#dispenseform").ajaxForm({
            type : "post",
            target : "#groupdispense",
            success : function() {
                $("#dataloading").css("display", "none");
                layer.alert("分发完成！");
            },
            error : function(data) {
                $("#dataloading").css("display", "none");
                layer.alert(data.responseText);
            }
        });
        $("#checkid").click(function() {
            $('input[type=checkbox]').prop('checked', $(this).prop('checked'));
        });
    });
    function startdispense() {
        $("#dataloading").css("display", "block");
        var num = valiGetBoxNum("scaleId");
        if (num < 1) {
            layer.open({
                content : "请选择要分发的量表！\r\n"
            });
        } else {
            $("#dispenseform").submit();
        }
    }

    $("#starttime").datepicker({ //绑定开始日期
        dateFormat : 'yy-mm-dd',
        changeMonth : true, //显示下拉列表月份
        changeYear : true, //显示下拉列表年份
        firstDay : "1",//设置开始为1号
        onSelect : function(dateText, inst) {
            //设置结束日期的最小日期
            $("#endtime").datepicker("option", "minDate", dateText);
        }
    });

    $("#endtime").datepicker({ //绑定开始日期
        dateFormat : 'yy-mm-dd',
        changeMonth : true, //显示下拉列表月份
        changeYear : true, //显示下拉列表年份
        firstDay : "1",//设置开始为1号
        onSelect : function(dateText, inst) {
            //设置开始日期的最大日期
            $("#starttime").datepicker("option", "maxDate", dateText);
        }
    });
</script>
