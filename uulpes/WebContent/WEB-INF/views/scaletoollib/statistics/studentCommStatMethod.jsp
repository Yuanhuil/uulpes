<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="statformurl" value="../scaletoollib/statistics/getDistricts.do"></c:set>
<form id="StatParams" name="StatParams" method="post" action="${statformurl}">
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div class="stepControl">
		<div class="step1">第一步</div>
		<div class="step2">第二步</div>
		<div class="step3">第三步</div>
		<div class="step4">第四步</div>
    </div>
    <div class="stepContent">
	    <div class="singleForm">
	      <ul>
	        <li>
	          <label class="name04">统计方法</label>
	   		  <select id="statMethod" class="selectNormal">
	   		  	  <option value="0" >请选择</option>
		   		  <c:forEach var="item" items="${orgLevelList}">
		   		  	<option value="${item.id}" >${item.orglevelname} </option>
		   		  </c:forEach>
	          </select>
	        </li>
	      </ul>
	    </div>
	</div>
<!--翻页导航start-->
  <div class="pageNav">
	  <input type="button" class="green" id="next" value="下一步">
  </div>
<!--翻页导航end-->
</div>
</form>
<script type="text/javascript">
	$(function(){
		//下一步按钮动作
		$("#next").click(function(evt){
			debugger;
			$.validationEngineLanguage.newLang();
		});
	});
</script>