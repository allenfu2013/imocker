package org.allen.imocker.dao;

import org.allen.imocker.entity.ApiCert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiCertRepository extends JpaRepository<ApiCert, Long> {
}
