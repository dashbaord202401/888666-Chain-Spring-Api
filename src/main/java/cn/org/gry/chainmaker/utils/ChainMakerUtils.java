package cn.org.gry.chainmaker.utils;

import org.apache.commons.lang3.StringUtils;
import org.chainmaker.pb.config.ChainConfigOuterClass;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.utils.CryptoUtils;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/7 11:40
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ChainMakerUtils {
    // 从证书中获取地址
    public static Address makeAddrFromCert (User user) {
        try {
            return new Address(CryptoUtils.certToAddrStr(user.getCertificate(), ChainConfigOuterClass.AddrType.ETHEREUM));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    // 从公钥中获取地址
    public static Address makeAddrFromKey (User user) {
        return new Address(CryptoUtils.pkToAddrStr(user.getPublicKey(), ChainConfigOuterClass.AddrType.ETHEREUM, "SHA256"));
    }

    public static BigInteger doubleString2BigInteger(String d) {
        // 将字符串分割为整数部分和小数部分
        String[] split = d.split("\\.");
        // 如果没有小数部分
        if (split.length == 1) {
            // 直接乘以10的10次方
            return new BigInteger(split[0] + StringUtils.repeat("0", 10));
        } else {
            // 如果有小数部分
            // 整数部分乘以10的10次方，小数部分乘以10的9-小数长度次方
            return new BigInteger(split[0] + split[1] + StringUtils.repeat("0", 10 - split[1].length()));
        }
    }

    public static String bigInteger2DoubleString(BigInteger value) {
        if (value == null || value.toString().length() < 10) {
            return null;
        }
        // 获取value字符串的后十位
        String low = value.toString().substring(value.toString().length() - 10);
        // 获取value字符串的前面部分
        String high = value.toString().substring(0, value.toString().length() - 10);
        // 去除小数部分后面的0
        low = StringUtils.stripEnd(low, "0");
        return high + "." + low;
    }

    // 将类型转换为map
    public static Map<String, Object> convertTypeToMap (List<String> keys, List<Type> values) {
        // 将keys和values转换为map
         Map<String, Object> map = new HashMap<>();
         for (int i = 0; i < keys.size(); i++) {
             if (values.get(i) instanceof DynamicStruct) {
                 map.put(keys.get(i), values.get(i));
             } else {
                 if (values.get(i) instanceof DynamicArray) {
                     // 如果不为空
                     if (!((List<?>)values.get(i).getValue()).isEmpty()) {
                         // 如果元素是基础类型
                         if (!(((List<?>) values.get(i).getValue()).get(0) instanceof DynamicStruct)) {
                             // 新建一个list接收转换后的值
                             List<Object> list = new ArrayList<>();
                             for (Type type : (List<Type>)values.get(i).getValue()) {
                                 list.add(type.getValue());
                             }
                             map.put(keys.get(i), list);
                             continue;
                         }
                     }
                 }
                 map.put(keys.get(i), values.get(i).getValue());
             }
         }
         return map;
    }
}
