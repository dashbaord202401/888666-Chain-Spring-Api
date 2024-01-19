package cn.org.gry.chainmaker.domain.enums;

import lombok.Getter;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description NFT类型
 * @since 2024/1/4 13:43
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
public enum NFTType {
    PackagedProduct("产品"),
    RawMaterial("原料"),
    Package("包装");

    NFTType(String value) {
        this.value = value;
    }

    private final String value;
}
