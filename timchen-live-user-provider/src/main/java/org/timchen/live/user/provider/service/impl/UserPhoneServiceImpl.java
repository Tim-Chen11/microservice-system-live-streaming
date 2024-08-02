package org.timchen.live.user.provider.service.impl;

import com.alibaba.nacos.api.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.idea.timchen.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.timchen.live.common.interfaces.utils.ConvertBeanUtils;
import org.timchen.live.common.interfaces.enums.CommonStatusEum;
import org.timchen.live.common.interfaces.utils.DESUtils;
import org.timchen.live.id.generate.enums.IdTypeEnum;
import org.timchen.live.id.generate.interfaces.IdGenerateRpc;
import org.timchen.live.user.dto.UserDTO;
import org.timchen.live.user.dto.UserLoginDTO;
import org.timchen.live.user.dto.UserPhoneDTO;
import org.timchen.live.user.provider.dao.mapper.IUserPhoneMapper;
import org.timchen.live.user.provider.dao.po.UserPhonePO;
import org.timchen.live.user.provider.service.IUserPhoneService;
import org.timchen.live.user.provider.service.IUserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Tim Chen
 * @Date: 10:45 2024-07-29
 * @Description:
 */
@Service
public class UserPhoneServiceImpl implements IUserPhoneService {
    @Resource
    private IUserPhoneMapper userPhoneMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private IUserService userService;
    @DubboReference
    private IdGenerateRpc idGenerateRpc;


    @Override
    public UserLoginDTO login(String phone) {
        // phone cannot be none
        if (StringUtils.isEmpty(phone)) {
            return null;
        }

        //is it registered before
        UserPhoneDTO userPhoneDTO = queryByPhone(phone);
        // if true, create token return userid
        if (userPhoneDTO != null) {
            return UserLoginDTO.loginSuccess(userPhoneDTO.getUserId());
        }
        //if not, generate user info, insert phone record, bind userid
        return registerAndLogin(phone);
    }

    private UserLoginDTO registerAndLogin(String phone) {
        Long userId = idGenerateRpc.getUnSeqId(IdTypeEnum.USER_ID.getCode());
        UserDTO userDTO = new UserDTO();
        userDTO.setNickName("user" + userId);
        userDTO.setUserId(userId);
        userService.insertOne(userDTO);
        UserPhonePO userPhonePO = new UserPhonePO();
        userPhonePO.setUserId(userId);
        userPhonePO.setPhone(DESUtils.encrypt(phone));
        userPhonePO.setStatus(CommonStatusEum.VALID_STATUS.getCode());
        System.out.println(userPhonePO);
        userPhoneMapper.insert(userPhonePO);
        redisTemplate.delete(cacheKeyBuilder.buildUserPhoneObjKey(phone));
        return UserLoginDTO.loginSuccess(userId);

    }

//    move to the gateway
//    public String createAndSaveLoginToken(Long userId) {
//        String token = UUID.randomUUID().toString();
//        //token -> xxx:xxx:token redis get userId
//        redisTemplate.opsForValue().set(cacheKeyBuilder.buildUserLoginTokenKey(token), userId, 30, TimeUnit.DAYS);
//        return token;
//    }

    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }

        String redisKey = cacheKeyBuilder.buildUserPhoneObjKey(phone);
        redisTemplate.opsForValue().get(redisKey);
        UserPhoneDTO userPhoneDTO = (UserPhoneDTO) redisTemplate.opsForValue().get(redisKey);
        if (userPhoneDTO != null) {
            //属于空值缓存对象
            if (userPhoneDTO.getUserId() == null) {
                return null;
            }
            return userPhoneDTO;
        }
        userPhoneDTO = this.queryByPhoneFromDB(phone);
        if (userPhoneDTO != null) {
            userPhoneDTO.setPhone(DESUtils.decrypt(userPhoneDTO.getPhone()));
            redisTemplate.opsForValue().set(redisKey, userPhoneDTO, 30, TimeUnit.MINUTES);
            return userPhoneDTO;
        }
        //cache breakdown, 缓存击穿，使用空值缓存解决
        userPhoneDTO = new UserPhoneDTO();
        redisTemplate.opsForValue().set(redisKey, userPhoneDTO, 5, TimeUnit.MINUTES);
        return null;
    }

    private UserPhoneDTO queryByPhoneFromDB(String phone) {
        LambdaQueryWrapper<UserPhonePO> queryWrapper = new LambdaQueryWrapper<UserPhonePO>();
        queryWrapper.eq(UserPhonePO::getPhone, DESUtils.encrypt(phone));
        queryWrapper.eq(UserPhonePO::getStatus, CommonStatusEum.VALID_STATUS.getCode());
        queryWrapper.orderByDesc(UserPhonePO::getCreateTime);
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convert(userPhoneMapper.selectOne(queryWrapper), UserPhoneDTO.class);
    }



    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        if (userId == null || userId < 10000) {
            return Collections.emptyList();
        }
        String redisKey = cacheKeyBuilder.buildUserPhoneListKey(userId);
        List<Object> userPhoneList = redisTemplate.opsForList().range(redisKey, 0, -1);
        if (!CollectionUtils.isEmpty(userPhoneList)) {
            //证明是空值缓存
            if (((UserPhoneDTO) userPhoneList.get(0)).getUserId() == null) {
                return Collections.emptyList();
            }
            return userPhoneList.stream().map(x -> (UserPhoneDTO) x).collect(Collectors.toList());
        }
        List<UserPhoneDTO> userPhoneDTOS = this.queryByUserIdFromDB(userId);
        if (!CollectionUtils.isEmpty(userPhoneDTOS)) {
            userPhoneDTOS.stream().forEach(x -> x.setPhone(DESUtils.decrypt(x.getPhone())));
            redisTemplate.opsForList().leftPushAll(redisKey, userPhoneDTOS.toArray());
            redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);
            return userPhoneDTOS;
        }
        //缓存击穿，空对象缓存
        redisTemplate.opsForList().leftPush(redisKey, new UserPhoneDTO());
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
        return Collections.emptyList();
    }

    private List<UserPhoneDTO> queryByUserIdFromDB(Long userId) {
        LambdaQueryWrapper<UserPhonePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPhonePO::getUserId, userId);
        queryWrapper.eq(UserPhonePO::getStatus, CommonStatusEum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convertList(userPhoneMapper.selectList(queryWrapper), UserPhoneDTO.class);
    }
}
