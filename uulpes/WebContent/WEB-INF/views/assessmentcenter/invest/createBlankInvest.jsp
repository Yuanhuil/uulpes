<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/invest"/>
<link href="${ctx }/css/createblank.css" rel="stylesheet" type="text/css">
<form id="investform" method="post" action="${baseaction}/createInvest.do">
<div  class="TE-box2" style="padding-top:30px;">
            <table border="0" cellspacing="0" cellpadding="0" width="700px" align="center">
                <tr>
                    <td align="left" width="100px" style="font-size:16px;">
                        <b>问卷名称：</b>
                    </td>
                    <td align="left">
                        <input name="investname" type="text" maxlength="100" id="investname" class="inputtext" style="height:25px;width:457px;line-height:25px;" />&nbsp;
                        
                        <span id="ctl01_ContentPlaceHolder1_RequiredFieldValidator1" style="color:Red;visibility:hidden;">*</span>
                    </td>
                </tr>
               
                <tr>
                <td colspan="2">
                <a href="javascript:;" class="a555" onclick="showHide(this);">添加问卷说明(可选)</a>
                </td>
                </tr>
                <tr id="trDesc" style="display:none;">
                    <td align="left" valign="top" width="100px">
                        问卷说明：
                    </td>
                    <td align="left" valign="top">
                        <textarea name="content" rows="2" cols="20" id="content" Value="非常感谢您填写我的问卷！" style="height:150px;width:460px;"></textarea>
                        <div id="divKeXuan">（可选）</div>
                            <div style="display:none;">
                                提示：问卷说明不能超过4000个字符<span id="spanDTip" style="display: none;">，已经输入<span id="spanHInput"></span>字符,还可以输入<span
                                    id="spanLeftInput"></span>字符</span></div>
                        
                    </td>
                    
                </tr>
              
            </table>

            <div style="margin:15px 0 30px;">
                <table align="center">
                    <tr>
                        <td valign="middle">
                            <span id="spanCode">
                                
                            </span>
                        </td>
                        <td style="text-align:center;">
                            <input type="submit"  value="确定" onclick="" class="button-mid blue" />&nbsp;<span id="ctl01_ContentPlaceHolder1_lblMsg" style="color:Red;"></span>
                        </td>
                        <td width="150">
                        </td>
                        <td valign="middle" style="display: none;">
                        </td>
                    </tr>
                </table>
            </div>
        </div>
      </form>
 <script type="text/javascript">
	$(function(){
		$("#investform").ajaxForm({
			target : "#content2"
		});
		});
 function showHide(obj){
     var trDesc=document.getElementById("trDesc");
     trDesc.style.display="";
     obj.parentNode.parentNode.style.display="none";
     //KE.init({
          //id: desc,
	      //  DesignPage:1,
          //items:EditToolBarItemsPageCut
     // });
     // KE.create(desc);
     // setInterval(new Function('KE.util.setData("' + desc + '")'), 500);
       //var attr_desc = document.getElementById(desc);
      //attr_desc.onblur = attr_desc.onclick = attr_desc.onchange = function () {
          //paper_attr_desc_onblur(this);
     // };
  }
 function paper_attr_desc_onblur(attr) {
     document.getElementById("spanHInput").innerHTML = attr.value.length;
     var left = 4000 - attr.value.length;
     if (left < 0) left = 0;
     document.getElementById("spanLeftInput").innerHTML = left;
     document.getElementById("spanDTip").style.display = "";
      document.getElementById("spanDTip").parentNode.style.display = "";
     document.getElementById("divKeXuan").style.display="none";
 }
 $("textarea#content").ckeditor();
 </script>
