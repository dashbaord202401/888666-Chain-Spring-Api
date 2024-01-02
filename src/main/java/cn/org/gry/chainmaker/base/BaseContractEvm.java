package cn.org.gry.chainmaker.base;

import cn.org.gry.chainmaker.config.SdkConfigPool;
import cn.org.gry.chainmaker.utils.ChainMakerUtils;
import cn.org.gry.chainmaker.utils.Result;
import cn.org.gry.chainmaker.utils.TokenHolder;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.chainmaker.pb.common.ContractOuterClass;
import org.chainmaker.pb.common.Request;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.*;
import org.chainmaker.sdk.config.ChainClientConfig;
import org.chainmaker.sdk.config.SdkConfig;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.chainmaker.sdk.utils.FileUtils;
import org.chainmaker.sdk.utils.SdkUtils;
import org.chainmaker.sdk.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/6 13:37
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */

@Component
public abstract class BaseContractEvm {
    // 链管理类
    private static final ChainManager chainManager = ChainManager.getInstance();
    // 合约参数名
    private static final String CONTRACT_ARGS_EVM_PARAM = "data";
    // 超时时间
    private static final Long rpcCallTimeout = 10000L;
    // 同步结果超时时间
    private static final Long syncResultTimeout = 10000L;
    // 合约名称
    protected String contractName;
    // 合约文件路径
    protected String EVM_CONTRACT_FILE_PATH = "";

    // 配置类池
    @Autowired
    private SdkConfigPool sdkConfigPool;

    protected String version = "_1.73";

    // 通过用户id获取链客户端
    public ChainClient getChainClient () throws Exception {
        String user = TokenHolder.getToken();
        String USER_TLS_KEY_PATH = "src/main/resources/crypto-config/TestCMorg" + user + "/certs/user/TlsKey.key";
        String USER_TLS_CERT_PATH = "src/main/resources/crypto-config/TestCMorg" + user + "/certs/user/TlsCert.crt";
        String USER_KEY_PATH = "src/main/resources/crypto-config/TestCMorg" + user + "/certs/user/SignKey.key";
        String USER_CERT_PATH = "src/main/resources/crypto-config/TestCMorg" + user + "/certs/user/SignCert.crt";
        String ORG_ID = "TestCMorg" + user;

        // 获取配置类
        SdkConfig sdkConfig = sdkConfigPool.acquire();

        // 获取链配置类
        ChainClientConfig chainClientConfig = sdkConfig.getChainClient();
        // 设置链配置类属性，组织id，用户证书，用户签名证书，用户签名私钥
        chainClientConfig.setOrg_id(ORG_ID);
        chainClientConfig.setUserKeyBytes(FileUtils.getResourceFileBytes(USER_TLS_KEY_PATH));
        chainClientConfig.setUserCrtBytes(FileUtils.getResourceFileBytes(USER_TLS_CERT_PATH));
        chainClientConfig.setUserSignKeyBytes(FileUtils.getResourceFileBytes(USER_KEY_PATH));
        chainClientConfig.setUserSignCrtBytes(FileUtils.getResourceFileBytes(USER_CERT_PATH));
        sdkConfig.setChain_client(chainClientConfig);

        // 获取链客户端
        try {
            return chainManager.createChainClient(sdkConfig);
        } finally {
            // 释放配置类
            sdkConfigPool.release(sdkConfig);
        }
    }

    // 创建合约
    public ContractOuterClass.Contract createEvmContract (User user, User[] users) {
        // 获取链客户端
        ChainClient chainClient = null;
        try {
            chainClient = getChainClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 构造合约创建参数
        Function function = new Function("", Collections.singletonList(ChainMakerUtils.makeAddrFromCert(user)),
                Collections.emptyList());
        // 编码合约创建参数
        String methodDataStr = FunctionEncoder.encode(function);
        Map<String, byte[]> paramMap = new HashMap<>();
        paramMap.put(CONTRACT_ARGS_EVM_PARAM, methodDataStr.substring(10).getBytes());

        // 发送合约创建请求
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            byte[] byteCode = FileUtils.getResourceFileBytes(EVM_CONTRACT_FILE_PATH);
            // 1. create payload
            Request.Payload payload = chainClient.createContractCreatePayload(Utils.calcContractName(contractName),
                    "1.0.0", byteCode,
                    ContractOuterClass.RuntimeType.EVM, paramMap);
            //2. create payloads with endorsement
            Request.EndorsementEntry[] endorsementEntries = SdkUtils.getEndorsers(
                    payload, users);
            // 3. send request
            responseInfo = chainClient.sendContractManageRequest(
                    payload, endorsementEntries, rpcCallTimeout, syncResultTimeout);
            System.out.println(responseInfo);
            if (responseInfo.getCode() == ResultOuterClass.TxStatusCode.SUCCESS) {
                ContractOuterClass.Contract contract = ContractOuterClass.Contract.newBuilder().mergeFrom(responseInfo.getContractResult().getResult().toByteArray()).build();
                String jsonStr = JsonFormat.printer().print(contract);
                System.out.println(jsonStr);
                return contract;
            }
        } catch (SdkException | InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // 获取合约信息
    public ContractOuterClass.Contract getContractInfoByName (){
        // 获取链客户端
        ChainClient chainClient = null;
        try {
            chainClient = getChainClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 发送合约信息获取请求
        try {
            return chainClient.getContractInfo(Utils.calcContractName(contractName), rpcCallTimeout);
        } catch (ChainMakerCryptoSuiteException | ChainClientException e) {
            throw new RuntimeException(e);
        }
    }

    // 执行合约
    public Result invokeContract(String method, List<Type> inputParameters, List<TypeReference<?>> outputType, List<String> outputName) {
        // 获取链客户端
        ChainClient chainClient = null;
        try {
            chainClient = getChainClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 编码合约执行参数
        ResultOuterClass.TxResponse responseInfo = null;
        Map<String, byte[]> param = new HashMap<>();
        Function function = new Function(method, inputParameters, outputType);
        String methodDataStr = FunctionEncoder.encode(function);
        method = methodDataStr.substring(0, 10);
        param.put(CONTRACT_ARGS_EVM_PARAM, methodDataStr.getBytes());
        // 发送合约执行请求
        try {
            responseInfo = chainClient.invokeContract(Utils.calcContractName(contractName), method,
                    null, param,rpcCallTimeout, syncResultTimeout);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("执行合约结果：");
        System.out.println(responseInfo);

        // 将字节数组转化为十六进制字符串
        String hexString = null;
        if (responseInfo != null) {
            hexString = Hex.toHexString(responseInfo.getContractResult().getResult().toByteArray());
        }

        Map<String, Object> data = new HashMap<>();
        // 将十六进制字符串解码成对应的返回类型
        if (!StringUtils.isBlank(hexString)) {
            List<Type> values = FunctionReturnDecoder.decode(hexString, function.getOutputParameters());
            // 将返回类型转化为Map
            data = ChainMakerUtils.convertTypeToMap(outputName, values);
        }

        Result result = null;
        if (responseInfo != null) {
            result = Result.success(responseInfo.getMessage(), responseInfo.getTxId(), data);
        }

        return result;
    }
}
