<script type="text/javascript">	
    var ACTION = '${pageContext.request.contextPath}/scaletoollib/nextPage.do';
	function selectOne(val) {
		var url = "";
		layer.open({content:"start anwser"});
		url = ACTION + '?' + getPartQry();
		url = url + '&' + 'a='+val;
		layer.open({content:url});
		window.location.replace(url);
		return true;
	}
	function getPartQry() {
		var qry = 'q='+${q}+'&'+'s='+${s};
		return qry;
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

<form id="quesform"  method="post">
	
	<table  align="center" width="80%"><tbody>
	<tr><td>
	<h2 id="hd" style="text-align:left">
	<font face="隶书" color="#FF6600">赵梓晨正在测试的量表-《${scale.title}》</font></H2>
	</td></tr></tbody></table>
	</b><p><u><font color=#999999><b>答题进度：${progress}</b> </font></u></p>
	<p><font face="楷体_GB2312" size="5"><strong>${title}</strong></font></p>
	<span>${ selectionQuestion }</span>
</form>