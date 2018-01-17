<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<njpes:contentHeader title="${appheadtitle }" index="true"/>
<%@include file="header.jsp"%>
<div class="navigation">
	<div class="wraper">
		<%@include file="topmenu.jsp"%>
	</div>
	<div class="contentBanner" id="contentBanner">
		<!-- <img src="images/messagePic.jpg" width="1000" height="136"  alt=""/> -->
	</div>
	<div class="content" id="_content">
 		<%@include file="menu.jsp"%>
 		
 		<!-- 具体主体内容 -->
 		<div id="content2" class="mainContent">
 		
		</div>
		<div id="firstpagecontent" class="firstpagecontentContent" ></div>
 	</div>
 	
 	
	
</div>
<%@include file="footer.jsp"%>
<script>
	$(document).ready(function(){
		    //menuFix();
		    $("li.nav-item")[0].click();
		    
	});
</script> 
<njpes:contentFooter/> 

