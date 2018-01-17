<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:choose>
	<c:when test="${orgType=='2' }">
		<c:set var="schoolAdminStuScalelook"
			value="${ctx }/scaletoollib/schoolAdminStuScalelook" />
		<div class="tableContent">
			<table class="table-fixed">
				<tr class="titleBg">
					<td width="20%">任务名称</td>
					<td width="15%">量表名称</td>
					<td width="10%">角色名称</td>
					<td width="10%">分发时间</td>
					<td width="15%">评测期限</td>
					<td width="20%">操作</td>
				</tr>
				<c:forEach var="examdoTaskSchool" items="${etsList }">
					<tr>
						<td>${examdoTaskSchool.taskname }</td>
						<td class="td-fixed" title="${examdoTaskSchool.scalenames }">${examdoTaskSchool.scalenames }</td>
						<td class="td-fixed" title="${examdoTaskSchool.rolename}">${examdoTaskSchool.rolename }</td>
						<td><fmt:formatDate  value="${examdoTaskSchool.createtime }"
								type="date" /></td>
						<td><fmt:formatDate value="${examdoTaskSchool.starttime }"
								type="date" />至<fmt:formatDate
								value="${examdoTaskSchool.endtime }" type="date" /></td>
						<td><c:if test="${examdoTaskSchool.xfflag==1  and examdoTaskSchool.expireflag==0}">
								<span class="header02"><input class="button-small white"
									id="${examdoTaskSchool.id }" type="button" value="下发"
									onclick="xiafaToSchool('${examdoTaskSchool.id }',this);"></span>
							</c:if> <c:if test="${examdoTaskSchool.chflag==1 }">
								<input class="button-small white" id="${examdoTaskSchool.id }"
									type="button" value="撤回"
									onclick="withdrawteacher('${examdoTaskSchool.id }',this,'sch');" />
							</c:if><c:if test="${examdoTaskSchool.chflag==0  and examdoTaskSchool.xfflag==0}"> <input class="button-small white"
							id="${examdoTaskSchool.id }" type="button" value="进程查询"
							onclick="queryprocessteacherforschool('${examdoTaskSchool.id }','${examdoTaskSchool.taskfrom }');"></c:if></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:when>
	<c:otherwise>
		<c:set var="eduScaleStulook"
			value="${ctx }/scaletoollib/eduScaleStulook" />
		<div class="tableContent">
			<table class="table-fixed">
				<tr class="titleBg">
					<td width="15%">任务名称</td>
					<td width="15%">量表名称</td>
					<td width="16%">对象</td>
					<td width="10%">范围</td>
					<td width="12%">分发时间</td>
					<td width="20%">评测期限</td>
					<td width="17%">操作</td>
				</tr>
				<c:forEach var="examdoTaskEducommission" items="${eteList }">
					<tr>
						<td class="td-fixed" title="${examdoTaskEducommission.taskname }">${examdoTaskEducommission.taskname }</td>
						<td class="td-fixed" title="${examdoTaskEducommission.scalenames }">${examdoTaskEducommission.scalenames }</td>
						<td class="td-fixed" title="${examdoTaskEducommission.rolename }">${examdoTaskEducommission.rolename }</td>
						<c:if test="${orgLevel==3 }">
						<td class="td-fixed" title="${examdoTaskEducommission.areanames}">${examdoTaskEducommission.areanames}</td>
						</c:if>
						<c:if test="${orgLevel==4 }">
						<td class="td-fixed" title="${examdoTaskEducommission.orgnames}">${examdoTaskEducommission.orgnames}</td>
						</c:if>
						<td><fmt:formatDate
								value="${examdoTaskEducommission.createtime }" type="date" /></td>
						<td><fmt:formatDate
								value="${examdoTaskEducommission.starttime }" type="date" />-<fmt:formatDate
								value="${examdoTaskEducommission.endtime }" type="date" /></td>
						<td><c:if test="${examdoTaskEducommission.xfflag==1 and examdoTaskSchool.expireflag==0}">
								<span class="header02"><input class="button-small white"
									id="${examdoTaskEducommission.id }" type="button" value="下发"
									onclick="xiafaToSchool('${examdoTaskEducommission.id }',this);"></span>
							</c:if> <c:if test="${examdoTaskEducommission.chflag==1 }">
								<input class="button-small white"
									id="${examdoTaskEducommission.id }" type="button" value="撤回"
									onclick="withdrawteacher('${examdoTaskEducommission.id }',this,'edu');" />
							</c:if><c:if test="${examdoTaskEducommission.chflag==0}"> <input class="button-small white"
							id="${examdoTaskSchool.id }" type="button" value="进程查询"
							onclick="queryprocessteacherforedu('${examdoTaskEducommission.id }',${orgLevel });"></c:if></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:otherwise>
