<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<link rel="stylesheet" type="text/css" href="../../themes/theme1/css/jquery.multiselect.css" />
<script type="text/javascript" src="../../js/jqueryplugin/multiselect/jquery.multiselect.min.js"></script>
<c:set var="statformurl" value="../../scaletoollib/statistics/${statType}/nextStep.do"></c:set>
<form id="StatParams" name="StatParams" method="post" action="${statformurl}">
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div class="stepControl">
		<c:if test="${stepshow>=0}"><div class="step1">第一步</div></c:if>
		<c:if test="${stepshow>=1}"><div class="step2">第二步</div></c:if>
		<c:if test="${stepshow>=2}"><div class="step3">第三步</div></c:if>
		<c:if test="${stepshow>=3}"><div class="step4">第四步</div></c:if>
		<c:if test="${stepshow>=4}"><div class="step4">第五步</div></c:if>
    </div>
    <div class="stepContent">
	    <div class="singleForm">
	      <ul>
	        <li>
	          <label class="name04">机构层级</label>
	   		  <select id="orgLevel" class="selectNormal">
	   		  	  <option value="0" >请选择</option>
		   		  <c:forEach var="item" items="${orgLevelList}">     
		   		  	<option value="${item.id}" >${item.name} </option>
		   		  </c:forEach>
	          </select>
	        </li>
	      </ul>
	    </div>
	    <div class="singleForm">
	      <ul>
	        <li>
	          <label class="name04">行政区划</label>
	   		  <select id="distincts" class="selectNormal">
	   			<option value="0" >请选择</option>
	          </select>
	        </li>
	      </ul>
	    </div>
	    <div class="singleForm">
	      <ul>
	        <li>
	          <label class="name04">学校名称</label>
	   		  <select name="select" id="school" class="selectNormal validate[required]">
	   		  	  <option value="0" >请选择</option>
	   		  	  <c:forEach var="item" items="${schoolList}">     
		   		  	<option value="${item.id}">
		   		  	${item.name}</option>
		   		  </c:forEach>
	          </select>
	        </li>
	      </ul>
	    </div>
	    <c:if test="${statObj==1}">
		    <div class="singleForm">
		      <ul>
		        <li>
		          <label class="name04" >年级名称</label>
		          <select name="select" id="grade" class="selectNormal">
		          	<option value="0" checked="checked">请选择</option>
		          </select>
		        </li>
		      </ul>
		    </div>
	    </c:if>
	    <input type="hidden" name="statObj" value="${statObj}"/>
	    <input type="hidden" name="orgType" value="${orgType}"/>
	</div>
<!--翻页导航start-->
  <div class="pageNav">
	  <input type="button" class="green" id="next" value="下一步">
  </div>
