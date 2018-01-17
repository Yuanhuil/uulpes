<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="tab-container" class='tab-container'>
	<form id="statform" method="post" action="../../scaletoollib/statistics/${statType}/nextStep.do">
		<ul class='etabs'>
			<li class='tab'><a id="stutab" statObj="1" data-target="#tabs_studentSchoolStat">学生统计</a></li>
			<li class='tab'><a id="teatab" statObj="2" data-target="#tabs_studentSchoolStat">教师统计</a></li>
		</ul>
	</form>
		<div class='panel-container'>
			<div id="tabs_studentSchoolStat">
			</div>
		</div>
	</div>
	<input type="hidden" id="step" value="${step}">
<script type="text/javascript">
$(function() {  
	debugger;
	//$('#tab-container').easytabs({cache:false});
	
	var step = $("#step").val();
	
	$(".tab a").on("click", function(e){
		var statobj;
		if(e.target.id === "stutab")
			statobj = $("#stutab").attr("statObj");
		else
			statobj = $("#teatab").attr("statObj");
		
		$("#statform").ajaxSubmit({
			target : "#tabs_studentSchoolStat",
	        data: { "step": step,"statObj":statobj},//参数列表
	        success: function(result){
	           //请求正确之后的操作
	        },
	        error: function(result){
	           //请求失败之后的操作
	        	layer.open({content:'错误: ' + jqXHR.responseText});
	        }
	    });
		$(".tab").removeClass("active");
		$(".tab a").removeClass("active");
		$(".tab").eq(statobj-1).addClass("active");
		$(".tab a[statobj="+statobj+"]").addClass("active");
	});
	
	var statobj = $(".tab a .active").attr("statobj");
	if(statobj)
		$(".tab a").eq(statobj-1).click();
	else
		$(".tab a").eq(0).click();
	$("#tabs_studentSchoolStat").addClass("active");
});
</script>