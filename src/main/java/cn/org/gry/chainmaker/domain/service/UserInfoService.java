package cn.org.gry.chainmaker.domain.service;

import cn.org.gry.chainmaker.domain.entity.UserInfo;
import cn.org.gry.chainmaker.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public String getAddressByUid (Long uid) {
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        try {
            return userInfo.getAddress();
        } catch (Exception e) {
            throw new RuntimeException("用户不存在");
        }
    }

    public String encodePwd (String pwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
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
