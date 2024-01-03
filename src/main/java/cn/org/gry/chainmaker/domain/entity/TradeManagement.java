package cn.org.gry.chainmaker.domain.entity;

import cn.org.gry.chainmaker.base.BaseDynamicStruct;
import cn.org.gry.chainmaker.contract.ContractTradeManagementEvm;
import cn.org.gry.chainmaker.utils.ChainMakerUtils;
import cn.org.gry.chainmaker.utils.Result;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.*;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/18 11:30
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Getter
@Setter
public class TradeManagement {
    @Autowired
    private ContractTradeManagementEvm contractTradeManagementEvm;

    public void setPackageLotContract (String lotAddress) {
        contractTradeManagementEvm.invokeContract("setPackageLotContract", Arrays.asList(new Address(lotAddress)), Arrays.asList(), Arrays.asList());
    }

    public void setProductLotContract (String lotAddress) {
        contractTradeManagementEvm.invokeContract("setProductLotContract", Arrays.asList(new Address(lotAddress)), Arrays.asList(), Arrays.asList());
    }

    public void setRawMaterialsContract (String rawMaterialsAddress) {
        contractTradeManagementEvm.invokeContract("setRawMaterialsContract", Arrays.asList(new Address(rawMaterialsAddress)), Arrays.asList(), Arrays.asList());
    }

    public void setPackagedProductsContract (String packagedProductsAddress) {
        contractTradeManagementEvm.invokeContract("setPackagedProductsContract", Arrays.asList(new Address(packagedProductsAddress)), Arrays.asList(), Arrays.asList());
    }

    public void RegisterSupplier (String supplierAddress, String name) {
        contractTradeManagementEvm.invokeContract("RegisterSupplier", Arrays.asList(new Address(supplierAddress), new Utf8String(name)), Arrays.asList(), Arrays.asList());
    }

    public void RegisterProducer (String producerAddress, String name) {
        contractTradeManagementEvm.invokeContract("RegisterProducer", Arrays.asList(new Address(producerAddress), new Utf8String(name)), Arrays.asList(), Arrays.asList());
    }

    public void RegisterUser (String userAddress, String name) {
        contractTradeManagementEvm.invokeContract("RegisterUser", Arrays.asList(new Address(userAddress), new Utf8String(name)), Arrays.asList(), Arrays.asList());
    }

    public Result getRawMaterialsNFT (BigInteger rawMaterialsId) {
        Result result = contractTradeManagementEvm.invokeContract(
                "getRawMaterialsNFT",
                Arrays.asList(new Uint256(rawMaterialsId)),
                Arrays.asList(
                        TypeReference.create(Bool.class),
                        TypeReference.create(Utf8String.class),
                        TypeReference.create(RMNFT.class),
                        TypeReference.create(Utf8String.class),
                        new TypeReference<DynamicArray<Uint256>>() {
                        },
                        new TypeReference<DynamicArray<Uint128>>() {
                        }
                ),
                Arrays.asList(
                        "success",
                        "msg",
                        "NFT",
                        "name",
                        "productLots",
                        "resumes"
                ));
        List<BigInteger> _resumes = (List<BigInteger>)result.getData().get("resumes");
        List<String> resumes = new ArrayList<>();
        for (BigInteger _resume : _resumes) {
            resumes.add(ChainMakerUtils.bigInteger2DoubleString(_resume));
        }
        result.getData().put("resumes", resumes);
        return result;
    }

    public Result getPackagedProductsNFT (BigInteger packagedProductsId) {
        return contractTradeManagementEvm.invokeContract(
                "getPackagedProductsNFT",
                Arrays.asList(new Uint256(packagedProductsId)),
                Arrays.asList(
                        TypeReference.create(Bool.class),
                        TypeReference.create(Utf8String.class),
                        new TypeReference<PPNFT>() {
                        },
                        TypeReference.create(Utf8String.class),
                        TypeReference.create(Utf8String.class),
                        new TypeReference<DynamicArray<TradeUser>>() {
                        },
                        new TypeReference<DynamicArray<RMInPP>>() {
                        }
                ),
                Arrays.asList(
                        "success",
                        "msg",
                        "NFT",
                        "name",
                        "producerName",
                        "tradeUsers",
                        "rawMaterials"
                ));
    }

