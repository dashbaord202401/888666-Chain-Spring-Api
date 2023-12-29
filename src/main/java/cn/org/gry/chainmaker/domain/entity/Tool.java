package cn.org.gry.chainmaker.domain.entity;

import cn.org.gry.chainmaker.contract.ContractToolEvm;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;

import java.util.Arrays;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/18 16:38
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class Tool {
    @Autowired
    private ContractToolEvm contractToolEvm;

    public Result getSendAddress () {
        return contractToolEvm.invokeContract("getSendAddress", Arrays.asList(), Arrays.asList(TypeReference.create(Address.class)), Arrays.asList("address"));
    }
}
