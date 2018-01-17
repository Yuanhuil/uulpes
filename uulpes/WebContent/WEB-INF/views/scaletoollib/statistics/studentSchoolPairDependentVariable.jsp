<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>
.name04 {
    width: 80px;
    float: initial;
}

.step {
    width: 25%;
    height: 40px;
    background-repeat: no-repeat;
    float: left;
    line-height: 40px;
    text-align: center;
}

.singleForm {
    width: initial;
    text-align: right;
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
            <div class="stepContent-sub">
                <div class="singleForm">
                    <ul>
                        <li><label class="name04">量表类型</label> <select
                            id="scaleType" class="selectNormal">
                                <option value="0">请选择</option>
                                <c:forEach var="item"
                                    items="${scaleTypeList}">
                                    <option value="${item.id}">${item.name}
                                    </option>
                                </c:forEach>
                        </select></li>
                    </ul>
                </div>
                <div class="singleForm">
                    <ul>
                        <li><label class="name04">量表名称</label> <select
                            id="scaleName" class="selectNormal">
                                <option value="0">请选择</option>
                        </select></li>
                    </ul>
                </div>
                <div class="singleForm">
                    <ul>
                        <li><label class="name04">测评时间</label> <select
                            id="scaleDimension" class="selectNormal">
                                <option value="0">请选择</option>
                        </select></li>
                    </ul>
                </div>
                <div class="singleForm">
                    <ul>
                        <li><label class="name04">对比测评时间</label> <select
                            id="scaleDimension1" class="selectNormal">
                                <option value="0">请选择</option>
                        </select></li>
                    </ul>
                </div>
                <input type="hidden" name="statObj" value="${statObj}" />
            </div>
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
        //量表类型下拉框动作
        $("#scaleType").change(
                function(val) {
                    var url = ctx + "/scaletoollib/statistics/getTaskScales.do";
                    var data = "";
                    var scaleType = $("#scaleType").val();
                    if (scaleType && scaleType != "") {
                        data = {
                            "scaleType" : scaleType
                        };
                    }
                    $.ajax({
                        url : url,
                        data : data,
                        type : "POST",
                        async : true,
                        success : function(d) {
                            if (d != "") {
                                var options = JSON.parse(d);
                                $("#scaleName").empty();
                                $("#scaleName").append("<option value='0' select='selected'>请选择</option>");
                                for (var i = 0; i < options.length; i++) {
                                    $("#scaleName").append(
                                            "<option value='"+options[i].code+"'>" + options[i].name + "</option>");
                                }
                            } else {
                                $("#scaleName").empty();
                                $("#scaleName").append("<option value='0' select='selected'>请选择</option>");
                            }
                        },
                        error : function(jqXHR, textStatus, errorThrown) {
                            $("#scaleName").empty();
                            layer.open({
                                content : '错误: ' + jqXHR.responseText
                            });
                        }
                    });
                });
        //量表下拉框动作
        $("#scaleName").change(
                function(val) {
                    debugger;
                    var url = ctx + "/scaletoollib/statistics/getTaskTimes.do";
                    var data = "";
                    var scaleName = $("#scaleName").val();
                    debugger;
                    if (scaleName && scaleName != "") {
                        data = {
                            "scale" : scaleName
                        };
                    }
                    $.ajax({
                        url : url,
                        data : data,
                        type : "POST",
                        async : true,
                        success : function(d) {
                            if (d != "") {
                                var options = JSON.parse(d);
                                $("#scaleDimension").empty();
                                $("#scaleDimension").append("<option value='0' select='selected'>请选择</option>");
                                for (var i = 0; i < options.length; i++) {
                                    $("#scaleDimension").append(
                                            "<option value='"+options[i].taskid+"'>" + options[i].createtime
                                                    + "</option>");
                                }
                                $("#scaleDimension1").empty();
                                $("#scaleDimension1").append("<option value='0' select='selected'>请选择</option>");
                                for (var i = 0; i < options.length; i++) {
                                    $("#scaleDimension1").append(
                                            "<option value='"+options[i].taskid+"'>" + options[i].createtime
                                                    + "</option>");
                                }
                            } else {
                                $("#scaleDimension").empty();
                                $("#scaleDimension").append("<option value='0' select='selected'>请选择</option>");
                                $("#scaleDimension1").empty();
                                $("#scaleDimension1").append("<option value='0' select='selected'>请选择</option>");
                            }
                        },
                        error : function(jqXHR, textStatus, errorThrown) {
                            $("#scaleDimension").empty();
                            layer.open({
                                content : '错误: ' + jqXHR.responseText
                            });
                        }
                    });
                });
        //下一步按钮动作
        $("#next").click(function(evt) {
            if ($("#dimpair").val() == 0) {
                layer.open({
                    content : '请选择对比量表维度'
                });
                $("#dimpair").flash('255,0,0', 1500, 3);
                return false;
            }

            if (!$("#StatParams").validationEngine('validate'))
                return false;
            debugger;
            var scale_code = $("#scaleName").val();
            var scale_code1 = scale_code;
            var taskid = $("#scaleDimension").val();
            var taskid1 = $("#scaleDimension1").val();

            $("#StatParams").ajaxSubmit({
                target : "#" + $("#tab-container div .active")[0].id,
                data : {
                    "scale" : scale_code,
                    "task" : taskid,
                    "scale1" : scale_code1,
                    "task1" : taskid1,
                    "step" : "${step+1}",
                    "stepshow" : "${stepshow}"
                },
                success : function() {
                },
                error : function(jqXHR, textStatus, errorThrown) {
                    layer.open({
                        content : '错误: ' + jqXHR.responseText
                    });
                }
            });
        });
    });
</script>