<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/taostation/volunteer"/>
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/create.do" />
	</c:when>
	<c:when test="${op eq '查看'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="" />
	</c:when>
	<c:when test="${op eq '修改'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/${entity.id}/update.do" />
	</c:when>
	<c:otherwise>
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/create.do" />
	</c:otherwise>
</c:choose>
<div id="editdialog" title="${empty op ? '新增' : op}志愿者">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity">
		<form:hidden path="id"/>
		<form:hidden path="name" id="teachername"/>
		<form:hidden path="orgname"/>
		<div class="filterContent">
			<ul>
				<li><label class="name04">区县</label><form:select id="formcountyid" path="countyid" cssClass="select_160 validate[required]">
				<form:option value="">请选择</form:option>
				<form:options items="${qxlist}" itemValue="code" itemLabel="name"/>
			</form:select></li>
			<li><label class="name04">学校</label><form:select id="formschoolorgid" path="schoolorgid" cssClass="select_160 validate[required]">
				<form:option value="">请先选择区县</form:option>
				<form:options items="${schoollist }" itemValue="orgid" itemLabel="xxmc"/>
			</form:select></li>
			<li><label class="name04">姓名</label> <form:select id="formname" path="accountid" cssClass="select_160 validate[required]">
					<form:option value="">请先选择学校</form:option>
					<form:options items="${teacherlist }" itemLabel="xm" itemValue="accountId"/>
				</form:select></li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><label class="name04">类型</label><form:select path="type" cssClass="select_160">
				<form:option value="">请选择</form:option>
				<form:options items="${typelist}" itemValue="id" itemLabel="name"/>
			</form:select></li>
			<li><label class="name04">工作年限</label><form:select path="worktime" cssClass="select_160 validate[required]">
				<form:option value="">请选择</form:option>
				<form:options items="${worktimelist}" itemValue="id" itemLabel="name"/>
			</form:select></li>
			<li><label class="name04">状态</label><form:select path="status" cssClass="select_160 validate[required]">
				<form:option value="">请选择</form:option>
				<form:options items="${statuslist}" itemValue="id" itemLabel="name"/>
			</form:select></li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><label class="name04">心研中心</label><form:select path="isxinyan" cssClass="select_160 validate[required]">
				<form:option value="">请选择</form:option>
				<form:options items="${sflist}" itemValue="id" itemLabel="name"/>
			</form:select></li>
			<li><label class="name04">认证级别</label><form:select path="qualificationlevel" cssClass="select_160 validate[required]">
				<form:option value="">请选择</form:option>
				<form:options items="${authlist}" itemValue="id" itemLabel="name"/>
			</form:select></li>
			<li><label class="name04">身份证号</label>
				<form:input path="idcard" cssClass="input_160" disabled="disabled"></form:input></li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><label class="name04">性别</label><form:select disabled="disabled" id="formgender" path="gender" cssClass="select_160"  >
				<form:option value="">请选择</form:option>
				<form:options items="${sexlist}" itemLabel="name" itemValue="id"/>
			</form:select></li>
			<li><label class="name04">学历</label><form:select disabled="disabled" path="education" cssClass="select_160">
				<form:option value="">请选择</form:option>
				<form:options items="${edulist}" itemValue="id" itemLabel="name"/>
			</form:select></li>
			<li><label class="name04">联系方式</label><form:input disabled="disabled" path="telephone" cssClass="input_160"></form:input></li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
			<li><label class="name04">电子邮箱</label><form:input disabled="disabled" path="email" cssClass="input_160"></form:input></li>
			</ul>
		</div>
		<div class="">
			<ul>
				<li><label class="name04">咨询经验</label>
				<form:textarea path="consultexp" style="margin-left: 0px; margin-right: 0px; width: 637px;"></form:textarea></li>
			</ul>
		</div>
		<div class="">
			<ul>
				<li><label class="name04">培训经历</label>
				<form:textarea path="trainexp" style="margin-left: 0px; margin-right: 0px; width: 637px;"></form:textarea></li>
			</ul>
		</div>
		<div class="">
			<ul>
				<li><label class="name04">擅长领域</label>
				<form:textarea path="expertin" style="margin-left: 0px; margin-right: 0px; width: 637px;"></form:textarea></li>
			</ul>
		</div>
		<div class="">
			<ul>
				<li><label class="name04">获奖情况</label>
				<form:textarea path="award" style="margin-left: 0px; margin-right: 0px; width: 637px;"></form:textarea></li>
			</ul>
		</div>
	</form:form>
</div>
<script type="text/javascript">
$(function(){
	$("#formcountyid").change(function(){
		var p1=$(this).children("option:selected").val();
		$.ajax({
			url:'${baseaction}/getSchools.do',
			data:{'countyid':p1},
			type:'post',
			success:function(data){
				$("#formschoolorgid option").remove();
				$("#formschoolorgid").append("<option value=''>请选择</option>");
				$.each(data,function(index,item){
					$("#formschoolorgid").append("<option value='"+ item.orgid+ "'>" + item.xxmc + "</option>");
				});
			},
			error:function(){
				console.log("发生错误");
			}
		});
	});
	$("#formschoolorgid").change(function(){
		var p1=$(this).children("option:selected").val();
		var text = $(this).children("option:selected").text();
		$.ajax({
			url:'${baseaction}/getTeachers.do',
			data:{'orgid':p1},
			type:'post',
			success:function(data){
				$("#orgname").val(text);
				$("#formname option").remove();
				$("#formname").append("<option value=''>请选择</option>");
				$.each(data,function(index,item){
					$("#formname").append("<option value='"+ item.id+ "'>" + item.realname + "</option>");
				});
			},
			error:function(){
				console.log("发生错误");
			}
		});
	});
	$("#formname").change(function(){
		var p1=$(this).children("option:selected").val();
		var text = $(this).children("option:selected").text();
		$.ajax({
			url:'${baseaction}/getTeacherInfo.do',
			data:{'accountid':p1},
			type:'post',
			success:function(data){
				if(data == null)return;
				$("#teachername").val(text);
				$("#idcard").attr("value",data.sfzh);
				$("#formgender").val(data.xbm);
				$("#education").val(data.xl);
				$("#telephone").val(data.lxdh);
				$("#email").val(data.email);
			},
			error:function(){
				console.log("发生错误");
			}
		});
	});
		var buttonsOps = {};
		<c:choose>
		<c:when test="${empty op || op eq '新增' || op eq '修改'}">
		buttonsOps = {
			"保存" : function() {
				if (!$("#editForm").validationEngine('validate'))
					return false;
				$("#editForm").ajaxSubmit({
					target : "#content2",
					success : function() {
						$("#editdialog").dialog("close");
						layer.open({content:"保存成功!"});
					},
					error : function() {
						layer.open({content:"保存失败"});
					}
				});
				$("#editForm").clearForm();
				return false;
			},
			"关闭" : function() {
				$("#editdialog").dialog("close");
			}
		};
		</c:when>
		<c:when test="${op eq '查看'}">
		buttonsOps = {
			"关闭" : function() {
				$("#editdialog").dialog("close");
			}
		};
		</c:when>
		</c:choose>
		$("#editdialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 780,
			width : 780,
			buttons : buttonsOps
		});
});

</script>