<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

<div>
  <table>
    <tr>
      <td width="15%">量表名称</td>
      <td width="85%"><h2>${scale.title}</h2></td>
    </tr>
    <tr>
      <td width="15%">题目数量</td>
      <td width="85%">${scale.questionNum }</td>
    </tr>
    <tr>
      <td width="15%">适用人群</td>
      <td width="85%">${scale.applicablePerson }</td>
    </tr>
    <tr>
      <td width="15%">量表类型</td>
      <td width="85%">${scale.scaleType }</td>
    </tr>
    <tr>
      <td width="15%">量表来源</td>
      <td width="85%">${scale.source }</td>
    </tr>
    <tr>
      <td width="15%">量表简介</td>
      <td width="85%">${scale.descn }</td>
    </tr>
  </table>
</div>