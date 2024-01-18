package cn.org.gry.chainmaker.domain.criteria;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/18 9:19
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class NFTInfoCriteria {
    private Boolean isOwner;
    private Long owner;
    private List<BigInteger> tokenIds;
    private String type;
    private String userType;
}
