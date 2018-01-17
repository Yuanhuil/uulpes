<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@include file="/WEB-INF/views/common/import-css.jspf"%>
<%@include file="/WEB-INF/views/common/import-js.jspf"%>

<head>


<link href="/pes/css/report.css" rel="stylesheet" type="text/css">
<style type="text/css">
.title {
	margin-top: 200px;
	float: none;
	font-size: 1.6em;
	font-weight: normal;
	line-height: 30px;
}

h1, h2, h3, h4, h5 {
	margin: 20px 0px 20px 0px
}


th {
	background-color: #ffffff;
	line-height: 20px;
	font-size:12px;
}

td {
	background-color: #ffffff;
	line-height: 20px;
	text-align: left;
	padding: 0 6px;
	text-overflow: ellipsis;
	/*white-space: nowrap; */
	overflow: hidden;
	font-size:12px;
}

</style>
</head>
<!-- <div class="title">
	<h1>团体档案</h1>
</div>
</br>
</br> -->
<div id="tab-container" class='tab-container'>

	<ul class='etabs' style="text-align: center;">
		<li class='tab'><a href="#tabs1-html">个人基本信息</a>
		</li>
		<li class='tab'><a id="testInfo" href="#tabs2-html">心理测评信息</a></li>
		<li class='tab'><a id="recordInfo" href="#tabs3-html">心理辅导信息</a>
		</li>
		<li class='tab'><a id="interventionInfo" href="#tabs4-html">危机干预信息</a>
		</li>
	</ul>
	 <div id="tabs1-html">
		<div id="personInfo">
			<%@include file="personInfo.jsp"%>
		</div>
	</div> 
	<div id="tabs2-html" class="table01">
		<form:form id="studentTeamReportForm" name="studentTeamReportForm"
			method="post" commandName="reportLookStudentFilterParam"
			target="testInfoTab">
			<div style="text-align: center">
				<ul>

					<li><label>量表名称</label> <form:select id="scaleName"
							path="scaleName" onchange="submit1()">

							<form:options items="${scaleList }" itemLabel="shortname"
								itemValue="id" />
						</form:select></li>
				</ul>
				<form:hidden path="xzxs" />
				<form:hidden path="qxorxxarray" />
				<form:hidden path="xd" />
				<form:hidden path="nj" />
				<form:hidden path="endtime" />
				<form:hidden path="starttime" />


			</div>
		</form:form>
		<div id="testInfoTab"></div>
	</div>
	<div id="tabs3-html" class="table01">
		<table class="table table-hover">
			<tbody>
				<c:forEach items="${list0}" var="m">
					<tr>
						<c:forEach items="${m}" var="m1">
							<td>${m1}</td>
						</c:forEach>


					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>
	<div id="tabs4-html" class="table01">
	<div>
		<table class="table table-hover">
			<tbody>
				<c:forEach items="${list_0}" var="m">
					<tr>
						<c:forEach items="${m}" var="m1">
							<td>${m1}</td>
						</c:forEach>


					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		<div>
		<table class="table table-hover">
			<tbody>
				<c:forEach items="${list_1}" var="m">
					<tr>
						<c:forEach items="${m}" var="m1">
							<td>${m1}</td>
						</c:forEach>


					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		<div>
		<table class="table table-hover">
			<tbody>
				<c:forEach items="${list_2}" var="m">
					<tr>
						<c:forEach items="${m}" var="m1">
							<td>${m1}</td>
						</c:forEach>


					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		<div>
		<table class="table table-hover">
			<tbody>
				<c:forEach items="${list_3}" var="m">
					<tr>
						<c:forEach items="${m}" var="m1">
							<td>${m1}</td>
						</c:forEach>


					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</div>

</div>
<script type="text/javascript">
	$(function() {
		$('#tab-container').easytabs();
		submit1();
	})

	function submit1() {
		var h = '/pes/scaletoollib/reportlook/stuTeamReportsEduCityurl.do';
		/*$('#testInfoTab').load(h); */

		$.post(h, {
			'xzxs':$("#xzxs").val(),
			'qxorxxarray':$("#qxorxxarray").val(),
			'nj' : $("#nj").val(),
			'xd' : $("#xd").val(),
			'scaleName' : $("#scaleName").val(),
			'endtime' : $("#endtime").val(),
			'starttime' : $("#starttime").val()
		}, function(data, textStatus) {
			$("#testInfoTab").html(data);
		});
	}
</script>