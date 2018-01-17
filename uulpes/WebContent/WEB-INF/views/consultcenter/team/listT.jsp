<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div class="tableContent">
	<table class="table table-hover">
		<tr class="titleBg">
			<td width="10%">真实姓名</td>
			<td width="20%">身份证号</td>
			<td width="10%">性别</td>
			<td width="30%">操作</td>
		</tr>
		<c:forEach var="item" items="${list}">
			<tr>
				<td>${item.xm}</td>
				<td>${item.sfzjh}</td>
				<td>${item.xbm}</td>
				<td><span class="header02"> 
					<input class="button-small add_1" type="button"  value="添加" ht="type='hidden' value='${item.id}'>	<td>${item.xm}</td>	<td>${item.sfzjh}</td>	<td>${item.xbm}</td>	<td><span class='header02'> <input	class='button-small del_1 ' type='button' value='删除'> </span></td> </tr>">
				</span></td>
			</tr>
		</c:forEach>
	</table>
</div>
<script type="text/javascript">
	$('.add_1').bind('click', function() {
		var ht = jQuery(this).attr("ht");
		var size= parseInt(jQuery("#memberSize").val())
		ht="<tr><input  name=teamPersons["+size+"].memberid "+ht;
		
		$('#tableT').append(ht);
		jQuery("#memberSize").attr("value",(size+1));
		jQuery(this).parent().parent().parent().remove();
		jQuery('.del_1').bind('click', function() {
			 jQuery(this).parent().parent().parent().remove();
		});
	});
</script>