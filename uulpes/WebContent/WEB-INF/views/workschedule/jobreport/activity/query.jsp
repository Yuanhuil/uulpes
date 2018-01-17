<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/jobreport/activity" />
<form:form id="queryform" name="form" action="${baseaction}/querydata.do" method="post" modelAttribute="entity">
    <div class="filterContent-dlg">
        <ul>
            <li>
                <label class="name03">活动记录</label>
                <form:input path="startTime" cssClass="input_160 validate[required]"></form:input>
            </li>
            <li>
                <label class="name03">至</label>
                <form:input path="endTime" cssClass="input_160 validate[required]"></form:input>
            </li>
        </ul>
    </div>
    <c:if test="${showchosen eq true }">
        <div class="filterContent-dlg">
            <ul>
                <li>
                    <label class="name03">机构选择</label>
                    <form:radiobutton path="queryOrgtype" value="1" label="区县教委"></form:radiobutton>
                    <form:radiobutton path="queryOrgtype" value="2" label="市直属学校"></form:radiobutton>
                </li>
            </ul>
        </div>
    </c:if>
    <div class="buttonContent">
        <input id="searchform" class="button-mid blue" type="submit" value="汇总统计">
        <input id="clearform" class="button-mid blue" type="button" value="重置">
    </div>
</form:form>
<script type="text/javascript">
    $(function() {
        $("#plancatalog").change(
	        function() {
	            var p1 = $(this).children("option:selected").val();
	            $.ajax({
	                url : '${baseaction}/getType.do',
	                data : {
	                    'catid' : p1
	                },
	                dataType : 'json',
	                success : function(data) {
	                    $("#typecheckboxs").empty().append("<label class='name03'>活动类型</label>");
	                    $.each(data, function(index, item) {
	                        $("#typecheckboxs").append(
	                                "<input type='checkbox' name='plantype' value='" + item.id + "' checked='checked'/>"
	                                        + item.name);
	                    });
	                },
	                error : function() {
	                    console.log("发生错误");
	                }
	
	            });
	        });
        $("#startTime").datepicker({ //绑定开始日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1", //设置开始为1号
            onSelect : function(dateText, inst) {
                //设置结束日期的最小日期
                $("#endTime").datepicker('option', 'minDate', new Date(dateText.replace('-', ',')));

            }
        });

        $("#endTime").datepicker({ //设置结束绑定日期
            dateFormat : 'yy-mm-dd',
            changeMonth : true, //显示下拉列表月份
            changeYear : true, //显示下拉列表年份
            firstDay : "1", //设置开始为1号
            onSelect : function(dateText, inst) {
                //设置开始日期的最大日期
                $("#startTime").datepicker('option', 'maxDate', new Date(dateText.replace('-', ',')));
            }
        });
        $("#queryform").validationEngine();
        $("#queryform").ajaxForm({
            target : "#tablelist"
        });
        $("#clearform").click(function() {
            $("#queryform").clearForm();
        });
    });
</script>