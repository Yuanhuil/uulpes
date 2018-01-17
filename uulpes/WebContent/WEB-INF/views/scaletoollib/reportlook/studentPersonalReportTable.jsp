<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/scaletoollib/reportlook" />
<div class="tableContent">
	<table class="table-fixed">
	  <thead>
		<tr class="titleBg">
			<th  width="3%"><input class="checkbox01"  type="checkbox"
			name="checkbox" id="checkbox" ></th>
			<th width="6%">姓名</th>
			<th width="6%">性别</th>
			<th width="12%">年级</th>
			<th width="8%">班级</th>
			<th width="20%">量表</th>
			<!-- <th width="6%">题数</th>-->
			<th width="8%">用时</th>
			<th width="12%">报告</th>
			<th width="10%">答案</th>
			<th width="9%">典型个案</th>
			<th width="6%">效度</th>
		  </tr>
		</thead>
		<c:forEach var="examresultStudent" items="${elistStudentPersonalReport }">
			<tr>
			    <td>
					<input id="cb_${examresultStudent.id}" class="checkbox01" type="checkbox" name="rowcheck"  value="${examresultStudent.id }" />
				</td>
				<td class="td-fixed" title="${examresultStudent.studentName }" >${examresultStudent.studentName }</td>
				<c:choose>
				  <c:when test="${examresultStudent.gender == '1'}">
				    <td>男</td>
				  </c:when>
				  <c:otherwise> 
				     <td>女</td>
				  </c:otherwise>
				</c:choose>
				<td>${examresultStudent.njmc }</td>
				<td>${examresultStudent.bjmc }</td>
				<td class="td-fixed" title="${examresultStudent.scaleName }">${examresultStudent.scaleName }</td>
				<!-- <td>${examresultStudent.questionnum }</td>-->
				<td>
				<c:if test="${examresultStudent.testtype==1 }">${examresultStudent.testtime}</c:if>
				<c:if test="${examresultStudent.testtype==2 }">导入</c:if>
				<c:if test="${examresultStudent.testtype==3 }">录入</c:if>
				</td>
				
				<!--<c:if test="${examresultStudent.scaleId==111000001||examresultStudent.scaleId==110100001 }">
				<td><c:if test="${typeflag==2&& examresultStudent.teacherVisible==true}"><a style="color:blue;" href="../../assessmentcenter/report/studentPersonalReport.do?resultId=${examresultStudent.id }&userid=${examresultStudent.studentId}"  target="_blank">三方共测</a></c:if>
				<c:if test="${role=='班主任'}"><a style="color:blue;" href="../../assessmentcenter/report/studentThreeAngleReportForTeacher.do?resultId=${examresultStudent.id }&userid=${examresultStudent.studentId}"  target="_blank">教师测评</a></c:if>
				 <a style="color:blue;" href="#" onclick="delStudentPersonalReport('${examresultStudent.id }','${examresultStudent.scaleId }')">删除</a> 
				</td>
				</c:if>-->
				
				<td><a style="color:blue;" href="../../assessmentcenter/report/studentPersonalReport.do?resultId=${examresultStudent.id }&userid=${examresultStudent.studentId}"  target="_blank">查看</a>
				               <a style="color:blue;" href="../../assessmentcenter/report/studentPersonalReport.do?resultId=${examresultStudent.id }&userid=${examresultStudent.studentId}&download=yes">下载</a>
				               <shiro:hasPermission name="assessmentcenter:checkreport:studentreport:delete">
				                   <a style="color:blue;" href="#" onclick="delStudentPersonalReport('${examresultStudent.id }','${examresultStudent.scaleId }')">删除</a>
				               </shiro:hasPermission> 
				               </td>
				<td>
				<a style="color:blue;"  href="../../scaletoollib/reportlook/showStudentAnswerAction.do?resultid=${examresultStudent.id }&threeangle=no"  target="_blank">查看</a>
				<a style="color:blue;"  href="../../scaletoollib/reportlook/showStudentAnswerAction.do?resultid=${examresultStudent.id }&export=yes&threeangle=no">下载</a>
				</td>
				<td><c:choose>
							<c:when test="${examresultStudent.warningGrade==1}">
								<img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
       						</c:when>
							<c:when test="${examresultStudent.warningGrade==2}">
								<img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
								<img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
							</c:when>
							<c:when test="${examresultStudent.warningGrade==3}">
            				  <img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
            				  <img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
            				  <img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
							</c:when>
							<c:when test="${examresultStudent.warningGrade==0}">
             				  正常
							</c:when>
						</c:choose></td>
				<td>${examresultStudent.validVal == 0?'无效':'有效' }</td>
			</tr>
		</c:forEach>
	</table>
