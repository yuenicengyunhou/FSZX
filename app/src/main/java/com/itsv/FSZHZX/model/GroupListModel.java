package com.itsv.FSZHZX.model;

import java.io.Serializable;
import java.util.List;

public class GroupListModel implements Serializable {


    private boolean success;
    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{

        private String code;
        private String name;
        private List<UserListBean> userList;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<UserListBean> getUserList() {
            return userList;
        }

        public void setUserList(List<UserListBean> userList) {
            this.userList = userList;
        }

        public static class UserListBean implements Serializable{
            /**
             * userId : 1273473258725588993
             * account : chenhaizhong
             * password :
             * name : 陈海忠
             * birthday : 1970-06-01 00:00:00
             * sex : M
             * email :
             * phone : 13651224160
             * roleId : 1270316989181411329
             * deptId : 25
             * status : ENABLE
             * avatar : https://www.fszxpt.cn:9528/api/User/previewByAvatar?avatar=1275351727210323970
             * position :
             * namePinyin : chenhaizhong
             * nameInitials : chz
             * nameInitialFirst : c
             * circles : 009001
             * party : 01
             * jieci : 1251054824296931329
             * officeTel :
             * address :
             * standingCommittee : Y
             * standingCommitteeSort : 2
             * committeeName : 01
             * committeeNumber :
             * positionName :
             */

            private long userId;
            private String account;
            private String password;
            private String name;
            private String birthday;
            private String sex;
            private String email;
            private String phone;
            private String roleId;
            private long deptId;
            private String status;
            private String avatar;
            private String position;
            private String namePinyin;
            private String nameInitials;
            private String nameInitialFirst;
            private String circles;
            private String party;
            private String jieci;
            private String officeTel;
            private String address;
            private String standingCommittee;
            private String standingCommitteeSort;
            private String committeeName;
            private String committeeNumber;
            private String positionName;
            private String nation;
            private String qualifications;
            private String placeWork;
            private String jobName;
            private String jobLevel;
            private String standingCommitteeType;


            public String getNation() {
                return nation;
            }

            public void setNation(String nation) {
                this.nation = nation;
            }

            public String getQualifications() {
                return qualifications;
            }

            public void setQualifications(String qualifications) {
                this.qualifications = qualifications;
            }

            public String getPlaceWork() {
                return placeWork;
            }

            public void setPlaceWork(String placeWork) {
                this.placeWork = placeWork;
            }

            public String getJobName() {
                return jobName;
            }

            public void setJobName(String jobName) {
                this.jobName = jobName;
            }

            public String getJobLevel() {
                return jobLevel;
            }

            public void setJobLevel(String jobLevel) {
                this.jobLevel = jobLevel;
            }

            public String getStandingCommitteeType() {
                return standingCommitteeType;
            }

            public void setStandingCommitteeType(String standingCommitteeType) {
                this.standingCommitteeType = standingCommitteeType;
            }

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getRoleId() {
                return roleId;
            }

            public void setRoleId(String roleId) {
                this.roleId = roleId;
            }

            public long getDeptId() {
                return deptId;
            }

            public void setDeptId(long deptId) {
                this.deptId = deptId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getNamePinyin() {
                return namePinyin;
            }

            public void setNamePinyin(String namePinyin) {
                this.namePinyin = namePinyin;
            }

            public String getNameInitials() {
                return nameInitials;
            }

            public void setNameInitials(String nameInitials) {
                this.nameInitials = nameInitials;
            }

            public String getNameInitialFirst() {
                return nameInitialFirst;
            }

            public void setNameInitialFirst(String nameInitialFirst) {
                this.nameInitialFirst = nameInitialFirst;
            }

            public String getCircles() {
                return circles;
            }

            public void setCircles(String circles) {
                this.circles = circles;
            }

            public String getParty() {
                return party;
            }

            public void setParty(String party) {
                this.party = party;
            }

            public String getJieci() {
                return jieci;
            }

            public void setJieci(String jieci) {
                this.jieci = jieci;
            }

            public String getOfficeTel() {
                return officeTel;
            }

            public void setOfficeTel(String officeTel) {
                this.officeTel = officeTel;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getStandingCommittee() {
                return standingCommittee;
            }

            public void setStandingCommittee(String standingCommittee) {
                this.standingCommittee = standingCommittee;
            }

            public String getStandingCommitteeSort() {
                return standingCommitteeSort;
            }

            public void setStandingCommitteeSort(String standingCommitteeSort) {
                this.standingCommitteeSort = standingCommitteeSort;
            }

            public String getCommitteeName() {
                return committeeName;
            }

            public void setCommitteeName(String committeeName) {
                this.committeeName = committeeName;
            }

            public String getCommitteeNumber() {
                return committeeNumber;
            }

            public void setCommitteeNumber(String committeeNumber) {
                this.committeeNumber = committeeNumber;
            }

            public String getPositionName() {
                return positionName;
            }

            public void setPositionName(String positionName) {
                this.positionName = positionName;
            }
        }
    }
}
