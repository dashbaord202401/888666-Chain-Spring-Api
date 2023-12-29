package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.entity.RM;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/20 10:57
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/contract/RawMaterials.do")
public class RMController {
    @Autowired
    private RM rm;

    @RequestMapping(params = "action=mint")
    public Result mint (@RequestParam("tokenURI") String tokenURI,
                        @RequestParam("initSum") String initSum,
                        @RequestParam("name") String name) {
        return rm.mint(tokenURI, initSum, name);
    }

    @RequestMapping(params = "action=transferFrom")
    public Result transferFrom (@RequestParam("from") String from,
                                @RequestParam("to") String to,
                                @RequestParam("tokenId") BigInteger tokenId,
                                @RequestParam("lotName") String lotName) {
        return rm.transferFrom(from, to, tokenId, lotName);
    }

    @RequestMapping(params = "action=transfer")
    public Result transfer (
            @RequestParam("to") String to,
            @RequestParam("tokenId") BigInteger tokenId,
            @RequestParam("lotName") String lotName
    )
    {
        return rm.transfer(to, tokenId, lotName);
    }

    @RequestMapping(params = "action=ownerOf")
    public Result ownerOf (@RequestParam("tokenId") BigInteger tokenId) {
        return rm.ownerOf(tokenId);
    }

    @RequestMapping(params = "action=balanceOf")
    public Result balanceOf (@RequestParam("owner") String owner) {
        return rm.balanceOf(owner);
    }
}
