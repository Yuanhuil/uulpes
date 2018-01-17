<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<%@include file="query2.jsp"%>
<div id="list1">
	<%@include file="list2.jsp"%>
</div>
<script type="text/javascript">

	$("#add1").on("click", function() {
		$("#dialog-form1").dialog("open");
		var h = '/pes/consultcenter/warningInterveneT/addOrUpdate.do?status=4&oldstatus=4';
		$('#editDiv').html();
		$('#editDiv').load(h, function() {
			$("#dialog-form1").dialog("open");
		});
	});
	
</script>