package com.itsv.FSZHZX.model;

import java.util.List;

public class MeetingDetaisModel {


    /**
     * success : true
     * code : 200
     * msg : 请求成功
     * data : {"id":1257854794816348161,"meetingTitle":"对公司的故事大概","meetingStartTime":"2020-05-06 04:07:41","meetingDuration":"30","meetingEndTime":null,"meetingContent":"对公司的故事","meetingOwnerUserId":1,"meetingOwnerUserName":"超级管理员","meetingOwnerPassword":"123456","meetingFileids":"1257854723051806722","createUserId":1,"createUserName":null,"createTime":"2020-05-06 02:08:48","enabled":"0","sdkRoomNum":"182500141","sdkConferenceUrl":"https://bj189.scmeeting.com/index.html#/meeting/182500141/IA==","sdkCid":"76EEF0D006E01B02","meetingUserList":[{"id":1257854794816348162,"meetingBaseId":1257854794816348161,"meetingUserId":1,"isCanhui":null,"isQiandao":null,"qiandaoTime":null,"content":null,"name":"超级管理员","phone":"18200000000","deptName":"开发部","meetingBaseResult":null}],"fileList":[{"fileId":"1257854723051806722","fileBucket":"NORMAL","fileName":"首页01.html","fileSuffix":"html","fileSizeKb":10,"finalName":"1257854723051806722.html","filePath":"D:/project/file/\\1257854723051806722.html","createTime":"2020-05-06 02:08:30","updateTime":null,"createUser":1,"updateUser":null,"groupName":null}]}
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
         * id : 1257854794816348161
         * meetingTitle : 对公司的故事大概
         * meetingStartTime : 2020-05-06 04:07:41
         * meetingDuration : 30
         * meetingEndTime : null
         * meetingContent : 对公司的故事
         * meetingOwnerUserId : 1
         * meetingOwnerUserName : 超级管理员
         * meetingOwnerPassword : 123456
         * meetingFileids : 1257854723051806722
         * createUserId : 1
         * createUserName : null
         * createTime : 2020-05-06 02:08:48
         * enabled : 0
         * sdkRoomNum : 182500141
         * sdkConferenceUrl : https://bj189.scmeeting.com/index.html#/meeting/182500141/IA==
         * sdkCid : 76EEF0D006E01B02
         * meetingUserList : [{"id":1257854794816348162,"meetingBaseId":1257854794816348161,"meetingUserId":1,"isCanhui":null,"isQiandao":null,"qiandaoTime":null,"content":null,"name":"超级管理员","phone":"18200000000","deptName":"开发部","meetingBaseResult":null}]
         * fileList : [{"fileId":"1257854723051806722","fileBucket":"NORMAL","fileName":"首页01.html","fileSuffix":"html","fileSizeKb":10,"finalName":"1257854723051806722.html","filePath":"D:/project/file/\\1257854723051806722.html","createTime":"2020-05-06 02:08:30","updateTime":null,"createUser":1,"updateUser":null,"groupName":null}]
         */

        private long id;
        private String meetingTitle;
        private String meetingStartTime;
        private String meetingDuration;
        private String meetingEndTime;
        private String meetingContent;
        private long meetingOwnerUserId;
        private String meetingOwnerUserName;
        private String meetingOwnerPassword;
        private String meetingFileids;
        private long createUserId;
        private String createUserName;
        private String createTime;
        private String enabled;
        private String sdkRoomNum;
        private String sdkConferenceUrl;
        private String sdkCid;
        private List<MeetingUserListBean> meetingUserList;
        private List<FileListBean> fileList;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getMeetingTitle() {
            return meetingTitle;
        }

        public void setMeetingTitle(String meetingTitle) {
            this.meetingTitle = meetingTitle;
        }

        public String getMeetingStartTime() {
            return meetingStartTime;
        }

        public void setMeetingStartTime(String meetingStartTime) {
            this.meetingStartTime = meetingStartTime;
        }

        public String getMeetingDuration() {
            return meetingDuration;
        }

        public void setMeetingDuration(String meetingDuration) {
            this.meetingDuration = meetingDuration;
        }

        public Object getMeetingEndTime() {
            return meetingEndTime;
        }

        public void setMeetingEndTime(String meetingEndTime) {
            this.meetingEndTime = meetingEndTime;
        }

        public String getMeetingContent() {
            return meetingContent;
        }

        public void setMeetingContent(String meetingContent) {
            this.meetingContent = meetingContent;
        }

        public long getMeetingOwnerUserId() {
            return meetingOwnerUserId;
        }

        public void setMeetingOwnerUserId(long meetingOwnerUserId) {
            this.meetingOwnerUserId = meetingOwnerUserId;
        }

        public String getMeetingOwnerUserName() {
            return meetingOwnerUserName;
        }

        public void setMeetingOwnerUserName(String meetingOwnerUserName) {
            this.meetingOwnerUserName = meetingOwnerUserName;
        }

        public String getMeetingOwnerPassword() {
            return meetingOwnerPassword;
        }

        public void setMeetingOwnerPassword(String meetingOwnerPassword) {
            this.meetingOwnerPassword = meetingOwnerPassword;
        }

