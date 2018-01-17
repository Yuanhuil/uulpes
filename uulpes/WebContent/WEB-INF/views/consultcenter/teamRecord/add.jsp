<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="编辑">
    <form:form id="editForm" method="post" commandName="record" action="/pes/consultcenter/teamRecord/save.do">
        <form:hidden path="id" />
        <form:hidden path="teamid" />
        <div class="filterContent">
            <ul>
                <li>
                    <label class="name04">咨询类型</label>
                    <form:select path="consultationtypeid" class="input_160">
                        <form:options items="${consultTypes }" itemValue="id" itemLabel="name"></form:options>
                    </form:select>
                </li>
                <li>
                    <label class="name04">咨询员</label>
                    <form:select path="teacherid" value="1" class="input_160" items="${teachers}" itemLabel="xm"
                        itemValue="id">
                    </form:select>
                </li>
            </ul>
        </div>
        <div class="filterContent">
            <ul>
                <li>
                    <form:label path="begintime" class="name04">开始时间</form:label>
                    <form:input path="begintime" class="input_160 date" />
                </li>
                <li>
                    <form:label path="endtime" class="name04">结束时间</form:label>
                    <form:input path="endtime" class="input_160 date" />
                </li>
            </ul>
        </div>
        <form:textarea path="reason" id="content" cols="80" rows="10"></form:textarea>
        <%-- <div class="filterContent">
			<ul>
				<li><form:label path="reason"
						class="name03">辅导缘由</form:label> <form:textarea
						path="reason" />
				</li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><form:label path="process"
						class="name03">辅导过程</form:label> <form:textarea
						path="process"  />
				</li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><form:label path="inspiration"
						class="name03">辅导感悟</form:label> <form:textarea
						path="inspiration"  />
				</li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><form:label path="result"
						class="name03">辅导效果</form:label> <form:textarea
						path="result" />
				</li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><form:label path="remark"
						class="name03">备注</form:label> <form:textarea
						path="remark"  />
				</li>
			</ul>
		</div> --%>
    </form:form>
</div>
<script type="text/javascript">
    $("#dialog-form1").dialog({
        appendTo : "#editDiv",
        autoOpen : false,
        modal : false,
        height : 600,
        width : 800,
        buttons : {
            "保存" : function() {
                $("#dialog-form1").dialog("close");
                $("#editForm").ajaxSubmit({
                    target : "#list",
                    data : {
                        'consultationtypeid1' : jQuery('#consultationtypeid').val(),
                        'consultationmodeid1' : jQuery('#consultationmodeid').val(),
                        'teacherid1' : jQuery('#teacherid').val(),

                        'beginDate' : jQuery('#beginDate').val(),
                        'endDate' : jQuery('#endDate').val(),
                    },
                    success : function() {
                        $("#dialog-form1").dialog("close");
                        layer.open({
                            content : "保存成功!"
                        });
                    },
                    error : function() {
                        layer.open({
                            content : "保存失败"
                        });
                    }
                });
                return false;
            },
            "取消" : function() {
                $("#dialog-form1").dialog("close");
            }
        },
    });

    $("#editForm").ajaxForm({
        target : "#list"
    });

    $("#begintime").datepicker({ //绑定开始日期
        dateFormat : 'yy-mm-dd',
        changeMonth : true, //显示下拉列表月份
        changeYear : true, //显示下拉列表年份
        showWeek : true, //显示星期
        showTime : true,
        firstDay : "1", //设置开始为1号
        onSelect : function(dateText, inst) {
            //设置结束日期的最小日期
            $("#endtime").datepicker('option', 'minDate', new Date(dateText.replace('-', ',')));

        }
    });

    $("#endtime").datepicker({ //设置结束绑定日期
        dateFormat : 'yy-mm-dd',
        changeMonth : true, //显示下拉列表月份
        changeYear : true, //显示下拉列表年份 showTime: true,
        showWeek : true, //显示星期
        showTime : true,
        firstDay : "1", //设置开始为1号
        onSelect : function(dateText, inst) {
            //设置开始日期的最大日期
            $('#begintime').datepicker('option', 'maxDate', new Date(dateText.replace('-', ',')));
        }
    });
    $("textarea#content").ckeditor({height: '240px'});
</script>