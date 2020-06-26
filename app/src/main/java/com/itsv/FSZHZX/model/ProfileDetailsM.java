package com.itsv.FSZHZX.model;

public class ProfileDetailsM {


    /**
     * success : true
     * code : 200
     * msg : 请求成功
     * data : {"id":1,"account":"admin","name":"超级管理员","email":"sn93@qq.com","avatar":null,"avatarUrl":"https://fs.itsv.com.cn:9528/api/open/file/previewImg?fileId=1261111218942083073","deptId":25,"roleList":null,"deptName":null,"roleNames":null,"roleTips":null,"systemTypes":null,"permissions":null,"tenantCode":null,"tenantDataSourceName":null,"gender":"男","phone":null,"positionName":null,"circles":null,"circlesName":null,"party":null,"partyName":null}
     */

    private boolean success;
    private int code;
    private String msg;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * account : admin
         * name : 超级管理员
         * email : sn93@qq.com
         * avatar : null
         * avatarUrl : https://fs.itsv.com.cn:9528/api/open/file/previewImg?fileId=1261111218942083073
         * deptId : 25
         * roleList : null
         * deptName : null
         * roleNames : null
         * roleTips : null
         * systemTypes : null
         * permissions : null
         * tenantCode : null
         * tenantDataSourceName : null
         * gender : 男
         * phone : null
         * positionName : null
         * circles : null
         * circlesName : null
         * party : null
         * partyName : null
         */

        private long id;
        private String account;
        private String name;
        private String email;
        private String avatar;
        private String avatarUrl;
        private long deptId;
        private Object roleList;
        private String deptName;
        private String roleNames;
        private String roleTips;
        private String systemTypes;
        private String permissions;
        private String tenantCode;
        private String tenantDataSourceName;
        private String gender;
        private String phone;
        private String positionName;
        private String circles;
        private String circlesName;
        private String party;
        private String partyName;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public long getDeptId() {
            return deptId;
        }

        public void setDeptId(long deptId) {
            this.deptId = deptId;
        }

        public Object getRoleList() {
            return roleList;
        }

        public void setRoleList(String roleList) {
            this.roleList = roleList;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public Object getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(String roleNames) {
            this.roleNames = roleNames;
        }

        public Object getRoleTips() {
            return roleTips;
        }

        public void setRoleTips(String roleTips) {
            this.roleTips = roleTips;
        }

        public Object getSystemTypes() {
            return systemTypes;
        }

        public void setSystemTypes(String systemTypes) {
            this.systemTypes = systemTypes;
        }

        public Object getPermissions() {
            return permissions;
        }

        public void setPermissions(String permissions) {
            this.permissions = permissions;
        }

        public Object getTenantCode() {
            return tenantCode;
        }

        public void setTenantCode(String tenantCode) {
            this.tenantCode = tenantCode;
        }

        public String getTenantDataSourceName() {
            return tenantDataSourceName;
        }

        public void setTenantDataSourceName(String tenantDataSourceName) {
            this.tenantDataSourceName = tenantDataSourceName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public Object getCircles() {
            return circles;
        }

        public void setCircles(String circles) {
            this.circles = circles;
        }

        public Object getCirclesName() {
            return circlesName;
        }

        public void setCirclesName(String circlesName) {
            this.circlesName = circlesName;
        }

        public String getParty() {
            return party;
        }

        public void setParty(String party) {
            this.party = party;
        }

        public String getPartyName() {
            return partyName;
        }

        public void setPartyName(String partyName) {
            this.partyName = partyName;
        }
    }
}
