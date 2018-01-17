<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="tab-container" class='tab-container'>

	<ul class='etabs'>
		<li class='tab'><a id="sjx" href="#tabs1-html">收件箱</a></li>
		<li class='tab'><a id="yfs" href="#tabs1-js">已发送</a></li>
		<li class='tab'><a id="cgx" href="#tabs2-js">草稿箱</a></li>
	</ul>
	<div id="tabs1-html"></div>
	<div id="tabs1-js"></div>
	<div id="tabs2-js"></div>

</div>

<%@include file="query.jsp"%>
<div id="list">
	<%@include file="list.jsp"%>
</div>

<div id="editDiv"></div>
<script type="text/javascript">
	$('#tab-container').easytabs();
</script>

