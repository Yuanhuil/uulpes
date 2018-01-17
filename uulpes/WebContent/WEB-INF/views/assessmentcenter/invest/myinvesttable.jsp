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
		<div class="tableContent">
			<table>
				<tr class="titleBg">
					<th width="15%">调查名称</th>
					<th width="15%">问卷名称</th>
					<th width="10%">分发时间</th>
					<th width="18%">评测期限</th>
					<th width="15%">操作</th>
				</tr>
				<c:forEach var="myInvestTask" items="${myInvesttaskList }">
				                       
					<tr>
						<td>${myInvestTask.taskname }</td>
						<td>${myInvestTask.scalename }</td>

						<td><fmt:formatDate type="date" value="${myInvestTask.createtime }"/></td>
						<td><fmt:formatDate type="date" value="${myInvestTask.starttime }"
								/>至<fmt:formatDate
								value="${myInvestTask.endtime }" type="date" /></td>
						<td><c:if test="${myInvestTask.resultid==null }">
							<a style="color:blue;" href="javascript:void(0);" onclick="doInvest('${myInvestTask.taskid }','${myInvestTask.scaleid }');">参与调查</a>
								
							</c:if><c:if test="${!empty myInvestTask.resultid }"> <a style="color:blue;" href="${baseaction }/${myInvestTask.taskid }/${myInvestTask.scaleid }/investresult.do" target="_blank">查看结果</a></c:if> </td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:when>

</c:choose>

<div id="box" class="scalepanel" onmouseout="disappear()"></div>
<script  type="text/javascript" >
$(function(){
	$(".button-small.white.result").click(function() {
		debugger;
		var h = $(this).attr("chref");
		$('#content2').load(h);
	});
});
	function doInvest(taskid,scaleid){
		var url = "../../assessmentcenter/invest/doInvest.do?taskid="+taskid+"&scaleid="+scaleid;
		$("#content2").load(url);
	}
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