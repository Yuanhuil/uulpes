<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/jobreport/plan" />
<form:form id="queryform" name="form"
	action="${baseaction}/querydata.do" method="post" modelAttribute="entity">
	<div class="filterContent">
		<ul>
		<%-- <c:if test="${suborgLevel !=null && fn:length(suborgLevel)>0}">
			<li><label class="name03">机构层级</label> 
			<form:select path="orgLevel" cssClass="input_160 validate[required]">
				<form:option value="">请选择</form:option>
				<form:options items="${suborgLevel }" itemValue="id" itemLabel="name"/>
			</form:select></li>
			</c:if> --%>
			<li><label class="name03">学年</label> 
			<form:select path="schoolyear"  data-placeholder="选择学年" cssClass="select_160" >
				<form:option value="">请选择</form:option>
				<form:options items="${schoolyears }" />
			</form:select></li>
			<li><label class="name03">学期</label>
			<form:select  path="term" data-placeholder="选择学期" cssClass="select_160" >
				<form:option value="">请选择</form:option>
				<form:options items="${schoolterm }" itemValue="id" itemLabel="name"/>
			</form:select></li>
		</ul>
		</div>
		<c:if test="${showchosen eq true }">
		<div class="filterContent">
			<ul>
				<li>
					<form:radiobutton path="queryOrgtype" value="1" label="区县教委" ></form:radiobutton>
					<form:radiobutton path="queryOrgtype" value="2" label="市直属学校" ></form:radiobutton>
					<form:radiobutton path="queryOrgtype" value="3" label="本机机构" ></form:radiobutton>
				</li>
			</ul>
		</div>
		</c:if>
	
	<div class="buttonContent">
			<input id="searchform" class="button-mid blue" type="submit"
				value="汇总统计">
			<input id="clearform" class="button-mid blue" type="button"
				value="重置">
	</div>
</form:form>

<script type="text/javascript">
	$(function(){
		$("#queryform").validationEngine();
		$("#queryform").ajaxForm({
			target : "#tablelist"
		});
		$("#clearform").click(function() {
			$("#queryform").clearForm();
		});
		$("#startTime").datepicker(
			{ //绑定开始日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置结束日期的最小日期
					$("#endTime").datepicker('option', 'minDate',
							new Date(dateText.replace('-', ',')));

				}
			});

		$("#endTime").datepicker(
			{ //设置结束绑定日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置开始日期的最大日期
					$("#startTime").datepicker('option', 'maxDate',
							new Date(dateText.replace('-', ',')));
				}
			});
	});
	
</script>