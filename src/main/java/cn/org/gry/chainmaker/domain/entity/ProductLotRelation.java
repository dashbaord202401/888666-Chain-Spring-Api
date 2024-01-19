package cn.org.gry.chainmaker.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description 生产批次与NFT关系表
 * @since 2024/1/17 15:05
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Entity
@Table(name = "product_lot_relation")
public class ProductLotRelation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "eid")
    private Long eid;

    @Column(name = "tokenID")
    private BigInteger tokenID;
}
