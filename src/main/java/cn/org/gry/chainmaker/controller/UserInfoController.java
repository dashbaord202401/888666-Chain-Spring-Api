package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.service.TradeManagement;
import cn.org.gry.chainmaker.domain.service.UserInfoService;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/4 16:20
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/contract/user.do")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TradeManagement tradeManagement;

    @RequestMapping(params = "action=getUid")
    public Result getUid() {
        return userInfoService.getUid();
    }

//    @RequestMapping(params = "action=registerUser")
//    public Result registerUser(@RequestParam("userInfo")UserInfo userInfo) throws IOException, NoSuchAlgorithmException {
////        return userInfoService.registerUser(userInfo);
//    }
}