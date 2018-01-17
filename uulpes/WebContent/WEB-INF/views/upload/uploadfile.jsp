<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>

     <form id="tf" class="form-horizontal" name="upform" method="post" enctype="multipart/form-data">  
         <div>  
         	<label for="sourceModule" class="col-sm-2 control-label"></label>  
            	<div class="nameBg">  
                  <input id="file" type="file" name="file"/><br/>
                </div> 
                <div class="loginButton" style="display: block;">    
　　                                             		<input class="button-login blue" type="button" value="提交" onclick="test();" /><br/>
						<h2><span style="color: red">注***将文件改为本校名称后再上传</span></h2>   
                </div>  
                </div>  
           </form>    
  
<script type="text/javascript">
function test(){
    var form = new FormData(document.getElementById("tf"));
    alert("数据上传中，点击确定继续上传。。。");
    $.ajax({
        url:"../../upload/uploaddata.do",
        type:"post",
        data:form,
        processData:false,
        contentType:false,
        success:function(result){
        	if (result == 1) {
                alert("数据上传成功");
                document.getElementById("tf").reset();
            } else {
            	alert("数据上传失败");
            }
        },
        error:function(e){
            alert("上传错误！请联系管理员");
            window.clearInterval(timer);
        }
    });        
    //get();//此处为上传文件的进度条
} 
</script>  
  
  
</body>  
  
</html>  