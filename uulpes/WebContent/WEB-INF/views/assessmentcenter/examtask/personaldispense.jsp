<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<body>
	<form name="form1" method="post" action="">
		    <table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="info" style="display:block">
			  <tr>
			    <td class="left_title_3" colspan="2">量表分发 →&nbsp;第一步</td>
			  </tr>
			  <tr>
			    <td colspan="2" class="list_table_caption"><b>量表分发：基本信息</b></td>
			  </tr>		  
			  <tr>
			    <td width="30%" align="right" >开始时间：</td>
			    <td><label>
			    <input type="text" id="starttime" name="starttime" value=""/> <font color="red">* yyyy-MM-dd</font>
			    </label></td>   
			  </tr>
			  <tr>
			    <td align="right">结束时间：</td>
			    <td><label>
			    <input type="text" id="endtime" name="endtime" value=""/>	 <font color="red">* yyyy-MM-dd</font>
			    </label></td>   
			  </tr>
			  <tr>
			    <td align="right">同一量表可再测试期限限制：</td>
			    <td><input name="flag" type="radio" value="1" checked/>有	<input type="radio" name="flag" value="0" />无</td>   
			  </tr>  
			  <tr>
			    <td colspan="2" align="center"><label><input type="button" class="class_sub" value="下一步" onClick="infoNext()"></label></td>
			  </tr>
			</table>
			<table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="colony" style="display:none">
			 <tr>
			    <td class="left_title_3" colspan="2">量表分发 →&nbsp;第二步</td>
			  </tr>
			  <tr>
			    <td colspan="2" class="list_table_caption"><b>量表分发：分发对象</b></td>
			  </tr>
			  <tr>
			    <td width="13%" align="right">分发对象：</td>
			    <td><label>
			      <select id="objectType" name="objectType" onChange="clickRoleUnit()">
				  	<option value="">请选择</option>
			        <option value="1">学生</option>
			        <option value="2">教师</option>
			      </select></label>
			    </td>
			  </tr>
			  <tr id="partgrade" style="display:none">    
			      <td width="13%"  align="right">分发群体：</td>
			      <td><label></select>年级：<select name="gradeOrderId" class="class_sub" id="gradeOrderId" onchange="changeGrade()">
		                <option value="">请选择</option>
				        <c:forEach var="item" items="${gradeList}">
				        	<option value="${item.key}">${item.value}</option>
						</c:forEach>
		            </select>班级：<select name="classId" class="class_sub" id="classId" onchange="changeClass()">
		              <option value="">请选择</option>
		            </select></label>
			      </td>
			  </tr>		  
			  <tr id="teacher" style="display:none">
			    <td width="13%" align="right">教师角色：</td>	
			    <td width="87%"><select name="teacherRole" class="class_sub" id="teacherRole" onchange="changeTeacher()">
		                <option value="0">请选择</option>
		                <option value="4">领导</option>
		                <option value="32">年级主任</option>
		                <option value="2">心理老师</option>
		                <option value="8">班主任</option>
		                <option value="16">任课老师</option>
		                <option value="64">学校管理员</option>
		            </select></td>   
			  </tr>
			  <tr id="student" style="display:none">
			    <td width="14%" align="right">&nbsp;</td>	
			    <td width="87%"> 
			        <table width="100%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="studentlist" style="display:block">
						
				    </table>
				</td>   
			  </tr>
			  <tr>
			    <td colspan="2" align="center"><input type="button" value="上一步" class="class_sub" onClick="colonyUp()"> <input type="button" class="class_sub" value="下一步" onClick="unitNext()"></td>
			  </tr>
			</table>
			<table width="98%" border="0" align="center"  cellpadding="0" cellspacing="1" class="table_style" id="scale" style="display:none">
			  <tr>
			    <td class="left_title_3" colspan="2">量表分发 →&nbsp;第三步</td>
			  </tr>
			  <tr>
			    <td colspan="2" class="list_table_caption"><b>量表分发：量表信息</b></td>
			  </tr>
			   <tr>
			    <td width="14%" align="right">量表：</td>
			    <td width="86%">
			    	<table width="98%"  border="0" cellspacing="0" id="scalelist">    	
				    </table>
			    </label></td>   
			  </tr>
			  <tr>
			    <td colspan="2" align="center"><input type="button" value="上一步" class="class_sub" onClick="scaleUp()"> <input type="button" class="class_sub" value="分发" onClick="dispense('unitdispenseinfo.tec')"></td>
			  </tr>
			</table>
	
	</form>
	
	
<script type="text/javascript">

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

