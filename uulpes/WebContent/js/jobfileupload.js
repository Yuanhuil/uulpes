		var job_uploader = WebUploader.create({
			// 自动上传。
			auto : true,
			// swf文件路径
			swf : '/js/webuploader-0.1.5/Uploader.swf',
			// 文件接收服务端。
			server : '/pes/workschedule/jobattachment/save.do',
			// 选择文件的按钮。可选。
			pick : '#picker',

		});
		job_uploader.on('uploadSuccess', function(file,res) {
			var list = res._raw;
			var element = JSON.parse(list)[0];
			var chk = $("<input  type='checkbox' name='fileuuids' checked='checked'></input>").attr("value",element.uuid);
			var a = $("<a></a>").html(element.name).attr("href",element.savePath).attr("target","_blank");
			var firstpage = $("<input type=\"button\" value=\"放置首页\" class=\"new button-small white firstpage\"/>").attr("uuid",element.uuid).attr("href","/pes/workschedule/jobattachment/" + element.uuid + "/firstpage.do");
			var op = $("<input type='button' value='删除' class='button-small white filedel new'></input>").attr("uuid",element.uuid).attr("href","/pes/workschedule/jobattachment/" + element.uuid + "/deluuid.do");
			var tr = $(".tmp").clone(true).removeClass("tmp");
			tr.find("td").first().append(chk);
			tr.find("td").eq(1).append(a);
			tr.find("td").last().append(firstpage).append(op);
			tr.attr("id",element.uuid);
			firstpage.on("click",function(){
				var href = $(this).attr("href");
				var firstpage = "1";
				if($(this).val() ==="放置首页"){
					firstpage = "1";
				}else{
					firstpage = "2";
				}
				$.ajax({
					url:href,
					data:{showfirstpage:firstpage},
					type:'post',
					success:function(s){
						if(firstpage === '1')
							$("#file_" + s + "> td>input").first().val("取消放置首页");
						else
							$("#file_" + s + "> td>input").first().val("放置首页");
					}
				});
			});
			op.on("click",function(){
				var href = $(this).attr("href");
				$.ajax({
					url:href,
					type:'post',
					success:function(){
						$("#" + element.uuid).remove();
					}
				});
			});
			$("#filelisttable").append(tr.show());
		});
		$(".button-small.white.filedel.old").on('click',function(){
			var href = $(this).attr("href");
			$.ajax({
				url:href,
				type:'post',
				success:function(s){
					$("#file_" + s).remove();
				}
			});
		});
		$(".button-small.white.firstpage.old").on('click',function(){
			var href = $(this).attr("href");
			var firstpage = "1";
			if($(this).val() ==="放置首页"){
				firstpage = "1";
			}else{
				firstpage = "2";
			}
			$.ajax({
				url:href,
				data:{showfirstpage:firstpage},
				type:'post',
				success:function(s){
					if(firstpage === '1')
						$("#file_" + s + "> td>input").first().val("取消放置首页");
					else
						$("#file_" + s + "> td>input").first().val("放置首页");
				}
			});
		});