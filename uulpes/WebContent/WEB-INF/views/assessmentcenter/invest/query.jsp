<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx}/assessmentcenter/invest"/>
<form:form id="investform" action="${baseaction}/list.do" method="post"  commandName="entity">
	<div class="filterContent">
		<ul>
			<li><label class="name03">问卷名称</label> 
				<form:input placeholder="调查问卷名称关键字" cssClass="input_160" path="name"></form:input>
			</li>
		</ul>
	
		<ul>
			<li><label class="name03">状态</label> <form:select
				path="status"  cssClass="input_160" itemValue="status">
					<option value="">请选择状态</option> 
						<c:forEach var="item" items="${status}">
							<c:forEach items="${item}" var="entry">  
							       <form:option value="${entry.key}">${entry.value}</form:option>
							</c:forEach>  
						</c:forEach>
				</form:select>
			</li>

		</ul>
	</div>
</form:form>
	<div class="buttonContent">
		<div class="buttonLeft">
		  <ul>
			<li>
			<!-- <input class="button-mid blue" type="button" value="下载" id="investdown">-->
			<input class="button-mid white" type="button" chref="${baseaction}/preadd.do" value="新建" id="investadd">
			<input class="button-mid white" type="button" chref="${baseaction}/del.do"  value="删除" id="investdel"></li>
		</ul>
		</div>
		<div class="buttonRight">
		  <ul>
		    <li><input id="searchsuborgs" class="button-mid blue" type="submit"	value="搜索">
		    </li>
		  </ul>
		</div>
	</div>

<script type="text/javascript">
	$(function(){
		//查询调查问卷
		$( "#searchsuborgs" ).on( "click", function() {
			
			debugger;
		   if(!$('#investform').validationEngine('validate'))
				 return false;
			$("#investform").ajaxSubmit({
				target : "#content2",
				success : function() {
					
				},
				error : function() {
					layer.open({content:"查询失败"});
				}
			});
			$("#investform").clearForm();
	 	});
		
		//添加调查问卷
		if($( "#investadd" ) != null){
		   $( "#investadd" ).on( "click", function() {
			   var h = $(this).attr("chref");
			   $("#content2").empty();
			   $("#content2").load(h);
			   console.log(h);
		 	}); 
	    }
		//删除调查问卷
		$( "#investdel" ).click(function(){
			   var url = $(this).attr("chref");
			   var ids = [];
			   $("input[id^='investitem_']").each(function(index){
				   if(this.checked){
					   var id = $(this).attr("id");
					   var itemId = id.replace("investitem_", "");
					   ids.push(itemId);
				   }
			   });
			   delInvest(url, ids);
		   });
		//删除调查问卷方法
	   function delInvest(url, itemIds){
			$.ajax({
				url: url,
				data:{"ids": JSON.stringify(itemIds)},
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
	   }
	});
</script>
