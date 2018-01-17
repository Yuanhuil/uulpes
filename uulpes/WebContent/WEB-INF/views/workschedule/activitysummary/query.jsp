<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/activitysummary" />
<form:form id="queryform" name="form"
	action="${baseaction}/querydata.do" method="post"
	modelAttribute="entity">
	<div class="filterContent">
		<ul>
			<li><label class="name03">学年</label> 
			<form:select path="schoolyear"  data-placeholder="选择学年" cssClass="select_160" >
				<form:option value="">请选择</form:option>
				<form:options items="${schoolyears }"/>
			</form:select></li>
			<li><label class="name03">学期</label>
			<form:select  path="term" data-placeholder="选择学期" cssClass="select_160" >
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
			<li>
				<label class="name03">活动总结</label> 
				<form:input id="querytitle" path="title" cssClass="input_160"></form:input>
			</li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li>
				<label class="name03">生成时间</label> 
				<form:input path="querystarttime" cssClass="input_160"></form:input>
			</li>
			<li>
				<label class="name03">至</label> 
				<form:input path="queryendtime" cssClass="input_160"></form:input>
			</li>
			
		</ul>
	</div>
	<div class="buttonContent">
	<div class="buttonLeft">
	<ul>
	<li>
				<input class="button-mid white" type="button" value="生成" chref="${baseaction}/add.do" id="add">
			</li>
			<li>
				<input class="button-mid white" type="button" value="删除" id="del" chref=""> 
			</li>
	</ul></div>
	<div class="buttonRight">
	<ul>
	<li><input id="searchform" class="button-mid blue" type="submit"
				value="搜索"></li>
			<li><input id="clearform" class="button-mid blue" type="button"
				value="重置"></li>
	</ul></div>
	</div>
</form:form>

<script type="text/javascript">
	$(function(){
		$("#del").click(function(){
			  var selectedData = [];
			  var selectRow = $("input[name='rowcheck']:checked");
			  if(selectRow.length === 0){
				  layer.open({content:"没有选择相关内容"});
				  return false;
			  }
			  layer.confirm('确定要删除所选择记录内容吗?', {
					btn : [ '是', '否' ]
				}, function(index) {
						layer.close(index);
			  			selectRow.each(function(){
				    	selectedData.push(this.value );
			  		});
			  		$('#content2').load("${baseaction }/delselected.do",{rowcheck:selectedData});
				});
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
		if($( "#add" ) != null){
			   $( "#add" ).on( "click", function() {
				   var h = $(this).attr("chref");
				   $("#content2").empty();
				   $("#content2").load(h);
			 	}); 
		   }
		$("#querystarttime").datepicker(
				{ //绑定开始日期
					dateFormat : 'yy-mm-dd',
					changeMonth : true, //显示下拉列表月份
					changeYear : true, //显示下拉列表年份
					firstDay : "1", //设置开始为1号
					onSelect : function(dateText, inst) {
						//设置结束日期的最小日期
						$("#queryendtime").datepicker('option', 'minDate',
								new Date(dateText.replace('-', ',')));

					}
				});

			$("#queryendtime").datepicker(
				{ //设置结束绑定日期
					dateFormat : 'yy-mm-dd',
					changeMonth : true, //显示下拉列表月份
					changeYear : true, //显示下拉列表年份
					firstDay : "1", //设置开始为1号
					onSelect : function(dateText, inst) {
						//设置开始日期的最大日期
						$("#querystarttime").datepicker('option', 'maxDate',
								new Date(dateText.replace('-', ',')));
					}
				});
	});
	
</script>