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
        <div class="stepContent-sub">
            <div class="stepContent">
                <c:if test="${statType==3 || statType==0}">
                    <div class="singleForm">
                        <ul>
                            <li><label class="name04">自变量</label>
                                <select id="independentVariable" class="selectNormal">
                                    <c:forEach var="item" items="${columns}">
                                        <option value="${item.key}">${item.value}</option>
                                    </c:forEach>
                                </select>
                             </li>
                        </ul>
                    </div>
                </c:if>
                <c:if test="${statType==5}">
                    <div class="singleForm">
                        <ul>
                            <li><label class="name04">自变量</label>
                                <select id="independentVariable" class="selectNormal">
                                    <c:forEach var="item" items="${columns}">
                                        <c:if test="${item.value!='性别'}">
                                            <option value="${item.key}">${item.value}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </li>
                        </ul>
                    </div>
                </c:if>
                <div class="singleForm">
                    <ul>
                        <li><label class="name04">自变量取值</label>
                            <select id="independentVariableValues" class="selectNormal">
                                <c:forEach var="item" items="${values}">
                                    <option value="${item.key}">${item.value}</option>
                                </c:forEach>
                            </select>
                        </li>
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
        var width = $("#independentVariable").css("width");
        var statType = "${statType}";
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

        if (statType == 3 || statType == 5 || statType==0) {
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
                                    "<option value='" + values[i].id + "'>" + values[i].name + "</option>");
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
            }
            $("#independentVariable").on(
                    "change",
                    function() {
                        $.ajax({
                            url : ctx + "/scaletoollib/statistics/getColumnValues.do",
                            data : {
                                "colName" : $("#independentVariable").val(),
                                "cols" : "['0']",
                                "vals" : "['0']"
                            },
                            type : "POST",
                            success : function(data) {
                                var values = JSON.parse(data);
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
            var vs = $("#independentVariable").multiselect({
                checkAllText : "全选",
                uncheckAllText : '全不选',
                selectedList : 20
            });
            $("#independentVariable").multiselect();
            $(".ui-multiselect").css("width", width);
        }
        //下一步按钮动作
        $("#next").click(function(evt) {
            if ($("#independentVariable").val() == 0) {
                layer.open({
                    content : '请选择自变量',
                    yes : function() {
                        layer.closeAll();
                        $("#independentVariable").flash('255,0,0', 1500, 3);
                    }
                });
                return false;
            }
            if ($("#independentVariableValues").val() == 0) {
                layer.open({
                    content : '请选择自变量取值',
                    yes : function() {
                        layer.closeAll();
                        $("#independentVariableValues").flash('255,0,0', 1500, 3);
                    }
                });
                return false;
            }
            if (!$("#StatParams").validationEngine('validate'))
                return false;
            var data = new Object();
            var statobj = $("[name=statObj]").val();
            if (statobj === 1)
                data.table = "student";
            else
                data.table = "teacher";
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
                data.vals = JSON.stringify($("#independentVariableValues").multiselect("getChecked").map(function() {
                    return this.value;
                }).get());
            }
            $("#StatParams").ajaxSubmit({
                target : "#" + $("#tab-container div .active")[0].id,
                data : data,
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
    });
</script>