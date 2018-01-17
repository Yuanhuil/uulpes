<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form:form id="baseinfoform" modelAttribute="entity"
    action="${ctx}/systeminfo/sys/organization/school/${schoolid}/${orgtype}/update.do"
    method="post">
    <form:hidden path="orgid" />
    <div id="tabs_baseinfo">
        <njpes:showMessage />
        <div class="filtercontent-org">
            <ul>
                <li class="school-size org-schoolinfo-li">${entity.xxmc }</li>
                <li class="org-schoolinfo-li schoolinfo-size">${bxlx[0].name}</li>
                <li class="org-schoolinfo-li"><input type="button" class="org-edit"
                    value="编辑" onclick="orgEdit()" /></li>
            </ul>
        </div>
        <div class="filterContent">
            <ul>
                <li class="org-tel" class="org-onecolumn">
                    <span class='org-info-label'>学校名称</span>
                    <form:input path="xxmc" cssClass="input-onecolumn validate[required]" disabled="true" />
                </li>

                <li class="org-tel" class="org-onecolumn"><span
                    class='org-info-label'>学校代码</span> <form:input
                        path="xxdm"
                        cssClass="input-onecolumn validate[required]"
                        disabled="true" /></li>

                <li class="org-tel" class="org-twocolumn"><span
                    class='org-info-label'>负责人</span> <form:input
                        path="xzxm"
                        cssClass="input-onecolumn validate[required]"
                        disabled="true" /></li>

            </ul>
        </div>
        <div class="filterContent">
            <ul>
                <li class="org-tel" class="org-twocolumn"><span
                    class='org-info-label'>联系电话</span> <form:input
                        path="lxdh"
                        cssClass="input-onecolumn validate[required]"
                        disabled="true" /></li>
                <c:if
                    test="${entity.xxbxlxm=='211' or entity.xxbxlxm=='218' or entity.xxbxlxm=='219' or entity.xxbxlxm=='221'or entity.xxbxlxm=='228'or entity.xxbxlxm=='229'or entity.xxbxlxm=='312'or entity.xxbxlxm=='345'}">
                    <li class="org-onecolumn"><span
                        class='org-info-label'>小学学制</span> <form:select
                            id="xxxz" path="xxxz"
                            cssClass="select-stulength" disabled="true">
                            <form:option value="5">五年制</form:option>
                            <form:option value="6">六年制</form:option>
                        </form:select></li>
                </c:if>
                <c:if
                    test="${entity.xxbxlxm=='311' or  entity.xxbxlxm=='312' or entity.xxbxlxm=='321' or entity.xxbxlxm=='329' or entity.xxbxlxm=='331'or entity.xxbxlxm=='332' or entity.xxbxlxm=='341' or entity.xxbxlxm=='345'}">
                    <li class="org-twocolumn"><span
                        class='org-info-label'>初中学制</span> <form:select
                            id="czxz" path="czxz"
                            cssClass="select-stulength" disabled="true">
                            <form:option value="3">三年制</form:option>
                            <form:option value="4">四年制</form:option>
                        </form:select></li>
                </c:if>
                <c:if
                    test="${entity.xxbxlxm=='341'  or  entity.xxbxlxm=='342'  or entity.xxbxlxm=='345'  or entity.xxbxlxm=='349' or entity.xxbxlxm=='351' or entity.xxbxlxm=='352'}">
                    <li class="org-twocolumn"><span
                        class='org-info-label'>高中学制</span> <form:select
                            id="gzxz" path="gzxz"
                            cssClass="select-stulength" disabled="true">
                            <form:option value="3">三年制</form:option>
                        </form:select></li>
                </c:if>
            </ul>
        </div>
        <div class="filterContent">
            <ul>
                <li class="org-onecolumn"><span
                    class='org-info-label'>电子信箱 </span> <form:input
                        path="dzxx"
                        cssClass="input-onecolumn validate[custom[email]]"
                        disabled="true" placeholder="x123@126.com" /></li>
                <li class="org-onecolumn"><span
                    class='org-info-label'>主页地址</span> <form:input
                        path="zydz"
                        cssClass="input-onecolumn validate[custom[url]]"
                        disabled="true"
                        placeholder="http://www.ludus.cn" /></li>
            </ul>
        </div>
        <div class="filtercontent-address" id="schooldzdiv">
            <ul>
                <li class="org-onecolumn"><span class="org-info-label">办学地址</span><form:select
                        path="org.provinceid" id="scty"
                        cssClass="prov input-scty validate[required]"
                        disabled="true"></form:select> <form:select
                        path="org.cityid" id="sqcty"
                        cssClass="city input-sqcty validate[required]"
                        disabled="true"></form:select> <form:select
                        path="org.countyid"
                        cssClass="dist input-sqcty validate[required]"
                        disabled="true"></form:select> <form:select
                        path="org.townid"
                        cssClass="street input-sqcty validate[required]"
                        disabled="true"></form:select> <form:input
                        id="xxdzo" path="xxdz"
                        cssClass="input_153 validate[required]"
                        disabled="true" placeholder="详细地址" /></li>
            </ul>
        </div>
        <div class="filterContent">
            <ul>
                <li class="org-onecolumn"><span class="org-info-label">简介</span><form:textarea id="jj"
                        path="org.introduce"
                        cssClass="validate[required]"
                        style="width:715px;height:200px;margin-top: -20px;margin-left: 66px;"
                        disabled="true"></form:textarea>
                </li>
            </ul>
        </div>
        <div class="buttonContent">

            <input class="org-save" type="button" id="baseinfo_modify"
                value="提交" onclick="submitbaseinfo(${locked});" />
        </div>
    </div>
    </div>
</form:form>
<script type="text/javascript">
$(function(){
    $("#baseinfo_modify").hide();
    var xxxz="${entity.xxxz}";
    var czxz="${entity.czxz}";
    if(xxxz==6)
        $("#xxxz").val(6);
    if(xxxz==5)
        $("#xxxz").val(5);
    if(czxz==4)
        $("#czxz").val(4);
    if(czxz==3)
        $("#czxz").val(3);

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
        $("#showmessage").empty().html("已上传成功");
    });
    $("#schooldzdiv").citySelect({
        prov:"${entity.org.provinceid}",
        city:"${entity.org.cityid}",
        dist:"${entity.org.countyid}",
        street:"${entity.org.townid}",
        nodata:"none"
    });
});

function submitbaseinfo(locked){
    if(locked=='1')
        layer.open({
            content : '信息修改已锁定，如需修改请联系上级主管部门'
        });
    else
        $("#baseinfoform").submit();
        $("#baseinfo_modify").hide();
}

function orgEdit(){
   $("#xxmc").removeAttr('disabled');
   $("#xxdm").removeAttr('disabled');
   $("#xzxm").removeAttr('disabled');
   $("#lxdh").removeAttr('disabled');
   $("#xxxz").removeAttr('disabled');
   $("#czxz").removeAttr('disabled');
   $("#dzxx").removeAttr('disabled');
   $("#zydz").removeAttr('disabled');
   $("#xxdzo").removeAttr('disabled');
   $("#scty").removeAttr('disabled');
   $("#sqcty").removeAttr('disabled');
   $("#jj").removeAttr('disabled');
   $("#baseinfo_modify").show();
}
</script>