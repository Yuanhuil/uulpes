<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<form id="subform" action="${pageContext.request.contextPath}/scaletoollib/scalelook/importscalenorm.do"   method="post" enctype="multipart/form-data">  
	<table width="80%">
	  <tr>
	    <td></td>
	    <td>
	        <a id="downloadscalenormtemp" href="#" chref="${ctx}/assessmentcenter/scalemanager/${scaleid }/downloadscalenormtemp.do" onclick="downloadScaleNormTemp();">常模导入模板下载</a>&nbsp;&nbsp;&nbsp;&nbsp;
	         <c:if test="${oplevel==orglevel}">
	   	   	 <a id="importnormbtn" href=" javascript:void(0)" chref="${pageContext.request.contextPath}/scaletoollib/scalelook/${scaleid}/importscalenorm.do" >导入常模</a>&nbsp;&nbsp;&nbsp;&nbsp;
	   	   	 
	   	   	 <a id="createnormbtn" href=" javascript:void(0)" chref="${pageContext.request.contextPath}/scaletoollib/scalelook/${scaleid}/createscalenorm.do" >创建常模</a>
	   		</c:if>
	    </td>

	  </tr>
	</table>
   </form>
<iframe id="ifile" style="display:none"></iframe>
<script type="text/javascript">
$(function(){
	$("#uploadscale").click(function(evt){
		$("#dataloading").css("display","block");
		$("#subform").ajaxSubmit({
			target : "#tablelist",
			success : function (r) {
				debugger;
				$("#dataloading").css("display","none");
	            if ( r.status == 'error' ){
	               
	            } else if (r.status == 'success') {
	            	
	            }
			},
			error : function(XmlHttpRequest, textStatus, errorThrown){
				debugger;
				$("#dataloading").css("display","none");
				layer.open({content:'错误: ' + XmlHttpRequest.responseText});
				}
		});
	});
});

$("#importnormbtn").click(function(evt){
	debugger;
	var h = $(this).attr("chref");
	$('#editformdiv').empty();
	$('#editformdiv').load(h, function(response,status,xhr) {
		$("#editdialog").dialog("open");
	});
});
$("#createnormbtn").click(function(evt){
	debugger;
	var h = $(this).attr("chref");
	$('#editformdiv').empty();
	$('#editformdiv').load(h, function(response,status,xhr) {
		$("#editdialog").dialog("open");
	});
});
$("#downloadscalenormtemp").click(function() {
	var h = $(this).attr("chref");
	document.getElementById("ifile").src=h;
});

</script>