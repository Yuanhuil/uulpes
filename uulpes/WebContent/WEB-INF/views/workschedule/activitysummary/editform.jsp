<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style>
.activityplan-dialog-ul {
    margin-left: 38px;
}
</style>
<c:set var="formaction" value="" scope="page" />
<c:set var="baseaction" value="${ctx }/workschedule/activitysummary" />
<c:choose>
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
<div id="editdialog" title="${empty op ? '新增' : op}活动总结">
	<form:form action="${formaction}" method="post" id="editForm"
		commandName="entity" enctype="multipart/form-data">
		<form:hidden path="iniContent"/>
        <div>
            <div id="editTitle" class="notice-title">
                <ul class="activityplan-dialog-ul">
                    <li class="activityplan-dialog-li"><label>学年</label>
                        <form:select id="select2_sy" path="schoolyear"  data-placeholder="选择学年" cssClass="input_160 validate[required]" >
                        <form:option value="">请选择</form:option>
                        <form:options items="${schoolyears }"/></form:select>
                    </li>
                    <li class="activityplan-dialog-li"><label>学期</label>
                        <form:select  id="select2_term" path="term" data-placeholder="选择学期" cssClass="input_160 validate[required]" >
                        <form:option value="">请选择</form:option>
                        <form:options items="${schoolterm }" itemValue="id" itemLabel="name"/></form:select>
                    </li>
                    <li class="activityplan-dialog-li"><label>活动类别</label>
                        <form:select id="select2_act" path="activitycatalog" data-placeholder="选择活动类别" cssClass="input_160 validate[required]">
                        <form:option value="">请选择</form:option>
                        <form:options items="${plancatalog }" itemValue="id" itemLabel="name"/></form:select>
                    </li>
                </ul>
                <ul class="activityplan-dialog-ul">
                    <li class="activityplan-dialog-li"><label>总结标题</label>
                        <form:input id="select2_title" path="title" cssClass="input_160" ></form:input>
                    </li>
                    <li class="activityplan-dialog-li"><label>活动类型</label>
                        <form:select  id="select2_type" path="activitytype" data-placeholder="选择活动类型" cssClass="input_160 validate[required]">
                        <form:option value="">请选择</form:option>
                        <form:options items="${plantype }" itemValue="id" itemLabel="name"/>
                        </form:select>
                    </li>
                </ul>
            <div>
		      <form:textarea id="select2_content" path="content" cols="80" rows="8"></form:textarea>
            </div>
        </div>
    </div>
	</form:form>
</div>
<script type="text/javascript">
	$(function(){
		$("#select2_act").change(function(){
			var p1=$(this).children("option:selected").val();
			$.ajax({
				url:'${ctx }/workschedule/activitysummary/getType.do',
				data:{'catid':p1},
				dataType:'json',
				success:function(data){
					$("#select2_type option").remove();
					$("#select2_type").append("<option value=''>请选择</option>");
					$.each(data,function(index,item){
						$("#select2_type").append("<option value='"+ item.id+ "'>" + item.name + "</option>");
					});
				},
				error:function(){
					console.log("发生错误");
				}

			});
		});

		$("textarea#select2_content").ckeditor({
		    height: '300px'
		});
		var buttonsOps = {};
		<c:choose>
		<c:when test="${empty op || op eq '新增' || op eq '修改'}">
		buttonsOps = {
				"保存": function(){
	        		if (!$("#editForm").validationEngine('validate'))
	    				return false;
	    			$("#editForm").ajaxSubmit({
	    				target : "#tablelist",
	    				success : function() {
	    					$("#editdialog").dialog("close");
	    					layer.open({content:"保存成功!"});
	    				},
	    				error : function() {
	    					layer.open({content:"保存失败"});
	    				}
	    			});
	    			$("#editForm").clearForm();
	    			return false;
	        	},
	        	"还原初始总结":function(){
	        		var c = $("#iniContent").val();
	        		CKEDITOR.instances['select2_content'].setData(c); 
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
			modal : false,
			height : 700,
			width : 900,
			position: { my: "top", at: "top", of: "#topHeader" },
			buttons : buttonsOps
		});
	});

</script>


