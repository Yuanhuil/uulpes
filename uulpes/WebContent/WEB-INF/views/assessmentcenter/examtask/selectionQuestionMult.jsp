<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<script type="text/javascript">	
function chkmutlok(btname,chkname,recorderId) {
	if(1>chkedOptCount(chkname)){
		layer.open({content:'至少选择一项！'});
		return false;
	}
	disableall(btname);	
	var recorder= document.getElementById(recorderId);	
	$('#a').val(recorder.value);
	$('#formid').submit();
}
function nextqestion(){
	debugger;
	var lastquestion = '${page.lastquestion}';
   	if(lastquestion=="yes"){
   	 clearInterval(timeID);
   	 $('#timelimitpage').css("display","none");
   	}
	var v=document.getElementsByName('answer_check');
	var j=0;
	var idx="";
	for (var i=0;i<v.length;i++){
	 if(v.item(i).checked){
	   idx = idx+v.item(i).value+",";
	   j++;
	 }
	}
	if(j<1){
		layer.open({content:'至少选择一项！'});
		return false;
	}
	idx = idx.substring(0,idx.length-1);
	$('#a').val(idx);
	if (intDiff!=null && intDiff!="undefined"){
    	$('#countdown').val(intDiff);
    }
	$('#formid').submit();
}
</script>
<style>

#${buttonCtlId} {
	font-size: 18px; 
	border-bottom-width: 1px;
	border-top-style: solid;
	border-right-style: solid;
	border-bottom-style: solid;	
	border-left-style: solid;
	border-bottom-color: #6699CC;
	background-color:transparent;
	color: #06417B;
	height:35px;
}
</style>
<h2 class="scaletitle">${tester}正测试量表-${scale.title}</h2> <br>
<!-- <table  align="center" width="80%" style="border-collapse:collapse;"><tbody>
<tr><td>
<h2 id="hd" style="text-align:left">
 <font face="楷体_GB2312" color="#FF6600">${tester}正在测试的量表-《${scale.title}》</font></H2>
</td></tr></tbody></table>-->
<p><u><font color=#999999><b>答题进度：${progress}</b> </font></u></p>

<!-- <p style="text-align:left;margin-top:20px;"><font face="楷体_GB2312" size="3"><strong>${subjecttitle}</strong></font></p>

<p style="margin-top:20px;margin-bottom:20px;"><font face="楷体_GB2312" size="3"><strong>${title}</strong></font></p>
<span>${ selectionQuestion }</span>
<span>${ buttonCtl }</span>
	${recordCtl}  
	
-->
<p style="text-align:left;margin-top:20px;margin-bottom:10px;"><font face="楷体_GB2312" size="3"><strong>${subjecttitle}</strong></font></p>
 
 <div style="width:90%; float:left; padding:20px;" >

  <div style="width:5%;float:left;display:inline;"><font face="楷体_GB2312" size="3"><strong>${currentQIdx}、</strong></font></div>
  <c:if test="${not empty headtitle}">
     <div style="width:95%;float:left;display:inline;"><font face="楷体_GB2312" size="3"><strong>${headtitle }</strong></font></div><br>
     <div style="width:95%;float:left;display:inline;margin: 20px auto auto 33px;"><font face="楷体_GB2312" size="3"><strong>${title }</strong></font></div>
  </c:if> 
  <c:if test="${empty headtitle}">
     <div style="width:95%;float:left;display:inline;"><font face="楷体_GB2312" size="3"><strong>${title }</strong></font></div>
  </c:if>
<c:if test="${empty haspic}">
<div id="quesioncontainer" class="answer" unselectable="on" onselectstart="return false;">

  <ul><c:forEach var="option" items="${optionList}" varStatus="status" >
   <li class="optionitem"  >
      <input type="checkbox" name="answer_check" class="answer_radio" value="${status.index}" >
     <font face="楷体_GB2312" size="3"><strong>${option.title }</strong></font>
   </li></c:forEach>
  </ul>
</div>
</c:if>
<c:if test="${haspic =='1'}">
<div id="quesioncontainer" class="answer" unselectable="on" onselectstart="return false;">

  
  <table style="border-collapse:collapse;margin:30px;">
  <tr><c:forEach var="option" items="${optionList}" varStatus="status" >
  <td style="text-align:left;">

      <input type="checkbox" name="answer_check" class="answer_radio" value="${status.index}" style="margin: auto 15px auto;"> ${option.title }</label>
  </td></c:forEach>
  </tr>
  </table>

  
</div>
</c:if>
</div>
 
 
 
 
<!-- 
<div class="answer" unselectable="on" onselectstart="return false;">
  <ul><c:forEach var="option" items="${optionList}" varStatus="status">
   <li> <label>
      <input type="checkbox" name="answer_radio" class="answer_radio" value="${status.index}" >
     ${option.title }</label>
   </li></c:forEach>
  
  </ul>
</div>
-->



<div style="text-align:center;">
  <ul>
    <li>
	  <input class="answer_btn" type="button" value="下一题"  onclick="nextqestion()"/>	
    </li>
  </ul>
</div>
<form id="formid" action="${baseaction}/nextpage.do" method="post">
<input id="s" type="hidden" name="s" value='${s}'/>
<input id="q" type="hidden" name="q" value='${empty q ? 0 : q}'/>
<input id="a" type="hidden" name="a" />
<input id="st" type="hidden" name="st" value='${subjecttitle}' />
<input id="stt" type="hidden" name="stt" value='${subjecttitletype}' />
<input id="countdown" type="hidden" name="countdown" />
</form>
<script type="text/javascript">
$(function(){
	//var q = ${q};
	//var s = ${s};
	
	$("#formid").ajaxForm({
	        		type:"post",
					target : "#firstpage",
					error:function(data){
						debugger;
						layer.open({content:'错误: ' + data.responseText});
					},
				});
	//如果有限时，那么判断上次测试是否非断电中断
	var countdown = '${page.countdown}';
	if (countdown!='' && countdown!=null){
	   debugger;
		$('#timelimitpage').css("display","inline-block");
	    intDiff = parseInt(countdown);
	    timer(intDiff);
	}
	//$("#guidform").ajaxForm({
	//	target : "#quespage"
	//});};
});

</script>