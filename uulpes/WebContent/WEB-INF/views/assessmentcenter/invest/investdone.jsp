<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/invest"/>
<style>
.divCom{
padding:10px 0;  text-align: left; line-height: 18px; color: #333333;  
}
.comletepage_result{
background:url(${ctx }/themes/theme1/images/completepage_result.gif) no-repeat;
width:181px;
height:34px;
float:right;
padding-right:40px;
text-align:center;
}
</style>
<!-- <form id="investdoneForm" action="${baseaction }/${taskid }/investresult.do">-->
<div class="survey" id="divTab" style="width: 680px; padding-top: 30px; padding-right: 0px; padding-bottom: 30px; padding-left: 0px; margin-top: 0px; margin-right: auto; margin-bottom: 10px; margin-left: auto;">
	<div style="width: 94%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px; margin-top: 0px; margin-right: auto; margin-bottom: 10px; margin-left: auto;">
	  <div class="divCom" >
	      <div  style="font-size: 14px;">您的答卷已经提交，感谢您的参与！</div>
	  </div>
	  <div class="comletepage_result" ><!-- <a href="javascript:void(0)" onclick="gotoInvestResult()">-->
	  <a href="${baseaction }/${taskid }/${scaleid }/investresult.do" target="_blank">
	  <img src="${ctx }/themes/theme1/images/investdone.gif" width="71" height="20"></a>
	  </div>
	</div>
</div>
<!-- </form>-->
<script type="text/javascript">
function gotoInvestResult(){
	
	$("#investdoneForm").ajaxSubmit({
		target : "#content2"
	});
}
</script>