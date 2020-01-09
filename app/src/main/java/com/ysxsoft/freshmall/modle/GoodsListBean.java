package com.ysxsoft.freshmall.modle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Create By 胡
 * on 2020/1/9 0009
 */
public class GoodsListBean implements Serializable {


    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    private ArrayList<DataBean> data;

    public static class DataBean implements Serializable{
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;
        private String pic;
        private String guige;
        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getGuige() {
            return guige;
        }

        public void setGuige(String guige) {
            this.guige = guige;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private String desc;
    }
}
