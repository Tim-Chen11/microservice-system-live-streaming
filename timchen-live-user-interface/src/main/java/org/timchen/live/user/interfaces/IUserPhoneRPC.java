package org.timchen.live.user.interfaces;

import java.util.List;
import org.timchen.live.user.dto.UserLoginDTO;
import org.timchen.live.user.dto.UserPhoneDTO;

/**
 * 用户手机相关RPC
 *
 * @Author Tim Chen
 * @Date: Created in 17:17 2023/6/13
 * @Description
 */
public interface IUserPhoneRPC {

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
