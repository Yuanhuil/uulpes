<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form id="subform" action="${pageContext.request.contextPath}/assessmentcenter/scalemanager/importscale.do"
    method="post" enctype="multipart/form-data">
    <table width="80%">
        <tr>
            <td>下载量表导入模板：</td>
            <td>
                <a href="javascript:void(0);" onclick="downloadtemplate('导入量表_新模板');">下载</a>
            </td>
        </tr>
        <tr>
            <td>选择量表模板文件：</td>
            <td>
                <input type="file" name="file" width="80%" />
                <input id="uploadscale" type="button" value="上 传" />
            </td>
        </tr>
    </table>
</form>
<form id="subform1" action="${pageContext.request.contextPath}/assessmentcenter/scalemanager/importscalequestionword.do"
    method="post" enctype="multipart/form-data">
    <table width="80%">
        <tr>
            <td>选择量表题本文件：</td>
            <td>
                <input type="file" name="file1" width="80%" />
                <input type="submit" value="上 传" />
            </td>
        </tr>
    </table>
</form>
<iframe id="ifile" style="display: none"></iframe>
<div id="dataloading" class="dataloading"></div>
<div id="tablelist">
    <%@include file="scaleimportresult.jsp"%>
</div>
<div id="tablelist1"></div>
<script type="text/javascript">
    $(function() {
        $("#uploadscale").click(function(evt) {
            $("#dataloading").css("display", "block");
            $("#subform").ajaxSubmit({
                target : "#tablelist",
                success : function(r) {
                    debugger;
                    $("#dataloading").css("display", "none");
                    if (r.status == 'error') {

                    } else if (r.status == 'success') {

                    }
                },
                error : function(XmlHttpRequest, textStatus, errorThrown) {
                    debugger;
                    $("#dataloading").css("display", "none");
                    layer.open({
                        content : '错误: ' + XmlHttpRequest.responseText
                    });
                }
            });
        });

        $("#subform1").ajaxForm({
            target : "#tablelist1",
            success : function(r) {
                layer.alert("上传题本成功!");
            },
            error : function(XmlHttpRequest, textStatus, errorThrown) {
                layer.alert("上传题本失败!");
            }
        });

    });
    function downloadtemplate(fname) {
        var url = "${ctx }/workschedule/activityrecord/" + fname + "/downloadtemplate.do";
        document.getElementById("ifile").src = url;
    }
</script>