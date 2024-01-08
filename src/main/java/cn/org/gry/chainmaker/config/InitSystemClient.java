package cn.org.gry.chainmaker.config;

import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainManager;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.config.ChainClientConfig;
import org.chainmaker.sdk.config.NodeConfig;
import org.chainmaker.sdk.config.SdkConfig;
import org.chainmaker.sdk.utils.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitSystemClient {
    // 管理员1签名证书及密钥
    static final String ADMIN1_KEY_PATH = "src/main/resources/crypto-config/TestCMorg1/certs/user/SignKey.key";
    static final String ADMIN1_CERT_PATH = "src/main/resources/crypto-config/TestCMorg1/certs/user/SignCert.crt";
    // 管理员2签名证书及密钥
    static final String ADMIN2_KEY_PATH = "src/main/resources/crypto-config/TestCMorg2/certs/user/SignKey.key";
    static final String ADMIN2_CERT_PATH = "src/main/resources/crypto-config/TestCMorg2/certs/user/SignCert.crt";
    // 管理员3签名证书及密钥
    static final String ADMIN3_KEY_PATH = "src/main/resources/crypto-config/TestCMorg3/certs/user/SignKey.key";
    static final String ADMIN3_CERT_PATH = "src/main/resources/crypto-config/TestCMorg3/certs/user/SignCert.crt";
    // 管理员1所属组织名
    static final String ORG_ID1 = "TestCMorg1";
    // 管理员2所属组织名
    static final String ORG_ID2 = "TestCMorg2";
    // 管理员3所属组织名
    static final String ORG_ID3 = "TestCMorg3";
    public static User admin1;
    public static User admin2;
    public static User admin3;
    // 管理员1TSL证书及密钥
    static String ADMIN1_TLS_KEY_PATH = "src/main/resources/crypto-config/TestCMorg1/certs/user/TlsKey.key";
    static String ADMIN1_TLS_CERT_PATH = "src/main/resources/crypto-config/TestCMorg1/certs/user/TlsCert.crt";
    // 管理员2TSL证书及密钥
    static String ADMIN2_TLS_KEY_PATH = "src/main/resources/crypto-config/TestCMorg2/certs/user/TlsKey.key";
    static String ADMIN2_TLS_CERT_PATH = "src/main/resources/crypto-config/TestCMorg2/certs/user/TlsCert.crt";
    // 管理员3TSL证书及密钥
    static String ADMIN3_TLS_KEY_PATH = "src/main/resources/crypto-config/TestCMorg3/certs/user/TlsKey.key";
    static String ADMIN3_TLS_CERT_PATH = "src/main/resources/crypto-config/TestCMorg3/certs/user/TlsCert.crt";
    // 配置文件地址
    static String SDK_CONFIG = "sdk_config.yml";
    static ChainClient chainClient;
    static ChainManager chainManager;

    @Bean
    public static ChainClient InItSystemChainClient() throws Exception {
        // 加载配置文件成Yaml对象
        Yaml yaml = new Yaml();
        InputStream in = InitSystemClient.class.getClassLoader().getResourceAsStream(SDK_CONFIG);

        // 将Yaml对象转换未SDKConfig
        SdkConfig sdkConfig;
        sdkConfig = yaml.loadAs(in, SdkConfig.class);
        assert in != null;
        in.close();

        // 从SDKConfig中获取节点配置信息
        for (NodeConfig nodeConfig : sdkConfig.getChainClient().getNodes()) {
            List<byte[]> tlsCaCertList = new ArrayList<>();
            if (nodeConfig.getTrustRootPaths() != null) {
                for (String rootPath : nodeConfig.getTrustRootPaths()) {
                    List<String> filePathList = FileUtils.getFilesByPath(rootPath);
                    for (String filePath : filePathList) {
                        tlsCaCertList.add(FileUtils.getFileBytes(filePath));
                    }
                }
            }
            byte[][] tlsCaCerts = new byte[tlsCaCertList.size()][];
            tlsCaCertList.toArray(tlsCaCerts);
            nodeConfig.setTrustRootBytes(tlsCaCerts);
        }

        ChainClientConfig chainClientConfig = sdkConfig.getChainClient();
        chainClientConfig.setOrg_id(ORG_ID1);
        chainClientConfig.setUserKeyBytes(FileUtils.getResourceFileBytes(ADMIN1_TLS_KEY_PATH));
        chainClientConfig.setUserCrtBytes(FileUtils.getResourceFileBytes(ADMIN1_TLS_CERT_PATH));
        chainClientConfig.setUserSignKeyBytes(FileUtils.getResourceFileBytes(ADMIN1_KEY_PATH));
        chainClientConfig.setUserSignCrtBytes(FileUtils.getResourceFileBytes(ADMIN1_CERT_PATH));

        chainManager = ChainManager.getInstance();
        chainClient = chainManager.getChainClient(sdkConfig.getChainClient().getChainId());

        if (chainClient == null) {
            chainClient = chainManager.createChainClient(sdkConfig);
        }

        // 创建管理员1
        admin1 = new User(ORG_ID1, FileUtils.getResourceFileBytes(ADMIN1_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN1_CERT_PATH),
                FileUtils.getResourceFileBytes(ADMIN1_TLS_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN1_TLS_CERT_PATH), false);
        // 创建管理员2
        admin2 = new User(ORG_ID2, FileUtils.getResourceFileBytes(ADMIN2_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN2_CERT_PATH),
                FileUtils.getResourceFileBytes(ADMIN2_TLS_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN2_TLS_CERT_PATH), false);

        admin3 = new User(ORG_ID3, FileUtils.getResourceFileBytes(ADMIN3_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN3_CERT_PATH),
                FileUtils.getResourceFileBytes(ADMIN3_TLS_KEY_PATH),
                FileUtils.getResourceFileBytes(ADMIN3_TLS_CERT_PATH), false);

        return chainClient;
    }
}
