package com.itsv.FSZHZX.model;

import java.util.List;

public class QuizModel {


    /**
     * success : true
     * code : 200
     * msg : 请求成功
     * data : [{"id":"1258209097687064577","questionTitle":"测试题目二","optionA":"选A呀","optionB":"选B呀","optionC":"选C呀","optionD":"选D呀","optionTrue":"C","questionSource":"测试来源","score":1,"hintContent":"这是提示：答案是C","explainContent":"这是解析：答案是C","createTime":"2020-05-07T01:36:40.000+0000","createUserId":null,"enable":null},{"id":"1258208737543151617","questionTitle":"测试题目一","optionA":"这是a选项","optionB":"这是b选项","optionC":"这是c选项","optionD":"这是d选项","optionTrue":"B","questionSource":"测试来源","score":1,"hintContent":"这是提示：测试题目一的正确答案是选项B","explainContent":"这是解析：测试题目一的正确答案是选项B","createTime":"2020-05-07T01:35:14.000+0000","createUserId":null,"enable":null},{"id":"1265106853764636674","questionTitle":"就是放辣椒","optionA":"134","optionB":"314","optionC":"314","optionD":"34","optionTrue":"B","questionSource":"13413","score":1,"hintContent":"31413413413431423","explainContent":"13414141341341413","createTime":"2020-05-26T02:25:53.000+0000","createUserId":null,"enable":null},{"id":"1258209282278383617","questionTitle":"测试题目三","optionA":"这是a选项","optionB":"这是b选项","optionC":"这是c选项","optionD":"这是d选项","optionTrue":"D","questionSource":"测试来源","score":1,"hintContent":"这是提示呀：答案是D","explainContent":"这是解析呀：答案是D","createTime":"2020-05-07T01:37:24.000+0000","createUserId":null,"enable":null}]
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
         * id : 1258209097687064577
         * questionTitle : 测试题目二
         * optionA : 选A呀
         * optionB : 选B呀
         * optionC : 选C呀
         * optionD : 选D呀
         * optionTrue : C
         * questionSource : 测试来源
         * score : 1
         * hintContent : 这是提示：答案是C
         * explainContent : 这是解析：答案是C
         * createTime : 2020-05-07T01:36:40.000+0000
         * createUserId : null
         * enable : null
         */

        private String id;
        private String questionTitle;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String optionTrue;
        private String questionSource;
        private int score;
        private String hintContent;
        private String explainContent;
        private String createTime;
        private String createUserId;
        private boolean enable;
        private boolean answered;
        private boolean correct;
        private String hintImportantContent;


        public String getHintImportantContent() {
            return hintImportantContent;
        }

        public void setHintImportantContent(String hintImportantContent) {
            this.hintImportantContent = hintImportantContent;
        }

        public boolean isCorrect() {
            return correct;
        }

        public void setCorrect(boolean correct) {
            this.correct = correct;
        }

        public boolean isEnable() {
            return enable;
        }

        public boolean isAnswered() {
            return answered;
        }

        public void setAnswered(boolean answered) {
            this.answered = answered;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQuestionTitle() {
            return questionTitle;
        }

        public void setQuestionTitle(String questionTitle) {
            this.questionTitle = questionTitle;
        }

        public String getOptionA() {
            return optionA;
        }

        public void setOptionA(String optionA) {
            this.optionA = optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public void setOptionB(String optionB) {
            this.optionB = optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public void setOptionC(String optionC) {
            this.optionC = optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public void setOptionD(String optionD) {
            this.optionD = optionD;
        }

        public String getOptionTrue() {
            return optionTrue;
        }

        public void setOptionTrue(String optionTrue) {
            this.optionTrue = optionTrue;
        }

        public String getQuestionSource() {
            return questionSource;
        }

        public void setQuestionSource(String questionSource) {
            this.questionSource = questionSource;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getHintContent() {
            return hintContent;
        }

        public void setHintContent(String hintContent) {
            this.hintContent = hintContent;
        }

        public String getExplainContent() {
            return explainContent;
        }

        public void setExplainContent(String explainContent) {
            this.explainContent = explainContent;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public Object getEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }
}
