<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<njpes:contentHeader title="${appheadtitle }" index="true" />
<link href="${ctx}/themes/theme1/css/maincss.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
/* var stat = 0;
var objAuth = "";
var authCode = "";
var dogentity = null;
var scope = "<dogscope/>\n";
     function removeDog(){
    	 $("#doginfo").html("已经拔掉加密狗，请使用用户名密码登录");
    	 $("#username").attr("value","");
		 $("#username").removeAttr("readonly");
    }

    function insertDog(){
	    $("#doginfo").html("探查到加密狗，请使用加密狗登录");
	    stat = objAuth.Open(scope, authCode);
		if(stat === 7){//没有发现加密狗，可能是不需要加密狗登陆的用户
			
		}else if (stat != 0 ) {
			//alert("探查加密狗错误!错误码[" + stat +"]");
			layer.open({content:"探查加密狗错误!错误码[" + stat +"]"});
		}else{//正常逻辑
			// Get username from the dog
			stat = objAuth.GetUserName();
			console.log(objAuth);
			if (stat != 0) {
				objAuth.Close();
				//alert ("获取机密狗的用户名错误，请联系售后解决。错误码[" + stat +"]");
				layer.open({content:"获取机密狗的用户名错误，请联系售后解决。错误码[" + stat +"]"});
			}
			$("#username").attr("value",objAuth.UserNameStr);
			$("#username").attr("readonly","true");
		}
		objAuth.Close();
    }
    
	function loadFunc() {
		//Get object
		objAuth = getAuthObject();

		if (navigator.userAgent.indexOf("Window") > 0) {
			if (window.ActiveXObject || "ActiveXObject" in window) //IE
			{
				objAuth.SetCheckDogCallBack("insertDog", "removeDog");
			} else {
				objAuth.SetCheckDogCallBack("insertDog", "removeDog");
				objAuth.InsertFunc = insertDog;
				objAuth.RemoveFunc = removeDog;
			}
		} else if (navigator.userAgent.indexOf("Mac") > 0) {
			setTimeout(checkDog, 1000);
		} else if (navigator.userAgent.indexOf("Linux") > 0) {
			setTimeout(checkDog, 1000);
		} else {
			;
		}
	}
	function validateLogin()
    {	
	    var challenge = "";
	    var stat = "";
	    var dogID = "";
	    var digest = "";
	    var pwd = $("#password").val();
    	
    	
	    if(window.ActiveXObject || "ActiveXObject" in window) //IE
	    {
		    //Add onfocus event
		    var obj = $("#password"); 
		    if (Object.hasOwnProperty.call(window, "ActiveXObject") && !window.ActiveXObject)
		    {// is IE11  
			    obj.addEventListener("onfocus", clearInfo, false);
		    }
		    else
		    {
			    obj.attachEvent("onfocus", clearInfo);  
		    }
	    }
    	
	    //Open the dog
	    stat = objAuth.Open(scope, authCode);
	    if(stat === 7){
	    	return true;
	    }else if (stat != 0 ) {
			alert("探查加密狗错误!错误码[" + stat +"]");
			return false;
		}
		// Get username from the dog
		stat = objAuth.VerifyUserPin(pwd);
		if (stat != 0) {
			objAuth.Close();
			alert ("验证机密狗的密码错误，请重新输入。错误码[" + stat +"]");
			return false;
		}
		//Get the DogID
	    stat = objAuth.GetDogID();
	    if(stat != 0)
	    {
		    objAuth.Close();
		    alert ("验证机密狗的唯一ID表示错误，请联系售后解决。错误码[" + stat +"]");
		    return false;
	    }
	   //Save the DogID
	    dogID = objAuth.DogIdStr;
	    $("#dogid").attr("value",dogID);
	    challenge = dogentity.challenge;
	    if(challenge.toString().length < 32)
	    {
	        if(challenge == "001")
	        {
	            reportStatus(1001);
	        }
	        else if(challenge == "002")
	        {
	            reportStatus(1002);
	        }
	        else
	        {
	            reportStatus(1003);
	        }
	        window.objAuth.Close();
	        return false;
	    }
	  //Generate digest
  	    stat = objAuth.GetDigest(challenge);
	    if(stat != 0)
	    {
		    objAuth.Close();
		    alert ("验证加密狗错误，请联系售后解决。错误码[" + stat +"]");
		    return false;
	    }
	    digest = objAuth.DigestStr;
	    $("#response").attr("value",digest);
	    objAuth.Close();
	    return true;
    } */
