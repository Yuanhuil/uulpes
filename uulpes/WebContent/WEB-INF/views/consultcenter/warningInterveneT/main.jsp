<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<%@include file="query.jsp"%>
<div id="list1">
	<%@include file="list.jsp"%>
</div>



<script type="text/javascript">

	$("#add1").on(
					"click",
					function() {
						$("#dialog-form1").dialog("open");
						var h = '/pes/consultcenter/warningInterveneT/addOrUpdate.do?intervene_show=none';
						$('#editDiv').html();
						$('#editDiv').load(h, function() {
							$("#dialog-form1").dialog("open");
						});
					});
</script>