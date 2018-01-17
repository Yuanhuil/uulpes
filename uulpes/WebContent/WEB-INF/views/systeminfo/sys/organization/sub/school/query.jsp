<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/systeminfo/sys/organization/sub/school"/>
<form:form id="suborgform" action="${baseaction}/search.do" method="post"  modelAttribute="entity">
	<form:hidden path="org.orgType"/>
	<form:hidden path="org.orgLevel"/>
	<form:hidden path="org.parentId"/>
	<div class="filterContent">
		<ul>
			<li><label class="name03">办学类型</label> 
			<form:select path="xxbxlxm" cssClass="input_140">
				<form:option value="">请选择</form:option>
				<form:options items="${bxlx}" itemValue="id" itemLabel="name"/>
			</form:select></li>
			<li><label class="name03">学校办别</label> 
			<form:select path="xxbbm" cssClass="input_140">
				<form:option value="">请选择</form:option>
				<form:options items="${xxbb}" itemValue="id" itemLabel="name"/>
			</form:select></li>
			<li><label>学校单位层次</label> 
			<form:select path="xxdwcc" cssClass="input_140">
				<form:option value="">请选择</form:option>
				<form:options items="${xxdwcc}" itemValue="id" itemLabel="name"/>
			</form:select></li>
		</ul>
	</div>	
	
	<div class="buttonContent">
		<ul>
			<li><form:input placeholder="关键字" cssClass="input_160"
				path="xxmc" /> <input id="searchsuborgs" class="button-mid blue" type="submit"
				value="搜索"></li>
			<li></li>
</form:form>
<form id="impform" action="${baseaction}/${parentorgid}/import.do"   method="post" enctype="multipart/form-data">
			<li>
			<shiro:hasPermission name="systeminfo:organization:subschool:create">
			<input class="button-mid blue" type="button" value="新增"
			chref="${baseaction}/${parentorgid}/add.do" id="suborgadd">
			</shiro:hasPermission>
			<input type="file" name="file" id="impfile" width="80%" style="display:none" />
			<shiro:hasPermission name="systeminfo:organization:subschool:create">
			<input class="button-mid blue" type="button" value="导入" id="suborgexport" >
			</shiro:hasPermission>
			<shiro:hasPermission name="systeminfo:organization:subschool:delete">
			<input class="button-mid blue" type="button" value="删除" id="del" chref="">  
			</shiro:hasPermission>
			<!-- <input class="button-mid blue" type="button" value="下载" id="suborgdown"></li> -->
		</ul>
	</div>
</form>
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

        if($( "#suborgadd" ) != null){
            $( "#suborgadd" ).on( "click", function() {
                $.ajax({
                    type: "POST",
                    url: "${baseaction }/count.do",
                    data: {
                        orgType:${user_org.orgType},
                        parentorgid:${user_org.id}
                    },
                    dataType: "json",
                    success: function(data){
                        if(data == 0){
                            layer.open({content:"已超过学校数量限制"});
                        }else if(data == 1) {
                            var h = $("#suborgadd").attr("chref");
                            $("#editformdiv").html();
                            $("#editformdiv").load(h,function(){
                                $( "#suborgdialog" ).dialog("open");
                            });
                        }
                    }
                });
            });
        }
		//$("#suborgexport").on("click",function(){
			//return  $("#impfile").click();
		//});
		$("#suborgexport").click(function(){
			debugger;
			var url="${baseaction}/redirectToImportSchool.do";
			$("#content2").load(url);
		});
		$("#impform").ajaxForm({
			target : "#content2"
		});
		
		$("#impfile").on("change", function(e){
			debugger;
			if($("#impfile").val()!==""){
				$("#impform").ajaxSubmit(
						{success: function(v){
							$("#impfile").val("");
							layer.open({content:'导入数据成功！'});
						}
		        });
			}
		});
	});
	
</script>
