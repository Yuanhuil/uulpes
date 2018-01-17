<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="tab-container" class='tab-container'>

	<div id="tabs1-js">
		<div class="buttonContent">
			<ul>
				<li>
					<button type="button" class="button-mid blue" id="add">添加</button>
				</li>
			</ul>
		</div>
		<div id="consultTypeList" class="tableContent" style="height:auto;">
			<%@include file="consultTypelist.jsp"%>
		</div>
	</div>

</div>
<div id="consultTypeEdit"></div>
<script type="text/javascript">


	$("#add")
			.on(
					"click",
					function() {
						$("#dialog-form1").dialog("open");
						var fid = jQuery('#introduceId').attr("value");
						var h = '/pes/consultcenter/consultType/addOrUpdate.do?fid='
								+ fid;
						$('#consultTypeEdit').html();
						$('#consultTypeEdit').load(h, function() {
							$("#dialog-form1").dialog("open");
						});
					});
</script>
