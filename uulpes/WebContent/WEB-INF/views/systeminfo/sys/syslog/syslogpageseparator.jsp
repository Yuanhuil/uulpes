<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style type="text/css">
.page {
	height: 18px;
	width: 40px;
	float: left;
	padding-left: 7px;
	padding-right: 7px;
	text-align: center;
	line-height: 18px;
}

.page a {
	display: block;
	height: 18px;
	width: 40px;
}

.page a:hover {
	color: #009;
	text-decoration: underline;
}

.page_text {
	height: 14px;
	width: 50px;
	line-height: 14px;
	text-align: center;
}      
</style>
<c:set var="searchlog" value="../sys/searchSyslogs.do"></c:set>
<div
	style="height: 18px; width: 500px; margin: 20px 0px 0px 0px; float: right;">
	<div class="page" style="width: 60px;">
		共
		<c:out value="${pageSeparatorEntity.itemCount}"></c:out>
		条
	</div>
	<div class="page" style="width: 50px;">
		第
		<span id="pageIndex"><c:out value="${pageSeparatorEntity.pageNumber}"></c:out></span>
		页
	</div>
	<div class="page" style="width: 50px;">
		共
		<c:out value="${pageSeparatorEntity.pageCount}"></c:out>
		页
	</div>
	<div class="page">
		<c:choose>
			<c:when test="${pageSeparatorEntity.pageNumber>1}">
				<a href='javascript:redirectPage("${pageSeparatorEntity.pageNumber-1}")'>上一页</a>
			</c:when>
			<c:otherwise>
				              上一页
				    </c:otherwise>
		</c:choose>
	</div>
	<div class="page">
		<c:choose>
			<c:when test="${pageSeparatorEntity.pageNumber<pageSeparatorEntity.pageCount}">
				<a href='javascript:redirectPage("${pageSeparatorEntity.pageNumber+1}")'>下一页</a>
			</c:when>
			<c:otherwise>
				             下一页
				    </c:otherwise>
		</c:choose>
	</div>
	<div class="page" style="width: 10px;">
		<a href="#" style="width: 10px;">GO</a>
	</div>
	<div class="page" style="padding: 0px;">
		<input id="setPageNumber" name="setPageNumber" type="text"
			class="page_text" />
	</div>
	<div class="page" style="padding-left: 20px;">
		<a href="#"><img src="../../themes/theme1/images/button_03.png"
			onclick="redirectPage(document.getElementById('setPageNumber').value)"></a>
	</div>
</div>
<script type="text/javascript">
var pageCount=${pageSeparatorEntity.pageCount};
//跳转到指定页面
function redirectPage(pageNumber) 
{
	if(checkPageNumber(pageNumber))
	{
		syslogFilterForm.pageNumber.value = pageNumber;
		syslogFilterForm.action="../sys/pageSearchSyslogs.do";
		/* $("#syslogFilterForm").submit(function() {
		    // 提交表单
		    $(this).ajaxSubmit();
		    // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
		    return false;
		   }); */
		var options={
				target : "#tableContent"
		};
		 $("#syslogFilterForm").ajaxSubmit(options);
	//	syslogFilterForm.submit();
	}
}
//判断当前输入页码是否有效
function checkPageNumber(pageNumber) 
{
	if (pageNumber < 1) {
		layer.open({content:"页码必须大于0！"});
		return false;
	} 
	else if (pageNumber <= pageCount) 
	{
		return true;
	} 
	else {
		layer.open({content:"页码输入过大！"});
		return false;
	}
}
</script>