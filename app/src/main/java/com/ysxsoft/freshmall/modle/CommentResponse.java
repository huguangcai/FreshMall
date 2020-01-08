package com.ysxsoft.freshmall.modle;

/**
 * Create By 胡
 * on 2020/1/8 0008
 */
public class CommentResponse {

    /**
     * code : 200
     * data : 2
     * msg : 成功
     */

    private int code;
    private int data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
