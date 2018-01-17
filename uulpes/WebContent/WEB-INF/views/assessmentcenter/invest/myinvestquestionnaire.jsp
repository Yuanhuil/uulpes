<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link href="${ctx}/css/investquestion.css" rel="stylesheet" type="text/css">
<style>
	.table2{
			border:1px solid #000;
			border-width:1px 0 0 1px;
			margin:2px 0 2px 0;
			border-collapse:collapse;
			font-size:14px;
		}
	.table2 td{
			border:1px solid #000;
			border-width:0 1px 1px 0;
			margin:2px 0 2px 0;
			height:20px;
		}
</style>	
<h2 style="text-align:center;" >问卷调查：${questionnaire.title}</h2>
	<table style="width:98%" class="table2">
		<tr>
			<td style="width:15%"><strong>问卷说明:</strong></td>
			<td>${questionnaire.descn}</td>
		</tr>	
		<!-- <tr>
			<td><strong>指导语：</strong></td>
			<td>${questionnaire.guidance}</td>
		</tr>-->
	</table>
	<br/>
	<div style="border-style:solid; border-width:1px; border-color:#000">
	<form id="subform" action="${ctx }/assessmentcenter/invest/investdone.do" method="post">
	<c:forEach var="queBlk" items="${questionnaire.questionBlocks }" varStatus="status">
	<div class="div_question" onclick="$('.div_question').css('background','white');this.style.background='rgb(237, 250, 254)'">
	  <div class="div_title_question_all">
	     <div class="div_title_question">
	     <div class="div_topic_question">${status.index+1}.</div>${queBlk.question.title}
	     <span style="color:red;">&nbsp;*</span>
	     </div>
	     <div style="clear:both;"></div>
	  </div>
	  <div class="div_table_radio_question">
	      <div class="div_table_clear_top"></div>
	          <ul class="ulradiocheck">
	          <c:forEach var="opt" items="${queBlk.question.options }" varStatus="status1">
		        <c:set var="key"> <c:out value="${status1.index+0}" /></c:set>     
	              <li style="width: 19%;"><input type="radio" name="r_${status.index}" id="q6_1" value="${status1.index }"><label for="q6_1">${optionLabelMap[key]} .${opt.title}</label></li>
	              </c:forEach>
	              <div style="clear:both;"></div>
	          </ul>
	          <ul class="ulradiocheck">
	              <div style="clear:both;"></div>
	          </ul><div style="clear:both;">
	          </div>
	          <div class="div_table_clear_bottom"></div>
	    </div>
	</div>
	</c:forEach>
	<div style="text-align:center;margin-bottom:10px;">
	<input id="investsubmit" class="button-big blue" type="submit" onclick value="提交"/>
	</div>
	<!-- <table style="width:98%" class="table2">
		<tr><td>
		  <c:forEach var="queBlk" items="${questionnaire.questionBlocks }" varStatus="status">
		    <h4>题${status.index+1}.&nbsp;&nbsp;${queBlk.question.title}</h4>
		      <c:forEach var="opt" items="${queBlk.question.options }" varStatus="status1">
		        <c:set var="key"> <c:out value="${status1.index+0}" /></c:set>     
		       ${optionLabelMap[key]} .${opt.title}<input class="answer_radio"  type="radio" value="${status1.index }"  name="r_${status.index}"/>&nbsp;&nbsp;&nbsp;
		      </c:forEach>
		  </c:forEach>
		  </td></tr>
		  <tr>
			<td style="text-align:center;"><input id="investsubmit" class="button-big blue" type="submit" onclick value="提交"/></td>
		</tr>
	</table>-->
	<input type="hidden" name="scaleid" value="${scaleid }">
	<input type="hidden" name="taskid" value="${taskid }">
	</form>
	</div>
	<script>
	$(function(){
	$("#subform").ajaxForm({
		target : "#content2"
	});
	});
	function changedivquestion(){
		$(".div_question").css("background","green");
	}
	</script>