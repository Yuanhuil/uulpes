<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="dialog-form1" title="编辑">
    <form:form id="editForm" action="/pes/consultcenter/warningInterveneS/save.do" method="post"
        commandName="warningInterveneS">
        <form:hidden path="id" />
        <form:hidden path="schoolId" />
        <form:hidden path="status" />
        <input type="text" hidden="hidden" id="oldstatus" value="${oldstatus}">
        <div id="warningbasicinfo">
            <div class="filterContent">
                <ul>
                    <li>
                        <label class="name04">姓名</label>
                        <form:input path="name" class="input_160" />
                    </li>
                    <li>
                        <label class="name04">性别</label>
                        <form:select path="sex" class="input_160" items="${sexEnum}" itemLabel="info" itemValue="id"></form:select>
                    </li>
                    <li>
                        <label class="name04">身份证号</label>
                        <form:input path="cardid" class="input_160" />
                    </li>
                </ul>
            </div>
            <div class="filterContent">
                <ul>
                    <li>
                        <label class="name04">预警时间</label>
                        <form:input path="warningTime" class="input_160" readonly="true" />
                    </li>
                    <li>
                        <label class="name04">预警级别</label>
                        <form:select path="level" class="input_160" items="${warningLever}" itemLabel="name"
                            itemValue="state" onChange="showDisposeTypeOption(true)"></form:select>
                    </li>
                    <li>
                        <label class="name04">处置方式</label>
                        <form:select path="disposeType" class="input_160" defaultValue="${warningInterveneS.disposeType}">
                        </form:select>
                    </li>
                </ul>
            </div>
            <div class="filterContent">
                <ul>
                    <li>
                        <label class="name04">处理角色</label>
                        <form:select id="roleId" path="disposeRoleId" class="input_160" onChange="selectTeacher()">
                            <form:option value="">----</form:option>
                            <form:option value="21">心理咨询员</form:option>
                        </form:select>
                    </li>
                    <li>
                        <label class="name04">处理者</label>
                        <form:select path="disposePerson" class="input_160">
                            <form:option value="">请选择</form:option>
                            <form:options items="${disposePersonList}" itemValue="xm" itemLabel="xm" />
                        </form:select>
                    </li>
                </ul>
            </div>
            <div class="filterContent" id="intervene_show" style="display: ${intervene_show};">
                <ul>
                    <li>
                        <label class="name04">干预时间</label>
                        <form:input path="interveneTime" class="input_160" />
                    </li>
                    <li>
                        <label class="name04">干预方式</label>
                        <form:select path="type" class="input_160">
                            <form:options items="${interveneType}" itemLabel="name" itemValue="state"></form:options>
                        </form:select>
                    </li>
                    <li>
                        <label class="name04">干预结果</label>
                        <form:select path="result" class="input_160">
                            <form:options items="${interveneResult}" itemLabel="name" itemValue="state"></form:options>
                        </form:select>
                    </li>
                </ul>
            </div>
        </div>
        <c:if test="${fn:length(xdlist) > 1 }">
            <div class="filterContent">
                <ul>
                    <li>
                        <label class="name04">学 段</label>
                        <form:select path="groupid" cssClass="input_160">
                            <form:option value="0">请选择</form:option>
                            <form:options items="${xdlist}" itemValue="id" itemLabel="info" />
                        </form:select>
                    </li>
                    <form:hidden path="grade" />
                    <form:hidden path="className" />
                    <li>
                        <label class="name04">年 级</label>
                        <select id="nj" class="input_160">
                            <option value="">${warningInterveneS.grade}</option>
                        </select>
                    </li>
                    <li>
                        <label class="name04">班 级</label>
                        <select id="bh" class="input_160">
                            <option value="0">${warningInterveneS.className}</option>
                        </select>
                    </li>
                </ul>
            </div>
            <div class="buttonContent" id="selectStudent">
                <input type="button" onclick="selectStudent()" value="查询" class="button-mid blue" />
            </div>
        </c:if>
        <c:if test="${fn:length(xdlist) == 1 }">
            <div class="filterContent">
                <ul>
                    <form:hidden path="groupid" />
                    <form:hidden path="grade" />
                    <form:hidden path="className" />
                    <li>
                        <label class="name04">年 级</label>
                        <select id="nj" class="input_160">
                            <option value="">${warningInterveneS.grade}</option>
                        </select>
                    </li>
                    <li>
                        <label class="name04">班 级</label>
                        <select id="bh" class="input_160">
                            <option value="">${warningInterveneS.className}</option>
                        </select>
                    </li>
                </ul>
            </div>
        </c:if>
        <div class="filterContent" id="showStudent" style.display="none">
            <table id="studenthide">
                <thead>
                    <th style="text-align: center">学号</th>
                    <th style="text-align: center">姓名</th>
                    <th style="text-align: center">性别</th>
                    <th style="text-align: center">年级</th>
                    <th style="text-align: center">班级</th>
                    <th style="text-align: center">操作</th>
                </thead>
                <tbody id="studentMsg" style="text-align: center">
                </tbody>
            </table>
        </div>
        <div style="display: ${intervene_show=='none'?'block':'none'};">
            特别记录</br>
            <form:textarea path="record" cols="80" rows="10"></form:textarea>
        </div>
        <div style="display: ${intervene_show};">
            干预过程</br>
            <form:textarea path="process" cols="80" rows="10"></form:textarea>
        </div>
    </form:form>
