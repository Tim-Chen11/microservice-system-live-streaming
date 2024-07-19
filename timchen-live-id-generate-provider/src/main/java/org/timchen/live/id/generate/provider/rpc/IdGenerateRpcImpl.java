package org.timchen.live.id.generate.provider.rpc;

import com.baomidou.mybatisplus.extension.ddl.history.IDdlGenerator;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.timchen.live.id.generate.interfaces.IdGenerateRpc;
import org.timchen.live.id.generate.provider.service.IdGenerateService;

/**
 * @Author: Tim Chen
 * @Date: 13:40 2024-07-15
 * @Description:
 */
@DubboService
public class IdGenerateRpcImpl implements IdGenerateRpc {

    @Resource
    private IdGenerateService idGenerateService;


    @Override
    public Long getSeqId(Integer id) {
        return idGenerateService.getSeqId(id);
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return idGenerateService.getUnSeqId(id);
    }
}
