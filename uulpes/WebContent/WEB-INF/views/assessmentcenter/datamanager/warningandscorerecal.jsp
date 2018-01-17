<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="filterContent">
				<ul>
					<li><label class="name03">量表</label> <select class="select_160" 
							id="scale">
							<option value="-1" >请选择</option>
							<c:forEach var="item" items="${scaleList}">
				        	<option value="${item.code}">${item.title}</option>
							</c:forEach>
						</select></li>
					<li><label class="name03">类别</label> <select class="select_160" id="typeflag" name="typeflag"  >
							<option value="-1">请选择</option>
							<option value="1">学生</option>
							<option value="2">教师</option>
						</select></li>
				</ul>
</div>
<div class="buttonContent">
	<input class="button-small blue" id="recalbutton" type="button" value="重新计算" onclick="recal();" />
</div>

<script>
function recal(){
	var scaleid = $("#scale").val();
	var typeflag = $("#typeflag").val();
	$.ajax({

		type : "POST",
		url : "../../assessmentcenter/datamanager/scaleWarningRecalculate.do",
		data:{
			"scaleid":scaleid,
			"typeflag":typeflag
		},
		success : function(msg) {
			if(msg=="success")
				layer.open({content:'重新计算完成'});
		},
		error : function() {
			layer.open({content:'重新计算失败'});
		}
	});
}
</script>