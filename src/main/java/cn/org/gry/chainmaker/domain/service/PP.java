package cn.org.gry.chainmaker.domain.service;

import cn.org.gry.chainmaker.base.BaseContractEvm;
import cn.org.gry.chainmaker.base.erc721.ERC721;
import cn.org.gry.chainmaker.contract.ContractPackagedProductsEvm;
import cn.org.gry.chainmaker.domain.dto.PackagedProductInfoDTO;
import cn.org.gry.chainmaker.repository.RawMaterialRepository;
import cn.org.gry.chainmaker.utils.Result;
import cn.org.gry.chainmaker.utils.TokenHolder;
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
 * @since 2023/12/19 9:29
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class PP extends ERC721 {
    private final ContractPackagedProductsEvm contractPackagedProductsEvm;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Autowired
    public PP(ContractPackagedProductsEvm contractPackagedProductsEvm) {
        this.contractPackagedProductsEvm = contractPackagedProductsEvm;
        setBaseContractEvm(contractPackagedProductsEvm);
    }

    public Result mint(
            PackagedProductInfoDTO packagedProductInfoDTO
    ) {
        return contractPackagedProductsEvm.invokeContract(
                "mint",
                Arrays.asList(
                        new Uint256(packagedProductInfoDTO.getNumberOfTokens()),
                        new Utf8String(packagedProductInfoDTO.getTokenURI()),
                        new Utf8String(packagedProductInfoDTO.getName()),
                        new Uint256(packagedProductInfoDTO.getProductLot()),
                        new Uint256(packagedProductInfoDTO.getProduceTime())
                ),
                Collections.singletonList(new TypeReference<DynamicArray<Uint256>>() {
                }),
                Collections.singletonList("tokens")
        );
    }

    public Result transferFrom(Long from, Long to, BigInteger tokenId) {
        return contractPackagedProductsEvm.invokeContract(
                "transferFrom",
                Arrays.asList(
                        new Address(userInfoService.getAddressByUid(from)),
                        new Address(userInfoService.getAddressByUid(to)),
                        new Uint256(tokenId)
                ),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    public Result transfer(Long to, BigInteger tokenId) {
        return contractPackagedProductsEvm.invokeContract(
                "transfer",
                Arrays.asList(
                        new Address(userInfoService.getAddressByEuidAndType(to, TokenHolder.get("toType"))),
                        new Uint256(tokenId)
                ),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    public Result transferBatch(Long to, BigInteger tokenId, BigInteger numberOfTokens) {
        return contractPackagedProductsEvm.invokeContract(
                "transferBatch",
                Arrays.asList(
                        new Address(userInfoService.getAddressByEuidAndType(to, TokenHolder.get("toType"))),
                        new Uint256(tokenId),
                        new Uint256(numberOfTokens)
                ),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    public Result burn (List<BigInteger> tokenIds, String finalName) {
        List<Uint256> _tokenIds = new ArrayList<>();
        for (BigInteger id : tokenIds) {
            _tokenIds.add(new Uint256(id));
        }
        return contractPackagedProductsEvm.invokeContract(
                "burn",
                Arrays.asList(
                        new DynamicArray<>(
                            Uint256.class,
                            _tokenIds
                        ),
                        new Utf8String(finalName)
                ),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    public Result approvalTMForAll(boolean approved) {
        return contractPackagedProductsEvm.invokeContract("approvalTMForAll", Collections.singletonList(new Bool(approved)), Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public void setBaseContractEvm(BaseContractEvm baseContractEvm) {
        super.baseContractEvm = baseContractEvm;
    }
}
