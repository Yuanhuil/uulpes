<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="" class="p-title-content">
    <input type="text" id="userid" value="${entity.id}" style="display:none">
    <input type="text" id="sfzjh" value="${entity.sfzjh}" style="display:none">
         <div class='div-person'>
            <div class='person-msg'>
                <div class='person-aboutme'>
                    <img class='person-img' src="${ctx }/themes/theme1/images/stumsg_01.png">
                    <span class='person-about' >关于我</span>
                </div>
                <div class='student-archive-one'>
                    <ul>
                        <li class='teacher-msg-two'>真实姓名：${entity.xm}</li>
                        <c:if test="${entity.xbm==1 }">
                            <li class='teacher-msg'>性别：男</li>
                        </c:if>
                        <c:if test="${entity.xbm==2 }">
                            <li class='teacher-msg'>性别：女</li>
                        </c:if>
                        <li class='teacher-msg'>身份证号：${enity.sfzjh }</li>
                        <li class='teacher-msg'>工号：${enity.gh }</li>
                    </ul>
                </div>
            </div>
            <div class='stu-study'>
                <div class='stu-msg-study'>
                    <img class='person-images' src="${ctx }/themes/theme1/images/stumsg_02.png">
                    <span class='student-study' >学校信息</span>
                </div>
                <div class='student-archive-one'>
                    <ul>
                        <div class='student-archive-msg'>
                        <li class='teacher-msg-two'>学校名称：${entity.xxmc }</li>
                        <li class='teacher-msg'>角色名称：${entity.rolename }</li>
                        <li class='teacher-msg'>联系电话：${entity.lxdh }</li>
                        <li class='teacher-msg'>电子信箱：${entity.dzxx }</li>
                        </div>
                    </ul>
                </div>
            </div>
        </div>
</div>
