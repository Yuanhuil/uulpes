<script type="text/javascript">	
function answer() {
		window.location.replace("${pageContext.request.contextPath}/scaletoollib/nextpage.do?s=${s}");
		return true;
	}
</script>
<table align="center" class="tb1">
  <tbody>
    <tr>
      <td width="721" height="45"><div align="center" class="STYLE1">量表测试</div></td>
    </tr>
    <tr>
      <td>
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="1" bordercolorlight="#cccccc" class="tb2">
        <tbody>
          <tr>
            <td width="20%" height="25" align="left" bgcolor="#CEF5FF">量表名称：</td>
            <td width="79%" height="25" align="left" bgcolor="#FFFFFF" class="STYLE1">${scale.title}</td>
          </tr>
          <tr>
            <td width="20%"  height="23" align="left" bgcolor="#FFFFFF">量表类型：</td>
            <td width="79%"  height="23" align="left" bgcolor="#FFFFFF">${scale.type}</td>
          </tr>
          <tr>
            <td width="20%" height="25" align="left" bgcolor="#CEF5FF">量表测试人：</td>
            <td width="79%" height="25" align="left" bgcolor="#FFFFFF">赵梓晨</td>
 
          </tr>
          <tr>
            <td height="25" align="left" valign="top" bgcolor="#FFFFFF">量表介绍：</td>
            <td width="79%"  height="25" align="left" bgcolor="#FFFFFF">${scale.descn}</td>
          </tr>
          <tr>
            <td height="30" colspan="2" bgcolor="#CEF5FF"><div align="center">
              <input name="button" type="button" onClick="answer(); return false" value="开始测验" />
              <input type='button' onclick="javascript:{window.location.replace('scaleAttachCtl.app?rl=1');}" value='返回'>
            </div></td>
          </tr>
        </tbody>
      </table>
      </td>
    </tr>
  </tbody>
</table>