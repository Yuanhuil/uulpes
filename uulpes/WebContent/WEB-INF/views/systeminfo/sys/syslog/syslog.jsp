<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div id="filterContent">
		<%@include  file="syslogfileter.jsp" %>
	</div>
		<div id="tableContent" class="tableContent">
			<%@include  file="syslogtable.jsp" %>
		</div>

<script type="text/javascript">
$("#syslogFilterForm").ajaxForm({
	target : "#tableContent"
});
	 $(function() {  
	 $( "#datepicker" ).datepicker();
	 $( "#datepickers" ).datepicker();
	 });
	
	 function searchLog(){
	 $.ajax({
	 type: "POST",
	 url: "../sys/searchSyslogs.do",
	 data: {
	 "orglevelid":$("#organizationlevel").val(),
	 "orgid":$("#organizations").val(),
	 "roleid":$("#roles").val(),
	 "menuid":$("#resouces").val(),
	 "permissionid":$("#permission").val(),
	 "starttime":$("#starttime").val(),
	 "endtime":$("#endtime").val()
	 },
	 success: function(msg){
	 if(msg!="none"){
	 var objArray = jQuery.parseJSON(msg);
	 for(var i=0;i<objArray.length;i++){
	
	 }
	 }else{
	 layer.open({content:"查询为空"});
	 }
	 },
	 error: function()
	 {  	
	 layer.open({content:"调用出现错误，删除失败"});
	 }
	 }); 
	 }

	 function downloadExcel(){
	 $.ajax({
	 type: "POST",
	 url: "../sys/downloadSyslogs.do",
	 data: {
	 "orglevelid":$("#organizationlevel").val(),
	 "orgid":$("#organizations").val(),
	 "roleid":$("#roles").val(),
	 "menuid":$("#resouces").val(),
	 "permissionid":$("#permission").val(),
	 "starttime":$("#starttime").val(),
	 "endtime":$("#endtime").val()
	 },
	 success: function(msg){
	 if(msg!="none"){
	 var objArray = jQuery.parseJSON(msg);
	 for(var i=0;i<objArray.length;i++){
	
	 }
	 }else{
	 layer.open({content:"查询为空"});
	 }
	 },
	 error: function()
	 {  	
	 layer.open({content:"调用出现错误，删除失败"});
	 }
	 }); 
	 }
</script>