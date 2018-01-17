<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="编辑">
	<input hidden="true" id="viewStatus" value="${viewStatus} ">

	<form:form id="editForm" method="post" commandName="email"
		action="/pes/consultcenter/email/save.do" style="display:none">
		<form:hidden path="id" />
		<form:hidden path="toid" />
		<form:hidden path="emailstatus" />
		<div class="filterContent">
			<ul>
				<li><form:label path="title" class="name04">邮件标题</form:label> <form:input
						path="title" class="input_160" /></li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><form:label path="fromid" class="name04">发件人</form:label> <input
					class="input_160" value="${sentUserName}" readonly> <form:hidden
						path="fromid" class="input_160" /></li>
			</ul>
		</div>

		<div class="filterContent">
			<ul>
				<li><label class="name04">收件人</label> <input
					class="input_160" value="${toUserName}" readonly> </li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li style="text-align:left;" class="name04">问题概述</li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><form:textarea path="describes" /></li>
			</ul>
		</div>

	</form:form>
	<div id="history"></div>
</div>
<script type="text/javascript">
	$("#dialog-form1").dialog({
		appendTo : "#editDiv",
		autoOpen : false,
		modal : true,
		height : 500,
		width : 800,
		buttons : {
		<c:if test="${empty view || view eq 'false'}">
			"发送" : function() {
			   
				$("#dialog-form1").dialog("close");
				$("#emailstatus").attr("value", "1");
				$("#editForm").ajaxSubmit({
					target : "#list",
					data : {
						'title1' : jQuery('#title1').val(),
						'beginDate' : jQuery('#beginDate').val(),
						'endDate' : jQuery('#endDate').val(),
					},
					success : function() {
						$("#dialog-form1").dialog("close");
						$("#yfs").attr("class", "active");
						$("#yfs").parent().addClass("active");
						$("#cgx").removeClass("active");
						$("#cgx").parent().removeClass("active");
						$("#sjx").removeClass("active");
						$("#sjx").parent().removeClass("active");
						layer.open({content:"发送成功!"});
					},
					error : function() {
						layer.open({content:"发送失败"});
					}
				});
				return false;
			},
			"存草稿" : function() {
				$("#dialog-form1").dialog("close");
				$("#emailstatus").attr("value", "0");
				$("#editForm").ajaxSubmit({
					target : "#list",
					data : {
						'title1' : jQuery('#title1').val(),
						'beginDate' : jQuery('#beginDate').val(),
						'endDate' : jQuery('#endDate').val(),

					},
					success : function() {
						$("#dialog-form1").dialog("close");
						$("#cgx").attr("class", "active");
						$("#cgx").parent().addClass("active");
						$("#yfs").removeClass("active");
						$("#yfs").parent().removeClass("active");
						$("#sjx").removeClass("active");
						$("#sjx").parent().removeClass("active");
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

	$("#editForm").ajaxForm({
		target : "#list"
	});
	jQuery('#reply').bind('click', function() {
		jQuery('#editForm').attr("style", "display:block");
	});
	$(document).ready(function() {
		var v = jQuery('#viewStatus').attr("value");
		if (v == 1) {
			jQuery('#viewForm').attr("style", "display:block");
		}
		if (v == 2) {
			jQuery('#editForm').attr("style", "display:block");
		}
		if (v == 3) {
			jQuery('#viewForm').attr("style", "display:block");
			jQuery('#editForm').attr("style", "display:block");
		}
		var id = jQuery('#id').attr("value");
		
		var h = '/pes/consultcenter/email/parentsEmails.do?id=' + id;
		$('#history').load(h);

	})
</script>