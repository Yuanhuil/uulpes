<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/congress" />
<form:form id="queryform" name="form" action="${baseaction}/querydata.do" method="post" modelAttribute="entity">
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">会议主题</label>
                <form:input path="subject" cssClass="input_160"></form:input>
            </li>
            <li>
                <label class="name03">主持人</label>
                <form:input path="emcee" cssClass="input_160"></form:input>
            </li>
            <li>
                <label class="name03">记录人</label>
                <form:input path="recordperson" cssClass="input_160"></form:input>
            </li>
        </ul>
    </div>
    <div class="filterContent">
        <ul>
            <li>
                <label class="name03">日期</label>
                <form:input path="starttime" cssClass="input_160"></form:input>
            </li>
            <li>
                <label class="name03">地点</label>
                <form:input path="place" cssClass="input_160"></form:input>
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
                    <input class="button-mid white" type="button" value="导入" id="import_btn"
                        chref="${baseaction}/importform.do" />
                </li>
                <li>
                    <input class="button-mid white" type="button" value="删除" id="del" chref="">
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
                    $(".initialization").val("<b>内容简介</b>：</br></br><b>特别记录</b>：");
                    $("#editdialog").dialog("open");
                });
            });
        }
        $("#starttime").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1", //设置开始为1号
        });

    });
</script>