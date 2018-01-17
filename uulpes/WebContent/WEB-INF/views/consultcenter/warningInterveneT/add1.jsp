<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="编辑">
	<form:form id="editForm1"
		action="/pes/consultcenter/warningInterveneT/saveAdd.do"
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
						<option value="1" class="disposeType" level="1">与班主任会商</option>
						<option value="2" class="disposeType" level="1">重点观察</option>
						<option value="3" class="disposeType" level="1">班主任约谈</option>
						<option value="4" class="disposeType" level="1">咨询员约谈</option>
						<option value="5" class="disposeType" level="2">报告校领导</option>
						<option value="6" class="disposeType" level="2">校内干预与保护</option>
						<option value="7" class="disposeType" level="3">报告校领导</option>
						<option value="8" class="disposeType" level="3">校内干预与保护</option>
						<option value="9" class="disposeType" level="3">区县中心介入</option>
						<option value="10" class="disposeType" level="3">市中心介入</option>

					</form:select></li>
			
			<li><label class="name04">处理角色</label> <select id="roleId" onChange="selectTeacher()"
				class="input_160">
					<option value="">----</option>
					<option value="1">超级管理员</option>
					<option value="3">系统管理员</option>
					<option value="4">维护管理员</option>
					<option value="6">修改管理员</option>
					<option value="9">审核管理员</option>
					<option value="10">监控管理员</option>
					<option value="11">市级管理员</option>
					<option value="12">市级负责人</option>
					<option value="14">市教委管理员</option>
					<option value="15">市教委工作人员</option>
					<option value="16">市教委负责人</option>
					<option value="20">学校管理员</option>
					<option value="21">心理老师</option>
					<option value="22">心理咨询老师</option>
					<option value="29">任课老师</option>
					<option value="38">区县管理员</option>
					

			</select></li>
			<li><label class="name04">处理者</label> <form:select
				path="disposePerson" class="input_160">
					<option value="${warningInterveneT.disposePerson}">${warningInterveneT.disposePerson}</option>
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
		height : 700,
		width : 800,
		buttons : {
		<c:if test="${empty view || view eq 'false'}">
			"保存" : function() {
				$("#dialog-form1").dialog("close");
				$("#editForm1").ajaxSubmit({
					target : "#list1",
					data : {
					    'iswarnsure1':jQuery('#iswarnsure').val(),
						'warningGrade1':jQuery('#warningGrade').val(),
						'xbm1':jQuery('#xbm').val(),
						'xm1' :jQuery('#xm').val(),
						'scaleId1':jQuery('#scaleId').val(),
						'roleId1': jQuery('#roleId').val(),
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
	$("textarea#process").ckeditor({height: '240px'});
	$("textarea#record").ckeditor({height: '300px'});
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