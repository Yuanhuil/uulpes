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
                                <c:forEach var="scale"
                                    items="${scaleList}">
                                    <option value="${scale.code}">${scale.title}
                                    </option>
                                </c:forEach>
                        </select></li>
                    </ul>
                </div>
                <c:choose>
                    <c:when test="${statType==3}">
                        <div class="singleForm">
                            <ul>
                                <li><label class="name04">量表维度</label>
                                    <select id="scaleDimension"
                                    class="selectNormal">
                                        <option value="0">请选择</option>
                                </select></li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="singleForm">
                            <ul>
                                <li><label class="name04">量表维度</label>
                                    <select id="scaleDimension"
                                    class="selectNormal">
                                </select></li>
                            </ul>
                        </div>
                    </c:otherwise>
                </c:choose>
                <c:if test="${statType==2}">
                    <div class="singleForm">
                        <ul>
                            <li><label class="name04">常模</label> <select
                                id="scaleNorm" class="selectNormal">
                                    <option value="0">请选择</option>
                            </select></li>
                        </ul>
                    </div>
                </c:if>
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
        var width = $("#scaleDimension").css("width");
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
                    var url = ctx + "/scaletoollib/statistics/getScales.do";
                    var data = "";
                    var scaleType = $("#scaleType").val();
                    var scale = $("#scaleName").val();
                    if (scaleType == "0") {
                        $("#scaleName").empty();
                        $("#scaleDimension").empty();
                        if ($("#scaleNorm"))
                            $("#scaleNorm").empty();
                    } else {
                        var dims = "";
                        if ($("#scaleDimension")) {
                            dims = JSON.stringify($("#scaleDimension").multiselect("getChecked").map(function() {
                                return this.value;
                            }).get());
                        }
                        var norm = "";
                        if (statType === '2')
                            norm = $("#scaleNorm").val();
                        if (scaleType && scaleType != "") {
                            data = {
                                "scaleType" : scaleType,
                                "scale" : scale,
                                "dims" : dims,
                                "norm" : norm
                            };
                        }
                        $
                                .ajax({
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
                                                        "<option value='"+options[i].code+"'>" + options[i].name
                                                                + "</option>");
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
                    }
                });

        var statType = "${statType}";

        var vs = $("#scaleDimension").multiselect({
            checkAllText : "全选",
            uncheckAllText : '全不选',
            selectedList : 20
        });
        $(".ui-multiselect").css("width", width);

        //$("#scaleDimension").multiselect();

        //量表下拉框动作
        $("#scaleName").change(
                function(val) {
                    var url = ctx + "/scaletoollib/statistics/getDimensions.do";
                    var data = "";
                    var scaleName = $("#scaleName").val();
                    var dims = "";
                    if ($("#scaleDimension")) {
                        dims = JSON.stringify($("#scaleDimension").multiselect("getChecked").map(function() {
                            return this.value;
                        }).get());
                    }
                    var norm = "";
                    if (statType === '2')
                        norm = $("#scaleNorm").val();
                    debugger;
                    if (scaleName && scaleName != "" && scaleName != "0") {
                        data = {
                            "scale" : scaleName,
                            "dims" : dims,
                            "norm" : norm
                        };
                        $.ajax({
                            url : url,
                            data : data,
                            type : "POST",
                            async : true,
                            success : function(d) {
                                if (d != "") {
                                    var options = JSON.parse(d);
                                    $("#scaleDimension").empty();
                                    for (var i = 0; i < options.length; i++) {
                                        $("#scaleDimension").append(
                                                "<option value='"+options[i].id+"'>" + options[i].name + "</option>");
                                    }
                                    vs.multiselect('refresh');
                                    $(".ui-multiselect").css("width", width);
                                } else {
                                    $("#scaleDimension").empty();
                                }
                            },
                            error : function(jqXHR, textStatus, errorThrown) {
                                $("#scaleDimension").empty();
                                layer.open({
                                    content : '错误: ' + jqXHR.responseText
                                });
                            }
                        });
                    } else {
                        $("#scaleDimension").empty();
                        vs.multiselect('refresh');
                        $(".ui-multiselect").css("width", width);
                    }
                });

        if ($("#scaleName").val() != undefined && $("#scaleName").val() != "0") {
            $.ajax({
                url : ctx + "/scaletoollib/statistics/getDimensions.do",
                data : {
                    "scale" : $("#scaleName").val()
                },
                type : "POST",
                success : function(data) {
                    var values = JSON.parse(data);
                    debugger;
                    $("#scaleDimension").empty();
                    for (var i = 0; i < values.length; i++) {
                        $("#scaleDimension").append(
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
        }

        //}

        //量表下拉框动作改变常模
        $("#scaleName").change(
                function(val) {
                    var url = ctx + "/scaletoollib/statistics/getNorms.do";
                    if ($("#scaleNorm")) {
                        var data = "";
                        var scale = $("#scaleName").val();
                        var dims = "";
                        if ($("#scaleDimension")) {
                            dims = JSON.stringify($("#scaleDimension").multiselect("getChecked").map(function() {
                                return this.value;
                            }).get());
                        }
                        var norm = "";
                        if (statType === '2')
                            norm = $("#scaleNorm").val();
                        debugger;
                        if (scale && scale != 0) {
                            data = {
                                "scale" : scale,
                                "dims" : dims,
                                "norm" : norm
                            };
                            $.ajax({
                                url : url,
                                data : data,
                                type : "POST",
                                async : true,
                                success : function(d) {
                                    if (d != "") {
                                        var options = JSON.parse(d);
                                        $("#scaleNorm").empty();
                                        $("#scaleNorm").append("<option value='0' select='selected'>请选择</option>");
                                        for (var i = 0; i < options.length; i++) {
                                            $("#scaleNorm").append(
                                                    "<option value='"+options[i].id+"'>" + options[i].name
                                                            + "</option>");
                                        }
                                    } else {
                                        $("#scaleNorm").empty();
                                        $("#scaleNorm").append("<option value='0' select='selected'>请选择</option>");
                                    }
                                },
                                error : function(jqXHR, textStatus, errorThrown) {
                                    $("#scaleNorm").empty();
                                    layer.open({
                                        content : '错误: ' + jqXHR.responseText
                                    });
                                }
                            });
                        } else {
                            $("#scaleNorm").empty();
                        }
                    }
                });

        //下一步按钮动作
        $("#next").click(function(evt) {
            //检查量表是否已选择
            if ($("#scaleName").val() == 0) {
                layer.open({
                    content : '请选择量表名称',
                    yes : function() {
                        layer.closeAll();
                        $("#scaleName").flash('255,0,0', 1500, 3);
                    }
                });
                return false;
            }
            if ($("#scaleDimension").val() == 0) {
                layer.open({
                    content : '请选择维度名称',
                    yes : function() {
                        layer.closeAll();
                        $("#scaleDimension").flash('255,0,0', 1500, 3);
                    }
                });
                return false;
            }
            if (statType === '2') {
                if ($("#scaleNorm").val() == 0) {
                    layer.open({
                        content : '请选择常模',
                        yes : function() {
                            layer.closeAll();
                            $("#scaleNorm").flash('255,0,0', 1500, 3);
                        }
                    });
                    return false;
                }
            }
            if (!$("#StatParams").validationEngine('validate'))
                return false;
            var data = new Object();
            data.step = "${step+1}";
            data.stepshow = "${stepshow}";
            data.scale = $("#scaleName").val();
            if ($("#scaleDimension")) {
                data.dims = JSON.stringify($("#scaleDimension").multiselect("getChecked").map(function() {
                    return this.value;
                }).get());
            }
            if (statType === '2')
                data.norm = $("#scaleNorm").val();
            //debugger;
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