<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>
.step {
    width: 25%;
    height: 40px;
    background-repeat: no-repeat;
    float: left;
    line-height: 40px;
    text-align: center;
}

.name04 {
    float: initial;
}
</style>
<c:set var="statformurl"
    value="../../scaletoollib/statistics/${statType}/nextStep.do"></c:set>
<form id="StatParams" name="StatParams" method="post"
    action="${statformurl}">
    <div>
        <!--搜索条件区start（每行三列搜索条件，可以复制）-->

        <div class="stepControl">
            <c:if test="${sessionScope.stepcnt>=1}">
                <c:choose>
                    <c:when test="${stepshow==0}">
                        <div class="step" cur="0">第一步</div>
                    </c:when>
                    <c:otherwise>
                        <div class="step">第一步</div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${sessionScope.stepcnt>=2}">
                <c:choose>
                    <c:when test="${stepshow==1}">
                        <div class="step" cur="1">第二步</div>
                    </c:when>
                    <c:otherwise>
                        <div class="step">第二步</div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${sessionScope.stepcnt>=3}">
                <c:choose>
                    <c:when test="${stepshow==2}">
                        <div class="step" cur="2">第三步</div>
                    </c:when>
                    <c:otherwise>
                        <div class="step">第三步</div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${sessionScope.stepcnt>=4}">
                <c:choose>
                    <c:when test="${stepshow==3}">
                        <div class="step" cur="3">第四步</div>
                    </c:when>
                    <c:otherwise>
                        <div class="step">第四步</div>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
        <div class="stepContent">
            <div class="singleForm">
                <ul>
                    <li><label class="name04">学校名称</label> <select
                        name="select" id="school"
                        class="selectNormal validate[required]">
                            <option value="0">请选择</option>
                            <c:forEach var="item" items="${schoolList}">
                                <option value="${item.id}">
                                    ${item.xxmc}</option>
                            </c:forEach>
                    </select></li>
                </ul>
            </div>
            <c:if test="${statObj==1}">
                <div class="singleForm">
                    <ul>
                        <li><label class="name04">年级名称</label> <select
                            name="select" id="grade"
                            class="selectNormal">
                        </select></li>
                    </ul>
                </div>
                <div class="singleForm">
                    <ul>
                        <li><label class="name04">班级名称</label> <select
                            name="select" id="class"
                            class="selectNormal">
                        </select></li>
                    </ul>
                </div>
            </c:if>
            <div class="singleForm">
                <ul>
                    <li><label class="name04">开始时间</label> <input
                        class="statistics-input" type="text"
                        id="starttime"></input></li>
                </ul>
            </div>
            <div class="singleForm">
                <ul>
                    <li><label class="name04">结束时间</label> <input
                        class="statistics-input" type="text"
                        id="endtime"></input></li>
                </ul>
            </div>
            <input type="hidden" name="statObj" value="${statObj}" />
        </div>
        <!--翻页导航start-->
        <div class="pageNav">
            <input type="button" class="green" id="next" value="下一步">
        </div>
        <!--翻页导航end-->
    </div>
</form>
<script type="text/javascript">
    $(function() {
        $("#starttime").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
        $("#endtime").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1" //设置开始为1号
        });
        debugger;
        //高亮步骤
        $(".step").each(
                function(index) {
                    $(this).css("background-image",
                            "url(../../themes/" + theme + "/images/step" + (index + 1) + "_normal.png)");
                });
        $(".step[cur]").each(
                function(index) {
                    $(this).css(
                            "background-image",
                            "url(../../themes/" + theme + "/images/step" + (Number($(this).attr("cur")) + 1)
                                    + "_hover.png)");
                });
        var statObj = $("[name=statObj]").val();
        <c:if test="${statObj==1}">
        //学校下拉框动作
        $("#school").change(function(val) {

            var url = ctx + "/scaletoollib/statistics/getGrades.do";
            $.ajax({
                url : url,
                data : {
                    "school" : "['" + $("#school").val() + "']",
                    "grade" : $("#grade").val(),
                    "class" : $("#class").val(),
                    "starttime" : $("#starttime").val(),
                    "endtime" : $("#endtime").val()
                },
                type : "POST",
                async : true,
                success : function(d) {
                    if (d != "") {
                        var options = JSON.parse(d);
                        $("#grade").empty();
                        $("#grade").append("<option value='0' select='selected'>请选择</option>");
                        for (var i = 0; i < options.length; i++) {
                            $("#grade").append("<option value='"+options[i].id+"'>" + options[i].njmc + "</option>");
                        }
                    } else {
                        $("#grade").empty();
                        $("#grade").append("<option value='0' select='selected'>请选择</option>");
                    }
                },
                error : function(jqXHR, textStatus, errorThrown) {
                    $("#grade").empty();
                    layer.open({
                        content : '错误: ' + jqXHR.responseText
                    });
                }
            });
        });

        //年级按钮动作
        $("#grade").change(function(val) {
            var url = ctx + "/scaletoollib/statistics/getClasses.do";
            $.ajax({
                url : url,
                data : {
                    "school" : "['" + $("#school").val() + "']",
                    "grade" : $("#grade").val(),
                    "class" : $("#class").val(),
                    "starttime" : $("#starttime").val(),
                    "endtime" : $("#endtime").val()
                },
                type : "POST",
                async : true,
                success : function(d) {
                    if (d != "") {
                        var options = JSON.parse(d);
                        $("#class").empty();
                        $("#class").append("<option value='0' select='selected'>请选择</option>");
                        for (var i = 0; i < options.length; i++) {
                            $("#class").append("<option value='"+options[i].id+"'>" + options[i].bjmc + "</option>");
                        }
                    } else {
                        $("#class").empty();
                        $("#class").append("<option value='0' select='selected'>请选择</option>");
                    }
                },
                error : function(jqXHR, textStatus, errorThrown) {
                    $("#class").empty();
                    layer.open({
                        content : '错误: ' + jqXHR.responseText
                    });
                }
            });
        });
        </c:if>

        //下一步按钮动作
        $("#next").click(function(evt) {
        	var starttimeTeam = document.getElementById("starttime").value;
            var endtimeTeam = document.getElementById("endtime").value;
            if (starttimeTeam == '' || starttimeTeam == null) {
                layer.open({
                    content : '请输入开始时间!'
                });
                return;
            }
            if (endtimeTeam == '' || endtimeTeam == null) {
                layer.open({
                    content : '请输入终止时间!'
                });
                return;
            }
            if (!$("#StatParams").validationEngine('validate'))
                return false;
            debugger;
            $("#StatParams").ajaxSubmit({
                target : "#" + $("#tab-container div .active")[0].id,
                data : {
                    "school" : "['" + $("#school").val() + "']",
                    "grade" : $("#grade").val(),
                    "class" : $("#class").val(),
                    "starttime" : $("#starttime").val(),
                    "endtime" : $("#endtime").val(),
                    "step" : "${step+1}",
                    "stepshow" : "${stepshow}"
                },
                success : function() {
                    //layer.open({content:"保存成功!"});
                },
                error : function(jqXHR, textStatus, errorThrown) {
                    layer.open({
                        content : '错误: ' + jqXHR.responseText
                    });
                }
            });
        });
        if (statObj) {
            $("#school option:eq(1)").attr('selected', 'selected');
            $("#school").attr('disabled', true);
            $("#school").trigger("change");
        }

    });
</script>