package com.njpes.www.entity.baseinfo;

import java.io.Serializable;

public class RoleResourcePermission implements Serializable {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column role_res_perm.id
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column role_res_perm.role_id
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    private Long roleId;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column role_res_perm.res_id
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    private Long resId;

    private Resource resource;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column role_res_perm.perm_ids
     *
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    private String permIds;

    private String permIdsNames;

    private String permIdsEngNames;

    public String getPermIdsEngNames() {
        return permIdsEngNames;
    }

    public void setPermIdsEngNames(String permIdsEngNames) {
        this.permIdsEngNames = permIdsEngNames;
    }

    public String getPermIdsNames() {
        return permIdsNames;
    }

    public void setPermIdsNames(String permIdsNames) {
        this.permIdsNames = permIdsNames;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column role_res_perm.id
     *
     * @return the value of role_res_perm.id
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column role_res_perm.id
     *
     * @param id
     *            the value for role_res_perm.id
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column role_res_perm.role_id
     *
     * @return the value of role_res_perm.role_id
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column role_res_perm.role_id
     *
     * @param roleId
     *            the value for role_res_perm.role_id
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column role_res_perm.res_id
     *
     * @return the value of role_res_perm.res_id
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    public Long getResId() {
        return resId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column role_res_perm.res_id
     *
     * @param resId
     *            the value for role_res_perm.res_id
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    public void setResId(Long resId) {
        this.resId = resId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the
     * value of the database column role_res_perm.perm_ids
     *
     * @return the value of role_res_perm.perm_ids
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    public String getPermIds() {
        return permIds;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column role_res_perm.perm_ids
     *
     * @param permIds
     *            the value for role_res_perm.perm_ids
     * @mbggenerated Tue Mar 24 16:20:48 CST 2015
     */
    public void setPermIds(String permIds) {
        this.permIds = permIds;
    }

    public String toString() {
        return "{id:" + id + ";roleId:" + roleId + ";resId:" + resId + resource.getResName() + ";permIds:" + permIds
                + ";permsIdsNames:" + permIdsNames + "}";
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

}