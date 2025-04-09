package org.timchen.live.im.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.timchen.live.im.interfaces.ImTokenRpc;
import org.timchen.live.im.provider.service.ImTokenService;

/**
 * @Author: Tim Chen
 * @Date: 16:38 2024-08-05
 * @Description:
 */
@DubboService
public class ImTokenRpcImpl implements ImTokenRpc {

    @Resource
    private ImTokenService imTokenService;

    @Override
    public String createImLoginToken(long userId, int appId) {
        return imTokenService.createImLoginToken(userId,appId);
    }

    @Override
    public Long getUserIdByToken(String token) {
        return imTokenService.getUserIdByToken(token);
    }
}
