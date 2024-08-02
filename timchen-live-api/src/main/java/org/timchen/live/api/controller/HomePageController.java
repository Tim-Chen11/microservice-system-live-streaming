package org.timchen.live.api.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.timchen.live.common.interfaces.vo.WebResponseVO;
import org.timchen.live.web.starter.TimchenRequestContext;

/**
 * @Author: Tim Chen
 * @Date: 16:10 2024-08-01
 * @Description:
 */
@RestController
@RequestMapping("/home")
public class HomePageController {

//    @Resource
//    private IHomePageService homePageService;

    @PostMapping("/initPage")
    public WebResponseVO initPage() {
        Long userId = TimchenRequestContext.getUserId();
        System.out.println(userId);
//        HomePageVO homePageVO = new HomePageVO();
//        homePageVO.setLoginStatus(false);
//        if (userId != null) {
//            homePageVO = homePageService.initPage(userId);
//            homePageVO.setLoginStatus(true);
//        }
        return WebResponseVO.success();
    }
}
