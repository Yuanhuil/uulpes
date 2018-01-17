<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>





<table id="content">
	<thead>
		<tr class="titleBg">
			<th width="25%">咨询类型</th>
			<th width="25%">开关</th>
			<th width="25%">排序</th>
			<th width="25%">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${consultTypes}" var="m">
			<tr>
				<td><a>${m.name}</a></td>
				<td>${m.status==1?"开":"关"}</td>
				<td><a>${m.sort}</a></td>
				<td><input class="button-small white edit1" id="${m.id}"
					type="button" value="编辑"> <input
					class="button-small white del" id="${m.id}" type="button"
					value="删除"> <input class="button-small white switch"
					id="${m.id}" type="button" value="切换">
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>








<script type="text/javascript">
	jQuery('.edit1').bind('click', function() {

		var id = jQuery(this).attr("id");
		var h = '/pes/consultcenter/consultType/addOrUpdate.do?id=' + id;
		$('#consultTypeEdit').html();
		$('#consultTypeEdit').load(h, function() {
			$("#dialog-form1").dialog("open");
		});

	});

	jQuery('.del').bind(
			'click',
			function() {
				if(confirm('确认删除')){
				var id = jQuery(this).attr("id");
				
				divLoadUrl("consultTypeList",
						'/pes/consultcenter/consultType/delete.do?id='
								+ id);
				}
			});
	jQuery('.switch').bind(
			'click',
			function() {
				var id = jQuery(this).attr("id");
				divLoadUrl("consultTypeList",
						'/pes/consultcenter/consultType/updateStatus.do?id='
								+ id);
			});
</script>