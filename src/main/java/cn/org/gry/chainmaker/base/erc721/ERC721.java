package cn.org.gry.chainmaker.base.erc721;

import cn.org.gry.chainmaker.base.BaseContractEvm;
import cn.org.gry.chainmaker.utils.Result;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description ERC721合约通用方法
 * @since 2023/12/29 11:01
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
public abstract class ERC721 {
    protected BaseContractEvm baseContractEvm;

    public abstract void setBaseContractEvm(BaseContractEvm baseContractEvm);

    // 获取NFT的钱包地址（所有者）
    public Result ownerOf(BigInteger tokenId) {
        return baseContractEvm.invokeContract(
                "ownerOf",
                Collections.singletonList(new Uint256(tokenId)),
                Collections.singletonList(TypeReference.create(Address.class)),
                Collections.singletonList("owner"));
    }

    // 获取指定钱包地址的NFT数量
    public Result balanceOf(String owner) {
        return baseContractEvm.invokeContract("balanceOf", Collections.singletonList(new Address(owner)), Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("balance"));
    }

    // 获取所有NFT的总数量
    public Result totalSupply() {
        return baseContractEvm.invokeContract("totalSupply", Collections.emptyList(), Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("totalSupply"));
    }

    // 设置操作所有NFT的权限给指定钱包
    public Result setApprovalForAll (String operator, boolean approved) {
        return baseContractEvm.invokeContract("setApprovalForAll", Arrays.asList(new Address(operator), new Bool(approved)), Collections.emptyList(), Collections.emptyList());
    }
}
