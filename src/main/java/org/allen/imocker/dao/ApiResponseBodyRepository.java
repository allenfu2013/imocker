package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiDoc;
import org.allen.imocker.entity.ApiResponseBody;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiResponseBodyRepository extends JpaRepository<ApiResponseBody, Long> {

    void deleteApiResponseBodiesByApiDoc(ApiDoc apiDoc);
}
