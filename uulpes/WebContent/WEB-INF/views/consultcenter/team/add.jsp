<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style> 
</style> 
<c:set var="baseaction" value="${ctx }" />
<div id="dialog-form1" title="编辑" style="height:500px; overflow:auto">
	<form:form id="editForm" method="post" commandName="teamPage"
		action="${baseaction}/consultcenter/team/save.do">
		<form:hidden path="team.id" />
		<div class="filterContent">
			<ul>
				<li><label class="name04">团队类型</label> <form:select path="team.teamtype" class="input_160"
						id="teamtypeA" onchange="showTable()"
						dfv="${teamPage.team.teamtype}">
						<form:options items="${teamTypeEnum }" itemValue="value"
							itemLabel="info"></form:options>
					</form:select></li>
				<li><form:label path="team.name" class="name04">团队名称</form:label>
					<form:input path="team.name" class="input_160" /></li>
			</ul>
		</div>

		<div class="filterContent">
			<ul>
				<input id="memberSize" type="hidden"
					value="${teamPage.team.personnum}">
				<li><form:label path="team.personnum" class="name04">人数</form:label>
					<form:input path="team.personnum" class="input_160" /></li>
				<li><form:label path="team.createtime" class="name04">创建时间</form:label>
					<form:input path="team.createtime" class="input_160 date" /></li>
			</ul>
		</div>
		<div id="person">
			<table id="tableS" class="table table-hover">
				<tr class="titleBg">
					<td width="10%">真实姓名</td>
					<td width="20%">身份证号</td>
					<td width="10%">性别</td>
					<td width="20%">年级名称</td>
					<td width="20%">班级名称</td>
					<td width="20%">操作</td>
				</tr>
				<c:forEach var="item" items="${studentList}" varStatus="status">
					<tr>
						<input name="teamPersons[${status.index}].memberid" type="hidden"
							value="${item.id}">
						<td>${item.xm}</td>
						<td>${item.sfzjh}</td>
						<td>${item.xbm}</td>
						<td>${item.njmc}</td>
						<td>${item.bjmch}</td>
						<td><span class="header02"> <input
								class="button-small del_1 " type="button" value="删除"> </span>
						</td>
					</tr>
				</c:forEach>
			</table>
			<table id="tableT" class="table table-hover">
				<tr class="titleBg" >
					<td width="20%">真实姓名</td>
					<td width="18%">身份证号</td>
					<td width="20%">性别</td>
					<td width="20%">操作</td>
				</tr>
				<c:forEach var="item" items="${teacherList}" varStatus="status">
					<tr>
						<input name="teamPersons[${status.index}].memberid" type="hidden"
							value="${item.id}">
						<td>${item.xm}</td>
						<td>${item.sfzjh}</td>
						<td>${item.xbm}</td>
						<td><span class="header02"> <input
								class="button-small del_1 " type="button" value="删除"> </span></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</form:form>
	<div id="studentQ">
		<form:form id="studentform"
			action="${baseaction}/consultcenter/team/selectStudent.do"
			method="post" modelAttribute="student">
			<%-- <c:if test="${fn:length(xdlist) > 1 }">
				<div class="filterContent">
					<ul>
						<li><label class="name04">学 段</label> <form:select path="xd"
								cssClass="dropBox01" items="${xdlist}" itemValue="id"
								itemLabel="info"></form:select>
						</li>
						<li><label class="name04">年 级</label> <form:select path="nj"
								cssClass="dropBox01"></form:select>
						</li>
						<li><label class="name04">班 级</label> <form:select path="bh"
								cssClass="dropBox01" />
						</li>
						<li><input id="searchsuborgs" class="button-mid"
							type="submit" value="搜索">
						</li>
					</ul>
				</div>
			</c:if>
			<c:if test="${fn:length(xdlist) == 1 }">
				<div class="filterContent">
					<ul>
						<form:hidden path="xd" />
						<li><label class="name04">年 级</label> <form:select path="nj"
								cssClass="dropBox01" items="${njlist}" itemValue="nj"
								itemLabel="njmc"></form:select>
						</li>
						<li><label class="name04">班 级</label> <form:select path="bh"
								cssClass="dropBox01" />
						</li>
						<li><input id="searchsuborgs" class="button-mid "
							type="submit" value="搜索">
						</li>
					</ul>
				</div>
			</c:if> --%>
			
	<c:if test="${fn:length(xdlist) > 1 }">
		<div class="filterContent">
			<ul>
				<li><label class="name04">学  段</label> <form:select path="xd" cssClass="input_160">
				<option value=''>请选择</option>
				<form:options  items="${xdlist}"  itemValue="id"  itemLabel="info"/>
				</form:select></li>
				
				<li><label class="name04">年  级</label> <form:select path="nj" class="input_160">
			
				</form:select></li>
				<li><label class="name04">班  级</label><form:select path="bh" class="input_160">
				
				</form:select></li>
				<input id="searchsuborgs" class="button-mid "
							type="submit" value="搜索">
						
			</ul>
		</div>
	</c:if>
	<c:if test="${fn:length(xdlist) == 1 }">
		<div class="filterContent">
			<ul>
				<li><label class="name04">学  段</label> <form:select path="xd" cssClass="input_160">
				<option value=''>请选择</option>
				<form:options  items="${xdlist}"  itemValue="id"  itemLabel="info"/>
				</form:select></li>
				
				<li><label class="name04">年  级</label> <form:select path="nj" class="input_160">
				<option value=''>请选择</option>
				<form:options  items="${njlist}"  itemValue="gradeid"  itemLabel="njmc"/>
				</form:select></li>
				<li><label class="name04">班  级</label><form:select path="bh" class="input_160">
				
				</form:select></li>
				<li><input id="searchsuborgs" class="button-mid "
							type="submit" value="搜索">
						</li>
			</ul>
		</div>
	</c:if>
			
			
			
			
			
			
		</form:form>
	</div>
	<div id="teacherQ">
		<form:form id="teacherform"
			action="${baseaction}/consultcenter/team/selectTeacher.do"
			method="post" modelAttribute="teacher">
			<div class="filterContent">
				<ul>
					<li><label class="name04">角色名称</label> <form:select
							path="roleId" cssClass="select_160">
							<form:option value="">----</form:option>
							<form:options items="${rolelist }" itemValue="id"
								itemLabel="roleName" />
						</form:select>
					</li>
					<li><input id="searchsuborgs" class="button-mid "
						type="submit" value="搜索">
					</li>
					<li><input id="clearform" class="button-mid "
						type="button" value="重置">
					</li>

				</ul>
			</div>
		</form:form>
	</div>
	<div id="select"></div>
	
