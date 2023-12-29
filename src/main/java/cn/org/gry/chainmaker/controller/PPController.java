package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.entity.PP;
import cn.org.gry.chainmaker.utils.Result;
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
 * @since 2023/12/20 11:05
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/contract/PackagedProducts.do")
public class PPController {
    @Autowired
    private PP pp;

    @RequestMapping(params = "action=mint")
    public Result mint (
            @RequestParam("numberOfTokens") BigInteger numberOfTokens,
            @RequestParam("tokenURI") String tokenURI,
            @RequestParam("name") String name,
            @RequestParam("productLot") String productLot,
            @RequestParam("childIDs") List<BigInteger> childIDs,
            @RequestParam("resumes") List<String> resumes
    )
    {
        return pp.mint(numberOfTokens, tokenURI, name, productLot, childIDs, resumes);
    }

    @RequestMapping(params = "action=transferFrom")
    public Result transferFrom (
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("tokenId") BigInteger tokenId
    )
    {
        return pp.transferFrom(from, to, tokenId);
    }

    @RequestMapping(params = "action=balanceOf")
    public Result balanceOf (
            @RequestParam("owner") String owner
    )
    {
        return pp.balanceOf(owner);
    }

    @RequestMapping(params = "action=transfer")
    public Result transfer (
            @RequestParam("to") String to,
            @RequestParam("tokenId") BigInteger tokenId
    )
    {
        return pp.transfer(to, tokenId);
    }
}
