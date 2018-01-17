<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<script src="${ctx}/js/jquerydispense.js"></script>
<style type="text/css">
	.scalepanel{
		border:none;
		width:300px;
		height:220px;
		float:right;
		background-color: #FFFFFF;
		border: solid 1px #B8B8B8;
		display:none;
		border-radius: 10px;
	}
	.dataloading{
  	  display:none;
  	  width:300px;
  	  height:200px;
      background:url(../../images/dataloading.gif) no-repeat  center;
      margin:auto;
  }
</style>
<script type="text/javascript">
	function startdispense(){
		layer.open({content:"startdispense"});
	var num = valiGetBoxNum("scaleId");
	if(num<1){
		layer.open({content:"请选择要分发的量表！\r\n"});
	}else {
		$("#dispenseform").submit();	
	}
}
</script>
<c:set var="baseaction" value="${ctx}/assessmentcenter/scaledispense"/>
	<div class="stepControl">
		<div id="step1" class="step1_sel">第一步:选择时间</div>
		<div id="step2" class="step2">第二步:选择对象</div>
		<div id="step3" class="step3">第三步:选择量表</div>
		<div id="step4" class="step4">第四步:命名任务</div>
	</div>
	<form id="dispenseform" action="${baseaction}/edudistribute.do" method="post">
		   <table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="info" style="display:inline-table">
		  <tr>
		    <td colspan="2" class="list_table_caption"><b>量表分发：基本信息</b></td>
		  </tr>		  
		  <tr>
		    <td width="30%" style="text-aligh:center;" >开始时间：</td>
		    <td><label>
		    <input class="input_160" type="text" id="starttime" name="starttime" value=""/> <font color="red">* yyyy-MM-dd</font>
		    </label></td>   
		  </tr>
		  <tr>
		    <td style="text-aligh:center;">结束时间：</td>
		    <td><label>
		    <input class="input_160" type="text" id="endtime" name="endtime" value=""/>	 <font color="red">* yyyy-MM-dd</font>
		    </label></td>   
		  </tr>
		 <!-- <tr>
		    <td align="right">同一量表可再测试期限限制：</td>
		    <td><input name="flag" type="radio" value="1" checked/>有	<input type="radio" name="flag" value="0" />无</td>   
		  </tr>  -->
		  <tr>
		    <td colspan="2"  style="text-align: center;"><label><input class="button-mid blue"  type="button" id="next" class="class_sub" value="下一步" onClick="infoNext()"></label></td>
		  </tr>
		</table>
		
		<table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="colony" style="display:none">
		  <tr>
		    <td colspan="2" class="list_table_caption"><b>量表分发：选择对象</b></td>
		  </tr>
		  
		  <tr id="orgLevel" >
		    <td width="13%" style="text-aligh:center;">机构层级：</td>
		    <td width="87%"><label>
		      <select class="select_160" id="orgLevelSel" name="orgLevelSel" onChange="clickOrgLevel()">
		      </select></label>
		    </td>
		  </tr>
		  <tr id="city" style="display:none">
		    <td width="13%" style="text-aligh:center;">市、地区</td>
		    <td width="87%"><table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="cityList" style="border-collapse: collapse;display:table-row">
					
			 </table>
		    </td>
		  </tr>
		  <tr id="county" style="display:none">
		    <td width="13%" style="text-aligh:center;">区县</td>
		    <td width="87%"> <table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="countyList" style="display:inline-table">
					
			 </table></td>
		  </tr>
 
		 <tr id="town" style="display:none">
		 	<td width="13%" style="text-align: center;border:1px solid #e0e0e0 ;">选择学校&nbsp;&nbsp;<input class="checkbox01"  type="checkbox" id="schoolck" ></td>
		 	<td width ="87%" style="vertical-align:top;width:130px;height:200px;border:1px solid #e0e0e0; multiple="multiple">
			 	<div style="width:700px; height:225px; overflow:scroll;">
			 		<table width="100%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="schoolList" style="border-collapse:collapse;display:table-row;"></table>
				</div>
			</td>
		 </tr>
		 <tr>
		    <td width="13%" style="text-align: center;">分发对象</td>
		    <td><label>
		      <select class="select_160" id="objectType" name="objectType" onChange="clickObjectTypeEdu()">
			  	<option value="">请选择</option>
		        <option value="1">学生</option>
		        <option value="2">教师</option>
		      </select></label>
		    </td>
		  </tr>
		  <tr id="partgrade" style="display:none">    
		      <td width="13%"  style="text-align: center;">学段：</td>
		      <td><label><select class="select_160" id="gradepart" name="gradepart" onChange="clickPartEdu()">
		      	<option value="0">请选择</option>
		        <option value="1">小学</option>
		        <option value="2">初中</option>
		        <option value="3">高中</option>
		      </select></label>
		      </td>
		  </tr>
		  <tr id="grade1" style="display:none">    
		      <td width="13%"  style="text-align: center;">年级名称：</td>
		      <td><table width="87%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="gradeList1" style="border-collapse:collapse;display:inline-table">
					<tr  style="display:table-row">
						<td><input class="checkbox01" type='checkbox' id='nj1' name='nj1' value="3">三年级</td>
						<td><input class="checkbox01" type='checkbox' id='nj1' name='nj1' value="4">四年级</td>
						<td><input class="checkbox01" type='checkbox' id='nj1' name='nj1' value="5">五年级</td>
						<td><input class="checkbox01" type='checkbox' id='nj1' name='nj1' value="6">六年级</td>
						
					</tr>
			 </table>
		    </td>
		  </tr>
		  <tr id="grade2" style="display:none">    
		      <td width="13%"  align="right">年级名称：</td>
		      <td><table width="87%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="gradeList2" style="border-collapse:collapse;display:inline-table">
					<tr   style="display:table-row">
						<td><input class="checkbox01" type='checkbox' id="nj2"    name='nj2' value="7">一年级</td>
						<td><input class="checkbox01" type='checkbox' id="nj2"    name='nj2' value="8">二年级</td>
						<td><input class="checkbox01" type='checkbox' id="nj2"    name='nj2' value="9">三年级</td>
						<td><input class="checkbox01" type='checkbox' id="nj2"    name='nj2' value="10">四年级</td>
					</tr>
			 </table>
		      </td>
		  </tr>
		   <tr id="grade3" style="display:none">    
		      <td width="13%"  style="text-align: center;">年级名称：</td>
		      <td><table width="87%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="gradeList3" style="border-collapse:collapse;display:inline-table">
					<tr   style="display:table-row">
						<td><input class="checkbox01" type='checkbox' id="nj3"   name='nj3' value="11">一年级</td>
						<td><input class="checkbox01" type='checkbox' id="nj3"   name='nj3' value="12">二年级</td>
						<td><input class="checkbox01"  type='checkbox' id="nj3"   name='nj3' value="13">三年级</td>
					</tr>
			 </table>
		      </td>
		  </tr>
		  <tr id="teacher" style="display:none">
		    <td width="13%" style="text-align: center;">教师类型:</td>	
		    <td width="87%" id="teacherRoleContainer" ><!-- <input class="checkbox01" type="checkbox" name="teacherRole" value="24" />
		      领导&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="checkbox01" type="checkbox" name="teacherRole" value="32" />
		      心理咨询师&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <input class="checkbox01" type="checkbox" name="teacherRole" value="21" />
			  心理老师&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			  <input class="checkbox01" type="checkbox" name="teacherRole" value="23" />
			  班主任&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <input class="checkbox01" type="checkbox" name="teacherRole" value="29" />
		      任课老师 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <input class="checkbox01" type="checkbox" name="teacherRole" value="20" />
		      学校管理员    --></td>   
		  </tr>
		  <tr id="subschool" style="display:none">
		    <td width="13%" style="text-align: center;">选择学校：</td>
		    <td><table width="87%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="subschoolList" name="subschoolList" style="display:table-row;border-collapse:collapse;">
					
			 </table>
		    </td>
		  </tr>
		  <tr>
		    <td colspan="4"  style="text-align: center;"><input class="button-mid blue"  type="button" value="上一步" class="class_sub" onClick="colonyUp()"> <input class="button-mid blue"  type="button" class="class_sub" value="下一步" onClick="eduColonyNext()"></td>
		  </tr>
		</table>
		<table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="scale" style="display:none">
		  <tr><td>
			      <table width="98%" border="0" align ="center" id="scalefilter">
			          <tr>
			            <td width="18%" style="text-align:center;">量表来源：</td>
			            <td width="25%">
			            	<select class="select_160" id="scalesource">
			            	</select>
			            </td>
			            <td width="18%" style="text-align:center;">量表类别</td>
			            <td width="25%">
			            	<select class="select_160" id="scaletype">
			            	</select>
			            </td>
			              <td width="14%" style="text-align:center;" rowspan="2">
			              	<input class="button-big blue" type="button" value="查找量表" style="height:30px;" onclick="searchScale()"></input>
			              </td>
			          </tr>
			           <tr>
			            <td width="15%" style="text-align:center;">是否预警</td>
			            <td width="20%">
			                <select class="select_160" id="iswarning">
			                  <option value="-1">请选择</option>
			                  <option value="true">预警</option>
			                  <option value="false">不预警</option>
			                </select>
			            </td>
			            <td style="text-align:center;">量表名称</td>
			            <td>
			                <input class="input_160" width="100" type="text" id="scalename" />
			            </td>
			           
			          </tr>
			     </table>
			  </td></tr>
			  <!-- <tr>
			    <td colspan="2" class="list_table_caption"><b>量表分发：量表信息</b></td>
			  </tr>-->
			  <tr>
			    <td>
			      <div style="width:98%;height:480px;overflow:auto">
				      <table width="98%"  border="0" cellspacing="0" id="scalelist"> <tbody> 

						</tbody>
					 </table>
				 </div>
			    </td>
			  </tr>
		  <tr>
		    <td colspan="2" style="text-align: center;"><input class="button-mid blue"  type="button" value="上一步" class="class_sub" onClick="scaleUp()"> <input class="button-mid blue"  type="button" class="class_sub" value="下一步" onClick="scaleNext()"></td>
		  </tr>
	</table>
	<table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="task" style="display:none">
			  <tr>
			    <td colspan="2" class="list_table_caption"><b>量表分发：命名任务</b></td>
			  </tr>
			   <tr>
			    <td width="14%" align="right">任务名称:</td>
			    <td width="86%">
			    	<input class="input_260" type ="text" id="taskname" name="taskname" />
			    </td>   
			  </tr>
			  <tr>
			    <td colspan="2" style="text-align: center;"><input class="button-mid blue"  type="button" value="上一步"  onClick="taskUp()"/> <input type="button" class="button-mid blue" value="分发" onclick="startdispense()"/></td>
			  </tr>
		</table>
		<input id="schoolarray" type="hidden" name="schoolarray"/>
	</form>
	<div id="dataloading" class="dataloading">
	
	</div>
	<div id="scaleSetDiv" class="scalepanel">
		<table id="scalesetTable" border=0 style="border-spacing:0px;margin-top:5px;">
			<tr><td style="text-align:right;">家长查看权限</td><td><label id="ppLb" onclick="setPP()">ON</label></td></tr>
			<tr><td style="text-align:right;">班主任查看权限</td><td><label id="tpLb" onclick="setTP()">ON</label></td></tr>
			<tr><td style="text-align:right;">学生查看权限</td><td><label id="spLb" onclick="setSP()">ON</label></td></tr>
			<tr><td style="text-align:right;">预警量表查看权限</td><td><select id="warningSelect">
				<option value="0">请选择</option>
				<option value="1">一级</option>
				<option value="2">二级</option>
				<option value="3">三级</option>
			</select>以下可查看</td></tr>
			<tr>
				<td colspan="2" style="text-align:center;">
					<input class="button-mid blue"  type="button" value="确定" onclick="saveScaleSet()">
					<input class="button-mid blue"  type="button" value="取消" onclick="cancelScaleSet()">
				</td>
			</tr>		
		</table>		
	</div>

