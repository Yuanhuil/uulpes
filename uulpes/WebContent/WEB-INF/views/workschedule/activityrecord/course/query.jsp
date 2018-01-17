<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/activityrecord/course" />
<form:form id="queryform" name="form" action="${baseaction}/querydata.do" method="post" modelAttribute="entity">
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">类型</label>
                <form:select id="queryactivitytype" path="activitytype" cssClass="select_160">
                    <form:option value="">请选择</form:option>
                    <form:options items="${plantype}" itemLabel="name" itemValue="id" />
                </form:select>
            </li>
            <li class="filterContent-course">
                <label>主讲人</label>
                <form:input path="speaker" cssClass="input_160"></form:input>
            </li>
            <li>
                <label class="name03">对象</label>
                <form:input path="audience" cssClass="input_160"></form:input>
            </li>
        </ul>
    </div>
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">标题</label>
                <form:input id="querytitle" path="title" cssClass="input_160"></form:input>
            </li>
            <li>
                <label class="name03">级别</label>
                <form:select path="level" cssClass="select_160">
                    <form:option value="">请选择</form:option>
                    <form:options items="${levellist}" itemLabel="name" itemValue="id" />
                </form:select>
            </li>
        </ul>
    </div>
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">时间</label>
                <form:input id="querystarttime" path="startactivitytime" cssClass="input_160"></form:input>
            </li>
            <li>
                <label class="name03">至</label>
                <form:input id="queryendtime" path="endactivitytime" cssClass="input_160"></form:input>
            </li>
        </ul>
    </div>
    <div class="buttonContent">
        <div class="buttonLeft">
            <ul>
                <li>
                    <input class="button-mid white" type="button" value="新增" chref="${baseaction}/add.do" id="add">
                </li>
                <li>
                    <input class="button-mid white" type="button" value="删除" id="del" chref="">
                </li>
                <li>
                    <input class="button-mid white" type="button" value="导入" id="import_btn"
                        chref="${baseaction}/importform.do" />
                </li>
            </ul>
        </div>
        <div class="buttonRight">
            <ul>
                <li>
                    <input id="searchform" class="button-mid blue" type="submit" value="搜索">
                </li>
                <li>
                    <input id="clearform" class="button-mid blue" type="button" value="重置">
                </li>
            </ul>
        </div>
    </div>
</form:form>
<script type="text/javascript">
    $(function() {
        $("#import_btn").on("click", function() {
            var h = $(this).attr("chref");
            $("#importdiv").html();
            $("#importdiv").load(h, function() {
                $("#importdialog").dialog("open");
            });

        });
        $("#del").click(function() {
            var selectedData = [];
            var selectRow = $("input[name='rowcheck']:checked");
            if (selectRow.length === 0) {
                layer.open({
                    content : "没有选择相关内容"
                });
                return false;
            }
            layer.confirm('确定要删除所选择记录内容吗?', {
                btn : [ '是', '否' ]
            }, function(index) {
                layer.close(index);
                selectRow.each(function() {
                    selectedData.push(this.value);
                });
                $('#content2').load("${baseaction }/delselected.do", {
                    rowcheck : selectedData
                });
            });
        });

        $("#queryform").ajaxForm({
            target : "#tablelist"
        });
        $("#clearform").click(function() {
            $("#queryform").clearForm();
        });
        if ($("#add") != null) {
            $("#add").on("click", function() {
                var h = $(this).attr("chref");
                $("#editformdiv").html();
                $("#editformdiv").load(h, function() {
                    $(".initialization").val("<b>内容简介</b>：</br></br><b>基本过程</b>：</br></br><b>特别记录</b>：</br>");
                    $("#editdialog").dialog("open");
                });
            });
        }
        $("#querystarttime").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1", //设置开始为1号
            onSelect : function(dateText, inst) {
                //设置结束日期的最小日期
                $("#queryendtime").datepicker('option', 'minDate', new Date(dateText.replace('-', ',')));

            }
        });

        $("#queryendtime").datepicker({ //设置结束绑定日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1", //设置开始为1号
            onSelect : function(dateText, inst) {
                //设置开始日期的最大日期
                $("#querystarttime").datepicker('option', 'maxDate', new Date(dateText.replace('-', ',')));
            }
        });
    });
</script>