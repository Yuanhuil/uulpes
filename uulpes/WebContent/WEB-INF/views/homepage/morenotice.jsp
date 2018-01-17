<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<njpes:contentHeader title="${appheadtitle }" index="true"/>
<style>
 .hot-query {
		padding: 4px 0 0;
		height: auto;
		overflow: hidden;
		width: 100%;
		text-align:center;
	}
.hot-query li {
	display: inline-block;
	border-left: 1px solid #ccc;
	height:20px
	line-height: 20px;
	padding: 0 5px;
}
.ztc_a{color:#ffffff; text-decoration:none;}
.ztc_a:visited{color:#3399CC; text-decoration:none;font-weight:bold;}
.ztc_a:hover{color:#CF0000; text-decoration:underline;font-weight:bold;}
.line-con {
float: left;
}
.line-con a{
margin-left: 5px;
}
.schoolcon{
	max-height: 200px;
	overflow-y: auto;
	background-color: white;
	border: solid;
	border-width: 1px;
	border-color: rgb(185, 185, 172);
}
</style>
<div class="topHeader" id="topHeader">
<div class="logo" style="float:left">
<div class="logoPic">
	<img src="${ctx}/themes/theme1/images/logo.png" width="80" height="80"  alt=""/>
</div>
<div class="titleText">
<div class="mainTitle">心理健康教育信息化管理平台</div>
</div>

</div>

</div>

<div class="imageSlider">
</div>

<form id="morenoticequeryform" name="morenoticequeryform" action="${ctx }/homepage/morenotice.do"></form>

<div class="morepublicindexContent">
  <div class="publicContentMore"><div class="titleBar">
<div class="titleIndex">最新公告</div>
<div class="more"></div>
</div>
<div class="messageList04">
<div id="tablelist" >
	<%@include file="noticetablelist.jsp"%>

</div>
</div>
</div>

</div>
<div id="editformdiv">

</div>
<div id="footer">版权所有 © ${appfooter }</div>
<njpes:contentFooter/> 

