package cn.org.gry.chainmaker.domain.service;

import cn.org.gry.chainmaker.domain.entity.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/5 10:40
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
@Service
public class CaService {
    @Value("${ca.url}")
    private String caServiceUrl;

    @Value("${ca.username}")
    private String caServiceUsername;

    @Value("${ca.password}")
    private String caServicePassword;

    @Value("${ca.org}")
    private String caServiceOrg;

    @Value("${ca.country}")
    private String caServiceCountry;

    @Value("${ca.locality}")
    private String caServiceLocality;

    @Value("${ca.province}")
    private String caServiceProvince;

    // 生成证书
    public byte[][] genCert (UserInfo userInfo) throws IOException {
        String token = login();
        HttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(caServiceUrl + "/gencert");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("orgId", caServiceOrg);
        jsonObject.addProperty("userId", userInfo.getType() + userInfo.getEuid());
        jsonObject.addProperty("userType", "client");
        jsonObject.addProperty("certUsage", "tls-sign");
        jsonObject.addProperty("privateKeyPwd", "");
        jsonObject.addProperty("country", caServiceCountry);
        jsonObject.addProperty("locality", caServiceLocality);
        jsonObject.addProperty("province", caServiceProvince);
        jsonObject.addProperty("token", token);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));
        Map<String, Object> resultMap = (Map<String, Object>)new ObjectMapper().readValue(EntityUtils.toString(httpClient.execute(httpPost).getEntity()), Map.class).get("data");
        byte[][] result = new byte[2][];
        result[0] = resultMap.get("cert").toString().getBytes();
        result[1] = resultMap.get("privateKey").toString().getBytes();
        return result;
    }

    // 获取token
    public String login () throws IOException {
        // 创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httpPost = new HttpPost(caServiceUrl + "/login");
        String jsonLoginString = "{\n" +
                "    \"appId\": \"" + caServiceUsername + "\",\n" +
                "    \"appKey\": \"" + caServicePassword + "\"\n" +
                "}";
        httpPost.setEntity(new StringEntity(jsonLoginString));
        httpPost.setHeader("Content-type", "application/json");
        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        Map<String, Object> resultMap = new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), Map.class);
        return ((Map<String, Object>)resultMap.get("data")).get("accessToken").toString();
    }
}
