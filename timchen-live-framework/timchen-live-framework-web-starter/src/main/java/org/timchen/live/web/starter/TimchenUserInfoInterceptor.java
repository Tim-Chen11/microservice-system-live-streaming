package org.timchen.live.web.starter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.timchen.live.common.interfaces.enums.GatewayHeaderEnum;

/**
 * @Author: Tim Chen
 * @Date: 16:35 2024-08-01
 * @Description:
 */
public class TimchenUserInfoInterceptor implements HandlerInterceptor {

    //所有web请求来到这里的时候，都要被拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIdStr = request.getHeader(GatewayHeaderEnum.USER_LOGIN_ID.getName());
        //可能走的是白名单url
        if (StringUtils.isEmpty(userIdStr)) {
            return true;
        }
        //如果userId不为空，则把它放在线程本地变量里面去
        TimchenRequestContext.set(RequestConstants.TIMCHEN_USER_ID, Long.valueOf(userIdStr));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        TimchenRequestContext.clear();
    }
}
