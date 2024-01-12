package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.entity.RepositoryInfo;
import cn.org.gry.chainmaker.domain.entity.UserInfo;
import cn.org.gry.chainmaker.domain.service.PP;
import cn.org.gry.chainmaker.domain.service.TradeManagement;
import cn.org.gry.chainmaker.domain.service.UserInfoService;
import cn.org.gry.chainmaker.utils.Result;
import cn.org.gry.chainmaker.utils.TokenHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private PP pp;

    @RequestMapping(params = "action=getUid")
    public Result getUid() {
        return userInfoService.getUid();
    }

    @RequestMapping(params = "action=registerUser")
    public Result registerUser(UserInfo userInfo, @RequestParam("name") String name) {
        userInfo = userInfoService.registerUser(userInfo);
        TokenHolder.put("uid", "1");
        return tradeManagement.RegisterUser(userInfo.getAddress(), name);
    }

    @RequestMapping(params = "action=registerSupplier")
    public Result registerSupplier(UserInfo userInfo, @RequestParam("name") String name) {
        userInfo = userInfoService.registerUser(userInfo);
        TokenHolder.put("uid", "1");
        return tradeManagement.RegisterSupplier(userInfo.getAddress(), name);
    }

    @RequestMapping(params = "action=registerProducer")
    public Result registerProducer(UserInfo userInfo, @RequestParam("name") String name) {
        userInfo = userInfoService.registerUser(userInfo);
        TokenHolder.put("uid", "1");
        return tradeManagement.RegisterProducer(userInfo.getAddress(), name);
    }

    @RequestMapping(params = "action=registerRepository")
    public Result registerRepository(RepositoryInfo repositoryInfo, @RequestParam("name") String name) {
        UserInfo userInfo = userInfoService.registerUser(repositoryInfo.getUserInfo());
        Long uid = Long.valueOf(TokenHolder.get("uid"));
        TokenHolder.put("uid", userInfo.getUid().toString());
        pp.setApprovalForAll(userInfoService.getAddressByUid(uid), true);
        TokenHolder.put("uid", "1");
        return tradeManagement.RegisterRepository(userInfoService.getAddressByUid(uid), userInfo.getAddress(), name);
    }
}