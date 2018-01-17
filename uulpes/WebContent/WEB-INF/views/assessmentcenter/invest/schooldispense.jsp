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
<c:set var="baseaction" value="${ctx}/assessmentcenter/invest"/>
	<div class="stepControl">
		<div id="step1" class="step1_sel">第一步:选择时间</div>
		<div id="step2" class="step2">第二步:选择对象</div>
		<div id="step3" class="step3">第三步:调查名称</div>
		<!-- <div id="step4" class="step4">第四步:命名任务</div>-->
	</div>
	<form id="dispenseform" action="${baseaction}/schooldistribute.do" method="post">
		   <table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="info" style="display:inline-table">
		  <!-- <tr>
		    <td class="left_title_3" colspan="2">量表分发 →&nbsp;第一步</td>
		  </tr>-->
		  <tr>
		    <td colspan="2" class="list_table_caption"><b>量表分发：基本信息</b></td>
		  </tr>		  
		  <tr>
		    <td width="30%" style="text-align: center;">开始时间：</td>
		    <td><label>
		    <input class="input_160" type="text" id="starttime" name="starttime" value=""/> <font color="red">* yyyy-MM-dd</font>
		    </label></td>   
		  </tr>
		  <tr>
		    <td style="text-align: center;">结束时间：</td>
		    <td><label>
		    <input class="input_160" type="text" id="endtime" name="endtime" value=""/>	 <font color="red">* yyyy-MM-dd</font>
		    </label></td>   
		  </tr>
		  <!-- <tr>
		    <td align="right">同一量表可再测试期限限制：</td>
		    <td><input name="flag" type="radio" value="1" checked/>有	<input type="radio" name="flag" value="0" />无</td>   
		  </tr> --> 
		  <tr>
		    <td colspan="2" style="text-align: center;"><label><input type="button" id="next" class="button-mid blue" value="下一步" onClick="infoNext()"></label></td>
		  </tr>
		</table>
		<table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="colony" style="display:none">
		 <!-- <tr>
		    <td class="left_title_3" colspan="2">量表分发 →&nbsp;第二步</td>
		  </tr>-->
		  <tr>
		    <td colspan="2" class="list_table_caption"><b>量表分发：分发对象</b></td>
		  </tr>
		  <tr>
		    <td width="13%" style="text-align: center;">分发对象：</td>
		    <td><label>
		      <select class="select_160" id="objectType" name="objectType" onChange="clickRole()">
			  	<option value="">请选择</option>
		        <option value="1">学生</option>
		        <option value="2">教师</option>
		      </select></label>
		    </td>
		  </tr>
		  <tr id="partgrade" style="display:none">    
		      <td width="13%"  style="text-align: center;">学段：</td>
		      <td><label><select class="select_160" id="gradepart" name="gradepart" onChange="clickPart()">
		      	<option value="0">请选择</option>
		        <c:forEach items="${xdlist}" var="xd">
		        <option value="${xd}"><c:choose><c:when test="${xd=='1'}">小学</c:when><c:when test="${xd=='2'}">初中</c:when><c:when test="${xd=='3'}">高中</c:when><c:otherwise>中职</c:otherwise> </c:choose></option>
		       </c:forEach>
		      </select></label>
		      </td>
		  </tr>
		  <tr id="student" style="display:none">
		    <td width="13%" style="text-align: center;">&nbsp;</td>	
		    <td width="87%"> 
		        <table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="gradeclass" style="display:inline-table">
					
			    </table>
			</td>   
		  </tr>
		  <tr id="teacher" style="display:none">
		    <td width="13%" align="right">&nbsp;</td>	
		    <td width="87%" ><input class="checkbox01" type="checkbox" name="teacherRole" value="4" />
		      领导&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="checkbox01" type="checkbox" name="teacherRole" value="24" />
		      心理咨询老师&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <input class="checkbox01" type="checkbox" name="teacherRole" value="21" />
			  心理老师&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			  <input class="checkbox01" type="checkbox" name="teacherRole" value="23" />
			  班主任&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <input class="checkbox01" type="checkbox" name="teacherRole" value="29" />
		      任课老师 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <input class="checkbox01" type="checkbox" name="teacherRole" value="20" />
		      学校管理员    </td>   
		  </tr>
		  <tr>
		    <td colspan="4" style="text-align: center;"><input type="button" value="上一步" class="button-mid blue" onClick="investTargetUp()"> <input type="button" class="button-mid blue" value="下一步" onClick="investTargetNext()"></td>
		  </tr>
		</table>
		
		
	<table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="task" style="display:none">
			  <!-- <tr>
			    <td class="left_title_3" colspan="2">量表分发 →&nbsp;第四步</td>
			  </tr>-->
			  <tr>
			    <td colspan="2" class="list_table_caption"><b>问卷分发：问卷调查命名</b></td>
			  </tr>
			   <tr>
			    <td width="14%" style="text-align: center;">问卷调查名称:</td>
			    <td width="86%">
			    	<input class="input_260" type ="text" id="taskname" name="taskname" />
			    </td>   
			  </tr>
			  <tr>
			    <td colspan="2" style="text-align: center;"><input type="button" value="上一步" class="button-mid blue" onClick="investTaskUp()"/> <input type="button" class="button-mid blue" value="分发" onclick="startInvestdispense()"/></td>
			  </tr>
		</table>
		<input type="hidden" value="${scaleid}" name="scaleid" id="scaleid"/>
	</form>
	
