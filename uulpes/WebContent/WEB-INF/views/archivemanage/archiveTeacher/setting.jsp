<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form id="achiveform" action="/pes/archivemanage/archiveGraduatedStudent/view.do">
<div id="editDiv" style="display:none">
	<input type="hidden" id="velueSelect">
	<div id="u1" class="text" style="text-align:center;">
		<h1>
			<span>生成档案设置</span>
		</h1>
	</div>
	<c:set var="currentCol" value="${0}"/>
	<c:set var="colNum" value="${6}"/>
	<table>
		<tr><th colspan="6">学生背景信息设置</th></tr>
		<tr>
		<c:forEach var="attr" items="${attr_list}">
		<td>

		    <input type='checkbox' name="attrIds" checked="checked" value="${attr.id}"/>${attr.label}

		
		<c:set var="currentCol" value="${currentCol+1}"/>
		<c:if test="${currentCol==colNum}"><c:set var="currentCol" value="${0}"/></tr>
		<tr></c:if>
		</c:forEach>
		<c:if test="${currentCol!=0&&currentCol < colNum}" >
		<c:forEach begin="${currentCol}" end="${colNum-1}" step="1">
		<td>&nbsp;</td>
		</c:forEach>
		</c:if>
		</tr>	
	</table>
	
	
	<br>
	
	<table>
		
		<tr>
			<td width="15%">心理测评信息<input id="cpcheckbox" name="cpcheckbox" type="checkbox"  checked="checked"></td>
			<td width="70%"></td>
		</tr>
		
		<tr>
			<td width="15%">心理辅导信息<input id="fdcheckbox" name="fdcheckbox" type="checkbox"  checked="checked"></td>
			<td width="70%">
			</td>
		</tr>
		<tr>
			<td width="15%">危机干预信息<input id="gycheckbox" name="gycheckbox" type="checkbox"  checked="checked"></td>
			<td width="70%">
		</tr>
		<tr>
			<td width="15%">时间设置</td>
			<td width="70%">
				学年
			<select id="startschoolyear"  name="startschoolyear" class="select_100" >
				<option value="">请选择</option>
				<c:forEach items="${schoolyears}" var="year">
				<option value="${year }">${year }</option>
				</c:forEach>
			</select>
			学期
			<select  id="startterm" name="startterm" class="select_100" >
				<option value="">请选择</option>
				<c:forEach items="${schoolterm}" var="term">
				<option  value="${term.id }">${term.name }</option>
				</c:forEach>
			</select>
			
			
			------学年
			<select id="endschoolyear" name="endschoolyear" class="select_100" >
				<option value="">请选择</option>
				<c:forEach items="${schoolyears}" var="year">
				<option value="${year }">${year }</option>
				</c:forEach>
			</select>
			学期
			<select  id="endterm" name="endterm" class="select_100" >
				<option value="">请选择</option>
				<c:forEach items="${schoolterm}" var="term">
				<option  value="${term.id }">${term.name }</option>
				</c:forEach>
			</select>
			</td>
		</tr>
		
	</table>
</div>
</form>
<script>
$("#fdstarttime").datepicker(
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

$("#fdendtime").datepicker(
		 { //绑定开始日期
			 dateFormat : 'yy-mm-dd',
			 changeMonth : true, //显示下拉列表月份
			 changeYear : true, //显示下拉列表年份
			 firstDay : "1" ,//设置开始为1号
			onSelect : function(dateText, inst) {
				//设置开始日期的最大日期
				 $("#starttime").datepicker("option","maxDate",dateText);
			}
		});
$("#gystarttime").datepicker(
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

$("#gyendtime").datepicker(
		 { //绑定开始日期
			 dateFormat : 'yy-mm-dd',
			 changeMonth : true, //显示下拉列表月份
			 changeYear : true, //显示下拉列表年份
			 firstDay : "1" ,//设置开始为1号
			onSelect : function(dateText, inst) {
				//设置开始日期的最大日期
				 $("#starttime").datepicker("option","maxDate",dateText);
			}
		});
</script>
