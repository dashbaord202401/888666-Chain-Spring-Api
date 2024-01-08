package cn.org.gry.chainmaker.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Type;

import java.util.List;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description 基础自定义结构体，所有自定义结构体都需要继承该类
 * @since 2023/12/21 20:08
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class BaseDynamicStruct extends DynamicStruct {

    public BaseDynamicStruct(Type... values) {
        super(values);
    }

    // 以下方法为了序列化时忽略
    @JsonIgnore
    @Override
    public List<Type> getValue() {
        return super.getValue();
    }

    @JsonIgnore
    @Override
    public String getTypeAsString() {
        return super.getTypeAsString();
    }

    @JsonIgnore
    @Override
    public Class<Type> getComponentType() {
        return super.getComponentType();
    }

    @JsonIgnore
    @Override
    public List<Object> getNativeValueCopy() {
        return super.getNativeValueCopy();
    }
}