<script type="text/javascript">
	$(function(){
		$("#dispenseform").ajaxForm({
		        		type:"post",
						target : "#schooldispense"
					});

	});
	function startInvestdispense(){
		if($("#taskname").val()==""){
			layer.open({content:"调查问卷名称不能为空！\r\n"});
		}else {
			$("#dispenseform").submit();	
		}
	}
	
	
		$("#starttime").datepicker(
				{ //绑定开始日期
					prevText: '<上月',  
			        nextText: '下月>',  
			        currentText: '今天',  
			        monthNames: ['一月','二月','三月','四月','五月','六月',  
			        '七月','八月','九月','十月','十一月','十二月'],  
			        monthNamesShort: ['一','二','三','四','五','六',  
			        '七','八','九','十','十一','十二'],  
			        dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],  
			        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],  
			        dayNamesMin: ['日','一','二','三','四','五','六'],  
			        weekHeader: '周',  
					dateFormat : 'yy-mm-dd',
					changeMonth : true, //显示下拉列表月份
					changeYear : true, //显示下拉列表年份
					showWeek : true, //显示星期	
					firstDay : "1", //设置开始为1号
					onSelect : function(dateText, inst) {
						//设置结束日期的最小日期
						$("#endtime").datepicker("option","minDate",dateText);
					}
				});
	
		$("#endtime").datepicker(
				{ //设置结束绑定日期
					prevText: '<上月',  
			        nextText: '下月>',  
			        currentText: '今天',  
			        monthNames: ['一月','二月','三月','四月','五月','六月',  
			        '七月','八月','九月','十月','十一月','十二月'],  
			        monthNamesShort: ['一','二','三','四','五','六',  
			        '七','八','九','十','十一','十二'],  
			        dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],  
			        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],  
			        dayNamesMin: ['日','一','二','三','四','五','六'],  
			        weekHeader: '周',  
					dateFormat : 'yy-mm-dd',
					changeMonth : true, //显示下拉列表月份
					changeYear : true, //显示下拉列表年份
					showWeek : true, //显示星期	
					firstDay : "1", //设置开始为1号
					onSelect : function(dateText, inst) {
						//设置开始日期的最大日期
						 $("#starttime").datepicker("option","maxDate",dateText);
					}
				});

	
</script>

