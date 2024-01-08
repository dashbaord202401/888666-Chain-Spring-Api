package cn.org.gry.chainmaker.utils;

import java.util.Map;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/2 14:05
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
public class TokenHolder {

    private static final ThreadLocal<Map<String, String>> tokenThreadLocal = new ThreadLocal<>();

    public static String get(String key) {
        return tokenThreadLocal.get().get(key);
    }

    public static void put(String key, String value) {
        if (tokenThreadLocal.get() == null) {
            tokenThreadLocal.set(new java.util.HashMap<>());
        }
        tokenThreadLocal.get().put(key, value);
    }

    public static void clearToken() {
        tokenThreadLocal.remove();
    }
}
