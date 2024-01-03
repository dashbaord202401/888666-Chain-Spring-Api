package cn.org.gry.chainmaker.base;

import lombok.Getter;
import lombok.Setter;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;

import java.util.List;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/22 13:41
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class BaseDynamicArray<T extends Type> extends DynamicArray<T> {
    @SafeVarargs
    public BaseDynamicArray(Class<T> type, T... values) {
        super(type, values);
    }

    public BaseDynamicArray(Class<T> type, List<T> values) {
        super(type, values);
    }

}
