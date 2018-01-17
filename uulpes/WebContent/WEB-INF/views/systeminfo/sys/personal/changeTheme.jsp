<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="mainContent">
	<form:form id="queryform" name="form" modelAttribute="account">

		<div class="tabDiv">
			<a>选择皮肤</a>
			<form:select id="theme" path="theme" cssClass="input_160">
				<form:options items="${themeEnum}" itemLabel="info" itemValue="id" />
			</form:select>
			<input id="searchform" class="button-mid blue" type="button"
				value="保存" onclick="changeTheme()">
		</div>
		<div class="skinList">
			<ul>
				<li onclick="changeThemeValue('theme1')">
					<p>魅力蓝</p>
					<img src="/pes/themes/theme1/images/blue.jpg" width="200" height="200" alt="">
				</li>
				<li onclick="changeThemeValue('theme2')"><p>自然绿</p> <img src="/pes/themes/theme1/images/green.jpg" width="200"
					height="200" alt=""></li>
				<li onclick="changeThemeValue('theme3')">
					<p>魔力灰</p> <img src="/pes/themes/theme1/images/gray.jpg" width="200" height="200"
					alt="">
				</li>
			</ul>
		</div>




	</form:form>

</div>
<script type="text/javascript">
	function changeTheme() {
		$.ajax({
			type : "POST",
			url : "../sys/updateTheme.do",
			data : {
				"theme" : $("#theme").val(),
			},
			success : function(msg) {
				layer.open({content:msg});
				location.reload() ;
			},
			error : function() {
				layer.open({content:"调用出现错误，保存失败"});
			}
		});
	}
	
	function changeThemeValue(value) {
		//$("#theme").find("option[value="+value+"]").attr("selected",true);
		 $("#theme").val(value);
	}
</script>