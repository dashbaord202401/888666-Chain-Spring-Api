package cn.org.gry.chainmaker.domain.service;

import cn.org.gry.chainmaker.base.BaseContractEvm;
import cn.org.gry.chainmaker.base.erc721.ERC721;
import cn.org.gry.chainmaker.contract.ContractProductLotEvm;
import cn.org.gry.chainmaker.domain.dto.ProductLotInfoDTO;
import cn.org.gry.chainmaker.domain.entity.ProductLotRelation;
import cn.org.gry.chainmaker.repository.ProductLotRelationRepository;
import cn.org.gry.chainmaker.repository.RawMaterialRepository;
import cn.org.gry.chainmaker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/16 16:59
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class ProductLot extends ERC721 {
    private final ContractProductLotEvm contractProductLotEvm;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Autowired
    private ProductLotRelationRepository productLotRelationRepository;

    @Autowired
    public ProductLot (ContractProductLotEvm contractProductLotEvm) {
        this.contractProductLotEvm = contractProductLotEvm;
        setBaseContractEvm(contractProductLotEvm);
    }

    public Result mint(
            ProductLotInfoDTO productLotInfoDTO
    ) {
        List<Uint256> childIDs = new ArrayList<>();
        for (int i = 0; i < productLotInfoDTO.getChildIDs().size(); i++) {
            childIDs.add(new Uint256(rawMaterialRepository.findByTokenURI(productLotInfoDTO.getChildIDs().get(i)).getTokenID()));
        }
        Result result = contractProductLotEvm.invokeContract(
                "mint",
                Arrays.asList(
                        new Utf8String(productLotInfoDTO.getTokenURI()),
                        new Utf8String(productLotInfoDTO.getName()),
                        new Utf8String(productLotInfoDTO.getLotName()),
                        new Uint256(productLotInfoDTO.getProduceTime()),
                        new DynamicArray<>(Uint256.class, childIDs),
                        new DynamicArray<>(Uint128.class, productLotInfoDTO.getResumes())
                ),
                Arrays.asList(TypeReference.create(Uint256.class)),
                Arrays.asList("tokenId")
        );
        ProductLotRelation productLotRelation = new ProductLotRelation();
        productLotRelation.setEid(Long.valueOf(productLotInfoDTO.getTokenURI()));
        productLotRelation.setTokenID((BigInteger)result.getData().get("tokenId"));
        productLotRelationRepository.save(productLotRelation);
        return result;
    }

    @Override
    public void setBaseContractEvm(BaseContractEvm baseContractEvm) {

    }
}
