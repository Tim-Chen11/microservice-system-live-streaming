package org.timchen.live.user.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.idea.timchen.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.timchen.live.common.interfaces.utils.ConvertBeanUtils;
import org.timchen.live.user.constants.CacheAsyncDeleteCode;
import org.timchen.live.user.constants.UserProviderTopicNames;
import org.timchen.live.user.dto.UserCacheAsyncDeleteDTO;
import org.timchen.live.user.dto.UserDTO;
import org.timchen.live.user.provider.dao.mapper.IUserMapper;
import org.timchen.live.user.provider.dao.po.UserPO;
import org.timchen.live.user.provider.service.IUserService;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Tim Chen
 * @Date: 16:32 2024-06-12
 * @Description:
 */

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;

    @Resource
    private UserProviderCacheKeyBuilder userProviderCacheKeyBuilder;

    @Resource
    private MQProducer mqProducer;

    @Override
    public UserDTO getByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        String key = userProviderCacheKeyBuilder.buildUserInfoKey(userId);
        // First get from redis
        UserDTO userDTO = redisTemplate.opsForValue().get(key);
        if (userDTO != null) {
            return userDTO;
        }
        // No data in redis then get from mysql
        userDTO = ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
        if (userDTO != null) {
            redisTemplate.opsForValue().set(key, userDTO, 30, TimeUnit.MINUTES);
        }
        return userDTO;
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }
        int updateStatus = userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));

        if (updateStatus > -1) {
            String key = userProviderCacheKeyBuilder.buildUserInfoKey(userDTO.getUserId());
            redisTemplate.delete(key);

            UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = new UserCacheAsyncDeleteDTO();
            userCacheAsyncDeleteDTO.setCode(CacheAsyncDeleteCode.USER_INFO_DELETE.getCode());
            Map<String, Object> jsonParam = new HashMap<>();
            jsonParam.put("userId", userDTO.getUserId());
            userCacheAsyncDeleteDTO.setJson(JSON.toJSONString(jsonParam));

            try {
                Message message = new Message();
                message.setTopic(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC);
                message.setBody(JSON.toJSONString(userCacheAsyncDeleteDTO).getBytes());
                // 1 means delay 1 second to send
                message.setDelayTimeLevel(1);
                mqProducer.send(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        if (userDTO == null || userDTO.getUserId() == null) {
            return false;
        }

        userMapper.insert(ConvertBeanUtils.convert(userDTO, UserPO.class));
        return true;
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return Maps.newHashMap();
        }
        userIdList = userIdList.stream().filter(id -> id > 1000).collect(Collectors.toList());
        if (CollectionUtils.isEmpty((userIdList))) {
            return Maps.newHashMap();
        }
        // redis -- using multiGet method can highly improve the performance
        List<String> keyList = new ArrayList<>();
        userIdList.forEach((userId -> {
            keyList.add(userProviderCacheKeyBuilder.buildUserInfoKey(userId));
        }));
        List<UserDTO> userDTOList = redisTemplate.opsForValue().multiGet(keyList).stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(userDTOList) && userDTOList.size() == userIdList.size()) {
            return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO));
        }

        List<Long> userIdInCacheList = userDTOList.stream().map(UserDTO::getUserId).collect(Collectors.toList());
        List<Long> userIdNotInCacheList = userIdList.stream().filter(x -> !userIdInCacheList.contains(x)).collect(Collectors.toList());
        // performance is not good if using mysql directly, max consumption will be using union all to all 100 sharding database
        // Using multiple thread to operate different id according to its corresponding database rather than search using union all directly
        Map<Long, List<Long>> userIdMap = userIdNotInCacheList.stream().collect(Collectors.groupingBy(userId -> userId % 100));
        List<UserDTO> dbQueryResult = new CopyOnWriteArrayList<>();
        userIdMap.values().parallelStream().forEach(queryUserIdList -> {
            dbQueryResult.addAll(ConvertBeanUtils.convertList(userMapper.selectBatchIds(queryUserIdList), UserDTO.class));
        });
        if (!CollectionUtils.isEmpty(dbQueryResult)) {
            //When huge amount of user go into a room, we need to get their info rapidly. So store them into redis
            Map<String, UserDTO> saveCacheMap = dbQueryResult.stream().collect(Collectors.toMap(userDto -> userProviderCacheKeyBuilder.buildUserInfoKey(userDto.getUserId()), x -> x));
            redisTemplate.opsForValue().multiSet(saveCacheMap);
            //pipeline batch transform, reduce network IO cost
            redisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                    for (String redisKey : saveCacheMap.keySet()) {
                        operations.expire((K) redisKey, createRandomExpireTime(), TimeUnit.SECONDS);
                    }
                    return null;
                }
            });
            userDTOList.addAll(dbQueryResult);
        }
        return userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, userDTO -> userDTO));
    }

    private int createRandomExpireTime() {
        int time = ThreadLocalRandom.current().nextInt(1000);
        return time + 60 * 30;
    }
}
