<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
    <h3>文件上传</h3>
    <form action="${pageContext.request.contextPath}/scaletoollib/importscale.do"   method="post" enctype="multipart/form-data">  
        <input type="file" name="file" />
        <input type="submit" value="上 传" />
    </form>
    <h5>上传结果：</h5>  ${resultStr}
</body>  
</html>