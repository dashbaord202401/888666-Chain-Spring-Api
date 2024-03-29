package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.service.RM;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description 原料NFTController
 * @since 2023/12/20 10:57
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/contract/rawMaterial.do")
public class RMController {
    @Autowired
    private RM rm;

    @RequestMapping(params = "action=mintBySuppler")
    public Result mintBySuppler(@RequestParam("tokenURI") String tokenURI,
                                @RequestParam("initSum") String initSum,
                                @RequestParam("name") String name
    ) {
        return rm.mint(tokenURI, initSum, "", new Date(0), name);
    }

    @RequestMapping(params = "action=mintByProducer")
    public Result mintByProducer(@RequestParam("tokenURI") String tokenURI,
                               @RequestParam("initSum") String initSum,
                               @RequestParam("supplyName") String supplyName,
                               @RequestParam("produceTime") Long produceTime,
                               @RequestParam("name") String name
    ) {
        return rm.mint(tokenURI, initSum, supplyName, new Date(produceTime), name);
    }

    @RequestMapping(params = "action=transferFrom")
    public Result transferFrom(@RequestParam("from") Long from,
                               @RequestParam("to") Long to,
                               @RequestParam("tokenId") BigInteger tokenId) {
        return rm.transferFrom(from, to, tokenId);
    }

    @RequestMapping(params = "action=transfer")
    public Result transfer(
            @RequestParam("to") Long to,
            @RequestParam("tokenId") BigInteger tokenId
    ) {
        return rm.transfer(to, tokenId);
    }

    @RequestMapping(params = "action=ownerOf")
    public Result ownerOf(@RequestParam("tokenId") BigInteger tokenId) {
        return rm.ownerOf(tokenId);
    }

    @RequestMapping(params = "action=balanceOf")
    public Result balanceOf(@RequestParam("owner") String owner) {
        return rm.balanceOf(owner);
    }
}
