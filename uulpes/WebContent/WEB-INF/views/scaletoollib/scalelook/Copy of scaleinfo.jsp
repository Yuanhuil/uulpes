<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>
.pop-up-mainTitle{
	margin: 10px 0;
}
.pop-up-normalTitle{
    margin: 8px 0;
}
.pop-up-normalText{
	margin: 0 0 8px 0;
	line-height: 24px;
}
</style>
<div class="pop_up_mainTitle"><h2>${scale.title}</h2></div>
<div>
	<div class="pop-up-mainTitle">
		<span>题目数量：${scale.questionNum }</span>
		<span style="margin-left:20px;">适用人群：${scale.applicablePerson }</span>
	</div>
	<div class="pop_up_normalTitle">
		<span>量表类型：${scale.scaleType }</span>
		<span style="margin-left:20px;">量表来源：${scale.source }</span>
	</div>
	<div >
		<div class="pop_up_normalTitle">量表简介：</div>
		<div class="pop_up_normalText">${scale.descn }</div>
	</div>
</div>