<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset="utf-8">
    <style type="text/css">
	table{
		background-color:#52B1CD;
		font-size: 14px;
		line-height:22px;
	}
	table tr{
	    background-color:#fff;
	}
	.left_title_2 {
		color:#000;
		font-weight:bold;
		line-height:20px;
		text-align:right;
	}
	.STYLE3 {
		font-size: 24pt;
		font-weight: bold;
		line-height:50px;
	}
</style>
</head>
<body>
  <table width="719px" border="1" cellspacing="0" cellpadding="0" align="center" bordercolor="#52B1CD">
	<tr><td height="65" colspan="5" bgcolor="#f4f7f4" align="center" class="STYLE3" >个体评语</td></tr>    
	<tr><td height="25" align="right" width="25%" colspan="2"  class="left_title_2">姓名：</td>
		<td height="25" align="left" width="25%">${student.name}</td>
		<td height="25" align="right" width="25%" class="left_title_2">性别：</td>
		<td height="25" align="left" width="25%">${student.genderWord}</td>
	</tr>
	<tr>
		<td height="25" align="right" colspan="2" class="left_title_2">年级班级：</td>
		<td height="25" align="left" >${student.gradeName}-${student.classTitle}</td>
		<td height="25" align="right" class="left_title_2">评语生成日期：</td>
		<td height="25" align="left" >${now}</td>
	</tr>
	<tr height="125"><td colspan="5" valign="top"><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;“心理评估与心理档案管理系统”是由北京成均科技有限公司完成的专业心理检测系统，
	心理学研究由北京师范大学心理学院郑日昌教授主持，汇集了22名博士、13名硕士共35名心理学专家进行了为期15年的研究。
	该系统通过了中国教育部、中国心理卫生协会两大权威机构的成果鉴定，专家一致认为该系统是系统的、科学的、有效的心理检测工具。<br>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;下列评语是在个体完成的量表基础上撰写的。所完成的量表越多，评语越完整、越全面。<br></p></td></tr>
	
	<#if mentalDesc?length!=0>
	<tr height="155"><td width="10%" align="center"><strong>心<br>理<br>健<br>康<br>评<br>语<strong></td>
	<td valign="top" colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${mentalDesc}</td></tr></#if>
	<#if personalityDesc?length!=0>
	<tr height="225"><td width="10%" align="center"><strong>人<br>格<br>评<br>语<strong></td>
	<td valign="top" colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${personalityDesc}</td></tr></#if>
	<#if potentialDesc?length!=0><tr height="225"><td width="10%" align="center"><strong>智<br>能<br>评<br>语<strong></td>
	<td valign="top" colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${potentialDesc}</td></tr></#if>
	<tr><td width="10%" align="center"><strong>体<br>育<br>评<br>语<strong></td>
	<td valign="top" colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${medicalDesc}</td></tr>
	
	</table>


</html>