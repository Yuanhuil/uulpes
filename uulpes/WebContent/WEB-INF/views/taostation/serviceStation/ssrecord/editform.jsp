<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/taostation/serviceStation/ssrecord/${entity.ssid }"/>
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="${baseaction }/create.do" />
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
		<c:set var="formaction" scope="page" value="${baseaction }/create.do" />
	</c:otherwise>
</c:choose>
<div id="editdialog" title="${empty op ? '新增' : op}流动站记录">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity">
		<form:hidden path="ssid"/>
		<div class="filterContent-dlg">
			<ul>
				<li><label>服务标题</label> <form:input path="title" cssClass="input_300"></form:input></li>
				<li><label>服务时间</label> <form:input path="servicetime" cssClass="input_300"></form:input></li>
			</ul>
		</div>
		<div>
		服务记录
		</div>
		<form:textarea path="comment"></form:textarea>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">附件</label></li>
				<li>
					<div id="uploader" class="wu-example">
    					<div class="btns">
        					<div id="picker">选择文件</div>
    					</div>
					</div>
				</li>
			</ul>
		</div>
		<div class="tableContent">
			<table id="filelisttable">
				<tr>
					<th style="display:none"></th>
					<th>名称</th>
					<th>操作</th>
				</tr>
				<tr class="tmp" style="display:none">
					<td style="display:none"></td>
					<td></td>
					<td></td>
				</tr>
				<c:forEach items="${attachments}" var="a">
					<tr id="file_${a.attachment.id}">
						<td><a href="${a.attachment.savePath}" target="_blank">${a.attachment.name}</a></td>
						<td><input type="button" value="删除" class="old button-small white filedel" href="${baseaction }/file/${entity.id}/${a.attachment.id}/del.do"/></td>
					</tr>
				</c:forEach>
			</table>
		</div>
		
	</form:form>
</div>
<script type="text/javascript">
$(function(){
	$("#servicetime").datepicker(
			{ //绑定开始日期
				dateFormat : 'yy-mm-dd',
				changeMonth : true, //显示下拉列表月份
				changeYear : true, //显示下拉列表年份
				firstDay : "1" //设置开始为1号
			});
	var job_uploader = WebUploader.create({
		// 自动上传。
		auto : true,
		// swf文件路径
		swf : '/js/webuploader-0.1.5/Uploader.swf',
		// 文件接收服务端。
		server : '/pes/taostation/attachment/save.do',
		// 选择文件的按钮。可选。
		pick : '#picker',

	});
	job_uploader.on('uploadSuccess', function(file,res) {
		var list = res._raw;
		var element = JSON.parse(list)[0];
		var chk = $("<input  type='checkbox' name='fileuuids' checked='checked'></input>").attr("value",element.uuid);
		var a = $("<a></a>").html(element.name).attr("href",element.savePath).attr("target","_blank");
		var op = $("<input type='button' value='删除' class='button-small white filedel new'></input>").attr("uuid",element.uuid).attr("href","/pes/taostation/attachment/" + element.uuid + "/deluuid.do");
		var tr = $(".tmp").clone(true).removeClass("tmp");
		tr.find("td").first().append(chk);
		tr.find("td").eq(1).append(a);
		tr.find("td").last().append(op);
		tr.attr("id",element.uuid);
		op.on("click",function(){
			var href = $(this).attr("href");
			$.ajax({
				url:href,
				type:'post',
				success:function(){
					$("#" + element.uuid).remove();
				}
			});
		});
		$("#filelisttable").append(tr.show());
	});
	$("textarea#comment").ckeditor();
	$(".button-small.white.filedel.old").on('click',function(){
		var href = $(this).attr("href");
		$.ajax({
			url:href,
			type:'post',
			success:function(s){
				$("#file_" + s).remove();
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
			height : 600,
			width : 1000,
			buttons : buttonsOps
		});
});

</script>