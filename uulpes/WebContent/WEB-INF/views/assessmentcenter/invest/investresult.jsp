<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="MSHTML 11.00.9600.18125">
<link href="css/invest.css" rel="stylesheet" type="text/css">
<title>${questionnaire.title }问卷调查结果</title>
</head>
<BODY style='background: url("images/investbg.jpg") fixed no-repeat center 0px rgb(176, 224, 250);'>
<DIV id="container">
<DIV id="ctl02_header" style="width: 920px; -ms-overflow-x: hidden; background-repeat: repeat-x;"></DIV>
<div>
<DIV id="ctl02_wenjuan" style="padding-bottom: 15px; margin-left: 10px;">
	<STYLE>
	   body
	   {
	       color:#444;
	   }
     .go {
		width: 47px;
		height:60px;
		position: fixed;
		_position: absolute;
		_top: expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||200)-(parseInt(this.currentStyle.marginBottom,10)||0)));
		left: 50%;margin-left:460px;
		bottom: 45%;
		}
		.go a {
		background: url(/images/go.gif) no-repeat;
		display: block;
		text-indent: 999em;
		width:47px;
		border: 0;
		overflow: hidden;
		outline:0;
		float: left;
		}
		.go .top {
		background-position: 0 0px;
		height:46px;
		}
		.go .bottom {
		background-position: 0 -47px;
		height: 46px;
		}
		.go .top:hover{background-position:-48px -0px}
		.go .bottom:hover{background-position:-48px -47px}
   </STYLE>
<DIV  style="margin: 20px 0px; line-height: 28px; padding-left: 20px;">
  <DIV style="text-align: center; padding-top: 10px;"><B><SPAN style="font-size: 16px;">${questionnaire.title }问卷</SPAN></B></DIV></DIV>

<DIV  style="background: rgb(255, 255, 255); margin: 0px auto; padding: 0px 30px 20px; width: 850px;">

  <DIV id="divSumData">
    <DIV style="clear: both;"></DIV>

<UL id="ulList" style="list-style-type: none;">


<LI>
  <DIV style="background: rgb(204, 238, 255); padding: 8px; line-height: 24px;"><SPAN 
   style="color: black; font-weight: bold;"><!-- 第1页--></SPAN> 
                      
  <H3 style="margin: 0px; padding: 0px;"><SPAN  style="color: black; font-size: 14px;"><!-- 一：&nbsp;总体满意度--></SPAN></H3>               
        &nbsp;                    <SPAN style="color: rgb(0, 102, 255);"></SPAN></DIV>
  <DIV style="margin: 5px 0px;"></DIV></LI>


<c:forEach var="investInfo" items="${investInfoArray}">
<LI>
  <DIV  style="line-height: 24px;"><SPAN  style="color: rgb(61, 129, 238); font-weight: bold;">第${investInfo.qid+1 }题：</SPAN> 
                      
  <H3 style="margin: 0px; padding: 0px;"><SPAN   style="color: rgb(51, 51, 51); font-size: 14px;">${investInfo.qtitle}</SPAN></H3>    
                   &nbsp;<SPAN style="color: rgb(0, 102, 255);">[${investInfo.qtype }]</SPAN>                 
  </DIV>
  <DIV style="margin: 5px 0px;"></DIV>
  <DIV>
  <DIV style="margin-top: 5px;">
  <TABLE class="tableResult"   style="width: 820px; font-size: 12px; border-collapse: collapse; background-color: rgb(217, 229, 237);" 
  border="0" cellspacing="0" cellpadding="3" chartid="c10000">
    <THEAD>
    <TR align="center" class="text2"  style="font-weight: bold;">
      <TD  style="cursor: pointer;">选项</TD>
      <TD  align="center" 
        style="width: 50px; cursor: pointer;">小计</TD>
      <TD align="left" style="width: 360px;">比例</TD></TR></THEAD>
    <TBODY>
    <c:forEach var="opt" items="${investInfo.questionBlock.question.options }" varStatus="status">
    <c:set var="key"> <c:out value="${status.index+0}" /></c:set>  
    <TR style="background: white;">
      <TD val="1">${opt.title}</TD>
      <TD align="center">${investInfo.countMap[key] }</TD>
      <TD>
        <DIV class="bar">
        <DIV class="precent" style="width: ${investInfo.countpMap[key] }; display: block;"><IMG width="149" 
        height="13" alt="" src="images/vote_cl_v2.png"></DIV></DIV>
        <DIV style="margin-top: 3px; float: left;">${investInfo.countpMap[key]}</DIV>
        <DIV style="clear: both;"></DIV></TD></TR>
    </c:forEach>
       </TBODY>
    <TFOOT>
    <TR>
      <TD><B>本题有效填写人次</B></TD>
      <TD align="center"><B>${investInfo.sum}</B></TD>
      <TD></TD></TR></TFOOT></TABLE>
  <DIV style="padding-top: 5px;"></DIV></DIV>
</DIV>
<SPAN style="height: 30px;">
  <HR size="2" class="listhr" style="background: rgb(238, 238, 255); color: rgb(238, 238, 255);">
  </SPAN>
</LI>

</c:forEach>
</UL>
</DIV>
</DIV>
</DIV>
</DIV>

<DIV style="width: 920px; margin-top: 20px;">
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
  <TBODY>
  <TR>
    <TD height="1">
      <DIV style="background: rgb(187, 187, 187); margin: 0px auto; width: 910px; height: 1px; line-height: 1px; font-size: 0px;"></DIV></TD></TR>
  <TR>
    <TD height="10"></TD></TR>
  <TR>
    <TD align="center" valign="middle">
      <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
        <TBODY>
        <TR>
          <TD align="center" style="color: rgb(102, 102, 102); font-family: Tahoma, 宋体; font-size: 12px;">
            <DIV id="ctl02_divCopyRight" style="margin-top: 10px; -ms-overflow-x: hidden;"> 
                                                   Copyright 2006-2015 版权所有 </DIV></TD></TR>
        <TR id="ctl02_trPoweredBy">
          <TD align="center" style="color: rgb(102, 102, 102); font-family: Tahoma;">
            <DIV style="height: 10px;"></DIV>成均教育提供技术支持                                 
          </TD></TR>
        <TR>
          <TD></TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD height="20"></TD></TR></TBODY></TABLE></DIV>
</DIV>
</body>
</html>