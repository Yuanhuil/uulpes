<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="statformurl" value="../../scaletoollib/statistics/${statType}/nextStep.do"></c:set>
<form id="StatParams" name="StatParams" method="post" action="${statformurl}">
<div>
	<!--搜索条件区start（每行三列搜索条件，可以复制）-->
	<div class="stepControl">
		<c:if test="${step>=0}"><div class="step1">第一步</div></c:if>
		<c:if test="${step>=1}"><div class="step2">第二步</div></c:if>
		<c:if test="${step>=2}"><div class="step3">第三步</div></c:if>
		<c:if test="${step>=3}"><div class="step4">第四步</div></c:if>
    </div>
    <div class="stepContent">
	    <div class="singleForm">
	      <ul>
	        <li>
	          <label class="name04">量表类型</label>
	   		  <select id="scaleType" class="selectNormal">
	   		  	  <option value="0" >请选择</option>
		   		  <c:forEach var="item" items="${scaleTypeList}">     
		   		  	<option value="${item.id}" >${item.name} </option>
		   		  </c:forEach>
	          </select>
	        </li>
	      </ul>
	    </div>
	    <div class="singleForm">
	      <ul>
	        <li>
	          <label class="name04">量表名称</label>
	   		  <select id="scaleName" class="selectNormal">
	   		  	  <option value="0" >请选择</option>
	   		  	  <c:forEach var="item" items="${scaleList}">     
		   		  	<option value="${item.id}" >${item.title} </option>
		   		  </c:forEach>
	          </select>
	        </li>
	      </ul>
	    </div>
	    <div class="singleForm">
	      <ul>
	        <li>
	          <label class="name04">量表维度</label>
	   		  <select id="scaleDimension" class="selectNormal">
	   		  	  <option value="0" >请选择</option>
	          </select>
	        </li>
	      </ul>
	    </div>
	    <c:if test="${statType==3}">
		    <div class="singleForm">
		      <ul>
		        <li>
		          <label class="name04">常模</label>
		   		  <select id="scaleNorm" class="selectNormal">
		   		  	  <option value="0" >请选择</option>
		          </select>
		        </li>
		      </ul>
		    </div>
		</c:if>
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