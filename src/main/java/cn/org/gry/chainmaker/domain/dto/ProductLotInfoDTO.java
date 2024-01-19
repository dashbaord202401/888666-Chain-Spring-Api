package cn.org.gry.chainmaker.domain.dto;

import cn.org.gry.chainmaker.utils.ChainMakerUtils;
import lombok.Getter;
import lombok.Setter;
import org.web3j.abi.datatypes.generated.Uint128;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description 接收生产批次信息
 * @since 2024/1/16 17:09
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class ProductLotInfoDTO {
    private String tokenURI;
    private String name;
    private String lotName;
    private BigInteger produceTime;
    private List<Long> childIDs;
    private List<String> resumes;
    public List<Uint128> getResumes() {
        List<Uint128> _resumes = new ArrayList<>();
        for (String resume : resumes) {
            _resumes.add(new Uint128(ChainMakerUtils.doubleString2BigInteger(resume)));
        }
        return _resumes;
    }
}
