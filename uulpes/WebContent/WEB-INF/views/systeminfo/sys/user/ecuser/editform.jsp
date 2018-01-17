<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/systeminfo/sys/user/ecuser"/>
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/create.do" />
		<c:remove var="dogshow"/>
		<c:set var="dogshow" scope="page" value="1"></c:set>
	</c:when>
	<c:when test="${op eq '查看'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="" />
		<c:remove var="dogshow"/>
		<c:set var="dogshow" scope="page" value="2"></c:set>
	</c:when>
	<c:when test="${op eq '修改'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/${entity.id}/update.do" />
		<c:remove var="dogshow"/>
		<c:set var="dogshow" scope="page" value="2"></c:set>
	</c:when>
	<c:otherwise>
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/create.do" />
		<c:remove var="dogshow"/>
		<c:set var="dogshow" scope="page" value="1"></c:set>
	</c:otherwise>
</c:choose>
<div id="editdialog" title="${empty op ? '新增' : op}教委人员信息">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity">
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">机构层级</label> <form:select id="formorglevel" path="orglevel" cssClass="select_160 validate[required]">
					<form:option value="0">请选择</form:option>
					<form:options items="${orglevelList}" itemValue="id" itemLabel="name"></form:options>
				</form:select></li>
				<li><label class="name04">所属单位</label>
				<form:select id="fromorgid" path="orgid" cssClass="select_160 validate[required]" >
					<form:option value="">请选择</form:option>
					<form:options items="${sunOrgList }" itemValue="id" itemLabel="name" />
				</form:select></li>
				
				<li><label class="name04">角色名称</label>
				<form:select id="formroleid" path="roleid" cssClass="select_160 validate[required]" >
					<form:option value="">请选择</form:option>
					<form:options items="${rolelist }" itemValue="id" itemLabel="roleName" />
				</form:select></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">真实姓名</label>
				<form:input path="xm" cssClass="select_160 validate[required]" ></form:input></li>
				<li><label class="name04">身份证号</label>
				<c:choose>
					<c:when test="${op eq '修改'}">
						<form:input id="fromsfzjh" path="sfzjh" disabled="true" cssClass="input_160 validate[required,ajax[ajaxIDCard],ajax[ajaxUserCheck]]" data-prompt-position="inline"></form:input>
					</c:when>
					<c:otherwise>
						<form:input id="fromsfzjh" path="sfzjh"  cssClass="input_160 validate[required,ajax[ajaxIDCard],ajax[ajaxUserCheck]]" data-prompt-position="inline"></form:input>
					</c:otherwise>
				</c:choose>
				</li>
				<li><label class="name04">性别</label>
				<form:select path="xbm" cssClass="select_160 validate[required]">
					<form:option value="">请选择</form:option>
					<form:options  items="${sexlist }" itemValue="id" itemLabel="name" />
				</form:select></li>
			</ul>
		</div>
		<div class="filterContent-dlg">
			<ul>
				<li><label class="name04">联系电话</label>
				<form:input path="lxdh" cssClass="input_160 validate[required]"></form:input></li>
				<c:if test="${showcreatedog}">
				<li><label class="name04">加密狗</label>
				<form:select path="isdogid" cssClass="select_160 validate[required]">
					<form:option value="">请选择</form:option>
					<form:options  items="${sflist }" itemValue="id" itemLabel="name" />
				</form:select></li>
				<input type="hidden" id="dogid" name="dogid">
				<input type="hidden" id=challenge name="challenge">
				<input type="hidden" id="vendorID" name="vendorID" />
				<input type="hidden" id="Factor" name="Factor" />
				</c:if>
			</ul>
		</div>
		<form:hidden path="accountId"/>

		
	</form:form>
