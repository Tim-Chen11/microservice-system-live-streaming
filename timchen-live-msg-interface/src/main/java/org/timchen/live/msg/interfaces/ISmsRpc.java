package org.timchen.live.msg.interfaces;


import org.timchen.live.msg.dto.MsgCheckDTO;
import org.timchen.live.msg.enums.MsgSendResultEnum;

/**
 * @Author Tim Chen
 * @Date: Created in 17:21 2023/6/11
 * @Description
 */
public interface ISmsRpc {

    /**
     * 发送短信登录验证码接口
     *
     * @param phone
     * @return
     */
    MsgSendResultEnum sendLoginCode(String phone);

    /**
     * 校验登录验证码
     *
     * @param phone
     * @param code
     * @return
     */
    MsgCheckDTO checkLoginCode(String phone, Integer code);

}