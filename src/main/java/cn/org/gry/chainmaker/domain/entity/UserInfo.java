package cn.org.gry.chainmaker.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/3 11:13
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @Column(name = "uid")
    private Long uid;

    @Column(name = "signCert")
    private byte[] signCert;

    @Column(name = "signKey")
    private byte[] signKey;

    @Column(name = "tlsCert")
    private byte[] tlsCert;

    @Column(name = "tlsKey")
    private byte[] tlsKey;

    @Column(name = "org")
    private String org;

    @Column(name = "address")
    private String address;
}
