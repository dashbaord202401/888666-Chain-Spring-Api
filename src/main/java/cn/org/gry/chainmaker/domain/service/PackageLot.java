package cn.org.gry.chainmaker.domain.service;

import cn.org.gry.chainmaker.base.BaseContractEvm;
import cn.org.gry.chainmaker.base.erc721.ERC721;
import cn.org.gry.chainmaker.contract.ContractPackageLotEvm;
import cn.org.gry.chainmaker.repository.UserInfoRepository;
import cn.org.gry.chainmaker.utils.Result;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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
public class PackageLot extends ERC721 {
    private ContractPackageLotEvm contractLotEvm;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    public PackageLot(ContractPackageLotEvm contractLotEvm) {
        this.contractLotEvm = contractLotEvm;
        setBaseContractEvm(contractLotEvm);
    }

    public Result mint(String tokenURI, String name, List<BigInteger> _childIDs) {
        List<Uint256> childIDs = new ArrayList<>();
        for (BigInteger childID : _childIDs) {
            childIDs.add(new Uint256(childID));
        }
        DynamicArray<Uint256> dynamicArray = new DynamicArray<>(Uint256.class, childIDs);
        return contractLotEvm.invokeContract("mint",
                Arrays.asList(new Utf8String(tokenURI), new Utf8String(name), dynamicArray),
                Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("tokenId"));
    }

    public Result transferFrom(Long from, Long to, BigInteger tokenId) {
        return contractLotEvm.invokeContract("transferFrom", Arrays.asList(new Address(userInfoService.getAddressByUid(from)), new Address(userInfoService.getAddressByUid(to)), new Uint256(tokenId)), Collections.emptyList(), Collections.emptyList());
    }

    public Result transfer(Long to, BigInteger tokenId) {
        return contractLotEvm.invokeContract("transfer", Arrays.asList(new Address(userInfoService.getAddressByUid(to)), new Uint256(tokenId)), Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public void setBaseContractEvm(BaseContractEvm baseContractEvm) {
        super.baseContractEvm = baseContractEvm;
    }
}
