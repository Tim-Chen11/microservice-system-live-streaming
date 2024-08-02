package org.timchen.live.account.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.timchen.live.account.interfaces.IAccountTokenRPC;
import org.timchen.live.account.provider.service.IAccountTokenService;

/**
 * @Author: Tim Chen
 * @Date: 16:47 2024-07-30
 * @Description:
 */
@DubboService
public class AccountTokenRPCImpl implements IAccountTokenRPC {

    @Resource
    private IAccountTokenService accountTokenService;

    @Override
    public String createAndSaveLoginToken(Long userId) {
        return accountTokenService.createAndSaveLoginToken(userId);
    }

    @Override
    public Long getUserIdByToken(String tokenKey) {
        return accountTokenService.getUserIdByToken(tokenKey);
    }
}
