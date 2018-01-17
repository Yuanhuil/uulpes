<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link rel="stylesheet" href="/themes/theme1/css/modifypass.css"
    type="text/css" />
<div id="mainContent">
    <ul class='etabs'>
        <li class='tab'><a href="#tabs1-html">修改密码</a></li>
        <li class='tab'><a href="#tabs1-js">修改帐号</a></li>
    </ul>
    <div id="tabs1-html">
        <div class="imgmove">
            <img
                src="${ctx}/themes/${sessionScope.user.theme}/images/pwd.png"
                width="128" height="128" alt="" />
        </div>
        <input type="hidden" id="dogid" name="dogid" value="1"></input>
        <div class="imgmove">
            <ul class="ul-center">
                <li class="li-down"><label class="account-info-label">原始密码</label>
                    <input id="oraginalPass" class="input-long" type="password">
                </li>
                <li class="li-down"><label class="account-info-label">新密码</label>
                    <input id="newPass" class="input-long"
                    type="password"></li>
                <li class="li-down"><label class="account-info-label">重复密码</label>
                    <input id="newPassAgain" class="input-long"
                    type="password"></li>
                <li><input type="button" class="save-pwd"
                    value="保存" onclick="modifyPass()"></li>
            </ul>
        </div>
    </div>
    <div id="tabs1-js">
        <div class="imgmove">
            <img
                src="${ctx}/themes/${sessionScope.user.theme}/images/pwd.png"
                width="128" height="128" alt="" />
        </div>
        <div class="imgmove">
            <ul class="ul-center">
                <li class="li-down"><label class="account-info-label">新用户名</label>
                    <input id="username" class="input-long" type="text"
                        onblur="changeNickNameTip();checkUsername()" onclick="changeNickNameTip(true)">
                </li>
                <li class="li-down" id="hint" style="display:none"><p class="account-info-hint">6-30个字符，支持英文、数字、"_"、"."或"@"</p></li>
                <li class="li-down"><label class="account-info-label">密码</label>
                    <input id="newPass1" class="input-long" type="password"></li>
                <li><input type="button" class="save-pwd" value="保存" onclick="updateUsername()"></li>
            </ul>
        </div>
    </div>