<!--翻页导航end-->
</div>
</form>
<script type="text/javascript">
	$(function(){
		debugger;
		var width = $("#independentVariable").css("width");
		var ds = $("#distincts").multiselect({
			checkAllText: "全选",
			uncheckAllText: '全不选',
			selectedList : 20
		});
		var ss = $("#school").multiselect({
			checkAllText: "全选",
			uncheckAllText: '全不选',
			selectedList : 20
		});
		$(".ui-multiselect").css("width", width);
		var statObj = $("[name=statObj]").val();
		var orgType = $("[name=orgType]").val();
		//组织机构层级下拉框动作
		$("#orgLevel").change(function(val){
			var url = ctx + "/scaletoollib/statistics/getDistricts.do";
			debugger;
			$.ajax({
				url: url,
				data:{"orgLevel": val.target.value},
				type:"POST",
				async:true,
				success:function(d){
					if(d != ""){
						var options = JSON.parse(d);
						$("#distincts").empty();
						//$("#distincts").append("<option value='0' select='selected'>请选择</option>");
						for(var i=0;i<options.length;i++){
							var checked = false;
							$("#distincts").append("<option value='"+options[i].id+"'" + (options.length===1?" select='selected' ":"") + ">"+options[i].name+"</option>");
						}
						ds.multiselect('refresh');
						$(".ui-multiselect").css("width", width);
						ds.multiselect('checkAll');
					}else{
						$("#distincts").empty();
						ds.multiselect('refresh');
						$("#school").empty();
						$("#school").multiselect('refresh');
						$(".ui-multiselect").css("width", width);
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					$("#distincts").empty();
					console.log(XMLHttpRequest);
					console.log(textStatus);
					console.log(errorThrown);
				}
			});
		});
		
		//行政区划下拉框动作
		$("#distincts").change(function(val){
			var url = ctx + "/scaletoollib/statistics/getSchools.do";
			if($("#distincts")){
				distincts = JSON.stringify($("#distincts").multiselect("getChecked").map(function(){
					   return this.value;    
				}).get());
			}
			if(distincts!=undefined && distincts!=""){
				debugger;
				$.ajax({
					url: url,
					data:{"orgLevel": $("#orgLevel").val(), "dist":distincts},
					type:"POST",
					async:true,
					success:function(d){
						if(d != ""){
							var options = JSON.parse(d);
							$("#school").empty();
							//$("#school").append("<option value='0' select='selected'>请选择</option>");
							for(var i=0;i<options.length;i++){
								$("#school").append("<option value='"+options[i].id+"'>"+options[i].xxmc+"</option>");
							}
							ss.multiselect('refresh');
							ss.multiselect('checkAll'); // 0-based index;
							$(".ui-multiselect").css("width", width);
						}else{
							$("#school").empty();
							//$("#school").append("<option value='0' select='selected'>请选择</option>");
						}
					},
					error:function(jqXHR, textStatus, errorThrown){
						$("#school").empty();
						layer.open({content:'错误: ' + jqXHR.responseText});
					}
				});
			}else{
				$("#school").empty();
			}
		});
		
		//学校下拉框动作
		$("#school").change(function(val){
			var url = ctx + "/scaletoollib/statistics/getGrades.do";
			if($("#school")){
				schools = JSON.stringify($("#school").multiselect("getChecked").map(function(){
					   return this.value;    
				}).get());
			}
			debugger;
			$.ajax({
				url: url,
				data:{"school": schools},
				type:"POST",
				async:true,
				success:function(d){
					if(d != ""){
						var options = JSON.parse(d);
						$("#grade").empty();
						$("#grade").append("<option value='0' select='selected'>请选择</option>");
						for(var i=0;i<options.length;i++){
							$("#grade").append("<option value='"+options[i].id+"'>"+options[i].njmc+"</option>");
						}
					}else{
						$("#grade").empty();
						$("#grade").append("<option value='0' select='selected'>请选择</option>");
					}
				},
				error:function(jqXHR, textStatus, errorThrown){
					$("#grade").empty();
					layer.open({content:'错误: ' + jqXHR.responseText});
				}
			});
		});
		
		//下一步按钮动作
		$("#next").click(function(evt){
			if($("#distincts")==undefined || $("#distincts").val()=="0"){
				layer.open({content:"请至少选择行政区划"});
				return false;
			}
			var distincts = "";
			if($("#distincts")){
				distincts = JSON.stringify($("#distincts").multiselect("getChecked").map(function(){
					   return this.value;    
				}).get());
			}
			var schools = "";
			if($("#school")){
				schools = JSON.stringify($("#school").multiselect("getChecked").map(function(){
					   return this.value;    
				}).get());
			}
			if (!$("#editForm").validationEngine('validate'))
				return false;
			$("#StatParams").ajaxSubmit({
				target : "#"+$("#tab-container div .active")[0].id,
				data: {
					"orgLevel": $("#orgLevel").val(),
					"district": distincts,
					"school": schools, 
					"grade": $("#grade").val(), 
					"starttime": $( "#starttime" ).val(),
					"endtime": $( "#endtime" ).val(),
					"step":"${step+1}",
					"stepshow":"${stepshow}"
				},
				success : function() {
					//layer.open({content:"保存成功!"});
				},
				error:function(jqXHR, textStatus, errorThrown){
					layer.open({content:'错误: ' + jqXHR.responseText});
				}
			});
		});
		//如果是学校默认选择当前组织机构
		debugger;
		if(orgType==2){
			$("#school option:eq(1)").attr('selected','selected');
			$("#school").attr('disabled',true);
			$("#school").trigger("change");
		}
	});
</script>