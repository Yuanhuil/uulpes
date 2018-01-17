<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="stuPersonalCommentsurl" value="../../scaletoollib/reportlook/stuPersonalCommentsurl.do"></c:set>
<form:form id="studentPersonalCommentsForm" name="studentPersonalCommentsForm" method="post" commandName="reportLookStudentFilterParam" action="${stuPersonalCommentsurl }">
	<div class="filterContent">
		<ul>
			<li><label class="name03">学段</label> <form:select class="select_140" id="xd_tab3"
					path="xd" onchange="getNj();">
					<form:option value="-1">请选择</form:option>
					<form:options items="${xdList }" itemLabel="xdmc" itemValue="xd" />
				</form:select></li>
			<li><label class="name03">年级名称</label> <form:select class="select_140" id="nj_tab3"
					path="nj" onchange="getBj();">
					<form:option value="-1">请选择</form:option>
				</form:select></li>
			<li><label class="name03">班级名称</label> <form:select class="select_140" id="bj_tab3"
					path="bj">
					<form:option value="-1">请选择</form:option>
				</form:select></li>
			<li><label class="name03">真实姓名</label> <form:input class="select_140" id="username" path="username" type="text"></form:input></li>
		</ul>
	</div>
	<div class="filterContent">
		<ul>
			<li><label class="name03">性别</label> <form:select class="select_140" path="gender">
						<form:option value="3">请选择</form:option>
						<form:option value="1">男</form:option>
						<form:option value="2">女</form:option>
			</form:select></li>
			<li><label class="name03">身份证号</label><form:input class="input_140" id="identiyId" path="identiyId" type="text"></form:input></li>
		</ul>
	</div>
	<div class="buttonContent">
		<ul>
			<li><input class="button-small blue"
				type="submit" value="查询"/></li>
			<li><input class="button-small blue"
				type="button" value="重置" onclick="commentsReset();" /></li>
		</ul>
	</div>
</form:form>
<script type="text/javascript">
function commentsReset(){
	$("#studentPersonalCommentsForm").reset();
}
function getNj() {
	var xd = document.getElementById("xd_tab3").value;
	if (xd == -1) {
		$("#nj_tab3").html("<option value='-1'>请选择</option>");
		$("#bj_tab3").html("<option value='-1'>请选择</option>");
		return;
	}
	$
			.ajax({
				type : "POST",
				url : "../../scaletoollib/reportlook/queryDistinctNJFromExamresultStudent.do",
				data : {
					"xd" : xd
				},
				success : function(msg) {
					var nj = $("#nj_tab3");
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

	var nj = document.getElementById("nj_tab3").value;
	if (nj == -1) {
		$("#bj_tab3").html("<option value='-1'>请选择</option>");
		return;
	}
	var xd = document.getElementById("xd_tab3").value;
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
					var bj = $("#bj_tab3");
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
</script>