package cn.org.gry.chainmaker.domain.entity;

import lombok.Data;

import javax.persistence.*;


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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "euid")
    private Long euid;

    @Column(name = "signCert")
    private byte[] signCert = " ".getBytes();

    @Column(name = "signKey")
    private byte[] signKey = " ".getBytes();

    @Column(name = "tlsCert")
    private byte[] tlsCert = " ".getBytes();

    @Column(name = "tlsKey")
    private byte[] tlsKey = " ".getBytes();

    @Column(name = "org")
    private String org;

    @Column(name = "address")
    private String address = " ";

    @Column(name = "pwd")
    private String pwd = "123456";
}
