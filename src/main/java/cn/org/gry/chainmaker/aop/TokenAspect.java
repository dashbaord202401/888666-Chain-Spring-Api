package cn.org.gry.chainmaker.aop;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/2 13:48
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */

import cn.org.gry.chainmaker.domain.service.UserInfoService;
import cn.org.gry.chainmaker.utils.TokenHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class TokenAspect {

    @Autowired
    private UserInfoService userInfoService;

    @Order(1)
    @Before("execution(* cn.org.gry.chainmaker.controller.*.transfer*(..)) || execution(* cn.org.gry.chainmaker.controller.*.mint*(..))")
    public void beforeControllerTransfer(JoinPoint joinPoint) {
        // 获取HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取Header中的Token
        String pwd = request.getParameter("pwd");

        if (!userInfoService.verifyPwd(Long.valueOf(TokenHolder.get("uid")), pwd)) {
            throw new RuntimeException("密码错误");
        }
    }

    @Order(0)
    @Before("execution(* cn.org.gry.chainmaker.controller.*.*(..))")
    public void beforeControllerMethod(JoinPoint joinPoint) {
        // 获取HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取RequestBody中的id，并将其存为euid
        TokenHolder.put("euid", request.getParameter("id"));
        // 将Token设置进ThreadLocal
        TokenHolder.put("uid", userInfoService.getUid().getData().get("uid").toString());
    }
}

