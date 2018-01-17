<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@include file="query.jsp"%>
<div id="list">
	<%@include file="list.jsp"%>
</div>

<div id="editDiv" style="display:none">
	<input type="hidden" id="velueSelect">
	<div id="u1" class="text">
		<h1>
			<span>生成档案设置</span>
		</h1>
	</div>
	<table>
		<tr>
			<td width="20%">个人基本信息</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">姓名</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">姓名拼音</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">角色</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">性别</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">工号</td>
		</tr>
		<tr>
			<td width="20%"></td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">学校编号</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">学校名称</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">入职时间</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">配偶姓名</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">年龄</td>
		</tr>
		<tr>
			<td width="20%"></td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">出生地</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">电话</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">地址</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">邮编</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">身份证号</td>
		</tr>
		<tr>
			<td width="20%"></td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">血型</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">民族</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">宗教信仰</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">籍贯</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">户口所在地</td>
		</tr>
		<tr>
			<td width="20%"></td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">健康状况</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">政治面貌</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">户口性质</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">流动人口状况</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">特长</td>
		</tr>
		<tr>
			<td width="20%"></td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">邮箱</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">主页</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">照片</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">体重</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">身高</td>
		</tr>
		<tr>
			<td width="20%">心理测评信息</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">量表名称</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">测评时间</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">测评得分</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">结果图表</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">结果解释</td>
		</tr>
		<tr>
			<td width="20%"></td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">指导建议</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">测评次序</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked=""></td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked=""></td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked=""></td>
		</tr>
		<tr>
			<td width="20%">心理辅导信息</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">辅导时间</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">咨询类型</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">辅导老师</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked=""></td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked=""></td>
		</tr>
		<tr>
			<td width="20%">危机干预信息</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">辅导时间</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">咨询类型</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">辅导老师</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">预警级别</td>
			<td width="16%"><input id="u171_input" type="checkbox"
				value="checkbox" checked="">预警状态</td>
		</tr>
		
		
	</table>
</div>