</div>
<script type="text/javascript">
$(function (){
    $("#studenthide").hide();
});

$("#warningTime").datepicker({
	dateFormat : 'yy-mm-dd', //更改时间显示模式
	showAnim : "slide", //显示日历的效果slide、fadeIn、show等
	changeMonth : true, //是否显示月份的下拉菜单，默认为false
	changeYear : true, //是否显示年份的下拉菜单，默认为false
	showWeek : true, //是否显示星期,默认为false
//	showButtonPanel : true, //是否显示取消按钮，并含有today按钮，默认为false
//	closeText : 'close', //设置关闭按钮的值
//yearRange:'2010:2012',	//显示可供选择的年份
});

function selectStudent(){
    var sid = 0;
    var xd = $("#groupid").val();
    var nj = $("#nj").val();
    var bjid = $("#bh").val();
    if(xd !=null && xd !=""&& xd != "0"){
            $.ajax({
            url : "/pes/systeminfo/sys/user/student/recordSearch.do",
            data : {
                "xd" : xd,
                "nj" : nj,
                "sbjid" : bjid,
                "id" : sid
            },
            dataType : "json",
            type : "POST",
            success : function(data) {
                if(data==null || data ==""){
                    $("#studenthide").hide();
                    alert("该查询条件没有学生信息")
                }else{
                    var msg = "";
                    for(var i=0;i<data.length;i++){
                        msg+="<tr>";
                        msg+="<td style='text-align: center'>"+data[i].xh+"</td>";
                        msg+="<td style='text-align: center'>"+data[i].xm+"</td>";
                        if(data[i].xbm==1){
                            msg+="<td style='text-align: center'>男</td>";
                        }else{
                            msg+="<td style='text-align: center'>女</td>";
                        }
                        msg+="<td style='text-align: center'>"+data[i].njmc+"</td>";
                        msg+="<td style='text-align: center'>"+data[i].bjmch+"</td>";
                        msg+="<td style='text-align: center'><input  class='button-small white edit' type='button' onclick='addStudent("+data[i].id+")' value='添加' /></td>";
                        msg+="</tr>";
                    }
                   $("#studenthide").show();
                   $("#studentMsg").html(msg);
                }
            },
            error : function(jqXHR, textStatus, errorThrown) {
                layer.open({
                    content : '错误: ' + jqXHR.responseText
                });
            }
        });
        }else{
            alert("请选择学段进行查询")
        }
}

function addStudent(x){
    var xd = 0;
    var nj = "";
    var bjid = 0;
    $.ajax({
        url : "/pes/systeminfo/sys/user/student/recordSearch.do",
        data : {
            "xd" : xd,
            "nj" : nj,
            "sbjid" : bjid,
            "id" : x
        },
        dataType : "json",
        type : "POST",
        success : function(data) {
            $("#name").val(data[0].xm);
            $("#cardid").val(data[0].sfzjh);
            if(data[0].xbm == 1){
                $("#sex").val(1);
            }else{
                $("#sex").val(0);
            }
            $("#warningbasicinfo").css("display","");
            $("#studenthide").hide();
        },
        error : function(jqXHR, textStatus, errorThrown) {
            layer.open({
                content : '错误: ' + jqXHR.responseText
            });
        }
    });
}


