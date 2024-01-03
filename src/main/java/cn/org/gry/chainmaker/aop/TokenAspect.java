package cn.org.gry.chainmaker.aop;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/2 13:48
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */

import cn.org.gry.chainmaker.utils.TokenHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class TokenAspect {
    @Before("execution(* cn.org.gry.chainmaker.controller.*.*(..))")
    public void beforeControllerMethod(JoinPoint joinPoint) {
        // 获取HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取Header中的Token
        String token = request.getHeader("token");

        // 将Token设置进ThreadLocal
        TokenHolder.setToken(token);
    }
}

