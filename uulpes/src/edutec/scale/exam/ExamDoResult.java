package edutec.scale.exam;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.njpes.www.entity.baseinfo.organization.Organization;

import edutec.scale.db.CachedScaleMgr;
import edutec.scale.model.Scale;

public class ExamDoResult implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long userId;
    private int scaleId;
    private int roleFlag;
    private Date okTime;
    private String name;
    private int gender;
    private long orgId;
    private int gradeOrderId;
    private int classId;
    private String classTitle;
    private Organization organization;

    @Autowired
    public CachedScaleMgr cachedScaleMgr;

    public ExamDoResult(Organization organization) {
        this.organization = organization;
    }

    public ExamDoResult() {
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
        this.setOrgId(organization.getId());
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getScaleId() {
        return scaleId;
    }

    public void setScaleId(int scaleId) {
        this.scaleId = scaleId;
    }

    public Date getOkTime() {
        return okTime;
    }

    public void setOkTime(Date okTime) {
        this.okTime = okTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public int getGradeOrderId() {
        return gradeOrderId;
    }

    public void setGradeOrderId(int gradeOrderId) {
        this.gradeOrderId = gradeOrderId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classOrderId) {
        this.classId = classOrderId;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getGradeName() {
        // return
        // organization.getSchoolPartEnum().getGradeName(getGradeOrderId()+"");
        return "";
    }

    public String getScaleTitle() {
        Scale scale = cachedScaleMgr.get(scaleId + "");
        return scale.getTitle();
    }

    public int getRoleFlag() {
        return roleFlag;
    }

    public void setRoleFlag(int roleFlag) {
        this.roleFlag = roleFlag;
    }

    public String getRoleName() {
        // return RoleFlags.ROLE_DESCN.get(getRoleFlag()+"");
        return "";
    }
}
