package cn.org.gry.chainmaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/20 10:26
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
public class SdkConfigPoolConfiguration {
    @Bean
    public SdkConfigPool sdkConfigPool() throws Exception {
        return new SdkConfigPool(100);
    }
}
