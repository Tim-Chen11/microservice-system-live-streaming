package org.timchen.live.im.interfaces;

/**
 * @Author: Tim Chen
 * @Date: 16:37 2024-08-05
 * @Description:
 */
public interface ImTokenRpc {

    /**
     * 创建用户登录im服务的token
     *
     * @param userId
     * @param appId
     * @return
     */
    String createImLoginToken(long userId, int appId);

    /**
     * 根据token检索用户id
     *
     * @param token
     * @return
     */
    Long getUserIdByToken(String token);
}
