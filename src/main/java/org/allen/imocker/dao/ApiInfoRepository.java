package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApiInfoRepository extends JpaRepository<ApiInfo, Long>, JpaSpecificationExecutor<ApiInfo> {

    List<ApiInfo> findByTenantIdAndApiName(Long tenantId, String apiName);
}
