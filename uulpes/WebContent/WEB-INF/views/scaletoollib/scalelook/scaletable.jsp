<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>
.ui-dialog .ui-dialog-content {
    overflow: auto;
}
</style>
<c:set var="baseaction" value="${ctx }/scaletoollib/scalelook" />
<div class="tableContent">
    <form id="scaledownload"></form>
    <table>
        <tr class="titleBg">
            <th width="5%">序号</th>
            <th width="16%">量表名称</th>
            <th width="8%">英文简称</th>
            <th width="8%">题目数量</th>
            <th wdith="7%">常模</th>
            <th width="16%">适用人群</th>
            <th width="12%">量表类型</th>
            <th width="8%">测验时间</th>
            <th width="20%">操作</th>
        </tr>
        <c:forEach var="scale" items="${scaleList }" varStatus="status">
            <tr>
                <td>${status.index+1}</td>
                <td>${scale.title }</td>
                <td>${scale.shortname }</td>
                <td>${scale.questionNum }</td>
                <td>
                    <a href="javascript:void(0);" onclick="scalenorm('${baseaction }/${scale.code }/scalenorm.do');">进入</a>
                </td>
                <td>${scale.applicablePerson }</td>
                <td>${scale.scaleType }</td>
                <td>${scale.examtime }分</td>
                <td>
                    <span class="header02"><input class="button-normal blue look" id="${scale.code }"
                            type="button" value="查看" chref="${baseaction }/${scale.code }/view.do" /> <input
                            class="button-normal blue download" id="${scale.code }" type="button" value="下载"
                            chref="${baseaction }/${scale.code }/download.do" /> <shiro:hasPermission
                            name="assessmentcenter:checkscale:delete">
                            <c:if test="${scale.isOriginalFlag == 0}">
                                <input class="button-normal blue del" type="button" value="删除"
                                    chref="${baseaction }/${scale.code }/delete.do" />
                            </c:if>
                        </shiro:hasPermission></span>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div id="checkScaleDialog" style="display: none;">
    <%@include file="scaledetailinfo.jsp"%>
</div>
<iframe id="ifile" style="display: none"></iframe>
<script type="text/javascript">
    $(".button-normal.blue.look").click(function() {
        var h = $(this).attr("chref");
        $("#checkScaleDialog").load(h, function() {
            $("#checkScaleDialog").dialog({
                title : '查看量表',
                autoOpen : true,
                modal : true,
                width : 700,
                height : 600
            });
        });
    });
    $(".button-normal.blue.del").click(function() {
        if (confirm("确认删除此量表么？否则还得上传！")) {
            var h = $(this).attr("chref");
            $('#content2').load(h);
        }
    });
    $(".button-normal.blue.download").click(function() {
        var h = $(this).attr("chref");
        document.getElementById("ifile").src = h;
    });
    function downloadButton(scaleid, node) {
        layer.open({
            content : "wait"
        });
    }
    function scalenorm(h) {
        debugger;
        $('#content2').load(h);
    }

   function mouseleave(code,value){
        if(confirm('确定修改吗 ？')){
            var showTitle = value;
                $.ajax({
                    async:false,
                    type: "POST",
                    url: "/pes/scaletoollib/scalelook/updateShowTitle.do",//注意路径
                    data:{"code":code,"showtitle":showTitle},
                    dataType:"json",
                    success:function(data){
                        debugger;
                        if(data==true){
                            alert("修改成功");
                        }
                    }
                });
        }else{
            $("#showScaleTitle").load("/pes/scaletoollib/scalelook/listScales.do");
        }
    }
</script>