package com.ysxsoft.freshmall.modle;

import java.util.List;

public class MallTypeBean {

    /**
     * code : 0
     * msg : 成功
     * data : ["分类一","分类二","分类三","分类四"]
     */

    private int code;
    private String msg;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