$("#interveneTime").datepicker({
	dateFormat : 'yy-mm-dd', //更改时间显示模式
	showAnim : "slide", //显示日历的效果slide、fadeIn、show等
	changeMonth : true, //是否显示月份的下拉菜单，默认为false
	changeYear : true, //是否显示年份的下拉菜单，默认为false
	showWeek : true, //是否显示星期,默认为false
//	showButtonPanel : true, //是否显示取消按钮，并含有today按钮，默认为false
//	closeText : 'close', //设置关闭按钮的值
//yearRange:'2010:2012',	//显示可供选择的年份
});
	$("#dialog-form1").dialog({
		appendTo : "#editDiv",
		autoOpen : false,
		modal : false,
		height : 700,
		width : 800,
		buttons : {
		<c:if test="${empty view || view eq 'false'}">
			"保存" : function() {
				$("#dialog-form1").dialog("close");
				$("#editForm").ajaxSubmit({
					target : "#list",
					data : {
						'name1' : jQuery('#name1').val(),
						'sex1' : jQuery('#sex1').val(),
						'level1' : jQuery('#level1').val(),
						'result1' : jQuery('#result1').val(),
						'type1' : jQuery('#type1').val(),
						'oldstatus':jQuery('#oldstatus').val(),
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
			</c:if>
			"取消" : function() {
				$("#dialog-form1").dialog("close");
			}
		},
	});

	$("#editForm").ajaxForm({
		target : "#list"
	});

	$("#groupid").change(function(){
			$("#nj").empty();
			var xd = $(this).val();
			$.ajax({
				url:"${baseaction}/pes/systeminfo/sys/user/student/getGrades.do",
				data:{
					  "xd":xd
					},
				dataType:"json",
				type:"POST",
				success:function(data){
					$("#nj").append("<option value='0'>请选择</option>");
					$.each(data,function(i,k){
						$("#nj").append("<option value='" + k.gradeid + "'>" + k.njmc + "</option>");
					});
				}
			});
		});
		$("#nj").change(function(){
			$("#bh").empty();
			var nj = $(this).val();
			$("#grade").attr("value", $(this).find("option:selected").text());
			var xd = $("#groupid").val();
			$.ajax({
				url:"${baseaction}/pes/systeminfo/sys/user/student/getClasses.do",
				data:{"nj":nj,
					  "xd":xd
					},
				dataType:"json",
				type:"POST",
				success:function(data){
					$("#bh").append("<option value='0'>请选择</option>");
					$.each(data,function(i,k){
						$("#bh").append("<option value='" + k.id + "'>" + k.bjmc + "</option>");
					});
				}
			});
		});
	$("#bh").change(function(){
		$("#className").attr("value", $(this).find("option:selected").text());
	})

	$("textarea#process").ckeditor({height: '140px'});
	$("textarea#record").ckeditor({height: '160px'});
	function showDisposeTypeOption(selectFirst){
		var level=$("#level").val();
		if(level==undefined){
			level=1;
		}
		if(level==1){
			$('#disposeType').html("");
			$('#disposeType').append('<option value="1" class="disposeType" level="1">与班主任会商</option><option value="2" class="disposeType" level="1">重点观察</option><option value="3" class="disposeType" level="1">班主任约谈</option><option value="4" class="disposeType" level="1">咨询员约谈</option>');	
		}
		if(level==2){
			$('#disposeType').html("");
			$('#disposeType').append('<option value="5" class="disposeType" level="2">报告校领导</option><option value="6" class="disposeType" level="2">校内干预与保护</option>');	
		}
		if(level==3){
			$('#disposeType').html("");
			$('#disposeType').append('<option value="7" class="disposeType" level="3">报告校领导</option><option value="8" class="disposeType" level="3">校内干预与保护</option><option value="9" class="disposeType" level="3">区县中心介入</option><option value="10" class="disposeType" level="3">市中心介入</option>');	
		}
		if(!selectFirst){
			var defaultValue=$('#disposeType').attr("defaultValue");
			$('#disposeType').find("option[value='"+defaultValue+"']").attr("selected",true);
		}
		$('#disposeType').find('option').each(function(i,item){
			if(selectFirst){
				$(item).attr("selected",true);
				selectFirst=false;
			}
		});
	}
	function selectTeacher(){
			var roleId = $("#roleId").val();
					$.ajax({
						url : "${baseaction}/pes/systeminfo/sys/user/teacher/getTeachersJson.do",
						data : {
							"roleId" : roleId
						},
						dataType : "json",
						type : "POST",
						success : function(data) {
							$("#disposePerson").empty();
							$.each(data, function(i, k) {
								$("#disposePerson").append(
										"<option value='" + k.xm + "'>"
												+ k.xm + "</option>");
							});
						}
					});
	}
	showDisposeTypeOption(false);
    <c:choose>
        <c:when test="${pageOpenFlag == 1 && fn:length(xdlist) > 1}">
            $("#warningbasicinfo").hide();
        </c:when>
        <c:otherwise>
            $("#selectStudent").hide();
        </c:otherwise>
    </c:choose>
</script>