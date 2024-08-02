package org.timchen.live.user.provider.service;

import org.timchen.live.user.dto.UserLoginDTO;
import org.timchen.live.user.dto.UserPhoneDTO;

import java.util.List;

/**
 * @Author: Tim Chen
 * @Date: 10:43 2024-07-29
 * @Description:
 */
public interface IUserPhoneService {
    /**
     * login + register
     * @param phone
     * @return
     */
    UserLoginDTO login(String phone);

    /**
     * search info by phone
     *
     * @param phone
     * @return
     */
    UserPhoneDTO queryByPhone(String phone);

    /**
     * search phone by user id
     *
     * @param userId
     * @return
     */
    List<UserPhoneDTO> queryByUserId(Long userId);
}
