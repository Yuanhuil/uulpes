<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/organization/schoolclass" />
<form id="upgradeform" action="${baseaction }/upgrade.do" method="post">
    <table style="width: 30%; margin-left: 250px; border-collapse: collapse;">
        <tr>
            <td style="text-align: right;">
                <img style="width: 100px; height: 100px;" src="${ctx }/themes/theme1/images/classupgrade.jpg" />
            </td>
            <td>
                <a id="uploadschoolclass" href="javascript:void(0);" onclick="update();">执行升级</a>
            </td>
        </tr>
    </table>
</form>
<div>
    <div style="float: left;">
        <img style="width: 100px; heiht: 100px; margin-top: 20px;" src="${ctx }/images/alert.png">
    </div>
    <div style="font-size: 14px;">
        提供学生跨学年自动升级操作，将上一学年学生自动升级到新学年；比如学年结束时，执行此功能后，则一年级一班的学生自动升级为 二年级一班，无须再单独为每个学生执行升级。<br>
        <br> 警告：升级操作之后，不可恢复；<br> 提示：新学年开学，应在导入新生之前执行升级，即将上一年学生信息升级到新学年之后，在添加入学新生信息。<br> 提示：
        升级如果产生毕业班，当前年，如【2016】，为毕业班的届别。<br> 毕业班学生执行升级操作以后，学生数据将封存，如需查看毕业生数据，请进入"
        <a href=''>毕业生数据管理</a>
        "。
    </div>
</div>
<script type="text/javascript">
    function update() {
        $.ajax({
            url : "${ctx }/systeminfo/sys/organization/schoolclass/upgrade.do",

            type : "POST",
            success : function(msg) {
                layer.alert("升级完成！");
            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                debugger;
                layer.alert("升级过程失败！");
            }

        });
    }
</script>