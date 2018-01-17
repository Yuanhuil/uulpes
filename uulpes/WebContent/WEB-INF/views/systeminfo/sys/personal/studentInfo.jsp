<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link rel="stylesheet" href="${ctx}/js/webuploader-0.1.5/style.css?3">
<style type="text/css">
//
隐藏日的显示div
.ui-datepicker-calendar {
    display: none;
}
</style>
<div id="mainContent">
    <div>
        <table id="studentMsgUpdate">
            <tr>
                <td>
                    <label>真实姓名</label>
                </td>
                <td>
                    <input id="xm" class="input-long" type="text" value="${student.xm}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td>
                    <label>学号</label>
                </td>
                <td>
                    <input id="xh" class="input-long" type="text" value="${student.xh}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td>
                    <label>性别</label>
                </td>
                <td>
                    <input id="gender" class="input-long" type="text" value="${student.xbm==1?'男':'女'}" disabled="">
                </td>
            </tr>
            <tr>
                <td>
                    <label>身份证号</label>
                </td>
                <td>
                    <input id="sfzjh" class="input-long" type="text" value="${student.sfzjh}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td>
                    <label>现住地址</label>
                </td>
                <td>
                    <input id="xzz" class="input-long" type="text" value="${student.xzz}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td>
                    <label>户口所在地</label>
                </td>
                <td>
                    <input id="hkszd" class="input-long" type="text" value="${student.hkszd}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td>
                    <label>联系电话</label>
                </td>
                <td>
                    <input id="lxdh" class="input-long" type="text" value="${student.lxdh}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td>
                    <label>电子信箱</label>
                </td>
                <td>
                    <input id="dzxx" class="input-long" type="text" value="${student.dzxx}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center;">
                    <input type="button" class="btn08" id="bt_save" value="保存" onclick="updatePersonleDetail();">
                    <input type="button" class="btn08" id="bt_cancel" value="取消" onclick="goback()">
                </td>
            </tr>
        </table>
    </div>
    <div class="bigimgdiv">
        <div class="viewImg" style="margin-left: 610px; margin-top: 70px;">
            <img src="${zp}" width="201" height="200" alt="" />
            <input type="hidden" id='zp'>
        </div>
        <div id="wrapper" style="display: none; width: 300px; margin-left: 440px;">
            <div id="uploader">
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
        <ul>
            <li class="lidiv">
                <input type="button" class="btn05" value="修改背景信息"
                    onclick="editBjxx('${ctx}/systeminfo/sys/user/student/${student.id }/editStudentBjxx/personalCenter.do');">
                <shiro:hasPermission name="personalcenter:personalinfo:update">
                    <input type="button" id="bt_edit" class="btn06" value="编辑" onclick="toEdit();">
                </shiro:hasPermission>
            </li>
        </ul>
        <ul>
            <li>
                <input type="button" class="btn07" value="上传图片" onclick="uploadImg()" id="imgshow" />
            </li>
        </ul>
     </div>
     <div id="studentMsgView">
        <ul>
            <li class="li01">
                <label class="name06">${student.xm}</label>
        </ul>
        <ul class="ul01">
            <li class='msg-alignment'>学号</li>
            <li>：&nbsp;${student.xh}</li>
        </ul>
        <ul class="ul02">
            <c:if test="${student.xbm == 1}">
                <li class='msg-alignment'>性别</li>
                <li>：&nbsp;男</li>
            </c:if>
            <c:if test="${student.xbm == 2}">
                <li class='msg-alignment'>性别</li>
                <li>：&nbsp;女</li>
            </c:if>
        </ul>
        <ul class="ul02">
            <li class='msg-alignment'>身份证号</li>
            <li>：&nbsp;${student.sfzjh}</li>
        </ul>
        <ul class="ul02">
            <li class='msg-alignment'>户口所在地</li>
            <li>：&nbsp;${student.hkszd}</li>
        </ul>
        <ul class="ul02">
            <li class='msg-alignment'>现住地址</li>
            <li>：&nbsp;${student.xzz}</li>
        </ul>
        <ul class="ul02">
            <li class='msg-alignment'>联系电话</li>
            <li>：&nbsp;${student.lxdh}</li>
        </ul>
        <ul class="ul02">
            <li class='msg-alignment'>电子信箱</li>
            <li>：&nbsp;${student.dzxx}</li>
        </ul>
    </div>
</div>
<script src="${ctx}/js/webuploader-0.1.5/uploadPic.js?11" type="text/javascript"></script>
<script type="text/javascript">
    $(function() {
        $("#studentMsgUpdate").hide();
    });
    function updatePersonleDetail() {
        $.ajax({
            type : "POST",
            url : "../sys/updateStudentInfo.do",
            data : {
                "xzz" : $("#xzz").val(),
                "hkszd" : $("#hkszd").val(),
                "dszybz" : $('input[name="dszybz"]:checked').val(),
                "lxdh" : $("#lxdh").val(),
                "dzxx" : $("#dzxx").val(),
                "rxny" : $("#rxny").val(),
                "id" : "${student.id}",
            },
            success : function(msg) {
                if (msg == "success") {
                    layer.open({
                        content : "更新成功"
                    });
                    toNoEdit();
                } else {
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
        $("#dszybz").val("");
        $("#lxdh").val("");
        $("#dzxx").val("");
        $("#rxny").val("");
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

    function toNoEdit() {
        $("#xzz").attr('disabled', 'disabled');
        $("#hkszd").attr('disabled', 'disabled');
        $("#dszybz").attr('disabled', 'disabled');
        $("#lxdh").attr('disabled', 'disabled');
        $("#dzxx").attr('disabled', 'disabled');
        $("#rxny").attr('disabled', 'disabled');
        $("#bt_edit").show();
        $("#bt_save").hide();
        $("#bt_cancel").hide();
        $("#wrapper").hide();
    }

    function toEdit() {
        $(".bigimgdiv").css('margin-top', '-468px');
        $("#studentMsgUpdate").show();
        $("#studentMsgView").hide();
        $("#imgshow").hide();
        $("#xzz").removeAttr('disabled');
        $("#hkszd").removeAttr('disabled');
        $("#dszybz").removeAttr('disabled');
        $("#lxdh").removeAttr('disabled');
        $("#dzxx").removeAttr('disabled');
        $("#rxny").removeAttr('disabled');
        $("#bt_edit").hide();
        $("#bt_save").show();
        $("#bt_cancel").show();
        $("#wrapper").show();
        imageUpload("zp");
    }

    function editBjxx(h) {
        $('#mainContent').load(h);
    }

    function uploadImg() {
        $(".viewImg").css('margin-left', '60px');
        $("#wrapper").css('margin-top', '-240px');
        $("#studentMsgView").hide();
        $("#imgshow").hide();
        $("#xzz").removeAttr('disabled');
        $("#hkszd").removeAttr('disabled');
        $("#gzny").removeAttr('disabled');
        $("#lxdh").removeAttr('disabled');
        $("#dzxx").removeAttr('disabled');
        $("#zyrkxd").removeAttr('disabled');
        $("#bt_edit").hide();
        $("#bt_save").show();
        $("#bt_cancel").show();
        $("#wrapper").show();
        $(".lidiv").hide();
        imageUpload("zp");
    }
</script>
