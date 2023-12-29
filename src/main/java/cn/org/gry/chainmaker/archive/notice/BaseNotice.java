package cn.org.gry.chainmaker.archive.notice;

import org.chainmaker.sdk.archivecenter.Notice;
import org.chainmaker.sdk.archivecenter.ProcessMessage;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/8 16:17
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
public class BaseNotice implements Notice {
    @Override
    public void heightNotice(ProcessMessage processMessage) {
        System.out.println(processMessage.getMessage());
    }
}
