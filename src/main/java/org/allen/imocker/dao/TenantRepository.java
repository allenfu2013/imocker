package org.allen.imocker.dao;

import org.allen.imocker.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TenantRepository extends JpaRepository<Tenant, Long>, JpaSpecificationExecutor<Tenant> {

    Tenant findOneByAbbrName(String abbrName);

    Tenant findOneByEmail(String email);

}
