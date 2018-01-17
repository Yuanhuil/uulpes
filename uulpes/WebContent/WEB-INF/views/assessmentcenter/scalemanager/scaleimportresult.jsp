<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tableContent" style="font-family:微软雅黑;">
<div style="text-align:center;"><h1 style="font-size:16px; font-weight:bold;">${scale.title }</h1></div>
<div style="text-align:left;"><h2 style="font-size:14px; font-weight:bold;">指导语：</h2></div>
<p style="text-align:left;margin-left:30px;">${scale.guidance }</p>
<div style="text-align:left;"><h2 style="font-size:14px; font-weight:bold;">量表描述：</h2></div>
<p style="text-align:left;margin-left:30px;">${scale.descn }</p>
<div style="text-align:left;"><h2 style="font-size:14px; font-weight:bold;">维度数量:${fn:length(scale.dimensionMap)}</h2></div>
<div style="text-align:left;"><h2 style="font-size:14px; font-weight:bold;">维度：${toDimHTML}</h2></div>
<div style="text-align:left;"><h2 style="font-size:14px; font-weight:bold;">题目数量：${scale.questionNum }</h2></div>
<div style="text-align:left;"><h2 style="font-size:14px; font-weight:bold;">题目列表</h2></div>
<div style="text-align:left;margin-left:80px;">${toQuestionHTML}</div>
</div>