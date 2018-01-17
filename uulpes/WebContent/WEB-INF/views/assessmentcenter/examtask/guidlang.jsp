<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<form id="formid" action="${baseaction}/nextpage.do" method="post">
	<div class="tipContent">
		<table cellspacing="1">
	      <tr>
	        <td width="203" rowspan="3"><img src="${ctx }/themes/theme1/images/tipPic.png" width="280" height="400"  alt=""/></td>
	        <td width="614" height="47" style="font-size:16px; font-weight:bold;">
	${scale.title}-指导语 </td>
	      </tr>
	      <tr>
	        <td height="180">${scale.guidance} </td>
	      </tr>
	      <tr>
	        <td height="153"><input class="button-big blue"  type="submit"  value="完全理解">  <input class="button-big gray" type="button" value="返回" onclick="goback('${testtype}','${threeangletest}')"></td>
	      </tr>
	    </table>
    </div>
</form>
<script type="text/javascript">
$(function(){
	var data = ${s};
	$("#formid").ajaxForm({
	        		data:{"s":data},
	        		type:"post",
					target : "#firstpage"
				});
	//$("#guidform").ajaxForm({
	//	target : "#quespage"
	//});};
});
function goback(testtype,threeangletest){
	if(testtype=='3'){
		var url;
		if(threeangletest=="true")
			url="${ctx }/assessmentcenter/threeangle/list.do";	
		else
			url="${ctx }/assessmentcenter/datamanager/dispatchToStuDataManagePage.do";
				
		$("#content2").load(url);
  	}
	if(testtype=='1'){
		var url="${ctx }/systeminfo/sys/test/main.do";
		$("#content2").load(url);
 	 }
}
</script>