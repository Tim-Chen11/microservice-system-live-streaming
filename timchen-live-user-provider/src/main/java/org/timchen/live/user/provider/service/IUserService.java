package org.timchen.live.user.provider.service;

import org.timchen.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author: Tim Chen
 * @Date: 16:30 2024-06-12
 * @Description:
 */
public interface IUserService {
    /**
     * search by user id
     *
     * @param userId
     * @return
     */
    UserDTO getByUserId(Long userId);

    /**
     * update user info
     *
     * @param userDTO
     * @return
     */
    boolean updateUserInfo(UserDTO userDTO);

    /**
     * insert user info
     *
     * @param userDTO
     * @return
     */
    boolean insertOne(UserDTO userDTO);

    /**
     * get user info batch
     *
     * @param userIdList
     * @return
     */
    Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList);
}
