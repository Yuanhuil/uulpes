<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/report.css" rel="stylesheet" type="text/css">
<title>个人报告</title>
    <%@include file="/WEB-INF/views/common/taglibs.jspf"%>
    <c:set var="baseaction" value="${pageContext.request.contextPath}/assessmentcenter/report"/>
</head>
<style>
</style>
<body>
<div class='title'>
    <ul>
        <li class='stu-report'>个人测评报告</li>
    </ul>
    <ul>
        <li class='stu-testdata'>测试日期:${page.endTime}</li>
    </ul>
</div>
    <div class='report-title'>
        <ul class='report-ul'>
            <li class='report-head'>一、基本信息</li>
        </ul>
    </div>
    <div class='information'>
        <ul class='report-ul'>
            <img src='${page.userinfo.zp}'  alt="" style="max-width:200px;max-height:150px;width:200px;height:150px;margin-top: 20px;"  />
            <li class='report-body-head'>姓名：${page.userinfo.xm }</li>
            <li class='report-msg'>姓名拼音：${page.userinfo.xmpy }</li>
            <li class='report-msg'>性别：${page.userinfo.xb }</li>
            <li class='report-msg'>民族：${page.userinfo.mz }</li>
            <li class='report-msg'>身份证号：${page.userinfo.sfzjh }</li>
            <li class='report-school'>学校：${page.userinfo.xxmc }</li>
            <li class='report-tail'>年级：${page.userinfo.njmc }</li>
            <li class='report-tail'>班级：${page.userinfo.bjmc }</li>
            <li class='report-tail'>年龄：${page.userinfo.age }</li>
            <li class='report-tail'>学号：${page.userinfo.xh }</li>
        </ul>
    </div>
    <div class='report-title'>
        <ul class='report-ul'>
            <li class='report-head'>二、量表简介</li>
        </ul>
    </div>
    <div class='scale-introduction'>
        <ul class='scale-report'>
            <li>${page.scale.descn}</li>
        </ul>
    </div>
    <div class='report-result'>
        <ul class='report-ul'>
            <li class='report-head'>三、测评结果</li>
        </ul>
    </div>
    <div class=''>
        <c:choose>
            <c:when test="${page.reportGraph=='折-1中' }">
                <div id="chart">
                    <%@include file="16pf.jsp"%>
                </div>
            </c:when>
        <c:otherwise>
            <div class="table01">
                ${page.dimScoreTable}
            </div>
            <div class="chart">
                <c:if test="${not empty page.isImage1Display && page.isImage1Display==1}"><c:if test="${not empty page.image1}"><div><img src='${baseaction}/scalechart.do?${page.image1}'/></div></c:if></c:if>
                <c:if test="${not empty page.image2}"><div><img src='${baseaction}/reportchart.do?${page.image2}'/></div></c:if>
                <c:if test="${not empty page.image3}"><div><img src='${baseaction}/reportchart.do?${page.image3}'/></div></c:if>
            </div>
        </c:otherwise>
        </c:choose>
    </div>
    <div class='report-title'>
        <ul class='report-ul'>
            <li class='report-head'>四、结果解释与指导意见</li>
        </ul>
    </div>
    <div class='evaluation'>
        <c:forEach var="dimDetail" items="${page.dimDetailList}">
            <c:if test="${not empty dimDetail.desc}"><h3>${dimDetail.dimtitle}</h3></c:if>
            <c:if test="${not empty dimDetail.desc}"><p>结果解释：${dimDetail.desc}</p></c:if>
            <c:if test="${not empty dimDetail.device}"><p>指导建议：${dimDetail.device}</p></c:if>
            <c:if test="${!empty dimDetail.subdimlist}">
                <c:forEach var="subDimDetail" items="${dimDetail.subdimlist}">
                    <p>${subDimDetail.dimtitle}</p>
                    <c:if test="${not empty subDimDetail.desc}"><p>结果解释：${subDimDetail.desc}</p></c:if>
                    <c:if test="${not empty subDimDetail.device}"><p>指导建议：${subDimDetail.device}</p></c:if>
                </c:forEach>
            </c:if>
            <c:if test="${not empty dimDetail.image}"><div class="chart"><img src='${baseaction}/${dimDetail.image}'/></div></c:if>
        </c:forEach>
        <c:if test="${not empty page.summarizedesc}"><p>总结果解释：${page.summarizedesc}</p></c:if>
        <c:if test="${not empty page.summarizedevice}"><p>总指导建议：${page.summarizedevice}</p></c:if>
    </div>
    <div class='report-title'>
        <ul class='report-ul'>
            <li class='report-head'>五、测评常见问题</li>
        </ul>
    </div>
    <div class='evaluation'>
        <p>1.心检为什么是有效的？心理学研究认为，心理特征是可以通过人们的言语、活动、表情等表现出来的。所以，通过观察和测量人们的行为活动，我们就能够了解人们的心理特征。就好像中医通过望、
                                闻、问、切了解人们的生理健康状况一样。当然，观察和测量人们的行为活动是有技巧的，必须运用先进的心理测量理论和复杂的数学统计方法，才有可能准确测量人们的心理特征。</p>
        <p>2.成均教育心检系统为什么不同于随处可见的“心理测试”？ 成均教育心检系统经历了理论建构、质性访谈、量表初次修订、量表再次修订、量表常模建立、量表信度和效度检验等复杂的开发流程。开发工作由北京师范大学
                                心理学院郑日昌教授主持，申请了教育部“八五”重点规划课题“儿童、青少年的心理卫生和心理咨询研究”、“九五”重点规划课题“学生心理健康及教育问题研究”、“十五”重点规划课题“学生心理健康教育评鉴系统”，组织
                                了22名博士、13名硕士共35名心理学专家进行了为期15年的开发研究。多名专家在国外从事过合作研究，对目前世界上最领先的心理测量理论有深切的认识，无论是理论建构还是统计方法，无论是质的研究还是量的研究，开
                                发小组所运用都是世界前沿性的技术。这就确保了产品的质量。同时，成均教育能够针对您的问题提供持续性的售后服务。与市面上博人一笑的所谓“心理测验”、“高考指导”等不可同日可语。</p>
        <p>3.为什么报告解释不太象被测者呢？影响心检结果的因素很多，我们提供的心检产品都是由个体自己填写答案选项，因此，个体对测试题目的理解直接影响到结果的解释。个体提供的答案选项可能更符合社会所认同的价值
                                观，而不是本人的客观情况。同时，我们要认识到了解人的心理是一件非常困难的事情，也许在您仔细阅读报告之后，会对被测者有个重新认识，会发现一些平时所忽略的心理特征。</p>
        <p>4.从一次心检是不是就可以判定一个人呢？人的很多方面都是不断变化的，有的变化较快，有的则相对稳定，尤其对于中小学生来说，心理的很多方面都还没有成熟，是不断发展的，一次的心检结果只能代表目前阶段学生所
                                处的水平和面临的问题。另外，任何心理测验都难免会存在一定误差，个体作答时的身体和情绪状态，作答环境的物理特征，以及个体对心检的态度等，都会影响心检结果。所以说，一次心检结果不能作为一个人的永久标签，
                                一次心检的结果不理想，或得出不太好的结论，这些并不是恒定不变的，可能是作答出现的误差，我们需要结合生活中的表现全面地看待心检的结果，并且按照我们的建议有针对性地进行改善。</p>
        <p>5.为什么有时会出现多个个体的报告有较多相同之处？在单个心检报告中，可能会发现个体在某些方面的表现是较为相似的，这是很正常的现象。 首先，心理学对心理规律的研究发现，大多数人都处于中间水平，即相差不
                                多，较为相似，而只有10％－30％的人处于分数的两极，在某一特质上表现特别优秀或特别劣势。其次，对于某两个分数（如3.7分和3.8分），我们并不能武断地因为很小的差距就判定这两位被试具有很大的差异，这样
                                的评判过于绝对、不够科学，所以我们将分数划分成类别和等级，按照等级给出相应的解释，这样对被测者可能更加公正。最后，从单个测验的结果可能难以看到个体的全面特点，所以，在多位被试的报告有较多相同之处出现
                                时，建议多做些测验，从各方面了解个体的情况，这样可能有利于区分不同个体的特点。</p>
    </div>
    <div class='report-title'>
        <ul class='report-ul'>
            <li class='report-head'>六、免责条款</li>
        </ul>
    </div>
    <div class='evaluation'>
        <p>以下说明与本报告主文一起构成本报告统一整体，不可分割：</p>
        <p>1.本心检是由被测者完全自愿接受的。</p>
        <p>2.本报告主文表示对该被测者的道德、兴趣、情绪、性格、学习能力、学习方法、学习态度、创造倾向等某一素质或综合素质进行测量和评价的结果，并不代表其它意思。</p>
        <p>3.因本报告主文内容可能引发的与任何第三人之纠纷或冲突，均由该被测者本人承担，我公司不承担任何法律责任。我公司亦不在此类纠纷或冲突中充当证人、调停人或其它形式之参与人。</p>
        <p>4.本报告不得用于非法目的，我公司不承担任何由此而发生或可能发生之法律责任。</p>
        <p>5.当本报告打印、持有、出具、展示或以其它任何形式使用时，即表明本报告之持有人或接触人已审读、理解并同意以上各条款之规定。</p>
    </div>
    <div class='report-title'>
        <ul class='report-ul'>
            <li class='report-head'>七、理论基础</li>
        </ul>
    </div>
    <div class='evaluation'>
        <p>以下说明与本报告主文一起构成本报告统一整体，不可分割：</p>
        <p>“心理评估与心理档案管理系统”是以郑日昌教授提出的“全人教育模型”为理论基础。郑日昌教授认为教育的根本目的就是育人。“人”这个字虽然简单，所含的内容却相当丰富。这个大写的人字，可以分成两部分：
                                一个生理部分(右侧的一捺)，即人的身体，或者说硬件；二是心理部分(左侧的一撇)，即人的精神，或者说软件。身体和精神的结合，构成一个完整的“人”，二者缺一不可。育人就是要从这两方面来
                                育，一是体育，即提高身体素质；一是心育，即提高心理素质。</p>
    </div>
</body>
</html>