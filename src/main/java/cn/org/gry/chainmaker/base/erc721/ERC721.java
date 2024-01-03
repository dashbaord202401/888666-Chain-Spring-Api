package cn.org.gry.chainmaker.base.erc721;

import cn.org.gry.chainmaker.base.BaseContractEvm;
import cn.org.gry.chainmaker.utils.Result;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/29 11:01
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
public abstract class ERC721 {
    protected BaseContractEvm baseContractEvm;

    public abstract void setBaseContractEvm(BaseContractEvm baseContractEvm);

    public Result ownerOf(BigInteger tokenId) {
        return baseContractEvm.invokeContract(
                "ownerOf",
                Collections.singletonList(new Uint256(tokenId)),
                Collections.singletonList(TypeReference.create(Address.class)),
                Collections.singletonList("owner"));
    }

    public Result balanceOf(String owner) {
        return baseContractEvm.invokeContract("balanceOf", Collections.singletonList(new Address(owner)), Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("balance"));
    }

    public Result totalSupply() {
        return baseContractEvm.invokeContract("totalSupply", Collections.emptyList(), Collections.singletonList(TypeReference.create(Uint256.class)), Collections.singletonList("totalSupply"));
    }
}
