package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiDoc;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApiDocRepository extends JpaRepository<ApiDoc, Long>, JpaSpecificationExecutor<ApiDoc> {

    List<ApiDoc> findAllByProject(String project);

    @EntityGraph(value = "apiDoc.header.parameter.responseBody.error", type = EntityGraph.EntityGraphType.LOAD)
    ApiDoc findOneById(Long id);
}
