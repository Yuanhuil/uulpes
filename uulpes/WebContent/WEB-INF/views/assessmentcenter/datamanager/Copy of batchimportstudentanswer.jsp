<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx}/assessmentcenter/datamanager"/>
<div>这里放说明</div>
<form id="dispenseform" action="${baseaction}/downloadStudentAnswerTemp.do" method="post">
	<div class="filterContent">
		<ul>
			<li><label class="name03">年级名称</label> <select class="input_160" name="nj" id="nj" onchange="getClassAndScaleAccordingGrade();">
				<option value="-1">请选择</option>
				<c:forEach var="grade" items="${gradeList }">
					<option value="${grade.gradeid }">${grade.njmc }</option>
				</c:forEach>
			</select></li>
			<li><label class="name03">班级名称</label> <select class="input_160" id="bj" name="bj">
				<option value="-1">请选择</option>
			</select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">量表类别</label> <select class="input_160" id="scaleType" name="scaleType" onchange="queryScales();" 
				class="input_160">
				<option value="-1">请选择</option>
				<c:forEach var="scaletype" items="${scaleTypes}">
					<option value="${scaletype.name}">${scaletype.name}</option>
				</c:forEach>
				</select></li>
		</ul>
		<ul>
			<li><label class="name03">量表来源</label> <select id="scaleSource"  onchange="queryScales();" 
				class="input_160">
					<option value="-1">请选择</option>
					<c:forEach var="scaleSource" items="${scaleSources }">
					<option value="${scaleSource.name }">${scaleSource.name }</option>
				</c:forEach>
			</select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">量表名称</label> <select id="scaleName" name="scaleName"
				class="input_160">
					<option value="-1">请选择</option>
			</select></li>
		</ul>
	</div>
	<div class="buttonContent">
		<ul>
			<li><input  value="下载答题模板"  class="button-small blue" type="submit">
			<input class="button-small blue" 	type="button" value="重置"  /></input></li>
		</ul>
	</div>
</form>
<div><hr></div>
<form id="answeruploadform" action="${baseaction}/uploadStudentAnswer.do" method="post">
<div class="filterContent">
	<ul>
		<li><label class="name03">答案导入</label> <input style="padding-left:0px;"
			class="input_300" type="file" name="file"><input class="button-small blue"
			type="button" value="上传答案" onclick="uploadStudentAnswer()" /></input></li>
	</ul>
</div>
</form>
<div>
答案导入结果
</div>

<div id="uploadresult">
      
</div>

<script type="text/javascript">
	function uploadStudentAnswer(){
		$("#answeruploadform").ajaxSubmit({
			target : "#uploadresult"
		});
	}
	function getClassAndScaleAccordingGrade(){
		debugger;
		var gradeid=document.getElementById("nj").value;
		if(gradeid=="-1"){
			document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
			return;
		}
		$.ajax({
			   type: "POST",
			   url: "../../ajax/getClassesAndScalesByGradeId.do",
			   data: {
				   "gradeid":gradeid
				   },
			   success: function(msg){
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);
						var htmlstr="";
						var classarray=callbackarray.classList;
						$("#bj").empty();
						$("#bj").append("<option value='-1'>请选择</option>");
						//var htmlstr="";
						for(var i=0;i<classarray.length;i++){
							var classSchool = classarray[i];
							var classId = classSchool.id;
							var bjmc = classSchool.bjmc;
							$("#bj").append("<option value='"+classId+"'>"+bjmc+"</option>");
						}
						debugger;
						var scaleArray=callbackarray.scaleList;
						$("#scaleName").empty();
						$("#scaleName").append("<option value='-1'>请选择</option>");
						for(var i=0;i<scaleArray.length;i++){
							var scale = scaleArray[i];
							var typeId = scale.code;
							var title = scale.title;
							$("#scaleName").append("<option value='"+typeId+"'>"+title+"</option>");
						}
				   }else{
						document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
						document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:"根据年级展现班级出错!"});
		   	 }
			}); 
	}
	function getClassByGradeId(){
		var gradeid=document.getElementById("nj").value;
		if(gradeid=="-1"){
			document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
			return;
		}
		$.ajax({
			   type: "POST",
			   url: "../../ajax/getClassesByGradeId2.do",
			   data: {
				   "gradeid":gradeid
				   },
			   success: function(msg){
				   if(msg!='[]'){
						var callbackarray=jQuery.parseJSON(msg);
						$("#bj").empty();
						$("#bj").append("<option value='-1'>请选择</option>");
						for(var i=0;i<callbackarray.length;i++){
							var classSchool = callbackarray[i];
							var classId = classSchool.id;
							var bjmc = classSchool.bjmc;
							$("#bj").append("<option value='"+classId+"'>"+bjmc+"</option>");
						}
				   }else{
						document.getElementById("bj").innerHTML="<option value='-1'>请选择</option>";
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:"根据年级展现班级出错!"});
		   	 }
			}); 
	}
	function queryScales(){
		debugger;
		var gradeid=document.getElementById("nj").value;
		var typeid=document.getElementById("scaleType").value;
		var sourceid = document.getElementById("scaleSource").value;
		//if(typeid=="-1"){
			//document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
			//return;
		//}
		$.ajax({
			   type: "POST",
			   url: "../../ajax/queryScales.do",
			   data: {
				   "gradeid":gradeid,
				   "typeid":typeid,
				   "sourceid":sourceid
				   },
			   success: function(msg){
				   if(msg!='[]'){
					   debugger;
						var callbackarray=jQuery.parseJSON(msg);
						var htmlstr="";
						$("#scaleName").empty();
						$("#scaleName").append("<option value='-1'>请选择</option>");
						//var htmlstr="";
						for(var i=0;i<callbackarray.length;i++){
							var scaleinfo = callbackarray[i];
							var scaleid = scaleinfo.code;
							var scalename = scaleinfo.title;
							$("#scaleName").append("<option value='"+scaleid+"'>"+scalename+"</option>");
						}
						
				   }else{
						document.getElementById("scaleName").innerHTML="<option value='-1'>请选择</option>";
				   }
			   },
		     error: function()
		     {  	
		    	 layer.open({content:"不存在该量表类型的量表!"});
		   	 }
			}); 
	}
	
</script>