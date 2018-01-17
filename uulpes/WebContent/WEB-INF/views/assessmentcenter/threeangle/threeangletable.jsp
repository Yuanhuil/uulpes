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
			<th width="11%">年级名称</th>
			<th width="12%">班级名称</th>
			<th width="20%">量表名称</th>
			<th width="10%">测试</th>
			<th width="18%">报告</th>
			<th width="13%">答案</th>
		</tr>
		<c:forEach var="examdo" items="${examdoList }">
		    <tr class="titleBg">
			<td><input type="checkbox"></input></td>
			<td>${examdo.xm }</td>			
			<td>
				<c:if test="${examdo.xbm eq 1}">男</c:if>
				<c:if test="${examdo.xbm eq 2}">女</c:if>
			</td>
			<td>${examdo.njmc}</td>
			<td>${examdo.bjmc }</td>
			<td>${examdo.scalename }</td>
			
			<td>
			<c:if test="${empty examdo.oktime}"><a style="color:blue;" href="javascript:void(0);" onclick="startTest('${examdo.xm }','${examdo.userid }','${examdo.taskid }','${examdo.resultid}','${examdo.scaleid}','${typeflag }','${examdo.nj}','${examdo.bj }','${examdo.xbm}','${examdo.studentvisible}','${examdo.teachervisible}');">开始测试</a></c:if>
			</td>
			<td>
			<c:if test="${not empty examdo.oktime}">
				<c:if test="${typeflag==2}">
				    <a style="color:blue;" href="../../assessmentcenter/report/studentThreeAngleReportForTeacher.do?resultId=${examdo.resultid }&userid=${examdo.userid}"  target="_blank">班主任测评</a>
				</c:if>
				<c:if test="${typeflag==3}">
					<a style="color:blue;" href="../../assessmentcenter/report/studentThreeAngleReportForParent.do?resultId=${examdo.resultid }&userid=${examdo.userid}"  target="_blank">家长测评</a>
				</c:if>
				<c:if test="${exmdo.statusflag!=0 }"><a style="color:blue;" href="../../assessmentcenter/report/studentPersonalReport.do?resultId=${examdo.resultid }&userid=${examdo.userid}"  target="_blank">共测报告</a></c:if>
			
			</c:if>
			</td>
			
			
			<td><c:if test="${not empty examdo.oktime}">
			    <a style="color:blue;"  href="../../scaletoollib/reportlook/showStudentAnswerAction.do?resultid=${examdo.resultid }&threeangle=yes&v=${typeflag}"  target="_blank">查看</a>
				<a style="color:blue;"  href="../../scaletoollib/reportlook/showStudentAnswerAction.do?resultid=${examdo.resultid }&threeangle=yes&v=${typeflag}&export=yes">下载</a>
            </c:if></td>
		</tr>
		</c:forEach>
	</table>
</div>
<script>
function startTest(username,userid,taskid,resultid,scaleid,typeflag,nj,bj,gender,studentvisible,teachervisible){
	debugger;
	var url = "../../assessmentcenter/examtask/openstudentquestion.do?xm="+encodeURI(username)+"&userid="
		+ userid+"&taskid="+taskid+"&resultid="+resultid+"&scaleId="+scaleid+"&typeflag="+typeflag+"&nj="+nj+"&bj="+bj+"&gender="+gender+"&studentvisible="+studentvisible+"&teachervisible="+teachervisible+"&threeangletest=true";
$("#content2").load(url);
}
</script>