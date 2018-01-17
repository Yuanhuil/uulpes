<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<html>
<head><title>发生错误</title></head>
<script type="text/javascript">
	$(function(){
		$("#showerrordetails").click(function(){
			if($("#errordetails").is(":hidden")){
				$("#showerrordetails").html("隐藏详细错误信息：");
				$("#errordetails").show();
			}else{
				$("#showerrordetails").html("显示详细错误信息：");
				$("#errordetails").hide();
			}
		});
	});
</script>
<body>
<% Exception e = (Exception)request.getAttribute("ex"); %>
<H2>错误异常: <%= e.getClass().getSimpleName()%></H2>
<hr />
<P>错误描述：</P>
程序运行是发生异常错误，如看见该页面请联系管理员，点击【显示详细错误信息】，将该错误发给管理员。<br>
<P><a id="showerrordetails" style="cursor:pointer;">显示详细错误信息：</a></P>
<div id="errordetails" style="display:none;"> 
<%= e.getMessage()%>
<% e.printStackTrace(new java.io.PrintWriter(out)); %>
</div>
</body>
</html>