</c:choose>
<script>

function xiafaToEdu(etid, node,stype) {
	$.ajax({
		type : "POST",
		url : "../../scaletoollib/monitorprocess/xiafaToEdu.do",
		data : {
			"etid" : etid,
			"objecttype":'1',
			"stype":stype
		},
		success : function(msg) {
			if (msg) {
				$(node).remove();
			} else {
				layer.open({content:"下发操作失败，请联系管理员!"});
			}
		},
		error : function() {
			layer.open({content:"调用出现错误，下发失败"});
		}
	});
}
	function withdrawteacher(etid, node, stype) {
		if (confirm("您确认要撤回分发的量表事件么？")) {
			$
					.ajax({
						type : "POST",
						url : "../../scaletoollib/monitorprocess/checkProcessingStatus.do",
						data : {
							"etid" : etid,
							"stype":stype
						},
						success : function(msg) {
							if (msg) {
								// 直接从数据库中删除掉，并从页面中也删除掉
								if (stype == 'sch') {
									deleteEtschoolid(2,etid, node);
								} else if (stype = 'edu') {
									deleteEtschoolidEdu(2,etid, node);
								}
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

	function deleteEtschoolid(typeflag,etschoolid, node) {
		$.ajax({
			type : "POST",
			url : "../../scaletoollib/monitorprocess/deleteEtschoolid.do",
			data : {
				"typeflag":typeflag,
				"etschoolid" : etschoolid
			},
			success : function(msg) {
				if (msg == "success") {
					// 直接从数据库中删除掉，并从页面中也删除掉
					$(node).parent().parent().parent().remove();
				} else {
					layer.open({content:"有进程已经开始，不允许撤销操作!"});
				}
			},
			error : function() {
				layer.open({content:"调用出现错误，删除失败"});
			}
		});
	}

	function deleteEtschoolidEdu(typeflag,etuid, node) {
		$.ajax({
			type : "POST",
			url : "../../scaletoollib/monitorprocess/deleteEudId.do",
			data : {
				"typeflag":typeflag,
				"etuid" : etuid
			},
			success : function(msg) {
				if (msg == "success") {
					// 直接从数据库中删除掉，并从页面中也删除掉
					$(node).parent().parent().parent().remove();
				} else {
					layer.open({content:"有进程已经开始，不允许撤销操作!"});
				}
			},
			error : function() {
				layer.open({content:"调用出现错误，删除失败"});
			}
		});
	}
	function queryprocessteacher(etschoolid) {
		var url = "../../scaletoollib/monitorprocess/lookTeacherProcessingStatus.do?etschoolid="
				+ etschoolid;
		//$("#content2").load(url);
		layer.open({area: ['700px','550px'],type: 2, content:url});
	}
	function queryprocessteacherforschool(etschoolid,taskfrom) {
		debugger;
		var url = "../../scaletoollib/monitorprocess/lookTeacherProcessingStatusForSchool.do?etschoolid="
				+ etschoolid+"&taskfrom="+taskfrom;
		//$("#content2").load(url);
		layer.open({area: ['700px','550px'],type: 2, content:url});
	}
	function queryprocessteacherforedu(etschoolid,orglevel) {
		debugger;
		var url=null;
		if(orglevel==3)
		    url = "../../scaletoollib/monitorprocess/lookTeacherProcessingStatusForCity.do?etschoolid="
				+ etschoolid;
		if(orglevel==4)
			url = "../../scaletoollib/monitorprocess/lookTeacherProcessingStatusForCounty.do?etschoolid="
				+ etschoolid;
		//$("#content2").load(url);
		layer.open({area: ['700px','550px'],type: 2, content:url});
	}

	function xiafaToSchool(etid, node,stype) {
	    var url = "../../scaletoollib/monitorprocess/xiafaToSchool.do?etid="
	        + etid+"&objecttype=2&stype="+stype;
	    $("#content2").load(url);
	}
</script>
