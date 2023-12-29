package cn.org.gry.chainmaker.base.dynamicarray;

import cn.org.gry.chainmaker.base.BaseDynamicArray;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/22 13:48
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */

@Getter
@Setter
public class Uint256DynamicArray extends BaseDynamicArray<Uint256> {
    private final List<BigInteger> values = new ArrayList<>();

    public Uint256DynamicArray(Uint256... values) {
        super(Uint256.class, values);
        for (Uint256 value : values) {
            this.values.add(value.getValue());
        }
    }

    public Uint256DynamicArray(List<Uint256> values) {
        super(Uint256.class, values);
        for (Uint256 value : values) {
            this.values.add(value.getValue());
        }
    }

    @JsonIgnore
    @Override
    public String getTypeAsString() {
        return super.getTypeAsString();
    }

    @JsonIgnore
    @Override
    public Class<Uint256> getComponentType() {
        return super.getComponentType();
    }

    @JsonIgnore
    @Override
    public List<Object> getNativeValueCopy() {
        return super.getNativeValueCopy();
    }

    @JsonIgnore
    @Override
    public List<Uint256> getValue() {
        return super.getValue();
    }
}
