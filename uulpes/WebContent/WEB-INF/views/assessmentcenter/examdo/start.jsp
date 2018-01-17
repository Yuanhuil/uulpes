<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<!-- controller传到前台的分发量表列表 -->

<div class="tableContent">
	<table>
		<tr class="titleBg">
			<td width="40%">量表名称</td>
			<td width="40%">${scale.title}</td>
		</tr>
		<tr class="titleBg">
			<td width="40%">量表简称</td>
			<td width="40%">${scale.shortname}</td>
		</tr>
		<tr class="titleBg">
			<td width="40%">量表来源</td>
			<td width="40%">${scale.source}</td>
		</tr>
		<tr class="titleBg">
			<td width="40%">量表简介</td>
			<td width="40%">${scale.descn}</td>
		</tr>
		<tr class="titleBg">
			<td width="40%">指导语</td>
			<td width="40%">${scale.guidance}</td>
		</tr>
		<tr class="titleBg">
			<td width="40%">题目数量</td>
			<td width="40%">${scale.questionNum}</td>
		</tr>
		<tr class="titleBg">
			<td colspan="2">
				<span class="header02"> 
					<input class="button-small white firstpage" type="button" chref="${ctx}/assessmentcenter/examdo/${scale.code}/0.do" value="开始">
				</span>
			</td>
		</tr>
		
	</table>
	
</div>
<script type="text/javascript">
   $(function(){
	   $( ".button-small.white.firstpage" ).click(function(){
		   var h = $(this).attr("chref");
		   $("#content2").load(h);
	   });
   })
</script>
