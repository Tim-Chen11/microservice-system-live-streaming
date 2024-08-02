package org.timchen.live.account.interfaces;

/**
 * @Author: Tim Chen
 * @Date: 15:59 2024-07-30
 * @Description:
 */
public interface IAccountTokenRPC {


    /**
     * 创建一个登录token
     *
     * @param userId
     * @return
     */
    String createAndSaveLoginToken(Long userId);

    /**
     * 校验用户token
     *
     * @param tokenKey
     * @return
     */
    Long getUserIdByToken(String tokenKey);

}
