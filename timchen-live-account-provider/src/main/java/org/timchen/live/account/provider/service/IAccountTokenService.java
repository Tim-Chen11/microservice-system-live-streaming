package org.timchen.live.account.provider.service;

/**
 * @Author: Tim Chen
 * @Date: 16:50 2024-07-30
 * @Description:
 */
public interface IAccountTokenService {
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

