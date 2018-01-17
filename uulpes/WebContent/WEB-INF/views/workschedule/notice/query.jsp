<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/notice" />
<form:form id="queryform" name="form"
	action="${baseaction}/querydata.do" method="post"
	modelAttribute="entity">
	<div class="filterContent">
		<ul>
			<li><label class="name03">角色名称</label> 
			<form:select path="authorRole" cssClass="select_160">
					<form:option value="">请选择</form:option>
			 <form:options  items="${rolelist }" itemValue="id" itemLabel="roleName"/>
					</form:select></li>
			<li><label class="name03">类别</label>
			<form:select  path="catalog" cssClass="select_160">
			 <form:option value="">请选择</form:option>
			 <form:options  items="${cataloglist }" itemValue="id" itemLabel="name"/>
			 </form:select></li>
			<li><label class="name03">状态</label>
			<form:select  path="state" cssClass="select_160" >
			 <form:option value="">请选择</form:option>
			 <form:options  items="${jobstatelist }" itemValue="id" itemLabel="name"/>
			</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li>
				<label class="name03">标题</label>
				<form:input id="querytitle" path="title" cssClass="input_160"></form:input>
			</li>
			<li>
				<label class="name03">发布者</label>
				<form:input path="authorName" cssClass="input_160"></form:input>
			</li>
		</ul>
	</div>

	<div class="filterContent">
		<ul>
			<li>
				<label class="name03">发布时间</label>
				<form:input path="startTime" cssClass="input_160"></form:input>
			</li>
			<li>
				<label class="name03">至</label>
				<form:input path="endTime" cssClass="input_160"></form:input>
			</li>
		</ul>
	</div>
	<div  class="buttonContent">
	<div class="buttonLeft">
		<ul>
		<li>
				<input class="button-mid white" type="button" value="新增" chref="${baseaction}/add.do" id="add">
			</li>
			<li>
				<input class="button-mid white" type="button" value="删除" id="del" chref=""> 
			</li>
		</ul>
		</div>
		<div class="buttonRight">
		<ul><li><input id="searchform" class="button-mid blue" type="submit"
				value="搜索"></li>
			<li><input id="clearform" class="button-mid blue" type="button"
				value="重置"></li></ul>
		</div>
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
				   $("#editformdiv").html();
				   $("#editformdiv").load(h,function(){
					   $( "#editdialog" ).dialog("open");
				   });
			 	}); 
		   }
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