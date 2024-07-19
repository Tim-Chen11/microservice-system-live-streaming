package org.timchen.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.timchen.live.user.provider.dao.po.UserPO;

/**
 * @Author: Tim Chen
 * @Date: 16:23 2024-06-12
 * @Description:
 */
@Mapper
public interface IUserMapper extends BaseMapper<UserPO> {
}
