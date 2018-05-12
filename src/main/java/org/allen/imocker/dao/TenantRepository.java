package org.allen.imocker.dao;

import org.allen.imocker.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Tenant findOneByAbbrName(String abbrName);

}
