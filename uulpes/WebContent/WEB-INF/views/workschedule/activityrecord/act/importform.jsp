<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/workschedule/plan" />
<div id="importdialog" title="文件导入">
	<table>
		<tr>
			<td>1.下载<!-- <a href="${ctx }/excel/workschedule/导入心理活动记录_新模板.xlsx" target="_blank">导入心理活动记录模板</a>-->
			<a href="javascript:void(0);" onclick="downloadtemplate('导入心理活动记录_新模板');">导入心理活动记录模板</a>；
			<br>
			    2.按照模板的要求进行填写；<br>
			    3.不能新增、删除、修改任何一个单元格，否则无法导入；<br>
			    4.如果有任何疑问请联系管理员。</td>
		</tr>
		<tr>
			<td ><div id="import">选择文件</div></td>
		</tr>
		<tr>
			<td id="resultmessage">导入结果</td>
		</tr>
	</table>
</div>
<iframe id="ifile" style="display:none"></iframe>
<script type="text/javascript">
$(function(){
	var import_uploader = WebUploader.create({
		// 自动上传。
		auto : true,
		// swf文件路径
		swf : '/js/webuploader-0.1.5/Uploader.swf',
		// 文件接收服务端。
		server : '/pes/util/importExcelController/imp.do',
		// 选择文件的按钮。可选。
		pick : '#import'
	});
	import_uploader.on('uploadSuccess', function(file,res) {
		$("#resultmessage").empty().html("导入成功,你可以关闭该对话框也可以继续导入。");
		
	});
	import_uploader.on( 'uploadError', function( file ) {
		$("#resultmessage").empty().html("导入出错，请联系管理员解决。");
	});
	$("#importdialog").dialog({
		appendTo : "#importdiv",
		autoOpen : false,
		modal : false,
		height : 380,
		width : 400,
		buttons : {
			"关闭" : function() {
				$("#importdialog").dialog("close");
				$("#queryform").ajaxSubmit({
					target : "#tablelist",
				});
			}
		}
	});
});
function downloadtemplate(file){
	var url = "${ctx }/workschedule/activityrecord/"+file+"/downloadtemplate.do";
	document.getElementById("ifile").src=url;
}
</script>

