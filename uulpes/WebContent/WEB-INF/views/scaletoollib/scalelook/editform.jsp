<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/assessmentcenter/scalemanager" />
<c:choose>
	<c:when test="${op eq '新增'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="${baseaction }/createnorm.do" />
	</c:when>
	<c:when test="${op eq '导入'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="${baseaction }/${scaleid }/importscalenorm.do" />
	</c:when>
	<c:when test="${op eq '查看'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="" />
	</c:when>
	<c:when test="${op eq '修改'}">
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page"
			value="${baseaction }/${entity.id}/update.do" />
	</c:when>
	<c:otherwise>
		<c:remove var="formaction" />
		<c:set var="formaction" scope="page" value="${baseaction }/create.do" />
	</c:otherwise>
</c:choose>
<div id="editdialog" title="${empty op ? '导入' : op}常模">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity" enctype="multipart/form-data">
		<c:choose>
	<c:when test="${op eq '导入'}">
	<table>
	  <tr>
	    <td>量表名称 </td>
	    <td><input type="text" ></td>
	    <td rowspan="5" width="180" style="vertical-align: top;">导入提醒：1.只有管理员有权限导入本级单位常模。2.修订人：多个修订人以空格或逗号分割 3.时间格式：2010-01-01,2015-03-04</td>
	  </tr>
	  <tr>
	    <td>常模名称 </td>
	    <td><input type="text" id="normname" name="normname"></td>
	  </tr>
	  <tr>
	    <td>修订次数 </td>
	    <td><input type="text" id="editnum" name="editnum"></td>
	  </tr>
	   <tr>
	    <td>修订时间 </td>
	    <td><input type="text" id="edittime" name="edittime" ></td>
	  </tr>
	   <tr>
	    <td>修 订 人 </td>
	    <td><input type="text" id="editer" name="editer" ></td>
	  </tr>
	  <tr>
	  <td>选择常模文件</td>
	    <td colspan="2">
	   <input 	class="input_300" type="file" name="file">
	    </td>
	  </tr>
	</table>
	</c:when>
	<c:when test="${op eq '新增'}">
	<table>
	  <tr>
	    <td>量表名称 </td>
	    <td>${scalename }</td>
	    <td rowspan="5" width="180" style="vertical-align: top;">创建常模提醒：1.常模通常每隔三年创建一次。2.只能创建本级单位常模。</td>
	  </tr>
	  <tr>
	    <td>常模名称 </td>
	    <td><input type="text" id="normname" name="normname"></td>
	  </tr>
	  <tr>
	    <td>样本开始时间</td>
	    <td><input type="text" id="starttime" name="starttime"></td>
	  </tr>
	   <tr>
	    <td>样本结束时间</td>
	    <td><input type="text" id="endtime" name="endtime"></td>
	  </tr>
	  <tr>
	    <td>常模说明</td>
	    <td><input type="text" id="description"  name="description"></td>
	  </tr>

	</table>
	</c:when>
	</c:choose>
	<input type="hidden" name ="scaleid" value="${scaleid }">
	</form:form>
</div>
<script type="text/javascript">
	$(function(){
		var buttonsOps = {};
		<c:choose>
		<c:when test="${op eq '新增'}">
		buttonsOps = {
				"创建": function(){
	        		if (!$("#editForm").validationEngine('validate'))
	    				return false;
	        		debugger;
	        		var normname=$("#normname").val();
	        		var description=$("#description").val();
	        		var starttime=$("#starttime").val();
	        		var endtime=$("#endtime").val();
	        		var url = "../../assessmentcenter/scalemanager/createnorm.do";
	    			$.ajax({
	    		      type: "POST",
	    		      data:{
	    		    	scaleid:${scaleid},
	    		    	normname:normname,
	    		    	description:description,
	    		    	starttime:starttime,
	    		    	endtime:endtime
	    		      },
	    		      url: url,
	    		      success: function(data){
	    		    	  debugger;
	    		    	  if(data=="success"){
	    		    		  $("#editdialog").dialog("close");
	    		    		  layer.open({content:"成功创建常模!"});
	    		    	  }
	    		    	  else
	    		    		  layer.open({content:"创建常模失败"});
	    		      },
	    		      error:function(){
	    		    	  layer.open({content:"创建常模失败"});
	    		      }
	    		   });

	    			$("#editForm").clearForm();
	    			return false;
	        	},
	            "取消": function() {
	            	$( "#editdialog" ).dialog( "close" );
	            }
		};
		</c:when>
		<c:when test="${empty op || op eq '导入' || op eq '修改'}">
		buttonsOps = {
				"导入": function(){
	        		if (!$("#editForm").validationEngine('validate'))
	    				return false;
	    			$("#editForm").ajaxSubmit({
	    				target : "#content2",
	    				success : function() {
	    					$("#editdialog").dialog("close");
	    					layer.open({content:"成功导入常模!"});
	    				},
	    				error : function() {
	    					layer.open({content:"成功导入常模!"});
	    				}
	    			});
	    			$("#editForm").clearForm();
	    			return false;
	        	},
	            "取消": function() {
	            	$( "#editdialog" ).dialog( "close" );
	            }
		};
		</c:when>
		<c:when test="${op eq '查看'}">
			buttonsOps = {
				"返回" : function() {
					$("#editdialog").dialog("close");
				}
			};
		</c:when>
		</c:choose>
		$("#editdialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 350,
			width : 520,
			buttons : buttonsOps
		});
		
		
		$("#starttime").datepicker(
				 { //绑定开始日期
					 dateFormat : 'yy-mm-dd',
					 changeMonth : true, //显示下拉列表月份
					 changeYear : true, //显示下拉列表年份
					 firstDay : "1" //设置开始为1号
					
				});
	
		$("#endtime").datepicker(
				 { //绑定开始日期
					 dateFormat : 'yy-mm-dd',
					 changeMonth : true, //显示下拉列表月份
					 changeYear : true, //显示下拉列表年份
					 firstDay : "1" //设置开始为1号
				});
	});
	
</script>


