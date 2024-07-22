package org.timchen.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.timchen.live.user.provider.dao.po.UserTagPO;

/**
 * @Author: Tim Chen
 * @Date: 11:24 2024-07-19
 * @Description:
 */
@Mapper
public interface IUserTagMapper extends BaseMapper<UserTagPO> {

    /**
     *  Using bitwise operation OR to set bit for specify what tag this user has, allow only one success
     *
     * @param userId
     * @param fieldName
     * @param tag
     * @return
     */
    @Update("update t_user_tag set ${fieldName}=${fieldName} | #{tag} where user_id=#{userId} and ${fieldName} & #{tag}=0")
    int setTag(Long userId, String fieldName, long tag);


    /**
     * Using bitwise to cancel specifies tag, allow only one success
     *
     * @param userId
     * @param fieldName
     * @param tag
     * @return
     */
    @Update("update t_user_tag set ${fieldName}=${fieldName} &~ #{tag} where user_id=#{userId} and ${fieldName} & #{tag}=#{tag}")
    int cancelTag(Long userId, String fieldName, long tag);
}
