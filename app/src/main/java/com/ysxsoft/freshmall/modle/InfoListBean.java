package com.ysxsoft.freshmall.modle;

import java.util.List;

public class InfoListBean {

    /**
     * code : 0
     * msg : 获取成功
     * pages : 1
     * data : [{"id":1,"title":"111","content":"111111","uid":18,"read":1,"add_time":"2019-04-03 14:29:05"},{"id":2,"title":"222","content":"2131312","uid":0,"read":1,"add_time":"2019-04-03 14:29:39"}]
     */

    private int code;
    private String msg;
    private int pages;
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

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 111
         * content : 111111
         * uid : 18
         * read : 1
         * add_time : 2019-04-03 14:29:05
         */

        private String id;
        private String title;
        private String content;
        private String uid;
        private String read;
        private String add_time;

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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getRead() {
            return read;
        }

        public void setRead(String read) {
            this.read = read;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
