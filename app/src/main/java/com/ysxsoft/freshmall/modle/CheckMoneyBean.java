package com.ysxsoft.freshmall.modle;

/**
 * Create By 胡
 * on 2019/12/3 0003
 */
public class CheckMoneyBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"yfei":20,"zcount":20}
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
         * yfei : 20
         * zcount : 20
         */

        private String yfei;
        private String zcount;

        public String getMbyou() {
            return mbyou;
        }

        public void setMbyou(String mbyou) {
            this.mbyou = mbyou;
        }

        private String mbyou;

        public String getYfei() {
            return yfei;
        }

        public void setYfei(String yfei) {
            this.yfei = yfei;
        }

        public String getZcount() {
            return zcount;
        }

        public void setZcount(String zcount) {
            this.zcount = zcount;
        }
    }
}
