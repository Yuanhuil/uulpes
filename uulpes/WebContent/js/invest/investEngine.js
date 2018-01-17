/*
 * Question 问题
 * Author:shibin
 * Date: 2015-6-13
 */
//step01 定义JQuery的作用域
(function ($) {
	//单选选项对象
	$.extend({singleOption: function () {
		var id = null;        //序号
		var name = null;      //题目id+_+optionid
		var title = null;
		var value = null;
		var titleTemplate = "{charNo}：&nbsp;{title}";
		var selectTemplate = "<li style=\"width: 19%;float:left;padding-left:20px\"><input type=\"radio\" name=\"{name}\" value=\"{value}\" />" +
				"<label>{content}</label></li>";
		var selectAddTemplate = "<li style=\"width: 19%;float:left;padding-left:20px\"><input type=\"radio\" name=\"{name}\" value=\"{value}\" class=\" {validation}\" ><label style=\"display: inline-block;\">{content}</label>"
            + "<input class=\"underline itemExtend\" type=\"text\" value=\"{extendValue}\" rel=\"{rel}\" "
            + "style=\"color: rgb(153, 153, 153); position: static;\"></li>";
		this.getId = function(){
			return id;
		};
		this.setId = function(Id){
			id = Id;
		};
		//设置option的名称，通常是题目id+_+optionid
		this.getName = function(){
			return name;
		};
		this.setName = function(Name){
			name = Name;
		};
		//设置答案内容
		this.getTitle = function(){
			return title;
		};
		this.setTitle = function(Title){
			title = Title;
		};
		//设置答案值
		this.getValue = function(){
			return value;
		};
		this.setValue = function(Value){
			value = Value;
		};
		this.setSeletTemplate = function(temp){
			selectTemplate = temp;
		};
		//获得答案html
		this.getOptionHTML = function(){
			var tTemplate = titleTemplate.replace("{title}", this.getTitle());
			tTemplate = tTemplate.replace("{charNo}", this.getCharNo(this.getId()));
			var sTemplate = selectTemplate.replace("{name}", this.getName());
			sTemplate = sTemplate.replace("{value}", this.getValue());
			sTemplate = sTemplate.replace("{content}", tTemplate);
			return sTemplate;
		};
		//数字转字母,1-A,2-B etc
		this.getCharNo = function(no){
			return String.fromCharCode(64 + parseInt(no)); 
		};
	}});
	
	//多选选项对象
	//$.extend({multiOption: function () {}});
	//$.multiOption.prototype = new $.singleOption();
	$.extend({multiOption:function(){
    	$.singleOption.call(this);
    	this.setSeletTemplate("<li style=\"width: 19%;float:left;padding-left:20px\"><input type=\"checkbox\" class=\"checkbox01\" name=\"{name}\" value=\"{value}\" /><label>{content}</label></li>");
    }});
	
	//$.multiOption.prototype.setSeletTemplate("<input type=\"checkbox\" name=\"{name}\" value=\"{value}\" /><label>{content}</label>");
    //单选题目基类
    $.extend({singleQuestion:function(){
        this.id = null;
        this.type = "1";           //多选或单选
        this.title = null;          //题干
        this.titleTemplate = "<li style=\"width: 100%;\"><div class=\"div_topic_question\"><input type=\"checkbox\" id=\"sel_{id}\" class=\"checkbox01\"></div><div class=\"div_topic_question\">{id}.</div><label>{title}</label></li>";
        this.qoptions = null;        //选项
        this.questionContainer = "<div class=\"div_question\" id=\"{id}\">{content}</div>";   //题的Div容器
        this.content = null;        //模板
        //设置id
        this.getId = function(){
        	return this.id;
        };
        this.setId = function(Id){
        	this.id = Id;
        };
        //设置类型
        this.getType = function(){
        	return this.type;
        };
        this.setType = function(Type){
        	this.type = Type;
        };
        //设置整个题目内容
        this.getContent = function(){
        	return this.content;
        };
        this.setContent = function(Content){
        	this.content = Content;
        };
        //获得选项的html
        this.getOptionsHTML = function(){
        	var htmlStr = "";
        	for(var i=0;i<this.getOptions().length;i++){
        		htmlStr = htmlStr + this.getOptions()[i].getOptionHTML();
        	}
        	return htmlStr;
        };
        //设置题干
        this.getTitle = function(){
        	return this.title;
        };
        this.setTitle = function(Title){
        	this.title = Title;
        };
        //题干的html串
        this.getTitleHtml = function(){
        	return this.titleTemplate.replaceAll("{id}", this.id).replace("{title}", this.title);
        };
        //问题的html
        this.getQuestionHTML = function(){
        	var qContainer = this.questionContainer.replace("{id}", this.getId());
        	return qContainer.replace("{content}", this.getTitleHtml() + this.getOptionsHTML());
        };
        //设置答案
        this.getOptions = function(){
        	return this.qoptions;
        };
        this.setOptions = function(ops){
        	this.qoptions = ops;
        };
        
    }});
    
    //多选题目基类
    $.extend({multiQuestion:function(){
    	$.singleQuestion.call(this);
    	this.setType("2");  
    }});
    //$.multiQuestion.prototype = new $.singleQuestion();
    //$.multiQuestion.prototype.setType("2");           //多选或单选
    
    //问卷对象，记录所有的题目信息，增加删除修改
    $.extend({questionnire:function(){
    	var _self = this;
    	this.container = null;
    	this.questions = [];
    	//如果是修改，则需要初始化
    	this.init = function(questions){
    		//初始化容器，创建div
			if(this.container===null){
				if($("#invest_questions")){
					this.container = $("#invest_questions");
				}else{
					this.container = $('<div></div>');
					this.container.attr("id", "invest_questions");
					this.container.insertAfter("#invest_query");
				}
			}
    		//赋值questions
    		for(var i=0;i<questions.length;i++){
    			if(questions[i].type==="single"){
        			newQ = new $.singleQuestion();
        		}else{
        			//多选
        			newQ = new $.multiQuestion();
        		}
    			newQ.setId(i + 1);
    			newQ.setTitle(questions[i].title);
    			var opts = questions[i].options;
    			var options = [];
    			for(var j=0;j<opts.length;j++){
    				if(questions[i].type==="single"){
    					var opt = new $.singleOption();
    					opt.setId(opts[j].id);
    					opt.setTitle(opts[j].title);
    					opt.setValue(opts[j].value);
    					options.push(opt);
    				}else{
    					var opt = new $.multiOption();
    					opt.setId(opts[j].id);
    					opt.setTitle(opts[j].title);
    					opt.setValue(opts[j].value);
    					options.push(opt);
    				}
    			}
    			newQ.setOptions(options);
    			//div装载新增题的内容
    			this.questions.push(newQ);
    			this.container.append(newQ.getQuestionHTML());
    		}
    	};
    	this.add = function(questionEditor){
    		//增加question数组
    		var newQ;
    		if($("#questionType").val()==="single"){
    			newQ = new $.singleQuestion();
    		}else{
    			//多选
    			newQ = new $.multiQuestion();
    		}
			newQ.setId(this.questions.length + 1);
			newQ.setTitle($("#invest_editor_title").val());
			var opts = [];
			$("#invest_editor_options input").each(function(i){
				//跳过题目，只取选项
				if($("#questionType").val()==="single"){
					var opt = new $.singleOption();
					opt.setId(i + 1);
					opt.setTitle($(this).val());
					opt.setValue(i);
					opts.push(opt);
				}else{
	    			//多选
					var opt = new $.multiOption();
					opt.setId(i + 1);
					opt.setTitle($(this).val());
					opt.setValue(i);
					opts.push(opt);
	    		}
				
			});
			newQ.setOptions(opts);
			//创建div
			if(this.container===null){
				if($("#invest_questions")){
					this.container = $("#invest_questions");
				}else{
					this.container = $('<div></div>');
					this.container.attr("id", "invest_questions");
					this.container.insertAfter("#invest_query");
				}
			}
			//div装载新增题的内容
			this.questions.push(newQ);
			this.container.append(newQ.getQuestionHTML());
    	};
    	//全选
    	this.selectAll = function(){
    		$("input[id^='sel_']").each(function(val){
    			this.checked = true;
    		});
    	};
    	//反选
    	this.selectReverse = function(){
    		$("input[id^='sel_']").each(function(val){
    			if(this.checked){
    				this.checked = false;
    			}else{
    				this.checked = true;
    			}
    		});
    	};
    	//删除选中的题目
    	this.del = function(){
    		var ids = [];
    		$("input[id^='sel_']").each(function(val){
    			if(this.checked){
    				this.parentElement.parentElement.parentElement.remove();
    				var id = this.id.replace("sel_","");
    				ids.push(id);
    			}
    		});
    		ids.sort(function(a,b){return b-a;});
    		for(var i=0;i<ids.length;i++){
    			_self.questions.splice(ids[i]-1, 1);
    		}
    		this.reorder();
    	};
    	//选中的题目
    	this.getSelectionQuestion = function(){
    		var selects = $("input[id^='sel_']");
    		var selectQuestions = [];
    		selects.each(function(i){
    			if(this.checked){
    				var id = this.id.replace("sel_","");
        			selectQuestions.push(_self.questions[Number(id-1)]);
    			}
    		});
    		return selectQuestions;
    	};
    	//重新排号
    	this.reorder = function(){
    		$("input[id^='sel_']").each(function(i){
    			if(_self.questions[i].getId()!=i+1){
    				_self.questions[i].setId(i+1);
    				//this.parentElement.innerHTML = _self.questions[i].getQuestionHTML();
    				$(this).parent().parent().parent().replaceWith(_self.questions[i].getQuestionHTML());
    			}
    		});
    	};
    	//保存更新
    	this.saveUpdate = function(editor){
    		var id = editor.updateNo;
    		var qnode = $("#" + id);
    		qnode.children().remove();
    		//增加question数组
    		if($("#questionType").val()==="single"){
    			var newQ = new $.singleQuestion();
    			newQ.setId(id);
    			newQ.setTitle($("#invest_editor_title").val());
    			var opts = [];
    			$("#invest_editor_options input").each(function(i){
    				//跳过题目，只取选项
					var opt = new $.singleOption();
					opt.setId(i + 1);
					opt.setTitle($(this).val());
					opt.setValue(i);
					opts.push(opt);
    			});
				newQ.setOptions(opts);
				
				//div装载新增题的内容
				this.questions[id-1] = newQ;
				qnode.append(newQ.getQuestionHTML());
    		}
    		$("#invest_query input").each(function(){
    			this.style.removeProperty("display");
			});
			$("#invest_upd_sav").each(function(){
				this.style.display = "none";
			});
    	};
    	//保存
    	this.save = function(evt){
    		var url = evt.target.attributes.chref.value;
    		var questions = [];
    		var qs = _self.questions;
    		for(var i=0;i<qs.length;i++){
    			var question = new Object();
    			var q = qs[i];
    			//单选还是多选
				question.type=q.getType();
    			var options = new Array();
    			var ops = q.getOptions();
    			question.title = q.getTitle();
    			for(var j=0;j<ops.length;j++){
    				options.push(ops[j].getTitle());
    			}
    			question.options = options;
    			questions.push(question);
    		};
    		$.ajax({
				url: url,
				data:{"title": $("#investtitle").text(),
					  "descn":$("#investdescn").html(),
					"questions": JSON.stringify(questions)},
				type:"POST",
				async:true,
				success:function(d){
					if(d === "success"){
						alert("保存成功");
						//$("content2").html();
						$("#content2").load(ctx + "/assessmentcenter/invest/list.do");
					}else{
						alert("出错重试");
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					console.log(XMLHttpRequest);
					console.log(textStatus);
					console.log(errorThrown);
				}
				
			});
    	};
    	//修改时保存
    	this.update = function(evt){
    		debugger;
    		var url = evt.target.attributes.chref.value;
    		var questions = [];
    		var qs = _self.questions;
    		for(var i=0;i<qs.length;i++){
    			var question = new Object();
    			var q = qs[i];
    			//单选还是多选
				question.type=q.getType();
    			var options = new Array();
    			var ops = q.getOptions();
    			question.title = q.getTitle();
    			for(var j=0;j<ops.length;j++){
    				options.push(ops[j].getTitle());
    			}
    			question.options = options;
    			questions.push(question);
    		};
    		$.ajax({
				url: url,
				data:{"title": $("#name").val(),
					"questions": JSON.stringify(questions)},
				type:"POST",
				async:true,
				success:function(d){
					if(d === "success"){
						alert("保存成功");
						//$("content2").html();
						$("#content2").load(ctx + "/assessmentcenter/invest/list.do");
					}else{
						alert("出错重试");
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					console.log(XMLHttpRequest);
					console.log(textStatus);
					console.log(errorThrown);
				}
				
			});
    	};
    	//显示到某道题
    	this.scrollTo = function(i){
    		$("#invest_questions").animate({scrollTop:$("#"+i).offset().top},1000);
    	};
    	
    }});
    
    $.extend({questionEditor:function(div){
    	_self = this;
    	this.count = 4;            //option计数
    	this.container = null;     //编辑器的div容器
    	this.container = null;  //整个编辑器的div容器
    	this.questionnire = null; //保存添加题的对象
    	this.updateNo = null;     //保存更新的题号
    	this.titleTemplate = "<div class=\"filterContent_auto\"><ul><li style=\"width:100%;text-align:left\"><label for=\"{for}\" style=\"display: inline-block;\">题目内容</label><input class=\"underline itemExtend\" style=\"width:480px\"type=\"text\" id=\"invest_editor_title\" value=\"\">"
    		+"<img src=\"" + themeUrl + "/images/add.png\" id=\"invest_addoption\" title=\"增加选项\" style=\"width:20px;height:20px;vertical-align:middle\"></ul></div>";
    	//单选题模板
    	this.singleQuestionTemplate = "<li><label for=\"{for}\" style=\"display: inline-block;\">{no}：</label>"
            + "<input class=\"underline itemExtend\" type=\"text\" value=\"\">" 
            +"<img src=\"" + themeUrl + "/images/del.png\" id=\"invest_deloption_{id}\" title=\"删除选项\" style=\"width:20px;height:20px;vertical-align:middle\"></li> ";
    	//多选题模板
    	this.multiQuestionTemplate = "<li><input type=\"checkbox\" name=\"invest_option\" id=\"editor_{id}\" value=\"{value}\" class=\" {validation}\" ><label for=\"{for}\" style=\"display: inline-block;\">{content}</label>"
            + "<input class=\"underline itemExtend\" type=\"text\" value=\"\" rel=\"{rel}\" >"
            +"<img src=\"" + themeUrl + "/images/del.png\" id=\"invest_deloption_{id}\" title=\"删除选项\" style=\"width:20px;height:20px;vertical-align:middle\"></li> ";
    	this.isSingle = function(){
    		return $("#questionType").val() === "single";
    	};
    	//增加题目
    	this.add = function(){
    		if(this.container != null && this.questionnire != null){
    			this.questionnire.add(this);
    			this.questionnire.scrollTo(this.questionnire.questions.length);
    		}
    	};
    	//显示
    	this.show = function(){
    		if(this.container){
    			this.container.children().remove();
    			if($("#invest_typeRadio").value ==="single" || $("#invest_typeRadio").value===undefined){
    				var str = "<div class=\"filterContent_auto\"><ul id=\"invest_editor_options\">";
    				str = this.titleTemplate + str;
    				for(var i=0;i<this.count;i++){
    					var tmpStr = this.singleQuestionTemplate.replace("{no}", String.fromCharCode(65 + i));
    					str = str + tmpStr.replace("{id}", i) ;
    				}
    				str = str + "</ul></div>";
    				this.container.append(str);
    			}
    			//增加题目
    			$("#invest_add").on("click", function(){
    				_self.add();
    			});
    			//删除题目
    			$("#invest_del").on("click", function(){
    				_self.questionnire.del();
    			});
    			//给增加、删除选项图片加动作
    			$("#invest_addoption").on("click", function(){
    				debugger;
    				_self.addoption();
    			});
    			$("img[id^='invest_deloption_']").on("click", function(evt){
    				_self.deloption(evt);
    			});
    			//全选动作
    			$("#invest_selall").on("click", function(){
    				_self.questionnire.selectAll();
    			});
    			//反选动作
    			$("#invest_unsel").on("click", function(){
    				_self.questionnire.selectReverse();
    			});
    			//保存更新结果
    			//反选动作
    			$("#invest_upd_sav").on("click", function(){
    				if(_self.updateNo)
    					_self.questionnire.saveUpdate(_self);
    			});
    			
    			//修改动作
    			$("#invest_upd").on("click", function(){
    				var questions = _self.questionnire.getSelectionQuestion();
    				if(questions.length > 1){
    	    			alert("只能修改一个题目");
    	    		}else if(questions.length < 1){
    	    			alert("请选择一个要修改的题目");
    	    		}else{
    	    			_self.updateNo = questions[0].getId();
    	    			_self.copy(questions[0]);
    	    			$("#invest_query input").each(function(){
    	    				this.style.display = "none";
    	    			});
    	    			$("#invest_upd_sav").each(function(){
    	    				this.style.removeProperty("display");
    	    			});
    	    		}
    			});
    			//保存问卷
    			if($("#invest_sav")){
    				$("#invest_sav").on("click", function(evt){
        				_self.questionnire.save(evt);
        			});
    			}
    			if($("#invest_savUpdate")){
    				$("#invest_savUpdate").on("click", function(evt){
        				_self.questionnire.update(evt);
        			});
    			}
    		}
    	};
    	
    	//增加option
    	this.addoption = function(){
    		//if(this.isSingle()){
    			this.count = this.count + 1;
    			var tmpStr = this.singleQuestionTemplate.replace("{no}", String.fromCharCode(64 + this.count));
    			tmpStr = tmpStr.replace("{id}", this.count) ;
    			$("#invest_editor_options").append(tmpStr) ;
    			$("#invest_deloption_" + this.count).on("click", function(evt){
    				_self.deloption(evt);
    			});
    		/*}else{
    			this.count = this.count + 1;
    		}*/
    	};
    	
    	//删除option
    	this.deloption = function(evt){
    		evt.target.parentElement.remove();
    		this.count = this.count - 1;
    		this.reorderoption();
    	};
    	
    	//重新排序option
    	this.reorderoption = function(){
    		var labels = $("#invest_editor_options li label");
    		//跳过第一个题目
    		for(var i=0;i<labels.length;i++){
    			labels[i].innerHTML = String.fromCharCode(65 + i) + "：";
    		}
    		var imgs = $("#invest_editor_options li img");
    		//跳过第一个题目
    		for(var i=0;i<imgs.length;i++){
    			imgs[i].id = "invest_deloption_" + Number(i+1);
    		}
    	};
    	
    	//拷贝
    	this.copy = function(question){
    		this.container.children().remove();
			var str = "<ul id=\"invest_editor_options\">";
			str = this.titleTemplate + str;
			this.count = question.getOptions().length;
			for(var i=0;i<this.count;i++){
				var tmpStr = this.singleQuestionTemplate.replace("{no}", String.fromCharCode(65 + i));
				str = str + tmpStr.replace("{id}", i) ;
			}
			str = str + "</ul>";
			this.container.append(str);
			var opts = question.getOptions();
			var title = question.getTitle();
			$("#invest_editor_options li input").each(function(i){
				this.value = opts[i].getTitle();
			});
			$("#invest_editor_title").val(title);
			//给增加、删除选项图片加动作
			$("#invest_addoption").on("click", function(){
				_self.addoption();
			});
			$("img[id^='invest_deloption_']").on("click", function(evt){
				_self.deloption(evt);
			});
    	};
    	
    	//进入编辑模式
    	this.updateMode = function(){
    		
    	};
    	
		if(typeof div === "string"){
			this.container = $("#" + div);
		}else if(typeof div === "object"){
			this.container = div;
		};
		if(this.container){
			//显示编辑器
			this.show();
			$("input[name='invest_typeRadio']").on("click", function(){
				alert( $(this).text() );
			});
			this.questionnire = new $.questionnire();
		};
    }});
    $.questionEditor.prototype =  new $.singleQuestion();
    
})(jQuery);

