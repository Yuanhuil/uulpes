<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/jobmemorabilia" />
<form:form id="queryform" name="form"
	action="${baseaction}/querydata.do" method="post" modelAttribute="entity">
	<div class="filterContent">
		<ul>
			<li><label class="name03">活动类别</label> 
			<form:select path="activitycatalog" cssClass="input_160">
					<form:option value="">请选择</form:option>
					<form:options items="${plancatalog }" itemValue="id" itemLabel="name"/>
			</form:select></li>
			<li><label class="name03">活动类型</label> 
			<form:select path="activitytype" cssClass="input_160">
				<form:option value="">请选择</form:option>
			</form:select></li>
			
			<c:if test="${orglevel le 3 }">
			<li><label class="name03">机构层级</label>
			<form:select path="orgLevel" cssClass="input_160">
				<form:option value="">请选择</form:option>
				<form:options items="${orglevelList}"  itemValue="id"  itemLabel="name"/>
			</form:select>
			</li>
			</c:if>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">记录标题</label>
			<form:input id="form_title" path="title" cssClass="input_160"></form:input></li>
			<li><label class="name03">时间</label>
			<form:input path="starttime" cssClass="input_160"></form:input></li>
			<li><label class="name03">至</label>
			<form:input path="endtime" cssClass="input_160"></form:input></li>
		</ul>
	</div>
	<c:if test="${orglevel le 4 }">
	<div class="filterContent1" id="dzdiv">
			<ul>
				<li><label class="name03">行政区划</label>
				<form:select path="provinceid" cssClass="prov select_140" style="float:left;"></form:select>
				<form:select path="cityid" cssClass="city select_140" style="float:left;margin-left:10px;"></form:select>
				<form:select path="countyid" cssClass="dist select_140" style="float:left;margin-left:10px;"></form:select>
				<form:select path="townid" cssClass="street select_140" style="float:left;margin-left:10px;"></form:select></li>
			</ul>
		</div>
	</c:if>
	<div class="buttonContent">
	<input id="searchform" class="button-mid blue" type="submit"
				value="搜索">
			<input id="clearform" class="button-mid blue" type="button"
				value="重置">
	</div>
</form:form>

<script type="text/javascript">
	$(function(){
		$("#dzdiv").citySelect({
			prov:"${currentorg.provinceid}",
			city:"${currentorg.cityid}",
			dist:"${currentorg.countyid}",
			street:"${currentorg.townid}",
			defaultprov:"${currentorg.provinceid}",
			defaultcity:"${currentorg.cityid}",
		    defaultdist:"${currentorg.countyid}",
			nodata:"none"
		}); 
		$("#activitycatalog").change(function(){
			var p1=$(this).children("option:selected").val();
			$.ajax({
				url:'${baseaction}/getType.do',
				data:{'catid':p1},
				dataType:'json',
				success:function(data){
					$("#activitytype option").remove();
					$("#activitytype").append("<option value=''>请选择</option>");
					$.each(data,function(index,item){
						$("#activitytype").append("<option value='"+ item.id+ "'>" + item.name + "</option>");
					});
				},
				error:function(){
					console.log("发生错误");
				}
				
			});
		});
		$("#queryform").validationEngine();
		$("#queryform").ajaxForm({
			target : "#tablelist"
		});
		$("#clearform").click(function() {
			$("#queryform").clearForm();
		});
		$("#starttime").datepicker(
			{ //绑定开始日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置结束日期的最小日期
					$("#endtime").datepicker('option', 'minDate',
							new Date(dateText.replace('-', ',')));

				}
			});

		$("#endtime").datepicker(
			{ //设置结束绑定日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				firstDay : "1", //设置开始为1号
				onSelect : function(dateText, inst) {
					//设置开始日期的最大日期
					$("#starttime").datepicker('option', 'maxDate',
							new Date(dateText.replace('-', ',')));
				}
			});
	});
	
</script>