package com.ysxsoft.freshmall.modle;

public class UserRulesDetailBean {

    /**
     * code : 0
     * msg : 返回成功
     * data : {"id":1,"title":"阿萨德发生","content":"的说法是否","time":"2019-04-12 11:40:40"}
     */

    private int code;
    private String msg;
    private DataBean data;

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
         * title : 阿萨德发生
         * content : 的说法是否
         * time : 2019-04-12 11:40:40
         */

        private String id;
        private String title;
        private String content;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
