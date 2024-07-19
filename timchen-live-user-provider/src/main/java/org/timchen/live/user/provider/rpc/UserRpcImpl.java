package org.timchen.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.timchen.live.user.dto.UserDTO;
import org.timchen.live.user.interfaces.IUserRpc;
import org.timchen.live.user.provider.service.IUserService;

import java.util.List;
import java.util.Map;

/**
 * @Author: Tim Chen
 * @Date: 14:16 2024-06-03
 * @Description:
 */

@DubboService
public class UserRpcImpl implements IUserRpc {

    @Resource
    private IUserService userService;


    @Override
    public UserDTO getByUserId(Long userId) {
        return userService.getByUserId(userId);
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        return userService.insertOne(userDTO);
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        return userService.batchQueryUserInfo(userIdList);
    }
}
