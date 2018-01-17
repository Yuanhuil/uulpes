<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<link rel="stylesheet" href="${ctx}/js/webuploader-0.1.5/style.css?3">
<style>
.scroll-pane {
	background: none;
	overflow: auto;
	width: 99%;
	float: left;
}

.scroll-content {
	background: none;
	width: 1200px;
	float: left;
}

.scroll-content-item {
	background: none;
	width: 110px;
	height: 90px;
	float: left;
	margin: 10px;
	font-size: 3em;
	line-height: 96px;
	text-align: center;
}

.scroll-bar-wrap {
	background: none;
	clear: left;
	padding: 0 4px 0 20px;
	margin: 0 -1px -1px -1px;
}

.scroll-bar-wrap .ui-slider {
	background: none;
	border: 0;
	height: 2em;
	margin: 0 auto;
}

.scroll-bar-wrap .ui-handle-helper-parent {
	position: relative;
	width: 100%;
	height: 100%;
	margin: 0 auto;
}

.scroll-bar-wrap .ui-slider-handle {
	top: .2em;
	height: 1.5em;
}

.scroll-bar-wrap .ui-slider-handle .ui-icon {
	margin: -8px auto 0;
	position: relative;
	top: 50%;
}
</style>
<div class="" id="dialog-form" title="编辑中心简介">
	<div class="buttonContent">
		<ul>
			<li><input type="button" id="save" class="button-mid blue"
				value="保存" /></li>
		</ul>
	</div>
	<form:form id="editForm" method="post"
		action="/pes/consultcenter/introduce/save.do"
		modelAttribute="introducePage">
		<div class="leftDiv1" style="margin: 5% auto">
			<es:showGlobalError commandName="introducePage" />

			<form:hidden path="introduce.id" />

			<div class="control-group">
				<form:label path="introduce.name" cssClass="control-label">名称</form:label>
				<form:input path="introduce.name" class="input01"
					value="${introducePage.introduce.name}" placeholder="小于50个字符" />
			</div>

			<div class="control-group">
				<form:label path="introduce.address" cssClass="control-label">地址</form:label>
				<form:input path="introduce.address"
					cssClass="validate[required,custom[name]]"
					value="${introducePage.introduce.address}" placeholder="小于50个字符" />
			</div>
			<div class="control-group">
				<form:label path="introduce.telphone" cssClass="control-label">电话</form:label>
				<form:input path="introduce.telphone"
					cssClass="validate[required,custom[name]]"
					value="${introducePage.introduce.telphone}" />
			</div>
			<div class="control-group">
				<form:label path="introduce.content" cssClass="control-label">简介</form:label>
				<form:textarea path="introduce.content" class="input01"
					cssClass="validate[required,custom[name]]"
					value="${introducePage.introduce.content}" style="width:173px" />
			</div>
			<div class="control-group">
				<form:label path="introduce.content" cssClass="control-label">时间</form:label>
			</div>
			<div class="control-group">
				<input class="checkbox" id="0" type="checkbox"
					${introducePage.introduceJobTimes[0].week<10?'checked':'a'} />
				<form:hidden path="introduceJobTimes[0].week"
					value="${introducePage.introduceJobTimes[0].week}" id="week0" />
				<form:hidden path="introduceJobTimes[0].id"
					value="${introducePage.introduceJobTimes[0].id}" />
				<form:label path="introduceJobTimes[0].week"
					cssClass="control-label" value="1">星期一</form:label>
				<form:select path="introduceJobTimes[0].starttimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
				--
				<form:select path="introduceJobTimes[0].endtimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
			</div>
			<div class="control-group">
				<input class="checkbox" id="1" type="checkbox"
					${introducePage.introduceJobTimes[1].week<10?'checked':'a'} />
				<form:hidden path="introduceJobTimes[1].week"
					value="${introducePage.introduceJobTimes[1].week}" id="week1" />
				<form:hidden path="introduceJobTimes[1].id"
					value="${introducePage.introduceJobTimes[1].id}" />
				<form:label path="introduceJobTimes[1].week"
					cssClass="control-label" value="2">星期二</form:label>
				<form:select path="introduceJobTimes[1].starttimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
				--
				<form:select path="introduceJobTimes[1].endtimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
			</div>
			<div class="control-group">
				<input class="checkbox" id="2" type="checkbox"
					${introducePage.introduceJobTimes[2].week<10?'checked':'a'} />
				<form:hidden path="introduceJobTimes[2].week"
					value="${introducePage.introduceJobTimes[2].week}" id="week2" />
				<form:hidden path="introduceJobTimes[2].id"
					value="${introducePage.introduceJobTimes[2].id}" />
				<form:label path="introduceJobTimes[2].week"
					cssClass="control-label" value="3">星期三</form:label>
				<form:select path="introduceJobTimes[2].starttimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
				--
				<form:select path="introduceJobTimes[2].endtimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
			</div>
			<div class="control-group">
				<input class="checkbox" id="3" type="checkbox"
					${introducePage.introduceJobTimes[3].week<10?'checked':'a'} />
				<form:hidden path="introduceJobTimes[3].week"
					value="${introducePage.introduceJobTimes[3].week}" id="week3" />
				<form:hidden path="introduceJobTimes[3].id"
					value="${introducePage.introduceJobTimes[3].id}" />
				<form:label path="introduceJobTimes[3].week"
					cssClass="control-label" value="4">星期四</form:label>
				<form:select path="introduceJobTimes[3].starttimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
				--
				<form:select path="introduceJobTimes[3].endtimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
			</div>
			<div class="control-group">
				<input class="checkbox" id="4" type="checkbox"
					${introducePage.introduceJobTimes[4].week<10?'checked':'a'} />
				<form:hidden path="introduceJobTimes[4].week"
					value="${introducePage.introduceJobTimes[4].week}" id="week4" />
				<form:hidden path="introduceJobTimes[4].id"
					value="${introducePage.introduceJobTimes[4].id}" />
				<form:label path="introduceJobTimes[4].week"
					cssClass="control-label" value="5">星期五</form:label>
				<form:select path="introduceJobTimes[4].starttimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
				--
				<form:select path="introduceJobTimes[4].endtimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
			</div>
			<div class="control-group">
				<input class="checkbox" id="5" type="checkbox"
					${introducePage.introduceJobTimes[5].week<10?'checked':'a'} />
				<form:hidden path="introduceJobTimes[5].week"
					value="${introducePage.introduceJobTimes[5].week}" id="week5" />
				<form:hidden path="introduceJobTimes[5].id"
					value="${introducePage.introduceJobTimes[5].id}" />
				<form:label path="introduceJobTimes[5].week"
					cssClass="control-label" value="6">星期六</form:label>
				<form:select path="introduceJobTimes[5].starttimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
				--
				<form:select path="introduceJobTimes[5].endtimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
			</div>
			<div class="control-group">
				<input class="checkbox" id="6" type="checkbox"
					${introducePage.introduceJobTimes[6].week<10?'checked':'a'} />
				<form:hidden path="introduceJobTimes[6].week"
					value="${introducePage.introduceJobTimes[6].week}" id="week6" />
				<form:hidden path="introduceJobTimes[6].id"
					value="${introducePage.introduceJobTimes[6].id}" />
				<form:label path="introduceJobTimes[6].week"
					cssClass="control-label" value="6">星期日</form:label>
				<form:select path="introduceJobTimes[6].starttimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
				--
				<form:select path="introduceJobTimes[6].endtimeid" class="input01"
					style="width:60px" items="${timeEnum}" itemLabel="info"
					itemValue="value"></form:select>
			</div>
		</div>
		<div style="width: 60%;float: left;">
			<div class="photoArea">
				<input hidden name="imageIds" id="coachAttachment_fileid"
					value="${imageIds}"
					size="${fn:length(introducePage.coachAttachments)}">
				<div class="scroll-pane ui-widget ui-widget-header ui-corner-all"  style="height: 121px;">
					<div class="scroll-content">
						<c:forEach items="${introducePage.coachAttachments}" var="m">
							<div class="scroll-content-item ui-widget-header img_del"
								id="${m.id}">
								<li class="photo" id="li${m.id}" style="margin: 0px 5px 5px 0px"
									onMouseOver="hiddenAndShow(${m.id},1)"><img
									src="/pes/util/commonAttachment/downloadFile.do?id=${m.fileid}"
									height="90px" width="110px" />
							</div>
	</li>
	</c:forEach>

