package cn.org.gry.chainmaker.domain.entity;

import cn.org.gry.chainmaker.contract.ContractRawMaterialsEvm;
import cn.org.gry.chainmaker.utils.ChainMakerUtils;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/18 17:12
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class RM {
    @Autowired
    private ContractRawMaterialsEvm contractRawMaterialsEvm;

    public Result mint (String tokenURI, String initSum) {
        return contractRawMaterialsEvm.invokeContract(
                "mint",
                Arrays.asList(new Utf8String(tokenURI), new Uint128(ChainMakerUtils.doubleString2BigInteger(initSum))),
                Arrays.asList(TypeReference.create(Uint256.class)),
                Arrays.asList("token"));
    }

    public Result transferFrom (String from, String to, BigInteger tokenId) {
        return contractRawMaterialsEvm.invokeContract(
                "transferFrom",
                Arrays.asList(new Address(from), new Address(to), new Uint256(tokenId)),
                Arrays.asList(),
                Arrays.asList());
    }

    public Result transfer (String to, BigInteger tokenId) {
        return contractRawMaterialsEvm.invokeContract(
                "transfer",
                Arrays.asList(new Address(to), new Uint256(tokenId)),
                Arrays.asList(),
                Arrays.asList());
    }

    public Result ownerOf (BigInteger tokenId) {
        return contractRawMaterialsEvm.invokeContract(
                "ownerOf",
                Arrays.asList(new Uint256(tokenId)),
                Arrays.asList(TypeReference.create(Address.class)),
                Arrays.asList("owner"));
    }

    public Result balanceOf (String owner) {
        return contractRawMaterialsEvm.invokeContract("balanceOf", Collections.singletonList(new Address(owner)), Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("balance"));
    }

    public Result totalSupply () {
        return contractRawMaterialsEvm.invokeContract("totalSupply", Collections.emptyList(), Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("totalSupply"));
    }
}
