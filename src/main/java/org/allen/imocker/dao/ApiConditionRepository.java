package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiConditionRepository extends JpaRepository<ApiCondition, Long> {

    @Modifying
    @Query("delete from ApiCondition where apiInfo.id = ?1")
    void deleteApiConditionsByApiInfo(Long apiInfoId);

    List<ApiCondition> findAllByApiInfo(ApiInfo apiInfo);
}
