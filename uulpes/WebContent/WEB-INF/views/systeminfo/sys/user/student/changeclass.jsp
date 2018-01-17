<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form id="changeclassform" action="${baseaction}/student/search.do" method="post" >
		<div class="filterContent">
			<ul>
				<li><label class="name03">选择班级</label><select id="bj" cssClass="select_160" onchange="setNewClass()" >
					<option value="">请选择</option>
					<c:forEach items="${classList}" var="item">
		        		<option value="${item.id}">${item.bjmc}</option>
		       		</c:forEach>
				</select></li>
			</ul>
		</div>
</form>
<script>
  function setNewClass(){
	  var bj = document.getElementById("bj"); //定位id
	  var index = bj.selectedIndex; // 选中索引 
	  var value = bj.options[index].value; // 选中值
	  parent.setNewClassId(value);
  }
</script>
