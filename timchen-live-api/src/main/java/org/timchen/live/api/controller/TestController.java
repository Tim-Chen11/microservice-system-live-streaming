package org.timchen.live.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.timchen.live.api.vo.TestVO;

/**
 * @Author: Tim Chen
 * @Date: 13:50 2024-07-28
 * @Description:
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger= LoggerFactory.getLogger(TestController.class);

    @PostMapping("/testPost1")
    public String testPost1(String id){
        logger.info("[testPost1] id is{}",id);
        return "testPost1";
    }

    @PostMapping("/testPost2")
    public String testPost2(TestVO testVO){
        logger.info("[testPost2] id is{}",testVO);
        return "testPost2";
    }
}
