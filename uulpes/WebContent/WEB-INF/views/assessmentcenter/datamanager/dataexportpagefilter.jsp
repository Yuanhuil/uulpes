<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>
.ui-multiselect {
	width: 160px !important;
}
</style>
<link rel="stylesheet" type="text/css" href="../../themes/theme1/css/jquery.multiselect.css" />
<script type="text/javascript" src="../../js/jqueryplugin/multiselect/jquery.multiselect.min.js"></script>
	<c:choose>
		<c:when test="${orgType=='2' }">
			<c:set var="exportSchoolurl"
				value="../../assessmentcenter/datamanager/exportstuanswerinsch.do"></c:set>
		</c:when>
		<c:otherwise>
			<c:if test="${orgLevel==3 }">
			  <c:set var="exportSchoolurl" value="../../assessmentcenter/datamanager/exportstuanswerinedu.do"></c:set>
			</c:if>
			<c:if test="${orgLevel==4 }">
			  <c:set var="exportSchoolurl" value="../../assessmentcenter/datamanager/exportstuanswerinedu.do"></c:set>
			</c:if>
		
		</c:otherwise>
	</c:choose>
<form:form id="exportFilterParamForm" name="exportFilterParamForm"
	method="post" commandName="dataManageFilterParam"
	action="${exportSchoolurl }">
	<c:choose>
		<c:when test="${orgType=='2' }">
			<div class="filterContent">
				<ul>
					<li><label class="name03">学段</label> <form:select class="select_160" 
							id="xd" path="xd" onchange="getNj('6');">
							<form:option value="-1" id="se_export">请选择</form:option>
							<form:options name="xdse_export" items="${xdList }"
								itemLabel="xdmc" itemValue="xd" />
						</form:select></li>
					<li><label class="name03">年级名称</label> <form:select class="select_160" 
							id="nj" path="nj" onchange="queryBj();">
							<form:option value="-1">请选择</form:option>
						</form:select></li>
					<li><label class="name03">班级名称</label> <form:select class="select_160" 
							id="bj" path="bj" >
						</form:select></li>
				</ul>
				<input id="bjarray" name="bjarray" type="hidden"/> 
			</div>
		</c:when>
		<c:otherwise>
		    <c:if test="${orgLevel=='3'}">
				<div class="filterContent">
					<ul>
						<li><label class="name03">机构层级</label> <select id="xzxs" name="xzxs"
							class="select_160" onchange="queryQXXXFun('${cityId }');">
								<option value="-1">请选择</option>
								<option value="1">区县</option>
								<option value="2">市直属学校</option>
						</select></li>
					</ul>
				</div>
				<div class="filterContent">
					<ul>
						<li><label class="name03">区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;县</label> <select id="qx_or_xx" name="qx_or_xx"
							class="select_160" >
						</select></li>
						<li><label class="name03">学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;段</label><form:select class="select_160" 
							id="xd" path="xd" onchange="getNj('3');">
							<form:option value="-1" id="se_export">请选择</form:option>
							<form:option value="1">小学</form:option>
							<form:option value="2">初中</form:option>
							<form:option value="3">高中</form:option>
						</form:select></li>
						<li><label class="name03">年级名称</label> <form:select id="nj"
								path="nj" class="select_160" type="text" onchange="changeNj(3);">
								<form:option value="-1">请选择</form:option>

							</form:select></li>
					</ul>
					<input id="qxorxxarray" name="qxorxxarray" type="hidden"/> 
				</div>
				<!-- <label id="qxOrxx" class="name03">区县</label>
				<div class="filterContent" id="qx_or_xxcontainer"
					style="border: thin solid black; width: 500px; height: 200px; margin-left: 80px;"></div>-->
				
			</c:if>
			<c:if test="${orgLevel=='4'}">
				<div class="filterContent">
					<ul>
					    <li><label class="name03">选择学校</label> <select id="qx_or_xx" name="qx_or_xx"
							class="select_160"  onchange="getXdInSchool();">
					          <option value="-1">请选择</option>
							  <c:forEach var="item" items="${schoollist}" >
            					<option value="${item.id}">${item.name}</option>
       						  </c:forEach>
						</select></li>
						<li><label class="name03">学段</label><form:select class="select_160" 
							id="xd" path="xd" onchange="getNj('4');">
							<form:option value="-1" id="se_export">请选择</form:option>

						</form:select></li>
						<li><label class="name03">年级名称</label> <form:select id="nj"
								path="nj" class="select_160" type="text" onchange="changeNj(4);">
								<form:option value="-1">请选择</form:option>
								<form:options items="${njList }" itemLabel="njmc"
									itemValue="njmc" />
						</form:select></li>
					</ul>
					<input id="qxorxxarray" name="qxorxxarray" type="hidden"/> 
				</div>
			</c:if>
		</c:otherwise>
	</c:choose>
	<div class="filterContent">
		<ul>
			<li><label class="name03">量表类别</label> <form:select
					class="select_160" id="scaleType" path="scaleTypeId" onchange="changeScaleTypeOrSource('${orgType}','${orgLevel }')">
					<form:option value="-1" label="请选择"></form:option>
				</form:select></li>
			<li><label class="name03">量表来源</label> <form:select
					class="select_160" id="scaleSource" path="scaleSourceId" onchange="changeScaleTypeOrSource('${orgType}','${orgLevel }')">
					<form:option value="-1" label="请选择"></form:option>
				</form:select></li>
			<li><label class="name03">量表名称</label> <form:select class="select_160"
					id="scaleName" path="scaleId">
					<form:option value="-1">请选择</form:option>
				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">评测时间</label> <form:input class="input_160"
					path="testStartTime" type="text" id="testStartTime"></form:input></li>
				<li><label class="name03">至</label><form:input class="input_160"
					id="testEndTime" path="testEndTime"></form:input></li>
		</ul>
	</div>
	<div class="filterContent4">
		<ul>
		    <li><input class="button-mid blue" id="selectBkBtn" value="导出背景信息" type="button" onclick="selectBk()"/></li>
			<!-- <li><label class="name03">选择背景</label>
			 <input id="backgroundChk" type="checkbox" onclick="selectBk()" /></li>-->
		</ul>
	</div>
	<div id="backgroud"><%@include file="attrform.jsp"%></div>
	<div class="buttonContent">

		    <input class="button-small blue" type="button" value="导出数据" onclick="exportData('${orgType}','${orgLevel}');"/>
	
			<input class="button-small blue" id="clearform" type="button" value="重置" onclick="resetForm();" />
	</div>
