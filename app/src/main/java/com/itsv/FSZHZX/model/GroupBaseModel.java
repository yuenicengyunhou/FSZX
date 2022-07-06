package com.itsv.FSZHZX.model;

import java.io.Serializable;
import java.util.List;

public class GroupBaseModel implements Serializable {


    /**
     * success : true
     * code : 200
     * msg : 请求成功
     * data : [{"groupType":"2","groupSort":10,"parameter":"第十届","id":1249956020193599489},{"groupType":"2","groupSort":11,"parameter":"第十一届","id":1251054824296931329},{"groupType":"2","groupSort":12,"parameter":"第十二届","id":1250504167202508801}]
     */

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

    public static class DataBean {
        /**
         * groupType : 2
         * groupSort : 10
         * parameter : 第十届
         * id : 1249956020193599489
         */

        private String groupType;
        private int groupSort;
        private String parameter;
        private String id;

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
        }

        public int getGroupSort() {
            return groupSort;
        }

        public void setGroupSort(int groupSort) {
            this.groupSort = groupSort;
        }

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
