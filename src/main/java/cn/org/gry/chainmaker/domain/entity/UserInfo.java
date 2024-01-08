package cn.org.gry.chainmaker.domain.entity;

import lombok.Data;

import javax.persistence.*;


/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description 用户信息类
 * @since 2024/1/3 11:13
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Entity
@Table(name = "user_info")
public class UserInfo {
    // 用户id
    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    // echain用户id
    @Column(name = "euid")
    private Long euid;

    // 签名证书
    @Column(name = "signCert")
    private byte[] signCert = " ".getBytes();

    // 签名私钥
    @Column(name = "signKey")
    private byte[] signKey = " ".getBytes();

    // tls证书
    @Column(name = "tlsCert")
    private byte[] tlsCert = " ".getBytes();

    // tls私钥
    @Column(name = "tlsKey")
    private byte[] tlsKey = " ".getBytes();

    // 组织名
    @Column(name = "org")
    private String org;

    // 钱包地址
    @Column(name = "address")
    private String address = " ";

    // 支付密码
    @Column(name = "pwd")
    private String pwd = "123456";
}
