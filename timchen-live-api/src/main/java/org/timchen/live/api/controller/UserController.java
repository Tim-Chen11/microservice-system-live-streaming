package org.timchen.live.api.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.timchen.live.user.dto.UserDTO;
import org.timchen.live.user.interfaces.IUserRpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Tim Chen
 * @Date: 22:27 2024-06-03
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @DubboReference
    private IUserRpc userRpc;

    @GetMapping("/getUserInfo")
    public UserDTO getUserInfo(Long userId){
        return userRpc.getByUserId(userId);
    }

    @GetMapping("/batchQueryUserInfo")
    public Map<Long, UserDTO> batchQueryUserInfo(String userIdStr){
        String[] idStr = userIdStr.split(",");
        List<Long> userIdList = new ArrayList<>();
        for(String userId:idStr){
            userIdList.add(Long.valueOf(userId));
        }
        return userRpc.batchQueryUserInfo(userIdList);
    }

    @GetMapping("/updateUserInfo")
    public boolean updateUserInfo(Long userId, String nickname){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName(nickname);
        return userRpc.updateUserInfo(userDTO);
    }

    @GetMapping("/insertOne")
    public boolean insertOne(Long userId){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName("idea-test");
        userDTO.setSex(1);
        return userRpc.insertOne(userDTO);
    }
}
