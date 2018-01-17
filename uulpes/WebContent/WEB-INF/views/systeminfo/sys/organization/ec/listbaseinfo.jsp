<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="baseinfoform" modelAttribute="entity"
	action="${ctx}/systeminfo/sys/organization/ec/${orgid}/${orgtype}/update.do"
	method="post">
	<form:hidden path="orgid"/>
	<div id="tabs_baseinfo">
		<div class="filtercontent-org">
            <ul>
                <li class="org-schoolinfo-li"><input type="button" class="org-edit"
                    value="编辑" onclick="orgEdit()" /></li>
            </ul>
        </div>
		<div class="filterContent">
			<ul>
				<li>
				<c:if test="${!empty editorgname }">
					<label class="name03">机构名称</label> <form:input path="jwmc"
						cssClass="input_160 validate[required]" disabled="true"/>
				</c:if>
				<c:if test="${empty editorgname }">
					<label class="name03">机构名称</label> <form:input path="jwmc"
						cssClass="input_160 validate[required]" disabled="disabled"/>
				</c:if>
				</li>
				<li><label class="name03">机构代码</label> <form:input path="jwdm"
						cssClass="input_160 validate[required]" disabled="true" /></li>
				<li><label class="name03">负责人</label> <form:input path="jwfzr"
						cssClass="input_160 validate[required]" disabled="true"/></li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><label class="name03">主页地址</label> <form:input path="zydz"
						cssClass="input_160 validate[custom[url]]" disabled="true"/></li>
				<li><label class="name03">电子信箱</label> <form:input path="dzxx"
						cssClass="input_160 validate[custom[email]]" disabled="true"/></li>
				<li><label class="name03">单位级别</label> <form:select path="org.orgLevel"
						cssClass="input_160 validate[required]" disabled="true">
						<form:options items="${orglevelist}" itemLabel="name" itemValue="id"/>
						</form:select></li>
			</ul>
		</div>
		<div class="filterContent1" id="dzdiv">
			<ul>
				<li><label class="name03">机构地址</label> 
				<form:select path="org.provinceid" cssClass="prov input_140 validate[required]" style="float:left;margin-left:10px;"></form:select>
				<form:select path="org.cityid" cssClass="city input_140 validate[required]" style="float:left;margin-left:10px;"></form:select>
				<form:select path="org.countyid" cssClass="dist input_140 validate[required]" style="float:left;margin-left:10px;"></form:select>
				<form:select path="org.townid" cssClass="street input_140 validate[required]" style="float:left;margin-left:10px;"></form:select>
				</li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				<li><label class="name03">详细地址</label> <form:input path="jwdz" cssClass="input_160 validate[required]" disabled="true"/></li>
			</ul>
		</div>
		<div>
			<ul>
				<li><label class="name03">简介</label>
				<form:textarea path="org.introduce" cssClass="validate[required]" style="width:500px;height:200px;"></form:textarea>
				</li>
			</ul>
		</div>
		<div class="buttonContent">
		<input type="button" class="org-save" id="baseinfo_modify" value="提交" onclick="submitbaseinfo(${locked});" />
		</div>
	</div>
</form:form>
<script>
$(function(){
var uploader = WebUploader.create({
	// 自动上传。
	auto : true,
	// swf文件路径
	swf : '/js/webuploader-0.1.5/Uploader.swf',
	// 文件接收服务端。
	server : '/pes/workschedule/jobattachment/save.do',
	// 选择文件的按钮。可选。
	pick : '#picker',
	accept: {
        title: 'Images',
        extensions: 'gif,jpg,jpeg,bmp,png',
        mimeTypes: 'image/*'
    }

});
uploader.on('uploadSuccess', function(file,res) {
	debugger;
	var list = res._raw;
	var element = JSON.parse(list)[0];
	$("#org\\.imageurl").attr("value",element.uuid);
	$("#showmessage").html("已上传成功");
});
$("#dzdiv").citySelect({
	prov:"${entity.org.provinceid}",
	city:"${entity.org.cityid}",
	dist:"${entity.org.countyid}",
	street:"${entity.org.townid}",
	defaultprov:"${entity.org.provinceid}",
	nodata:"none"
}); 
});
function submitbaseinfo(locked){
	if(locked=='1')
		layer.open({
			content : '信息修改已锁定，如需修改请联系市教委'
		});
	else
		$("#baseinfoform").submit();
}

function orgEdit(){
	   $("#jwmc").removeAttr('disabled');
	   $("#jwdm").removeAttr('disabled');
	   $("#jwfzr").removeAttr('disabled');
	   $("#zydz").removeAttr('disabled');
	   $("#dzxx").removeAttr('disabled');
	   $("#jwdz").removeAttr('disabled');
	   $("#baseinfo_modify").show();
	}
</script>