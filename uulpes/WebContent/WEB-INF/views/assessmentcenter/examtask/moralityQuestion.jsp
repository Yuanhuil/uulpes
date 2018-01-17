<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<script src="${ctx}/js/commqsort.js"></script>
<script type="text/javascript">	
function selFunc(obj){
	debugger;
	if (obj.value=="y"){
		yesDiv.style.display="block";
		noDiv.style.display="none";
	}else{
		yesDiv.style.display="none";
		noDiv.style.display="block";
	}
	selDiv.style.display="block";	
	obj.checked=true;
}
function submitForm() {
	debugger;
	var b= getItemValue('question1');
	var q1 ;
	if (b=='y') {
		q1 = 0;
		ARRAY_Q[4]=-1;
		ARRAY_Q[5]=-1;
		ARRAY_Q[6]=-1;
		ARRAY_Q[7]=-1;
	}
	else {
		q1 = 1;
		ARRAY_Q[0]=-1;
		ARRAY_Q[1]=-1;
		ARRAY_Q[2]=-1;
		ARRAY_Q[3]=-1;
	}
	if (noCheck(b)){
		alert("没有选择题目！");
		return false;
	}
	if (document.getElementById("a2").value==""){
		alert("没有排序题目");
		return false;
	}
	var a1 = ARRAY_Q.join(",");
	var answer = q1+','+a1+'$'+a2.value;
	$('#a').val(answer);
	$('#formid').submit();
	return true;
}

function setItemValues(index,qid) {
	debugger;
	var selectedValue = getItemValue(qid);
	var b= getItemValue('question1');
	if (b=='y') 
		ARRAY_Q[index] = selectedValue;
	else
		ARRAY_Q[parseInt(index)+4] = selectedValue;

}
function getItemValue(itemName) {
	var items = document.getElementsByName(itemName);
	if (items!=null) {
	var len   = items.length;
	for (var i = 0; i < len; i = i + 1) {
		if (items[i].checked) {
			return items[i].value;
		}
	}
	}
	return -1;
}

function noCheck(yesorno) {
	var startindex=0;
	if(yesorno=='y') 
	{
		for (var i = 0; i < 4; i = i + 1) {
			if (ARRAY_Q[i]==-1)
			 return true;
		}
	}
	else 
	{
		for (var i = 4; i < 8; i = i + 1) {
			if (ARRAY_Q[i]==-1)
			 return true;
		}
	}
}
var ARRAY_Q =  [-1,-1,-1,-1,-1,-1,-1,-1];
</script>
<style type='text/css'>
.H2 /* Question text parameters */
{font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
font-size: 14px;
font-weight: bold;
text-decoration: none;
text-align: left;
padding: 5px;
color: #ffffff;
background-color: #0099ff}

</style>

<input type='hidden' id='a1'>&nbsp;
<input type='hidden' id='a2' value="0,1,2,3">
<table width='90%' border='0' cellpadding='0' cellspacing='0' class='tablebckg' align='center'>
	<tr>
		<td>
			<font face="楷体_GB2312" size="3"><strong>
			&nbsp;&nbsp;&nbsp;&nbsp;${page.headtitle }
			</strong></font>
			<br><br><font face="楷体_GB2312" size="3"><strong>${page.pdtitle}：</strong></font>
    		<input type="radio" onclick="selFunc(this)" name="question1" value="y"><font face="楷体_GB2312" size="3"><strong>${page.jyLabel }</strong></font>&nbsp;&nbsp;&nbsp;
    		<input type="radio" onclick="selFunc(this)" name="question1" value="n"><font face="楷体_GB2312" size="3"><strong>${page.jnLabel }</strong></font>
			<br>
		</td>
	</tr>
			<tr id="yesDiv" style="display:none;">	
		<td>					
			<table style="width:700px;border-collapse:collapse;">	
				<c:forEach var="qes_y" items="${page.qlist_yes}" varStatus="status" >			
					<tr>
						<td colspan="5"  class='H2'>${status.index+1 }.${qes_y.title }</td>
					</tr>
					<tr>
					  <c:forEach var="option" items="${qes_y.options}" >		
						<td style="font-size:14px;"><input type="radio" name="${qes_y.id}" value="${option.value }" onclick="setItemValues('${status.index}','${qes_y.id}')" style="vertical-align:middle;"/>${option.title }</td>
				      </c:forEach>
					</tr>
					<tr><td colspan="5">&nbsp;</td></tr>
				</c:forEach>
				
					
			</table>
		</td>
	</tr>
	<tr id="noDiv" style="display:none;">
		<td>
			<table style="width:700px;border-collapse:collapse;">	
				<c:forEach var="qes_n" items="${page.qlist_no}" varStatus="status" >			
					<tr>
						<td colspan="5"  class='H2'>${status.index+1 }.${qes_n.title }</td>
					</tr>
					<tr>
					  <c:forEach var="option" items="${qes_n.options}" >		
						<td style="font-size:14px;"><input type="radio" name="${qes_n.id}" value="${option.value }" onclick="setItemValues('${status.index}','${qes_n.id}')" style="vertical-align:middle;"/>${option.title }</td>
				      </c:forEach>
					</tr>
					<tr><td colspan="5">&nbsp;</td></tr>
				</c:forEach>
			</table>			
		</td>		
	</tr>
	<tr id="selDiv" style="display:none;">
		<td><table  style="width:700px;">
			    <tr><td class='H2'>对重要性进行排序：先选中题目再点击“上移”或“下移”按钮进行重要性排序,把您认为最重要的排在最上面，依次排序：</td></tr>
				<tr><td>
<table style="border-collapse:collapse;">
	<tr>
		<td width="150" rowspan="2" valign="top">
<select id="orderCtl" size="5" multiple>
  <option value="0">----------第一题----------</option>
  <option value="1">----------第二题----------</option>
  <option value="2">----------第三题----------</option>
  <option value="3">----------第四题----------</option> 
</select></td>
		<td valign="top"><input type="button" value="下移" onClick="moveDown('orderCtl','a2')"></td>
	</tr>
	<tr>
		<td valign="top"><input type="button" value="上移"  onClick="moveUp('orderCtl','a2')"></td>
	</tr>
</table>
				   </td>
					</tr>
					<TR>
					<td style="text-align:center;">
					<HR>
					<input class="answer_btn" type="button" value="下一页"  onclick="submitForm()"/>	
					</td>
					</TR>
</table>
</td>
</tr>
</table>





<form id="formid" action="${baseaction}/nextpage.do" method="post">
<input id="a" type="hidden" name="a" />
<input id="s" type="hidden" name="s" value='${s}'/>
<input id="q" type="hidden" name="q" value='${empty q ? 0 : q}'/>
</form>
<script type="text/javascript">
$(function(){
	$("#formid").ajaxForm({
	        		type:"post",
					target : "#firstpage"
				});
});

</script>