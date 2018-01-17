<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/scaletoollib/scalelook" />
<div class="tableContent">
	<table>
		<tr class="titleBg">
			<th width="15%">任务名称</th>
			<th width="10%">量表名称</th>
			<th width="8%">年级名称</th>
			<th width="8%">班级名称</th>
			<th width="15%">分发时间</th>
			<th width="27%">评测期限</th>
			<th width="17%">操作</th>
		</tr>
		<c:forEach var="examdoTaskSchool" items="${etsList }">
			<tr>
				<td>${examdoTaskSchool.taskname }</td>
				<td>${examdoTaskSchool.scalenames }</td>
				<td>${examdoTaskSchool.nj }</td>
				<td>${examdoTaskSchool.bj}</td>
				<td><fmt:formatDate value="${examdoTaskSchool.createtime }"
						type="both" /></td>
				<td><fmt:formatDate value="${examdoTaskSchool.starttime }"
						type="both" />-<fmt:formatDate
						value="${examdoTaskSchool.endtime }" type="both" /></td>
				<td>
				<c:if test="${examdoTaskSchool.xfflag==1 }">
					<span class="header02"><input
						class="button-small white" id="${examdoTaskSchool.id }"
						type="button" value="下发"
						onclick="nextTozwf('${examdoTaskSchool.id }',this);"></span>
				</c:if>
				<c:if test="${examdoTaskSchool.chflag==1 }">
					<input
						class="button-small white" id="${examdoTaskSchool.id }"
						type="button" value="撤回"
						onclick="withdrawstudent('${examdoTaskSchool.id }',this);"></span>
				</c:if>
					<input class="button-small white" style="width: 80px;"
					id="${examdoTaskSchool.id }" type="button" value="进程查询"
					onclick="queryprocessstudent('${examdoTaskSchool.id }');"></td>
			</tr>
		</c:forEach>
	</table>
</div>
<script>
	function withdrawstudent(etschoolid, node) {
		if (confirm("您确认要撤回分发的量表事件么？")) {
			$
					.ajax({
						type : "POST",
						url : "../../scaletoollib/monitorprocess/checkStudentProcessingStatus.do",
						data : {
							"etschoolid" : etschoolid
						},
						success : function(msg) {
							if (msg) {
								// 直接从数据库中删除掉，并从页面中也删除掉
								deleteEtschoolid(etschoolid, node);
							} else {
								layer.open({content:'有进程已经开始，不允许撤销操作!'});
							}
						},
						error : function() {
							layer.open({content:'调用出现错误，删除失败'});
						}
					});
		}

	}

	function deleteEtschoolid(etschoolid, node) {
		$.ajax({
			type : "POST",
			url : "../../scaletoollib/monitorprocess/deleteEtschoolid.do",
			data : {
				"etschoolid" : etschoolid
			},
			success : function(msg) {
				if (msg == "success") {
					// 直接从数据库中删除掉，并从页面中也删除掉
					$(node).parent().parent().parent().remove();
				} else {
					layer.open({content:'有进程已经开始，不允许撤销操作!'});
				}
			},
			error : function() {
				layer.open({content:'调用出现错误，删除失败'});
			}
		});
	}

	function queryprocessstudent(etschoolid) {
		var url = "../../scaletoollib/monitorprocess/lookStudentProcessingStatus.do?etschoolid="
				+ etschoolid;
		$("#content2").load(url);
	}
</script>