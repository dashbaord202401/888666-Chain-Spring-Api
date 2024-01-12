package cn.org.gry.chainmaker.domain.service;

import cn.org.gry.chainmaker.domain.entity.UserInfo;
import cn.org.gry.chainmaker.domain.enums.UserType;
import cn.org.gry.chainmaker.repository.UserInfoRepository;
import cn.org.gry.chainmaker.utils.Result;
import cn.org.gry.chainmaker.utils.TokenHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/4 14:40
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private Tool tool;

    @Autowired
    private CaService caService;

    public String getAddressByUid (Long uid) {
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        try {
            return userInfo.getAddress();
        } catch (Exception e) {
            throw new RuntimeException("用户不存在");
        }
    }

    public String getAddressByEuid (Long euid) {
        UserInfo userInfo = userInfoRepository.findByEuid(euid);
        try {
            return userInfo.getAddress();
        } catch (Exception e) {
            throw new RuntimeException("用户不存在");
        }
    }

    public UserInfo registerUser (UserInfo userInfo) {
        userInfo.setOrg(caService.getCaServiceOrg());
        userInfo.setPwd(encodePwd(userInfo.getPwd()));
        userInfo = userInfoRepository.save(userInfo);
        byte[][] val1;
        try {
             val1 = caService.genCert(userInfo);
        } catch (Exception e) {
            throw new RuntimeException("证书生成失败");
        }

        byte[] cert = val1[0];
        byte[] key = val1[1];

        userInfo.setTlsCert(cert);
        userInfo.setSignCert(cert);
        userInfo.setSignKey(key);
        userInfo.setTlsKey(key);

        userInfo = userInfoRepository.save(userInfo);
        TokenHolder.put("uid", userInfo.getUid().toString());

        userInfo.setAddress(tool.getAddress().getData().get("address").toString());
        return userInfoRepository.save(userInfo);
    }

    public Result getUid() {
        UserInfo userInfo = userInfoRepository.findByEuid(Long.valueOf(TokenHolder.get("euid")));
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", userInfo.getUid().toString());
            return Result.success("success", "", map);
        } catch (Exception e) {
            throw new RuntimeException("用户不存在");
        }
    }

    public String encodePwd (String pwd) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("获取加密算法失败");
        }
        md.update(pwd.getBytes());
        byte[] digest = md.digest();
        // Convert the byte array to a hexadecimal string
        StringBuilder result = new StringBuilder();
        for (byte b : digest) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }

    public Boolean verifyPwd (Long uid, String pwd) {
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        try {
            return userInfo.getPwd().equals(encodePwd(pwd));
        } catch (Exception e) {
            throw new RuntimeException("用户不存在");
        }
    }
}
