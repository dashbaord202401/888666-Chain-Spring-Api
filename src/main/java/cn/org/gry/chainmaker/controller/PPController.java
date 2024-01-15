package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.dto.PackagedProductInfoDTO;
import cn.org.gry.chainmaker.domain.service.PP;
import cn.org.gry.chainmaker.domain.service.UserInfoService;
import cn.org.gry.chainmaker.utils.Result;
import cn.org.gry.chainmaker.utils.TokenHolder;
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
@RequestMapping("/contract/packagedProduct.do")
public class PPController {
    @Autowired
    private PP pp;

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(params = "action=mint")
    public Result mint(
        PackagedProductInfoDTO packagedProductInfoDTO
    ) {
        return pp.mint(packagedProductInfoDTO);
    }

    @RequestMapping(params = "action=transferFrom")
    public Result transferFrom(
            @RequestParam("from") Long from,
            @RequestParam("to") Long to,
            @RequestParam("tokenId") BigInteger tokenId
    ) {
        return pp.transferFrom(from, to, tokenId);
    }

    @RequestMapping(params = "action=transferBatch")
    public Result transferBatch(
            @RequestParam("to") Long to,
            @RequestParam("tokenId") BigInteger tokenId,
            @RequestParam("num") BigInteger num
    ) {
        return pp.transferBatch(to, tokenId, num);
    }

    @RequestMapping(params = "action=balanceOf")
    public Result balanceOf(
            @RequestParam("owner") String owner
    ) {
        return pp.balanceOf(owner);
    }

    @RequestMapping(params = "action=transfer")
    public Result transfer(
            @RequestParam("to") Long to,
            @RequestParam("tokenId") BigInteger tokenId
    ) {
        return pp.transfer(to, tokenId);
    }

    @RequestMapping(params = "action=burn")
    public Result burn(
            @RequestParam("tokenIds") List<BigInteger> tokenIds,
            @RequestParam("finalName") String finalName
    ) {
        return pp.burn(tokenIds, finalName);
    }

    @RequestMapping(params = "action=getTokensFromOwner")
    public Result getTokensFromOwner(
            @RequestParam("owner") Long owner
    ) {
        return pp.getTokensFromOwner(userInfoService.getAddressByEuidAndType(owner, TokenHolder.get("toType")));
    }
}
