<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<script src="${ctx}/js/jquerydispense.js"></script>
<style type="text/css">
.list_table_caption {
    font-family: "瀹嬩綋";
    font-size: 12px;
    color: #000000;
    border-top-width: 1px;
    border-right-width: 1px;
    border-left-width: 1px;
    border-bottom-style: none;
    border-top-color: #666666;
    border-right-color: #666666;
    border-left-color: #666666;
    padding-top: 5px;
    padding-bottom: 6px;
    padding-left: 5px;
    color: #757575;
}

.left_title_3 {
    background-color: #9FC8BF;
    color: #3C4D4A;
    font-weight: bold;
    line-height: 20px;
    text-align: center;
}

.class_sub {
    background-color: #FFF;
    border: 1px solid #52B1CD;
    height: 20px;
    width: 100px;
    font-size: 11px;
}

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
<form id="dispenseform" action="${baseaction}/singledistribute.do" method="post">
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
    <table id="colony" class="table-object">
        <tr>
            <td width="60px" calss="distribute-object">分发对象</td>
            <td>
                <label>
                    <select class="select_160" id="objectType" name="objectType" onChange="clickRoleUnit()">
                        <option value="">请选择</option>
                        <option value="1">学生</option>
                        <option value="2">教师</option>
                    </select>
                </label>
                <li class='show-teacher' style="display: inline-block;" id='showTeacher'>
                    教师角色
                    <label>
                        <select class="teacher-role" name="teacherRole" id="teacherRole" onchange="changeTeacher()">
                            <option value="">请选择</option>
                        </select>
                    </label>
                </li>
            </td>
        </tr>
        <tr id="partgrade" class="tr-hide">
            <td width="60px" calss="distribute-object">分发群体</td>
            <td>
                <select class="scaletime-class" name="gradeOrderId" class="class_sub" id="gradeOrderId"
                    onchange="changeGrade()">
                    <option value="">请选择年级</option>
                    <c:forEach var="item" items="${gradeList}">
                        <option value="${item.key}">${item.value}</option>
                    </c:forEach>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<select class="scaletime-class" name="classId" id="classId"
                    onchange="changeClass()">
                    <option value="">请选择班级</option>
                </select>
            </td>
        </tr>
        <tr id="student" class="tr-hide">
            <td width="60px" class="start-time">
                全选&nbsp;&nbsp;
                <input class="checkbox" type="checkbox" name="checkbox" id="checkid">
            </td>
            <td width="720px">
                <table width="100%" border="0" align="center" frame=void cellpadding="0" cellspacing="1"
                    id="studentlist" style="border-collapse: collapse;">
                </table>
            </td>
        </tr>
    </table>
    <div class="step-one">
        <input type="button" class="green" value="下一步" onClick="unitNext()">
    </div>
    <table class="table_style table-scale" id="scale">
        <tr>
            <td>
                <table width="98%" border="0" align="center" id="scalefilter">
                    <tr>
                        <td width="18%" class="start-time">量表来源</td>
                        <td width="25%">
                            <select class="select_160" id="scalesource">
                            </select>
                        </td>
                        <td width="18%" class="start-time">量表类别</td>
                        <td width="25%">
                            <select class="select_160" id="scaletype">
                            </select>
                        </td>
                        <td width="14%" rowspan="2" class="start-time">
                            <input class="btn06" type="button" value="查找量表" onclick="searchScale()"
                                style="height: 30px;"></input>
                        </td>
                    </tr>
                    <tr>
                        <td width="15%" class="start-time">是否预警</td>
                        <td width="20%">
                            <select class="select_160" id="iswarning">
                                <option value="-1">请选择</option>
                                <option value="true">预警</option>
                                <option value="false">不预警</option>
                            </select>
                        </td>
                        <td class="start-time">量表名称</td>
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
                        <tbody></tbody>
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
            target : "#personaldispense",
            success : function() {
                layer.alert("分发完成！");
            },
            error : function(data) {
                layer.alert(data.responseText);
            }
        });
        $("#checkid").click(function() {
            $('input[type=checkbox]').prop('checked', $(this).prop('checked'));
        });
    });
    function startdispense() {
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
        firstDay : "1", //设置开始为1号
        onSelect : function(dateText, inst) {
            //设置结束日期的最小日期
            $("#endtime").datepicker("option", "minDate", dateText);
        }
    });

    $("#endtime").datepicker({ //绑定开始日期
        dateFormat : 'yy-mm-dd',
        changeMonth : true, //显示下拉列表月份
        changeYear : true, //显示下拉列表年份
        firstDay : "1", //设置开始为1号
        onSelect : function(dateText, inst) {
            //设置开始日期的最大日期
            $("#starttime").datepicker("option", "maxDate", dateText);
        }
    });
</script>
