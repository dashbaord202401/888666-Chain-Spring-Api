package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.contract.SystemContract;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/12 11:17
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/contract/system.do")
public class SystemController {
    @Autowired
    private SystemContract systemContract;

    @RequestMapping(params = "action=getBlockByHeight")
    public Result getOwner(@RequestParam("height") Long height, @RequestParam("withRWSet") Boolean withRWSet) {
        return systemContract.getBlockByHeight(height, withRWSet);
    }

    @RequestMapping(params = "action=getTxByTxId")
    public Result getTxByTxId(@RequestParam("txId") String txId) {
        return systemContract.getTxByTxId(txId);
    }
}
