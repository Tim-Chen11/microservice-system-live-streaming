package org.timchen.live.user.provider.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import org.timchen.live.user.constants.UserTagsEnum;
import org.timchen.live.user.interfaces.IUserTagRpc;

/**
 * @Author: Tim Chen
 * @Date: 11:19 2024-07-19
 * @Description:
 */

@DubboService
public class UserTagRpcImpl implements IUserTagRpc {
    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        return false;
    }
}
