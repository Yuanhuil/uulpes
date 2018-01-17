<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<c:set var="baseaction" value="${ctx }/assessmentcenter/invest"/>
<link href="${ctx }/css/createblank.css" rel="stylesheet" type="text/css">
<link href="${ctx }/css/investquestion.css" rel="stylesheet" type="text/css">
<link href="${ctx }/css/divdlg.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	var themeUrl = "${ctx}/themes/theme1/";
	function showDlg()
    {
        //显示遮盖的层
        var objDeck = document.getElementById("deck");
        if(!objDeck)
        {
            objDeck = document.createElement("div");
            objDeck.id="deck";
            document.body.appendChild(objDeck);
        }
        objDeck.className="showDeck";
        objDeck.style.filter="alpha(opacity=50)";
        objDeck.style.opacity=40/100;
        objDeck.style.MozOpacity=40/100;
        //显示遮盖的层end
        
        //禁用select
       // hideOrShowSelect(true);
        
        //改变样式
        document.getElementById('divBox').className='showDlg';
        
        //调整位置至居中
        adjustLocation();
        
    }
    
    function cancel()
    {
        document.getElementById('divBox').className='hideDlg';
        document.getElementById("deck").className="hideDeck";
        //hideOrShowSelect(false);
    }
    
    function hideOrShowSelect(v)
    {
        var allselect = document.getElementsByTagName("select");
        for (var i=0; i<allselect.length; i++)
        {
            //allselect[i].style.visibility = (v==true)?"hidden":"visible";
            allselect[i].disabled =(v==true)?"disabled":"";
        }
    }
    
    function adjustLocation()
    {
        var obox=document.getElementById('divBox');
        if (obox !=null && obox.style.display !="none")
        {
            var w=368;
            var h=129;
            var oLeft,oTop;
            
            if (window.innerWidth)
            {
                oLeft=window.pageXOffset+(window.innerWidth-w)/2 +"px";
                oTop=window.pageYOffset+(window.innerHeight-h)/2 +"px";
            }
            else
            {
                var dde=document.documentElement;
                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
            }
            
            obox.style.left=oLeft;
            obox.style.top=oTop;
        }
    }
</script>
<form:form id="investform" action="${baseaction}/save.do" method="post"  modelAttribute="invest">
<!-- <div style="margin-right: auto; margin-bottom: 0px; margin-left: auto;">-->
<div style="border: 1px solid #e34433">
        <div class="filterContent">
			<ul>
				<li><label class="name03">题目类型</label>
				<select id="questionType" class="select_130">
					<option value="single">单选题</option>
					<option value="multi">多选题</option>
				</select></li>
			</ul>
        </div>

    <div  id="invest_editor" style="height:inherit"></div>
    <div style="clear:both"></div>
    </div>
    <div class="buttonContent" id="invest_query">
		<ul>
			<li>
				<input class="button-small blue" type="button" value="增加" id="invest_add">
				<input class="button-small blue" type="button" value="删除" id="invest_del">
				<input class="button-small blue" type="button" value="修改" id="invest_upd">
				<input class="button-small blue" type="button" value="保存" id="invest_upd_sav" style="display:none">
				<input class="button-small blue" type="button" value="保存问卷" id="invest_sav" chref="${baseaction}/save.do">
			</li>
			<li>
				<input class="button-small blue" type="button" value="全选" id="invest_selall">
				<input class="button-small blue" type="button" value="反选" id="invest_unsel">
			</li>
		</ul>
	</div>
    
	<div class="survey" style=" overflow: auto; padding-top: 15px; padding-right: 24px; padding-bottom: 15px; padding-left: 25px; background-image: none; background-attachment: scroll; background-repeat: repeat; background-position-x: 0%; background-position-y: 0%; background-color: rgb(255, 255, 255);">
	    <div style="width: 780px;margin: 0 auto; padding-left: 10px;">
	         <div id="question" class="surveycontent">
	             <div id="divId" class="surveyhead" style="border: 2px solid #ffffff; margin-top: 3px;
	                  width: 730px; cursor: pointer;" title="鼠标点击编辑问卷标题与问卷说明" onclick="showDlg();">
	                  <h1 id="investtitle" style="cursor: pointer;">${title }</h1>
	                  <div id="investdescn" class="surveydescription" style="cursor: pointer;">${guide }</div>
	             </div>
	         </div>
	         <div style="clear: both;"></div>
	    </div>
	     <div id="divbatchq" style="display:none; margin:20px auto 10px; width:220px;">
	             <a href="javascript:" class="BS_btn" style="height:35px; line-height:35px; width:200px; text-align:center; background:#56bbed;">批量添加题目</a>
	     </div>
	     <div class="div_question" id="invest_questions"></div>
	</div>

<!-- </div>-->
</form:form>
<div id="divBox" class="hideDlg" style="" >
        <ul class="div_title_attr_question" style="padding: 10px 0 0 10px; margin: 0px; background:#fff;">
            <li style="list-style-type: none;"><strong>问卷标题：</strong><input id="paper_attr_title" value="${title }"
                title="在此输入问卷的标题，例如：老师授课满意度调查" onblur="paper_attr_title_onblur(this);" style="font-size: 12px;
                width: 400px;" class="" type="text" value="请输入您的问卷的标题" size="54" maxlength="100" />
            </li>
            <li style="margin-top: 15px;">
                <table>
                    <tr>
                        <td valign="middle">
                            <strong>问卷说明：</strong>
                        </td>
                        <td>
                            <textarea id="paper_attr_desc" name="content"  onblur="paper_attr_desc_onblur(this);" rows="10" title="在此输入问卷说明" >${guide }</textarea><br />
                        </td>
                    </tr>
                    
                </table>
            </li>
        </ul>
        <div style="text-align: center; margin-top: 10px;">
           <a href="javascript:;" onclick="" class="sumitbutton">确定</a>
           <a href="javascript:;"  class="sumitbutton"  onclick="cancel();">取消</a>
        </div>

</div>
<script type="text/javascript">
$(function(){
	var editor = new $.questionEditor("invest_editor");
	$("#paper_attr_desc").ckeditor({toolbar:[['Styles','Format','Font','FontSize','TextColor','BGColor']]});
	$(".surveyhead").mouseover(function(){
		 $(".surveyhead").css("border-style","solid");
		 $(".surveyhead").css("border-width","2px");
		 $(".surveyhead").css("border-color","rgb(253,181,83)");
	});
	$(".surveyhead").mouseout(function(){
		 $(".surveyhead").css("border-style","solid");
		 $(".surveyhead").css("border-width","2px");
		 $(".surveyhead").css("border-color","rgb(255,255,255)");
	});
});

</script>