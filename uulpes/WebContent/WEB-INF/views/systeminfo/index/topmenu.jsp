<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<script type="text/javascript">
	var menus = ${menus};
</script>

<div class="navigation">
<!-- 代码 开始 -->
	<div class="wraper">
		<div class="nav">
			<ul class="ul04">
				<c:forEach items="${menus}" var="m" varStatus="status">
					<li class="nav-item" onclick="changeSubMenu(this,'${status.index}')"><a href="#">${m.name}</a></li>
				</c:forEach>
			</ul>
			<!--移动的滑动-->
			<li class="nav-item" style="display:none">
        </div>
			<!--移动的滑动 end-->
	</div>
</div>