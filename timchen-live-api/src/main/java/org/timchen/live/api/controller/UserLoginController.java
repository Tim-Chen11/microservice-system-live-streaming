package org.timchen.live.api.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.timchen.live.api.service.IUserLoginService;
import org.timchen.live.common.interfaces.vo.WebResponseVO;

/**
 * @Author: Tim Chen
 * @Date: 9:43 2024-07-30
 * @Description:
 */
@RestController
@RequestMapping("/userLogin")
public class UserLoginController {

    @Resource
    private IUserLoginService userLoginService;

    //发送验证码
    @PostMapping("/sendLoginCode")
    public WebResponseVO sendLoginCode(String phone) {
        return userLoginService.sendLoginCode(phone);
    }

    //登录请求 验证码是否合法 -> 初始化注册/老用户登录
    @PostMapping("/login")
    public WebResponseVO login(String phone, Integer code, HttpServletResponse response) {
        return userLoginService.login(phone, code, response);
    }

}