</div>
<script type="text/javascript">
    $('#mainContent').easytabs();
    var iddoglogin = '${iddoglogin}';
    var dogentity, usrName, objAuth;
    var scope = "<dogscope/>\n";
    function checkdog() {
        objAuth = getAuthObject();
        $.ajax({
            url : '/pes/doginfo/getconfiginfo.do',
            type : 'post',
            cache : false,
            async : false,
            dataType : 'json',
            success : function(d) {
                dogentity = d;
                authCode = d.authCode;
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                layer.open({
                    content : "获取加密狗信息出错，请联系管理员。"
                });
                return false;
            }

        });
        stat = objAuth.Open(scope, authCode);
        if (stat === 7) {//没有发现加密狗，可能是不需要加密狗登陆的用户
            layer.open({
                content : "没有发现加密狗插入"
            });
            return false;
        } else if (stat != 0) {
            layer.open({
                content : "探查加密狗错误!错误码[" + stat + "]"
            });
            return false;
        } else {//正常逻辑
            stat = objAuth.GetDogID();
            if (stat != 0) {
                objAuth.Close();
                layer.open({
                    content : "加密狗不能获取重要信息,请联系管理员,错误码[" + stat + "]"
                });
                return false;
            }
            dogID = objAuth.DogIdStr;
            $("#dogid").attr("value", dogID);
        }
    }
    if (typeof (iddoglogin) != "undefined" && iddoglogin == '1') {
        checkdog();
    }

    function resett() {
        $("#oraginalPass").val("");
        $("#newPass").val("");
        $("#newPassAgain").val("");
    }

    function modifyPass() {
        var bool = validateNewPass();
        if (!bool)
            return;
        sendNewPassToBack();
    }

    function validateNewPass() {
        if (($("#newPass").val()) != ($("#newPassAgain").val())) {
            layer.open({
                content : "两次输入新密码不一致！"
            });
            $("#newPass").val("");
            $("#newPassAgain").val("");
            return false;
        }
        if (($("#oraginalPass").val() == undefined)
                || ($("#newPass").val() == undefined)
                || ($("#newPassAgain").val() == undefined))
            return false;
        return true;

    }

    function sendNewPassToBack() {
        var pwd = $("#newPass").val();
        var oldPwd = $("#oraginalPass").val();
        if (pwd.length <= 4) {
            alert("密码长度不能少于四位！！！")
            return false;
        } else {
            $.ajax({
                type : "POST",
                url : "../sys/updatePersonPassword.do",
                data : {
                    "sourcePassword" : $("#oraginalPass").val(),
                    "nowPassword" : $("#newPass").val(),
                    "dogid" : $("#dogid").val()
                },
                success : function(msg) {
                    debugger;
                    if (msg == "notEqual") {
                        layer.open({
                            content : "您输入的初始密码错误"
                        });
                    } else if (msg == "needdog") {
                        layer.open({
                            content : "请插入密码狗修改密码"
                        });
                    } else if (msg == "success_dog") {
                        debugger;
                        var stat = objAuth.VerifyUserPin(oldPwd);
                        if (stat != 0) {
                            layer.open({
                                content : "原始密码错误,错误码[" + stat + "]"
                            });
                            return false;
                        }
                        stat = objAuth.ChangeUserPin(pwd);
                        objAuth.Close();
                        layer.open({
                            content : "更改密码成功，下次登录请使用新密码!"
                        });
                        //location.href = "../../";
                    } else {
                        layer.open({
                            content : "更改密码成功，下次登录请使用新密码!"
                        });
                        //location.href = "../../";
                    }
                    resett();
                },
                error : function() {
                    layer.open({
                        content : "调用出现错误，更改密码失败"
                    });
                }
            });
        }
    }

    function checkUsername() {
        if (!userName()) {
            return;
        }
        $.ajax({
            type : "POST",
            url : "../sys/checkPersonUserNameExist.do",
            data : {
                "username" : $("#username").val(),
            },
            success : function(msg) {
                if (msg == "true") {
                    layer.open({
                        content : "用户名已存在"
                    });
                }
            },
            error : function() {
                layer.open({
                    content : "调用出现错误"
                });
            }
        });

    }
    function updateUsername() {
        if (!userName()) {
            return;
        }
        if ($("#newPass1").val() == undefined || $("#newPass1").val() == "") {
            layer.open({
                content : "密码不能为空"
            });
        }
        $.ajax({
            type : "POST",
            url : "../sys/updatePersonUserName.do",
            data : {
                "username" : $("#username").val(),
                "password" : $("#newPass1").val()
            },
            success : function(msg) {
                if (msg == "exist") {
                    layer.open({
                        content : "用户名已存在"
                    });
                } else if (msg == "notEqual") {
                    layer.open({
                        content : "您输入的密码错误"
                    });
                } else if (msg == "success") {
                    layer.open({
                        content : "更改用户名成功，请重新登录"
                    });
                    setTimeout("location.href = '../../'",1000);
                }
                resett();
            },
            error : function() {
                layer.open({
                    content : "调用出现错误，更改用户名失败"
                });
            }
        });

    }

    function userName() {
        var s = $("#username").val();
        if (s == undefined || s == "") {
            layer.open({
                content : "新用户名不能为空"
            });
            return false;
        }
        if (s.length < 6) {
            layer.open({
                content : "新用户名长度不能小于6"
            });
            return false;
        }
        var regu = "^[0-9a-zA-Z\_\@\.]+$";
        var re = new RegExp(regu);
        if (re.test(s)) {
            return true;
        } else {
            layer.open({
                content : "新用户名不合法"
            });
            return false;
        }
    }

    function changeNickNameTip(flag) {
        if (flag) {
            $("#hint").css('display','');
        } else {
            $("#hint").css('display','none');
        }
    }
</script>