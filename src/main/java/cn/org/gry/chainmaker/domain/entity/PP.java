package cn.org.gry.chainmaker.domain.entity;

import cn.org.gry.chainmaker.base.BaseContractEvm;
import cn.org.gry.chainmaker.base.erc721.ERC721;
import cn.org.gry.chainmaker.contract.ContractPackagedProductsEvm;
import cn.org.gry.chainmaker.utils.ChainMakerUtils;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/19 9:29
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class PP extends ERC721 {
    private ContractPackagedProductsEvm contractPackagedProductsEvm;

    @Autowired
    public PP(ContractPackagedProductsEvm contractPackagedProductsEvm) {
        this.contractPackagedProductsEvm = contractPackagedProductsEvm;
        setBaseContractEvm(contractPackagedProductsEvm);
    }

    public Result mint (
            BigInteger numberOfTokens,
            String tokenURI,
            String name,
            String productLot,
            List<BigInteger> childIDs,
            List<String> resumes) {
        List<Uint256> _childIDs = new ArrayList<>();
        List<Uint128> _resumes = new ArrayList<>();
        for (BigInteger childID : childIDs) {
            _childIDs.add(new Uint256(childID));
        }
        for (String resume : resumes) {
            _resumes.add(new Uint128(ChainMakerUtils.doubleString2BigInteger(resume)));
        }
        return contractPackagedProductsEvm.invokeContract(
                "mint",
                Arrays.asList(
                        new Uint256(numberOfTokens),
                        new Utf8String(tokenURI),
                        new Utf8String(name),
                        new Utf8String(productLot),
                        new DynamicArray<Uint256>(Uint256.class, _childIDs),
                        new DynamicArray<Uint128>(Uint128.class, _resumes)
                        ),
                Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {
                }),
                Arrays.asList("tokens")
        );
    }

    public Result transferFrom (String from, String to, BigInteger tokenId) {
        return contractPackagedProductsEvm.invokeContract(
                "transferFrom",
                Arrays.asList(
                        new Address(from),
                        new Address(to),
                        new Uint256(tokenId)
                ),
                Arrays.asList(),
                Arrays.asList()
        );
    }

    public Result transfer (String to, BigInteger tokenId) {
        return contractPackagedProductsEvm.invokeContract(
                "transfer",
                Arrays.asList(
                        new Address(to),
                        new Uint256(tokenId)
                ),
                Arrays.asList(),
                Arrays.asList()
        );
    }

    public Result approvalTMForAll (boolean approved) {
        return contractPackagedProductsEvm.invokeContract("approvalTMForAll", Arrays.asList(new Bool(approved)), Arrays.asList(), Arrays.asList());
    }

    @Override
    public void setBaseContractEvm(BaseContractEvm baseContractEvm) {
        super.baseContractEvm = baseContractEvm;
    }
}
