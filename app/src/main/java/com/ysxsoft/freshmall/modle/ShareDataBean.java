package com.ysxsoft.freshmall.modle;

public class ShareDataBean {

    /**
     * code : 0
     * msg : 返回成功
     * data : {"fxpic":"http://oto.sanzhima.cn/uploads/images/20190412/b4b2e9d0859551bca79bc54cccfdf413.png","fxtitle":"生鲜商城开业了~","fxcontent":"生鲜商城开业了~生鲜商城开业了~生鲜商城开业了~生鲜商城开业了~生鲜商城开业了~生鲜商城开业了~","fxurl":"https://www.baidu.com"}
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
         * fxpic : http://oto.sanzhima.cn/uploads/images/20190412/b4b2e9d0859551bca79bc54cccfdf413.png
         * fxtitle : 生鲜商城开业了~
         * fxcontent : 生鲜商城开业了~生鲜商城开业了~生鲜商城开业了~生鲜商城开业了~生鲜商城开业了~生鲜商城开业了~
         * fxurl : https://www.baidu.com
         */

        private String fxpic;
        private String fxtitle;
        private String fxcontent;
        private String fxurl;

        public String getFxpic() {
            return fxpic;
        }

        public void setFxpic(String fxpic) {
            this.fxpic = fxpic;
        }

        public String getFxtitle() {
            return fxtitle;
        }

        public void setFxtitle(String fxtitle) {
            this.fxtitle = fxtitle;
        }

        public String getFxcontent() {
            return fxcontent;
        }

        public void setFxcontent(String fxcontent) {
            this.fxcontent = fxcontent;
        }

        public String getFxurl() {
            return fxurl;
        }

        public void setFxurl(String fxurl) {
            this.fxurl = fxurl;
        }
    }
}
