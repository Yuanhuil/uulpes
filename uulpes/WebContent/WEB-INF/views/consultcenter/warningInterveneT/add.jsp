<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="编辑">
	<form:form id="editForm1"
		action="/pes/consultcenter/warningInterveneT/save.do"
		method="post" commandName="warningInterveneT">
		<form:hidden path="id" />
		<form:hidden path="schoolId" />
		<form:hidden path="status" />
		<input type="text" hidden="hidden" id="oldstatus" value="${oldstatus}">
		<div class="filterContent">
			<ul>
				<li><label class="name04">角色名称</label> <form:select
						path="roleName" cssClass="input_160">
						<form:option value="">----</form:option>
						<form:options items="${rolelist }" itemValue="id"
							itemLabel="roleName" />
					</form:select></li>
				<li><label class="name04">姓名</label> <form:input path="name"
						class="input_160" /></li>
				<li><label class="name04">性别</label> <form:select path="sex"
						class="input_160" items="${sexEnum}" itemLabel="info"
						itemValue="id"></form:select></li>

			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><label class="name04">身份证号</label> <form:input
						path="cardid" class="input_160" /></li>
				<li><label class="name04"> 预警级别</label> <form:select
						path="level" class="input_160" items="${warningLever}"
						itemLabel="name" itemValue="state"
						onChange="showDisposeTypeOption(true)"></form:select></li>
				<li><label class="name04">预警时间</label> <form:input
						path="warningTime" class="input_160" readonly="true" /></li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><label class="name04">处置方式</label> <form:select
						path="disposeType" class="input_160"
						defaultValue="${warningInterveneT.disposeType}">
						<form:option value="1" class="disposeType" level="1">与班主任会商</form:option>
						<form:option value="2" class="disposeType" level="1">重点观察</form:option>
						<form:option value="3" class="disposeType" level="1">班主任约谈</form:option>
						<form:option value="4" class="disposeType" level="1">咨询员约谈</form:option>
						<form:option value="5" class="disposeType" level="2">报告校领导</form:option>
						<form:option value="6" class="disposeType" level="2">校内干预与保护</form:option>
						<form:option value="7" class="disposeType" level="3">报告校领导</form:option>
						<form:option value="8" class="disposeType" level="3">校内干预与保护</form:option>
						<form:option value="9" class="disposeType" level="3">区县中心介入</form:option>
						<form:option value="10" class="disposeType" level="3">市中心介入</form:option>

					</form:select></li>
			
			<li><label class="name04">处理角色</label> <form:select id="roleId" path="disposeRoleId" onChange="selectTeacher()"
				class="input_160">
					<form:option value="">----</form:option>
					<form:option value="21">心理咨询员</form:option>
			</form:select></li>
			<li><label class="name04">处理者</label> <form:select
				path="disposePerson" class="input_160">
					<form:option value="">请选择</form:option>
				<form:options  items="${disposePersonList}"  itemValue="xm"  itemLabel="xm"/>	
			</form:select></li>
			</ul>
		</div>
		<div class="filterContent" id="intervene_show"
			style="display: ${intervene_show};">
			<ul>
				<li><label class="name04">干预时间</label> <form:input
						path="interveneTime" class="input_160" /></li>


				<li><label class="name04">干预方式</label> <form:select path="type"
						class="input_160">

						<form:options items="${interveneType}" itemLabel="name"
							itemValue="state"></form:options>

					</form:select></li>
				<li><label class="name04">干预结果</label> <form:select
						path="result" class="input_160">

						<form:options items="${interveneResult}" itemLabel="name"
							itemValue="state"></form:options>

					</form:select></li>

			</ul>

		</div>
		<div style="display: ${intervene_show=='none'?'block':'none'};">
			特别记录</br>
			<form:textarea path="record" cols="80" rows="10"></form:textarea>
		</div>
		<div style="display: ${intervene_show};">
			干预过程</br>
			<form:textarea path="process" cols="80" rows="10"></form:textarea>
		</div>

	</form:form>
</div>
<script type="text/javascript">
	$("#dialog-form1").dialog({
		appendTo : "#editDiv",
		autoOpen : false,
		modal : false,
		height : 600,
		width : 800,
		buttons : {
		<c:if test="${empty view || view eq 'false'}">
			"保存" : function() {
				$("#dialog-form1").dialog("close");
				$("#editForm1").ajaxSubmit({
					target : "#list1",
					data : {
					    'roleName1':jQuery('#roleName1').val(),
						'name1' : jQuery('#name1').val(),
						'sex1' : jQuery('#sex1').val(),
						'level1' : jQuery('#level1').val(),
						'oldstatus':jQuery('#oldstatus').val(),
						'beginDate' : jQuery('#beginDate').val(),
						'endDate' : jQuery('#endDate').val(),
					},
					success : function() {
						$("#dialog-form1").dialog("close");
						layer.open({content:"保存成功!"});
					},
					error : function() {
						layer.open({content:"保存失败"});
					}
				});
				return false;
			},
			</c:if>
			"取消" : function() {
				$("#dialog-form1").dialog("close");
			}
		},
	});
    $("#warningTime").datepicker({
	dateFormat : 'yy-mm-dd', //更改时间显示模式
	showAnim : "slide", //显示日历的效果slide、fadeIn、show等
	changeMonth : true, //是否显示月份的下拉菜单，默认为false
	changeYear : true, //是否显示年份的下拉菜单，默认为false
	showWeek : true, //是否显示星期,默认为false
//	showButtonPanel : true, //是否显示取消按钮，并含有today按钮，默认为false
//	closeText : 'close', //设置关闭按钮的值
//yearRange:'2010:2012',	//显示可供选择的年份
});

$("#interveneTime").datepicker({
	dateFormat : 'yy-mm-dd', //更改时间显示模式
	showAnim : "slide", //显示日历的效果slide、fadeIn、show等
	changeMonth : true, //是否显示月份的下拉菜单，默认为false
	changeYear : true, //是否显示年份的下拉菜单，默认为false
	showWeek : true, //是否显示星期,默认为false
//	showButtonPanel : true, //是否显示取消按钮，并含有today按钮，默认为false
//	closeText : 'close', //设置关闭按钮的值
//yearRange:'2010:2012',	//显示可供选择的年份
});
	$("#editForm1").ajaxForm({
		target : "#list1"
	});
	$("textarea#process").ckeditor({height: '160px'});
	$("textarea#record").ckeditor({height: '200px'});
	function showDisposeTypeOption(selectFirst){
		var level=$("#level").val();
		if(level==undefined){
			level=1;
		}
		if(!selectFirst){
			var defaultValue=$('#disposeType').attr("defaultValue");
			$('#disposeType').find("option[value='"+defaultValue+"']").attr("selected",true);
		}
		$('#disposeType').find('option').each(function(i,item){
			var level1 = $(item).attr("level");
			if(level1===level){
				$(item).show();
				if(selectFirst){
					$(item).attr("selected",true);
					selectFirst=false;
				}
			}else{
				$(item).hide();
			}
		});
	}
	
	function selectTeacher(){
			var roleId = $("#roleId").val();
					$.ajax({
						url : "${baseaction}/pes/systeminfo/sys/user/teacher/getTeachersJson.do",
						data : {
							"roleId" : roleId	
						},
						dataType : "json",
						type : "POST",
						success : function(data) {
							$("#disposePerson").empty();
							$.each(data, function(i, k) {
							  
								$("#disposePerson").append(
										"<option value='" + k.xm + "'>"
												+ k.xm + "</option>");
							});
							
						}

					});
	}
	showDisposeTypeOption(false);
</script>