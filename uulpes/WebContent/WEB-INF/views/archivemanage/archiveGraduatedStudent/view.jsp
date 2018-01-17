<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@include file="/WEB-INF/views/common/import-css.jspf"%>
<%@include file="/WEB-INF/views/common/import-js.jspf"%>
<head>


<link href="/pes/css/report.css" rel="stylesheet" type="text/css">
<style type="text/css">



</style>
</head>
<body>


	<div id="tab-container" class='tab-container'>

		<ul class='etabs' style="text-align: center;">
			<li class='tab'><a href="#tabs1-html">个人基本信息</a></li>
			<c:if test="${cpDisplay=='on'}">
			<li class='tab'><a id="testInfo" href="#tabs2-html">心理测评信息</a></li>
			</c:if>
			<c:if test="${fdDisplay=='on'}">
			<li class='tab'><a id="recordInfo" href="#tabs3-html">心理辅导信息</a>
			</li>
			</c:if>
			<c:if test="${gyDisplay=='on'}">
			<li class='tab'><a id="interventionInfo" href="#tabs4-html">危机干预信息</a>
			</li>
			</c:if>
		</ul>
		<div id="tabs1-html">
			<div id="personInfo">
				<%@include file="personInfo.jsp"%>
			</div>
		</div>
		<div id="tabs2-html">
			<div id="testInfoTab"></div>
		</div>
		<div id="tabs3-html">
			<div id="recordInfoTab"></div>
		</div>
		<div id="tabs4-html">
			<div id="interventionInfoTab"></div>
		</div>

	</div>






	<script type="text/javascript">
		var startschoolyear = '${startschoolyear}';
		var startterm = '${startterm}';
		var endterm = '${endterm}';
		var endschoolyear = '${endschoolyear}';
		var pp = "&startyear="+startschoolyear+"&startterm="+startterm+"&endyear="+endschoolyear+"&endterm="+endterm;
		
	
		$(function() {
			$('#tab-container').easytabs();
		})

		$("#testInfo")
				.click(
						function() {

							var h = '/pes/archivemanage/archiveGraduatedStudent/studentCompositeReport.do?resultId=2&userid='
									+ $("#userid").val()+pp;
							;
							$('#testInfoTab').load(h);
						});

		$("#recordInfo")
				.click(
						function() {

							var h = '/pes/archivemanage/archiveGraduatedStudent/studentRecord.do?sfzjh='
									+ $("#sfzjh").val()+pp;
							;
							$('#recordInfoTab').load(h);
						});
		$("#interventionInfo")
				.click(
						function() {

							var h = '/pes/archivemanage/archiveGraduatedStudent/warningIntervene.do?status=4&sfzjh='
									+ $("#sfzjh").val()+pp;
							;
							$('#interventionInfoTab').load(h);
						});
	</script>