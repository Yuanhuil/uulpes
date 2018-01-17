<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/consultcenter/email/list.do" commandName="email"
	method="post">
	<form:hidden path="fromid" />
	<form:hidden path="toid" />
	<form:hidden path="emailstatus" id="emailstatusQuery" />
	<input hidden id="userid" value="${userid }" style="display:none" />
	<div class="filterContent">
		<ul>
			<li><label class="name03">标题</label> <input type="test"
				name="title" id="title1" class="input_160"></li>
			<li><label class="name03">操作时间</label> <input type="text"
				name="beginDate" id="beginDate" class="input_160"></li>
			<li><label class="name03">至 </label> <input type="text"
				name="endDate" id="endDate" class="input_160"></li>
		</ul>
	</div>
	<div class="buttonContent">
		<input type="submit" id="query" name="query" value="查询"
				class="button-mid blue" /> <input type="button" name="clear"
				id="clear" value="重置" class="button-mid blue" /> 
				<shiro:hasPermission name="consultcenter:email:create"><input
				type="button" id="add" value="添加" class="button-mid blue" /></shiro:hasPermission>
	</div>
</form:form>
<script type="text/javascript">
	$("#queryform").ajaxForm({
		target : "#list"
	});
	$("#clear").click(function() {
		$('#queryform').clearForm();
	});
	$("#sjx").click(function() {
		var id = $('#userid').attr("value");
		$('#toid').attr("value", id);
		$('#fromid').attr("value", null);
		$('#emailstatusQuery').attr("value", 1);
		$('#query').click();

	});
	$("#yfs").click(function() {
		var id = $('#userid').attr("value");
		$('#fromid').attr("value", id);
		$('#toid').attr("value", null);
		$('#emailstatusQuery').attr("value", 1);
		$('#query').click();

	});
	$("#cgx").click(function() {
		var id = $('#userid').attr("value");
		$('#fromid').attr("value", id);
		$('#toid').attr("value", null);
		$('#emailstatusQuery').attr("value", 0);
		$('#query').click();
	});

	$("#add").on("click", function() {
		$("#dialog-form1").dialog("open");
		var h = '/pes/consultcenter/email/addOrUpdate.do';
		$('#editDiv').html();
		$('#editDiv').load(h, function() {
			$("#dialog-form1").dialog("open");
		});
	});

	$("#beginDate").datepicker(
			{ //绑定开始日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				showWeek : true, //显示星期	
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置结束日期的最小日期
					$("endDate").datepicker('option', 'minDate',
							new Date(dateText.replace('-', ',')));

				}
			});

	$("#endDate").datepicker(
			{ //设置结束绑定日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				showWeek : true, //显示星期	
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置开始日期的最大日期
					$('#beginDate').datepicker('option', 'maxDate',
							new Date(dateText.replace('-', ',')));
				}
			});
</script>