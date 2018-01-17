<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/scaletoollib/scalelook" />
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th width="3%"><input type="checkbox"></input></th>
			<th width="8%">姓名</th>
			<th width="6%">性别</th>
			<th width="10%">身份证号</th>
			<th width="13%">教师角色</th>
			<th width="25%">量表名称</th>
			<th width="10%">题目数量</th>
			<th width="12%">录入答案</th>
		</tr>
		<c:forEach var="examdo" items="${examdoList }">
		    <tr class="titleBg">
			<td><input type="checkbox"></input></td>
			<td>${examdo.xm }</td>
			<td><c:if test="${examdo.xbm eq 1}">男</c:if>
				<c:if test="${examdo.xbm eq 2}">女</c:if></td>
			<td>${examdo.sfzjh }</td>
			<td>${examdo.rolename}</td>
			<td>${examdo.scalename }</td>
			<td>${examdo.questionnum }</td>
			<td><input class="button-normal blue" style="width: 60px;"
					id="${examdoTaskSchool.id }" type="button" value="录入答案"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.roleid}','${examdo.xbm}');"/></td>
		</tr>
		</c:forEach>
	</table>
</div>
<script>
	function importAnswer(username,userid,taskid,resultid,scaleid,roleid,gender){
		var url = "../../assessmentcenter/examtask/openteacherquestion.do?xm="+encodeURI(username)+"&userid="
			+ userid+"&taskid="+taskid+"&resultid="+resultid+"&scaleId="+scaleid+"&roleid="+roleid+"&gender="+gender;
	$("#content2").load(url);
	}
</script>