        public String getMeetingFileids() {
            return meetingFileids;
        }

        public void setMeetingFileids(String meetingFileids) {
            this.meetingFileids = meetingFileids;
        }

        public long getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(long createUserId) {
            this.createUserId = createUserId;
        }

        public String getCreateUserName() {
            return createUserName;
        }

        public void setCreateUserName(String createUserName) {
            this.createUserName = createUserName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }

        public String getSdkRoomNum() {
            return sdkRoomNum;
        }

        public void setSdkRoomNum(String sdkRoomNum) {
            this.sdkRoomNum = sdkRoomNum;
        }

        public String getSdkConferenceUrl() {
            return sdkConferenceUrl;
        }

        public void setSdkConferenceUrl(String sdkConferenceUrl) {
            this.sdkConferenceUrl = sdkConferenceUrl;
        }

        public String getSdkCid() {
            return sdkCid;
        }

        public void setSdkCid(String sdkCid) {
            this.sdkCid = sdkCid;
        }

        public List<MeetingUserListBean> getMeetingUserList() {
            return meetingUserList;
        }

        public void setMeetingUserList(List<MeetingUserListBean> meetingUserList) {
            this.meetingUserList = meetingUserList;
        }

        public List<FileListBean> getFileList() {
            return fileList;
        }

        public void setFileList(List<FileListBean> fileList) {
            this.fileList = fileList;
        }

        public static class MeetingUserListBean {
            /**
             * id : 1257854794816348162
             * meetingBaseId : 1257854794816348161
             * meetingUserId : 1
             * isCanhui : null
             * isQiandao : null
             * qiandaoTime : null
             * content : null
             * name : 超级管理员
             * phone : 18200000000
             * deptName : 开发部
             * meetingBaseResult : null
             */

            private long id;
            private long meetingBaseId;
            private long meetingUserId;
            private String isCanhui;
            private String isQiandao;
            private String qiandaoTime;
            private String content;
            private String name;
            private String phone;
            private String deptName;
            private Object meetingBaseResult;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getMeetingBaseId() {
                return meetingBaseId;
            }

            public void setMeetingBaseId(long meetingBaseId) {
                this.meetingBaseId = meetingBaseId;
            }

            public long getMeetingUserId() {
                return meetingUserId;
            }

            public void setMeetingUserId(long meetingUserId) {
                this.meetingUserId = meetingUserId;
            }

            public String getIsCanhui() {
                return isCanhui;
            }

            public void setIsCanhui(String isCanhui) {
                this.isCanhui = isCanhui;
            }

            public String getIsQiandao() {
                return isQiandao;
            }

            public void setIsQiandao(String isQiandao) {
                this.isQiandao = isQiandao;
            }

            public String getQiandaoTime() {
                return qiandaoTime;
            }

            public void setQiandaoTime(String qiandaoTime) {
                this.qiandaoTime = qiandaoTime;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDeptName() {
                return deptName;
            }

            public void setDeptName(String deptName) {
                this.deptName = deptName;
            }

            public Object getMeetingBaseResult() {
                return meetingBaseResult;
            }

            public void setMeetingBaseResult(Object meetingBaseResult) {
                this.meetingBaseResult = meetingBaseResult;
            }
        }

        public static class FileListBean {
            /**
             * fileId : 1257854723051806722
             * fileBucket : NORMAL
             * fileName : 首页01.html
             * fileSuffix : html
             * fileSizeKb : 10
             * finalName : 1257854723051806722.html
             * filePath : D:/project/file/\1257854723051806722.html
             * createTime : 2020-05-06 02:08:30
             * updateTime : null
             * createUser : 1
             * updateUser : null
             * groupName : null
             */

            private String fileId;
            private String fileBucket;
            private String fileName;
            private String fileSuffix;
            private int fileSizeKb;
            private String finalName;
            private String filePath;
            private String createTime;
            private Object updateTime;
            private int createUser;
            private Object updateUser;
            private Object groupName;

            public String getFileId() {
                return fileId;
            }

            public void setFileId(String fileId) {
                this.fileId = fileId;
            }

            public String getFileBucket() {
                return fileBucket;
            }

            public void setFileBucket(String fileBucket) {
                this.fileBucket = fileBucket;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getFileSuffix() {
                return fileSuffix;
            }

            public void setFileSuffix(String fileSuffix) {
                this.fileSuffix = fileSuffix;
            }

            public int getFileSizeKb() {
                return fileSizeKb;
            }

            public void setFileSizeKb(int fileSizeKb) {
                this.fileSizeKb = fileSizeKb;
            }

            public String getFinalName() {
                return finalName;
            }

            public void setFinalName(String finalName) {
                this.finalName = finalName;
            }

            public String getFilePath() {
                return filePath;
            }

            public void setFilePath(String filePath) {
                this.filePath = filePath;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }

            public int getCreateUser() {
                return createUser;
            }

            public void setCreateUser(int createUser) {
                this.createUser = createUser;
            }

            public Object getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(Object updateUser) {
                this.updateUser = updateUser;
            }

            public Object getGroupName() {
                return groupName;
            }

            public void setGroupName(Object groupName) {
                this.groupName = groupName;
            }
        }
    }
}
