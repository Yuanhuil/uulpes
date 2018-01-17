<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div id="" class="p-title-content">
    <input type="text" id="userid" value="${entity.id}" style="display:none">
    <input type="text" id="sfzjh" value="${entity.sfzjh}" style="display:none">
    <c:set var="currentCol" value="${0}"/>
    <c:set var="colNum" value="${3}"/>
        <div class='div-person'>
            <div class='person-msg'>
                <div class='person-aboutme'>
                    <img class='person-img' src="${ctx }/themes/theme1/images/stumsg_01.png">
                    <span class='person-about' >关于我</span>
                </div>
                <div class='student-archive-one'>
                    <ul>
                        <li class='teacher-msg-two'>姓名：${entity.xm}</li>
                        <c:if test="${entity.xbm==1 }">
                            <li class='teacher-msg'>性别：男</li>
                        </c:if>
                        <c:if test="${entity.xbm==2 }">
                            <li class='teacher-msg'>性别：女</li>
                        </c:if>
                        <li class='teacher-msg'>民族：${bjxx['102'] }</li>
                        <li class='teacher-msg'>籍贯：${bjxx['103'] }</li>
                    </ul>
                </div>
            </div>
            <div class='stu-study'>
                <div class='stu-msg-study'>
                    <img class='person-images' src="${ctx }/themes/theme1/images/stumsg_02.png">
                    <span class='student-study' >学习信息</span>
                </div>
                <div class='student-archive-one'>
                    <ul>
                        <div class='student-archive-msg'>
                        <li class='teacher-msg-two'>学号：${entity.xh }</li>
                        <li class='teacher-msg'>年级名称：${entity.njmc }</li>
                        <li class='teacher-msg'>班级名称：${entity.bjmch }</li>
                        <li class='teacher-msg'>入学年月：${entity.rxny }</li>
                        </div>
                        <div class='student-archive-msg'>
                            <li class='msg-student'>学生类别：${bjxx['204'] }</li>
                            <li class='msg-student'>学生成绩：${bjxx['203'] }</li>
                            <li class='msg-student'>就学方式：${bjxx['205'] }</li>
                            <li class='msg-student'>学生来源：${bjxx['220'] }</li>
                        </div>
                    </ul>
                </div>
            </div>
        </div>
        <div class='msg-tel'>
            <div class='person-msg'>
                <div class='person-aboutme'>
                    <img class='person-img' src="${ctx }/themes/theme1/images/stumsg_03.png">
                    <span class='person-about' >联系方式</span>
                </div>
                <div class='student-archive-one'>
                    <ul>
                        <li class='teacher-msg-two'>联系电话：${bjxx['108'] }</li>
                        <li class='teacher-msg'>电子信箱：${bjxx['111'] }</li>
                        <li class='teacher-msg'>通讯地址：${bjxx['109'] }</li>
                        <li class='teacher-msg'>邮政编码：${bjxx['110'] }</li>
                    </ul>
                </div>
            </div>
            <div class='stu-study-msg'>
                <div class='stu-msg-study'>
                    <img class='person-img' src="${ctx }/themes/theme1/images/stumsg_04.png">
                    <span class='person-about' >健康状况</span>
                </div>
                <div class='student-archive-two'>
                    <ul>
                        <div class='student-archive-msg'>
                            <li class='teacher-msg-two'>血型：${bjxx['106'] }</li>
                            <li class='teacher-msg'>身高：${bjxx['201'] }</li>
                            <li class='teacher-msg'>体重：${bjxx['202'] }</li>
                            <li class='teacher-msg'>特长：${bjxx['107'] }</li>
                        </div>
                    </ul>
                </div>
                <div class='msg-stu-category'>
                        <li class='msg-student'>出生日期：${bjxx['104'] }</li>
                        <li class='msg-student'>身体状况：${bjxx['105'] }</li>
                        <li class='msg-student'>学生体质达标：${bjxx['219'] }</li>
                </div>
            </div>
        </div>
        <div>
            <div class='div-person'>
            <div class='stu-status'>
                <div class='person-aboutme'>
                    <img class='person-img' src="${ctx }/themes/theme1/images/stumsg_05.png">
                    <span class='person-about' >家庭状况</span>
                </div>
                <div class='father-msg'>
                    <li class='teacher-msg-two'>父亲职业：${bjxx['206']}</li>
                    <li class='teacher-msg'>父亲文化程度：${bjxx['207'] }</li>
                    <li class='teacher-msg'>父亲健康状况：${bjxx['216'] }</li>
                    <li class='teacher-msg'>母亲职业：${bjxx['208'] }</li>
                </div>
                <div class='mother-msg'>
                        <li class='msg-student'>母亲文化程度：${bjxx['209'] }</li>
                        <li class='msg-student'>母亲健康状况：${bjxx['217'] }</li>
                        <li class='msg-student'>家庭状况：${bjxx['218'] }</li>
                        <li class='msg-student'>独生子女：${bjxx['215'] }</li>
                </div>
                <div class='mother-msg'>
                        <li class='msg-student'>离异家庭子女：${bjxx['210'] }</li>
                        <li class='msg-student'>单亲家庭子女：${bjxx['211'] }</li>
                        <li class='msg-student'>再婚家庭子女：${bjxx['212'] }</li>
                        <li class='msg-student'>来城务工农民子女：${bjxx['213'] }</li>
                </div>
                <div class='home-msg'>
                        <li class='msg-student'>流动人口家庭子女：${bjxx['214'] }</li>
                        <li class='msg-student'>家庭困难程度：${bjxx['221'] }</li>
                </div>
            </div>
        </div>
        </div>
</div>
