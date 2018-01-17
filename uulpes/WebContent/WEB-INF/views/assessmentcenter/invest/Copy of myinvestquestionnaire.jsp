<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
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
		<tr>
			<td><strong>指导语：</strong></td>
			<td>${questionnaire.guidance}</td>
		</tr>
	</table>
	<br/>
	<form id="subform" action="${ctx }/assessmentcenter/invest/investdone.do" method="post">
	<table style="width:98%" class="table2">
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
	</table>
	<input type="hidden" name="scaleid" value="${scaleid }">
	<input type="hidden" name="taskid" value="${taskid }">
	</form>
	<script>
	$(function(){
	$("#subform").ajaxForm({
		target : "#content2"
	});
	});

	</script>