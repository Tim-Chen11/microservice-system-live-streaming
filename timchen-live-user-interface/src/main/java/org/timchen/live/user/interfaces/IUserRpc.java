package org.timchen.live.user.interfaces;

import org.timchen.live.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author: Tim Chen
 * @Date: 14:15 2024-06-03
 * @Description:
 */
public interface IUserRpc {

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
