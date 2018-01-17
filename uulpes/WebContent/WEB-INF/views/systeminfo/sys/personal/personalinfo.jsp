<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link rel="stylesheet" href="${ctx}/js/webuploader-0.1.5/style.css?3">
<div id="mainContent">
    <div class="leftDiv1">
        <div class="userImage">
            <img src="${zp}" width="201" height="200" alt="" />
        </div>
        <div id="wrapper" style="display: none">
            <div id="container">
                <div id="uploader">
                    <!--头部，相册选择和格式选择-->
                    <div class="queueList">
                        <div id="dndArea" class="placeholder">
                            <div id="filePicker"></div>
                            <p>或将照片拖到这里，单次最多可选1张</p>
                        </div>
                    </div>
                    <div class="statusBar" style="display: none;">
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
        <ul>
            <input id="id" type="hidden" value="${parent.id}">
            <li>
                <label class="name03">真实姓名</label>
                <input id="trueName" class="input-long" type="text" value="${parent.cyxm}" disabled="disabled">
            </li>
            <li>
                <label class="name03">监&nbsp;&nbsp;护&nbsp;&nbsp;人</label>
                <select id="sfjhr" class="select_160">
                    <option value="0" ${parent.sfjhr==0?'selected':''}>是</option>
                    <option value="1" ${parent.sfjhr==1?'selected':''}>否</option>
                </select>
            </li>
            <li>
                <label class="name03">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</label>
                <select id="xbm" class="select_160">
                    <option value="1" ${parent.xbm==1?'selected':''}>男</option>
                    <option value="2" ${parent.xbm==2?'selected':''}>女</option>
                </select>
            </li>
            <li>
                <label class="name03">民&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;族</label>
                <input id="mzm" class="input-long" type="text" value="${parent.mzm}">
            </li>
            <li>
                <label class="name03">工作单位</label>
                <input id="cygzdw" class="input-long" type="text" value="${parent.cygzdw}">
            </li>
            <li>
                <label class="name03">电子信箱</label>
                <input id="dzxx" class="input-long" type="text" value="${parent.dzxx}">
            </li>
            <li>
                <label class="name03">联系地址</label>
                <input id="lxdz" class="input-long" type="text" value="${parent.lxdz}">
            </li>
            <li>
                <label class="name03">联系电话</label>
                <input id="sjhm" class="input-long" type="text" value="${parent.sjhm}">
            </li>
            <li>
                <shiro:hasPermission name="personalcenter:personalinfo:update">
                    <input type="button" id="bt_edit" class="btn02" value="编辑" onclick="toEdit();"
                        style="margin-left: 100px">
                </shiro:hasPermission>
                <input type="button" class="btn02" id="bt_save" value="保存" onclick="updatePersonleDetail();" hidden>
                <input type="button" class="btn02" id="bt_cancel" hidden value="取消" onclick="toNoEdit();">
            </li>
        </ul>
    </div>
</div>
<script src="${ctx}/js/webuploader-0.1.5/uploadPic.js?11" type="text/javascript"></script>
<script type="text/javascript">
    function updatePersonleDetail() {
        $.ajax({
            type : "POST",
            url : "../sys/updatePersonDetail.do",
            data : {
                "id" : $("#id").val(),
                "sfjhr" : $("#sfjhr").val(),
                "mzm" : $("#mzm").val(),
                "cygzdw" : $("#cygzdw").val(),
                "dzxx" : $("#dzxx").val(),
                "lxdz" : $("#lxdz").val(),
                "trueName" : $("#trueName").val(),
                "xbm" : $("#xbm").val(),
                "sjhm" : $("#sjhm").val(),
            },
            success : function(msg) {
                if (msg == "success")
                    toNoEdit();
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

    function toNoEdit() {
        location.reload();
    }

    function toEdit() {
        $("#bt_edit").hide();
        $("#bt_save").show();
        $("#bt_cancel").show();
        imageUpload("zp");
    }
</script>
