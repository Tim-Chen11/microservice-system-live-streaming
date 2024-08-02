package org.timchen.live.id.generate.enums;

/**
 * @Author: Tim Chen
 * @Date: 11:31 2024-07-29
 * @Description:
 */
public enum IdTypeEnum {

    USER_ID(1,"用户有序id生成策略"),
    USER_UNSEQ_ID(2, "无顺序id生成策略");

    int code;
    String desc;

    IdTypeEnum(int code, String desc) {
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
