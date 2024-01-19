package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.dto.ProductLotInfoDTO;
import cn.org.gry.chainmaker.domain.service.ProductLot;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description 产品批次NFTController
 * @since 2024/1/16 17:07
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/contract/productLot.do")
public class ProductLotController {
    @Autowired
    private ProductLot productLot;

    @RequestMapping(params = "action=mint")
    public Result mint(ProductLotInfoDTO productLotInfoDTO) {
        return productLot.mint(productLotInfoDTO);
    }
}
