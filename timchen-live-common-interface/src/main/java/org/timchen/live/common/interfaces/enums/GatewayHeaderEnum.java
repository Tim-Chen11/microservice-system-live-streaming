package org.timchen.live.common.interfaces.enums;

/**
 * @Author: Tim Chen
 * @Date: 9:34 2024-08-01
 * @Description:
 */
public enum GatewayHeaderEnum {

    USER_LOGIN_ID("用户id","timchen_gh_user_id");

    String desc;
    String name;

    GatewayHeaderEnum(String desc, String name) {
        this.desc = desc;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }
}