</script>
</head>
<body onload="loadFunc()">
<div class="container">
	<div class="loginTopBg">
  		<div class="loginTop">
  		    <div class="loginlogo"><!-- <img src="images/logo.png" width="80" height="80"  alt=""/>--></div><div class="companyName">${appheadtitle }</div>
  	    </div>
	</div>
	<div style="position:absolute;width:100px;height:10px;top:0px;left;right:10px;">
	<a href="/pes/DogAuthPluginSetup.exe">加密狗插件下载</a>
	</div>
	<form method="post" id="loginform">
	<div class="loginMain">
		<div class="rightWhite">
 			  <div class="mainTab" style="display:none">
				<ul>
					 <li><input type="radio" name="logintype" value="3">陶老师工作站</li>
					 <li><input type="radio" name="logintype" checked="checked" value="1">管理平台</li>
				</ul>
			</div>
			<span>${error}</span>
			<div class="nameBg">
				<input type="text" name="username" id="username"
						class="input-xlarge validate[required]"
						placeholder="请输入用户名/工号/学号">
			</div>
			<div class="pwdBg">
					<input type="password" name="password" id="password"
						class="input-xlarge validate[required]" placeholder="请输入密码">
			</div>
			<div class="loginButton">
				<input class="button-login blue" type="submit" value="登录" onclick="return validateLogin();"> 
			</div>
			<input type="hidden" id="dogid" name="dogid" value="1"/>
			<input type="hidden" id="response" name="response" value="1"/>
			<div id="doginfo">
				 
			</div>
		</div>
	</div>
	</form>
</div>
<div class="loginFooter">版权所有 © ${appfooter }</div>
<njpes:contentFooter />
<script type="text/javascript">
$(function(){
	 try{
			//Get object
			objAuth = getAuthObject();
			//Get Auth Code
		$.ajax({
				url : '/pes/doginfo/getconfiginfo.do',
				type : 'post',
				cache : false,
				async : false,
				dataType : 'json',
				success : function(d) {
					debugger;
					if (d.dogid == 0) {
						forbidLogin();
						//alert("获取加密狗信息出错，请联系管理员。");
						layer.open({content:'错误: ' + "没有加密狗，请插入加密狗."});
					}else if(d.dogid == 2){
						forbidLogin();
						//alert("获取加密狗信息出错，请联系管理员。");
						layer.open({content:'错误: ' + "SuperDog API dynamic library not found，请联系管理员."});
					}else if(d.dogid == 3){
						forbidLogin();
						//alert("获取加密狗信息出错，请联系管理员。");
						layer.open({content:'错误: ' + "SuperDog API dynamic library is corrupt，请联系管理员."});
					}else {
						dogentity = d;
						authCode = d.authCode;
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					forbidLogin();
					//alert("获取加密狗信息出错，请联系管理员。");
					layer.open({content:'错误: ' + "获取加密狗信息出错，请联系管理员."});
				}

			});

			// Open the dog
			stat = objAuth.Open(scope, authCode);
			if(stat === 7){//没有发现加密狗，可能是不需要加密狗登陆的用户
				
			}else if (stat != 0 ) {
				//alert("探查加密狗错误!错误码[" + stat +"]");
				layer.open({content:'探查加密狗错误!错误码[' + stat +"]"});
			}else{//正常逻辑
				// Get username from the dog
				stat = objAuth.GetUserName();
				if (stat != 0) {
					objAuth.Close();
					//alert ("获取机密狗的用户名错误，请联系售后解决。错误码[" + stat +"]");
					layer.open({content:"获取机密狗的用户名错误，请联系售后解决。错误码[" + stat +"]"});
				}
				$("#username").attr("value",objAuth.UserNameStr);
				$("#username").attr("readonly","readonly");
			}
			objAuth.Close();
		} catch (e) {
		}
	$("#loginform").validationEngine();
	});

function forbidLogin(){
    var username=document.getElementById("username");
    username.setAttribute("readonly","readonly");
    username.setAttribute("placeholder","请插入加密狗");
    var password=document.getElementById("password");
    password.setAttribute("readonly","readonly");
    password.setAttribute("placeholder","请插入加密狗");
}
</script>
