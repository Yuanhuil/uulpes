<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/scaletoollib/scalelook" />
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th width="3%"><input type="checkbox"></input></th>
			<th width="8%">姓名</th>
			<th width="7%">性别</th>
			<th width="15%">身份证号</th>
			<th width="10%">年级名称</th>
			<th width="9%">班级名称</th>
			<th width="20%">量表名称</th>
			<th width="8%">题目数量</th>
			<th >录入答案</th>
		</tr>
		<c:forEach var="examdo" items="${examdoList }">
		    <tr class="titleBg">
			<td ><input type="checkbox"></input></td>
			<td >${examdo.xm }</td>
			<td >
			    <c:if test="${examdo.xbm eq 1}">男</c:if>
				<c:if test="${examdo.xbm eq 2}">女</c:if>
			</td>
			<td >${examdo.sfzjh }</td>
			<td >${examdo.njmc}</td>
			<td >${examdo.bjmc }</td>
			<td >${examdo.scalename }</td>
			<td >${examdo.questionnum }</td>
			<td >
			<c:if test="${examdo.ifmh == true}">
			  <c:if test="${examdo.statusflag==0 }">
			        <input class="button-normal blue" 	type="button" value="学生版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','1');"/>
					 <input class="button-normal blue" 	type="button" value="教师版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','2');"/>
					 <input class="button-normal blue" 	type="button" value="家长版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','3');"/>
			  </c:if>
			<c:if test="${examdo.statusflag==1 }">
					 <input class="button-normal blue" 	type="button" value="教师版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','2');"/>
					 <input class="button-normal blue" 	type="button" value="家长版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','3');"/>
			  </c:if> 
			<c:if test="${examdo.statusflag==2 }">
			        <input class="button-normal blue" 	type="button" value="学生版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','1');"/>
					 <input class="button-normal blue" 	type="button" value="家长版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','3');"/>
			  </c:if>
			<c:if test="${examdo.statusflag==3 }">
					 <input class="button-normal blue" 	type="button" value="家长版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','3');"/>
			  </c:if>
			<c:if test="${examdo.statusflag==4 }">
			        <input class="button-normal blue" 	type="button" value="学生版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','1');"/>
					 <input class="button-normal blue" 	type="button" value="教师版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','2');"/>
			  </c:if>
			<c:if test="${examdo.statusflag==5 }">    
					 <input class="button-normal blue" 	type="button" value="教师版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','2');"/>
			  </c:if>
			<c:if test="${examdo.statusflag==6 }">
			        <input class="button-normal blue" 	type="button" value="学生版" style="padding:0px 0px 0px 0px;"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','1');"/>
			  </c:if>
			</c:if>
			<c:if test="${examdo.ifmh == false}">
			<input class="button-normal blue" 	type="button" value="录入"
					onclick="importAnswer('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}','${examdo.normid}','0');"/>
					</c:if></td>
		</tr>
		</c:forEach>
	</table>
</div>
<script>
	function importAnswer(username,userid,taskid,resultid,scaleid,nj,bj,gender,studentvisible,teachervisible,normid,version){
		var url = "../../assessmentcenter/examtask/openstudentquestion.do?xm="+encodeURI(username)+"&userid="
			+ userid+"&taskid="+taskid+"&resultid="+resultid+"&scaleId="+scaleid+"&nj="+nj+"&bj="+bj+"&gender="+gender+"&studentvisible="+studentvisible+"&teachervisible="+teachervisible+"&normid="+normid+"&v="+version;
	$("#content2").load(url);
	}
</script>