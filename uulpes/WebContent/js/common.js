// JavaScript 构造 endwith()方法
String.prototype.endWith = function(oString) {
	var reg = new RegExp(oString + "$");
	return reg.test(this);
};
// JavaScript 构造 startWith()方法
String.prototype.startWith = function(str) {
	var reg = new RegExp("^" + str);
	return reg.test(this);
};
// JavaScript 构造 replaceAll()方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
};
// JavaScript 构造 isNull()方法
String.prototype.isNull = function() {
	if(this==undefined){
		 return true;
	}
	if(this==null){
		return true;
	}
	if(this==''){
		return true;
	}
	return false;
}

function isNull(value){
	if(value==undefined){
		 return true;
	}
	if(value==null){
		return true;
	}
	if(value==''){
		return true;
	}
	return false;
}
// 在指定div加载指定url
function divLoadUrl(divid,url){
	$("#"+divid).empty();
	$("#"+divid).load(url, function(responseText, textStatus, XMLHttpRequest) {
		if (textStatus == "error") {  
	       console.log("获取数据url错误 ");  
	    }  
	    else if (textStatus == "timeout") {  
	    	console.log("获取数据url操时 ");
	    }  
	});
}
/**
 * 删除父元素
 * 
 * @param i
 *            表示第几级 0为删除自己
 */

function removeParent(i){
	if(i===0){
		console.log(jQuery(this));
		jQuery(this).remove();
	}else{
		var parent;
		while(i>0){
			parent=jQuery(this).parent();
			i--;
		}
		console.log(parent);
		console.log(parent.html());
		parent.remove();
	}
	
}

function removeByID(id){
       console.log(id);
		jQuery("#"+id).remove();
	
}
/**
 * 选中值为 value 的选项
 * 
 * @param id
 * @param value 选中值
 * 
 * 
 */
function select2selected(id,value){
	if(isNull(value)){
		 value=$('#'+id).attr("oldValue");
	}
	$('#'+id).find('option').each(function(i,item){
		var name = $(item).attr("value");
		if(name===value){
			$(item).attr("selected","selected");
		}
	});
		
}

//组装查询条件
function  getQueryValue(divId) {
	
	var str='';
	$tbody = $("#"+divId+"");
	$tbody.find('input').each(function(i,item){
		var name = $(item).attr("name"),
	    val =  $(item).val(),
	    type=$(item).attr("type");
		if(type=='radio'){
			if($(item).attr("checked")=="checked"){
				 str+="&"+name+"="+$.trim(val);
			}
		}else if(name!=null&&val!=null&&$.trim(val)!=""){
// if($(item).attr("type_input") === "string"){
// val = "*" + val + "*";
// }
			str+="&"+name+"="+$.trim(val);
		}		
	});
	$tbody.find('select').each(function(i,item){
		var name = $(item).attr("name"),multiple=$(multiple).attr("multiple");
		if(name!=null&&multiple=='multiple'){
			var array_of_checked_values = $(item).multiselect("getChecked").map(function(){
	            return this.value;   
			}).get();
			var val="";
			for(var i=0;i<array_of_checked_values.length;i++){
				val+=$.trim(array_of_checked_values[i])+",";
			}
			if(val!=""){
				str+="&"+name+"="+val.substring(0,val.length-1);
			}
			
		}else{
			
			 var   val =  $(item).val();
				if(name!=null&&val!=null&&$.trim(val)!=""){
					str+="&"+name+"="+$.trim(val);
				}
		}
	});
	if(str.length>0){
		 return str;
	}
	
	 return '';
	}
// 组装查询条件为json格式
function  getQueryValueJson(divId) {
	var a=getQueryValue(divId);
	   
	
	 var obj =null;
   if(a!=''){
	    a=a.replace("&","");
	    a=a.replaceAll('&','","');
	    a=a.replaceAll('=','":"');
	   a='{"'+a+'"}';
	   var obj = jQuery.parseJSON(a);
   }
   return obj;
}
//让某个元素闪烁提示
jQuery.fn.flash = function( color, duration, times )
{
  var current = this.css( 'color' );
  for(var i=0;i<times;i++){
	  this.animate( { color: 'rgb(' + color + ')' }, duration / 2 );
	  this.animate( { color: current }, duration / 2 );
  }
}