package cn.org.gry.chainmaker.domain.service;

import cn.org.gry.chainmaker.base.BaseDynamicStruct;
import cn.org.gry.chainmaker.contract.ContractTradeManagementEvm;
import cn.org.gry.chainmaker.domain.criteria.NFTInfoCriteria;
import cn.org.gry.chainmaker.domain.entity.ProductLotRelation;
import cn.org.gry.chainmaker.domain.entity.RawMaterialInfo;
import cn.org.gry.chainmaker.domain.enums.NFTType;
import cn.org.gry.chainmaker.repository.ProductLotRelationRepository;
import cn.org.gry.chainmaker.repository.RawMaterialRepository;
import cn.org.gry.chainmaker.repository.UserInfoRepository;
import cn.org.gry.chainmaker.utils.ChainMakerUtils;
import cn.org.gry.chainmaker.utils.Result;
import cn.org.gry.chainmaker.utils.TokenHolder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
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

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ProductLotRelationRepository productLotRelationRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    public void setPackageLotContract(String lotAddress) {
        contractTradeManagementEvm.invokeContract("setPackageLotContract", Collections.singletonList(new Address(lotAddress)), Collections.emptyList(), Collections.emptyList());
    }

    public void setProductLotContract(String lotAddress) {
        contractTradeManagementEvm.invokeContract("setProductLotContract", Collections.singletonList(new Address(lotAddress)), Collections.emptyList(), Collections.emptyList());
    }

    public void setRawMaterialsContract(String rawMaterialsAddress) {
        contractTradeManagementEvm.invokeContract("setRawMaterialsContract", Collections.singletonList(new Address(rawMaterialsAddress)), Collections.emptyList(), Collections.emptyList());
    }

    public void setPackagedProductsContract(String packagedProductsAddress) {
        contractTradeManagementEvm.invokeContract("setPackagedProductsContract", Collections.singletonList(new Address(packagedProductsAddress)), Collections.emptyList(), Collections.emptyList());
    }

    public Result RegisterSupplier(String supplierAddress, String name) {
        return contractTradeManagementEvm.invokeContract("RegisterSupplier", Arrays.asList(new Address(supplierAddress), new Utf8String(name)), Collections.emptyList(), Collections.emptyList());
    }

    public Result RegisterProducer(String producerAddress, String name) {
        return contractTradeManagementEvm.invokeContract("RegisterProducer", Arrays.asList(new Address(producerAddress), new Utf8String(name)), Collections.emptyList(), Collections.emptyList());
    }

    public Result RegisterUser(String userAddress, String name) {
       return contractTradeManagementEvm.invokeContract("RegisterUser", Arrays.asList(new Address(userAddress), new Utf8String(name)), Collections.emptyList(), Collections.emptyList());
    }

    public Result RegisterRepository (String parentAddress, String repositoryAddress, String name) {
        return contractTradeManagementEvm.invokeContract("RegisterRepository", Arrays.asList(new Address(parentAddress), new Address(repositoryAddress), new Utf8String(name)), Collections.emptyList(), Collections.emptyList());
    }

    public Result getRawMaterialsNFT(BigInteger rawMaterialsId) {
        Result result = contractTradeManagementEvm.invokeContract(
                "getRawMaterialsNFT",
                Collections.singletonList(new Uint256(rawMaterialsId)),
                Arrays.asList(
                        TypeReference.create(Bool.class),
                        TypeReference.create(Utf8String.class),
                        TypeReference.create(RMNFT.class),
                        new TypeReference<DynamicArray<Uint256>>() {
                        },
                        new TypeReference<DynamicArray<Uint128>>() {
                        }
                ),
                Arrays.asList(
                        "success",
                        "msg",
                        "NFT",
                        "productLots",
                        "resumes"
                ));
        // 将BigInteger转换为String （转换成浮点数）
        List<BigInteger> _resumes = (List<BigInteger>) result.getData().get("resumes");
        List<String> resumes = new ArrayList<>();
        for (BigInteger _resume : _resumes) {
            resumes.add(ChainMakerUtils.bigInteger2DoubleString(_resume));
        }
        result.getData().put("resumes", resumes);
        return result;
    }

    public Result getPackagedProductsNFT(BigInteger packagedProductsId) {
        return contractTradeManagementEvm.invokeContract(
                "getPackagedProductsNFT",
                Collections.singletonList(new Uint256(packagedProductsId)),
                Arrays.asList(
                        TypeReference.create(Bool.class),
                        TypeReference.create(Utf8String.class),
                        new TypeReference<PPNFT>() {
                        },
                        new TypeReference<DynamicArray<Trade>>() {
                        },
                        new TypeReference<DynamicArray<RMInPP>>() {
                        }
                ),
                Arrays.asList(
                        "success",
                        "msg",
                        "NFT",
                        "trade",
                        "rawMaterials"
                ));
    }

    public Result getProductsFromPackage(BigInteger tokenId) {
        return contractTradeManagementEvm.invokeContract(
                "getProductsFromPackages",
                Collections.singletonList(new Uint256(tokenId)),
                Arrays.asList(
                        new TypeReference<PKLNFT>() {
                        },
                        new TypeReference<DynamicArray<Uint256>>() {
                        }),
                Arrays.asList("nft", "productIds"));
    }

    public Result getStatist() {
        return contractTradeManagementEvm.invokeContract(
                "getStatist",
                Collections.emptyList(),
                Arrays.asList(
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class),
                        TypeReference.create(Uint256.class)),
                Arrays.asList("totalRM", "totalPP", "totalPKL", "balanceOfRM", "balanceOfPP", "balanceOfPKL"));
    }

    public Result list(NFTInfoCriteria criteria) {
        String methodName = "listFor";
        ArrayList<Uint256> _tokenIds = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(criteria.getTokenIds())) {
            for (BigInteger tokenId : criteria.getTokenIds()) {
                _tokenIds.add(new Uint256(tokenId));
            }
        }
        if (criteria.getType().equals(NFTType.RawMaterial.name())) {
            methodName += "RawMaterial";
        } else if (criteria.getType().equals(NFTType.PackagedProduct.name())) {
            methodName += "Product";
        } else if (criteria.getType().equals(NFTType.Package.name())) {
            methodName += "Package";
        } else {
            return Result.fail("type error", "", new HashMap<>());
        }
        TokenHolder.put("uid", userInfoRepository.findByEuidAndType(criteria.getOwner(), TokenHolder.get("toType")).getUid().toString());
        Result result = contractTradeManagementEvm.invokeContract(
                methodName,
                Arrays.asList(
                        new Bool(criteria.getIsOwner()),
                        new DynamicArray<>(Uint256.class, _tokenIds)),
                Collections.singletonList(
                        new TypeReference<DynamicArray<ListElem>>() {
                        }),
                Collections.singletonList("list"));
        Pageable pageable = PageRequest.of(1, 10);
        if (ObjectUtils.isEmpty(result.getData().get("list"))) {
            result.getData().put("list", new PageImpl<>(new ArrayList<>(), pageable, 0));
            return result;
        }
        ((List<ListElem>)result.getData().get("list")).sort(Comparator.comparing(ListElem::getTokenID));
        result.getData().put("list", new PageImpl<>((List<ListElem>) result.getData().get("list"), pageable, ((List<ListElem>) result.getData().get("list")).size()));
        return result;
    }

    public Result getInfo(NFTInfoCriteria criteria) {
        String methodName = "listFor";
        List<Uint256> _tokenIds = new ArrayList<>();
        if (criteria.getType().equals(NFTType.RawMaterial.name())) {
            methodName += "RawMaterial";
            if (ObjectUtils.isNotEmpty(criteria.getTokenIds())) {
                for (BigInteger tokenId : criteria.getTokenIds()) {
                    RawMaterialInfo rawMaterialInfo = rawMaterialRepository.findByTokenURI(tokenId.longValue());
                    if (ObjectUtils.isNotEmpty(rawMaterialInfo)) {
                        _tokenIds.add(new Uint256(rawMaterialInfo.getTokenID()));
                    }
                }
            }
        } else if (criteria.getType().equals(NFTType.PackagedProduct.name())) {
            methodName += "Product";
            if (ObjectUtils.isNotEmpty(criteria.getTokenIds())) {
                for (BigInteger tokenId : criteria.getTokenIds()) {
                    _tokenIds.add(new Uint256(tokenId));
                }
            }
        } else if (criteria.getType().equals(NFTType.Package.name())) {
            methodName += "Package";
        } else {
            return Result.fail("type error", "", new HashMap<>());
        }
        if (_tokenIds.isEmpty()) {
            return Result.fail("tokenIds error", "", new HashMap<>());
        }
        TokenHolder.put("uid", userInfoRepository.findByEuidAndType(criteria.getOwner(), TokenHolder.get("toType")).getUid().toString());
        Result result = contractTradeManagementEvm.invokeContract(
                methodName,
                Arrays.asList(
                        new Bool(criteria.getIsOwner()),
                        new DynamicArray<>(Uint256.class, _tokenIds)),
                Collections.singletonList(
                        new TypeReference<DynamicArray<ListElem>>() {
                        }),
                Collections.singletonList("list"));
        Pageable pageable = PageRequest.of(1, 10);
        ((List<ListElem>)result.getData().get("list")).sort(Comparator.comparing(ListElem::getTokenID));
        result.getData().put("list", new PageImpl<>((List<ListElem>) result.getData().get("list"), pageable, ((List<ListElem>) result.getData().get("list")).size()));
        return result;
    }

    public Result getProductsLotNFT(Long lotId) {
        ProductLotRelation productLotRelation = productLotRelationRepository.findByEid(lotId);
        if (productLotRelation == null) {
            return Result.fail( "lotId error", "", new HashMap());
        }
        return contractTradeManagementEvm.invokeContract(
                "getProductsLotNFT",
                Collections.singletonList(new Uint256(productLotRelation.getTokenID())),
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

    /***
     * 原料NFT信息
     */
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

        public RMNFT(
                Uint256 tokenID,
                Uint256 lotId,
                Uint256 produceTime,
                Utf8String supplierName,
                Uint256 supplierTime,
                Utf8String producerName,
                Uint128 totalSum,
                Utf8String name,
                Utf8String lotName
        ) {
            super(tokenID, lotId, produceTime, supplierName, supplierTime, producerName, totalSum, name, lotName);
            this.tokenID = tokenID.getValue();
            this.lotId = lotId.getValue();
            this.produceTime = new Date(produceTime.getValue().longValue() * 1000);
            this.supplierName = supplierName.getValue();
            if (!supplierTime.getValue().equals(BigInteger.valueOf(0)))
                this.supplierTime = new Date(supplierTime.getValue().longValue() * 1000);
            this.producerName = producerName.getValue();
            this.totalSum = ChainMakerUtils.bigInteger2DoubleString(totalSum.getValue());
            this.name = name.getValue();
            this.lotName = lotName.getValue();
        }
    }

    /***
     * 产品NFT信息
     */
    @Getter
    @Setter
    public static class PPNFT extends BaseDynamicStruct {
        private BigInteger tokenID;
        private BigInteger productLotID;
        private String productLotName;
        private BigInteger packageLotID;
        private Boolean isBinding;
        private String owner;
        private String ownerName;
        private String producerName;
        private String name;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date produceTime;

        public PPNFT(
                Uint256 tokenID,
                Uint256 productLotID,
                Utf8String productLotName,
                Uint256 packageLotID,
                Bool isBinding,
                Address owner,
                Utf8String ownerName,
                Utf8String producerName,
                Utf8String name,
                Uint256 produceTime
        ) {
            super(tokenID, productLotID, productLotName, packageLotID, isBinding, owner, ownerName, producerName, name, produceTime);
            this.tokenID = tokenID.getValue();
            this.productLotID = productLotID.getValue();
            this.productLotName = productLotName.getValue();
            this.packageLotID = packageLotID.getValue();
            this.isBinding = isBinding.getValue();
            this.owner = owner.getValue();
            this.ownerName = ownerName.getValue();
            this.producerName = producerName.getValue();
            this.name = name.getValue();
            if (!produceTime.getValue().equals(BigInteger.valueOf(0))) {
                this.produceTime = new Date(produceTime.getValue().longValue() * 1000);
            }
        }
    }

    /***
     * 包装批次NFT信息
     */
    @Getter
    @Setter
    public static class PKLNFT extends BaseDynamicStruct {
        private BigInteger tokenID;
        private Boolean isBinding;
        private String owner;
        private String ownerName;
        private String name;

        public PKLNFT(
                Uint256 tokenID,
                Bool isBinding,
                Address owner,
                Utf8String ownerName,
                Utf8String name
        ) {
            super(tokenID, isBinding, owner, ownerName, name);
            this.tokenID = tokenID.getValue();
            this.isBinding = isBinding.getValue();
            this.owner = owner.getValue();
            this.ownerName = ownerName.getValue();
            this.name = name.getValue();
        }
    }

    /***
     * 交易信息
     */
    @Getter
    @Setter
    public static class Trade extends BaseDynamicStruct {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date timestamp;
        private String from;
        private String to;
        private String fromName;
        private String toName;

        public Trade (
                Uint256 timestamp,
                Address from,
                Address to,
                Utf8String fromName,
                Utf8String toName
        ) {
            super(timestamp, from, to, fromName, toName);
            this.timestamp = new Date(timestamp.getValue().longValue() * 1000);
            this.from = from.getValue();
            this.to = to.getValue();
            this.fromName = fromName.getValue();
            this.toName = toName.getValue();
        }
    }

    /***
     * 产品内的原料信息
     */
    @Getter
    @Setter
    public static class RMInPP extends BaseDynamicStruct {
        private BigInteger tokenID;
        private String name;

        public RMInPP(
                Uint256 tokenID,
                Utf8String name
        ) {
            super(tokenID, name);
            this.tokenID = tokenID.getValue();
            this.name = name.getValue();
        }
    }

    /***
     * 生产批次NFT信息
     */
    @Getter
    @Setter
    public static class PLNFT extends BaseDynamicStruct {
        private BigInteger tokenID;
        private String lotName;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date produceTime;
        private String producerName;
        private String owner;
        private String ownerName;
        private String name;

        public PLNFT(
                Uint256 tokenID,
                Utf8String lotName,
                Uint256 produceTime,
                Utf8String producerName,
                Address owner,
                Utf8String ownerName,
                Utf8String name
        ) {
            super(tokenID, lotName, produceTime, producerName, owner, ownerName, name);
            this.tokenID = tokenID.getValue();
            this.lotName = lotName.getValue();
            if (!produceTime.getValue().equals(BigInteger.valueOf(0)))
                this.produceTime = new Date(produceTime.getValue().longValue() * 1000);
            this.producerName = producerName.getValue();
            this.owner = owner.getValue();
            this.ownerName = ownerName.getValue();
            this.name = name.getValue();
        }
    }

    /***
     * 列表元素信息
     */
    @Getter
    @Setter
    public static class ListElem extends BaseDynamicStruct {
        private BigInteger tokenID;
        private String name;
        private String owner;
        private String ownerName;
        private String totalSum;

        public ListElem(
                Uint256 tokenID,
                Utf8String name,
                Address owner,
                Utf8String ownerName,
                Uint256 totalSum
        ) {
            super(tokenID, name, owner, ownerName, totalSum);
            this.tokenID = tokenID.getValue();
            this.name = name.getValue();
            this.owner = owner.getValue();
            this.ownerName = ownerName.getValue();
            this.totalSum = ChainMakerUtils.bigInteger2DoubleString(totalSum.getValue());
        }
    }

}