    public Result getProductsFromPackage (BigInteger tokenId) {
        return contractTradeManagementEvm.invokeContract(
                "getProductsFromPackages",
                Collections.singletonList(new Uint256(tokenId)),
                Arrays.asList(
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Utf8String.class),
                        TypeReference.create(Utf8String.class),
                        TypeReference.create(Bool.class),
                        new TypeReference<DynamicArray<Uint256>>() {
                        }),
                Arrays.asList("tokenId", "name", "owner", "isBinding", "childIDs"));
    }

    public Result getStatist () {
        return contractTradeManagementEvm.invokeContract(
                "getStatist",
                Arrays.asList(),
                Arrays.asList(
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class)),
                Arrays.asList("totalRM", "totalPP", "totalPKL", "balanceOfRM", "balanceOfPP", "balanceOfPKL"));
    }

    public Result list (Boolean owner, String type) {
        Result result = contractTradeManagementEvm.invokeContract(
                "list",
                Arrays.asList(
                        new Bool(owner),
                        new Utf8String(type)),
                Arrays.asList(
                        new TypeReference<DynamicArray<ListElem>>() {
                        }),
                Arrays.asList("list"));
        Pageable pageable = PageRequest.of(1, 10);
        result.getData().put("list", new PageImpl<>((List<ListElem>)result.getData().get("list"), pageable, ((List<ListElem>)result.getData().get("list")).size()));
        return result;
    }

    public Result getProductsLotNFT(BigInteger lotId) {
        return contractTradeManagementEvm.invokeContract(
                "getProductsLotNFT",
                Arrays.asList(new Uint256(lotId)),
                Arrays.asList(
                        TypeReference.create(PLNFT.class),
                        new TypeReference<DynamicArray<Uint256>>() {
                        },
                        new TypeReference<DynamicArray<Uint256>>() {
                        }
                ),
                Arrays.asList(
                        "nft",
                        "rawMaterials",
                        "products"
                ));
    }

    @Getter
    @Setter
    public static class RMNFT extends BaseDynamicStruct {
        private BigInteger tokenID;
        private BigInteger lotId;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date produceTime;
        private String supplierName;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date supplierTime;
        private String producerName;
        private String totalSum;
        private String name;
        private String lotName;

        public RMNFT (
            Uint256 tokenID,
            Uint256 lotId,
            Uint256 produceTime,
            Utf8String supplierName,
            Uint256 supplierTime,
            Utf8String producerName,
            Uint128 totalSum,
            Utf8String name,
            Utf8String lotName
        )
        {
            super(tokenID, lotId, produceTime, supplierName, supplierTime, producerName, totalSum, name, lotName);
            this.tokenID = tokenID.getValue();
            this.lotId = lotId.getValue();
            this.produceTime = new Date(produceTime.getValue().longValue() * 1000);
            this.supplierName = supplierName.getValue();
            if (!supplierTime.getValue().equals(BigInteger.valueOf(0))) this.supplierTime = new Date(supplierTime.getValue().longValue() * 1000);
            this.producerName = producerName.getValue();
            this.totalSum = ChainMakerUtils.bigInteger2DoubleString(totalSum.getValue());
            this.name = name.getValue();
            this.lotName = lotName.getValue();
        }
    }

    @Getter
    @Setter
    public static class PPNFT extends BaseDynamicStruct {

        private BigInteger tokenID;
        private BigInteger productLotID;
        private BigInteger packageLotID;
        private Boolean isBinding;
        private String owner;

        public PPNFT (
            Uint256 tokenID,
            Uint256 productLotID,
            Uint256 packageLotID,
            Bool isBinding,
            Utf8String owner
        ) {
            super(tokenID, productLotID, packageLotID, isBinding, owner);
            this.tokenID = tokenID.getValue();
            this.productLotID = productLotID.getValue();
            this.packageLotID = packageLotID.getValue();
            this.isBinding = isBinding.getValue();
            this.owner = owner.getValue();
        }
    }

    @Getter
    @Setter
    public static class TradeUser extends BaseDynamicStruct {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date timestamp;
        private String name;

        public TradeUser (
            Uint256 timestamp,
            Utf8String name
        )
        {
            super(timestamp, name);
            this.timestamp = new Date(timestamp.getValue().longValue() * 1000);
            this.name = name.getValue();
        }
    }

    @Getter
    @Setter
    public static class RMInPP extends BaseDynamicStruct {
        private BigInteger tokenID;
        private String name;

        public RMInPP (
            Uint256 tokenID,
            Utf8String name
        )
        {
            super(tokenID, name);
            this.tokenID = tokenID.getValue();
            this.name = name.getValue();
        }
    }

    @Getter
    @Setter
    public static class PLNFT extends BaseDynamicStruct {
        private BigInteger tokenID;
        private String lotName;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date produceTime;
        private String producerName;
        private String name;

        public PLNFT (
            Uint256 tokenID,
            Utf8String lotName,
            Uint256 produceTime,
            Utf8String producerName,
            Utf8String name
        )
        {
            super(tokenID, lotName, produceTime, producerName, name);
            this.tokenID = tokenID.getValue();
            this.lotName = lotName.getValue();
            if (!produceTime.getValue().equals(BigInteger.valueOf(0))) this.produceTime = new Date(produceTime.getValue().longValue() * 1000);
            this.producerName = producerName.getValue();
            this.name = name.getValue();
        }
    }

    @Getter
    @Setter
    public static class ListElem extends BaseDynamicStruct {
        private BigInteger tokenID;
        private String owner;
        private String name;
        private String totalSum;

        public ListElem (
            Uint256 tokenID,
            Utf8String name,
            Utf8String owner,
            Uint256 totalSum
        )
        {
            super(tokenID, name, owner, totalSum);
            this.tokenID = tokenID.getValue();
            this.name = name.getValue();
            this.owner = owner.getValue();
            this.totalSum = ChainMakerUtils.bigInteger2DoubleString(totalSum.getValue());
        }
    }

}
