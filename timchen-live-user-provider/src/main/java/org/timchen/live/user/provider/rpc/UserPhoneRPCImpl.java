package org.timchen.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.timchen.live.user.dto.UserLoginDTO;
import org.timchen.live.user.dto.UserPhoneDTO;
import org.timchen.live.user.interfaces.IUserPhoneRPC;
import org.timchen.live.user.provider.service.IUserPhoneService;

import java.util.List;

/**
 * @Author: Tim Chen
 * @Date: 10:40 2024-07-29
 * @Description:
 */
@DubboService
public class UserPhoneRPCImpl implements IUserPhoneRPC {

    @Resource
    private IUserPhoneService userPhoneService;

    @Override
    public UserLoginDTO login(String phone) {
        return userPhoneService.login(phone);
    }

    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        return userPhoneService.queryByPhone(phone);
    }

    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        return userPhoneService.queryByUserId(userId);
    }
}

