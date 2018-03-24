package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiDoc;
import org.allen.imocker.entity.ApiHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiHeaderRepository extends JpaRepository<ApiHeader, Long> {

    void deleteApiHeadersByApiDoc(ApiDoc apiDoc);
}
