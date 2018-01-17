<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div class="tableContent">
	<table>
		<tr class="titleBg">
			<td width="5%"><input type="checkbox" id="selAll" /></td>
			<td width="25%">问卷名称</td>
			<td width="8%">题目数量</td>
			<td width="8%">状态</td>
			<td width="30%">操作</td>
		</tr>
		<c:forEach var="item" items="${invests}">
			<tr>
				<td><input type="checkbox" id="investitem_${item.id}" /></td>
				<td>${item.name}</td>
				<td >${item.qnum}</td>
				<td>
					<c:if test="${item.status==0}">
						审核中
					</c:if>
					<c:if test="${item.status==1}">
						已发布
					</c:if>
				</td>
				<td width="20%"><span class="header02"> 
					<input class="button-small white review" id="review_${item.id}" type="button" chref="${ctx}/assessmentcenter/invest/${item.id}/view.do" value="查看">
					<input class="button-small white update" id="update_${item.id}" type="button" chref="${ctx}/assessmentcenter/invest/${item.id}/update.do" value="修改"> 
					<input class="button-small white del" type="button" chref="${ctx}/assessmentcenter/invest/${item.id}/del.do" value="删除">
					<input class="button-small white distribute" type="button" 
					<c:choose>
					<c:when test="${orgtype=='2'}">
					  chref="${ctx}/assessmentcenter/invest/${item.id}/schooldispenseview.do"
					</c:when>
					<c:when test="${orgtype=='1'}">
					  chref="${ctx}/assessmentcenter/invest/${item.id}/edudispenseview.do"
					</c:when> 
					<c:when test="${orgtype=='3'}">
					  chref="${ctx}/assessmentcenter/invest/${item.id}/edudispenseview.do"
					</c:when> 
					</c:choose>
					value="分发">
				</span></td>
			</tr>
			
		</c:forEach>
	</table>
	
</div>

<script type="text/javascript">
	$(function(){
		//查看调查问卷
		$( ".button-small.white.review" ).click(function(){
		   var h = $(this).attr("chref");
		   $('#editformdiv').empty();
		   $('#editformdiv').load(h,function(){
			   $( "#dialog-form" ).dialog("open");
		   });
	   });
		//全选动作
	   $( "#selAll" ).click(function(){
		   var checked = this.checked;
		   $("input[id^='investitem_']").each(function(index){
			   if(checked){
				   this.checked = true;
			   }else{
				   this.checked = false;
			   }
		   });
	   });
		//删除问卷
	   $( ".button-small.white.del" ).click(function(){
		   var url = $(this).attr("chref");
		   $.ajax({
				url: url,
				type:"POST",
				async:true,
				success:function(d){
					if(d === "success"){
						layer.open({content:"删除成功"});
						$("#content2").load(ctx + "/assessmentcenter/invest/list.do");
					}else{
						layer.open({content:"出错重试"});
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					console.log(XMLHttpRequest);
					console.log(textStatus);
					console.log(errorThrown);
				}
			});
	   });
	   
	   //修改问卷
	   $( ".button-small.white.update" ).click(function(){
		   var h = $(this).attr("chref");
		   $("#content2").empty();
		   $("#content2").load(h);
	   });
	   
	   //查看问卷
	   $(".button-small.white.review").click(function() {
			var h = $(this).attr("chref");
			$('#checkScaleDialog').empty();
			$("#checkScaleDialog").load(h, function() {
				$("#checkScaleDialog").dialog({
					title : '查看调查问卷',
					autoOpen : true,
					modal : true,
					width : 600,
					height : 600,
					buttons : [ {
						text : '关闭',
						click : function() {
							$("#checkScaleDialog").dialog("close");
						}
					}]
				});
			});
		});
	   
	});
	//修改问卷
	   $( ".button-small.white.distribute" ).click(function(){
		   var h = $(this).attr("chref");
		   $("#content2").empty();
		   $("#content2").load(h);
	   });
	   
</script>