</div>
	<script type="text/javascript">
		jQuery('.del_1').bind('click', function() {
			jQuery(this).parent().parent().parent().remove();
		});
		$("#studentform").ajaxForm({
			target : "#select"
		});
		$("#teacherform").ajaxForm({
			target : "#select"
		});
		/* $(function() {
			$(".dropBox01").chosen({});
			$("#xd")
					.change(
							function() {
								$("#nj").empty();
								var xd = $(this).val();
								$
										.ajax({
											url : "${baseaction}/systeminfo/sys/user/student/getGrades.do",
											data : {
												"xd" : xd
											},
											dataType : "json",
											type : "POST",
											success : function(data) {
												$("#nj").chosen("destroy");
												$
														.each(
																data,
																function(i, k) {
																	$("#nj")
																			.append(
																					"<option value='" + k.nj + "'>"
																							+ k.njmc
																							+ "</option>");
																});
												$("#nj").chosen({})
											}

										});
							});
			$("#nj")
					.change(
							function() {
								$("#bh").empty();
								var nj = $(this).val();
								var xd = $("#xd").val();
								$
										.ajax({
											url : "${baseaction}/systeminfo/sys/user/student/getClasses.do",
											data : {
												"nj" : nj,
												"xd" : xd
											},
											dataType : "json",
											type : "POST",
											success : function(data) {
												$("#bh").chosen("destroy");
												$
														.each(
																data,
																function(i, k) {
																	$("#bh")
																			.append(
																					"<option value='" + k.bh + "'>"
																							+ k.bjmc
																							+ "</option>");
																});
												$("#bh").chosen({})
											}

										});
							});
		}); */
		
		
		
		
		
		$("#xd").change(function(){
			$("#nj").empty();
			var xd = $(this).val();
			$.ajax({
				url:"${baseaction}/systeminfo/sys/user/student/getGrades.do",
				data:{
					  "xd":xd	 
					},
				dataType:"json",
				type:"POST",
				success:function(data){
					$("#nj").append("<option value=''>请选择</option>");
					$.each(data,function(i,k){
						$("#nj").append("<option value='" + k.gradeid + "'>" + k.njmc + "</option>");
					});
				}
				
			});
		});
		$("#nj").change(function(){
			$("#bh").empty();
			var nj = $(this).val();
			
			var xd = $("#xd").val();
			$.ajax({
				url:"${baseaction}/systeminfo/sys/user/student/getClasses.do",
				data:{"nj":nj,
					  "xd":xd	 
					},
				dataType:"json",
				type:"POST",
				success:function(data){
					
					$("#bh").append("<option value=''>请选择</option>");
					$.each(data,function(i,k){
						$("#bh").append("<option value='" + k.bh + "'>" + k.bjmc + "</option>");
					});
				}
				
			});
		});
	
	</script>





<script type="text/javascript">
	$("#dialog-form1").dialog({
		appendTo : "#editDiv",
		autoOpen : false,
		modal : true,
		height : 500,
		width : 800,
		buttons : {
			"保存" : function() {
				$("#dialog-form1").dialog("close");
				$("#editForm").ajaxSubmit({
					target : "#list1",
					success : function() {
						$("#dialog-form1").dialog("close");
						layer.open({content:"保存成功!"});
					},
					error : function() {
						layer.open({content:"保存失败"});
					}
				});
				return false;
			},
			"取消" : function() {
				$("#dialog-form1").dialog("close");
			}
		},
	});

	$("#editForm").ajaxForm({
		target : "#list1"
	});

	$(".date").datepicker({
		dateFormat : 'yy-mm-dd', //更改时间显示模式
		showAnim : "slide", //显示日历的效果slide、fadeIn、show等
		changeMonth : true, //是否显示月份的下拉菜单，默认为false
		changeYear : true, //是否显示年份的下拉菜单，默认为false
		showWeek : true, //是否显示星期,默认为false
		showButtonPanel : true, //是否显示取消按钮，并含有today按钮，默认为false
		showTime : true,
		closeText : 'close', //设置关闭按钮的值
	});

	function showTable() {
		var teamtype = $("#teamtypeA").val();
		if (teamtype == 1) {
			$("#tableT").attr("style", "");
			$("#tableS").attr("style", "display:none");
			$("#studentQ").attr("style", "display:none");
			$("#teacherQ").attr("style", "");
		} else {
			$("#tableS").attr("style", "");
			$("#tableT").attr("style", "display:none");
			$("#studentQ").attr("style", "");
			$("#teacherQ").attr("style", "display:none");
		}
	}
	$(document).ready(function() {
		var teamtype = $("#teamtypeA").attr("dfv");
		if (teamtype === undefined) {
		} else {
			var optionss = $("#teamtypeA").find("option:selected");
			$("#teamtypeA").find("option").remove();
			$("#teamtypeA").append(optionss);
		}
		showTable();
	});
</script>