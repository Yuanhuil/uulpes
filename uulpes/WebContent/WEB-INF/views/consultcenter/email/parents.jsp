<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>



	<c:forEach items="${list}" var="m">
		<div
			style="font-size: 12px;font-family: Arial Narrow;padding:2px 0 2px 0;">------------------
			原始邮件 ------------------</div>
		<div style="font-size: 12px;background:#efefef;padding:8px;">
			<div>
				<b>发件人:</b> "${m.fromid}"
			</div>
			<div>
				<b>发送时间:</b> <fmt:formatDate value="${m.sendDate}"
						pattern="yyyy-MM-dd   hh:mm:ss" />
			</div>
			<div>
				<b>收件人:</b> "${m.toid}"
			</div>
			
		</div>
		<div>
			<br>
		</div>
		<div>${m.describes}</div>
		<div>
			<br>
		</div>
		<div>
			<br>
		</div>
	</c:forEach>





