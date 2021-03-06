<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<njpes:contentHeader title="南京中小学心理健康教育系统" index="true"/>
<style>
 .hot-query {
		padding: 4px 0 0;
		height: auto;
		overflow: hidden;
		width: 100%;
	}
.hot-query li {
	display: inline-block;
	border-left: 1px solid #ccc;
	height:20px
	line-height: 20px;
	padding: 0 5px;
}
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
<div class="logo">
<div class="logoPic">
	<img src="${ctx}/themes/theme1/images/logo.png" width="80" height="80"  alt=""/>
</div>
<div class="titleText">
<div class="mainTitle">中小学心理健康教育</div>
</div>
</div>
<div>
	<a href="${ctx }/login.do" target="_blank"><input id="loginid" type="button" value="登录系统"></a></input>
</div>
</div>
<div class="navigation">
	<div class="content" id="_content">
		<div id="firstpagecontent" class="firstpagecontentContent" >
			<div class="leftContent">
	<div class="titleBar">
		<div class="titleIndex">最新公告</div>
		<div class="more">more</div>
	</div>
	<div class="content01">
		<div class="messageImage theme-default"> 
		<div id="slider" class="nivoSlider">
				<c:forEach var="m" items="${jobnoticelist }">
					<img
					src="${m.firstpageimageUrl }"
					alt="" />
				</c:forEach>
		</div>
		</div>
	</div>
		<div class="messageList" id="jobnoticelist">
			<ul>
				<c:forEach var="m" items="${jobnoticelist }">
					<li><span class="timeRight"> <fmt:formatDate
								value="${m.writeTime}" type="date" />
					</span><a class="titleLeft viewnotice" chref="${ctx }/workschedule/notice/${m.id }/view.do">${m.title}</a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
<div class="rightContent">
	<div class="titleBar">
		<div class="titleIndex">单位简介</div>
		<div class="more">more</div>
		<div class="messageImage">
			<!-- <img src="${ctx}/themes/${sessionScope.user.theme}/images/pic002.png"
				alt="" />-->
		    <img src="${orgimage}" alt="" />
		</div>
		<div class="messageContent">${orgintroduce}...</div>
	</div>
</div>
<div class="leftContent_900">
	<div class="titleBar">
		<div class="titleIndex">活动计划</div>
	</div>
	<c:forEach var="m" items="${jobplanlist }">
		<div class="imageList">
			<h1>${m.title }</h1>
			<p>${m.schoolyear}学年</p>
		</div>
	</c:forEach>

	<div class="titleBar">
		<div class="titleIndex">活动总结</div>
		<div class="messageList02">
			<ul>
				<c:forEach var="m" items="${jobsummarylist }">
					<li><span class="timeRight"><fmt:formatDate
								value="${m.createTime}" type="date" /></span><a class="titleLeft viewactivity" chref="${ctx }/workschedule/active/${m.id }/view.do">${m.title
							}</a></li>
				</c:forEach>
			</ul>
		</div>

	</div>
</div>
<div class="rightContent">
	<div class="titleBar">
		<div class="titleIndex">部门简介</div>
		<div class="more">more</div>
		<div class="messageImage">
			<img src="${ctx}/themes/theme1/images/pic003.png"
				alt="" />
		</div>
		<div class="messageContent">${introduce.content}</div>

	</div>
</div>
<div class="rightContent">
	<div class="titleBar">
		<div class="titleIndex">直通车</div>
		<div class="more">more</div>
	</div>
	<div class="messageList01" style="position:relative;z-index:9999">
		<ul class="hot-query">
			<li id="directtrainitem1" class="directtrainitem" style="margin:5px 0px;background-color: rgb(0, 153, 204)" onclick="gotoDirectTrain(this,'320102')"><a href="javascript:void(0)">玄武区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320103')"><a href="javascript:void(0)" >白下区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320104')"><a href="javascript:void(0)">秦淮区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320105')"><a href="javascript:void(0)">建邺区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320106')"><a href="javascript:void(0)">鼓楼区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,'320107')"><a href="javascript:void(0)">下关区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320111)"><a href="javascript:void(0)">浦口区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320113)"><a href="javascript:void(0)">栖霞区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320114)"><a href="javascript:void(0)">雨花区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320121)"><a href="javascript:void(0)">江宁区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320123)"><a href="javascript:void(0)">六合区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320112)"><a href="javascript:void(0)">开发区</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320124)"><a href="javascript:void(0)">溧水县</a></li>
			<li class="directtrainitem"  style="margin:5px 0px;" onclick="gotoDirectTrain(this,320125)"><a href="javascript:void(0)">高淳县</a></li>
		</ul>
		<div class="schoolcon" id="schoolcon">
		   
		</div>
		<!-- <ul>
			<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
			<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
			<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
			<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
			<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
			<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
			<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
			<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>

		</ul>-->
	</div>
</div>
<div class="rightContent">
	<div class="titleBar">
		<div class="titleIndex">区县公告</div>
		<div class="more">more</div>
		<div class="messageList01">
			<ul>
				<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
				<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
				<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
				<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
				<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
				<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
				<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>
				<li><span class="timeRight">2015/7/5</span><a class="titleLeft">南京市2014-2015</a></li>

			</ul>
		</div>
	</div>
</div>
		
		</div>
 	</div>
 	
 	
	
</div>

<div id="editformdiv">

</div>
<njpes:contentFooter/> 
<script type="text/javascript">
	$(function() {
		var directtrainitem1 = document.getElementById("directtrainitem1");
		gotoDirectTrain(directtrainitem1,'320102');
		$('#slider').nivoSlider({ controlNav: false}); 
		$(".titleLeft.viewnotice").click(function() {
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function(response, status, xhr) {
				$("#viewdialog").dialog("open");
			});
		});
		$(".titleLeft.viewactivity").click(function() {
			var h = $(this).attr("chref");
			$('#editformdiv').empty();
			$('#editformdiv').load(h, function(response, status, xhr) {
				$("#viewdialog").dialog("open");
			});
		});

	});
	function gotoDirectTrain(node,code){
		debugger;
		$(".hot-query .directtrainitem").css("backgroundColor", "");
		if(node)
			node.style.backgroundColor = '#09C';

		$.ajax({
			   type: "POST",
			   url: "/pes/ajax/getDirectTrainForSchools.do",
			   data: {
				   "code":code
				   },
			   success: function(msg){
				   debugger;
					var schoolarray = jQuery.parseJSON(msg);
					var schoolcon = $("#schoolcon");
					if(schoolarray.length>0){
						var astr="<p style='line-height:20px;'>";
						for(var i=0;i<schoolarray.length;i++){
							var xxmc = schoolarray[i].xxmc;
							if(xxmc.indexOf("市")>0){
								var xxmcStrArray = xxmc.split("市");
								if(xxmcStrArray.length>1)
									xxmc = xxmcStrArray[1];
							}
							if(xxmc.indexOf("县")>0){
								var xxmcStrArray = xxmc.split("县");
								if(xxmcStrArray.length>1)
									xxmc = xxmcStrArray[1];
							}
							if(xxmc.indexOf("区")>0){
								var xxmcStrArray = xxmc.split("区");
								if(xxmcStrArray.length>1)
									xxmc = xxmcStrArray[1];
							}
							
							var zydz = schoolarray[i].zydz;
							astr = astr+"<a href='http://"+zydz+"' target='_Blank' style='margin-right:15px;'>"+xxmc+"</a>";
						}
						astr+="</p>"
						schoolcon.html(astr);
					}else{
						schoolcon.html("");
					}
			   }
			});
	}
</script>
