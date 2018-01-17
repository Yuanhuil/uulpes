<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link rel="stylesheet" type="text/css"
    href="../../themes/theme1/css/jquery.multiselect.css" />
<script type="text/javascript"
    src="../../js/jqueryplugin/multiselect/jquery.multiselect.min.js"></script>
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
                    <li><label class="name04">自变量</label> <select
                        id="independentVariable" class="selectNormal">
                            <c:forEach var="item" items="${columns}">
                                <option value="${item.key}">${item.value}
                                </option>
                            </c:forEach>
                    </select></li>
                </ul>
            </div>
            <c:if test="${statType==3 || statType==5}">
                <div class="singleForm">
                    <ul>
                        <li><label class="name04">自变量取值</label> <select
                            id="independentVariableValues"
                            class="selectNormal">
                                <c:forEach var="item" items="${values}">
                                    <option value="${item.key}">${item.value}
                                    </option>
                                </c:forEach>
                        </select></li>
                    </ul>
                </div>
            </c:if>
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
        var statType = "${statType}";
        var width = $("#independentVariable").css("width");
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
        if (statType == 3 || statType == 5) {
            var vs = $("#independentVariableValues").multiselect({
                checkAllText : "全选",
                uncheckAllText : '全不选',
                selectedList : 20
            });
            $(".ui-multiselect").css("width", width);
            if ($("#independentVariable").val() != undefined) {
                $.ajax({
                    url : ctx + "/scaletoollib/statistics/getColumnValues.do",
                    data : {
                        "colName" : $("#independentVariable").val()
                    },
                    type : "POST",
                    success : function(data) {
                        var values = JSON.parse(data);
                        debugger;
                        $("#independentVariableValues").empty();
                        for (var i = 0; i < values.length; i++) {
                            $("#independentVariableValues").append(
                                    "<option value='" + values[i].id + "'>" + values[i].name + "</option>")
                        }
                        vs.multiselect('refresh');
                        $(".ui-multiselect").css("width", width);
                    }
                });
            }
            $("#independentVariable").on(
                    "change",
                    function() {
                        debugger;
                        $.ajax({
                            url : ctx + "/scaletoollib/statistics/getColumnValues.do",
                            data : {
                                "colName" : $("#independentVariable").val()
                            },
                            type : "POST",
                            success : function(data) {
                                var values = JSON.parse(data);
                                debugger;
                                $("#independentVariableValues").empty();
                                for (var i = 0; i < values.length; i++) {
                                    $("#independentVariableValues").append(
                                            "<option value='" + values[i].id + "'>" + values[i].name + "</option>")
                                }
                                vs.multiselect('refresh');
                                $(".ui-multiselect").css("width", width);
                            },
                            error : function(jqXHR, textStatus, errorThrown) {
                                layer.open({
                                    content : '错误: ' + jqXHR.responseText
                                });
                            }
                        });
                    });
        } else {
            $("#independentVariable").multiselect({
                checkAllText : "全选",
                uncheckAllText : '全不选',
                selectedList : 20
            });
            $(".ui-multiselect").css("width", width);
        }
        //下一步按钮动作
        $("#next").click(function(evt) {
            debugger;
            if (!$("#StatParams").validationEngine('validate'))
                return false;
            var data = new Object();
            data.table = "student";
            data.step = "${step+1}";
            data.stepshow = "${stepshow}";
            //如果取值下拉框不存在，则自变量下拉框是多选
            if ($("#independentVariableValues").length === 0) {
                data.cols = JSON.stringify($("#independentVariable").multiselect("getChecked").map(function() {
                    return this.value;
                }).get());
            } else {
                if ($("#independentVariable")) {
                    data.cols = JSON.stringify([ $("#independentVariable").val().trim() ]);
                }
            }

            if ($("#independentVariableValues")) {
                //debugger;
                var vals = $("#independentVariableValues").multiselect("getChecked").map(function() {
                    return this.value;
                }).get();
                if (vals && vals.length < 3) {
                    layer.open({
                        content : "请选择至少三个自变量取值",
                        yes : function() {
                            layer.closeAll();
                            $("html,body").animate({
                                scrollTop : $(".ui-multiselect").offset().top
                            }, 1000);
                            $(".ui-multiselect").flash('255,0,0', 1000, 2);
                        }
                    });
                    return false;
                } else {
                    data.vals = JSON.stringify(vals);
                }
            }
            $("#StatParams").ajaxSubmit({
                target : "#" + $("#tab-container div .active")[0].id,
                data : data,
                success : function(d) {
                    try {
                        JSON.parse(d);
                    } catch (e) {
                        throw d;
                    }
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