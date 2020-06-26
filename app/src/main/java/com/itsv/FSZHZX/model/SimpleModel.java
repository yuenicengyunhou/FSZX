package com.itsv.FSZHZX.model;

public class SimpleModel {


    /**
     * success : true
     * code : 200
     * msg : 请求成功
     * data : {"name":"超级管理员","positionName":null,"avatarUrl":"https://fs.itsv.com.cn:9528/api/open/file/previewImg?fileId=1261111218942083073","weekCorrectRate":"65%"}
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
         * name : 超级管理员
         * positionName : null
         * avatarUrl : https://fs.itsv.com.cn:9528/api/open/file/previewImg?fileId=1261111218942083073
         * weekCorrectRate : 65%
         */

        private String name;
        private String positionName;
        private String avatarUrl;
        private String weekCorrectRate;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getWeekCorrectRate() {
            return weekCorrectRate;
        }

        public void setWeekCorrectRate(String weekCorrectRate) {
            this.weekCorrectRate = weekCorrectRate;
        }
    }
}
