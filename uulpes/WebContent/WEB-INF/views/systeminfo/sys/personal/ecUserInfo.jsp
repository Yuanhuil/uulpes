<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link rel="stylesheet" href="${ctx}/js/webuploader-0.1.5/style.css?3">
<div id="mainContent">
	<div class="leftDiv1">
		<div class="userImage">
			<img
				src="${zp}"
				width="201" height="200" alt="" /> <input type="hidden" id='zp'
				>
		</div>
		<div id="wrapper" style="display:none">
			<div id="container">
				<div id="uploader">
					<!--头部，相册选择和格式选择-->
					<div class="queueList">
						<div id="dndArea" class="placeholder">
							<div id="filePicker"></div>
							<p>或将照片拖到这里，单次最多可选1张</p>
						</div>
					</div>
					<div class="statusBar" style="display:none;">
						<div class="progress">
							<span class="text">0%</span> <span class="percentage"></span>
						</div>
						<div class="info"></div>
						<div class="btns">
							<div id="filePicker2"></div>
							<div class="uploadBtn">开始上传</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<div class="rightDiv1">
		<table>

			<tr>
				<td><label class="name03">真实姓名</label></td>
				<td><input id="trueName" class="input-long" type="text"
					value="${teacher.xm}" disabled="disabled"></td>
			</tr>
			<tr>
				<td><label class="name03">工号</label></td>
				<td><input id="trueName" class="input-long" type="text"
					value="${teacher.gh}" disabled="disabled"></td>
			</tr>
			<tr>
				<td><label class="name03">身份证号</label></td>
				<td><input id="identifyCard" class="input-long" type="text"
					value="${teacher.sfzjh}" disabled="disabled"></td>
			</tr>
			<tr>
				<td><label class="name03">性别</label></td>
				<td><input id="organization" class="input-long" type="text"
					value="${teacher.xbm==1?'男':'女'}" disabled="disabled"></td>
			</tr>
			<tr>
				<td><label class="name03">现住址</label></td>
				<td><input id="xzz" class="input-long" type="text"
					value="${teacher.xzz}" disabled="disabled"></td>
			</tr>
			<tr>
				<td><label class="name03">户口所在地</label></td>
				<td><input id="hkszd" class="input-long" type="text"
					value="${teacher.hkszd}" disabled="disabled"></td>
			</tr>
			<tr>
				<td>参加工作年月</td>
				<td><input id="gzny" class="input-long  date" type="text"
					value="${teacher.gzny}" disabled="disabled"></td>
			</tr>
			<tr>
				<td><label class="name03">联系电话</label></td>
				<td><input id="lxdh" class="input-long" type="text"
					value="${teacher.lxdh}" disabled="disabled"></td>
			</tr>
			<tr>
				<td><label class="name03">电子信箱</label></td>
				<td><input id="dzxx" class="input-long" type="text"
					value="${teacher.dzxx}" disabled="disabled"></td>
			</tr>
			
			<tr>
				<td colspan="2" style="text-align: center;">
				<shiro:hasPermission name="personalcenter:personalinfo:update">
				    <input	type="button" id="bt_edit" class="btn02" value="编辑"
					onclick="toEdit();">
				</shiro:hasPermission>
					<input type="button" class="btn02"
					id="bt_save" value="保存" onclick="updatePersonleDetail();" hidden><input
					type="button" class="btn02" id="bt_cancel" hidden value="取消"
					onclick="toNoEdit();"></td>
			</tr>
		</table>
	</div>
</div>
<script src="${ctx}/js/webuploader-0.1.5/uploadPic.js?11"
	type="text/javascript"></script>
<script type="text/javascript">
	function updatePersonleDetail() {
		$.ajax({
			type : "POST",
			url : "../sys/updateEcUserInfo.do",
			data : {
				"xzz" : $("#xzz").val(),
				"hkszd" : $("#hkszd").val(),
				"gzny" : $("#gzny").val(),
				"lxdh" : $("#lxdh").val(),
				"dzxx" : $("#dzxx").val(),
			
				"zp" : $("#zp").val(),
				"id" : "${teacher.id}",
			},
			success : function(msg) {
				if (msg == "success"){
					layer.open({
						content : "更新成功"
					});
					toNoEdit();
				}
				else {
					layer.open({
						content : "更新失败"
					});
				}
			},
			error : function() {
				layer.open({
					content : "调用出现错误，删除失败"
				});
			}
		});
	}

	function resett() {
		$("#xzz").val("");
		$("#hkszd").val("");
		$("#gzny").val("");
		$("#lxdh").val("");
		$("#dzxx").val("");
		
	}

	function toNoEdit() {
		$("#xzz").attr('disabled', 'disabled');
		$("#hkszd").attr('disabled', 'disabled');
		$("#gzny").attr('disabled', 'disabled');
		$("#lxdh").attr('disabled', 'disabled');
		$("#dzxx").attr('disabled', 'disabled');
		
		$("#bt_edit").show();
		$("#bt_save").hide();
		$("#bt_cancel").hide();
		$("#wrapper").hide();
		
	}

	function toEdit() {

		$("#xzz").removeAttr('disabled');
		$("#hkszd").removeAttr('disabled');
		$("#gzny").removeAttr('disabled');
		$("#lxdh").removeAttr('disabled');
		$("#dzxx").removeAttr('disabled');
		
		$("#bt_edit").hide();
		$("#bt_save").show();
		$("#bt_cancel").show();
		$("#wrapper").show();
		imageUpload("zp");
	}

	$(".date").datepicker({ //绑定开始日期
		dateFormat : 'ymmdd', //更改时间显示模式
		showAnim : "slide", //显示日历的效果slide、fadeIn、show等
		changeMonth : true, //是否显示月份的下拉菜单，默认为false
		changeYear : true, //是否显示年份的下拉菜单，默认为false
		showWeek : true, //是否显示星期,默认为false
		showButtonPanel : true, //是否显示取消按钮，并含有today按钮，默认为false
		closeText : 'close', //设置关闭按钮的值
	});
	
	
</script>







