package org.timchen.live.msg.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.timchen.live.msg.provider.dao.po.SmsPO;

/**
 * @Author: Tim Chen
 * @Date: 2:17 2024-07-29
 * @Description:
 */
@Mapper
public interface SmsMapper extends BaseMapper<SmsPO> {
}
