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
<div style="width: 503px;float: left;padding-top: 0px;height: 90px;">
<div style="float:right;margin-top:0px;">
	<a href="${ctx }/login.do" ><input class="button-mid blue" id="loginid" type="button" value="登录系统"></a>
</div>
</div>
</div>

<div class="imageSlider">
<c:if test="userlevel==3">
  <div style="">
  <ul class="hot-query">
			<li id="directtrainitem1" class="directtrainitem" style="margin:5px 0px;background-color: rgb(0, 153, 204)"><a class="ztc_a" href="javascript:void(0)">南京市</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320102')"><a class="ztc_a" href="javascript:void(0)" >玄武区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320103')"><a class="ztc_a" href="javascript:void(0)" >白下区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320104')"><a class="ztc_a" href="javascript:void(0)">秦淮区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320105')"><a class="ztc_a" href="javascript:void(0)">建邺区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320106')"><a class="ztc_a" href="javascript:void(0)">鼓楼区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320107')"><a class="ztc_a" href="javascript:void(0)">下关区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320111)"><a class="ztc_a" href="javascript:void(0)">浦口区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320113)"><a class="ztc_a" href="javascript:void(0)">栖霞区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320114)"><a class="ztc_a" href="javascript:void(0)">雨花区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320121)"><a class="ztc_a" href="javascript:void(0)">江宁区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320123)"><a class="ztc_a" href="javascript:void(0)">六合区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320112)"><a class="ztc_a" href="javascript:void(0)">开发区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320124)"><a class="ztc_a" href="javascript:void(0)">溧水县</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320125)"><a class="ztc_a" href="javascript:void(0)">高淳县</a></li>
		</ul>
  </div>
  </c:if>
</div>
<div class="publicindexContent">
  <div class="publicContent"><div class="titleBar">
<div class="titleIndex">最新公告</div>
<div class="more"><a href="${ctx }/homepage/-1/morenotice.do" target="_blank">更多…</a></div>
</div>
<div class="messageList03">
    <ul>
		<c:forEach var="m" items="${jobnoticelist }">
					<li><span class="timeRight"> <fmt:formatDate
								value="${m.writeTime}" type="date" />
					</span><a href="#" class="titleLeft viewnotice" chref="${ctx }/workschedule/notice/${m.id }/view.do">${m.title}</a></li>
		</c:forEach>
	</ul>
</div>
</div>
<div class="publicContent"><div class="titleBar">
<div class="titleIndex">活动计划</div>
<div class="more"><a href="${ctx }/homepage/-1/moreactivityplan.do" target="_blank">更多…</a></div>
</div>
<div class="messageList03">
<ul>
		<c:forEach var="m" items="${jobplanlist }">
			<li><span class="timeRight">${m.schoolyear}学年</span><a href="#" class="titleLeft viewactivityplan" chref="${ctx }/workschedule/activityplan/${m.id }/view.do">${m.title
				}</a></li>
		</c:forEach>
</ul>
</div>
</div>
<div class="publicContent">
<div class="titleBar">
<div class="titleIndex">活动总结</div>
<div class="more"><a href="${ctx }/homepage/-1/moreactivitysummary.do" target="_blank">更多…</a></div>
</div>
<div class="messageList03">
	<ul>
		<c:forEach var="m" items="${jobsummarylist }">
					<li><span class="timeRight"><fmt:formatDate
								value="${m.createTime}" type="date" /></span><a href="#"  class="titleLeft viewactivitysummary" chref="${ctx }/workschedule/activitysummary/${m.id }/view.do">${m.title
							}</a></li>
		</c:forEach>
	</ul>
</div>
</div>
<!-- 
<div class="publicContent">
<div class="titleBar">
<div class="titleIndex">区县公告</div>
<div class="more">more</div>
<div class="messageList03">
<ul>
<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>


</ul>
</div>
</div>
</div>
-->
</div>
<div id="editformdiv">

</div>
<div id="footer">版权所有 © ${appfooter }</div>
<njpes:contentFooter/> 
<script type="text/javascript">
	$(function() {
		$('#slider').nivoSlider({ controlNav: false}); 
		$(".titleLeft.viewnotice").click(function() {
			debugger;
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function(response, status, xhr) {
				$("#viewdialog").dialog("open");
			});
		});
		$(".titleLeft.viewactivityplan").click(function() {
			debugger;
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function(response, status, xhr) {
				$("#viewdialog").dialog("open");
			});
		});
		$(".titleLeft.viewactivitysummary").click(function() {
			debugger;
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function(response, status, xhr) {
				debugger;
				$("#editdialog").dialog("open");
			});

			//layer.open({area: ['700px','500px'],type: 2,title:'活动总结', content:h});
		});
	});

</script>