</div>
<div class="scroll-bar-wrap ui-widget-content ui-corner-bottom">
	<div class="scroll-bar"></div>
</div>
</div>
<%--
			
					
					
				<ul>
					<%--<form:hidden path="coachAttachments[0].fileid" />
					<form:hidden path="coachAttachments[0].sort" value="1" />
					<form:hidden path="coachAttachments[0].id" />
					
					<c:forEach items="${introducePage.coachAttachments}" var="m">
						<li class="photo" style="margin: 0px 5px 5px 0px"><img class="img_del"
							src="/pes/util/commonAttachment/downloadFile.do?id=${m.fileid}"
							height="90px" width="110px" id="${m.id}" />
						</li>
					</c:forEach>
				</ul>--%>
</div>

<div id="wrapper" style="margin:130px auto;">
	<div id="container">
		<div id="uploader">
			<!--头部，相册选择和格式选择-->
			<div class="queueList">
				<div id="dndArea" class="placeholder">
					<div id="filePicker"></div>
					<p>或将照片拖到这里，单次最多可选300张</p>
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
<input type="submit" id="submitEdit" name="query" hidden value="查询" />
</form:form>
</div>
<script src="${ctx}/js/webuploader-0.1.5/upload.js?11"
	type="text/javascript"></script>
<script type="text/javascript">
	jQuery('.checkbox').bind('click', function() {
		var id = jQuery(this).attr("id");
		var week = parseInt(jQuery("#week" + id).attr("value"));
		if (week < 10) {
			week = week + 10;
			jQuery("#week" + id).attr("value", week);
		} else {
			week = week - 10;
			jQuery("#week" + id).attr("value", week);
		}
	});
	jQuery('#save').bind('click', function() {
		jQuery('#submitEdit').click();
	});

	$("#editForm").ajaxForm({
		target : "#content2"
	});
	//imageUpload("filePicker", "coachAttachment_fileid");

	$(document)
			.ready(
					function() {
						$(".scroll-content")
								.width(
										(${fn:length(introducePage.coachAttachments)} * 133)
												+ "px");
						imageUpload("coachAttachment_fileid");
						$(".img_del")
								.each(
										function(i) {
											var id = $(this).attr('id');
											if ($("#" + $(this).attr('id')
													+ "DIV").length == 0) {
												$(
														"#"
																+ $(this).attr(
																		'id')
																+ "DIV")
														.remove();
												var divObj = $("<div style='display:none'  onMouseOut='hiddenAndShow("
														+ id
														+ ",2);'  onclick=removePictore('"
														+ id
														+ "');><img src='${ctx}/themes/${sessionScope.user.theme}/images/delete.png' width='45' height='45'  /></div>");
												divObj.addClass("divX");
												divObj.attr("id", $(this).attr(
														"id")
														+ "DIV");
												divObj.attr("title", "删除该图片");
												divObj
														.css({
															left : $(this)
																	.position().left,
															top : $(this)
																	.position().top
														});

												$(this).append(divObj);

											}
										});
					});

	function removePictore(resourceCode) {
		if (confirm('图片将会直接删除')) {
			$.ajax({
				cache : true,
				type : "POST",
				url : "/pes/consultcenter/introduce/deleteImg.do?id="
						+ resourceCode,
				async : false,
				error : function(request) {
					layer.open({content:"删除失败"});
				},
				success : function(data) {
					layer.open({content:"删除成功"});
					var width = $(".scroll-content").width();
					if (width > 532) {
						$(".scroll-content").width((width - 133) + "px");
					} else {
						$(".scroll-content").width("532px");
					}
					$("#" + resourceCode).remove();
					$("#" + resourceCode + "DIV").remove();
				}
			});
		}
	}

	function hiddenAndShow(id, value) {
		if (value == 1) {
			$("#li" + id).hide();
			$("#" + id + "DIV").show();
		} else {
			$("#li" + id).show();
			$("#" + id + "DIV").hide();
		}

	}
</script>
