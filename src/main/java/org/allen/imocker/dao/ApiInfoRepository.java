package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiInfoRepository extends JpaRepository<ApiInfo, Long>, JpaSpecificationExecutor<ApiInfo> {

    List<ApiInfo> findAllByApiName(String apiName);

    @EntityGraph(value = "apiInfo.condition", type = EntityGraph.EntityGraphType.FETCH)
    ApiInfo findOneByTenantAndShortApiNameAndMethod(Tenant tenant, String shortApiName, String method);

    @EntityGraph(value = "apiInfo.condition", type = EntityGraph.EntityGraphType.FETCH)
    ApiInfo findOneByShortApiNameAndMethodAndUserId(String shortApiName, String method, Long userId);

    @Query("select count(id) > 0 from ApiInfo where tenant.id = ?1 and shortApiName = ?2 and method = ?3")
    boolean existByShortApiNameAndMethod(Long tenantId, String shortApiName, String method);

    List<ApiInfo> findAllByUriVariableIsNotNull();

    @EntityGraph(value = "apiInfo.condition", type = EntityGraph.EntityGraphType.FETCH)
    ApiInfo findById(Long id);

    Page<ApiInfo> findAll(Specification<ApiInfo> specification, Pageable pageable);

}
