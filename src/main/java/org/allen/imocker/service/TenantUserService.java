package org.allen.imocker.service;

import org.allen.imocker.controller.request.QueryTenantRequest;
import org.allen.imocker.dao.TenantUserRepository;
import org.allen.imocker.entity.TenantUser;
import org.allen.imocker.entity.type.ApplyStatus;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenantUserService {

    @Autowired
    private TenantUserRepository tenantUserRepository;

    public void createUser(TenantUser tenantUser) {
        tenantUser.setLoginPwd(DigestUtils.md5Hex(tenantUser.getLoginPwd()));
        tenantUserRepository.save(tenantUser);
    }

    public Page<TenantUser> pageQuery(final QueryTenantRequest request, PageRequest pageRequest) {
        return tenantUserRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();

            if (request.getApplyStatus() != null) {
                list.add(cb.equal(root.get("status").as(ApplyStatus.class), request.getApplyStatus()));
            }

            return cb.and(list.toArray(new Predicate[list.size()]));
        }, pageRequest);
    }

}
