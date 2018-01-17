<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link rel="stylesheet" href="${ctx}/js/webuploader-0.1.5/style.css?3">
<div id="mainContent">
    <div>
        <table id="teacherMsgUpdate">
            <tr>
                <td style="text-align: right">真实姓名</td>
                <td>
                    <input id="trueName" type="text" value="${teacher.xm}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td style="text-align: right">工号</td>
                <td>
                    <input id="trueName" type="text" value="${teacher.gh}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td style="text-align: right">身份证号</td>
                <td>
                    <input id="identifyCard" type="text" value="${teacher.sfzjh}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td style="text-align: right">性别</td>
                <td>
                    <input id="organization" type="text" value="${teacher.xbm==1?'男':'女'}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td style="text-align: right">现住址</td>
                <td>
                    <input id="xzz" type="text" value="${teacher.xzz}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td style="text-align: right">户口所在地</td>
                <td>
                    <input id="hkszd" type="text" value="${teacher.hkszd}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td style="text-align: right">参加工作年月</td>
                <td>
                    <input id="gzny date" type="text" value="${teacher.gzny}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td style="text-align: right">联系电话</td>
                <td>
                    <input id="lxdh" type="text" value="${teacher.lxdh}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td style="text-align: right">电子信箱</td>
                <td>
                    <input id="dzxx" type="text" value="${teacher.dzxx}" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td style="text-align: right">主要任课学段</td>
                <td>
                    <input id="zyrkxd" type="text" value="${teacher.zyrkxd}" disabled="disabled">
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
                    onclick="editBjxx('${ctx}/systeminfo/sys/user/teacher/${teacher.id }/editTeacherBjxx/personalCenter.do');">
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
    <div id="teacherMsgView">
        <ul>
            <li class="li01">
                <label class="name06">真实姓名:${teacher.xm}</label>
        </ul>
        <ul class="ul01">
            <c:if test="${teacher.xbm == 1}">
                <li>
                    <label>性别:男</label>
                </li>
            </c:if>
            <c:if test="${teacher.xbm == 2}">
                <li>
                    <label>性别:女</label>
                </li>
            </c:if>
        </ul>
        <ul class="ul02">
            <li>
                <label>身份证号:${teacher.sfzjh}</label>
            </li>
        </ul>
        <ul class="ul02">
            <li>
                <label>户口所在地:${teacher.hkszd}</label>
            </li>
        </ul>
        <ul class="ul02">
            <li>
                <label>现住地址:${teacher.xzz}</label>
            </li>
        </ul>
        <ul class="ul02">
            <li>
                <label>联系电话:${teacher.lxdh}</label>
            </li>
        </ul>
        <ul class="ul02">
            <li>
                <label>电子信箱:${teacher.dzxx}</label>
            </li>
        </ul>
        <div class='teacher-four'>
        </div>
        <ul class="ul02">
            <li>
                <label>工号:${teacher.gh}</label>
            </li>
        </ul>
        <ul class="ul02">
            <li>
                <label>主要任课学段:${teacher.zyrkxd}</label>
            </li>
        </ul>
    </div>
    <%-- <input id="trueName" type="text" value="${teacher.xm}" disabled="disabled"> --%>
</div>
<script src="${ctx}/js/webuploader-0.1.5/uploadPic.js?11" type="text/javascript"></script>
<script type="text/javascript">
    $(function() {
        $("#teacherMsgUpdate").hide();
    });
    function updatePersonleDetail() {
        $.ajax({
            type : "POST",
            url : "../sys/updateTeacherInfo.do",
            data : {
                "xzz" : $("#xzz").val(),
                "hkszd" : $("#hkszd").val(),
                "gzny" : $("#gzny").val(),
                "lxdh" : $("#lxdh").val(),
                "dzxx" : $("#dzxx").val(),
                "zyrkxd" : $("#zyrkxd").val(),
                "zp" : $("#zp").val(),
                "id" : "${teacher.id}",
            },
            success : function(msg) {
                if (msg == "success") {
                    layer.open({
                        content : "更新成功"
                    });
                    goback();
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
        $("#gzny").val("");
        $("#lxdh").val("");
        $("#dzxx").val("");
        $("#zyrkxd").val("");
    }

    function goback() {
        $("#mainContent").load('${ctx}/systeminfo/sys/queryPersonDetail.do');
    }

    function toEdit() {
        $(".bigimgdiv").css('margin-top', '-468px');
        $("#teacherMsgUpdate").show();
        $("#teacherMsgView").hide();
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
        $(".lidiv").hide();
        imageUpload("zp");
    }

    function uploadImg() {
        $(".viewImg").css('margin-left', '60px');
        $("#wrapper").css('margin-top', '-240px');
        $("#teacherMsgView").hide();
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

    $(".date").datepicker({ //绑定开始日期
        dateFormat : 'ymmdd', //更改时间显示模式
        showAnim : "slide", //显示日历的效果slide、fadeIn、show等
        changeMonth : true, //是否显示月份的下拉菜单，默认为false
        changeYear : true, //是否显示年份的下拉菜单，默认为false
        showWeek : true, //是否显示星期,默认为false
        showButtonPanel : true, //是否显示取消按钮，并含有today按钮，默认为false
        closeText : 'close', //设置关闭按钮的值
    });

    function editBjxx(h) {
        $('#mainContent').load(h);
    }
</script>
