<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="stuComplianceReportStuUrl"
	value="../../scaletoollib/reportlook/searchStuComplianceReportStuUrl.do"></c:set>
<form:form id="studentComplianceReportForm"
	name="studentComplianceReportForm" method="post"
	commandName="reportLookStudentFilterParam"
	action="${stuComplianceReportStuUrl }">
	<div class="filterContent">
		<ul>
			<li><label class="name03">学段</label> <form:select id="xd_tab2" class="select_160"
					path="xd" onchange="getNj();">
					<form:option value="-1" id="se2">请选择</form:option>
					<form:options name="xdse2" items="${xdList }" itemLabel="xdmc" itemValue="xd" />
				</form:select></li>
			<li><label class="name03">年级名称</label> <form:select id="nj_tab2" class="select_160" 
					path="nj" onchange="getBj();">
					<form:option value="-1">请选择</form:option>
				</form:select></li>
			<li><label class="name03">班级名称</label> <form:select id="bj_tab2" class="select_160" 	path="bj">
					<form:option value="-1">请选择</form:option>
				</form:select></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">性别</label> <form:select path="gender"  class="select_160">
					<form:option value="-1">请选择</form:option>
					<form:option value="1">男</form:option>
					<form:option value="2">女</form:option>
				</form:select></li>
			<li><label class="name03">真实姓名</label> <form:input id="username"
					path="username" type="text"  class="input_160"></form:input></li>
			<li><label class="name03">身份证号</label> <form:input
					id="identiyId" path="identiyId" type="text"  class="input_160"></form:input></li>
		</ul>
	</div>
	<div class="buttonContent">
		
			<input class="button-small blue" type="button" value="查询" onclick="searchReport();"/>
			<input class="button-small blue" type="button" value="重置"	onclick="complianceCommentsReset();" />
		
	</div>
</form:form>
<script type="text/javascript">
	function complianceCommentsReset() {
		$("#studentComplianceReportForm")[0].reset();
		$("#nj_tab2").html("<option value='-1'>请选择</option>");
		$("#bj_tab2").html("<option value='-1'>请选择</option>");
	}
	function getNj() {
		var xd = document.getElementById("xd_tab2").value;
		if (xd == -1) {
			$("#nj_tab2").html("<option value='-1'>请选择</option>");
			$("#bj_tab2").html("<option value='-1'>请选择</option>");
			return;
		}
		$
				.ajax({
					type : "POST",
					url : "../../scaletoollib/reportlook/queryDistinctNJFromExamresultStudent.do",
					data : {
						"xd" : xd,
						"xzxs":null
					},
					success : function(msg) {
						var nj = $("#nj_tab2");
						var njarray = jQuery.parseJSON(msg);
						if (njarray.length > 0) {
							var optionstr = "<option value='-1'>请选择</option>";
							for ( var i = 0; i < njarray.length; i++) {
								var njmc = njarray[i].nj;
								optionstr = optionstr
										+ "<option value="+njmc+">" + njmc
										+ "级</option>";
							}
							nj.html(optionstr);
						} else {
							nj.html("");
							nj.html("<option value='-1'>请选择</option>");
						}
					},
					error : function() {
						layer.open({content:"调用出现错误，查找年级失败"});
					}
				});
	}

	function getBj() {

		var nj = document.getElementById("nj_tab2").value;
		if (nj == -1) {
			$("#bj_tab2").html("<option value='-1'>请选择</option>");
			return;
		}
		var xd = document.getElementById("xd_tab2").value;
		if (xd == -1) {
			return;
		}
		$
				.ajax({
					type : "POST",
					url : "../../scaletoollib/reportlook/queryDistinctBJFromExamresultStudent.do",
					data : {
						"xd" : xd,
						"nj" : nj
					},
					success : function(msg) {
						var bj = $("#bj_tab2");
						var bjarray = jQuery.parseJSON(msg);
						if (bjarray.length > 0) {
							var optionstr = "<option value='-1'>请选择</option>";
							for ( var i = 0; i < bjarray.length; i++) {
								var bjj = bjarray[i].bj;
								var bjmc = bjarray[i].bjmc;
								optionstr = optionstr
										+ "<option value="+bjj+">" + bjmc
										+ "</option>";
							}
							bj.html(optionstr);
						} else {
							bj.html("");
							bj.html("<option value='-1'>请选择</option>");
						}
					},
					error : function() {
						layer.open({content:"调用出现错误，查找班级失败"});
					}
				});
	}
	
	function searchReport(){
		debugger;
		$("#studentComplianceReportForm").ajaxSubmit({
			//target : "#tabs_studentReport",
			target : "#studentComplianceReportTableCon",
			success:function(){
				//var xxddsa = document.getElementsByName("xdse2");
				//for(var i=0;i<xxddsa.length;i++){
					//xxddsa[i].removeAttribute("selected");
				//}
				//document.getElementById("se2").setAttribute("selected","selected");
			}
		});
	}
</script>