</div>
	<form id="reportdownloadform" method="post">
	  <input id="resultids" name="resultids"  type="hidden"/>
	</form>
 <iframe id="ifile" style="display:none"></iframe>
<script type="text/javascript">
$("#checkbox").on('click',function(event){
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
function delStudentPersonalReport(resultid,scaleid){
	if(confirm("删除报告将回到可测试状态，确认删除？")){
		var url ="../../assessmentcenter/report/delstudentPersonalReport.do";
		$.ajax({
	      type: "POST",
	      url: url,
	      data:{
	    	  resultid:resultid,
	    	  scaleid:scaleid
	    	  },
	      success: function(data){
	    	  debugger;
	    	  if(data=="success")
	    	  	$("#cb_"+resultid).parent().parent().remove(); 
	      }
	   });
	}
}
function delSelectedStudentPersonalReports(resultid,scaleid){
	if(confirm("删除报告将回到可测试状态，确认删除？")){
		debugger;
		 var str="";
		 var i=0;
		 $("input:checkbox[name='rowcheck']:checked").each(function(){
			 i++;
		     str+=$(this).val()+",";
		 });
		 if(i<1){
			 layer.open({content:"请选中要删除的记录！"});
			 return;
		 }
		 str=str.substring(0,str.length-1);
		var url ="../../assessmentcenter/report/delSelectedStudentPersonalReports.do";
		$.ajax({
	      type: "POST",
	      url: url,
	      data:{
	    	  resultids:str,
	    	  scaleid:scaleid
	    	  },
	      success: function(data){
	    	  debugger;
	    	  if(data=="success")
	    		  $("input:checkbox[name='rowcheck']:checked").each(function(){
	 		     var rid =$(this).val();
	 		    $("#cb_"+rid).parent().parent().remove(); 
	 		 });
	      }
	   });
	}
}

function downloadSelectedStudentPersonalReports(userid){
	debugger;
	 var str="";
	 var i=0;
	 $("input:checkbox[name='rowcheck']:checked").each(function(){
		 i++;
	     str+=$(this).val()+",";
	 });
	 if(i<1){
		 layer.open({content:"请选中要下载的报告记录！"});
		 return;
	 }
	 str=str.substring(0,str.length-1);
	 var url ="../../assessmentcenter/report/downloadSelectedStudentPersonalReports.do?resultids="+str;
		
	 document.getElementById("ifile").src=url;
		

}
function downloadSelectedStudentPersonalReports1(userid){
	debugger;
	 var str="";
	 var i=0;
	 $("input:checkbox[name='rowcheck']:checked").each(function(){
		 i++;
	     str+=$(this).val()+",";
	 });
	 if(i<1){
		 layer.open({content:"请选中要下载的报告记录！"});
		 return;
	 }
	 str=str.substring(0,str.length-1);
	 var url ="../../assessmentcenter/report/downloadSelectedStudentPersonalReports.do";
		
		
		$('#reportdownloadform').attr("action", url);
		$('#resultids').attr("value", str);
		$('#reportdownloadform').ajaxSubmit();

}
function showThreeAngleWin(id,studentVisible,teacherVisible,parentVisible){
	var oEvent=event;
    var oDiv=document.createElement('div');
    oDiv.style.left=oEvent.clientX+'px';  // 指定创建的DIV在文档中距离左侧的位置
    oDiv.style.top=oEvent.clientY+'px';  // 指定创建的DIV在文档中距离顶部的位置
    oDiv.style.border='1px solid #FF0000'; // 设置边框
    oDiv.style.position='absolute'; // 
    oDiv.style.width='200px'; // 指定宽度
    oDiv.style.height='200px'; // 指定高度
    document.body.appendChild(oDiv); 
}
</script>