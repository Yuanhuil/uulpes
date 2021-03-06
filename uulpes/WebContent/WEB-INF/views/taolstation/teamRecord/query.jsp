<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/consultcenter/teamRecord/list.do"
	commandName="record" method="post">
	<div class="filterContent">
		<ul>
			<li><label class="name03">咨询类型</label> <form:select
					class="select_160" path="consultationtypeid">
					<option value>请选择</option>
					<form:options items="${consultTypes }" itemValue="id"
						itemLabel="name"></form:options>
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

			<li><label class="name03">操作时间</label> <input type="text"
				class="input_160" name="beginDate" id="beginDate"></li>
			<li><label class="name03">至 </label> <input type="text"
				class="input_160" name="endDate" id="endDate"></li>
		</ul>
	</div>
	<div class="buttonContent">
		<ul>
			<li><input type="submit" id="query" name="query" value="查询"
				class="button-mid blue" /> <input type="button" name="clear"
				id="clear" value="重置" class="button-mid blue" /></li>
		</ul>
	</div>
</form:form>
<script type="text/javascript">
	$("#queryform").ajaxForm({
		target : "#list"
	});
	$("#clear").click(function() {
		$('#queryform').clearForm();
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