<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<script type="text/javascript">	
function selectOne(val) {
	$('#a').val(val);
	$('#formid').submit();
}
</script>
<style>
BODY {
	BACKGROUND: #fff; TEXT-ALIGN: center
}
.header {
	CLEAR: both; MARGIN: 0px auto; TEXT-ALIGN: left
}
#hd {
	BORDER-TOP: #4597bf 1px solid; MARGIN-TOP: 5px; BACKGROUND: #eaf3fc; BORDER-BOTTOM: #4597bf 1px solid
}
#bgc {
	BACKGROUND: #eaf3fc; LINE-HEIGHT: 20px; HEIGHT: 20px
}
INPUT {
	FONT-SIZE: 18px;
	MARGIN-BOTTOM: 15px;
	WIDTH: ${width};
	COLOR: #0099cc;
	HEIGHT: 35px;
	BACKGROUND-COLOR: #eaf3fc;
	TEXT-ALIGN: ${align};
	text-indent: 15px;
	border-top-width: thin;
	border-right-width: thin;
	border-bottom-width: thin;
	border-left-width: thin;
	border-top-style: dotted;
	border-right-style: dotted;
	border-bottom-style: solid;
	border-left-style: dotted;
	border-top-color: #0066cc;
	border-right-color: #0066cc;
	border-bottom-color: #3399CC;
	border-left-color: #0099cc;
}
.infos-wrap .test_contents .descs {
    font-size: 14px;
}
.infos-wrap .fb {
    font-weight: 700;
}


.sels_list {
    padding: 15px 0 20px;
    width: 640px;
}
 .items {
    padding: 5px 0;
    display: block;
    width: 640px;
    cursor: pointer;
}
.hover{
  color:#369;
  background:#EEE;
  border-radius:6px;
}
.i_mid {
    position: relative;
    padding: 3px 9px 2px 44px;
}
.sels_list .items .i_top .infos-wrap .test_contents .sels_list .items .i_bot {
    overflow: hidden;
    width: 640px;
    height: 5px;
}
.sels {
    position: absolute;
    top: 4px;
    left: 15px;
}
</style>
<h2 style="margin-top:10px;">${tester}正测试的量表-${scale.title}</h2> <br>

<!-- <table  align="center" width="80%" style="border-collapse:collapse;"><tbody>
<tr><td>
<h2 id="hd" style="text-align:left">
 <font face="楷体_GB2312" color="#FF6600">${tester}正在测试的量表-《${scale.title}》</font></H2>
</td></tr></tbody></table>-->
</b><p><u><font color=#999999><b>答题进度：${progress}</b> </font></u></p>

<p style="text-align:left;margin-top:20px;"><font face="楷体_GB2312" size="3"><strong>${subjecttitle}</strong></font></p>

<p style="margin-top:20px;margin-bottom:20px;"><font face="楷体_GB2312" size="3"><strong>${title}</strong></font></p>
<span>${ selectionQuestion }</span>
<form id="formid" action="${baseaction}/nextpage.do" method="post">
<input id="s" type="hidden" name="s" value='${s}'/>
<input id="q" type="hidden" name="q" value='${empty q ? 0 : q}'/>
<input id="a" type="hidden" name="a" />
<input id="st" type="hidden" name="st" value='${subjecttitle}' />
</form>
<script type="text/javascript">
$(function(){
	//var q = ${q};
	//var s = ${s};
	
	$("#formid").ajaxForm({
	        		type:"post",
					target : "#firstpage"
				});
	//$("#guidform").ajaxForm({
	//	target : "#quespage"
	//});};
});

</script>