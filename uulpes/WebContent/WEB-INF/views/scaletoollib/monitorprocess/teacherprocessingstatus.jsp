<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js"></script>
<script src="${ctx}/js/layer/layer.js" type="text/javascript"></script>
<style>
.table-fixed {
    table-layout: fixed;
    font-size: 13;
    border-collapse: collapse;
    margin: auto;
}

tr {
    background-color: #ffffff;
    line-height: 36px;
    text-align: left;
    padding: 10px;
}

th {
    background-color: #f0f0f0;
    line-height: 36px;
    text-align: left;
    padding: 0 6px;
}

td {
    background-color: #ffffff;
    line-height: 36px;
    text-align: left;
    padding: 0 6px;
    text-overflow: ellipsis;
    /*white-space: nowrap; */
    overflow: hidden;
}

.td-fixed {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.titleBg {
    background-color: #D4D4D4;
}
</style>
<div style="text-align: center;">
    <table border="1" class="table-fixed">
        <c:choose>
            <c:when test="${orglevel==2||orglevel==3||orglevel==4 }">
                <tr class="titleBg">
                    <td width="21%">量表名称</td>
                    <td width="10%">机构名称</td>
                    <td width="18%">角色名称</td>
                    <td width="18%">未测人数</td>
                    <td width="15%">总人数</td>
                    <td width="28%">评测进程</td>
                </tr>
                <c:forEach var="scaleProcessStruct" items="${resultList }">
                    <tr>
                        <td>${scaleProcessStruct.scaleName }</td>
                        <td>${scaleProcessStruct.orgName }</td>
                        <td>${scaleProcessStruct.roleName }</td>
                        <td>${scaleProcessStruct.noTestPerson}</td>
                        <td>${scaleProcessStruct.totalPerson }</td>
                        <td>${scaleProcessStruct.percentage}</td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr class="titleBg">
                    <td width="30%">量表名称</td>
                    <td width="15%">角色名称</td>
                    <td width="15%">未测人数</td>
                    <td width="10%">未测名单</td>
                    <td width="15%">总人数</td>
                    <td width="15%">评测进程</td>
                </tr>
                <c:forEach var="ScaleProcessStruct" items="${resultList }">
                    <tr>
                        <td>${ScaleProcessStruct.scaleName }</td>
                        <td>${ScaleProcessStruct.roleName }</td>
                        <td>${ScaleProcessStruct.noTestPerson}</td>
                        <td>
                            <a href="javascript:void(0);"
                                onclick="getNoTestDetail('${ScaleProcessStruct.orgid}','${ScaleProcessStruct.roleid}','${ScaleProcessStruct.taskId}','${ScaleProcessStruct.scaleid}');">查看</a>
                        </td>
                        <td>${ScaleProcessStruct.totalPerson }</td>
                        <td>${ScaleProcessStruct.percentage}</td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </table>
</div>
<script type="text/javascript">
    function getNoTestDetail(orgid, roleid, taskid, scaleid) {
        debugger;
        $
                .ajax({
                    type : "POST",
                    url : "../../scaletoollib/monitorprocess/getTeacherNoTestDetail.do",
                    data : {
                        "orgid" : orgid,
                        "roleid" : roleid,
                        "taskId" : taskid,
                        "scaleid" : scaleid
                    },
                    success : function(msg) {
                        debugger;
                        if (msg != '[]') {
                            var names = "<table style='border:1px solid #ccc;border-collapse:collapse;width:100%;font-size:11px;'>";
                            names += "<tr><th style='border:1px solid #ccc;'>姓名</th><th style='border:1px solid #ccc;'>身份证件号</th></tr>";
                            var callbackarray = msg;
                            for (var i = 0; i < callbackarray.length; i++) {
                                names += "<tr><td style='border:1px solid #ccc;width:100px;'>";
                                if (callbackarray[i] != null) {
                                    names += callbackarray[i].xm;
                                    names += "</td><td style='border:1px solid #ccc;width:200px'>";
                                    names += callbackarray[i].sfzjh;
                                }
                                names += "</td></tr>";
                            }
                            names += "</table>";
                            //layer.open({content: names});
                            layer.alert(names, {
                                title : "未测人员名单",
                                offset : 0,
                                shift : 6,
                                icon : 5
                            });
                        } else {

                        }
                    },
                    error : function() {
                        //layer.open({content:"获取未测学生名单出错!"});
                        layer.alert("获取未测学生名单出错!", {
                            offset : 0,
                            shift : 6,
                            icon : 5
                        });
                    }
                });
    }
</script>
