<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<script type="text/javascript">	
function selectAnswer(val) {
	debugger;
    //disableanswer("answeritem");
    //$("#quesioncontainer").css("display","none");
   	var lastquestion = '${page.lastquestion}';
   	if(lastquestion=="yes"){
   	 clearInterval(timeID);
   	 $('#timelimitpage').css("display","none");
   	}
    $(".optionitem").attr("onclick","");  
    if (intDiff!=null && intDiff!="undefined"){
    	$('#countdown').val(intDiff);
    }
	$('#a').val(val);
	$('#formid').submit();
}
function selectAnswer1(val) {
	debugger;
	var lastquestion = '${page.lastquestion}';
   	if(lastquestion=="yes"){
   	 clearInterval(timeID);
   	 $('#timelimitpage').css("display","none");
   	}
    //disableanswer("answeritem");
    //$("#quesioncontainer").css("display","none");
    $(".answer_radio").attr("onclick",""); 
    if (intDiff!=null && intDiff!="undefined"){
    	$('#countdown').val(intDiff);
    }
	$('#a').val(val);
	$('#formid').submit();
}
function disableanswer(name) {
	var tags = document.getElementsByName(name);
	for (var i = 0; i < tags.length; i = i + 1) {
		tags[i].disabled = true;
	}
}
//超时，直接提交
function timeout(){
	$(".answer_radio").attr("onclick","");  
	$('#timeout').val("yes");
	$('#formid').submit();
}
</script>

<h2 class="scaletitle">${tester}正测试量表-${scale.title}</h2> <br>

<!-- <table  align="center" width="80%" style="border-collapse:collapse;"><tbody>
<tr><td>
<h2 id="hd" style="text-align:left">
 <font face="楷体_GB2312" color="#FF6600">${tester}正在测试的量表-《${scale.title}》</font></H2>
</td></tr></tbody></table>-->
<p style="text-align:center"><u>答题进度：<font color=#FF0000><b>${progress}</b> </font></u></p>
<p style="text-align:left;margin-top:20px;margin-bottom:10px;"><font face="楷体_GB2312" size="3"><strong>${subjecttitle}</strong></font></p>
<!-- <div class="question" >${currentQIdx}、${title }</div>-->

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
   <li class="optionitem" onclick="selectAnswer(${status.index})" >
      <input type="radio" name="RadioGroup1" class="answer_radio" value="radio" id="RadioGroup1_0" >
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
      <input type="radio" name="RadioGroup1" class="answer_radio" value="radio" id="RadioGroup1_0" onclick="selectAnswer1(${status.index})" style="margin: auto 15px auto;" >${option.title }
  </td></c:forEach>
  </tr>
  </table>

  
</div>
</c:if>
</div>

<form id="formid" action="${baseaction}/nextpage.do" method="post">
<input id="s" type="hidden" name="s" value='${s}'/>
<input id="q" type="hidden" name="q" value='${empty q ? 0 : q}'/>
<input id="a" type="hidden" name="a" />
<input id="st" type="hidden" name="st" value='${subjecttitle}' />
<input id="stt" type="hidden" name="stt" value='${subjecttitletype}' />
<input id="timeout" type="hidden" name="timeout" />
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
	debugger;
	//如果有限时，那么判断上次测试是否非断电中断
	var countdown = '${page.countdown}';
	if (countdown!='' && countdown!=null){
	   debugger;
		$('#timelimitpage').css("display","inline-block");
	    intDiff = parseInt(countdown);
	    timer(intDiff);
	}

});

$(".items").click(function() {
	debugger;
	$(".answer").attr("disabled",true);
	var val = $(this).attr("href");
	$('#a').val(val);
	$('#formid').submit();
});

</script>