package org.timchen.live.gateway.filter;

import jakarta.annotation.Resource;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.timchen.live.account.interfaces.IAccountTokenRPC;
import org.timchen.live.common.interfaces.enums.GatewayHeaderEnum;
import org.timchen.live.gateway.properties.GatewayApplicationProperties;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.netty.handler.codec.http.cookie.CookieHeaderNames.MAX_AGE;
import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * @Author: Tim Chen
 * @Date: 21:12 2024-07-31
 * @Description:
 */
@Component
public class AccountCheckFilter implements GlobalFilter, Ordered {
    private  static final Logger LOGGER = LoggerFactory.getLogger(AccountCheckFilter.class);

    @DubboReference
    private IAccountTokenRPC accountTokenRPC;
    @Resource
    private GatewayApplicationProperties gatewayApplicationProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //get request url check if empty
        ServerHttpRequest request = exchange.getRequest();
        String reqUrl = request.getURI().getPath();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://web.timchen.live.com:5500");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
        headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);

        if (StringUtils.isEmpty(reqUrl)) {
            return Mono.empty();
        }

        //check if the url is in the white list, true then don't do token check
        List<String> notCheckUrlList = gatewayApplicationProperties.getNotCheckUrlList();
        for (String notCheckUrl : notCheckUrlList) {
            if (reqUrl.startsWith(notCheckUrl)) {
                LOGGER.info("请求没有进行token校验，直接传达给业务下游");
                //直接将请求转给下游
                return chain.filter(exchange);
            }
        }
        //if not, take cookie and check the cookie token
        List<HttpCookie> httpCookieList = request.getCookies().get("tctk");
        if (CollectionUtils.isEmpty(httpCookieList)) {
            LOGGER.error("请求没有检索到tctk的cookie，被拦截");
            return Mono.empty();
        }
        String timchenTokenCookieValue = httpCookieList.get(0).getValue();
        if (StringUtils.isEmpty(timchenTokenCookieValue) || StringUtils.isEmpty(timchenTokenCookieValue.trim())) {
            LOGGER.error("请求的cookie中的tctk是空，被拦截");
            return Mono.empty();
        }
        //get token, check token if legal to get corresponding userid
        Long userId = accountTokenRPC.getUserIdByToken(timchenTokenCookieValue);
        //token not legal then interrupt
        if (userId == null) {
            LOGGER.error("请求的token失效了，被拦截");
            return Mono.empty();
        }
        // gateway --(header)--> springboot-web(interceptor-->get header)
        ServerHttpRequest.Builder builder = request.mutate();
        builder.header(GatewayHeaderEnum.USER_LOGIN_ID.getName(), String.valueOf(userId));
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
