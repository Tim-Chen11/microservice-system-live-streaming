package org.timchen.live.msg.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.timchen.live.msg.dto.MsgCheckDTO;
import org.timchen.live.msg.enums.MsgSendResultEnum;
import org.timchen.live.msg.interfaces.ISmsRpc;
import org.timchen.live.msg.provider.service.ISmsService;

/**
 * @Author: Tim Chen
 * @Date: 1:37 2024-07-29
 * @Description:
 */
@DubboService
public class SmsRpcImpl implements ISmsRpc {

    @Resource
    private ISmsService smsService;

    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        return smsService.sendLoginCode(phone);
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        return smsService.checkLoginCode(phone, code);
    }

}
