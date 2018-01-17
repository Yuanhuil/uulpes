<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="sysformurl" value="../sys/searchSyslogs.do"></c:set>
<form:form id="syslogFilterForm" name="syslogFilterForm" method="post" commandName="syslogFilterParam" action="${sysformurl }">
	<div class="filterContent">
		<ul>
			<c:if test="${orglevel==3}">
				<li><label class="name03">机构层级</label> <select id="xzxs" name="xzxs"
					class="select_160" onchange="queryQXXXFun('${cityId }');">
						<option value="-1">请选择</option>
						<option value="1">区县</option>
						<option value="2">市直属学校</option>
				</select></li>
			</c:if>
			<c:if test="${orglevel==4 and orgType!='2'}">
				<li><label class="name03">机构名称</label> <form:select	id="organizations" path="orgid"  cssClass="input_160" onchange="changeRole('${orglevel}','${orgtype}')">
					<form:option value="${myorgid}">${myorgname }</form:option>
					<form:options items="${schoollist }" itemValue="id" itemLabel="name"></form:options>
					</form:select>
				</li>
			</c:if>
			<c:if test="${orglevel==3 }">
				<li><label class="name03">机构名称</label> <form:select	id="organizations" path="orgid"  cssClass="input_160" onchange="changeRole('${orglevel}','${orgtype}')">
					
				</form:select>
				</li>
			</c:if>
				<li><label class="name03">角色名称</label> <form:select id="roles" path="roleid" cssClass="input_160">
					<form:options items="${roles }" itemValue="id" itemLabel="roleName"></form:options>
					</form:select>
				</li>
		</ul>
	</div>

	<!--搜索条件区end（每行三列搜索条件，可以复制）-->
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div class="filterContent">
		<ul>
			<li><label class="name03">操作模块</label> <form:input id="menuname" type="text" path="menuname" cssClass="input_160"/></li>
			<li><label class="name03">操作名称</label> <form:input id="content" type="text" path="content" cssClass="input_160"/></li>
			<li><label class="name03">操作人</label> <form:input id="operator" type="text" path="operator" cssClass="input_160"/></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">开始时间</label><form:input path="starttime" class="input_160"
				type="text" id="datepicker"></form:input></li>
			<li><label class="name03">结束时间</label><form:input path="endtime" class="input_160"
				type="text" id="datepickers"></form:input></li>
		</ul>
	</div>
	<!--搜索条件区end（每行三列搜索条件，可以复制）-->
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div class="buttonContent">

				<input class="button-mid blue" type=submit value="搜索"	id="search" />
				<!-- <input class="button-mid blue" type="button" value="下载" onclick="downloadExcel();" />-->

	</div>

</form:form>
<script>
$(function(){
	$("#syslogFilterForm").ajaxForm({
		target : "#tableContent"
	});
	
});
	var myorgid =  '${myorgid}';
	var myorgname =  '${myorgname}';
	function searchLogs(){
	     document.syslogFilterForm.action="../sys/searchSyslogs.do";
	     document.syslogFilterForm.submit();
	}
	function downloadExcel(){
		 document.syslogFilterForm.action="../sys/downloadSyslogs.do";
		 document.syslogFilterForm.submit();
	}
	function changeRole(orglevel,orgtype){
		debugger;
		var idx = $("#organizations").prop('selectedIndex');
		if(idx==0){
			document.getElementById("roles").innerHTML="<option value='-1'>请选择</option>";
			return;
		}
		var url;
		var olevle,otype,rlevel;
		if(orglevel==3){
			var xzxs = $("#xzxs").val();
			if(xzxs == "-1"){//本单位

			}
			if(xzxs == "1"){//区县
				
				if(idx==1){
					olevel=3;
					otype=1;
					rlevel=3;
				}
				else{
					olevel=4;
					otype=1;
					rlevel=4;
				}
			}
			else if(xzxs == "2"){//直属学校
				if(idx==0){
					olevel=3;
					otype=1;
					rlevel=3;
				}
				else{
					olevel=4;
					otype=2;
					rlevel=6;
				}
			}
		}
		if(orglevel==4){
			if(idx==0){
				olevel=4;
				otype=1;
				rlevel=4;
			}
			else{
				olevel=6;
				otype=2;
				rlevel=6;
			}
		}
		if(orglevel==6){
			olevel=6;
			otype=2;
			rlevel=6;
		}
		$.ajax({
			type: "POST",
			   url: "../sys/chageRole.do",
			   data: {
				   "orglevel":olevel,
				   "orgtype":otype,
				   "rolelevel":rlevel
				   },
			   success: function(msg){
				   debugger;
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);
						$("#roles").empty();
						$("#roles").append("<option value='-1'>请选择</option>");
						for(var i=0;i<callbackarray.length;i++){
							var role = callbackarray[i];
							var roleid = role.id;
							var rolename = role.roleName;
							$("#roles").append("<option value='"+roleid+"'>"+rolename+"</option>");
						}
				   }else{
						document.getElementById("roles").innerHTML="<option value='-1'>请选择</option>";
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:"获取角色出错!"});
		   	 }
		});

	}
	function queryQXXXFun(cityId){
		debugger;
		var xzxs=document.getElementById("xzxs").value;
		if(xzxs=="-1"){
			document.getElementById("xzqh").innerHTML="<option value='-1'>请选择</option>";
			return;
		}
		if(xzxs=="1"){
			$.ajax({
				   type: "POST",
				   url: "../../ajax/getCountyEduStr.do",
				   data: {
					   "cityId":cityId
					   },
				   success: function(msg){
					   
					   if(msg!='[]'){
							var callbackarray=jQuery.parseJSON(msg);
							$("#organizations").empty();
							$("#organizations").append("<option value='-1'>请选择</option>");
					    	 $("#organizations").append("<option value='"+myorgid+"'>"+myorgname+"</option>");
							for(var i=0;i<callbackarray.length;i++){
								var org = callbackarray[i];
								var id = org.id;
								var name = org.name;
								$("#organizations").append("<option value='"+id+"'>"+name+"</option>");
							}
					   }else{
							document.getElementById("organizations").innerHTML="<option value='-1'>请选择</option>";
					   }
					   
				   },
			     error: function()
			     {  	
			    	 layer.open({content:"查询区县教委出错!"});
			   	 }
				}); 
		}else{
			$.ajax({
				   type: "POST",
				   url: "../../ajax/getSonSchools.do",
				   success: function(data){
					   debugger;
					   var dataXml = toXML(data);
					   var schools = dataXml.getElementsByTagName("schools");
					   $("#organizations").append("<option value='-1'>请选择</option>");
					   $("#organizations").append("<option value='"+myorgid+"'>"+myorgname+"</option>");
					    for(var i=0;i<schools.length;i++){
					    	var schoolid = schools[i].childNodes[0].firstChild.nodeValue;
					    	var schoolname = areas[i].childNodes[1].firstChild.nodeValue;
					    	$("#organizations").append("<option value='"+schoolid+"'>"+schoolname+"</option>");
						
						}
				   },
			     error: function()
			     {  	
			    	 layer.open({content:"查询直属学校出错!"});
			   	 }
				}); 
		}
	}
	function toXML(strxml){ 
		  try{ 
		     xmlDoc = new ActiveXObject("Microsoft.XMLDOM"); 
		     xmlDoc.loadXML(strxml); 
		  } 
		  catch(e){ 
		     var oParser=new DOMParser(); 
		     xmlDoc=oParser.parseFromString(strxml,"text/xml"); 
		  } 
		  return xmlDoc; 
		} 
</script>