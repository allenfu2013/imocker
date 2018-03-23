package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiConditionRepository extends JpaRepository<ApiCondition, Long> {

    void deleteApiConditionByApiInfo(ApiInfo apiInfo);
}
