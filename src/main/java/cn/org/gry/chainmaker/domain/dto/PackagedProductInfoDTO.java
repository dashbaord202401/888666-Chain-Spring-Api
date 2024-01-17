package cn.org.gry.chainmaker.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/11 16:27
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class PackagedProductInfoDTO {
    private BigInteger numberOfTokens;
    private String tokenURI;
    private String name;
    private BigInteger productLot;
    private BigInteger produceTime;
}
