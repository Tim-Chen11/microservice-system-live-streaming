package org.timchen.live.msg.provider.config;

/**
 * @Author: Tim Chen
 * @Date: 14:53 2024-07-30
 * @Description:
 */
public enum SmsTemplateIDEnum {

    SMS_LOGIN_CODE_TEMPLATE("1","登录验证码短信模版");

    String templateId;
    String desc;

    SmsTemplateIDEnum(String templateId, String desc) {
        this.templateId = templateId;
        this.desc = desc;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getDesc() {
        return desc;
    }
}
