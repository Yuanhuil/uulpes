<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/organization/sub/ec"/>
<form:form id="suborgform" action="${baseaction}/search.do" method="post"  modelAttribute="entity">
	<form:hidden path="org.orgType"/>
	<form:hidden path="org.orgLevel"/>
	<form:hidden path="org.parentId"/>
	<c:if test="${entity.org.orgLevel ne '5' or entity.org.orgLevel ne '6'}">
		<div class="filterContent1" id="dzdiv">
		<ul>
			<li><label class="name03">行政区划</label> 
			<form:select path="org.provinceid" cssClass="prov input_140" style="float:left;margin-left:43px;"></form:select>
				<form:select path="org.cityid" cssClass="city input_140" style="float:left;margin-left:10px;"></form:select>
				<form:select path="org.countyid" cssClass="dist input_140" style="float:left;margin-left:10px;"></form:select>
				<form:select path="org.townid" cssClass="street input_140" style="float:left;margin-left:10px;"></form:select>
		</ul>
		</div>
	</c:if>
	<div class="buttonContent">
		<div class="buttonLeft">
			<ul>
			<li><form:input placeholder="组织机构关键字" cssClass="input_100"
					path="org.name"/> <input id="searchsuborgs" class="button-mid white" type="submit"
					value="搜索"></li>
				<li>
				    <shiro:hasPermission name="systeminfo:organization:suborganization:create"><input class="button-mid white" type="button" value="新增" 
					chref="${baseaction}/${parentorgid}/add.do" id="suborgadd"></shiro:hasPermission>
				</li>
				<li><input type="file" name="file" id="impfile" width="80%" style="display:none" /></li>
				<li><shiro:hasPermission name="systeminfo:organization:suborganization:create"><input class="button-mid white" type="button" value="导入" id="suborgexport" chref="${baseaction}/${parentorgid}/import.do"></shiro:hasPermission></li>
				<li><shiro:hasPermission name="systeminfo:organization:suborganization:delete"><input class="button-mid white" type="button" value="删除" id="del" chref=""></shiro:hasPermission></li>
			</ul>
		</div>
	</div>
</form:form>
	
<script type="text/javascript">
	$(function(){
		$("#del").click(function(){
			  var selectedData = [];
			  var selectRow = $("input[name='rowcheck']:checked");
			  if(selectRow.length === 0){
				  layer.open({content:"没有选择相关内容"});
				  return false;
			  }
			  layer.confirm('确定要删除所选择记录内容吗?', {
					btn : [ '是', '否' ]
				}, function() {
					selectRow.each(function() {
						selectedData.push(this.value);
					});
					$('#content2').load("${baseaction }/delselected.do",{rowcheck:selectedData});
				}, function() {
				});
		});
		$("#dzdiv").citySelect({
			prov:"${entity.org.provinceid}",
			city:"${entity.org.cityid}",
			dist:"${entity.org.countyid}",
			street:"${entity.org.townid}",
			defaultprov:"${entity.org.provinceid}",
			defaultcity:"${entity.org.cityid}",
		    defaultdist:"${entity.org.countyid}",
		    defaultstreet:"${entity.org.townid}",
			nodata:"hidden",
			showstreet:false
		});
		$("#org.townid").css("display","none");
		if($( "#suborgadd" ) != null){
			   $( "#suborgadd" ).on( "click", function() {
				   var h = $(this).attr("chref");
				   $("#editformdiv").html();
				   $("#editformdiv").load(h,function(){
					   $( "#suborgdialog" ).dialog("open");
				   });
			 	}); 
		   }
		//$("#suborgexport").on("click",function(){
		//	return  $("#impfile").click();
		//});
		$("#suborgexport").click(function(){
			debugger;
			var url="${baseaction}/redirectToImportEc.do";
			$("#content2").load(url);
		});
		$("#impform").ajaxForm({
			target : "#content2"
		});
		
		$("#impfile").on("change", function(e){
			$("#impform").submit();
		});
	});
	
</script>
