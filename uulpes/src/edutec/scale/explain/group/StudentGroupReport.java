package edutec.scale.explain.group;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.njpes.www.dao.scaletoollib.ExamResultMapper;

import edutec.scale.exam.ExamResult;

@Scope("prototype")
@Service("studentGroupReport")
public class StudentGroupReport {
    @Autowired
    private ExamResultMapper examResultMapper;

    public void export(Map<String, Object> paramets) {
        List<ExamResult> list = examResultMapper.getStuErForSchool(paramets);
        String gradeId = (String) paramets.get("gradeId"); // 得到分析的所有班级
        String[] classId = (String[]) paramets.get("gradeClassId"); // 得到分析的所有班级
        String scaleId = (String) paramets.get("scaleId"); // 得到量表
        String startDate = (String) paramets.get("startDate"); // 开始时间
        String endDate = (String) paramets.get("endDate"); // 结束时间
        // segStatic = SegmentStaticFactory.createSegmentStatic(scaleId, org,
        // gradeId, classId);
        // GroupReportStatic grStatic = new GroupReportStatic(scaleId, gradeId,
        // classId);
        // for (ExamResult examResult : list) {
        // grStatic.incExmresult(examResult);
        // }
        // grStatic.complete();
        // segStatic.compelet();
        // myDoc = new Document(PageSize.A4);
        // RtfWriter2.getInstance(myDoc, output);
        // myDoc.open();
    }
}
