<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<style>

#prefixquestion .ui-slider-handle { border-color: #729fcf; }
 #prefixquestion .ui-slider-range { background: #729fcf; height:2px;}
</style>
<script type="text/javascript">
$(function(){
    $("#formid").ajaxForm({
       type:"post",
		target : "#firstpage"
	});
    var minv = ${page.prefixMin};
    var maxv = ${page.prefixMax};
    
$("#prefixquestion").slider(
{
	 orientation: "horizontal",
     range: "min",
	value:minv,
	min: minv,
	max: maxv,
	slide: function(event, ui) {
		$( "#selectvalue").val(ui.value );
	 $("#a").val(ui.value);
	}
});

$( "#selectvalue" ).val(minv );
});
function  nextqestion(){
	$('#formid').submit();
}
</script>

<h2 class="scaletitle">${tester}正测试量表-${scale.title}</h2> <br>
<b><p style="text-align:center"><u>答题进度：<font color=#FF0000><b>${progress}</b> </font></u></p>
<p style="text-align:left;margin-top:20px;margin-bottom:10px;"><font face="楷体_GB2312" size="3"><strong>${subjecttitle}</strong></font></p>
<div style="width:90%; float:left; padding:20px;margin: 0 auto 30px auto;" >

  <div style="width:5%;float:left;display:inline;"><font face="楷体_GB2312" size="3"><strong>${currentQIdx}、</strong></font></div>
  <div style="width:95%;float:left;display:inline;"><font face="楷体_GB2312" size="3"><strong>${title }</strong></font></div>

<div id="quesioncontainer" class="answer" unselectable="on" onselectstart="return false;">
<p style="text-align:center;margin:50px auto 0 auto;"><font face="楷体_GB2312" size="3"><strong><label for="amount" >当前选中值：</label></strong></font>
<input type="text" id="selectvalue" style="width:30px;border:0; color:#f6931f; font-weight:bold;">
</p>
	<div>
		<table style="border-collapse:collapse;">
		<tr>
		<td ><font face="楷体_GB2312" size="3"><strong>${page.prefix }</strong></font></td>
		<td><div id="prefixquestion" style="margin:0 auto;width:450px;"></div></td>
		<td><font face="楷体_GB2312" size="3"><strong>${page.postfix}</strong></font></td>
		</tr>
		</table>
	 </div>
</div>
</div>
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
</form>
