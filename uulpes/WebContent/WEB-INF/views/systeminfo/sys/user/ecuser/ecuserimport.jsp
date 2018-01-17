<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
 <form id="subform" action="${ctx }/systeminfo/sys/user/ecuser/import.do"   method="post" enctype="multipart/form-data">  
	<table width="80%">
	  <tr>
	    <td>下载教委人员导入模板：</td>
	    <td><a href="${ctx }/systeminfo/sys/user/ecuser/downloadecusertemplate.do" >下载</a></td>
	  </tr>
	  <tr>
	    <td>选择教委人员导入模板文件：</td>
	    <td><input type="file" name="file" width="80%" />
	        <input id="uploadecuser" name="uploadecuser" type="button" value="上 传" />
	    </td>
	  </tr>
	</table>
   </form>
    <div id="dataloading" class="dataloading">
	
	</div>
    <div id="tablelist">
	    <%@include file="ecuserimportresult.jsp"%>
	</div>
<script type="text/javascript">
$(function(){
	$("#uploadecuser").click(function(evt){
		$("#dataloading").css("display","block");
		$("#subform").ajaxSubmit({
			target : "#content2",
			success : function (r) {
				debugger;
				$("#dataloading").css("display","none");
            	layer.open({content:'导入数据成功！'});
            	$("#content2").load("${ctx }/systeminfo/sys/user/ecuser.do");
			},
			error:function(jqXHR, textStatus, errorThrown){
				$("#dataloading").css("display","none");
				layer.open({content:'错误：'+jqXHR.responseText});
			}
		});
	});
});
</script>

