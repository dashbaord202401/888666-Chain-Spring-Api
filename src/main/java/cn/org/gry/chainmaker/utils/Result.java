package cn.org.gry.chainmaker.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/8 9:59
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
@ToString
public class Result<T> {
    // 返回码
    private Integer code;
    // 交易id（只要发起就一定有）
    private String txId;
    // 返回信息
    private String message;
    // 返回数据
    private Map<String, T> data;

    private Result(Integer code, String message, String txId, Map<String, T> data) {
        this.code = code;
        this.message = message;
        this.txId = txId;
        this.data = data;
    }

    public static <T> Result success(String message, String txId, Map<String, T> data) {
        return new Result(ResultCode.SUCCESS, message, txId, data);
    }

    public static <T> Result fail(String message, String txId, Map<String, T> data) {
        return new Result(ResultCode.FAIL, message, txId, data);
    }
}
