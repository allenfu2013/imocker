package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiConditionRepository extends JpaRepository<ApiCondition, Long> {

    void deleteApiConditionsByApiInfo(ApiInfo apiInfo);

    List<ApiCondition> findAllByApiInfo(ApiInfo apiInfo);
}
