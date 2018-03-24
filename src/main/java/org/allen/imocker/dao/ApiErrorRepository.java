package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiDoc;
import org.allen.imocker.entity.ApiError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiErrorRepository extends JpaRepository<ApiError, Long> {

    void deleteApiErrorsByApiDoc(ApiDoc apiDoc);
}
