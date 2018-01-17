<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
 <table>
    <tr>
      <td width="15%">指导语</td>
      <td width="85%">${scale.guidance}</td>
    </tr>
    <tr>
   	  <td colspan="2" style="text-align:center;">题目列表</td>
    </tr>
    <tr>
   	  <td colspan="2">${toQuestionHTML}</td>
    </tr>
</table>

