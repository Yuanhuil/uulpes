<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="editdialog" title="${empty op ? '新增' : op}活动总结">
		<div class="contentTitle02">
			<a>${entity.title }</a>
		</div>
		<div class="contentTitle03">
			<a>${entity.schoolyear }学年度&nbsp;
			<c:if test="${entity.term eq '1'}">第一学期</c:if>
			<c:if test="${entity.term eq '2'}">第二学期</c:if>
			|<c:if test="${entity.activitycatalog eq '1'}">心理课</c:if>
			 <c:if test="${entity.activitycatalog eq '2'}">心理活动</c:if>
			 <c:if test="${entity.activitycatalog eq '3'}">心理教科研</c:if>
			|
			<c:if test="${entity.activitytype eq '1'}">讲座</c:if>
			<c:if test="${entity.activitytype eq '2'}">公开课</c:if>
			<c:if test="${entity.activitytype eq '3'}">团体辅导</c:if>
			<c:if test="${entity.activitytype eq '4'}">心理剧</c:if>
			<c:if test="${entity.activitytype eq '5'}">小论文</c:if>
			<c:if test="${entity.activitytype eq '6'}">社团活动</c:if>
			<c:if test="${entity.activitytype eq '7'}">班级与团队活动</c:if>
			<c:if test="${entity.activitytype eq '8'}">课题</c:if>
			<c:if test="${entity.activitytype eq '9'}">论文</c:if>
			<c:if test="${entity.activitytype eq '10'}">继续教育</c:if>
			<c:if test="${entity.activitytype eq '11'}">案例</c:if>
			</a>
		</div>
		<div class="content2">
		<h1>活动总结</h1><br>
		${entity.content }</div>
</div>
<script type="text/javascript">
	$(function(){
		var buttonsOps = {};
		<c:choose>
		<c:when test="${op eq '查看'}">
			buttonsOps = {
				"关闭" : function() {
					$("#editdialog").dialog("close");
				}
			};
		</c:when>
		</c:choose>
		$("#editdialog").dialog({
			appendTo : "#editformdiv",
			autoOpen : false,
			modal : true,
			height : 700,
			width : 900,
			position: { my: "top", at: "top", of: "#topHeader" },
			buttons : buttonsOps
		});
	});

</script>


