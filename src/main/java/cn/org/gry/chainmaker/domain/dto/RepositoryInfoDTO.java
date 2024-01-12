package cn.org.gry.chainmaker.domain.dto;

import cn.org.gry.chainmaker.domain.entity.UserInfo;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/12 13:30
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class RepositoryInfoDTO {
    private Long id;

    private BigInteger tokenID;

    private Long tokenURI;

    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEuid(this.tokenURI);
        userInfo.setType("Repository");
        return userInfo;
    }
}
