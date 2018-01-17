<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
 <form id="subform" action="${ctx }/systeminfo/sys/user/student/import.do"   method="post" enctype="multipart/form-data">  
	<table width="80%">
	  <tr>
	    <td>下载学生导入模板：</td>
	    <td>
	    	<input	class="button-normal blue download" 
					type="button" value="下 载" chref="${ctx }/systeminfo/sys/user/student/downloadstutemplate.do"/> 
	    </td>
	  </tr>
	  <tr>
	    <td>选择学生导入模板文件：</td>
	    <td><input type="file" name="file" width="80%" />
	        <input class="button-normal blue" id="uploadstudent" name="uploadstudent" type="button" value="上 传" />
	    </td>
	  </tr>
	</table>
   </form>
    <div id="dataloading" class="dataloading">
	
	</div>
    <div id="tablelist">
	    <%@include file="stuentimportresult.jsp"%>
	</div>
	<iframe id="ifile" style="display:none"></iframe>
<script type="text/javascript">
$(function(){
	$("#uploadstudent").click(function(evt){
		$("#dataloading").css("display","block");
		$("#subform").ajaxSubmit({
			target : "#content2",
			success : function (r) {
				$("#dataloading").css("display","none");
				//salert('导入数据成功！');
				layer.open({content:'导入数据成功！',
					yes:function(){
						layer.closeAll();
						$("#content2").load(ctx+"/systeminfo/sys/user/student.do");
					}
				});
		    },
			error:function(jqXHR, textStatus, errorThrown){
				$("#dataloading").css("display","none");
				//alert('导入数据失败！');
				layer.open({content:'错误: ' + jqXHR.responseText});
				//salert('错误: ' + jqXHR.responseText);
			}
		});
	});
	$(".button-normal.blue.download").click(function() {
		var h = $(this).attr("chref");
		document.getElementById("ifile").src=h;

	});
});
</script>