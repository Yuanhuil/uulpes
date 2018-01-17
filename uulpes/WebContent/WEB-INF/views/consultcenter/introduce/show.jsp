<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="tab-container" class='tab-container'>
	<input type="text" hidden value="${introducePage.introduce.id}"
		id="introduceId">
	<ul class='etabs'>
		<li class='tab'><a href="#tabs1-html">中心简介</a></li>
		<li class='tab'><a href="#tabs1-js">咨询类型</a></li>

	</ul>
	<div id="tabs1-html">
		<div id="view">
			<%@include file="view.jsp"%>
		</div>
	</div>
	<div id="tabs1-js">
		<div class="buttonContent ">
			
					<shiro:hasPermission name="consultcenter:centersetting:create">
					    <button type="button" class="button-mid blue" id="add">添加</button>
					</shiro:hasPermission>
				
		</div>
		<div id="consultTypeList" class="tableContent" style="height:auto;">
			<%@include file="consultTypelist.jsp"%>
		</div>
	</div>

</div>
<div id="consultTypeEdit"></div>
<script type="text/javascript">
	$('#tab-container').easytabs();

	$("#edit").on(
			"click",
			function() {
				var fid = jQuery('#introduceId').attr("value");
				divLoadUrl("view",
						"/pes/consultcenter/introduce/addOrUpdate.do?id="
								+ fid);
			});

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
