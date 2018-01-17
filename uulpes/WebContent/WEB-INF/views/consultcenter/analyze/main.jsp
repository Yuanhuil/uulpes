<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="tab-container" class='tab-container'>

	<ul class='etabs'>
		<li class='tab'><a href="#tabs1-html">心理辅导</a>
		</li>
		<li class='tab'><a href="#tabs1-js">危机预警</a>
		</li>

	</ul>
	<div id="tabs1-html">
		<%@include file="query.jsp"%>
		<div id="list">
			<%@include file="list.jsp"%>
		</div>
	</div>
	<div id="tabs1-js">
		<%@include file="query1.jsp"%> 
		<div id="list1">
			<%@include file="list.jsp"%>
		</div>
	</div>
</div>



<div id="editDiv"></div>


<script type="text/javascript">
	$('#tab-container').easytabs();
</script>