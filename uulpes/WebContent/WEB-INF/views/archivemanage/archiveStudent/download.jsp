<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<%@include file="/WEB-INF/views/common/import-css.jspf"%>
<%@include file="/WEB-INF/views/common/import-js.jspf"%>
<html>
<head>
</head>
<body>
    <div class="archive-download">
        <img class='person-img'
            src="${ctx }/themes/theme1/images/download.png"><span
            class='person-about' onclick="printdiv('download')"><a
            herf="javascript:(0)">点击下载</a></span>
    </div>
    <div id="download-item" style="width: 1050px; margin: 0 auto;">
        <div id="item-personInfo">
            <%@include file="personInfo.jsp"%>
        </div>
        <div id="item-testInfoTab"></div>
        <div id="item-recordInfoTab"></div>
        <div id="item-interventionInfoTab"></div>
    </div>
    <script>
        $("html,body").css("background-image", "initial");
        var startschoolyear = '${startschoolyear}';
        var startterm = '${startterm}';
        var endterm = '${endterm}';
        var endschoolyear = '${endschoolyear}';
        var pp = "&startyear=" + startschoolyear + "&startterm=" + startterm + "&endyear=" + endschoolyear
                + "&endterm=" + endterm;
        <c:if test="${cpDisplay=='on'}">
        var a = '/pes/archivemanage/archiveStudent/studentCompositeReport.do?resultId=2&userid='
                + $("#userid").val() + pp;
        $('#item-testInfoTab').load(a);
        </c:if>
        <c:if test="${fdDisplay=='on'}">
        var b = '/pes/archivemanage/archiveStudent/studentRecord.do?sfzjh=' + $("#sfzjh").val() + pp;
        $('#item-recordInfoTab').load(b);
        </c:if>
        <c:if test="${gyDisplay=='on'}">
        var c = '/pes/archivemanage/archiveStudent/warningIntervene.do?status=4&sfzjh=' + $("#sfzjh").val()
                + pp;
        $('#item-interventionInfoTab').load(c);
        </c:if>

        function printdiv(printpage) {
            var headstr = "<html><head><title></title></head><body>";
            var footstr = "</body></html>";
            var newstr = document.getElementById("download-item").innerHTML;
            var oldstr = document.body.innerHTML;
            document.body.innerHTML = headstr + newstr + footstr;
            window.print();
            document.body.innerHTML = oldstr;
            return false;
        }
    </script>
</body>
</html>