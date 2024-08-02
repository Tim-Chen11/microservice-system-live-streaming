package org.timchen.live.msg.provider.service.impl;

import com.alibaba.nacos.api.utils.StringUtils;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.apache.commons.lang3.RandomUtils;
import org.idea.timchen.live.framework.redis.starter.key.MsgProviderCacheKeyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.timchen.live.msg.dto.MsgCheckDTO;
import org.timchen.live.msg.enums.MsgSendResultEnum;
import org.timchen.live.msg.provider.config.ApplicationProperties;
import org.timchen.live.msg.provider.config.SmsTemplateIDEnum;
import org.timchen.live.msg.provider.config.ThreadPoolManager;
import org.timchen.live.msg.provider.dao.mapper.SmsMapper;
import org.timchen.live.msg.provider.dao.po.SmsPO;
import org.timchen.live.msg.provider.service.ISmsService;
import jakarta.annotation.Resource;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @Author: Tim Chen
 * @Date: 1:35 2024-07-29
 * @Description:
 */
@Service
public class SmsServiceImpl implements ISmsService {

    private static Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private MsgProviderCacheKeyBuilder msgProviderCacheKeyBuilder;
    @Resource
    private SmsMapper smsMapper;
    @Resource
    private ApplicationProperties applicationProperties;

    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return MsgSendResultEnum.MSG_PARAM_ERROR;
        }
        //generate verification code, 4 or 6 char, lasting period, same phone cannot send repeatedly, redis record code
        String codeCacheKey = msgProviderCacheKeyBuilder.buildSmsLoginCodeKey(phone);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(codeCacheKey))) {
            logger.warn("该手机号短信发送过于频繁，phone is {}", phone);
            return MsgSendResultEnum.SEND_FAIL;
        }
        int code = RandomUtils.nextInt(1000, 9999);
        redisTemplate.opsForValue().set(codeCacheKey, code, 60, TimeUnit.SECONDS);
        //send code
        ThreadPoolManager.commonAsyncPool.execute(() -> {
            boolean sendStatus = sendMsgToCCP(phone, code);
            if (sendStatus) {
                insertOne(phone, code);
            }
        });
        //record code
        return MsgSendResultEnum.SEND_SUCCESS;
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        //parameter check
        if (StringUtils.isEmpty(phone) || code == null || code < 1000 || code > 9999) {
            return new MsgCheckDTO(false, "参数异常");
        }

        //redis check code
        String codeCacheKey = msgProviderCacheKeyBuilder.buildSmsLoginCodeKey(phone);
        Integer cacheCode = (Integer) redisTemplate.opsForValue().get(codeCacheKey);
        if (cacheCode == null || cacheCode < 1000 || cacheCode > 9999) {
            return new MsgCheckDTO(false, "验证码已过期");
        }
        if (cacheCode.equals(code)) {
            redisTemplate.delete(codeCacheKey);
            return new MsgCheckDTO(true, "验证码校验成功");
        }
        return new MsgCheckDTO(false, "验证码校验失败");
    }

    @Override
    public void insertOne(String phone, Integer code) {
        SmsPO smsPO = new SmsPO();
        smsPO.setPhone(phone);
        smsPO.setCode(code);
        smsMapper.insert(smsPO);
    }

    /**
     * send code to third party SaaS
     *
     * @param phone
     * @param code
     * @return
     */
    private boolean sendMsgToCCP(String phone, Integer code) {
        try {
            //生产环境请求地址：app.cloopen.com
            String serverIp = applicationProperties.getSmsServerIp();
            //请求端口
            String serverPort = String.valueOf(applicationProperties.getPort());
            //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
            String accountSId = applicationProperties.getAccountSId();
            String accountToken = applicationProperties.getAccountToken();
            //请使用管理控制台中已创建应用的APPID
            String appId = applicationProperties.getAppId();
            CCPRestSmsSDK sdk = new CCPRestSmsSDK();
            sdk.init(serverIp, serverPort);
            sdk.setAccount(accountSId, accountToken);
            sdk.setAppId(appId);
            sdk.setBodyType(BodyType.Type_JSON);
            String to = applicationProperties.getTestPhone();
            String templateId = SmsTemplateIDEnum.SMS_LOGIN_CODE_TEMPLATE.getTemplateId();
            String[] datas = {String.valueOf(code), "1"};
            String subAppend = "1234";  //可选 扩展码，四位数字 0~9999
            String reqId = UUID.randomUUID().toString();  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
            //HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
            HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas, subAppend, reqId);
            logger.info("phone is: " + phone + "短信验证码: " + code);
            if ("000000".equals(result.get("statusCode"))) {
                //正常返回输出data包体信息（map）
                HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
                Set<String> keySet = data.keySet();
                for (String key : keySet) {
                    Object object = data.get(key);
                    logger.info("Key is {}, object is {}", key, object);
                }
            } else {
                //异常返回输出错误码和错误信息
                logger.error("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
                return false;
            }

            return true;
        } catch (Exception e) {
            logger.error("[sendSmsToCCP] error is ", e);
            throw new RuntimeException(e);
        }
//        finally {
//            return false;
//        }
    }

}
