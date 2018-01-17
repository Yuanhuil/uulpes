<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<script type="text/javascript">	
function answer() {
		//window.location.replace("${pageContext.request.contextPath}/scaletoollib/nextpage.do?s=${s}");
		//return true;
	$.ajax({
		url:"${baseaction}/nextpage.do?s=${s}",
		dataType:"json",
		type:"POST",
		success:function(data){
			console.log(data);
			 var h = ${template};
			   $("#quespage").html();
			   $("#quespage").load(h);
		}
		
	});
	}
</script>
<form id="formid" action="${baseaction}/nextpage.do" method="post">
<table align="center" class="tb1">
  <tbody>
    <tr>
      <td width="721" height="45"><div align="center" class="scaletitle">量表测试</div></td>
    </tr>
    <tr>
      <td>
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" bordercolorlight="#cccccc" class="tb2">
        <tbody>
          <tr>
            <td width="20%" height="25" align="left" bgcolor="#CEF5FF">量表名称：</td>
            <td width="79%" height="25" align="left" bgcolor="#FFFFFF" class="STYLE1">${scale.title}</td>
          </tr>
          <tr>
            <td width="20%"  height="23" align="left" bgcolor="#FFFFFF">量表类型：</td>
            <td width="79%"  height="23" align="left" bgcolor="#FFFFFF">${scale.scaleType}</td>
          </tr>
          <tr>
            <td width="20%" height="25" align="left" bgcolor="#CEF5FF">量表测试人：</td>
            <td width="79%" height="25" align="left" bgcolor="#FFFFFF">${tester}</td>
 
          </tr>
          <tr>
            <td height="25" align="left" valign="top" bgcolor="#FFFFFF">量表介绍：</td>
            <td width="79%"  height="25" align="left" bgcolor="#FFFFFF">${scale.descn}</td>
          </tr>
          <tr>
            <td height="30" colspan="2" bgcolor="#CEF5FF"><div align="center">
              <input class="button-big blue" name="button" type="submit"   value="开始测验" />
              <input class="button-big gray"  type='button' onclick="goback('${testtype}','${threeangletest}');" value='返回'>
            </div></td>
          </tr>
        </tbody>
      </table>
      </td>
    </tr>
  </tbody>
</table>
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
	debugger;
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