package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.service.Tool;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/20 10:56
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/echain/contract/tool.do")
public class ToolController {
    @Autowired
    private Tool tool;

    @RequestMapping(params = "action=getAddress")
    public Result getAddress() {
        return tool.getAddress();
    }
}