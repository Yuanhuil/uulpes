<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="queryformteam" name="form"
     action="/pes/consultcenter/team/list.do" commandName="team"
     method="post">
     <div class="filterContent">
     <ul>
        <li><label class="name03">团体类型</label> <form:select
               class="select_160" path="teamtype">
                 <form:options items="${teamTypeEnum }" itemValue="value"
                  itemLabel="info"></form:options>
                 </form:select></li>
        <li><label class="name03">团队名称</label> <input type="text"
             class="input_160" id="name1" name="name"></li>
        <li><label class="name03">团队人数</label> <input type="text"
             class="input_160" id="personnum1" name="personnum">
     </ul>
     </div>
     <div class="buttonContent">
         <input type="submit" id="query" name="query" value="查询"
             class="button-mid blue" /> <input type="button" name="clear"
             id="clearTeam" value="重置" class="button-mid blue" /> <input
             type="button" id="add" value="添加" class="button-mid blue" />
     </div>
</form:form>
<script type="text/javascript">
     $("#queryformteam").ajaxForm({
        target : "#list1"
    });
     $("#clearTeam").click(function() {
         $('#queryformteam').clearForm();
    });

	$("#add").on("click", function() {
		$("#dialog-form1").dialog("open");
		var h = '/pes/consultcenter/team/addOrUpdate.do';
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