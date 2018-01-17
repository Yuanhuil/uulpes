<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
    <h3>文件上传</h3>
    <form action="${pageContext.request.contextPath}/assessmentcenter/scalemanager/importscale.do"   method="post" enctype="multipart/form-data">  
        <input type="file" name="file" />
        <input type="submit" value="上 传" />
    </form>
    <h5>上传结果：</h5>  ${resultStr}
