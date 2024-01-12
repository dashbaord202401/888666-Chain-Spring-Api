package cn.org.gry.chainmaker.repository;

import cn.org.gry.chainmaker.domain.entity.RawMaterialInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2024/1/11 16:54
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterialInfo, Long> {
    @Override
    <S extends RawMaterialInfo> S save(S entity);

    RawMaterialInfo findByTokenURI(Long tokenURI);
}
