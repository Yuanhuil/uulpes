<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div id="dialog-form1" title="编辑">
	<form:form id="editForm" method="post" commandName="record"
		action="/pes/taolstation/taoRecord/save.do">
		<form:hidden path="id" />

		<div class="filterContent">
			<ul>
				<li><label class="name04">咨询类型</label> <form:select class="input_160"
						path="consultationtypeid">
						<form:options items="${consultTypes }" itemValue="id"
							itemLabel="name"></form:options>
					</form:select>
				</li>
				<li><label class="name04">咨询对象</label> <form:select class="input_160"
						path="objtype" items="${typeEnum}"
						itemLabel="info" itemValue="value">
					</form:select>
				</li>
				<li><label class="name04">咨询方式</label> <form:select class="input_160"
						path="consultationmodeid"
						items="${consultationModels}" itemLabel="name" itemValue="id"></form:select>
				</li>
			</ul>
		</div>
		
		
		
		<div class="filterContent">
			<ul>
				<li><form:label path="username"
						class="name04">姓名</form:label> <form:input
						path="username" class="input_160" />
				</li>
				<li><label class="name04">咨询员</label> <form:select
						path="teacherid" class="input_160" items="${teachers}" itemLabel="xm" itemValue="id" >
					</form:select>
				</li>
				<li><form:label path="data"
						class="name04">时间</form:label> <form:input
						path="data" class="input_160 date" />
				</li>
			</ul>
		</div>
		
		<div class="filterContent">
			<ul>
				<li><form:label path="xh"
						class="name04">学号</form:label> <form:input
						path="xh" class="input_160" />
				</li>
				
				<li><form:label path="sfzjh"
						class="name04">身份证号</form:label> <form:input
						path="sfzjh" class="input_160" />
				</li>
			</ul>
		</div>


		<form:textarea path="describes" id="content" cols="80" rows="10"></form:textarea>
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
						'consultationtypeid1' : jQuery('#consultationtypeid').val(),
						'consultationmodeid1' : jQuery('#consultationmodeid').val(),
						'teacherid1' : jQuery('#teacherid').val(),
						'username1' : jQuery('#username').val(),
						'objtype1' : jQuery('#objtype').val(),
						'beginDate' : jQuery('#beginDate').val(),
						'endDate' : jQuery('#endDate').val(),
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

	$(".date").datepicker({
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