</div>
<script type="text/javascript">
$(function(){
	var dogentity,usrName,objAuth;
	var scope = "<dogscope/>\n";
	var dogshow = '${dogshow}';
	var isdogvalue = "";
	var loginxm = "";
	if(dogshow === '1'){
		
		$("#isdogid").change(function(){
			debugger;
			isdogvalue=$(this).children("option:selected").val();
			if($("#xm").val() === "") {
				$("#xm").focus();
				layer.alert("请填写登录姓名!");
				return false;
			}
			loginxm = $("#xm").val();
			if(isdogvalue === '1'){
				//Get object
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
						layer.open({content:"获取加密狗信息出错，请联系管理员。"});
					}

				});
				stat = objAuth.Open(scope, authCode);
				debugger;
				if(stat === 7){//没有发现加密狗，可能是不需要加密狗登陆的用户
					layer.open({content:"没有发现加密狗插入"});
					$("#isdogid").val("2");
					return false;
				}else if (stat != 0 ) {
					layer.open({content:"探查加密狗错误!错误码[" + stat +"]"});
					$("#isdogid").val("2");
					return false;
				}else{//正常逻辑
					usrName = objAuth.UserNameStr;
					if("" != usrName)
					{
						objAuth.Close();
						layer.open ({content:"该加密狗已经被还用,请更换未被使用的加密狗,错误码[" + 1020 +"]"});
						$("#isdogid").val("2");
						return false;
					}
					stat = objAuth.VerifyUserPin("12345678");
				  	if(stat != 0)
				  	{
						objAuth.Close();
						layer.open ({content:"加密狗验证失败,请联系管理员,错误码[" + stat +"]"});
						$("#isdogid").val("2");
						return false;
				  	}
				  	stat = objAuth.GetDogID();
				  	if(stat != 0)
				  	{
						objAuth.Close();
						layer.open ({content:"加密狗不能获取重要信息,请联系管理员,错误码[" + stat +"]"});
						$("#isdogid").val("2");
						return false;
				  	}
				  	dogID = objAuth.DogIdStr;
				  	debugger;
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
				        objAuth.Close();
				        return false;
				    }
				    stat = objAuth.GetDigest(challenge);
				    if(stat != 0)
				  	{
						objAuth.Close();
						layer.open ({content:"加密狗不能获取重要信息,请联系管理员,错误码[" + stat +"]"});
						$("#isdogid").val("2");
						return false;
			      	}
				    var dist = objAuth.DigestStr;
				    $("#response").attr("value", dist);
				  	//Do authenticate
		        	$.ajax({
						url : '/pes/doginfo/doAuth.do',
						type : 'post',
						data:{dogid:dogID,result:dist,chal:challenge},
						cache : false,
						async : false,
						dataType : 'json',
						success : function(d) {
							stat = d;
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							objAuth.Close();
							layer.open ({content:"获取加密狗信息出错，请联系管理员。" + textStatus});
							$("#isdogid").val("2");
						}
					});
		          	if(stat != 0)
		          	{
				      	objAuth.Close();
				      	layer.open ({content:"加密狗不能获取重要信息,请联系管理员,错误码[" + stat +"]"});
				      	$("#isdogid").val("2");
				      	return false;
			      	}
				  	return true;
				}
			}
		});
	}
	
	
	$("#editForm").validationEngine({
		ajaxFormValidation: true,
		ajaxasync:false
	});
	$("#formorglevel").change(function(){
		var p1=$(this).children("option:selected").val();
		$.ajax({
			url:'${baseaction}/getSonSubOrgs.do',
			data:{'orglevel':p1},
			dataType:'json',
			success:function(data){
				$("#fromorgid option").remove();
				$("#fromorgid").append("<option value=''>请选择</option>");
				$.each(data,function(index,item){
					$("#fromorgid").append("<option value='"+ item.id+ "'>" + item.name + "</option>");
				});
			},
			error:function(){
				console.log("发生错误");
			}
			
		});
		$.ajax({
			url:'${baseaction}/getroles.do',
			data:{'orglevel':p1},
			dataType:'json',
			success:function(data){
				$("#formroleid option").remove();
				$("#formroleid").append("<option value=''>请选择</option>");
				$.each(data,function(index,item){
					$("#formroleid").append("<option value='"+ item.id+ "'>" + item.roleName + "</option>");
				});
			},
			error:function(){
				console.log("发生错误");
			}
			
		});
	});
	var buttonsOps = {};
	<c:choose>
	<c:when test="${empty op || op eq '新增' || op eq '修改'}">
	buttonsOps = {
		"保存" : function() {
			if (!$("#editForm").validationEngine('validate'))
				return false;
			$("#editForm").ajaxSubmit({
				target : "#content2",
				success : function() {
					if(dogshow === '1' && isdogvalue === '1'){
						stat = objAuth.RegisterUser(loginxm, 'admin123');
						objAuth.Close();
						if (stat != 0) {
							layer.alert("加密狗写入失败，请联系管理员");
						} else {
							layer.open({content:"保存成功!如果用户角色为管理员或者为心理老师,需要向开发商购买加密狗方可登录！"});
						}
					}
					$("#editdialog").dialog("close");
					$("#editForm").clearForm();
				},
				error : function() {
					$("#editForm").clearForm();
					}
				});
			},
			"取消" : function() {
				$("#editdialog").dialog("close");
				$("#editForm").clearForm();
			}
		};
		</c:when>
		<c:when test="${op eq '查看'}">
		buttonsOps = {
			"返回" : function() {
				$("#editdialog").dialog("close");
				$("#editForm").clearForm();
			}
		};
		</c:when>
		</c:choose>
		$("#editdialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 300,
			width : 800,
			buttons : buttonsOps
		});
	});
</script>