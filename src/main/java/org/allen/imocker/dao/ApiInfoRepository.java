package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApiInfoRepository extends JpaRepository<ApiInfo, Long>, JpaSpecificationExecutor<ApiInfo> {

    List<ApiInfo> findAllByApiName(String apiName);

    List<ApiInfo> findAllByUriVariableIsNotNull();

    @EntityGraph(value = "apiInfo.condition", type = EntityGraph.EntityGraphType.FETCH)
    ApiInfo findById(Long id);

    Page<ApiInfo> findAll(Specification<ApiInfo> specification, Pageable pageable);

}
