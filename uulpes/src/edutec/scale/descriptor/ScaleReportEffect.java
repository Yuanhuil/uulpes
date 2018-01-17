package edutec.scale.descriptor;

import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.scaletoollib.ExamResultMapper;
import com.njpes.www.entity.baseinfo.Student;
import com.njpes.www.entity.baseinfo.Teacher;

import edutec.group.domain.DimDescription;
import edutec.scale.questionnaire.DimDetail;
import edutec.scale.util.QuestionnaireConsts;
import heracles.util.UtilMisc;

@Component("ScaleReportEffect")
public class ScaleReportEffect {
    @Autowired
    private ExamResultMapper examResultDao;

    public ScaleReportEffect() {
    }

    public void dimExplain(DimDetail dimDetail, Descriptor descriptor) {
        Object observerUser = descriptor.getObserverUserInfo();// 看报告人
        String scaleSource = descriptor.getQuestionnaire().getScale().getSource();

        int typeFlag = 1;
        if (observerUser instanceof Student)
            typeFlag = 1;
        else if (observerUser instanceof Teacher)
            typeFlag = 2;
        else
            typeFlag = 3;
        if (!scaleSource.equals("产权量表"))
            typeFlag = 1;
        Map para = UtilMisc.toMap(Constants.SCALEID_PROP, descriptor.getQuestionnaire().getScaleId(),
                QuestionnaireConsts.WID, dimDetail.getId(), "grade", dimDetail.getDimBlk().getRank(), "typeFlag",
                typeFlag);
        // DimDescription dimDescription = DataQuery.getDescription(para);
        DimDescription dimDescription = examResultDao.getDimDescription(para);

        if (dimDescription != null) {
            String firstStr = dimDescription.getFirstStr();
            String otherStr = dimDescription.getOtherStr();
            String advice = dimDescription.getAdvice();
            if (firstStr != null)
                firstStr = getABString(firstStr);
            if (otherStr != null)
                otherStr = getABString(otherStr);
            if (advice != null)
                advice = getABString(advice);
            dimDetail.setDimDescn(firstStr + otherStr);
            dimDetail.setDimDescn1(firstStr);
            dimDetail.setDimAdvice(advice);
        } else {

            dimDetail.setDimDescn(StringUtils.defaultIfEmpty(descriptor.getDefaultText(), null));
            dimDetail.setDimAdvice(StringUtils.defaultIfEmpty(descriptor.getDefaultText(), null));
        }

    }

    public void MBTIExplain(String dimTitle, Map<Object, Object> params, Descriptor descriptor) {
        Object observerUser = descriptor.getObserverUserInfo();// 看报告人
        String scaleType = descriptor.getQuestionnaire().getScale().getScaleType();

        int typeFlag = 1;
        if (observerUser instanceof Student)
            typeFlag = 1;
        else if (observerUser instanceof Teacher)
            typeFlag = 2;
        else
            typeFlag = 3;
        if (!scaleType.equals("产权量表"))
            typeFlag = 1;
        Map para = UtilMisc.toMap(Constants.SCALEID_PROP, descriptor.getQuestionnaire().getScaleId(),
                QuestionnaireConsts.WID, dimTitle, "typeFlag", typeFlag);
        DimDescription dimDescription = examResultDao.getMBTIDescription(para);

        if (dimDescription != null) {
            String firstStr = dimDescription.getFirstStr();
            String otherStr = dimDescription.getOtherStr();
            String advice = dimDescription.getAdvice();
            if (firstStr != null)
                firstStr = getABString(firstStr);
            if (otherStr != null)
                otherStr = getABString(otherStr);
            if (advice != null)
                advice = getABString(advice);
            params.put("scaleshortname", "MBTI");
            params.put("mbtiDesripter", firstStr + otherStr);
            params.put("mbtiAdvice", advice);
        } else {
        }

    }

    private static String getABString(String str) {
        String resultStr = "";
        str = str.replaceFirst("[0-9][A-B]", "");
        String[] arr;
        boolean morethanten = false;
        if (str.contains("10A")) {
            morethanten = true;
            arr = str.split("[0-9][A]|[1-2][0-9][A]");
        } else
            arr = str.split("[0-9][A]");
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            String[] subArr;
            if (morethanten) {
                subArr = arr[i].split("[0-9][B]|[1-2][0-9][B-D]");
            } else
                subArr = arr[i].split("[0-9][B-D]");
            int index = rand.nextInt(subArr.length);
            String randomStr = subArr[index];
            resultStr += randomStr;
            // for(int j =0;j<subArr.length;j++){
            // System.out.println(subArr[j]);
            // }
        }
        // System.out.println(resultStr);
        return resultStr;
    }

    private static String getABString1(String str) {
        String resultStr = "";
        str = str.replaceAll("[0-9]", "");
        str = str.replaceFirst("[aA]", "");
        String[] arr = str.split("[aA]");
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {

            String[] subArr = arr[i].split("[a-zA-Z]");
            int index = rand.nextInt(subArr.length);
            String randomStr = subArr[index];
            resultStr += randomStr;
            // for(int j =0;j<subArr.length;j++){
            // System.out.println(subArr[j]);
            // }
        }
        return resultStr;
        // System.out.println(resultStr);
    }

}
