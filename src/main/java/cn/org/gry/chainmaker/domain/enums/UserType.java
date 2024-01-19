package cn.org.gry.chainmaker.domain.enums;

import lombok.Getter;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description 用户类型
 * @since 2024/1/11 11:09
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
public enum UserType {
    Suppler("供应商"),
    Producer("生产商"),
    User("用户"),
    Repository("仓库");

    UserType(String value) {
        this.value = value;
    }

    private final String value;
}
