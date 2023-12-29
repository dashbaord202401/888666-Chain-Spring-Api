package cn.org.gry.chainmaker.subscribe;

import org.chainmaker.sdk.ChainClient;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/8 10:52
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
public class Subscribe extends BaseSubscribe{
    @Override
    public void setChainClient(ChainClient chainClient) {
        super.chainClient = chainClient;
    }
}
