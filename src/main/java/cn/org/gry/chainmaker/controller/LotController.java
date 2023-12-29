package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.entity.Lot;
import cn.org.gry.chainmaker.domain.entity.PP;
import cn.org.gry.chainmaker.utils.Result;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/25 17:25
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/contract/lot.do")
public class LotController {
    @Autowired
    private Lot lot;

    @Autowired
    private PP pp;

    @RequestMapping(params = "action=mintForPackages")
    public Result mintForPackages (
            @RequestParam("tokenURI") String tokenURI,
            @RequestParam("childIDs") List<BigInteger> childIDs,
            @RequestParam("name") String name
    )
    {
        return lot.mintForPackages(tokenURI, name, childIDs);
    }

    @RequestMapping(params = "balanceOf")
    public Result balanceOf (
            @RequestParam("owner") String owner
    )
    {
        return lot.balanceOf(owner);
    }

    @RequestMapping(params = "action=transferPackageFrom")
    public Result transferPackageFrom (
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("tokenId") BigInteger tokenId
    )
    {
        pp.approvalTMForAll(true);
        return lot.transferPackageFrom(from, to, tokenId);
    }

    @RequestMapping(params = "action=transferPackage")
    public Result transferPackage (
            @RequestParam("to") String to,
            @RequestParam("tokenId") BigInteger tokenId
    )
    {
        pp.approvalTMForAll(true);
        return lot.transferPackage(to, tokenId);
    }
}
