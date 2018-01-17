<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th width="15%">全选</th>
			<th width="15%">姓名</th>
			<th width="10%">性别</th>
			<th width="8%">身份证号</th>
			<th width="8%">年级名称</th>
			<th width="8%">班级名称</th>
			<th width="17%">个人评语</th>
		</tr>
		<c:forEach var="examresultStudent" items="${elistStudentPersonalComments }">
			<tr>
				<td><input type="checkbox"></input></td>
				<td>${examresultStudent.studentName }</td>
				<td>${examresultStudent.gender }</td>
				<td>${examresultStudent.indentify }</td>
				<td>${examresultStudent.nj }</td>
				<td>${examresultStudent.bj }</td>
				<td width="7%">查看删除</td>
			</tr>
		</c:forEach>
	</table>
</div>