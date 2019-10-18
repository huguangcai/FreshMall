package com.ysxsoft.freshmall.modle;

import java.util.List;

public class UserRulesBean {


    /**
     * code : 0
     * msg : 成功
     * data : [{"id":2,"title":"仿佛大法师的","content":"阿萨德发生的发生阿萨德发生的发生的","time":"2019-04-12 13:38:24"},{"id":1,"title":"阿萨德发生","content":"的说法是否","time":"2019-04-12 11:40:40"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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
         * id : 2
         * title : 仿佛大法师的
         * content : 阿萨德发生的发生阿萨德发生的发生的
         * time : 2019-04-12 13:38:24
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
