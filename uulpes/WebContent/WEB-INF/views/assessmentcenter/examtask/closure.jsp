<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/report"/>
	<style type="text/css">
		.img1{
			position:relative;
			<c:if test="${page.image1}">background-image: url(${baseaction}/reportchart.do?${page.image1});</c:if>
			background-position:center;
		}
	</style>

<div class="tableContent1">
<div class="rpt_title" ><h1>【${page.userinfo.xm}】【${page.scale.title}】个人测评报告</h1></div>
<div class="rpt_p_title" ><h3>一、基本信息</h3></div>
<div class="rpt_table01" >
<c:if test="${page.typeflag==1}">
<table width="95%" border="1" cellspacing="1" cellpadding="1">
  <tr>
    <th width="15%" scope="col"><strong>学校</strong></th>
    <th colspan="3" scope="col">${page.userinfo.xxmc}</th>
  </tr>
  <tr>
    <td width="15%"><strong>姓名</strong></td>
    <td>${page.userinfo.xm}</td>
    <td width="30%"><strong>身份证号</strong></td>
    <td>${page.userinfo.sfzjh}</td>
  </tr>
  <tr>
    <td width="15%"><strong>姓名拼音</strong></td>
    <td>${page.userinfo.xmpy}</td>
    <td><strong>学号</strong></td>
    <td width="30%">${page.userinfo.xh}</td>
    </tr>
  <tr>
    <td><strong>年级</strong></td>
    <td>${page.userinfo.njmc}</td>
    <td><strong>班级</strong></td>
    <td>${page.userinfo.bjmc}</td>
    </tr>
  <tr>
    <td><strong>性别</strong></td>
    <td>${page.userinfo.xb }</td>
	<td><strong>民族</strong></td>
    <td>${page.userinfo.mz}</td>
    </tr>
  <tr>
	<td><strong>测评时间</strong></td>
    <td colspan="3">${page.endTime}</td>
    </tr>
</table>
</c:if>
<c:if  test="${page.typeflag==2}">
<table width="95%" border="1" cellspacing="1" cellpadding="1">
  <tr>
    <th width="15%" scope="col"><strong>学校</strong></th>
    <th colspan="3" scope="col">${page.userinfo.xxmc}</th>
  </tr>
  <tr>
    <td width="15%"><strong>姓名</strong></td>
    <td>${page.userinfo.xm}</td>
    <td width="30%"><strong>身份证号</strong></td>
    <td>${page.userinfo.sfzjh}</td>
  </tr>
  <tr>
    <td width="15%"><strong>姓名拼音</strong></td>
    <td>${page.userinfo.xmpy}</td>
    <td><strong>工号</strong></td>
    <td width="30%">${page.userinfo.gh}</td>
    </tr>
  <tr>
    <td><strong>性别</strong></td>
    <td>${page.userinfo.xb }</td>
	<td><strong>民族</strong></td>
    <td>${page.userinfo.mz}</td>
    </tr>
  <tr>
	<td><strong>测评时间</strong></td>
    <td colspan="3">${page.endTime}</td>
    </tr>
</table>
</c:if>
</div>
<div class="rpt_p_title" >
<h3 class="rpt_h3">二、量表简介</h3>
<p class="rpt_p">${page.scale.descn}</p>
</div>
<div class="rpt_p_title" ><h3>三、测评结果</h3></div>
<!-- <div class="rpt_table01">
${page.dimScoreTable}
</div>
<div class="rpt_chart">
	<c:if test="${not empty page.isImage1Display && page.isImage1Display==1}"><c:if test="${not empty page.image1}"><img src='${baseaction}/scalechart.do?${page.image1}'/></c:if></c:if>
	<c:if test="${not empty page.image2}"><img src='${baseaction}/reportchart.do?${page.image2}'/></c:if>
</div>
-->

<c:choose>
	<c:when test="${page.reportGraph=='折-1中' }">
		<div >
		<%@include file="16pf.jsp"%>
		</div>
	</c:when>
	<c:otherwise>
		<div class="rpt_table01">
			${page.dimScoreTable}
		</div>
	    <div class="rpt_chart">
			<c:if test="${not empty page.isImage1Display && page.isImage1Display==1}"><c:if test="${not empty page.image1}"><div><img src='${baseaction}/scalechart.do?${page.image1}'/></div></c:if></c:if>
			<c:if test="${not empty page.image2}"><div><img src='${baseaction}/reportchart.do?${page.image2}'/></div></c:if>
			<c:if test="${not empty page.image3}"><div><img src='${baseaction}/reportchart.do?${page.image3}'/></div></c:if>
		</div>
	</c:otherwise>
</c:choose>


<div class="rpt_p_title" >
<h3 class="rpt_h3">四、结果解释与指导建议</h3>
<c:if test="${page.scaleshortname!='MBTI'}">
	<c:forEach var="dimDetail" items="${page.dimDetailList}">
		<c:if test="${not empty dimDetail.desc}"><h3 style="margin: 10px auto 10px 30px;">${dimDetail.dimtitle}</h3></c:if>
		<c:if test="${not empty dimDetail.desc}"><p class="rpt_p">结果解释：${dimDetail.desc}</p></c:if>
		<c:if test="${not empty dimDetail.device}"><p class="rpt_p">指导建议：${dimDetail.device}</p></c:if>
		<c:if test="${!empty dimDetail.subdimlist}">
			<c:forEach var="subDimDetail" items="${dimDetail.subdimlist}">
				<h4 style="margin: 10px auto 10px 30px;">${subDimDetail.dimtitle}</h4>
				<c:if test="${not empty subDimDetail.desc}"><p class="rpt_p">结果解释：${subDimDetail.desc}</p></c:if>
				<c:if test="${not empty subDimDetail.device}"><p class="rpt_p">指导建议：${subDimDetail.device}</p></c:if>
			</c:forEach>
		</c:if>
		<c:if test="${not empty dimDetail.image}"><div class="rpt_chart"><img src='${baseaction}/${dimDetail.image}'/></div></c:if>
	</c:forEach>
	
	<!--<c:if test="${not empty page.dimsDescnTable}"><p class="rpt_p">${page.dimsDescnTable}</p></c:if>
	<c:if test="${not empty page.summarize}"><p class="rpt_p"><strong>${page.summarize}</strong></p></c:if>-->
	
	<c:if test="${not empty page.summarizedesc}"><h4>总结果解释：${page.summarizedesc}</h4></c:if>
	<c:if test="${not empty page.summarizedevice}"><h4>总指导建议：${page.summarizedevice}</h4></c:if>
</c:if>
<c:if test="${page.scaleshortname=='MBTI'}">
	<c:if test="${not empty page.mbtiDesripter}"><p>结果解释：${page.mbtiDesripter}</p></c:if>
	<c:if test="${not empty page.mbtiAdvice}"><p>指导建议：${page.mbtiAdvice}</p></c:if>
</c:if>
</div>
</div>
<script>
$(function(){
	if( $('#timelimitpage').length>0){
		window.clearInterval(timeID);
	    $('#timelimitpage').css("display","none");
	}
});
</script>