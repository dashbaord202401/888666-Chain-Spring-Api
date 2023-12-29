package cn.org.gry.chainmaker.domain.entity;

import cn.org.gry.chainmaker.contract.ContractLotEvm;
import cn.org.gry.chainmaker.utils.Result;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/18 10:51
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Getter
@Setter
public class Lot {
    @Autowired
    private ContractLotEvm contractLotEvm;

    public Result mintForPackages (String tokenURI, List<BigInteger> _childIDs) {
        List<Uint256> childIDs = new ArrayList<>();
        for (BigInteger childID : _childIDs) {
            childIDs.add(new Uint256(childID));
        }
        DynamicArray<Uint256> dynamicArray = new DynamicArray<>(Uint256.class, childIDs);
        return contractLotEvm.invokeContract("mintForPackages",
                Arrays.asList(new Utf8String(tokenURI), dynamicArray),
                Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("tokenId"));
    }

    public Result balanceOf (String owner) {
        return contractLotEvm.invokeContract("balanceOf", Collections.singletonList(new Address(owner)), Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("balance"));
    }

    public Result transferPackageFrom (String from, String to, BigInteger tokenId) {
        return contractLotEvm.invokeContract("transferPackageFrom", Arrays.asList(new Address(from), new Address(to), new Uint256(tokenId)), Arrays.asList(), Arrays.asList());
    }

    public Result transferPackage (String to, BigInteger tokenId) {
        return contractLotEvm.invokeContract("transferPackage", Arrays.asList(new Address(to), new Uint256(tokenId)), Arrays.asList(), Arrays.asList());
    }

    public Result totalSupply () {
        return contractLotEvm.invokeContract("totalSupply", Collections.emptyList(), Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("totalSupply"));
    }
}
