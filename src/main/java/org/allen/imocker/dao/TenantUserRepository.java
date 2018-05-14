package org.allen.imocker.dao;

import org.allen.imocker.entity.TenantUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TenantUserRepository extends JpaRepository<TenantUser, Long>, JpaSpecificationExecutor<TenantUser> {

    @EntityGraph(value = "TenantUser.tenant", type = EntityGraph.EntityGraphType.FETCH)
    TenantUser findOneByLoginNameAndLoginPwdAndTenant_AbbrName(String loginName,
                                                               String loginPwd,
                                                               String abbrName);



}
