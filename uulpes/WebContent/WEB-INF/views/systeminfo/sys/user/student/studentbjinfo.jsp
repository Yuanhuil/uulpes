<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<form id="bjform" action="${ctx }/systeminfo/sys/user/student/saveStudentBjxx.do" method="post">
    <input type="hidden" name="id" value="${student.id}">
    <c:set var="currentCol" value="${0}" />
    <c:set var="colNum" value="${2}" />
    <table>
        <tr>
            <c:forEach var="attr" items="${attr_list}" varStatus="i">
                <td>${attr.label}</td>
                <td>
                    <c:choose>
                        <c:when test="${attr.type=='text'}">
                            <c:if test="${attr.field.canEdit =='disabled'}">
                                <input class="input_160" type="text" id="${attr.field.objectIdentifier }"
                                    name="${attr.field.objectIdentifier }" value="${attr.value}"/>
                            </c:if>
                            <c:if test="${attr.field.canEdit =='enabled'}">
                                <input class="input_160" type="text" id="${attr.field.objectIdentifier }"
                                    name="${attr.field.objectIdentifier }" value="${attr.value}" />
                            </c:if>
                        </c:when>
                        <c:when test="${attr.type=='select' || attr.type=='yesno'|| attr.type=='range'}">
                            <c:if test="${attr.field.canEdit =='disabled'}">
                                <select id="${attr.field.objectIdentifier }" name="${attr.field.objectIdentifier }"
                                    class="select_160" >
                                    <option value="">请选择</option>
                                    <c:forEach items="${attr.optionList}" var="option" varStatus="status">
                                        <c:choose>
                                            <c:when test="${attr.value==option.value }">
                                                <option value="${option.value}" selected="selected">${option.title }
                                                </option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${option.value}">${option.title }</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </c:if>
                            <c:if test="${attr.field.canEdit =='enabled'}">
                                <select id="${attr.field.objectIdentifier }" name="${attr.field.objectIdentifier }"
                                    class="select_160">
                                    <option value="">请选择</option>
                                    <c:forEach items="${attr.optionList}" var="option" varStatus="status">
                                        <c:choose>
                                            <c:when test="${attr.value==option.value }">
                                                <option value="${option.value}" selected="selected">${option.title }
                                                </option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${option.value}">${option.title }</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </c:if>
                        </c:when>
                        <c:when test="${attr.type=='date'}">
                            <c:if test="${attr.field.canEdit =='disabled'}">
                                <input class="dateinfo" type="text" name="${attr.field.objectIdentifier }"
                                    id="${attr.field.objectIdentifier }" value="${attr.value}"/>
                            </c:if>
                            <c:if test="${attr.field.canEdit =='enabled'}">
                                <input class="dateinfo" type="text" name="${attr.field.objectIdentifier }"
                                    id="${attr.field.objectIdentifier }" value="${attr.value}" />
                            </c:if>
                        </c:when>
                    </c:choose>
                </td>
                <c:set var="currentCol" value="${currentCol+1}" />
                <c:if test="${currentCol==colNum}">
                    <c:set var="currentCol" value="${0}" />
        </tr>
        <tr>
            </c:if>
            </c:forEach>
            <c:if test="${currentCol!=0&&currentCol < colNum}">
                <c:forEach begin="${currentCol}" end="${colNum-1}" step="1">
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </c:forEach>
            </c:if>
        </tr>
        <tr>
            <td colspan="4" style="text-align: center;">
                <input id="savebjxx" class="button-mid blue" type="button" value="保存" />
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input onclick="goback('${flag }')" class="button-mid blue" type="button" value="返回" />
            </td>
        </tr>
    </table>
</form>
<script type="text/javascript">
    $(function() {
        $(".dateinfo").datepicker({ //绑定开始日期
            dateFormat : 'yymmdd'
        });
        $("#savebjxx").click(function() {
            debugger;
            var fvs = "";
            $("[name^='fvs.fvs']").each(function(i, e) {
                if ($(this).val() != "" && $(this).val() != undefined) {
                    fvs = fvs + $(this).attr("id") + "=" + $(this).val() + ",";
                }
            });
            if (fvs && fvs.length > 0)
                fvs = fvs.substring(0, fvs.length - 1);
            $("#bjform").ajaxSubmit({
                data : {
                    "fvs" : fvs
                },
                success : function() {
                    layer.open({
                        content : '已成功修改背景信息！'
                    });
                }
            });
        });
    });

    function goback(flag) {
        var url = "${ctx }/systeminfo/sys/user/student.do";
        if (flag == 'personalCenter') {
            url = '${ctx }/systeminfo/sys/queryPersonDetail.do'
        }
        $("#content2").load(url);
    }
</script>