<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/activitysummary/create"/>
 <div class="stepControl">
	<div class="step1_sel" id="select_step1">1.选择活动记录</div>
	<div class="step2" id="select_step2">2.生成总结</div>
 </div>
<div id="select1">
<form:form id="queryform" name="form"
	action="${baseaction}/querydata.do" method="post"
	modelAttribute="entity">
	<div class="filterContent">
		<ul>
			<li><label class="name03">学年</label> 
			<form:select path="schoolyear"  data-placeholder="选择学年" cssClass="select_160 validate[required]" >
				<form:option value="">请选择</form:option>
				<form:options items="${schoolyears }"/>
			</form:select></li>
			<li><label class="name03">学期</label>
			<form:select  path="term" data-placeholder="选择学期" cssClass="select_160 validate[required]" >
				<form:option value="">请选择</form:option>
				<form:options items="${schoolterm }" itemValue="id" itemLabel="name"/>
			</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">活动类别</label> 
			<form:select path="activitycatalog" data-placeholder="选择活动类别" cssClass="select_160">
					<form:option value="">请选择</form:option>
					<form:options items="${plancatalog }" itemValue="id" itemLabel="name"/>
					</form:select></li>
			<li><label class="name03">活动类型</label>
			<form:select  path="activitytype" data-placeholder="选择活动类型" cssClass="select_160">
					<form:option value="">请选择</form:option>
			</form:select></li>
		</ul>
	</div>
	<div class="buttonContent">
		<input id="searchform" class="button-mid blue" type="submit"
				value="搜索">
			<input id="clearform" class="button-mid blue" type="button"
				value="重置">
			<input id="backurl" class="button-mid blue" type="button"
				value="返回">
	</div>
</form:form>
<div id="create_tablelist">
	<%@include file="tablelist.jsp" %>
</div>
</div>

<div id="select2"></div>
<script type="text/javascript">
	$(function(){
		$("#activitycatalog").change(function(){
			var p1=$(this).children("option:selected").val();
			$.ajax({
				url:'${ctx }/workschedule/activitysummary/getType.do',
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
		$("#queryform").ajaxForm({
			beforeSubmit:function(){
				if(!$("#queryform").validationEngine('validate'))
					return false;
			},
			target : "#create_tablelist"
		});
		$("#clearform").click(function() {
			$("#queryform").clearForm();
		});
		$("#backurl").click(function(){
			$("#content2").load("${ctx }/workschedule/activitysummary/list.do");
		});
	});
	
</script>