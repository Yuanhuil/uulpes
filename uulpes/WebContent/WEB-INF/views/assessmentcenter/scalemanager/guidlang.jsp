<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>指导语</title>
</head>
<script type="text/javascript">	
	function submit() {
		window.location.replace("${pageContext.request.contextPath}/scaletoollib/nextpage.do?s=${s}");
		return true;
	}	
</script>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<LINK  href="styles/zhidao.css" type=text/css rel=stylesheet>
<BODY>
<TABLE height=2 cellSpacing=0 cellPadding=0 width=700 align=center border=0>
  <TBODY>
  <TR>
    <TD bgColor=#999999></TD></TR></TBODY></TABLE>
<TABLE height=416 cellSpacing=0 cellPadding=0 width=700 align=center bgColor=#ffffff border=0>
  <TBODY>
  <TR>
    <TD vAlign=top align=middle width=146 bgColor=#f5f5ed height=416>
      <TABLE cellSpacing=0 cellPadding=0 width=125 align=right border=0>
        <TBODY>
        <TR>
          <TD colSpan=3 height=40></TD></TR>
        <TR>
          <TD colSpan=3 height=10><IMG height=125 
            src="images/zhidao1_18.gif" width=125></TD></TR></TBODY>
       </TABLE>
    </TD>
    <TD vAlign=top align=middle width=554 bgColor=#f5f5ed>
      <TABLE height=200 cellSpacing=0 cellPadding=0 width=504 align=center border=0>
        <TBODY>
        <TR>
          <TD align=left width="50%" background=images/zhidao1_5.gif 
          height=51><IMG height=51 src="images/zhidao1_2.gif" width=74></TD>
          <TD align=right width="50%" background=images/zhidao1_5.gif><IMG 
            height=51 src="images/zhidao1_7.gif" width=139></TD></TR>
        <TR>
          <TD background=images/zhidao1_12.gif colSpan=2>
            <TABLE height=324 cellSpacing=0 cellPadding=0 width="95%" 
            align=center border=0>
              <TBODY>
              <TR>
                <TD vAlign=top align=left>
                <SPAN class=zhidao1>${scale.title}-指导语</SPAN>
                <br>
                <hr>
                <SPAN class=zdnr1> ${scale.guidance}</SPAN>
                <br>
                <hr>
                </TD>
              </TR>
              <TR>
                <TD vAlign=top align=right>
                  <P> 
 <input type='button' onclick='submit(); return false' value='我完全理解'>
 <input type='button' onclick="javascript:{window.location.replace('scaleAttachCtl.app?rl=1');}" value='返回'>
        		<TR>
          <TD background=images/zhidao1_12.gif colSpan=2 height=10></TD></TR>
        <TR>
          </TR></TBODY></TABLE></TD></TR></TBODY></TABLE>
<body>
</html>