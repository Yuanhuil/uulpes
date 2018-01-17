package edutec.scale.exam;

import java.util.Date;

public class ExamdoMs extends Examdo {
    private long stuId;
    private String stuName;
    private int gradeOrderId;
    private int typeFlag;
    private Date teacherOkTime;
    private Date parentOkTime;

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getGradeOrderId() {
        return gradeOrderId;
    }

    @Override
    public String getTitle() {
        return super.getTitle() + "-" + stuName;
    }

    public void setGradeOrderId(int gradeOrderId) {
        this.gradeOrderId = gradeOrderId;
    }

    public int getRoleFlag() {
        return typeFlag;
    }

    public void setRoleFlag(int typeFlag) {
        this.typeFlag = typeFlag;
        if (typeFlag == 2) {
            this.setScaleId("200013|" + stuId);
        } else {
            this.setScaleId("200012|" + stuId);
        }
    }

    public long getStuId() {
        return stuId;
    }

    public void setStuId(long stuId) {
        this.stuId = stuId;
    }

    public Date getTeacherOkTime() {
        return teacherOkTime;
    }

    public void setTeacherOkTime(Date teacherOkTime) {
        this.teacherOkTime = teacherOkTime;
        this.setOkTime(teacherOkTime);
    }

    public Date getParentOkTime() {
        return parentOkTime;
    }

    public void setParentOkTime(Date parentOkTime) {
        this.parentOkTime = parentOkTime;
        this.setOkTime(parentOkTime);
    }

}