</form:form>
<script type="text/javascript">
	var qxvs,bjvs;
	$(function() {
		$("#testStartTime").datepicker(
				{ //绑定开始日期
					dateFormat : 'yy-mm-dd',
					changeMonth : true, //显示下拉列表月份
					changeYear : true, //显示下拉列表年份
					firstDay : "1" //设置开始为1号
								});

		$("#testEndTime").datepicker(
				{ //绑定开始日期
					dateFormat : 'yy-mm-dd',
					changeMonth : true, //显示下拉列表月份
					changeYear : true, //显示下拉列表年份
					firstDay : "1" //设置开始为1号
								});

		bjvs = $("#bj").multiselect({
			checkAllText: "全选", 
			uncheckAllText: '全不选'
		}); 
		//qxvs = $("#qx_or_xx").multiselect({
			//checkAllText: "全选", 
			//uncheckAllText: '全不选'
		//}); 
		
		$(".multiselect").css("width","154px");
		$("#clearform").click(function(){
			$("#exportFilterParamForm").clearForm();
		});
	});
	function exportData1(){
		debugger;
		var xzxs=document.getElementById("xzxs").value;
		if(xzxs=="1"){//区县
			$("#exportFilterParamForm").attr("action", "../../assessmentcenter/datamanager/exportstuanswerinedu.do?type=edu");
		}
		if(xzxs=="2"){//直属学校
			$("#exportFilterParamForm").attr("action", "../../assessmentcenter/datamanager/exportstuanswerinsonschool.do");
		}
		$("#exportFilterParamForm").sumbit();
	}
	function exportData(orgType,orgLevel){
		debugger;
		if(orgType=='2'){
			if($("#bj")){
				var bjarrayJson = JSON.stringify($("#bj").multiselect("getChecked").map(function(){
					   return this.value;    
				}).get());
				$("#bjarray").val(bjarrayJson);
			}
		}
		else{
			if(orgLevel=='3'){

			}
			if(orgLevel=='4'){

			}
		}
		
		var scaleid = document.getElementById("scaleName").value;
		var testStartTime = document.getElementById("testStartTime").value;
		var testEndTime = document.getElementById("testEndTime").value;
		if(scaleid=='-1'||scaleid==null){
			layer.open({content:'请选量表!'});
			return;
		}
		if(testStartTime==''||testStartTime==null){
			layer.open({content:'请输入开始时间!'});
			return;
		}
		if(testEndTime==''||testEndTime==null){
			layer.open({content:'请输入终止时间!'});
			return;
		}
		$("#exportFilterParamForm").submit();
	}
    function selectBk() {
		var bgform = $('<form></form>');  
		bgform.attr('id',"bgform");
	    bgform.attr('action',"../../assessmentcenter/datamanager/showSelectStudentAttrForm.do");  
	    bgform.attr('method', 'post');
	    
	    bgform.ajaxSubmit({
			target : "#backgroud",
			success : function(msg) {
				$("#selectBkBtn").css("display","none");
			},
			error : function() {  
		         layer.open({content:'加载页面背景字段时出错!'});  
		      } 
		});
    }
    function getNj(orgLevel) {
		debugger;
		var id=null;
		if(orgLevel=='6') id=null;
		else
			id=document.getElementById("qx_or_xx").value;
		var xd = document.getElementById("xd").value;
		if (xd == -1) {
			$("#nj").html("<option value='-1'>请选择</option>");
			return;
		}
		var xzxs='0';
		if(orgLevel=='3')
			xzxs = document.getElementById("xzxs").value;
		$.ajax({
					type : "POST",
					url : "../../scaletoollib/reportlook/queryDistinctNJByCountyidOrSchoolid.do",
					data : {
						"id":id,
						"xd" : xd,
						"xzxs" : xzxs
					},
					success : function(msg) {
						debugger;
						var nj = $("#nj");
						var njarray = jQuery.parseJSON(msg);
						if (njarray.length > 0) {
							var optionstr = "<option value='-1'>请选择</option>";
							for ( var i = 0; i < njarray.length; i++) {
								var njid = njarray[i].nj;
								optionstr = optionstr
										+ "<option value="+njid+">" + njid
										+ "级</option>";
							}
							nj.html(optionstr);
						} else {
							nj.html("");
							nj.html("<option value='-1'>请选择</option>");
						}
					},
					error : function() {
						layer.open({content:'调用出现错误，查找年级失败'});
					}
				});
	}
    function queryBj() {
		debugger;
		var nj = document.getElementById("nj").value;
		if (nj == '-1') {
			$("#bj").html("<option value='-1'>请选择</option>");
			return;
		}
		var xd = document.getElementById("xd").value;
		if (xd == '-1') {
			return;
		}
		$
				.ajax({
					type : "POST",
					url : "../../ajax/queryBJAndScaleFromExamresultStudentInSchool.do",
					data : {
						"xd" : xd,
						"nj" : nj,
						"bj" :'-1'
					},
					success : function(msg) {
						debugger;
						var bj = $("#bj");
						var scaleselect = $("#scaleName");
						var callbackarray=jQuery.parseJSON(msg);
						var bjarray = callbackarray.ersList;
						var scaleList = callbackarray.scaleList;
						//var bjarray = jQuery.parseJSON(msg);
						if (bjarray.length > 0) {
							var optionstr = "";
							for ( var i = 0; i < bjarray.length; i++) {
								var bjid = bjarray[i].bj;
								var bjmc = bjarray[i].bjmc;
								optionstr = optionstr
										+ "<option value="+bjid+">" + bjmc
										+ "</option>";
							}
							bj.html(optionstr);
							bjvs.multiselect('refresh');
						} else {
							bj.html("");
							bj.html("<option value='-1'>请选择</option>");
						}
						
						//$("#scale_name").empty();
						//$("#scale_name").append("<option value='-1'>请选择</option>");
						if(scaleList.length>0){
							var optionstr1 = "<option value='-1'>请选择</option>";
							for(var i=0;i<scaleList.length;i++){
								var scale = scaleList[i];
								var scaleid = scale.code;
								var scalename = scale.title;
								//$("#scale_name").append("<option value='"+scaleid+"'>"+scalename+"</option>");
								optionstr1 = optionstr1
								+ "<option value="+scaleid+">" + scalename
								+ "</option>";
							}
							scaleselect.html(optionstr1);
						}
						else{
							scaleselect.html("");
							scaleselect.html("<option value='-1'>请选择</option>");
						}
						var scaleTypeMap = callbackarray.scaleTypeMap;
						if(scaleList.length>0){
							$("#scaleType").empty();
							$("#scaleType").append("<option value='-1'>请选择</option>");
							for(var typeid in scaleTypeMap){//用javascript的for/in循环遍历对象的属性 
								var typename = scaleTypeMap[typeid]; 
								$("#scaleType").append("<option value='"+typeid+"'>"+typename+"</option>");
							} 
						}
						else{
							$("#scaleType").html("");
							$("#scaleType").html("<option value='-1'>请选择</option>");
						}
						var scaleSourceMap = callbackarray.scaleSourceMap;
						$("#scaleSource").empty();
						$("#scaleSource").append("<option value='-1'>请选择</option>");
						for(var sourceid in scaleSourceMap){//用javascript的for/in循环遍历对象的属性 
							var sourcename = scaleSourceMap[sourceid]; 
							$("#scaleSource").append("<option value='"+sourceid+"'>"+sourcename+"</option>");
						} 
						
					},
					error : function() {
						layer.open({content:'调用出现错误，查找班级失败'});
					}
				});
	}
    function changeScaleTypeOrSource(orgType,orgLevel) {
		var xd = document.getElementById("xd").value;
		if (xd == '-1') {
			layer.open({content:'请选择学段!'});
			return;
		}
		
		var nj = document.getElementById("nj").value;
		if (nj == '-1') {
			layer.open({content:'请选择年级!'});
			return;
		}
		var countyidOrSchoolid =null;
		if(orgType=='2')
			countyidOrSchoolid =null;
		else  
			countyidOrSchoolid= document.getElementById("qx_or_xx").value;
		var xzxs = null;
		if(orgLevel==3)
		   xzxs = document.getElementById("xzxs").value;
	
		var scaletype=document.getElementById("scaleType").value;
		var scalesource=document.getElementById("scaleSource").value;
		$.ajax({
			 type: "POST",
			 url: "../../scaletoollib/reportlook/getEduScaleInfoFromExamdoStudent1.do",
			 data:{
				     "countyidOrSchoolid":countyidOrSchoolid,
				 	 "xzxs":xzxs,
					 "xd":xd,
					 "nj":nj,
					 "scaletype":scaletype,
					 "scalesource":scalesource
				 },
				   success: function(msg){
					   debugger;
					   if(msg!='[]'){
							var callbackarray=jQuery.parseJSON(msg);
							var scaleList = callbackarray.scaleList;
							
							$("#scaleName").empty();
							$("#scaleName").append("<option value='-1'>请选择</option>");
							for(var i=0;i<scaleList.length;i++){
								var scale = scaleList[i];
								var scaleid = scale.code;
								var scalename = scale.title;
								$("#scaleName").append("<option value='"+scaleid+"'>"+scalename+"</option>");
							}
					   }else{
							
					   }
				   },
			     error: function()
			     {  	
			    	 //layer.open({content:"根据年级展现班级出错!"});
			   	 }
				}); 

	}
	function getBj() {

		var nj = document.getElementById("nj_export").value;
		if (nj == -1) {
			$("#bj_export").html("<option value='-1'>请选择</option>");
			return;
		}
		var xd = document.getElementById("xd_export").value;
		if (xd == -1) {
			return;
		}
		$
				.ajax({
					type : "POST",
					url : "../../ajax/queryBJAndScaleFromExamresultStudentInSchool.do",
					data : {
						"xd" : xd,
						"nj" : nj,
						"bj" :'-1'
					},
					success : function(msg) {
						debugger;
						var bj = $("#bj_export");
						var scaleselect = $("#scale_name");
						var callbackarray=jQuery.parseJSON(msg);
						var bjarray = callbackarray.ersList;
						var scaleList = callbackarray.scaleList;
						//var bjarray = jQuery.parseJSON(msg);
						if (bjarray.length > 0) {
							var optionstr = "<option value='-1'>请选择</option>";
							for ( var i = 0; i < bjarray.length; i++) {
								var bjj = bjarray[i].bj;
								var bjmc = bjarray[i].bjmc;
								optionstr = optionstr
										+ "<option value="+bjj+">" + bjmc
										+ "</option>";
							}
							bj.html(optionstr);
						} else {
							bj.html("");
							bj.html("<option value='-1'>请选择</option>");
						}
						
						//$("#scale_name").empty();
						//$("#scale_name").append("<option value='-1'>请选择</option>");
						if(scaleList.length>0){
							var optionstr1 = "<option value='-1'>请选择</option>";
							for(var i=0;i<scaleList.length;i++){
								var scale = scaleList[i];
								var scaleid = scale.code;
								var scalename = scale.title;
								//$("#scale_name").append("<option value='"+scaleid+"'>"+scalename+"</option>");
								optionstr1 = optionstr1
								+ "<option value="+scaleid+">" + scalename
								+ "</option>";
							}
							scaleselect.html(optionstr1);
						}
						else{
							scaleselect.html("");
							scaleselect.html("<option value='-1'>请选择</option>");
						}
						var scaleTypeMap = callbackarray.scaleTypeMap;
						if(scaleList.length>0){
							$("#scaleType").empty();
							$("#scaleType").append("<option value='-1'>请选择</option>");
							for(var typeid in scaleTypeMap){//用javascript的for/in循环遍历对象的属性 
								var typename = scaleTypeMap[typeid]; 
								$("#scaleType").append("<option value='"+typeid+"'>"+typename+"</option>");
							} 
						}
						else{
							$("#scaleType").html("");
							$("#scaleType").html("<option value='-1'>请选择</option>");
						}
						var scaleSourceMap = callbackarray.scaleSourceMap;
						$("#scaleSource").empty();
						$("#scaleSource").append("<option value='-1'>请选择</option>");
						for(var sourceid in scaleSourceMap){//用javascript的for/in循环遍历对象的属性 
							var sourcename = scaleSourceMap[sourceid]; 
							$("#scaleSource").append("<option value='"+sourceid+"'>"+sourcename+"</option>");
						} 
						
					},
					error : function() {
						layer.open({content:'调用出现错误，查找班级失败'});
					}
				});
	}
	function changeBj() {
		debugger;
		var nj = document.getElementById("nj_export").value;
		var bj = document.getElementById("bj_export").value;
		var xd = document.getElementById("xd_export").value;
		$.ajax({
					type : "POST",
					url : "../../ajax/queryBJAndScaleFromExamresultStudentInSchool.do",
					data : {
						"xd" : xd,
						"nj" : nj,
						"bj":bj
					},
					success : function(msg) {
						debugger;
						var callbackarray=jQuery.parseJSON(msg);
						var scaleList = callbackarray.scaleList;
						$("#scale_name").empty();
						$("#scale_name").append("<option value='-1'>请选择</option>");
						for(var i=0;i<scaleList.length;i++){
							var scale = scaleList[i];
							var scaleid = scale.code;
							var scalename = scale.title;
							$("#scale_name").append("<option value='"+scaleid+"'>"+scalename+"</option>");
						}
						
						var scaleTypeMap = callbackarray.scaleTypeMap;
						$("#scaleType").empty();
						$("#scaleType").append("<option value='-1'>请选择</option>");
						for(var typeid in scaleTypeMap){//用javascript的for/in循环遍历对象的属性 
							var typename = scaleTypeMap[typeid]; 
							$("#scaleType").append("<option value='"+typeid+"'>"+typename+"</option>");
						} 
						var scaleSourceMap = callbackarray.scaleSourceMap;
						$("#scaleSource").empty();
						$("#scaleSource").append("<option value='-1'>请选择</option>");
						for(var sourceid in scaleSourceMap){//用javascript的for/in循环遍历对象的属性 
							var sourcename = scaleSourceMap[sourceid]; 
							$("#scaleSource").append("<option value='"+sourceid+"'>"+sourcename+"</option>");
						} 
						
					},
					error : function() {
						layer.open({content:'调用出现错误，查找班级失败'});
					}
				});
	}

	function searchStudentDispatch() {
		$("#exportFilterParamForm").ajaxSubmit(
				{
					target : "#tabs_datamanager",
					success : function() {
						var xxddsa = document.getElementsByName("xdse_export");
						for ( var i = 0; i < xxddsa.length; i++) {
							xxddsa[i].removeAttribute("selected");
						}
						document.getElementById("se_export").setAttribute(
								"selected", "selected");
					}
				});
	}
	function resetForm(){
		$("#exportFilterParamForm")[0].reset();
		$("#nj").html("<option value='-1'>请选择</option>");
		$("#bj").html("<option value='-1'>请选择</option>");
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
				   url: "../../scaletoollib/reportlook/getStudentExamdoQuxianStr.do",
				   data: {
					   "cityId":cityId
					   },
				   success: function(msg){
					   debugger;
					   if(msg!='[]'){
							var callbackarray=jQuery.parseJSON(msg);
							$("#qx_or_xx").empty();
							$("#qx_or_xx").append("<option value='-1'>请选择</option>");
							for(var i=0;i<callbackarray.length;i++){
								var quxian = callbackarray[i];
								var code = quxian.code;
								var name = quxian.name;
								$("#qx_or_xx").append("<option value='"+code+"'>"+name+"</option>");
							}
							//qxvs.multiselect('refresh');
					   }else{
						   $("#qx_or_xx").append("<option value='-1'>请选择</option>");
						   //qxvs.multiselect('refresh');
					   }
				   },
			     error: function()
			     {  	
			    	 layer.open({content:'根据年级展现班级出错!'});
			   	 }
				}); 
		}else{
			$.ajax({
				   type: "POST",
				   url: "../../scaletoollib/reportlook/getStudentExamdoSonSchools.do",
				   data: {
					   "cityId":cityId
					   },
				   success: function(msg){
					   if(msg!='[]'){
							var callbackarray=jQuery.parseJSON(msg);
							$("#xzqh").empty();
							$("#xzqh").append("<option value='-1'>请选择</option>");
							for(var i=0;i<callbackarray.length;i++){
								var classSchool = callbackarray[i];
								var classId = classSchool.id;
								var name = classSchool.name;
								$("#xzqh").append("<option value='"+classId+"'>"+name+"</option>");
							}
					   }else{
							document.getElementById("xzqh").innerHTML="<option value='-1'>请选择</option>";
					   }
				   },
			     error: function()
			     {  	
			    	 layer.open({content:'根据年级展现班级出错!'});
			   	 }
				}); 
		}
	}
	function changeXzqh(){
		var xzxs=document.getElementById("xzxs").value;
		if(xzxs=="1"){
			var countyid = document.getElementById("xzqh").value;
			$.ajax({
				   type: "POST",
				   url: "../../ajax/getStudentGradeAndScaleInCounty.do",
				   data: {
					   "countyid":countyid
					   },
				   success: function(msg){
					   debugger;
					   if(msg!='[]'){
							var callbackarray=jQuery.parseJSON(msg);
							var gradeList = callbackarray.gradeList;
							var scaleList = callbackarray.scaleList;
							
							$("#scale_name").empty();
							$("#scale_name").append("<option value='-1'>请选择</option>");
							for(var i=0;i<scaleList.length;i++){
								var scale = scaleList[i];
								var scaleid = scale.code;
								var scalename = scale.title;
								$("#scale_name").append("<option value='"+scaleid+"'>"+scalename+"</option>");
							}
							
							$("#nj").empty();
							$("#nj").append("<option value='-1'>请选择</option>");
							for(var i=0;i<gradeList.length;i++){
								var grade = gradeList[i];
								var gradeid = grade.gradeid;
								var njmc = grade.njmc;
								$("#nj").append("<option value='"+gradeid+"'>"+njmc+"</option>");
							}
							
							var scaleTypeMap = callbackarray.scaleTypeMap;
							$("#scaleType").empty();
							$("#scaleType").append("<option value='-1'>请选择</option>");
							for(var typeid in scaleTypeMap){//用javascript的for/in循环遍历对象的属性 
								var typename = scaleTypeMap[typeid]; 
								$("#scaleType").append("<option value='"+typeid+"'>"+typename+"</option>");
							} 
							var scaleSourceMap = callbackarray.scaleSourceMap;
							$("#scaleSource").empty();
							$("#scaleSource").append("<option value='-1'>请选择</option>");
							for(var sourceid in scaleSourceMap){//用javascript的for/in循环遍历对象的属性 
								var sourcename = scaleSourceMap[sourceid]; 
								$("#scaleSource").append("<option value='"+sourceid+"'>"+sourcename+"</option>");
							} 
					   }else{
							document.getElementById("nj").innerHTML="<option value='-1'>请选择</option>";
							document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
							document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
							document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
					   }
				   },
			     error: function()
			     {  	
			    	 layer.open({content:'根据年级展现班级出错!'});
			   	 }
				}); 
		}
		if(xzxs=="2"){
			var schoolid = document.getElementById("xzqh").value;
			$.ajax({
				   type: "POST",
				   url: "../../ajax/getStudentGradeAndScaleInSonSchool.do",
				   data: {
					   "schoolid":schoolid
					   },
				   success: function(msg){
					   debugger;
					   if(msg!='[]'){
							var callbackarray=jQuery.parseJSON(msg);
							var gradeList = callbackarray.gradeList;
							var scaleList = callbackarray.scaleList;
							
							$("#scale_name").empty();
							$("#scale_name").append("<option value='-1'>请选择</option>");
							for(var i=0;i<scaleList.length;i++){
								var scale = scaleList[i];
								var scaleid = scale.code;
								var scalename = scale.title;
								$("#scale_name").append("<option value='"+scaleid+"'>"+scalename+"</option>");
							}
							
							$("#nj").empty();
							$("#nj").append("<option value='-1'>请选择</option>");
							for(var i=0;i<gradeList.length;i++){
								var grade = gradeList[i];
								var gradeid = grade.gradeid;
								var njmc = grade.njmc;
								$("#nj").append("<option value='"+gradeid+"'>"+njmc+"</option>");
							}
							
							var scaleTypeMap = callbackarray.scaleTypeMap;
							$("#scaleType").empty();
							$("#scaleType").append("<option value='-1'>请选择</option>");
							for(var typeid in scaleTypeMap){//用javascript的for/in循环遍历对象的属性 
								var typename = scaleTypeMap[typeid]; 
								$("#scaleType").append("<option value='"+typeid+"'>"+typename+"</option>");
							} 
							var scaleSourceMap = callbackarray.scaleSourceMap;
							$("#scaleSource").empty();
							$("#scaleSource").append("<option value='-1'>请选择</option>");
							for(var sourceid in scaleSourceMap){//用javascript的for/in循环遍历对象的属性 
								var sourcename = scaleSourceMap[sourceid]; 
								$("#scaleSource").append("<option value='"+sourceid+"'>"+sourcename+"</option>");
							} 
					   }else{
							document.getElementById("nj").innerHTML="<option value='-1'>请选择</option>";
							document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
							document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
							document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
					   }
				   },
			     error: function()
			     {  	
			    	 layer.open({content:'根据年级展现班级出错!'});
			   	 }
				}); 
		}
		
	}
	function getXdInSchool(){
		debugger;
		var schoolid = document.getElementById("qx_or_xx").value;
		if(schoolid=='-1'){
			document.getElementById("xd").innerHTML="<option value='-1'>请选择</option>";
			document.getElementById("nj").innerHTML="<option value='-1'>请选择</option>";
			document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
			document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
			document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
			return;
		}
		$.ajax({
			   type: "POST",
			   url: "../../scaletoollib/reportlook/queryDistinctXdInSchool.do",
			   data: {
				   "schoolid":schoolid
				   },
			   success: function(msg){
				   debugger;
				   if(msg!='[]'){
					   var callbackarray=jQuery.parseJSON(msg);
					   for(var i=0;i<callbackarray.length;i++){
							var xdinfo = callbackarray[i];
							var xdid = xdinfo.xd;
							var xdmc = xdinfo.xdmc;
							$("#xd").append("<option value='"+xdid+"'>"+xdmc+"</option>");
						}
						
				   }
			       else{
					document.getElementById("nj").innerHTML="<option value='-1'>请选择</option>";
					document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
					document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
					document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
			   }
		   },
	     error: function()
	     {  	
	    	
	   	 }
		}); 
	}
	function changeSchool(){
		var schoolid = document.getElementById("school").value;
		$.ajax({
			   type: "POST",
			   url: "../../ajax/getStudentGradeAndScaleInSchool.do",
			   data: {
				   "schoolid":schoolid
				   },
			   success: function(msg){
				   debugger;
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);
						var gradeList = callbackarray.gradeList;
						var scaleList = callbackarray.scaleList;
						
						$("#scale_name").empty();
						$("#scale_name").append("<option value='-1'>请选择</option>");
						for(var i=0;i<scaleList.length;i++){
							var scale = scaleList[i];
							var scaleid = scale.code;
							var scalename = scale.title;
							$("#scale_name").append("<option value='"+scaleid+"'>"+scalename+"</option>");
						}
						
						$("#nj").empty();
						$("#nj").append("<option value='-1'>请选择</option>");
						for(var i=0;i<gradeList.length;i++){
							var grade = gradeList[i];
							var gradeid = grade.gradeid;
							var njmc = grade.njmc;
							$("#nj").append("<option value='"+gradeid+"'>"+njmc+"</option>");
						}
						
						var scaleTypeMap = callbackarray.scaleTypeMap;
						$("#scaleType").empty();
						$("#scaleType").append("<option value='-1'>请选择</option>");
						for(var typeid in scaleTypeMap){//用javascript的for/in循环遍历对象的属性 
							var typename = scaleTypeMap[typeid]; 
							$("#scaleType").append("<option value='"+typeid+"'>"+typename+"</option>");
						} 
						var scaleSourceMap = callbackarray.scaleSourceMap;
						$("#scaleSource").empty();
						$("#scaleSource").append("<option value='-1'>请选择</option>");
						for(var sourceid in scaleSourceMap){//用javascript的for/in循环遍历对象的属性 
							var sourcename = scaleSourceMap[sourceid]; 
							$("#scaleSource").append("<option value='"+sourceid+"'>"+sourcename+"</option>");
						} 
				   }else{
						document.getElementById("nj").innerHTML="<option value='-1'>请选择</option>";
						document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
						document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
						document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:'根据年级展现班级出错!'});
		   	 }
			}); 
	}
	function changeNj(orgLevel){
		debugger;
		var countyidOrSchoolid =null;
		if(orgLevel==2)
			countyidOrSchoolid =null;
		else  
			countyidOrSchoolid= document.getElementById("qx_or_xx").value;
		var nj = document.getElementById("nj").value;
		if(nj=='-1'){
			$("#scaleType").empty();
			$("#scaleType").append("<option value='-1'>请选择</option>");
			$("#scaleSource").empty();
			$("#scaleSource").append("<option value='-1'>请选择</option>");
			$("#scaleName").empty();
			$("#scaleName").append("<option value='-1'>请选择</option>");
			return;
		}
		var xzxs = null;
		if(orgLevel==3)
		   xzxs = document.getElementById("xzxs").value;
		var xd = document.getElementById("xd").value;
		if(xzxs=='1'){
			
		}
		$.ajax({
			 type: "POST",
			 url: "../../scaletoollib/reportlook/getEduScaleInfoFromExamdoStudent.do",
			 data:{
				     "countyidOrSchoolid":countyidOrSchoolid,
				 	 "xzxs":xzxs,
					 "xd":xd,
					 "nj":nj
				 },
				   success: function(msg){
					   debugger;
					   if(msg!='[]'){
							var callbackarray=jQuery.parseJSON(msg);
							var scaleList = callbackarray.scaleList;
							
							$("#scaleName").empty();
							$("#scaleName").append("<option value='-1'>请选择</option>");
							for(var i=0;i<scaleList.length;i++){
								var scale = scaleList[i];
								var scaleid = scale.code;
								var scalename = scale.title;
								$("#scaleName").append("<option value='"+scaleid+"'>"+scalename+"</option>");
							}
							
							var scaleTypeMap = callbackarray.scaleTypeMap;
							$("#scaleType").empty();
							$("#scaleType").append("<option value='-1'>请选择</option>");
							for(var typeid in scaleTypeMap){//用javascript的for/in循环遍历对象的属性 
								var typename = scaleTypeMap[typeid]; 
								$("#scaleType").append("<option value='"+typeid+"'>"+typename+"</option>");
							} 
							var scaleSourceMap = callbackarray.scaleSourceMap;
							$("#scaleSource").empty();
							$("#scaleSource").append("<option value='-1'>请选择</option>");
							for(var sourceid in scaleSourceMap){//用javascript的for/in循环遍历对象的属性 
								var sourcename = scaleSourceMap[sourceid]; 
								$("#scaleSource").append("<option value='"+sourceid+"'>"+sourcename+"</option>");
							} 
					   }else{
							document.getElementById("nj").innerHTML="<option value='-1'>请选择</option>";
							document.getElementById("scaleType").innerHTML="<option value='-1'>请选择</option>";
							document.getElementById("scaleSource").innerHTML="<option value='-1'>请选择</option>";
							document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
					   }
				   },
			     error: function()
			     {  	
			    	 layer.open({content:'搜索量表出错!'});
			   	 }
				}); 

	}
</script>