package org.allen.imocker.dao;

import org.allen.imocker.entity.TenantUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantUserRepository extends JpaRepository<TenantUser, Long> {

    @EntityGraph(value = "TenantUser.tenant", type = EntityGraph.EntityGraphType.FETCH)
    TenantUser findOneByLoginNameAndLoginPwdAndTenant_AbbrName(String loginName,
                                                               String loginPwd,
                                                               String abbrName);



}
