<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:choose>
    <c:when test="${orgType=='2' }">
        <c:set var="schoolAdminStuScalelook" value="${ctx }/scaletoollib/schoolAdminStuScalelook" />
        <div class="tableContent">
            <table class="table-fixed">
                <tr class="titleBg">
                    <th width="15%">任务名称</th>
                    <th width="20%">量表名称</th>
                    <th width="10%">年级名称</th>
                    <th width="8%">班级名称</th>
                    <th width="10%">分发时间</th>
                    <th width="18%">评测期限</th>
                    <th width="17%">操作</th>
                </tr>
                <c:forEach var="examdoTaskSchool" items="${etsList }">
                    <tr>
                        <td class="td-fixed">${examdoTaskSchool.taskname }</td>
                        <td class="td-fixed" title="${examdoTaskSchool.scalenames}">${examdoTaskSchool.scalenames }</td>
                        <td class="td-fixed" title="${examdoTaskSchool.njmc}">${examdoTaskSchool.njmc }</td>
                        <td class="td-fixed" title="${examdoTaskSchool.bjmc}">${examdoTaskSchool.bjmc}</td>
                        <td>
                            <fmt:formatDate type="date" value="${examdoTaskSchool.createtime }" />
                        </td>
                        <td>
                            <fmt:formatDate type="date" value="${examdoTaskSchool.starttime }" />
                            至
                            <fmt:formatDate value="${examdoTaskSchool.endtime }" type="date" />
                        </td>
                        <td>
                            <c:if test="${examdoTaskSchool.xfflag==1 and examdoTaskSchool.expireflag==0}">
                                <span class="header02"><input class="button-small white"
                                        id="${examdoTaskSchool.id }" type="button" value="下发"
                                        onclick="xiafaToSchool('${examdoTaskSchool.id }',this,'sch');"></span>
                            </c:if>
                            <c:if test="${examdoTaskSchool.chflag==1 }">
                                <input class="button-small white" id="${examdoTaskSchool.id }" type="button" value="撤回"
                                    onclick="withdrawstudent('${examdoTaskSchool.id }',this,'sch');" />
                            </c:if>
                            <c:if test="${examdoTaskSchool.chflag==0 and examdoTaskSchool.xfflag==0}">
                                <input class="button-small white" id="${examdoTaskSchool.id }" type="button" value="查看"
                                    onclick="queryprocessstudentforschool('${examdoTaskSchool.id }','${examdoTaskSchool.taskfrom }');">
                            </c:if>
                                <input type="button" class="button-small white" value="延期"
                                    onclick="delayEndTime('${examdoTaskSchool.id }')">
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <c:set var="eduScaleStulook" value="${ctx }/scaletoollib/eduScaleStulook" />
        <div class="tableContent">
            <table class="table-fixed">
                <tr class="titleBg">
                    <th width="15%">任务名称</th>
                    <th width="20%">量表名称</th>
                    <th width="16%">对象</th>
                    <th width="10%">范围</th>
                    <th width="12%">分发时间</th>
                    <th width="22%">评测期限</th>
                    <th width="17%">操作</th>
                </tr>
                <c:forEach var="examdoTaskEducommission" items="${eteList }">
                    <tr>
                        <td class="td-fixed" title="${examdoTaskEducommission.taskname }">${examdoTaskEducommission.taskname }</td>
                        <td class="td-fixed" title="${examdoTaskEducommission.scalenames }">${examdoTaskEducommission.scalenames }</td>
                        <td class="td-fixed" title="${examdoTaskEducommission.gradeTitles }">${examdoTaskEducommission.gradeTitles }</td>
                        <c:if test="${orgLevel==3 }">
                            <td class="td-fixed" title="${examdoTaskEducommission.areanames}">${examdoTaskEducommission.areanames}</td>
                        </c:if>
                        <c:if test="${orgLevel==4 }">
                            <td class="td-fixed" title="${examdoTaskEducommission.orgnames }">${examdoTaskEducommission.orgnames}</td>
                        </c:if>
                        <td>
                            <fmt:formatDate type="date" value="${examdoTaskEducommission.createtime }" />
                        </td>
                        <td>
                            <fmt:formatDate type="date" value="${examdoTaskEducommission.starttime }" />
                            至
                            <fmt:formatDate value="${examdoTaskEducommission.endtime }" type="date" />
                        </td>
                        <td>
                            <c:if test="${examdoTaskEducommission.xfflag==1 and examdoTaskEducommission.expireflag==0}">
                                <span class="header02"><input class="button-small white"
                                        id="${examdoTaskEducommission.id }" type="button" value="下发"
                                        onclick="xiafaToEdu('${examdoTaskEducommission.id }',this,'edu');"></span>
                            </c:if>
                            <c:if test="${examdoTaskEducommission.chflag==1 }">
                                <input class="button-small white" id="${examdoTaskEducommission.id }" type="button"
                                    value="撤回" onclick="withdrawstudent('${examdoTaskEducommission.id }',this,'edu');" />
                            </c:if>
                            <c:if test="${examdoTaskEducommission.chflag==0 }">
                                <input class="button-small white" id="${examdoTaskSchool.id }" type="button" value="查看"
                                    onclick="queryprocessstudentforedu('${examdoTaskEducommission.id }',${orgLevel });">
                            </c:if>
                                <input type="button" class="button-small white" value="延期"
                                    onclick="delayEndTime('${examdoTaskEducommission.id }')">
                        </td>
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
function xiafaToSchool(etid, node,stype) {
	var url = "../../scaletoollib/monitorprocess/xiafaToSchool.do?etid="
		+ etid+"&objecttype=1&stype="+stype;
	$("#content2").load(url);
}
	function withdrawstudent(etid, node,stype) {
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
								if(stype=='sch'){
									deleteSchoolTaskStudentId(1,etid, node);
								}
								else if(stype='edu'){
									deleteEduTaskStudentId(1,etid, node);
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

	function deleteSchoolTaskStudentId(typeflag,etschoolid, node) {
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

	function deleteEduTaskStudentId(typeflag,etuid, node){
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
	function queryprocessstudentforschool(etschoolid,taskfrom) {
		debugger;
		var url = "../../scaletoollib/monitorprocess/lookStudentProcessingStatusForSchool.do?etschoolid="
				+ etschoolid+"&taskfrom="+taskfrom;
		//$("#content2").load(url);
		layer.open({area: ['750px','550px'],type: 2, content:url});
	}
	function queryprocessstudentforedu(etschoolid,orglevel) {
		debugger;
		var url=null;
		if(orglevel==3)
		    url = "../../scaletoollib/monitorprocess/lookStudentProcessingStatusForCity.do?etschoolid="
				+ etschoolid;
		if(orglevel==4)
			url = "../../scaletoollib/monitorprocess/lookStudentProcessingStatusForCounty.do?etschoolid="
				+ etschoolid;
		//$("#content2").load(url);
		layer.open({area: ['750px','550px'],type: 2, content:url});
	}
	function delayEndTime(etschoolid) {
		$.ajax({
			type : "POST",
			url : "../../scaletoollib/monitorprocess/delayEndTime.do",
			data : {
				"etschoolid" : etschoolid
			},
			success : function(msg) {
				if (msg == "success") {
				    debugger;
					layer.open({content:"已将此任务延期一周!"});
					$("#studentsdispatched").empty().load("../../scaletoollib/monitorprocess/taskmanageOfStudent.do");
				} else {
					layer.open({content:"延期失败,请重试"});
				}
			},
			error : function() {
				layer.open({content:"延期失败,请重试"});
			}
		});
	}
</script>