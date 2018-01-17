<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="error" style="text-align: center">
	该时段时间不足一小时无法预约，请选择其他时段。
</div>
<script type="text/javascript">
	$("#dialog-form1")
			.dialog(
					{
						appendTo : "#editDiv",
						autoOpen : false,
						modal : true,
						height : 300,
						width : 400,
						buttons : {
							
							"取消" : function() {
								$("#dialog-form1").dialog("close");
							}
						},
					});

	

	
</script>