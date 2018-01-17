<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<njpes:contentHeader/>
<div class="panel">

    <form:form id="editForm" method="post" commandName="m" cssClass="form-horizontal" enctype="multipart/form-data">

        <form:hidden path="id"/>
        <form:hidden path="parentId"/>
        <form:hidden path="parentIds"/>
        <form:hidden path="resSort"/>

        <div class="filterContent-dlg">
        	<ul>
				<li><label class="name03">名称</label> 
                <form:input path="resName" cssClass="input_160 validate[required]" placeholder="小于50个字符" />
        	</li>
        	</ul>
        </div>


        <div class="filterContent-dlg">
        	<ul>
            <li><label class="name03">资源标识</label>
                <form:input path="resKey" placeholder="用于权限验证" cssClass="input_160"/></li>
             </ul>
        </div>

        <div class="filterContent-dlg">
        <ul>
            <li><label class="name03">URL地址</label>
                <form:input path="resUrl" placeholder="菜单跳转地址" cssClass="input_160"/></li>
                </ul>
        </div>



        <div class="filterContent-dlg">
             <ul>
            <li><label class="name03">是否显示</label>
            	<form:radiobutton path="isShow" value="1" label="是"/>
            	<form:radiobutton path="isShow" value="2" label="否"/></li>
            	</ul>
        </div>
        <div class="filterContent-dlg">
             <ul>
            <li><label class="name03">是否叶子</label>
            	<form:radiobutton path="isleaf" value="1" label="是"/>
            	<form:radiobutton path="isleaf" value="2" label="否"/></li>
            	</ul>
        </div>


        <div class="filterContent-dlg">
                <ul>
                <li>
               	 <input id="searchform" class="button-mid blue" type="submit"
				value="修改"></li>
                </ul>
        </div>

    </form:form>
</div>
<njpes:contentFooter/>