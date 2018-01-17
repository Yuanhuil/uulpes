<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/invest"/>
 <style type="text/css">
.scalepanel{
	display: none;
	width: 300px;
	height: 150px;
	background-color: #FFFFFF;
	border: 1px solid #333;
	padding: 12px;
	text-align: left;
	line-height: 175%;
	text-indent: 2em;
	position: absolute;
}
.scalepanel a{color:#000;text-decoration:underline;}
 </style>

<c:choose>
	<c:when test="${orgtype=='2' }">
		<c:set var="schoolAdminStuScalelook" value="${ctx }/scaletoollib/schoolAdminStuScalelook" />
		<div class="tableContent">
			<table>
				<tr class="titleBg">
					<th width="15%">调查名称</th>
					<th width="15%">问卷名称</th>
					<th width="12%">调查对象</th>
					<th width="10%">分发时间</th>
					<th width="18%">评测期限</th>
					<th width="15%">操作</th>
				</tr>
				<c:forEach var="investTask" items="${investTaskList }">
					<tr>
						<td>${investTask.name }</td>
						<td>${investTask.scalename }</td>
						<c:choose>
						  <c:when test="${investTask.objecttype==1 }">
						    <td id="td_${investTask.id}" onclick="showtarget(${investTask.id},'班级:${investTask.bjnames }','')" >
						  </c:when>
						  <c:when test="${investTask.objecttype==2 }">
						    <td id="td_${investTask.id}" onclick="showtarget(${investTask.id},'角色:${investTask.rolenames }','')" >
						  </c:when>
						</c:choose>
							<c:choose>
							  <c:when test="${ investTask.objecttype==1}">学生   点击查看</c:when>
							  <c:when test="${ investTask.objecttype==2}">教师   点击查看</c:when>
							  <c:otherwise>所有</c:otherwise> 
							</c:choose>
					
						</td>
						<td><fmt:formatDate type="date" value="${investTask.createtime }"
								 /></td>
						<td><fmt:formatDate type="date" value="${investTask.starttime }"
								/>至<fmt:formatDate
								value="${investTask.endtime }" type="date" /></td>
						<td><c:if test="${investTask.xfflag==1 and investTask.expireflag==0}">
								<a style="color:blue;" href="javascript:void(0)" onclick="investTransfer('${investTask.id }','sch');">下发</a>
								<!-- <input class="button-small white"
									id="${investTask.id }" type="button" value="下发"
									onclick="investTransfer('${investTask.id }','sch');">-->
							</c:if> <c:if test="${investTask.chflag==1 }">
							<a style="color:blue;" href="javascript:void(0)" onclick="withdraw('${investTask.id }',this,'sch');">撤回</a>
								<!-- <input class="button-small white" id="${investTask.id }"
									type="button" value="撤回"
									onclick="withdraw('${investTask.id }',this,'sch');"/>-->
							</c:if> 
							<c:if test="${investTask.chflag!=1&&investTask.xfflag!=1  }">
							<a style="color:blue;" href="${baseaction }/${investTask.id }/${investTask.scaleid }/investresult.do" target="_blank">查看结果</a> 
							</c:if></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:when>
	<c:otherwise>
		<c:set var="eduScaleStulook" value="${ctx }/scaletoollib/eduScaleStulook" />
		<div class="tableContent">
			<table>
				<tr class="titleBg">
					<th width="15%">调查名称</th>
					<th width="20%">问卷名称</th>
					<th width="11%">调查对象</th>
					<th width="10%">分发时间</th>
					<th width="18%">评测期限</th>
					<th width="15%">操作</th>
				</tr>
				<c:forEach var="investTask" items="${investTaskList }">
					<tr>
						<td>${investTask.name }</td>
						<td>${investTask.scalename }</td>
						<c:choose>
							<c:when test="${ orglevel==3&&investTask.objecttype==1}">
								<td id="td_${investTask.id}" onclick="showtarget(${investTask.id},'区县:${investTask.areanames }','年级:${investTask.njname }');" >
							</c:when>
							<c:when test="${ orglevel==3&&investTask.objecttype==2}">
								<td id="td_${investTask.id}" onclick="showtarget(${investTask.id},'区县:${investTask.areanames }','角色:${investTask.njname }');" >
							</c:when>
							<c:when test="${ orglevel==4&&investTask.objecttype==1}">
								<td id="td_${investTask.id}" onclick="showtarget(${investTask.id},'学校：${investTask.schoolnames }','年级:${investTask.njname }');" >
							</c:when>
							<c:when test="${ orglevel==4&&investTask.objecttype==2}">
								<td id="td_${investTask.id}" onclick="showtarget(${investTask.id},'学校：${investTask.schoolnames }','角色:${investTask.njname }');" >
							</c:when>
						</c:choose>
							<c:choose><c:when test="${ investTask.objecttype==1}">学生   点击查看</c:when>
							<c:when test="${ investTask.objecttype==2}">教师   点击查看</c:when></c:choose>
						</td>
						<td><fmt:formatDate type="date"
								value="${investTask.createtime }"  /></td>
						<td><fmt:formatDate type="date"
								value="${investTask.starttime }"  />至<fmt:formatDate
								value="${investTask.endtime }" type="date" /></td>
						<td><c:if test="${investTask.xfflag==1  and investTask.expireflag==0}">
								<span class="header02"><input class="button-small white"
									id="${investTask.id }" type="button" value="下发"
									onclick="investTransfer('${investTask.id }','edu');"></span>
							</c:if> <c:if test="${investTask.chflag==1 }">
								<input class="button-small white" id="${investTask.id }"
									type="button" value="撤回"
									onclick="withdraw('${investTask.id }',this,'edu');"/>
							</c:if> 
							<c:if test="${investTask.chflag!=1&&investTask.xfflag!=1  }">
							<a style="color:blue;" href="${baseaction }/${investTask.id }/${investTask.scaleid }/investresult.do" target="_blank">查看结果</a> 
							<!-- <input class="button-small white"
							id="${investTask.id }" type="button" value="结果查询"
							onclick="queryInvestResult('${investTask.id }');">--></c:if></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:otherwise>
</c:choose>

<div id="box" class="scalepanel" onmouseout="disappear()"></div>
<script  type="text/javascript" language="javascript" >
	function withdraw(itid, node,stype) {
		if (confirm("您确认要撤回分发的量表事件么？")) {
			$
					.ajax({
						type : "POST",
						url : "../../assessmentcenter/invest/checkProcessingStatus.do",
						data : {
							"itid" : itid,
							"stype":stype
						},
						success : function(msg) {
							debugger;
							if (msg) {
								// 直接从数据库中删除掉，并从页面中也删除掉								
								deleteInvestTask(itid, node,stype);
							} else {
								layer.open({content:"有进程已经开始，不允许撤销操作!"});
							}
						},
						error : function() {
							layer.open({content:"调用出现错误，删除失败"});
						}
					});
		}

	}

	function deleteInvestTask(itid, node,stype) {
		debugger;
		$.ajax({
			type : "POST",
			url : "../../assessmentcenter/invest/withdraw.do",
			data : {
				"itid" : itid,
				"stype": stype
			},
			success : function(msg) {
				if (msg == "success") {
					// 直接从数据库中删除掉，并从页面中也删除掉
					$(node).parent().parent().remove();
				} else {
					layer.open({content:"有进程已经开始，不允许撤销操作!"});
				}
			},
			error : function() {
				layer.open({content:"调用出现错误，删除失败"});
			}
		});
	}
	function investTransfer(itid,stype){
		$.ajax({
			type : "POST",
			url : "../../assessmentcenter/invest/investTransfer.do",
			data : {
				"itid" : itid,
				"stype": stype
			},
			success : function(msg) {
				if (msg == "success") {
					//
					layer.open({content:"下发成功!"});
				} else {
					layer.open({content:"下发操作失败!"});
				}
			},
			error : function() {
				layer.open({content:"调用出现错误，下发失败"});
			}
		});
	}
	
	function queryInvestResult(itid) {
		var url = "../../assessmentcenter/invest/queryInvestResult.do?itid="+ itid;
		$("#content2").load(url);
	}
	
	function disappear(){
	     document.getElementById("box").style.display="none"; 
	}
	function showtarget(id,content1,content2){
		 var td_id = "#td_"+id;
		 var td = $(td_id)[0];
		 var left = getLeft(td);
		 var top = getTop(td);
		 $("#box").css("left",left+"px");
		 $("#box").css("top",top+"px");
		 var content = "<p>"+content1+"</p><br><p>"+content2+"</p>";
		 
		 document.getElementById("box").innerHTML=content;
		 document.getElementById("box").style.display="block"; 
	}
	//获取元素的纵坐标
	function getTop(td){
		 var offset=td.offsetTop;
		 if(td.offsetParent!=null) offset+=getTop(td.offsetParent);
		 return offset;
	}
	//获取元素的横坐标
	function getLeft(td){
		 var offset=td.offsetLeft;
		 if(td.offsetParent!=null) offset+=getLeft(td.offsetParent);
		 return offset;
	}
	function aaa(a){
		layer.open({content:a});
	}
</script>