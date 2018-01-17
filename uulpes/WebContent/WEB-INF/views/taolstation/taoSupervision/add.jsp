<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="dialog-form1" title="编辑">
	<form:form id="editForm" method="post" commandName="taoSupervision"
		action="/pes/taolstation/taoSupervision/save.do">
		<form:hidden path="id" />

		
		
		
		
		<div class="filterContent">
			<ul>
				
				<li><label class="name04">接待者</label> <form:select
						path="teacherid" class="input_160" items="${teachers}" itemLabel="xm" itemValue="id" >
					</form:select>
				</li>
				<li><label class="name04">咨询方式</label> <form:select class="input_160"
						path="type"
						items="${consultationModels}" itemLabel="name" itemValue="id"></form:select>
				</li>
			</ul>
		</div>
		<div class="filterContent">
			<ul>
				
				<li><form:label path="starttime"
						class="name04">开始时间</form:label> <form:input
						path="starttime" class="input_160 date" />
				</li>
				<li><form:label path="endtime"
						class="name04">结束时间</form:label> <form:input
						path="endtime" class="input_160 date" />
				</li>
			</ul>
		</div>
		
		


		<form:textarea path="content" id="content" cols="80" rows="10"></form:textarea>
	</form:form>
</div>
<script type="text/javascript">
	
	$("#dialog-form1").dialog({
		appendTo : "#editDiv",
		autoOpen : false,
		
		height : 500,
		width : 900,
		modal: false,
		buttons : {
			"保存" : function() {
				$("#dialog-form1").dialog("close");
				$("#editForm").ajaxSubmit({
					target : "#list",
					data : {
						
						'teacherid1' : jQuery('#teacherid1').val(),
						'type1' : jQuery('#type1').val(),
						
						'starttime1' : jQuery('#starttime1').val(),
						'endtime1' : jQuery('#endtime1').val(),
					},
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
		target : "#list"
	});

	$(".date").datetimepicker({
		dateFormat : 'yy-mm-dd', //更改时间显示模式
		showAnim : "slide", //显示日历的效果slide、fadeIn、show等
		changeMonth : true, //是否显示月份的下拉菜单，默认为false
		changeYear : true, //是否显示年份的下拉菜单，默认为false
		showWeek : true, //是否显示星期,默认为false
	//	showButtonPanel : true, //是否显示取消按钮，并含有today按钮，默认为false
	//	closeText : 'close', //设置关闭按钮的值
	//yearRange:'2010:2012',	//显示可供选择的年份
	});
	
	$("textarea#content").ckeditor();
	
</script>