<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="编辑咨询类型">
	<form:form id="consultTypeEditForm" method="post"
		commandName="consultType"
		action="/pes/consultcenter/consultType/save.do">
		<form:hidden path="id" />
		<form:hidden path="fid" />
		<div class="popDiv_content">

			<ul>
				<li class="list01"><form:label path="name" class="name03">名称</form:label>
					<form:input path="name" class="input_160" type="text"
						placeholder="小于50个字符" /></li>
				<li class="list01"><form:label path="status"
						 class="name03">开关</form:label> <form:select
						path="status" class="input_160" items="${switchEnum}"
						itemLabel="info" itemValue="value"></form:select></li>
				<li class="list01"><form:label path="sort"
						 class="name03">排序</form:label> <form:input path="sort"
						 type="number" min="0"
						max="999" class="input_160" placeholder="只允许数字" /></li>
			</ul>



		</div>
	</form:form>
</div>
<script type="text/javascript">
	$("#dialog-form1").dialog({
		appendTo : "#consultTypeEdit",
		autoOpen : false,
		modal : true,
		height : 500,
		width : 800,
		buttons : {
			"保存" : function() {
				$("#dialog-form1").dialog("close");
				$("#consultTypeEditForm").ajaxSubmit({
					target : "#consultTypeList",
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
			"取消" : function() {
				$("#dialog-form1").dialog("close");
			}
		},
	});

	$("#consultTypeEditForm").ajaxForm({
		target : "#consultTypeList"
	});
</script>