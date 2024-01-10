
package cn.org.gry.chainmaker;

import cn.org.gry.chainmaker.config.InitSystemClient;
import cn.org.gry.chainmaker.contract.*;
import cn.org.gry.chainmaker.domain.entity.UserInfo;
import cn.org.gry.chainmaker.domain.service.*;
import cn.org.gry.chainmaker.utils.TokenHolder;
import org.chainmaker.sdk.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
class ChainSpringDemoApplicationTests {

    @Autowired
    private ContractPackageLotEvm contractPackageLotEvm;
    @Autowired
    private ContractRawMaterialsEvm contractRawMaterials;
    @Autowired
    private ContractPackagedProductsEvm contractPackagedProducts;
    @Autowired
    private ContractTradeManagementEvm contractTradeManagement;
    @Autowired
    private ContractProductLotEvm  contractProductLotEvm;
    @Autowired
    private ContractToolEvm contractToolEvm;

    @Autowired
    private PackageLot lot;

    @Autowired
    private Tool tool;

    @Autowired
    private RM rm;

    @Autowired
    private PP pp;

    @Autowired
    private TradeManagement tradeManagement;

    @Autowired
    private ContractToolEvm contractTool;

    @Autowired
    private UserInfoService userInfoService;


    @Test
    void contextLoads() {
        String Address_1 = "aa4ebe74154e3b0f63a2bd20290e2314f8728ee3";
        String Address_2 = "68db26564c267f14e13a6a2aad8f04d79513ee8b";
        String Address_3 = "d9973fef375a08fed8331d82899caed9486c8c31";
        String Address_4 = "3839bcfb4d575a565fae4ae5c50b52d5a616bf2b";

        TokenHolder.put("uid", "1");

        String add_pdlot = contractProductLotEvm.createEvmContract(InitSystemClient.admin1, new User[]{InitSystemClient.admin1, InitSystemClient.admin2, InitSystemClient.admin3}).getAddress();
        String add_pklot = contractPackageLotEvm.createEvmContract(InitSystemClient.admin1, new User[]{InitSystemClient.admin1, InitSystemClient.admin2, InitSystemClient.admin3}).getAddress();
        String add_rm = contractRawMaterials.createEvmContract(InitSystemClient.admin1, new User[]{InitSystemClient.admin1, InitSystemClient.admin2, InitSystemClient.admin3}).getAddress();
        String add_pp = contractPackagedProducts.createEvmContract(InitSystemClient.admin1, new User[]{InitSystemClient.admin1, InitSystemClient.admin2, InitSystemClient.admin3}).getAddress();
        String add_TM = contractTradeManagement.createEvmContract(InitSystemClient.admin1, new User[]{InitSystemClient.admin1, InitSystemClient.admin2, InitSystemClient.admin3}).getAddress();
        String add_Tool = contractTool.createEvmContract(InitSystemClient.admin1, new User[]{InitSystemClient.admin1, InitSystemClient.admin2, InitSystemClient.admin3}).getAddress();
        tradeManagement.setPackageLotContract(add_pklot);
        tradeManagement.setProductLotContract(add_pdlot);
        tradeManagement.setPackagedProductsContract(add_pp);
        tradeManagement.setRawMaterialsContract(add_rm);

        tradeManagement.RegisterSupplier(Address_1, "YJH_S1");
        tradeManagement.RegisterProducer(Address_2, "YJH_P2");
        tradeManagement.RegisterUser(Address_3, "YJH_D3");
        tradeManagement.RegisterRepository(Address_2, Address_4, "YJH_R4");
        TokenHolder.put("uid", "4");
        pp.setApprovalForAll(Address_2, true);
        TokenHolder.put("uid", "2");

        rm.mint("NACL", "123.123", "YJH_S", new Date(System.currentTimeMillis() / 1000), "食盐");
        rm.mint("NACL", "321.321", "YJH_S", new Date(System.currentTimeMillis() / 1000), "食盐");
        rm.mint("NACL", "456.4", "YJH_S", new Date(System.currentTimeMillis() / 1000), "食盐");
        rm.mint("NACL", "100.1", "YJH_S", new Date(System.currentTimeMillis() / 1000), "食盐");

        pp.mint(BigInteger.valueOf(10L), "NACL", "一袋食盐（小杯）","PP1", Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)), Arrays.asList("1", "1", "1"));
        pp.mint(BigInteger.valueOf(10L), "NACL", "一袋食盐（中杯）","PP2", Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)), Arrays.asList("1.5", "1.5", "1.5"));
        pp.mint(BigInteger.valueOf(10L), "NACL", "一袋食盐（大杯）","PP3", Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)), Arrays.asList("2", "2", "2"));
        pp.mint(BigInteger.valueOf(10L), "NACL", "一袋食盐（超大杯）","PP4", Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)), Arrays.asList("2.5", "2.5", "2.5"));
        pp.mint(BigInteger.valueOf(10L), "NACL", "一袋食盐（小杯）","PP5", Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)), Arrays.asList("1", "1", "1"));
        pp.mint(BigInteger.valueOf(10L), "NACL", "一袋食盐（中杯）","PP6", Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3)), Arrays.asList("1.5", "1.5", "1.5"));
    }

    @Test
    public void test () throws NoSuchAlgorithmException, IOException {
//        System.out.println(ChainMakerUtils.bigInteger2DoubleString(ChainMakerUtils.doubleString2BigInteger("123")));
//        System.out.println(userInfoService.encodePwd("123456"));
        UserInfo userInfo = new UserInfo();
        userInfo.setEuid(11121L);
        userInfoService.registerUser(userInfo);
//        System.out.println(ChainMakerUtils.makeAddrFromCert(InitSystemClient.admin1));
//        Map<String, String> map = new HashMap();
//        map.put("a", "b");
//        map.put("c", "d");
//        System.out.println(JSONObject.toJSON(map));
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("userType", "client");
//        jsonObject.addProperty("certUsage", "tls-sign");
//        System.out.println(jsonObject);
    }


    public static class TEST1 extends DynamicStruct {
        public TEST1 (
                Uint256 tokenID,
                Uint256 lotId,
                Uint256 produceTime,
                Utf8String supplierName,
                Uint256 supplierTime,
                Utf8String producerName,
                Uint256 totalSum
        )
        {
            super(tokenID, lotId, produceTime, supplierName, supplierTime, producerName, totalSum);
        }
    }
}

