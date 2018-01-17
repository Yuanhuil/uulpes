function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function DoMenu(emid)
{
 var obj = document.getElementById(emid); 
 obj.className = (obj.className.toLowerCase() == "expanded"?"collapsed":"expanded");
 if((LastLeftID!="")&&(emid!=LastLeftID)) //封闭上一个Menu
 {
  document.getElementById(LastLeftID).className = "collapsed";
 }
 LastLeftID = emid;
}
function GetMenuID()
{
 var MenuID="";
 var _paramStr = new String(window.location.href);
 var _sharpPos = _paramStr.indexOf("#");
  
 if (_sharpPos >= 0 && _sharpPos < _paramStr.length - 1)
 {
  _paramStr = _paramStr.substring(_sharpPos + 1, _paramStr.length);
 }
 else
 {
  _paramStr = "";
 }
 
 if (_paramStr.length > 0)
 {
  var _paramArr = _paramStr.split("&");
  if (_paramArr.length>0)
  {
   var _paramKeyVal = _paramArr[0].split("=");
   if (_paramKeyVal.length>0)
   {
    MenuID = _paramKeyVal[1];
   }
  }
 }
 
 if(MenuID!="")
 {
  DoMenu(MenuID);
 }
}


function changeSubMenu(node, index){
	
	
	var subMenuDiv = $("#PARENT");
	$(".nav .nav-item").css("backgroundColor", "");
	if(node){
		node.style.backgroundColor = '#2b7f51';
	}
	var $ul = $("<ul id='nav'></ul>");
	var currentNode = menus[index];
	var child = [];
	if(child)
		child = currentNode.children;
	for(var i=0;i<child.length;i++){
		var $li = $("<li id=" + child[i].id + " name=" + index + "_" + i + " ><a href='#' class='" + child[i].icon + "' >" + child[i].name + "</a></li>");
		var nextChildren = child[i].children;
		if(nextChildren){
			for(var j=0;j<nextChildren.length;j++){
				var smenu = $("<ul class='collapsed'><li id=" + nextChildren[j].id + " name=" + index + "_" + i + "_" + j + " ><a href='#'>"+nextChildren[j].name+"</a></li></ul>");
				$li.append(smenu);
			}
		}
		$ul.append($li);
	}
	subMenuDiv.empty();
	subMenuDiv.append($ul);
	expandSubMenu();
	if(menus){
		if(menus[index] && menus[index].url != "" && menus[index].hasChildren==false){
			loadContentDivIncontent(ctx +menus[index].url);
		}else if(menus[index] && menus[index].url != ""){
			loadContentDiv(ctx +menus[index].url);
		}else if(menus[index] && menus[index].children 
				&& menus[index].children[0] && menus[index].children[0].url!=""){
			subMenuDiv.find("ul:first li:first a:first").click();
		}else{
			subMenuDiv.find("ul:first li:first a:first").click();
			subMenuDiv.children(":first").find("li:first ul:first li:first a").click();
		}
	}
		
	$("#content2").empty();
}

function expandSubMenu(e){
	$("#PARENT ul li").click(function(evt)
	{
		var index = $(this).attr("name");
		if(index){
			var _count = index.split("_").length-1;
			if(!_count || _count<=0)
				return;
			if(_count>1){  //如果包含1一个以上“_”，说明是三级菜单
				//阻止事件冒泡
				if(evt.target.parentElement==this){
					evt.stopPropagation();
				}
				var ij = index?index.split("_"):undefined;
				if(ij){
					var node = menus[ij[0]].children[ij[1]].children[ij[2]];
					if(node && node.url && $.trim(node.url)!=""){
						//$("#PARENT").find("li").css('background-color','');
						$("#PARENT").find("li").css('borderRight','');
						//$("#"+node.id).css("background-color", "#09C");
						$("#"+node.id).css("borderRight", "4px solid #369863");
						loadContentDiv(ctx +node.url);
					}
				}
			}else{
				var ij = index?index.split("_"):undefined;
				if(ij){
					var node = menus[ij[0]].children[ij[1]];
					var id = node.id;
					var cat = $("#" + id + " ul");
					if(cat && cat.attr("class")){
						var classname = cat.attr("class").indexOf("expanded")>=0?"collapsed":"expanded";
					}
					//关闭其他的三级菜单
					var catul = $("#nav ul");
					catul.removeClass("expanded");
					catul.addClass("collapsed");
					if(node.children && node.children.length>0){
						cat.removeClass("collapsed");
						cat.removeClass("expanded");
						cat.addClass(classname);
					}
					$("#PARENT").find("li").css('background-color','');
					if(node && node.children && node.children.length==0 && node.url && $.trim(node.url)!="" ){
						//$("#"+node.id).css("background-color", "#09C");
						$("#PARENT").find("li").css('borderRight','');
						$("#"+node.id).css("borderRight", "4px solid #369863");
						loadContentDiv(ctx +node.url);
					}
				}
			}
		}
	});
}

function loadContentDiv(url){
	debugger;
	$("#content2").empty();
	$("#content2").show();
	$("#menu2").show();
	$("#firstpagecontent").hide();
	$("#content2").load(url, function(responseText, textStatus, XMLHttpRequest) {
		debugger;
		if (textStatus == "error" && XMLHttpRequest.status =='404') {  
	       layer.open({content:"请求地址没有找到,请联系管理员!"});  
	    }  
	    else if (textStatus == "timeout") {  
	    	 layer.open({content:"获取数据url超时 "});
	    }else if(textStatus == "error"){
	    	//layer.open({content:"服务器内部错误，请联系管理员!"});
	    	layer.open({content:responseText});
	    } 
	});
}
function loadContentDivIncontent(url){
	$("#content2").empty();
	$("#content2").hide();
	$("#menu2").hide();
	$("#firstpagecontent").show().load(url, function(responseText, textStatus, XMLHttpRequest) {
		debugger;
		if (textStatus == "error" && XMLHttpRequest.status =='404') {  
		       layer.open({content:"请求地址没有找到,请联系管理员!"});  
		    }  
		    else if (textStatus == "timeout") {  
		    	 layer.open({content:"获取数据url超时 "});
		    }else if(textStatus == "error"){
		    	//layer.open({content:"服务器内部错误，请联系管理员!"});
		    	layer.open({content:responseText});
		    } 
	});
}
//GetMenuID(); //*function顺序要留意，否则在Firefox里GetMenuID()不起效果。
//身份证校验
function IdentityCodeValid(code) { 
    var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
    var tip = "";
    var pass= true;
    
    if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
        tip = "身份证号格式错误";
        pass = false;
    }
    
   else if(!city[code.substr(0,2)]){
        tip = "地址编码错误";
        pass = false;
    }
    else{
        //18位身份证需要验证最后一位校验位
        if(code.length == 18){
            code = code.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
            //校验位
            var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++)
            {
                ai = code[i];
                wi = factor[i];
                sum += ai * wi;
            }
            var last = parity[sum % 11];
            if(parity[sum % 11] != code[17]){
                tip = "校验位错误";
                pass =false;
            }
        }
    }
    if(!pass) alert(tip);
    return pass;
}