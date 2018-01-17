<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="tab-container" class='tab-container'>

	<ul class='etabs'>
		<li class='tab'><a href="#tabs1-html">预约记录</a>
		</li>
		<li class='tab'><a id ="showView"  href="#tabs1-js">预约列表</a>
		</li>

	</ul>
	<div id="tabs1-html">
		<div id="list">
			<%@include file="list.jsp"%>
		</div>
	</div>
	<div id="tabs1-js">
		<%@include file="query1.jsp"%>
		<div id="list1">
			
		</div>
	</div>

</div>



<div id="editDiv"></div>


<script type="text/javascript">
	$('#tab-container').easytabs();

	
	

	$("#edit").on("click", function() {
	    var fid = jQuery('#introduceId').attr("value");
		divLoadUrl("view","/pes/consultcenter/introduce/addOrUpdate.do?id="+fid);
	});

	
	$("#add").on("click", function() {
		$("#dialog-form1").dialog("open");
		var fid=jQuery('#introduceId').attr("value");
		var h = '/pes/consultcenter/consultType/addOrUpdate.do?fid='+fid;
		$('#consultTypeEdit').html();
		$('#consultTypeEdit').load(h, function() {
			$("#dialog-form1").dialog("open");
		});
	});
</script>