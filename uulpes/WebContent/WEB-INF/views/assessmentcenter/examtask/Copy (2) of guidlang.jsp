<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<form id="formid" action="${baseaction}/nextpage.do" method="post">
<TABLE height=2 cellSpacing=0 cellPadding=0 width=700 align=center border=0>
  <TBODY>
  <TR>
    <TD bgColor=#999999></TD></TR></TBODY></TABLE>
<TABLE height=416 cellSpacing=0 cellPadding=0 width=700 align=center bgColor=#ffffff border=0>
  <TBODY>
  <TR>
  <TD vAlign="top"  bgColor=#f5f5ed style="alight:middle;width:146px;height:416px;">
      <TABLE cellSpacing=0 cellPadding=0 width=125 align=right border=0>
        <TBODY>
        <TR>
          <TD colSpan=3 height=40></TD></TR>
        <TR>
          <TD colSpan=3 height=10><IMG height=125 
            src="${ctx }/themes/theme1/images/zhidao1_18.gif" width=125></TD></TR></TBODY>
       </TABLE>
    </TD>
    <TD vAlign=top align=middle width=554 bgColor=#f5f5ed>
      <TABLE height=200 cellSpacing=0 cellPadding=0 width=504 align=center border=0>
        <TBODY>
        <TR>
          <TD align=left width="50%" background="${ctx }/themes/theme1/images/zhidao1_5.gif" 
          height=51><IMG height=51 src="${ctx }/themes/theme1/images/zhidao1_2.gif" width=74></TD>
          <TD align=right width="50%" background="${ctx }/themes/theme1/images/zhidao1_5.gif"><IMG 
            height=51 src="${ctx }/themes/theme1/images/zhidao1_7.gif" width=139></TD></TR>
        <TR>
          <TD background="${ctx }/themes/theme1/images/zhidao1_12.gif" colSpan=2>
            <TABLE height=324 cellSpacing=0 cellPadding=0 width="95%" 
            align=center border=0>
              <TBODY>
              <TR>
                <TD vAlign=top align=left>
                <SPAN class="scaletitle">${scale.title}-指导语</SPAN>
                <br>
                <hr>
                <SPAN class=zdnr1> ${scale.guidance}</SPAN>
                <br>
                <hr>
                </TD>
              </TR>
              <TR>
                <TD vAlign=top style="text-align:center;">
                  <P> 
               <input class="answer_btn" name="button" type="submit"   value="我完全理解" />
 <input  class="answer_btn" type='button' onclick="javascript:{window.location.replace('scaleAttachCtl.app?rl=1');}" value='返回'>
        		<TR>
          <TD background="${ctx }/themes/theme1/images/zhidao1_12.gif" colSpan="2" style="height:10px"></TD></TR>
        <TR>
          </TR></TBODY></TABLE></TD></TR></TBODY></TABLE>
</form>
<script type="text/javascript">
$(function(){
	var data = ${s};
	$("#formid").ajaxForm({
	        		data:{"s":data},
	        		type:"post",
					target : "#firstpage"
				});
	//$("#guidform").ajaxForm({
	//	target : "#quespage"
	//});};
});
</script>