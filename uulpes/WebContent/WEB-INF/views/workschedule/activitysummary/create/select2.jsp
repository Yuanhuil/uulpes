<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/activitysummary/create"/>
<form:form id="select2_queryform" name="form"
	action="${baseaction}/add.do" method="post"
	modelAttribute="entity">
	<form:hidden path="iniContent"/>
	<div class="filterContent">
		<ul>
			<li><label class="name03">学年</label> 
			<form:select id="select2_sy" path="schoolyear"  data-placeholder="选择学年" cssClass="select_160 validate[required]" >
				<form:option value="">请选择</form:option>
				<form:options items="${schoolyears }"/>
			</form:select></li>
			<li><label class="name03">学期</label>
			<form:select  id="select2_term" path="term" data-placeholder="选择学期" cssClass="select_160 validate[required]" >
				<form:option value="">请选择</form:option>
				<form:options items="${schoolterm }" itemValue="id" itemLabel="name"/>
			</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">活动类别</label> 
			<form:select id="select2_act" path="activitycatalog" data-placeholder="选择活动类别" cssClass="select_160">
					<form:option value="">请选择</form:option>
					<form:options items="${plancatalog }" itemValue="id" itemLabel="name"/>
					</form:select></li>
			<li><label class="name03">活动类型</label>
			<form:select  id="select2_type" path="activitytype" data-placeholder="选择活动类型" cssClass="select_160">
					<form:option value="">请选择</form:option>
					<form:options items="${plantype }" itemValue="id" itemLabel="name"/>
			</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label>活动总结标题</label> 
			<form:input id="select2_title" path="title" cssClass="input_160 validate[required]" ></form:input></li>
		</ul>
	</div>
	<div>活动总结</div>
		<form:textarea id="select2_content" path="content" cols="80" rows="8"></form:textarea>
	<div class="buttonContent">
			<input id="select2_save" class="button-mid blue" type="submit"
				value="保存">
			<input id="select2_initcontent" class="button-mid blue" type="button"
				value="还原初始总结">
			<input id="select2_back" class="button-mid blue" type="button"
				value="取消">
	</div>
</form:form>
<script type="text/javascript">
	$(function(){
		$("#select2_act").change(function(){
			var p1=$(this).children("option:selected").val();
			$.ajax({
				url:'${ctx }/workschedule/activitysummary/getType.do',
				data:{'catid':p1},
				dataType:'json',
				success:function(data){
					$("#select2_type option").remove();
					$("#select2_type").append("<option value=''>请选择</option>");
					$.each(data,function(index,item){
						$("#select2_type").append("<option value='"+ item.id+ "'>" + item.name + "</option>");
					});
				},
				error:function(){
					console.log("发生错误");
				}
				
			});
		});
		$("textarea#select2_content").ckeditor();
		$("#select2_queryform").validationEngine();
		$("#select2_queryform").ajaxForm({
			target : "#content2"
		});
		
		$("#select2_initcontent").click(function(){
			var c = $("#iniContent").val();
    		CKEDITOR.instances['select2_content'].setData(c); 
		});
		$("#select2_back").click(function(){
			$("#select1").show();
			$("#select2").empty();
			$("#select_step1").addClass("step1_sel").removeClass("step1");
			$("#select_step2").addClass("step2").removeClass("step2_sel");
		});
	});
	
</script>