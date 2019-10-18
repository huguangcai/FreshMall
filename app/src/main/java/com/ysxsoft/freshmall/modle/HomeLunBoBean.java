package com.ysxsoft.freshmall.modle;

import java.util.List;

public class HomeLunBoBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : [{"id":1,"banner":"http://192.168.1.163:8888/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg","title":"第一张图","time":"2019-02-22 14:59:21","type":"1"},{"id":2,"banner":"http://192.168.1.163:8888/uploads/images/20190222/038f8dbf9f271465f778efa5eea74756.jpg","title":"第二张图","time":"2019-02-22 14:59:33","type":"1"},{"id":3,"banner":"http://192.168.1.163:8888/uploads/images/20190222/92390857e55f4ab1c82de672c7fc8bb7.jpg","title":"第3张","time":"2019-02-22 14:59:43","type":"1"}]
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
         * id : 1
         * banner : http://192.168.1.163:8888/uploads/images/20190222/222fd64d72ed3c47b24bdf5862c68ae0.jpg
         * title : 第一张图
         * time : 2019-02-22 14:59:21
         * type : 1
         */

        private int id;
        private String banner;
        private String title;
        private String time;
        private String type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
