package cn.org.gry.chainmaker.controller;

import cn.org.gry.chainmaker.domain.enums.NFTType;
import cn.org.gry.chainmaker.domain.service.PP;
import cn.org.gry.chainmaker.domain.service.PackageLot;
import cn.org.gry.chainmaker.domain.service.RM;
import cn.org.gry.chainmaker.domain.service.TradeManagement;
import cn.org.gry.chainmaker.repository.UserInfoRepository;
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
 * @since 2023/12/20 11:09
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/contract/trademanagement.do")
public class TradeManagementController {
    @Autowired
    private TradeManagement tradeManagement;

    @Autowired
    private PackageLot packageLot;

    @Autowired
    private RM rm;

    @Autowired
    private PP pp;

    @RequestMapping(params = "action=setPackageLotContract")
    public void setLotContract(@RequestParam("lotAddress") String lotAddress) {
        tradeManagement.setPackageLotContract(lotAddress);
    }

    @RequestMapping(params = "action=setRawMaterialsContract")
    public void setRawMaterialsContract(@RequestParam("rawMaterialsAddress") String rawMaterialsAddress) {
        tradeManagement.setRawMaterialsContract(rawMaterialsAddress);
    }

    @RequestMapping(params = "action=setPackagedProductsContract")
    public void setPackagedProductsContract(@RequestParam("packagedProductsAddress") String packagedProductsAddress) {
        tradeManagement.setPackagedProductsContract(packagedProductsAddress);
    }

    @RequestMapping(params = "action=RegisterSupplier")
    public void RegisterSupplier(@RequestParam("supplierAddress") String supplierAddress, @RequestParam("name") String name) {
        tradeManagement.RegisterSupplier(supplierAddress, name);
    }

    @RequestMapping(params = "action=RegisterProducer")
    public void RegisterProducer(@RequestParam("producerAddress") String producerAddress, @RequestParam("name") String name) {
        tradeManagement.RegisterProducer(producerAddress, name);
    }

    @RequestMapping(params = "action=getRawMaterialsNFT")
    public Result getRawMaterialsNFT(@RequestParam("rawMaterialsId") BigInteger rawMaterialsId) {
        return tradeManagement.getRawMaterialsNFT(rawMaterialsId);
    }

    @RequestMapping(params = "action=getPackagedProductsNFT")
    public Result getPackagedProductsNFT(@RequestParam("packagedProductsId") BigInteger packagedProductsId) {
        return tradeManagement.getPackagedProductsNFT(packagedProductsId);
    }

    @RequestMapping(params = "action=getProductsFromPackage")
    public Result getProductsFromPackage(
            @RequestParam("tokenId") BigInteger tokenId
    ) {
        return tradeManagement.getProductsFromPackage(tokenId);
    }

    @RequestMapping(params = "action=list")
    public Result list(
            @RequestParam("isOwner") Boolean isOwner,
            @RequestParam("type") String type
    ) {
        return tradeManagement.list(isOwner, type);
    }

    @RequestMapping(params = "action=getStatist")
    public Result getStatist() {
        return tradeManagement.getStatist();
    }

    @RequestMapping(params = "action=getProductsLotNFT")
    public Result getProductsLotNFT(@RequestParam("lotId") BigInteger lotId) {
        return tradeManagement.getProductsLotNFT(lotId);
    }

    @RequestMapping(params = "action=transfer")
    public Result transfer(
            @RequestParam("type") String type,
            @RequestParam("tokenId") BigInteger tokenId,
            @RequestParam("to") Long to
    ) {
        if (type.equals(NFTType.Product.name())) {
            return pp.transfer(to, tokenId);
        } else if (type.equals(NFTType.Package.name())) {
            return packageLot.transfer(to, tokenId);
        } else if (type.equals(NFTType.RawMaterial.name())) {
            return rm.transfer(to, tokenId);
        }
        return Result.fail("type error", "", null);
    }
}