<script type="text/javascript">
	$(function(){
		$("#schoolck").click(function() {
			$("input[name='schoolid']").prop('checked', $(this).prop('checked'));
		});
		$("#dispenseform").ajaxForm({
		        		type:"post",
						target : "#edudispense"
					});

	});
	function startdispense(){
		$("#dataloading").css("display","block");
		//layer.open({content:"startdispense"});
		//学校多选
		setSchoolArray();
		var num = valiGetBoxNum("scaleId");
		if(num<1){
			layer.open({content:"请选择要分发的量表！\r\n"});
		}else {
			$("#dispenseform").submit();	
		}
	}
	
	function setSchoolArray(){
		if(townSchoolArray==null)return;
		if(townSchoolArray.length == 0)return;
		var valueTemp = '';
		  for(var i=0;i<townSchoolArray.length;i++){
			 var townSchool = townSchoolArray[i];
			 var schools = townSchool.schools;
			 if(schools!=null)
				 {
				   for(var j=0;j<schools.length;j++){
					   if(schools[j].checked == true){
						   valueTemp +=schools[j].id;
					   		valueTemp += ',';
					   }
				   }
				 }

		     }
		  if(valueTemp.length>1)
			  valueTemp=valueTemp.substring(0,valueTemp.length-1);
		  $("#schoolarray").val(valueTemp);
		  
	}	
	
		$("#starttime").datepicker(
				 { //绑定开始日期
					 dateFormat : 'yy-mm-dd',
					 changeMonth : true, //显示下拉列表月份
					 changeYear : true, //显示下拉列表年份
					 firstDay : "1" ,//设置开始为1号
					onSelect : function(dateText, inst) {
						//设置结束日期的最小日期
						$("#endtime").datepicker("option","minDate",dateText);
					}
				});
	
		$("#endtime").datepicker(
				 { //绑定开始日期
					 dateFormat : 'yy-mm-dd',
					 changeMonth : true, //显示下拉列表月份
					 changeYear : true, //显示下拉列表年份
					 firstDay : "1", //设置开始为1号
					onSelect : function(dateText, inst) {
						//设置开始日期的最大日期
						 $("#starttime").datepicker("option","maxDate",dateText);
					}
				});

	
</script>

