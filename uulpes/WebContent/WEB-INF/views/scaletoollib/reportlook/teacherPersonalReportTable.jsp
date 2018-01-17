<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/scaletoollib/reportlook" />
<div class="tableContent">
	<table class="table-fixed">
	  <thead>
		<tr class="titleBg">
			<th  width="3%"><input id="checkbox" class="checkbox01"  type="checkbox"
			name="checkbox"  ></th>
			<th width="7%">姓名</th>
			<th width="5%">性别</th>
			<th width="18%">身份证号</th>
			<th width="10%">角色名称</th>
			<th width="20%">量表名称</th>
			<!-- <th width="9%">题目数量</th>-->
			<th width="8%">用时</th>
			<th width="8%">报告</th>
			<th width="8%">答案</th>
			<th width="9%">典型个案</th>
			<th width="8%">效度</th>
		  </tr>
		</thead>
		<c:forEach var="examResultTeacher" items="${elistTeacherPersonalReport }">
			<tr>
			    <td>
					<input  id="cb_${examResultTeacher.id}"  class="checkbox01" type="checkbox" name="rowcheck"  value="${examResultTeacher.id }" />
				</td>
				<td class="td-fixed" title="${examResultTeacher.teacherName }">${examResultTeacher.teacherName }</td>
				<c:choose>
				  <c:when test="${examResultTeacher.gender == '1'}">
				    <td>男</td>
				  </c:when>
				  <c:when test="${examResultTeacher.gender == '2'}">
				    <td>女</td>
				  </c:when>
				  <c:otherwise> 
				     <td>未知</td>
				  </c:otherwise>
				</c:choose>
				<td>${examResultTeacher.indentify }</td>
				<td>${examResultTeacher.roleName }</td>
				<td class="td-fixed" title="${examResultTeacher.scaleName }">${examResultTeacher.scaleName }</td>
				<!-- <td>${examResultTeacher.questionnum }</td>-->
				<td>${examResultTeacher.testtime}</td>
				<td>
				     <a style="color:blue;" href="../../assessmentcenter/report/teacherPersonalReport.do?resultId=${examResultTeacher.id }&userid=${examResultTeacher.teacherId}"  target="_blank">查看</a>
				     <shiro:hasPermission name="assessmentcenter:checkreport:teacherreport:delete"><a style="color:blue;" href="#" onclick="delTeacherPersonalReport('${examResultTeacher.id }')">删除</a></shiro:hasPermission>
				</td>

				<td>
					<a style="color:blue;"  href="../../scaletoollib/reportlook/showTeacherAnswerAction.do?resultid=${examResultTeacher.id }"  target="_blank">查看</a>
					<a style="color:blue;"  href="../../scaletoollib/reportlook/showTeacherAnswerAction.do?resultid=${examResultTeacher.id }&export=yes">下载</a>
				</td>
				<td><c:choose>
							<c:when test="${examResultTeacher.warningGrade==1}">
								<img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
       						</c:when>
							<c:when test="${examResultTeacher.warningGrade==2}">
								<img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
								<img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
							</c:when>
							<c:when test="${examResultTeacher.warningGrade==3}">
            				  <img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
            				  <img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
            				  <img src="${ctx}/themes/${sessionScope.user.theme}/images/star.gif"/>
							</c:when>
							<c:when test="${examResultTeacher.warningGrade==0}">
             				  正常
							</c:when>
						</c:choose></td>
				<td>${examResultTeacher.validVal==0?'无效':'有效' }</td>
			</tr>
		</c:forEach>
	</table>
</div>
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
function delTeacherPersonalReport(resultid){
	if(confirm("删除报告将回到可测试状态，确认删除？")){
		var url ="../../assessmentcenter/report/delTeacherPersonalReport.do";
		$.ajax({
	      type: "POST",
	      url: url,
	      data:{resultid:resultid},
	      success: function(data){
	    	  debugger;
	    	  if(data=="success")
	    	  	$("#cb_"+resultid).parent().parent().remove(); 
	      }
	   });
	}
}
function delSelectedTeacherPersonalReports(resultid){
	if(confirm("删除报告将回到可测试状态，确认删除？")){
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
		var url ="../../assessmentcenter/report/delSelectedTeacherPersonalReports.do";
		$.ajax({
	      type: "POST",
	      url: url,
	      data:{resultids:str},
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
function downloadSelectedTeacherPersonalReports(userid){
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
	 var url ="../../assessmentcenter/report/downloadSelectedTeacherPersonalReports.do?resultids="+str;
		
	 document.getElementById("ifile").src=url;
		

}
</script>
