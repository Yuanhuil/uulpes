<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<table id="content" class="table table-hover">
	<thead>
		<tr>


			<th>开始时间</th>
			<th>结束时间</th>
			<th>量表名称</th>
			<th>测试状态</th>
			<th>完成时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="m">
			<tr>
				<td><a><fmt:formatDate value="${m.loTime}"
							pattern="yyyy-MM-dd" /> </a></td>
				<td><a><fmt:formatDate value="${m.hiTime}"
							pattern="yyyy-MM-dd" /> </a></td>
				<td><a>${scaleId[m.scaleId]}</a></td>
				<td>
				<c:if test="${m.okTime==null&&currentTime>m.hiTime.time}">已过期</c:if>
				<c:if test="${m.okTime==null&&currentTime<m.hiTime.time&&currentTime>m.loTime.time}">未测试</c:if>
				<c:if test="${m.okTime!=null}">测试完成</c:if>
				</td>
				<td><a><fmt:formatDate value="${m.okTime}"
							pattern="yyyy-MM-dd" /></a></td>


				<c:if test="${m.ifmh==false}">
				<td><input class="button-small white test" id="${m.id}"  studentvisible="${m.studentvisible}"
					taskid="${m.taskid}" scaleid="${m.scaleId}" xm="${m.xm}" type="button"
					style="display:${(m.okTime==null&&currentTime<m.hiTime.time&&currentTime>m.loTime.time)?'block':'none'}"
					value="测试"> 
	
					<a  href="../../assessmentcenter/report/${SOrT}PersonalReport.do?resultId=${m.id}&userid=${m.userId}" style="color:blue;display:${(m.okTime!=null)?'block':'none'}"  target="_blank">查看报告</a>
					</td>
				</c:if>
				<c:if test="${m.ifmh==true}">
				  <td>
				  	<c:if test="${m.statusflag==0||m.statusflag==2||m.statusflag==4||m.statusflag==6 }">
				  	
				  	<input class="button-small white test" id="${m.id}"  studentvisible="${m.studentvisible}"
					taskid="${m.taskid}" scaleid="${m.scaleId}" xm="${m.xm}" type="button" style="display:${(currentTime<m.hiTime.time&&currentTime>m.loTime.time)?'block':'none'}"
					value="测试"> 
					
					</c:if>
					<c:if test="${m.statusflag==1||m.statusflag==3||m.statusflag==5||m.statusflag==7 }">
						<a style="color:blue;" href="/pes/assessmentcenter/report/studentThreeAngleReportForStudent.do?resultId=${m.id }&userid=${m.userId}"  target="_blank">自测报告</a>
					</c:if>
					<c:if test="${m.statusflag!=0}"><a style="color:blue;" href="../../assessmentcenter/report/studentPersonalReport.do?resultId=${m.id }&userid=${m.userId}"  target="_blank">共测报告</a></c:if>
				  </td>
				</c:if>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div id="pagediv"></div>



<script type="text/javascript">
	$(function() {
		$("#pagediv").jstlPaginator({
			showtotalPage : true,
			showgotoPage : true,
			currentPage : "${page.currentPage}",
			totalPages : "${page.totalPage}",
			totalNumbers : "${page.totalResult}",
			onPageClicked : function(event, originalEvent, page) {
				$("#queryform").ajaxSubmit({
					data : {
						"currentPage" : page
					},
					target : "#list",
				});
			},

		});

	});

	jQuery('.test').bind(
			'click',
			function() {
				$("#dialog-form1").dialog("open");
				var id = jQuery(this).attr("id");
				var taskid = jQuery(this).attr("taskid");
				var scaleid = jQuery(this).attr("scaleid");
				var xm = 	jQuery(this).attr("xm");	 
				var studentvisible = jQuery(this).attr("studentvisible");
				 		 
				var h = '/pes/assessmentcenter/examtask/openquestion.do?xm='+ encodeURI(xm) + '&scaleId='
						+ scaleid + '&taskid=' + scaleid + '&resultid=' + id+'&studentvisible='+studentvisible;
				$('#content2').html();
				$('#content2').load(h, function() {
					$("#dialog-form1").dialog("open");
				});
			});
	
	function startThreeAngleTest(username,userid,taskid,resultid,scaleid,typeflag,nj,bj,gender,studentvisible,teachervisible){
		debugger;
		var url = "/pes/assessmentcenter/examtask/openstudentquestion.do?xm="+encodeURI(username)+"&userid="
			+ userid+"&taskid="+taskid+"&resultid="+resultid+"&scaleId="+scaleid+"&typeflag="+typeflag+"&nj="+nj+"&bj="+bj+"&gender="+gender+"&studentvisible="+studentvisible+"&teachervisible="+teachervisible;
	$("#content2").load(url);
	}
	/* jQuery('.seeValue')
			.bind(
					'click',
					function() {
						$("#dialog-form1").dialog("open");
						var userid = jQuery(this).attr("userid");
						var taskid = jQuery(this).attr("id");
						var SOrT=jQuery(this).attr("SOrT");
						
						var h = '/pes/assessmentcenter/report/'+SOrT+'PersonalReport.do?userid='
								+ userid + '&resultId=' + taskid;
						$('#list').html();
						$('#list').load(h, function() {
							$("#dialog-form1").dialog("open");
						});
					}); */
</script>