package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiDoc;
import org.allen.imocker.entity.ApiParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiParameterRepository extends JpaRepository<ApiParameter, Long> {

    void deleteApiParametersByApiDoc(ApiDoc apiDoc);
}
