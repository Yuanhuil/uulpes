<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link href="${ctx}/css/investquestion.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	var themeUrl = "${ctx}/themes/theme1/";
</script>
<c:set var="baseaction" value="${ctx}/assessmentcenter/invest"/>

<!--<form:form id="investform" action="${baseaction}/save.do" method="post"  modelAttribute="invest">-->
	<div class="filterContent">
		<ul>
			<li>
				<label class="name03">问卷名称</label> 
				<input id="name" placeholder="调查问卷名称" class="input_160" />
			</li>
			<li>
				<label class="name03">题目类型</label> 
				<select id="questionType" class="select_180">
					<option value="single">单选题</option>
					<option value="multi">多选题</option>
				</select>
			</li>
		</ul>
	</div>
	<div class="filterContent" id="invest_editor" style="height:inherit"></div>
	<div class="buttonContent" id="invest_query">
		<ul>
			<li>
				<input class="button-small blue" type="button" value="增加" id="invest_add">
				<input class="button-small blue" type="button" value="删除" id="invest_del">
				<input class="button-small blue" type="button" value="修改" id="invest_upd">
				<input class="button-small blue" type="button" value="保存" id="invest_upd_sav" style="display:none">
				<input class="button-small blue" type="button" value="保存问卷" id="invest_savUpdate">
			</li>
			<li>
				<input class="button-small blue" type="button" value="全选" id="invest_selall">
				<input class="button-small blue" type="button" value="反选" id="invest_unsel">
			</li>
		</ul>
	</div>

	<div class="tableContent" style="text-align:inherit;font-size:14px;line-height:30px" id="invest_questions"></div>
	
<!--</form:form>-->
	
<script type="text/javascript">
$(function(){
	var editor = new $.questionEditor("invest_editor");
	var sobj = JSON.parse('${invest}');
	$("#name").val(sobj.title);
	var questions = sobj.questions;
	editor.questionnire.init(questions);
	$("#invest_savUpdate")[0].setAttribute("chref", "${ctx}/assessmentcenter/invest/" + sobj.id + "/save.do");
});
</script>
