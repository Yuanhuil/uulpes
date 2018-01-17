<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tableContent">
	<table>
	  <thead>
		<tr class="titleBg">
			<th  width="2%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="sc_checkbox" ></th>
			<th width="10%">姓名</th>
			<th width="8%">性别</th>
			<th width="15%">身份证号</th>
			<th width="10%">年级名称</th>
			<th width="10%">班级名称</th>
			<th width="17%">个人复合报告</th>
		  </tr>
	  </thead>
		<c:forEach var="examresultStudent" items="${elistStudentComplianceReport }">
			<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${examresultStudent.studentId }" />
				</td>
				<td>${examresultStudent.studentName }</td>
				<c:choose>
				  <c:when test="${examresultStudent.gender == '1'}">
				    <td>男</td>
				  </c:when>
				  <c:otherwise> 
				     <td>女</td>
				  </c:otherwise>
				</c:choose>
				<td>${examresultStudent.indentify }</td>
				<td>${examresultStudent.njmc }</td>
				<td>${examresultStudent.bjmc }</td>
				<td width="7%">
				  <a style="color:blue;" href="../../assessmentcenter/report/studentCompositeReport.do?userid=${examresultStudent.studentId}"  target="_blank">查看</a>
				  <%-- <a style="color:blue;" href="../../assessmentcenter/report/downloadStuCompositeReport.do?userid=${examresultStudent.studentId}&download=yes">下载</a>  --%>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
<script type="text/javascript">
$("#sc_checkbox").on('click',function(event){
	var checkarray = document.getElementsByName("rowcheck");
	if(this.checked){
		for(var i=0;i<checkarray.length;i++){
			checkarray[i].checked=true;
		}
	}else{
		for(var i=0;i<checkarray.length;i++){
			checkarray[i].checked=false;
		}
	}
});
</script>