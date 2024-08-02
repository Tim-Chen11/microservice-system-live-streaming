package org.timchen.live.common.interfaces.enums;

/**
 * @Author: Tim Chen
 * @Date: 11:01 2024-07-29
 * @Description:
 */
public enum CommonStatusEum {

    INVALID_STATUS(0,"无效"),
    VALID_STATUS(1,"有效");

    CommonStatusEum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    int code;
    String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
