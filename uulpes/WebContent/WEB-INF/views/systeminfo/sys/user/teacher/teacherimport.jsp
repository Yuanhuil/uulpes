<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
 <form id="subform" action="${ctx }/systeminfo/sys/user/teacher/import.do"   method="post" enctype="multipart/form-data">  
	<table width="80%">
	  <tr>
	    <td>下载教师导入模板：</td>
	    <td>
	    	<input	class="button-normal blue download" 
					type="button" value="下 载" chref="${ctx }/systeminfo/sys/user/teacher/downloadtchtemplate.do"/> 
		</td>
	  </tr>
	  <tr>
	    <td>选择教师导入模板文件：</td>
	    <td><input type="file" name="file" width="80%" />
	        <input class="button-normal blue"  id="uploadteacher" name="uploadteacher" type="button" value="上 传" />
	    </td>
	  </tr>
	</table>
   </form>
    <div id="dataloading" class="dataloading">
	
	</div>
    <div id="tablelist">
	    <%@include file="teacherimportresult.jsp"%>
	</div>
	<iframe id="ifile" style="display:none"></iframe>
<script type="text/javascript">
$(function(){
	$("#uploadteacher").click(function(evt){
		$("#dataloading").css("display","block");
		$("#subform").ajaxSubmit({
			target : "#content2",
			success : function (r) {
				$("#dataloading").css("display","none");
            	layer.open({content:'导入数据成功！'});
            	$("#content2").load("${ctx }/systeminfo/sys/user/teacher.do");
			},
			error:function(jqXHR, textStatus, errorThrown){
				$("#dataloading").css("display","none");
				layer.open({content:'错误：'+jqXHR.responseText});
			}
		});
	});
	$(".button-normal.blue.download").click(function() {
		var h = $(this).attr("chref");
		document.getElementById("ifile").src=h;

	});
});
</script>