<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryform" name="form"
	action="/pes/systeminfo/sys/test/list.do"
	commandName="examdo" method="post">
<form:hidden path="userId"/>

	<div class="filterContent">
		<ul>
			
			<li><label class="name03">开始时间</label> <input type="text"
				class="input_160" name="beginDate" id="beginDate"></li>
			<li><label class="name03">结束时间 </label> <input type="text"
				class="input_160" name="endDate" id="endDate"></li>
			<li><input type="submit" id="query" name="query" value="查询"
		class="button-mid blue" /></li>
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