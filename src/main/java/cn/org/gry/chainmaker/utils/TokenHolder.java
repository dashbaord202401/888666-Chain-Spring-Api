package cn.org.gry.chainmaker.utils;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/2 14:05
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
public class TokenHolder {

    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    public static String getToken() {
        return tokenThreadLocal.get();
    }

    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }

    public static void clearToken() {
        tokenThreadLocal.remove();
    }
}
