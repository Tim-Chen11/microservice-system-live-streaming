package org.timchen.live.im.constants;

/**
 * @Author: Tim Chen
 * @Date: 17:07 2024-08-05
 * @Description:
 */
public enum AppIdEnum {
    TIMCHEN_LIVE_BIZ(10001,"直播业务");

    int code;
    String desc;

    AppIdEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
