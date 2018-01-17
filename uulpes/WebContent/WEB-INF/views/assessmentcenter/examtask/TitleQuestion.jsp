<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/examtask"/>
<script type="text/javascript">	
function startanswer() {
	debugger;
	$('#formid').submit();
	var limittime = "${limittime}";
	if (limittime!=''&& limittime!=null && limittime!="undefined"){
	    $('#timelimitpage').css("display","inline-block");
	    intDiff = parseInt('${limittime}')*60;
	    timer(intDiff);
	}
}
</script>
<h2 class="scaletitle">${tester}正测试量表-${scale.title}</h2> <br>
<p style="text-align:center"><u>答题进度：<font color=#FF0000><b>${progress}</b> </font></u></p>

 	<form id="formid" action="${baseaction}/nextpage.do" method="post">
    <table style="border-collapse:collapse;">
    	<tbody>
    	<tr>
    		<td><h2>${ page.titleQuetion }</h2></td>    		
    	</tr>
    	<tr>
    	<td style="text-align:center;">
    		
    		<input class="answer_btn" type="button" value="开始答题"  onclick="startanswer()" style="margin-top:20px;"/>	
    	</td>
    	</tr>
    	</tbody>    	
    </table>
    <input id="s" type="hidden" name="s" value='${s}'/>
	<input id="q" type="hidden" name="q" value='${empty q ? 0 : q}'/>
	<input id="a" type="hidden" name="a" />
	<input id="st" type="hidden" name="st" value='${subjecttitle}' />
	<input id="stt" type="hidden" name="stt" value='${subjecttitletype}' />
    </form>
 <script type="text/javascript">
 $(function(){	
	$("#formid").ajaxForm({
	        		type:"post",
					target : "#firstpage",
					error:function(data){
						debugger;
						layer.open({content:'错误: ' + data.responseText});
					},
				});
	if( $('#timelimitpage').length>0)
	     $('#timelimitpage').css("display","none");
});

</script>      