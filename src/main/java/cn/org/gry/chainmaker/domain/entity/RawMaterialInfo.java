package cn.org.gry.chainmaker.domain.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description 原料关系表
 * @since 2024/1/11 16:47
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Entity
@Getter
@Setter
@Table(name = "raw_info")
public class RawMaterialInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tokenID")
    private BigInteger tokenID;

    @Column(name = "tokenURI")
    private Long tokenURI;
}
