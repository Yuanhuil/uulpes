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
			<th width="5%">性别</th>
			<th width="18%">身份证号</th>
			<th width="18%">角色</th>
			<!-- <th width="18%">量表名称</th>
			<th width="8%">题目数量</th>-->
			<th width="17%">个人复合报告</th>
		 </tr>
	  </thead>
		<c:forEach var="examResultTeacher" items="${elistTeacherComplianceReport }">
			<tr>
				<td>
					<input class="checkbox01" type="checkbox" name="rowcheck"  value="${examResultTeacher.teacherId }" />
				</td>
				<td>${examResultTeacher.teacherName }</td>
				<c:choose>
				  <c:when test="${examResultTeacher.gender == '1'}">
				    <td>男</td>
				  </c:when>
				  <c:otherwise>
				     <td>女</td>
				  </c:otherwise>
				</c:choose>
				<td>${examResultTeacher.indentify }</td>
				<!-- <td>${examResultTeacher.scaleName }</td>-->
				<td>${examResultTeacher.roleName }</td>
				<!-- <td>${examResultTeacher.questionnum }</td>-->
				<td width="7%">
				  <a style="color:blue;" href="../../assessmentcenter/report/teacherCompositeReport.do?userid=${examResultTeacher.teacherId}"  target="_blank">查看</a>
				  <%-- <a style="color:blue;" href="../../assessmentcenter/report/downloadTeacherCompositeReport.do?userid=${examResultTeacher.teacherId}&download=yes">下载</a>  --%>
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