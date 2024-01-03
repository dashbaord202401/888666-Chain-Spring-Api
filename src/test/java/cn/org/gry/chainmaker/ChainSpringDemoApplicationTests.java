
package cn.org.gry.chainmaker;

import cn.org.gry.chainmaker.config.InitSystemClient;
import cn.org.gry.chainmaker.contract.*;
import cn.org.gry.chainmaker.domain.service.*;
import cn.org.gry.chainmaker.utils.ChainMakerUtils;
import cn.org.gry.chainmaker.utils.TokenHolder;
import org.chainmaker.sdk.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;

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


    @Test
    void contextLoads() {
        String Address_1 = "aa4ebe74154e3b0f63a2bd20290e2314f8728ee3";
        String Address_2 = "68db26564c267f14e13a6a2aad8f04d79513ee8b";
        String Address_3 = "d9973fef375a08fed8331d82899caed9486c8c31";
        String Address_4 = "3839bcfb4d575a565fae4ae5c50b52d5a616bf2b";

        TokenHolder.setToken("1");

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
        tradeManagement.RegisterProducer(Address_1, "YJH_P1");
        tradeManagement.RegisterUser(Address_3, "YJH_D3");
        tradeManagement.RegisterUser(Address_4, "YJH_W4");
        tradeManagement.RegisterUser(Address_1, "YJH_R1");
        tradeManagement.RegisterUser(Address_2, "YJH_C2");

        rm.mint("NACL", "123.123", "食盐");
        rm.mint("NACL", "321.321", "食盐");
        rm.mint("NACL", "456.4", "食盐");
        rm.mint("NACL", "100.1", "食盐");

        rm.transferFrom(Address_1, Address_2, BigInteger.valueOf(1), "Lot1");
        rm.transferFrom(Address_1, Address_2, BigInteger.valueOf(2), "Lot2");
        rm.transferFrom(Address_1, Address_2, BigInteger.valueOf(3), "Lot3");
    }

    @Test
    public void test () {
        System.out.println(ChainMakerUtils.bigInteger2DoubleString(ChainMakerUtils.doubleString2BigInteger("123")));
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

