<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/taolstation/taoRecord/list.do" commandName="taoRecord"
	method="post">
	<div class="filterContent">
		<ul>
			<li><label class="name03">咨询类型</label> <form:select
					class="select_160" path="consultationtypeid">
					<option value>请选择</option>
					<form:options items="${consultTypes}" itemLabel="name"
						itemValue="id"></form:options>
				</form:select></li>
			<li><label class="name03">咨询员</label> <form:select
					class="select_160" path="teacherid">
					<option value>请选择</option>
					<form:options items="${teachers}" itemLabel="xm" itemValue="id"></form:options>
				</form:select></li>
			<li><label class="name03">咨询方式</label> <form:select
					class="select_160" path="consultationmodeid">
					<option value>请选择</option>
					<form:options items="${consultationModels}" itemLabel="name"
						itemValue="id"></form:options>
				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">姓名</label> <input type="text"
				class="input_160" id="username" name="username"></li>
			<li><label class="name03">操作时间</label> <input type="text"
				name="beginDate" class="input_160" id="beginDate"></li>
			<li><label class="name03">至</label> <input type="text"
				name="endDate" class="input_160" id="endDate"></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>

			<li><label class="name03">咨询对象</label> <form:select
					class="select_160" path="objtype">
					<option value>请选择</option>
					<form:options items="${typeEnum}" itemLabel="info"
						itemValue="value"></form:options>
				</form:select></li>
		</ul>
	</div>
	<div class="buttonContent">
		<input type="submit" id="query" name="query" value="查询"
				class="button-mid blue" /> <input type="button" name="clear"
				id="clear" value="重置" class="button-mid blue" /> <input
				type="button" id="add" value="添加" class="button-mid blue" />
	</div>
</form:form>
<script type="text/javascript">
	$("#queryform").ajaxForm({
		target : "#list"
	});
	$("#clear").click(function() {
		$('#queryform').clearForm();
	});

	$("#add").on("click", function() {
		$("#dialog-form1").dialog("open");
		var h = '/pes/taolstation/taoRecord//addOrUpdate.do';
		$('#editDiv').html();
		$('#editDiv').load(h, function() {
			$("#dialog-form1").dialog("open");
		});
	});

	$("#beginDate").datepicker({ //绑定开始日期
		dateFormat : 'yy-mm-dd',
		changeMonth : true, //显示下拉列表月份
		changeYear : true, //显示下拉列表年份
		showWeek : true, //显示星期	
		firstDay : "1", //设置开始为1号
		onSelect : function(dateText, inst) {
			//设置结束日期的最小日期
			$("#endDate").datepicker("option", "minDate", dateText);
		}
	});

	$("#endDate").datepicker({ //设置结束绑定日期
		dateFormat : 'yy-mm-dd',
		changeMonth : true, //显示下拉列表月份
		changeYear : true, //显示下拉列表年份
		showWeek : true, //显示星期	
		firstDay : "1", //设置开始为1号
		onSelect : function(dateText, inst) {
			//设置开始日期的最大日期
			$("#beginDate").datepicker("option", "maxDate", dateText);
		}
	});
</script>