<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="" class="P_title">
<table  style="margin:auto;width:70%">
<tr>
<td width="25%">人数</td><td width="25%">${totalNum }人</td>
<td width="25%">性别</td><td width="25%">男:${maleNum }人<br>女:${femaleNum}人</td>
</tr>
<tr>
<td width="25%">民族</td><td width="25%">汉族:${hNationNum }人<br>其他名族:${otherNationNum }人</td>
<td width="25%">血型</td><td width="25%">A型:${xxANum }人<br>B型:${xxBNum }人<br>AB型:${xxABNum }人<br>O型:${xxONum }人</td>
</tr>
<tr>
<td width="25%">身体健康状况</td><td width="25%">健康或良好:${health10 }人<br>一般或较弱:${health20 }人<br>有慢性病:${health30 }人<br>有生理缺陷:${health40 }人<br>残疾:${health50 }人</td>
<td width="25%">就读方式</td><td width="25%">走读:${jxfs1 }人<br>住校:${jxfs2}人<br>借宿:${jxfs3 }人<br>其它:${jxfs4}人</td>
</tr>
<tr>
<td width="25%">家庭类别</td><td width="25%">双亲健全:${family10 }人<br>孤儿:${family20 }人<br>单亲:${family30 }人<br>父母离异:${family40 }人<br>双亲有残疾:${family50 }人<br>本人有残疾:${family60 }人<br>军烈属或优抚对象:${family70 }人<br>重病:${family80 }人<br>五保户:${family90 }人</td>
<td width="25%">家庭困难程度</td><td width="25%">特别困难:${kncd1 }人<br>一般困难:${kncd2 }人<br>不困难:${kncd3 }人</td>
</tr>
<tr>
<td width="25%">学生体质达标</td> <td width="25%">优秀:${tzdb1 }人<br>良好:${tzdb2 }人<br>及格:${tzdb3 }人<br>不合格:${tzdb4 }人</td>
<td width="25%">学生来源</td><td width="25%">正常入学:${xsly1 }人<br>借读:${xsly2 }人<br>其他:${xsly3 }人</td>
</tr>
</table>
</div>
