<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<script type="text/javascript">	
function selectOne(val) {
	$('#j').val(val);
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
</style>
<h2>${tester}正测试的量表-${scale.title}</h2> <br>
<form id="formid" action="${baseaction}/nextpage.do" method="post">
</b><p><u><font color=#999999><b>答题进度：${progress}</b> </font></u></p>

<p style="text-align:left;margin-top:20px;"><font face="楷体_GB2312" size="3"><strong>${subjecttitle}</strong></font></p>

 <div style="width:95%;float:left;display:inline;margin: 20px auto auto 33px;"><font face="楷体_GB2312" size="3"><strong>${title}</strong></font></div>
<div style="margin:100px auto;">${ selectionQuestion }</div>
<input id="s" type="hidden" name="s" value='${s}'/>
<input id="q" type="hidden" name="q" value='${empty q ? 0 : q}'/>
<input id="j" type="hidden" name="j" />
<input id="st" type="hidden" name="st" value='${subjecttitle}' />
<input id="stt" type="hidden" name="stt" value='${subjecttitletype}' />
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